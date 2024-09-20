document.addEventListener('DOMContentLoaded', () => {
    function getIdFromUrl() {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get('id');
    }

    function updateTableData(id) {
        fetch(`/data/logsOfFile?id=${id}`)  // Fetch data using the id from the URL
            .then(response => response.json())
            .then(response => {

                const logs = response.data.logs; // Access the logs array from the data map
                if (response.statusCode === 200 && response.data && response.data.logs && Array.isArray(response.data.logs)) {
                    const tbody = document.querySelector('#example tbody');
                    tbody.innerHTML = '';  // Clear existing table rows

                    // Assuming `logs` is the array of log objects you have fetched
                    logs.forEach((log, index) => {

                        const row = document.createElement('tr');

                        // Serial Number (S No.)
                        const serialNumberCell = document.createElement('td');
                        serialNumberCell.textContent = index + 1;  // Serial number starts from 1
                        row.appendChild(serialNumberCell);

                        // Date
                        const dateCell = document.createElement('td');
                        dateCell.textContent = log.date || 'N/A';
                        row.appendChild(dateCell);

                        // Inspection Level
                        const inspectionLevelCell = document.createElement('td');
                        inspectionLevelCell.textContent = log.from_department_user || 'N/A';
                        row.appendChild(inspectionLevelCell);

                        // Action
                        const actionCell = document.createElement('td');
                        actionCell.textContent = log.to_department_user || 'N/A';
                        row.appendChild(actionCell);

                        // // Status
                        // const statusCell = document.createElement('td');
                        // statusCell.textContent = log.status || 'N/A';  // Replace 'status' with the correct property from your log object
                        // row.appendChild(statusCell);
                        //
                        // // Remark
                        // const remarkCell = document.createElement('td');
                        // remarkCell.textContent = log.remark || 'N/A';  // Replace 'remark' with the correct property from your log object
                        // row.appendChild(remarkCell);


                        // Example: Set this dynamically based on some condition or data
                        const dynamicContent = `
        <a href="/noc/flowchart?id=${log.app_id}">
            <button class="btn btn-light">
                <span>
                    <img class="img card-logo" src="/img/flowchart.png" alt="rejected">
                </span>
            </button>
        </a>
    `;

// Inject the dynamic content into the flowChartButton div
                        document.getElementById('flowChartButton').innerHTML = dynamicContent;

                        // Append the row to the table body
                        document.querySelector('#example tbody').appendChild(row);
                    });

                } else {
                    console.error('No data available or the data format is incorrect.');
                }
            })
            .catch(error => console.error('Error fetching table data:', error));
    }

    const id = getIdFromUrl();  // Get the id from the URL
    if (id) {
        updateTableData(id);
    } else {
        console.error('No ID found in the URL');
    }
});
