document.addEventListener('DOMContentLoaded', () => {

    function updateReviewApplications() {
        fetch(`/data/fetchReviewApplications?financialYear=${finYear}`)
            .then(response => response.json())
            .then(response => {
                if (response.flag === 'Success') {
                    const data = response.data.applicationData;
                    const tableBody = document.getElementById('tableBody');

                    // Clear existing rows
                    tableBody.innerHTML = '';

                    // Initialize the Serial Number counter
                    let serialNumber = 1;

                    // Populate table with fetched data
                    data.forEach(item => {
                        const row = document.createElement('tr');

                        // Create table cells and append them to the row
                        const cell1 = document.createElement('td');
                        cell1.textContent = serialNumber || 'N/A';
                        row.appendChild(cell1);

                        const cell2 = document.createElement('td');
                        cell2.textContent = item.agency_name || 'N/A';
                        row.appendChild(cell2);

                        const cell3 = document.createElement('td');
                        cell3.textContent = item.applied_for || 'N/A';
                        row.appendChild(cell3);

                        const cell4 = document.createElement('td');
                        cell4.textContent = item.date || 'N/A';
                        row.appendChild(cell4);

                        // Create and append the "View" button cell
                        const cell5 = document.createElement('td');

                        // Create an anchor element
                        const link = document.createElement('a');
                        link.href = `/noc/viewApplication?id=${item.id}`
                        link.title = 'View Details';

                        // Create an image element
                        const img = document.createElement('img');
                        img.src = '/img/flowchartTable.png'; // Path to your logo
                        img.alt = 'View';
                        img.style.width = '24px'; // Adjust size as needed
                        img.style.height = '24px'; // Adjust size as needed

                        // Append the image to the anchor
                        link.appendChild(img);

                        // Append the anchor to the table cell
                        cell5.appendChild(link);

                        // Append the cell to the row
                        row.appendChild(cell5);

                        // For Flow Chart

                        // Create and append the "View" button cell
                        const cell6 = document.createElement('td');

                        // Create an anchor element
                        const flowChartLink = document.createElement('a');
                        flowChartLink.href = `/noc/flowchart?id=${item.id}`
                        flowChartLink.title = 'View Flow Chart';

                        // Create an image element
                        const flowChartImg = document.createElement('img');
                        flowChartImg.src = '/img/flowchart.png'; // Path to your logo
                        flowChartImg.alt = 'View';
                        flowChartImg.style.width = '24px'; // Adjust size as needed
                        flowChartImg.style.height = '24px'; // Adjust size as needed

                        // Append the image to the anchor
                        flowChartLink.appendChild(flowChartImg);

                        // Append the anchor to the table cell
                        cell6.appendChild(flowChartLink);

                        // Append the cell to the row
                        row.appendChild(cell6);


                        tableBody.appendChild(row);

                        serialNumber++;
                    });
                } else {
                    console.error('Failed to fetch data:', response.message);
                }
            })
            .catch(error => console.error('Error fetching data:', error));
    }

    // Get the financial year from the query parameter
    const urlParams = new URLSearchParams(window.location.search);
    const finYear = urlParams.get('finYear');

    // Fetch and display the data for the specified financial year
    if (finYear) {
        updateReviewApplications(finYear);
    }
    // populateDropdown('canal', canalList);




});




// Js For Show Review Application By Using Sidebar

document.addEventListener('DOMContentLoaded', () => {
    const selectElement = document.getElementById('financial-year-select');

    // Function to update the table with review applications based on selected financial year
    function updateReviewApplicationsBySidebar(finYear) {
        if (!finYear) {
            console.error('Financial year is not selected.');
            return;
        }

        fetch(`/data/fetchReviewApplications?financialYear=${finYear}`)
            .then(response => response.json())
            .then(response => {
                if (response.flag === 'Success') {
                    const data = response.data.applicationData;
                    const tableBody = document.getElementById('tableBody');

                    // Clear existing rows
                    tableBody.innerHTML = '';

                    // Initialize the Serial Number counter
                    let serialNumber = 1;

                    // Populate table with fetched data
                    data.forEach(item => {
                        const row = document.createElement('tr');

                        // Create table cells and append them to the row
                        const cell1 = document.createElement('td');
                        cell1.textContent = serialNumber || 'N/A';
                        row.appendChild(cell1);

                        const cell2 = document.createElement('td');
                        cell2.textContent = item.agency_name || 'N/A';
                        row.appendChild(cell2);

                        const cell3 = document.createElement('td');
                        cell3.textContent = item.applied_for || 'N/A';
                        row.appendChild(cell3);

                        const cell4 = document.createElement('td');
                        cell4.textContent = item.date || 'N/A';
                        row.appendChild(cell4);

                        // Create and append the "View" button cell
                        const cell5 = document.createElement('td');


                        // Create an anchor element
                        const link = document.createElement('a');
                        link.href = `/noc/viewOnlyApplication?id=${item.id}`;
                        link.title = 'View Details';
                        link.style.display = 'flex';
                        link.style.justifyContent = 'center';
                        link.style.alignItems = 'center';


                        // Create an image element
                        const img = document.createElement('img');
                        img.src = '/img/table.png'; // Path to your logo
                        img.alt = 'View';
                        img.style.width = '24px'; // Adjust size as needed
                        img.style.height = '24px'; // Adjust size as needed


                        // Append the image to the anchor
                        link.appendChild(img);

                        // Append the anchor to the table cell
                        cell5.appendChild(link);

                        // Append the cell to the row
                        row.appendChild(cell5);

                        // For Flow Chart
                        const cell6 = document.createElement('td');

                        // Create an anchor element
                        const flowChartLink = document.createElement('a');
                        flowChartLink.href = `/noc/flowchart?id=${item.id}`;
                        flowChartLink.title = 'View Flow Chart';

                        // Create an image element
                        const flowChartImg = document.createElement('img');
                        flowChartImg.src = '/img/FlowChart_new.png'; // Path to your logo
                        flowChartImg.alt = 'View';
                        flowChartImg.style.width = '24px'; // Adjust size as needed
                        flowChartImg.style.height = '24px'; // Adjust size as needed
                        flowChartLink.style.display = 'flex';
                        flowChartLink.style.justifyContent = 'center';
                        flowChartLink.style.alignItems = 'center';

                        // Append the image to the anchor
                        flowChartLink.appendChild(flowChartImg);

                        // Append the anchor to the table cell
                        cell6.appendChild(flowChartLink);

                        // Append the cell to the row
                        row.appendChild(cell6);

                        tableBody.appendChild(row);

                        serialNumber++;
                    });
                } else {
                    console.error('Failed to fetch data:', response.message);
                }
            })
            .catch(error => console.error('Error fetching data:', error));
    }

    // Event listener to update the table when the dropdown value changes
    selectElement.addEventListener('change', () => {
        const selectedFinYear = selectElement.value;
        updateReviewApplicationsBySidebar(selectedFinYear);
    });

    // Initialize the table with the current financial year on page load
    const initialFinYear = selectElement.value;
    if (initialFinYear) {
        updateReviewApplicationsBySidebar(initialFinYear);
    }
});
