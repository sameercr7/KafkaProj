
// JavaScript for Setting the Table Data Using Financial Year
document.addEventListener('DOMContentLoaded', () => {

    const selectElement = document.getElementById('financial-year-select');

    // Function to update the table with review applications based on selected financial year
    function updateTable(finYear) {
        if (!finYear) {
            console.error('Financial year is not selected.');
            return;
        }

        const status = 'REJECTED_APPLICATION_CLOSED';

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
                    'REJECTED_APPLICATION_CLOSED': 'अस्वीकृत',
                    'IN_PROGRESS': 'इन प्रोग्रेस'
                    // Add more mappings as needed
                };

                agencyTableData.forEach(item => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${item.applied_for || 'N/A'}</td>
                      <td>${statusMapping[item.status] || 'N/A'}</td>
                        <td>${item.to_user || 'N/A'}</td>
                        <td>${item.date || 'N/A'}</td>
                        <td>
                            <a href="/noc/flowcharttable?id=${item.id}" >
                                <button class="btn btn-light">
                                    <span> <img class="img card-logo" src="/img/flowchartTable.png" alt="table"></span>
                                </button>
                            </a>
                        
                        </td>
                       
                        <td> <a href="/noc/viewOnlyApplication?id=${item.id}" >
                                    <button class="btn btn-light"><span> <img class="img card-logo" src="/img/pdf.png" alt="rejected"></span></button>
                                    </a>
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
