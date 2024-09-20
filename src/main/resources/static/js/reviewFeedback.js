function fetchOrderList() {
    fetch('/data/feedbacklist')
        .then(response => response.json())
        .then(data => {
            if (data.flag === 'Success') {
                var orderTableBody = document.querySelector('#orderTable tbody');
                orderTableBody.innerHTML = ''; // Clear existing table data

                var feddbacks = data.data.feddbacks; // Updated to match response structure
                feddbacks.forEach(feedback => {
                    var row = `<tr>
                            <td>${feedback.agencyName}</td>
                            <td>${feedback.description}</td>
                               <td><img id="image-${feedback.id}" class="rounded-circle mb-2" width="80" alt="${feedback.name}"></td>
                                  <td><button class="btn btn-danger btn-sm" onclick="deleteOrder(${feedback.id})">Delete</button></td>
                                   <td><button class="btn btn-primary btn-sm" onclick="updateFeedback(${feedback.id})">Update</button></td>
                        </tr>`;
                    orderTableBody.insertAdjacentHTML('beforeend', row);
                    fetchImage(feedback.id);
                });
            } else {
                alert('Failed to Fetch Orders');
            }
        })
        .catch(error => console.error('Error fetching orders:', error));
}
function fetchImage(feedbackId) {
    const imageElement = document.getElementById(`image-${feedbackId}`);
    const imageUrl = `/data/viewfeedbackimage/${feedbackId}`;

    fetch(imageUrl)
        .then(response => response.blob())
        .then(blob => {
            const objectURL = URL.createObjectURL(blob);
            imageElement.src = objectURL;
        })
        .catch(error => {
            console.error('Error fetching image:', error);
            // Optionally set a default image or placeholder
            imageElement.src = '/img/profile.png';
        });
}

function deleteOrder(id) {
    if (confirm('Are you sure you want to delete this order?')) {
        fetch(`/data/deletefeedback/${id}`, {
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
function updateFeedback(feedbackId) {
    // Send the update request to the backend
    fetch(`/data/feedback/update`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded', // Sending form-urlencoded data
        },
        body: new URLSearchParams({
            id: feedbackId // Only sending the feedback ID
        })
    })
        .then(response => response.json())
        .then(data => {
            if (data.flag === 'Success') {
                alert('Feedback updated successfully!');
                fetchOrderList(); // Refresh the list to reflect the updated feedback
            } else {
                alert('Failed to update feedback');
            }
        })
        .catch(error => console.error('Error updating feedback:', error));
}

document.addEventListener('DOMContentLoaded', fetchOrderList);
