let id=""
// For Splitting the array In Two
function toSplitPdf(applicationFullData) {
    let firstList = [];
    let secondList = [];
    let midPoint = -1;

    // Find the midpoint where data_field ends with '.pdf' and check_list_id is null
    for (let i = 0; i < applicationFullData.length; i++) {
        if (applicationFullData[i].data_field.endsWith('.pdf') &&
            applicationFullData[i].check_list_id === null) {
            midPoint = i;
            break;
        }
    }

    //  midpoint mil gaya , split the data into two lists
    if (midPoint !== -1) {
        firstList = applicationFullData.slice(0, midPoint);
        secondList = applicationFullData.slice(midPoint);
        // so that always It has the latest up like stack
        secondList.reverse();
    } else {
        // Just for edge case
        firstList = applicationFullData;
    }

    return {firstList, secondList};
}

// for Populating data in Table DOCUMENTS & INFORMATION PROVIDED BY AGENCY
function toPopulateDataInDocByAgency(firstList) {
    // Get the table body element
    const tableBody = document.querySelector('#dynamicTable tbody');
    // Clear existing rows (if any)
    tableBody.innerHTML = '';

    console.log("item View Application",firstList);

    // Loop through the data and create table rows
    firstList.forEach((item, index) => {
        // Create a new row


        const row = document.createElement('tr');

        // Create cells for S.No, Document Description, and Input
        const cellSNo = document.createElement('td');
        cellSNo.textContent = item.association_id || '';

        const cellDescription = document.createElement('td');
        cellDescription.textContent = item.description || '';

        // Function to set the PDF source and show the modal
        function fileView(id) {
            const pdfReader = document.getElementById("viewFile");

            // Set the source of the PDF viewer
            // pdfReader.src = "/data/viewIndividualDocument/" + id;
            var adobeDCView = new AdobeDC.View({clientId: "cf31889a110540978179b9134b5dcb81"});
            adobeDCView.previewFile({
                content:{location: {url: "/data/viewIndividualDocument/" + id}},
                metaData:{fileName: "Selected Document.pdf"}
            }, {embedMode: "LIGHT_BOX"});
        }


        // Create a table cell for the item
        const cellInput = document.createElement('td');

        // Check if the data_field is a PDF file
        if (item.data_field && item.data_field.endsWith('.pdf')) {
            // Create a button to view the PDF
            const viewPdfButton = document.createElement('button');
            viewPdfButton.textContent = 'View PDF'; // Set button text
            viewPdfButton.type = 'button';
            viewPdfButton.classList.add('btn', 'btn-primary');

            viewPdfButton.setAttribute('data-bs-toggle', 'modal');
            viewPdfButton.setAttribute('data-bs-target', '#example'); // Target your specific modal

            // Add an event listener to call the fileView function and open the modal
            viewPdfButton.addEventListener('click', () => {
                // Call the fileView function with the current row's id
                fileView(item.cd_id);
            });

            // Append the button to the cell
            cellInput.appendChild(viewPdfButton);
        } else {
            // If it's not a PDF, just display the data
            cellInput.textContent = item.data_field || '';
        }


        // Append the cell to the row
        row.appendChild(cellInput);


        // Append cells to the row
        row.appendChild(cellSNo);
        row.appendChild(cellDescription);
        row.appendChild(cellInput);

        // Append the row to the table body
        tableBody.appendChild(row);
    });
}


