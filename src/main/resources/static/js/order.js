
    fetch('/data/orderlist')
        .then(response => response.json())
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
                              <td><button class="btn btn-danger btn-sm" onclick="downloadPDF(${order.id})">PDF</button></td>

                        </tr>`;
                    orderTableBody.insertAdjacentHTML('beforeend', row);
                });
            } else {
                alert('Failed to Fetch Orders');
            }
        })
        .catch(error => console.error('Error fetching orders:', error));
    function downloadPDF(orderId) {
        // Create a temporary link element
        var link = document.createElement('a');
        link.href = `/data/vieworderpdf/${orderId}`;
        link.download = `order_${orderId}.pdf`;
        link.style.display = 'none'; // Hide the link
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    }