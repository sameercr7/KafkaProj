document.getElementById('feedbackForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent the default form submission

    var formData = new FormData(this);

    fetch('/data/savingfeedback', {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data.flag === 'Success') {
                alert('Data Saved Successfully!');
            } else {
                alert('Failed to Save Data');
            }
            // Reload the page-->
            window.location.href = '/noc/agency_feedback';
        })
        .catch(error => console.error('Error:', error));
});
