// document.getElementById('orderForm').addEventListener('submit', function(event) {
//     event.preventDefault(); // Prevent the default form submission
//
//     var formData = new FormData(this);
//
//     fetch('/data/savingorder', {
//         method: 'POST',
//         body: formData
//     })
//         .then(response => response.json())
//         .then(data => {
//             if (data.flag === 'Success') {
//                 alert('Data Saved Successfully!');
//                 fetchOrderList(); // Fetch the order list after saving the data
//             } else {
//                 alert('Failed to Save Data');
//             }
//             // Reload the page-->
//             window.location.href = '/noc/postorder';
//         })
//         .catch(error => console.error('Error:', error));
// });
//
// // Function to fetch and display the order list
// function fetchOrderList() {
//     fetch('/data/orderlist')
//         .then(response => response.json())
//         .then(data => {
//             if (data.flag === 'Success') {
//                 var orderTableBody = document.querySelector('#orderTable tbody');
//                 orderTableBody.innerHTML = ''; // Clear existing table data
//
//                 var orders = data.data.orders;
//                 orders.forEach(order => {
//                     var row = `<tr>
//                             <td>${order.orderNo}</td>
//                             <td>${order.date}</td>
//                             <td>${order.description}</td>
//                              <td><button class="btn btn-danger btn-sm" onclick="deleteOrder(${order.id})">Delete</button></td>
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
// function deleteOrder(id) {
//     if (confirm('Are you sure you want to delete this order?')) {
//         fetch(`/data/deleteorder/${id}`, {
//             method: 'DELETE',
//         })
//             .then(response => response.json())
//             .then(data => {
//                 if (data.flag === 'Success') {
//                     alert('Order deleted successfully!');
//                     fetchOrderList(); // Refresh the list after deletion
//                 } else {
//                     alert('Failed to delete order');
//                 }
//             })
//             .catch(error => console.error('Error deleting order:', error));
//     }
// }

document.getElementById('orderForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent the default form submission

    var formData = new FormData(this);
    var actionUrl = this.action; // Get the form's action URL

    fetch(actionUrl, {
        method: 'POST', // Use PUT if you're updating data
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(`Server Error: ${response.status} - ${text}`);
                });
            }
            return response.json();
        })
        .then(data => {
            if (data.flag === 'Success') {
                alert('Data Saved Successfully!');
                fetchOrderList(); // Fetch the order list after saving the data
            } else {
                alert('Failed to Save Data');
            }
            window.location.href = '/noc/postorder'; // Reload the page after submission
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred: ' + error.message);
        });
});

function fetchOrderList() {
    fetch('/data/orderlist')
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(`Server Error: ${response.status} - ${text}`);
                });
            }
            return response.json();
        })
        .then(data => {
            if (data.flag === 'Success') {
                var orderTableBody = document.querySelector('#orderTable tbody');
                orderTableBody.innerHTML = ''; // Clear existing table data

                var orders = data.data.orders;
                orders.forEach(order => {
                    var row = `<tr>
                    <td>${order.orderNo}</td>
                    <td>${order.date}</td>
                    <td>${order.description}</td>
                    <td><button class="btn btn-danger btn-sm" onclick="deleteOrder(${order.id})">Delete</button></td>
                    <td><button class="btn btn-info btn-sm" onclick="updateOrder(${order.id})">Update</button></td>
                </tr>`;
                    orderTableBody.insertAdjacentHTML('beforeend', row);
                });
            } else {
                alert('Failed to Fetch Orders');
            }
        })
        .catch(error => console.error('Error fetching orders:', error));
}

function deleteOrder(id) {
    if (confirm('Are you sure you want to delete this order?')) {
        fetch(`/data/deleteorder/${id}`, {
            method: 'DELETE',
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => {
                        throw new Error(`Server Error: ${response.status} - ${text}`);
                    });
                }
                return response.json();
            })
            .then(data => {
                if (data.flag === 'Success') {
                    alert('Order deleted successfully!');
                    fetchOrderList(); // Refresh the list after deletion
                } else {
                    alert('Failed to delete order');
                }
            })
            .catch(error => console.error('Error deleting order:', error));
    }
}

function updateOrder(id) {
    fetch(`/data/order/${id}`)
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(`Server Error: ${response.status} - ${text}`);
                });
            }
            return response.json();
        })
        .then(data => {
            if (data.flag === 'Success') {
                var order = data.data.order;

                // Populate the form with the order details
                document.getElementById('orderNo').value = order.orderNo;
                document.getElementById('date').value = order.date;
                document.getElementById('description').value = order.description;

                // Set fields to read-only except for description
                document.getElementById('orderNo').readOnly = true;
                document.getElementById('date').readOnly = true;
                document.getElementById('description').readOnly = false;

                // Change the form's action URL to the update endpoint
                document.getElementById('orderForm').action = `/data/order/update`;
                document.getElementById('orderForm').method = 'PUT'; // Change to PUT if needed
            } else {
                alert('Failed to load order details');
            }
        })
        .catch(error => console.error('Error fetching order details:', error));
}

// Fetch the order list when the page loads
document.addEventListener('DOMContentLoaded', fetchOrderList);
