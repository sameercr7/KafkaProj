// JavaScript for Setting the Table Data Using Financial Year
document.addEventListener('DOMContentLoaded', () => {

    const selectElement = document.getElementById('financial-year-select');

    // Function to update the table with review applications based on selected financial year
    function updateTable(finYear) {
        if (!finYear) {
            console.error('Financial year is not selected.');
            return;
        }

        const status = 'ISSUED';

        fetch(`/data/agencyApplicationsByStatus?financialYear=${finYear}&status=${status}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(response => {
                const data = response.data;

                // Access the agency table data
                const agencyTableData = data.agencyTableData;
                const tableBody = document.getElementById('tableBody');
                tableBody.innerHTML = ''; // Clear existing rows

                // Define a mapping from English statuses to Hindi
                const statusMapping = {
                    'APPROVED': 'अनुमत',
                    'PENDING': 'लंबित',
                    'REJECTED': 'अस्वीकृत',
                    'IN_PROGRESS': 'इन प्रोग्रेस'
                    // Add more mappings as needed
                };

                agencyTableData.forEach(item => {

                    const applicationId = item.spc_id || '';  // Assuming application_id is the correct field

                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${item.applied_for || 'N/A'}</td>
                        <td>${item.status || 'N/A'}</td>
                         <td>${item.date || 'N/A'}</td>
                      
                     
                        <td>
                            <a href="/noc/flowcharttable?id=${item.id}" >
                                <button class="btn btn-light">
                                    <span> <img class="img card-logo" src="/img/flowchartTable.png" alt="table"></span>
                                </button>
                            </a>
                        
                        </td>
                       
                      <td>
                            <button class="btn btn-light" onclick="fileView('${applicationId}')">
                                <span><img class="img card-logo" src="/img/pdf.png" alt="pdf"></span>
                            </button>
                       </td>
                    `;
                    tableBody.appendChild(row);
                });
            })
            .catch(error => console.error('Error fetching table data:', error));
    }

    // Event listener to update the table when the dropdown value changes
    selectElement.addEventListener('change', () => {
        const selectedFinYear = selectElement.value;
        updateTable(selectedFinYear);
    });

    // Initialize the table with the current financial year on page load
    const initialFinYear = selectElement.value;
    if (initialFinYear) {
        updateTable(initialFinYear);
    }
});


async function commonfetch(url) {
    try {
        const response = await fetch(url);
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const data = await response.json();
        console.log("Fetched Data:", data);
        return data;
    } catch (error) {
        console.error('Error fetching data:', error);
    }
}

// Function to set the PDF source and show the modal
let fileViewModal;
let formSubmitListenerAdded = false; // Flag to track if form submit listener is added

async function fileView(id) {
    const pdfReader = document.getElementById("viewFile");
    const role = document.getElementById('role').value;
    const username = document.getElementById('username').value;
    const url = '/data/fetchUserIdFromUserSpring/' + username;

    try {
        const data = await commonfetch(url);
        console.log("User Spring Data:", data.data.userId);

        const urlForAgency = '/data/fetchUserIdFromUserSpring/' + data.data.userId;
        const dataForAgency = await commonfetch(urlForAgency);
        console.log("Agency Data:", dataForAgency.data.agencyName);

        // Set agency name value in the form
        const agencyNameField = document.getElementById('agencyName');
        agencyNameField.value = dataForAgency.data.agencyName;

        // Show the modal
        fileViewModal = new bootstrap.Modal(document.getElementById('fileViewModal'));
        fileViewModal.show();

        // Store the id globally for PDF preview
        lastId = id;

        // Add the form submit listener if not already added
        if (!formSubmitListenerAdded) {
            document.getElementById('fileViewForm').onsubmit = async function (event) {
                event.preventDefault();

                // Get form data
                const photoUpload = document.getElementById('photoUpload').files[0];
                const agencyName = document.getElementById('agencyName').value;
                const feedback = document.getElementById('feedback').value;

                // Create FormData object
                const formData = new FormData();
                formData.append('uploadImage', photoUpload); // 'uploadImage' should match your backend field
                formData.append('agencyName', agencyName);
                formData.append('description', feedback);

                // Send POST request to the server
                try {
                    const response = await fetch('/data/savingfeedback', {
                        method: 'POST',
                        body: formData
                    });

                    const responseData = await response.json();

                    if (response.ok) {
                        console.log('Feedback saved successfully:', responseData);
                        fileViewModal.hide();  // Close the modal after successful submission
                    } else {
                        console.error('Error saving feedback:', responseData);
                    }
                } catch (error) {
                    console.error('Error submitting form:', error);
                }
            };
            formSubmitListenerAdded = true; // Set the flag to true after adding the listener
        }

    } catch (error) {
        console.error('Error fetching user or agency data:', error);
    }
}

// Add the event listener for the modal close event once
document.getElementById('fileViewModal').addEventListener('hidden.bs.modal', function () {
    // Ensure previous listeners are removed to avoid multiple attachments
    document.getElementById('fileViewModal').removeEventListener('hidden.bs.modal', this);

    var adobeDCView = new AdobeDC.View({
        clientId: "cf31889a110540978179b9134b5dcb81", // Replace with your Adobe Client ID
    });

    adobeDCView.previewFile({
        content: {
            location: { url: "/data/viewIndividualCertificate/" + lastId }
        },
        metaData: {
            fileName: "Selected Document.pdf"
        }
    }, {
        embedMode: "LIGHT_BOX"
    });
});




// Initialize Adobe DC View
// var adobeDCView = new AdobeDC.View({
//     clientId: "cf31889a110540978179b9134b5dcb81", // Replace with your Adobe Client ID
// });
//
// adobeDCView.previewFile({
//     content: {
//         location: {url: "/data/viewIndividualCertificate/" + id}
//     },
//     metaData: {
//         fileName: "Selected Document.pdf"
//     }
// }, {
//     embedMode: "LIGHT_BOX"
// });


function openPdfModal(applicationId) {

    // Construct the URL using the application ID
    const pdfUrl = `/data/fetchCertificateBySpecialId/${applicationId}`;

    // Set the src of the embed element to the PDF URL
    document.getElementById('viewFile').src = pdfUrl;

    // Open the modal
    var myModal = new bootstrap.Modal(document.getElementById('pdfView'));
    myModal.show();
}


function removePdfSource() {
    // Clear the src attribute of the embed element when modal is closed
    document.getElementById('viewFile').src = '';
}

