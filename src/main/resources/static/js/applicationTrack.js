document.addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent the default form submission behavior

    const specialId = document.getElementById('spc_Id').value;
    console.log("specialId", specialId);


// Perform the first fetch, passing the specialId
    fetch(`/data/findAplicationId/${specialId}`)
        .then(response => response.json())
        .then(data => {
            console.log("Data from first fetch:", data);
            if (data.length > 0) {
                const id = data[0].id; // Assuming 'id' is part of the data received
                console.log(id);

                if (id) {
                    functionToFetchAppCurrid(id); // Pass the id to the next function
                } else {
                    console.error('No ID found in the response');
                    alert('No ID found in the response');
                }
            } else {
                console.error('No data received for the given special Id');
                alert('No Data Found for the given Special Id');
            }
        })
        .catch(error => {
            console.error('Error in the first fetch:', error);
            alert('An error occurred while fetching data.');
        });


    function functionToFetchAppCurrid(id) {
        // Perform the second fetch using the id obtained from the first fetch
        fetch(`/data/findAppCurrStatusId/${id}`)
            .then(response => response.json())
            .then(data => {
                console.log("Data from functionToFetchAppCurrid:", data);
                const userId = data[0].id; // Assuming 'userId' is part of the data received

                if (userId) {
                    updateLogsData(userId);
                } else {
                    console.error('No user ID found in the response');
                }
            })
            .catch(error => console.error('Error in functionToFetchAppCurrid:', error));
    }





    const flowchart = document.querySelector('.flowchart');
    const cardsPerRow = 4;

    // Function to create connectors
    function createConnectors(cards, cardsPerRow) {
        const totalRows = Math.ceil(cards.length / cardsPerRow);

        for (let i = 0; i < cards.length; i += cardsPerRow) {
            const rowIndex = Math.floor(i / cardsPerRow);
            let row = cards.slice(i, i + cardsPerRow);

            if (rowIndex % 2 === 1) {
                // Reverse row for odd rows
                row.reverse();
            }

            row.forEach((card, index) => {
                const isLastRow = (rowIndex === totalRows - 1);
                const isFirstInRow = (index === 0);
                const isLastInRow = (index === row.length - 1);
                const connectorHorizontal = document.createElement('div');
                connectorHorizontal.classList.add('connector-line', 'connector-horizontal');

                if (rowIndex % 2 === 0) {
                    // For even rows
                    if (isLastInRow && !isLastRow) {
                        // Add vertical connector if last in row and not last row
                        const connectorVertical = document.createElement('div');
                        connectorVertical.classList.add('connector-line', 'connector-vertical');
                        card.querySelector('.box').appendChild(connectorVertical);
                    } else if (!isLastInRow) {
                        card.querySelector('.box').appendChild(connectorHorizontal);
                    }
                } else {
                    // For odd rows
                    if (isFirstInRow && !isLastRow) {
                        const connectorVertical = document.createElement('div');
                        connectorVertical.classList.add('connector-line', 'connector-vertical');
                        card.querySelector('.box').appendChild(connectorVertical);
                    } else if (!isFirstInRow) {
                        connectorHorizontal.classList.add('connector-left');
                        card.querySelector('.box').appendChild(connectorHorizontal);
                    }
                }
            });

            row.forEach(card => flowchart.appendChild(card));
        }
    }

// Function to update the dashboard with data for a specific user ID
    function updateLogsData(userId) {
        console.log("userId", userId);

        fetch(`/data/logsOfFile?id=${userId}`)
            .then(response => response.json())
            .then(response => {
                if (response.statusCode === 200 && response.data && response.data.logs && Array.isArray(response.data.logs)) {
                    const logs = response.data.logs;
                    const container = document.querySelector('.flowchart');
                    const tbody = document.querySelector('#example tbody');
                    const tableDiv = document.querySelector('.tabularData');

                    container.innerHTML = ''; // Clear existing content
                    tbody.innerHTML = ''; // Clear existing table rows
                    tableDiv.style.display = 'block';

                    logs.forEach((log, index) => {
                        console.log("Data", log);

                        // Create and append the log container
                        const logDiv = document.createElement('div');
                        logDiv.className = 'col-3 mb-3';
                        logDiv.innerHTML = `
                        <div class="box">
                            <div class="box-content">
                                <div class="box-row">
                                    <span class="label">Name:</span>
                                    <span class="value">${log.from_department_user || 'N/A'}</span>
                                </div>
                                <div class="box-row">
                                    <span class="label">Action:</span>
                                    <span class="value">${log.to_department_user ? 'Forwarded to <br>' + log.to_department_user : 'N/A'}</span>
                                </div>
                                 <div class="box-row">
                                        <span class="label">Transfer Status:</span>
                                        <span class="value">${log.transfer_status || 'N/A'}</span>
                                    </div>
                                <div class="box-row">
                                        <span class="label">Remark:</span>
                                        <span class="value">${log.transfer_remark || 'N/A'}</span>
                                    </div>
                                <div class="box-row">
                                    <span class="label">Date:</span>
                                    <span class="value">${log.date || 'N/A'}</span>
                                </div>
                            </div>
                        </div>
                    `;
                        container.appendChild(logDiv);

                        // Create and append the table row
                        const row = document.createElement('tr');

                        // Serial Number (S No.)
                        const serialNumberCell = document.createElement('td');
                        serialNumberCell.textContent = index + 1; // Serial number starts from 1
                        row.appendChild(serialNumberCell);


                        // Date
                        const dateCell = document.createElement('td');
                        dateCell.textContent = log.date || 'N/A';
                        row.appendChild(dateCell);

                        // Inspection Level
                        const inspectionLevelCell = document.createElement('td');
                        inspectionLevelCell.textContent = log.from_department_user || 'N/A';
                        row.appendChild(inspectionLevelCell);

                        const transferStatusCell = document.createElement('td');
                        transferStatusCell.textContent = log.transfer_status || 'N/A';
                        row.appendChild(transferStatusCell);

                        const remarkCell = document.createElement('td');
                        remarkCell.textContent = log.transfer_remark || 'N/A';
                        row.appendChild(remarkCell);

                        // Action
                        const actionCell = document.createElement('td');
                        actionCell.textContent = log.to_department_user || 'N/A';
                        row.appendChild(actionCell);

                        // Append the row to the table body
                        tbody.appendChild(row);
                    });

                    // Recalculate and add connectors after updating the cards
                    const cards = Array.from(container.children);
                    createConnectors(cards); // Assuming cardsPerRow is managed globally or is set elsewhere
                } else {
                    console.error('Expected an array but got:', response.data.logs);
                }
            })
            .catch(error => console.error('Error fetching logs data:', error));
    }




});