//
// function fetchOrderList() {
//     fetch('/data/grevancelist')
//         .then(response => response.json())
//         .then(data => {
//             if (data.flag === 'Success') {
//                 var orderTableBody = document.querySelector('#orderTable tbody');
//                 orderTableBody.innerHTML = ''; // Clear existing table data
//
//                 var orders = data.data.orders;
//                 orders.forEach(order => {
//                     var reply = order.reply ? order.reply : 'Not Replied';
//                     var row = `<tr>
//                             <td>${order.applicationId}</td>
//                             <td>${order.description}</td>
//                             <td>${order.nocType}</td>
//                              <td>${reply}</td>
//
//                         </tr>`;
//                     orderTableBody.insertAdjacentHTML('beforeend', row);
//                 });
//             } else {
//                 alert('Failed to Fetch Orders');
//             }
//         })
//         .catch(error => console.error('Error fetching orders:', error));
// }
//
// // Fetch the order list when the page loads
// document.addEventListener('DOMContentLoaded', fetchOrderList);

function fetchOrderList() {
    fetch('/data/grevancelist')
        .then(response => response.json())
        .then(data => {
            if (data.flag === 'Success') {
                var orderTableBody = document.querySelector('#orderTable tbody');
                orderTableBody.innerHTML = ''; // Clear existing table data

                var orders = data.data.orders;
                orders.forEach(order => {
                    var reply = order.reply ? order.reply : 'Not Replied';
                    var row = `<tr>
                            <td>${order.applicationId}</td>
                            <td>${order.description}</td>
                         
                            <td>${reply}</td>
                            <td><button class="btn btn-primary update-btn" data-id="${order.id}" data-application-id="${order.applicationId}" data-description="${order.description}" data-reply="${reply}">REPLY</button></td>
                        </tr>`;
                    orderTableBody.insertAdjacentHTML('beforeend', row);
                });
            } else {
                alert('Failed to Fetch Orders');
            }
        })
        .catch(error => console.error('Error fetching orders:', error));
}

document.addEventListener('DOMContentLoaded', fetchOrderList);

// Handle form display and submission
document.addEventListener('click', function(event) {
    if (event.target.classList.contains('update-btn')) {
        var button = event.target;
        document.querySelector('#formId').value = button.getAttribute('data-id');
        document.querySelector('#formApplicationId').value = button.getAttribute('data-application-id');
        document.querySelector('#formDescription').value = button.getAttribute('data-description');
        document.querySelector('#formReply').value = button.getAttribute('data-reply');

        // Show the form
        document.querySelector('#updateForm').style.display = 'block';
    }
});

document.querySelector('#updateGrevanceForm').addEventListener('submit', function(event) {
    event.preventDefault();

    var formData = new FormData(this);
    fetch('/data/grevance/update', {
        method: 'PUT',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.flag === 'Success') {
                if (data.flag === 'Success') {
                    Swal.fire({
                        toast: true,
                        position: 'bottom-end',
                        icon: 'success',
                        title: `Posted Succsfully`,
                        showConfirmButton: false,
                        timer: 900000,
                        timerProgressBar: true
                    })
                }

                // Hide the form and refresh the table
                document.querySelector('#updateForm').style.display = 'none';
                fetchOrderList();
            } else {
                alert('Failed to update reply');
            }
        })
        .catch(error => console.error('Error updating reply:', error));
});
