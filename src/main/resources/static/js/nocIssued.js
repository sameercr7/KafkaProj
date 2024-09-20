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

        fetch(`/data/applicationStatus?financialYear=${finYear}&status=${status}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(response => {
                const data = response.data;

                // Check if data exists and contains agencyTableData
                if (data && Array.isArray(data.agencyTableData) && data.agencyTableData.length > 0) {
                    const agencyTableData = data.agencyTableData;
                    console.log("Agency Table Data: ", agencyTableData);

                    const tableBody = document.getElementById('tableBody');

                    if (!tableBody) {
                        console.error('Table body not found in the DOM.');
                        return;
                    }

                    tableBody.innerHTML = ''; // Clear existing rows

                    // Define a mapping from English statuses to Hindi
                    const statusMapping = {
                        'APPROVED': 'अनुमत',
                        'PENDING': 'लंबित',
                        'REJECTED': 'अस्वीकृत',
                        'IN_PROGRESS': 'इन प्रोग्रेस',
                        'Rejected-ReSubmit': 'अस्वीकृत-दुबारा सबमिट करें'
                        // Add more mappings as needed
                    };

                    // Iterate through each item in agencyTableData and add rows
                    agencyTableData.forEach(item => {
                        console.log("Data ", item);

                        // Handle undefined or missing fields
                        const agencyName = item.agency_name || 'N/A';
                        const date = item.date || 'N/A';
                        const status = item.status || 'N/A';
                        const applicationId = item.spc_id || '';  // Assuming application_id is the correct field

                        const row = document.createElement('tr');

                        // Create the row with the table data
                        row.innerHTML = `
        <td>${agencyName}</td>
        <td>${date}</td>
        <td>${status}</td>
        <td>
            <button class="btn btn-light" onclick="openPdfModal('${applicationId}')">
                <span><img class="img card-logo" src="/img/pdf.png" alt="pdf"></span>
            </button>
        </td>
    `;

                        tableBody.appendChild(row);
                    });

                } else {
                    console.error("No valid agency table data found or the data array is empty.");
                    const tableBody = document.getElementById('tableBody');
                    if (tableBody) tableBody.innerHTML = '<tr><td colspan="4">No data available</td></tr>';
                }
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



