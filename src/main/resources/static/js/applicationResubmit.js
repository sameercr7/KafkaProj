
// JavaScript for Setting the Table Data Using Financial Year
document.addEventListener('DOMContentLoaded', () => {

    const selectElement = document.getElementById('financial-year-select');

    // Function to update the table with review applications based on selected financial year
// Function to update the table with review applications based on selected financial year
    function updateTable(finYear) {
        if (!finYear) {
            console.error('Financial year is not selected.');
            return;
        }

        const status = 'RESUBMITTED';

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
                if (data && Array.isArray(data.agencyTableData)) {
                    const agencyTableData = data.agencyTableData;
                    console.log("Agency Table Data: ", agencyTableData);

                    const tableBody = document.getElementById('tableBody');
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
                        console.log("Checking Item: ", item);

                        const row = document.createElement('tr');

                        // Map the status to Hindi if exists in the mapping
                        const translatedStatus = statusMapping[item.status] || item.status || 'N/A';

                        row.innerHTML = `
                        <td>${item.agency_name || 'N/A'}</td>
                        <td>${item.applied_for || 'N/A'}</td>
                        <td>${item.date || 'N/A'}</td>
                        <td>${translatedStatus}</td>
                        <td><button class="btn btn-light"><span><img class="img card-logo" src="/img/pdf.png" alt="pdf"></span></button></td>
                    `;
                        tableBody.appendChild(row);
                    });
                } else {
                    console.error("No valid agency table data found");
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
