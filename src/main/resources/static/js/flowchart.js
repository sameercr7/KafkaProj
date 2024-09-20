// document.addEventListener('DOMContentLoaded', function() {
//     const flowchart = document.querySelector('.flowchart');
//     const cards = Array.from(flowchart.children);
//     const cardsPerRow = 4;
//     const totalRows = Math.ceil(cards.length / cardsPerRow);
//
//     for (let i = 0; i < cards.length; i += cardsPerRow) {
//         const rowIndex = Math.floor(i / cardsPerRow);
//         let row = cards.slice(i, i + cardsPerRow);
//
//         if (rowIndex % 2 === 1) {
//             // Reverse row for even rows
//             row.reverse();
//         }
//
//         row.forEach((card, index) => {
//             const isLastRow = (rowIndex === totalRows - 1);
//             const connectorHorizontal = document.createElement('div');
//             connectorHorizontal.classList.add('connector-line', 'connector-horizontal');
//
//             if (rowIndex % 2 === 0) {
//                 // For odd rows
//                 if (index < row.length - 1) {
//                     card.querySelector('.box').appendChild(connectorHorizontal);
//                 } else if (!isLastRow) {
//                     const connectorVertical = document.createElement('div');
//                     connectorVertical.classList.add('connector-line', 'connector-vertical');
//                     card.querySelector('.box').appendChild(connectorVertical);
//                 }
//             } else {
//                 // For even rows
//                 if (index === 0 && !isLastRow) {
//                     const connectorVertical = document.createElement('div');
//                     connectorVertical.classList.add('connector-line', 'connector-vertical');
//                     card.querySelector('.box').appendChild(connectorVertical);
//                 } else if (index > 0) {
//                     connectorHorizontal.classList.add('connector-left');
//                     card.querySelector('.box').appendChild(connectorHorizontal);
//                 }
//             }
//         });
//
//         row.forEach(card => flowchart.appendChild(card));
//     }
// });
//
//
//
// document.addEventListener('DOMContentLoaded', () => {
//
//     // Function to get the 'id' from the URL
//     function getIdFromUrl() {
//         const urlParams = new URLSearchParams(window.location.search);
//         return urlParams.get('id');
//     }
//
//     // Function to update the dashboard with data for a specific user ID
//     function updateLogsData(userId) {
//         fetch(`/data/logsOfFile?id=${userId}`)
//             .then(response => response.json())
//             .then(response => {
//                 if (response.statusCode === 200 && response.data && response.data.logs && Array.isArray(response.data.logs)) {
//                     const logs = response.data.logs; // Access the logs array from the data map
//                     const container = document.querySelector('.flowchart'); // Get the container where you want to insert the data
//                     container.innerHTML = ''; // Clear existing content
//
//                     logs.forEach(log => {
//                         const logDiv = document.createElement('div');
//                         logDiv.className = 'col-3 mb-3';
//                         logDiv.innerHTML = `
//                             <div class="box">
//                                 <div class="box-content">
//                                     <div class="box-row">
//                                         <span class="label">Name:</span>
//                                         <span class="value">${log.fromDepartmentUser || 'N/A'}</span>
//                                     </div>
//                                     <div class="box-row">
//                                         <span class="label">Action:</span>
//                                         <span class="value">${log.toDepartmentUser ? 'Forwarded to ' + log.toDepartmentUser : 'N/A'}</span>
//                                     </div>
//                                     <div class="box-row">
//                                         <span class="label">Date:</span>
//                                         <span class="value">${log.date || 'N/A'}</span>
//                                     </div>
//                                 </div>
//                             </div>
//                         `;
//                         container.appendChild(logDiv);
//                     });
//                 } else {
//                     console.error('Expected an array but got:', response.data.logs);
//                 }
//             })
//             .catch(error => console.error('Error fetching logs data:', error));
//     }
//
//     // Get the ID from the URL and update the dashboard
//     const userId = getIdFromUrl();
//     if (userId) {
//         updateLogsData(userId);
//     } else {
//         console.error('No user ID found in the URL');
//     }
// });

document.addEventListener('DOMContentLoaded', function() {
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

    // Function to get the 'id' from the URL
    function getIdFromUrl() {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get('id');
    }

    // Function to update the dashboard with data for a specific user ID
    function updateLogsData(userId) {
        fetch(`/data/logsOfFile?id=${userId}`)
            .then(response => response.json())
            .then(response => {
                if (response.statusCode === 200 && response.data && response.data.logs && Array.isArray(response.data.logs)) {
                    const logs = response.data.logs; // Access the logs array from the data map
                    const container = document.querySelector('.flowchart'); // Get the container where you want to insert the data
                    container.innerHTML = ''; // Clear existing content

                    logs.forEach(log => {
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
                                        <span class="value"> ${log.to_department_user ? 'Forwarded to <br>' + log.to_department_user : 'N/A'}</span>
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
                    });

                    // Recalculate and add connectors after updating the cards
                    const cards = Array.from(container.children);
                    createConnectors(cards, cardsPerRow); // Use the same number of cards per row
                } else {
                    console.error('Expected an array but got:', response.data.logs);
                }
            })
            .catch(error => console.error('Error fetching logs data:', error));
    }

    // Get the ID from the URL and update the dashboard
    const userId = getIdFromUrl();
    if (userId) {
        updateLogsData(userId);
    } else {
        console.error('No user ID found in the URL');
    }
});