// for Populating data in Table DOCUMENT/LETTER ATTACHED BY DEPARTMENT
function toPopulateDataInDocByDepartment(secondList) {
    // dynamicTableForDocument
    const tableBodyForAttachedDocuments = document.querySelector('#dynamicTableForDocument tbody');

    tableBodyForAttachedDocuments.innerHTML = '';

    // Loop through the data and create table rows
    secondList.forEach((item, index) => {
        // Create a new row
        console.log("item item item item",item);
        document.getElementsByClassName("heading")[0].innerText="Submitted NOC Application Special ID :- "+ item.spc_id;
        const row = document.createElement('tr');


        // Create cells for S.No, Document Description, and Input
        const cellSNo = document.createElement('td');
        cellSNo.textContent = index+1;

        // Create cells for S.No, Document Description, and Input
        const cellshiddenId = document.createElement('td');
        cellshiddenId.textContent = item.association_id || '';
        cellshiddenId.style.display = 'none'; // Hide the cell

        const cellDescription = document.createElement('td');
        cellDescription.textContent = item.description || '';

        // Function to set the PDF source and show the modal
        function fileView(id) {
            const pdfReader = document.getElementById("viewFile");

            // Set the source of the PDF viewer
            // pdfReader.src = "/data/viewIndividualDocument/" + id;
            var adobeDCView = new AdobeDC.View({clientId: "cf31889a110540978179b9134b5dcb81"});
            adobeDCView.previewFile({
                content:{location: {url: "/data/viewIndividualDocument/" + id}},
                metaData:{fileName: "Selected Document.pdf"}
            }, {embedMode: "LIGHT_BOX"});
        }

        // Create a table cell for the item
        const cellInput = document.createElement('td');

        // Check if the data_field is a PDF file
        if (item.data_field && item.data_field.endsWith('.pdf')) {
            // Create a button to view the PDF
            const viewPdfButton = document.createElement('button');
            viewPdfButton.textContent = 'View PDF'; // Set button text
            viewPdfButton.type = 'button';
            viewPdfButton.classList.add('btn', 'btn-primary');
            viewPdfButton.setAttribute('data-bs-toggle', 'modal');
            viewPdfButton.setAttribute('data-bs-target', '#example'); // Target your specific modal

            // Add an event listener to call the fileView function and open the modal
            viewPdfButton.addEventListener('click', () => {
                // Call the fileView function with the current row's id
                fileView(item.cd_id);

            });
            // Append the button to the cell
            cellInput.appendChild(viewPdfButton);
        } else {
            // If it's not a PDF, just display the data
            cellInput.textContent = item.data_field || '';
        }


        // Append the cell to the row
        row.appendChild(cellInput);


        // Append cells to the row
        row.appendChild(cellSNo);
        row.appendChild(cellshiddenId);
        row.appendChild(cellDescription);
        row.appendChild(cellInput);

        // Append the row to the table body
        tableBodyForAttachedDocuments.appendChild(row);
    });


}

document.addEventListener('DOMContentLoaded', () => {
    function updateViewApplicationsData() {
        const applicationId = document.getElementById("applicationId").value;

        if (applicationId) {
            fetch(`/data/fetchApplicationDataById?id=${applicationId}`)
                .then(response => response.json())
                .then(data => {
                    if (data.flag === 'Success') {
                        const applicationData = data.data.applicationData[0];


                        console.log("All  fetched Data", data)
                        console.log("applicationData", applicationData)

                        // Fill form fields
                        document.getElementById('nocType').value = applicationData.applied_for || '';
                        document.getElementById('district').value = applicationData.district || '';
                        document.getElementById('canal').value = applicationData.canal || '';
                        document.getElementById('ee').value = applicationData.ee || '';
                        document.getElementById('superintendentEngineer').value = applicationData.se || '';
                        document.getElementById('chiefEngineer2').value = applicationData.ce2 || '';
                        document.getElementById('chiefEngineer1').value = applicationData.ce1 || '';

                        // Populate table with all data
                        const applicationFullData = data.data.applicationData;

                        console.log("applicationFullData", applicationFullData)

                        // Split the data into two lists
                        const {firstList, secondList} = toSplitPdf(applicationFullData);


                        console.log("firstList", firstList)
                        console.log("secondList", secondList)


                        toPopulateDataInDocByAgency(firstList);

                        toPopulateDataInDocByDepartment(secondList);


                    } else {
                        console.error('Failed to fetch application data:', data.message);
                    }
                })
                .catch(error => console.error('Error fetching data:', error));
        } else {
            console.error('Application ID is missing');
        }

    }

    // Initial call to populate the form
    updateViewApplicationsData();

});

// Function to remove the PDF source when the modal is closed
function removePdfSource() {
    const pdfReader = document.getElementById("viewFile");

    // Clear the PDF source
    pdfReader.src = "";
}
 id = document.getElementById("applicationId").value;


function viewCombinedFile(){
    // Ensure the ID for combining the PDF is available
    if (id) {
        console.log("idForCombinePDFView", id); // Log the ID for debugging
        viewCombinedPdf(id); // Call the function to view the combined PDF
    } else {
        alert('Special ID not found.');
    }
}




console.log("applicationId",id);
// combinePDFView
function viewCombinedPdf(id) {
    const adobeDCView = new AdobeDC.View({clientId: "cf31889a110540978179b9134b5dcb81"});
    adobeDCView.previewFile({

        content: { location: { url:   "/data/viewCombinedDocument/" + id}},
        metaData: { fileName: "Combined Document.pdf" }
    }, { embedMode: "LIGHT_BOX" });
}
