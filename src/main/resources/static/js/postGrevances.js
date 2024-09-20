document.getElementById('grevancesForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent the default form submission

    var formData = new FormData(this);

    fetch('/data/savinggrevance', {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.flag === 'Success') {
                Swal.fire({
                    toast: true,
                    position: 'bottom-end',
                    icon: 'success',
                    title: `Posted Succsfully`,
                    showConfirmButton: false,
                    timer: 5000,
                    timerProgressBar: true
                }).then(() => {
                    // Reload the page after the toast disappears
                    window.location.href = '/noc/agency_grevances'; // Redirect after successful response
                });
            } else {
                alert('Failed to Save Data');
            }
            // // Reload the page-->
            // window.location.href = '/noc/agency_grevances';
        })
        .catch(error => console.error('Error:', error));
});

// Function to fetch and display the order list
function fetchOrderList() {
    fetch('/data/grevanceuser/{username}')
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
                          <td><button class="btn btn-danger btn-sm" onclick="deleteOrder(${order.id})">Delete</button></td>

                        </tr>`;
                    orderTableBody.insertAdjacentHTML('beforeend', row);
                });
            } else {
                alert('Failed to Fetch Orders');
            }
        })
        .catch(error => console.error('Error fetching orders:', error));
}

// Fetch the order list when the page loads
document.addEventListener('DOMContentLoaded', fetchOrderList);
function deleteOrder(id) {
    if (confirm('Are you sure you want to delete this order?')) {
        fetch(`/data/deletegrevance/${id}`, {
            method: 'DELETE',
        })
            .then(response => response.json())
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