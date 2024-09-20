console.log("Application Track Table");

// Event listener for form submission
document.addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent the default form submission behavior

    const specialId = document.getElementById('spc_Id').value;
    console.log("specialId:", specialId);

    // Perform the first fetch, passing the specialId
    fetch(`/data/findAplicationId/${specialId}`)
        .then(response => response.json())
        .then(data => {
            console.log("Data from first fetch:", data);
            const id = data.length > 0 ? data[0].id : null; // Check if data is not empty and extract 'id'
            console.log("ID from first fetch:", id);

            if (id) {
                functionToFetchAllLettersFromAppCurrid(id); // Pass the id to the next function
            } else {
                console.error('No ID found in the response');
            }
        })
        .catch(error => console.error('Error in the first fetch:', error));
});

// Function to populate the table
function populateTable(data) {
    const table = document.getElementById('trackingLettersForAgency');
    const heading = document.getElementById('trackingLettersForAgencyHeading');
    const tbody = table.querySelector('tbody');

    // Show the table and heading
    table.hidden = false;
    heading.hidden = false;

    // Clear existing rows
    tbody.innerHTML = '';
    console.log("list of letter  pdf path",data)
    // Populate table rows
    data.forEach((item, index) => {

        const row = document.createElement('tr');

        // S.No cell
        const serialCell = document.createElement('td');
        serialCell.textContent = index + 1;
        serialCell.style.textAlign = 'center'; // Center the text
        row.appendChild(serialCell);

        // Hidden Id cell
        const idCell = document.createElement('td');
        idCell.textContent = item.id;
        idCell.style.display = 'none'; // Hide the cell
        row.appendChild(idCell);


        const descriptionCell = document.createElement('td');
        descriptionCell.textContent = item.description;
        descriptionCell.style.textAlign = 'center'; // Center the text
        row.appendChild(descriptionCell);

        // Action cell with the "View" button
        const actionCell = document.createElement('td');
        const viewButton = document.createElement('a');
        viewButton.className = 'btn btn-primary text-decoration-none';
        viewButton.textContent = 'View'; // Button text


        actionCell.appendChild(viewButton);
        actionCell.style.textAlign = 'center'; // Center the text
        row.appendChild(actionCell);
        // Add an event listener to call the fileView function and open the modal
        viewButton.addEventListener('click', () => {
            // Call the fileView function with the current row's id
            fileView(item.id);
        });

        // Append the row to the table body
        tbody.appendChild(row);
    });
}

// Function to set the PDF source and show the modal
function fileView(id) {
console.log("id is ",id)
    const pdfReader = document.getElementById("viewFile");

    // Set the source of the PDF viewer
    // pdfReader.src = "/data/viewIndividualDocument/" + id;
    var adobeDCView = new AdobeDC.View({clientId: "cf31889a110540978179b9134b5dcb81"});
    adobeDCView.previewFile({
        content:{location: {url: "/data/viewIndividualLetters/" + id}},
        metaData:{fileName: "Selected Document.pdf"}
    }, {embedMode: "LIGHT_BOX"});
}



// Function to fetch current status ID
function functionToFetchAllLettersFromAppCurrid(id) {
    // Perform the second fetch using the id obtained from the first fetch
    fetch(`/data/findAllLettersFromAppCurrid/${id}`)
        .then(response => response.json())
        .then(data => {
            console.log("Data from functionToFetchAllLettersFromAppCurrid:", data);
            // Call the function to populate the table with data
            populateTable(data);
        })
        .catch(error => console.error('Error in functionToFetchAppCurrid:', error));
}

// Placeholder function for updating logs data (you need to implement this)
function updateLogsData(userId) {
    console.log("Updating logs data with user ID:", userId);
    // Your implementation here
}
