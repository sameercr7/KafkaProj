// This is For Get Applications Of Current OwnerShip
document.addEventListener('DOMContentLoaded', () => {
    const selectElement = document.getElementById('financial-year-select');

    const role = document.getElementById("role").value;

    // const urlParams = new URLSearchParams(window.location.search);
    // const status = decodeURIComponent(urlParams.get('status'));
    // const applicationId = urlParams.get('appId');
    //
    // console.log("Status:", status);
    // console.log("Application ID:", applicationId);
    //
    // // Now you can use these values in the transfer page
    // document.getElementById('currStatus').value = status;
    // document.getElementById('currAppId').value = applicationId;
    // Function to update the table with review applications based on selected financial year
    function updateReviewApplicationsBySidebar(finYear) {
        if (!finYear) {
            console.error('Financial year is not selected.');
            return;
        }

        fetch(`/data/fetchReviewApplicationsUnderOwnerShip?financialYear=${finYear}`)
            .then(response => response.json())
            .then(response => {
                if (response.flag === 'Success') {
                    const data = response.data.applicationData;
                    const tableBody = document.getElementById('tableBody');

                    // Clear existing rows
                    tableBody.innerHTML = '';

                    // Initialize the Serial Number counter
                    let serialNumber = 1;
                    const statusButton = document.createElement('button');
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

                        const cell5 = document.createElement('td');
                        const statusButton = document.createElement('button'); // Create the button element
                        const administrstionStatusButton = createStatusButton(item.administration_status);
                        cell5.appendChild(administrstionStatusButton);
                        cell5.className = "showHideToggle";
                        row.appendChild(cell5);
// HOD Status
                        const cell6 = document.createElement('td');
                        const hodStatusButton = createStatusButton(item.hod_status);
                        cell6.appendChild(hodStatusButton);
                        cell6.className = "showHideToggle";
                        row.appendChild(cell6);

// Admin Status
                        const cell7 = document.createElement('td');
                        const adminStatusButton = createStatusButton(item.admin_status);
                        cell7.appendChild(adminStatusButton);
                        cell7.className = "showHideToggle";
                        row.appendChild(cell7);

// Deputy Admin Status
                        const cell8 = document.createElement('td');
                        const deputyAdminStatusButton = createStatusButton(item.deputy_admin_status);
                        cell8.appendChild(deputyAdminStatusButton);
                        cell8.className = "showHideToggle";
                        row.appendChild(cell8);

// Sub Admin Status
                        const cell9 = document.createElement('td');
                        const subAdminStatusButton = createStatusButton(item.sub_admin_status);
                        cell9.appendChild(subAdminStatusButton);
                        cell9.className = "showHideToggle";
                        row.appendChild(cell9);

// CE1 Status
                        const cell10 = document.createElement('td');
                        const ce1StatusButton = createStatusButton(item.ce1status);
                        cell10.appendChild(ce1StatusButton);
                        cell10.className = "showHideToggle";
                        row.appendChild(cell10);


                        const cell11 = document.createElement('td');
                        const ce2StatusButton = createStatusButton(item.ce2status);
                        cell11.appendChild(ce2StatusButton);
                        cell11.className = "showHideToggle";
                        row.appendChild(cell11);

                        const cell12 = document.createElement('td');
                        const seStatusButton = createStatusButton(item.se_status);
                        cell12.appendChild(seStatusButton);
                        cell12.className = "showHideToggle";
                        row.appendChild(cell12);


                        const cell13 = document.createElement('td');
                        const eeStatusButton = createStatusButton(item.ee_status);
                        cell13.appendChild(eeStatusButton);
                        cell13.className = "showHideToggle";
                        row.appendChild(cell13);

                        // Create and append the "Review Application" button cell
                        const cell14 = document.createElement('td');

                        // Create an anchor element
                        const anchor = document.createElement('a');
                        anchor.className = 'btn btn-success text-decoration-none'; // Bootstrap classes for button and text decoration
                        anchor.setAttribute('href', `/noc/viewApplication?id=${item.id}`); // URL with the ID as a query parameter

                        // Set the text content for the link
                        anchor.textContent = 'Review Application'; // Button text

                        // Append the anchor to the table cell
                        cell14.appendChild(anchor);

                        // Append the cell to the row
                        row.appendChild(cell14);

                        // Create and append the "Transfer File" button cell
                        const cell15 = document.createElement('td');

                        // Create a button element
                        const button = document.createElement('button');
                        button.type = 'button';
                        button.className = 'btn btn-primary'; // Bootstrap class for primary button
                        button.setAttribute('data-bs-toggle', 'modal');
                        button.setAttribute('data-bs-target', '#staticBackdrop');
                        button.textContent = 'Transfer File'; // Button text
                        // Assign an ID to the button for later reference
                        button.id = 'transferFileButton';

                        if(role ==='admin'){

                            // Condition to enable the button
                            if (item.admin_status == 'REVIEWED') {
                                button.disabled = false; // Enable the button
                            } else {
                                button.disabled = true; // Keep the button disabled
                            }
                        }

                        if(role ==='ce1'){

                            // Condition to enable the button
                            if (item.ce1status == 'REVIEWED') {
                                button.disabled = false; // Enable the button
                            } else {
                                button.disabled = true; // Keep the button disabled
                            }
                        }

                        if(role ==='ce2'){

                            // Condition to enable the button
                            if (item.ce2status == 'REVIEWED') {
                                button.disabled = false; // Enable the button
                            } else {
                                button.disabled = true; // Keep the button disabled
                            }
                        }

                        if(role ==='se'){

                            // Condition to enable the button
                            if (item.se_status == 'REVIEWED') {
                                button.disabled = false; // Enable the button
                            } else {
                                button.disabled = true; // Keep the button disabled
                            }
                        }

                        if(role ==='ee'){

                            // Condition to enable the button
                            if (item.ee_status == 'REVIEWED') {
                                button.disabled = false; // Enable the button
                            } else {
                                button.disabled = true; // Keep the button disabled
                            }
                        }

                        if(role ==='deputyAdmin'){

                            // Condition to enable the button
                            if (item.deputy_admin_status == 'REVIEWED') {
                                button.disabled = false; // Enable the button
                            } else {
                                button.disabled = true; // Keep the button disabled
                            }
                        }

                        if(role ==='subAdmin'){

                            // Condition to enable the button
                            if (item.sub_admin_status == 'REVIEWED') {
                                button.disabled = false; // Enable the button
                            } else {
                                button.disabled = true; // Keep the button disabled
                            }
                        }

                        if(role ==='hod'){

                            // Condition to enable the button
                            if (item.hod_status == 'REVIEWED') {
                                button.disabled = false; // Enable the button
                            } else {
                                button.disabled = true; // Keep the button disabled
                            }
                        }


                        // // Condition to enable the button
                        // if (item.id == applicationId && status == 'REVIEWED') {
                        //     button.disabled = false; // Enable the button
                        // } else {
                        //     button.disabled = true; // Keep the button disabled
                        // }

                        // Use a custom data attribute for agency name
                        button.setAttribute('data-agency-name', item.id || 'N/A');

                        // Append the button to the table cell
                        cell15.appendChild(button);

                        // Append the cell to the row
                        row.appendChild(cell15);

                        // Create and append the "Transfer To Agency" button cell
                        const cell16 = document.createElement('td');

                        // Create an anchor element
                        const transferToAgency = document.createElement('a');
                        transferToAgency.className = 'btn btn-secondary text-decoration-none'; // Bootstrap classes for button and text decoration
                        transferToAgency.setAttribute('href', `/noc/transferToAgency?id=${item.id}`); // URL with the ID as a query parameter

                        // Set the text content for the link
                        transferToAgency.textContent = 'Transfer To Agency'; // Button text

                        // // Condition to enable "Transfer To Agency" button
                        // if (item.id == applicationId && status === 'REJECTED_REQUEST_FOR_RESUBMISSION') {
                        //     transferToAgency.classList.remove('disabled'); // Enable the link
                        // } else {
                        //     transferToAgency.classList.add('disabled'); // Disable the link
                        // }

                        if(role ==='admin'){

                            // Condition to enable "Transfer To Agency" button
                            if (item.admin_status === 'REJECTED_REQUEST_FOR_RESUBMISSION') {
                                transferToAgency.classList.remove('disabled'); // Enable the link
                            } else {
                                transferToAgency.classList.add('disabled'); // Disable the link
                            }
                        }

                        if(role ==='ce1'){

                            // Condition to enable "Transfer To Agency" button
                            if (item.ce1status === 'REJECTED_REQUEST_FOR_RESUBMISSION') {
                                transferToAgency.classList.remove('disabled'); // Enable the link
                            } else {
                                transferToAgency.classList.add('disabled'); // Disable the link
                            }
                        }

                        if(role ==='ce2'){

                            // Condition to enable "Transfer To Agency" button
                            if (item.ce2status === 'REJECTED_REQUEST_FOR_RESUBMISSION') {
                                transferToAgency.classList.remove('disabled'); // Enable the link
                            } else {
                                transferToAgency.classList.add('disabled'); // Disable the link
                            }
                        }

                        if(role ==='se'){

                            // Condition to enable "Transfer To Agency" button
                            if (item.se_status === 'REJECTED_REQUEST_FOR_RESUBMISSION') {
                                transferToAgency.classList.remove('disabled'); // Enable the link
                            } else {
                                transferToAgency.classList.add('disabled'); // Disable the link
                            }
                        }

                        if(role ==='ee'){

                            // Condition to enable "Transfer To Agency" button
                            if (item.ee_status === 'REJECTED_REQUEST_FOR_RESUBMISSION') {
                                transferToAgency.classList.remove('disabled'); // Enable the link
                            } else {
                                transferToAgency.classList.add('disabled'); // Disable the link
                            }
                        }

                        if(role ==='deputyAdmin'){

                            // Condition to enable the button
                            if (item.deputy_admin_status === 'REJECTED_REQUEST_FOR_RESUBMISSION') {
                                transferToAgency.classList.remove('disabled'); // Enable the link
                            } else {
                                transferToAgency.classList.add('disabled'); // Disable the link
                            }
                        }

                        if(role ==='subAdmin'){

                            // Condition to enable the button
                            if (item.sub_admin_status === 'REJECTED_REQUEST_FOR_RESUBMISSION') {
                                transferToAgency.classList.remove('disabled'); // Enable the link
                            } else {
                                transferToAgency.classList.add('disabled'); // Disable the link
                            }
                        }

                        if(role ==='hod'){

                            // Condition to enable the button
                            if (item.hod_status === 'REJECTED_REQUEST_FOR_RESUBMISSION') {
                                transferToAgency.classList.remove('disabled'); // Enable the link
                            } else {
                                transferToAgency.classList.add('disabled'); // Disable the link
                            }
                        }

                        // Append the anchor to the table cell
                        cell16.appendChild(transferToAgency);

                        // Append the cell to the row
                        row.appendChild(cell16);

                        tableBody.appendChild(row);

                        serialNumber++;
                    });

                    // Update modal content based on button click
                    document.addEventListener('click', function (event) {
                        if (event.target && event.target.matches('button[data-agency-name]')) {
                            const agencyName = event.target.getAttribute('data-agency-name');

                            // Set the agency name in the modal input field
                            document.getElementById('applicationId').value = agencyName;
                        }
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

function createStatusButton(status) {
    const statusButton = document.createElement('button');
    statusButton.textContent = status || 'N/A';

    switch (status) {
        case 'Submit':
            statusButton.style.backgroundColor = '#a435bb';
            statusButton.style.color = 'white';
            break;
        case 'Forwarded':
            statusButton.style.backgroundColor = '#ffc107';
            statusButton.style.color = 'black';
            break;
        case 'Rejected-ReSubmit':
            statusButton.style.backgroundColor = '#da2d2d';
            statusButton.style.color = 'white';
            break;
        case 'Rejected-Close':
            statusButton.style.backgroundColor = '#890000';
            statusButton.style.color = 'white';
            break;
        case 'Approved':
            statusButton.style.backgroundColor = '#28a745';
            statusButton.style.color = 'white';
            break;
        case 'Cross-Verified':
            statusButton.style.backgroundColor = '#007bff';
            statusButton.style.color = 'white';
            break;
        case 'Issued':
            statusButton.style.backgroundColor = '#6c757d';
            statusButton.style.color = 'white';
            break;

        case 'Reviewed':
            statusButton.style.backgroundColor = '#39dd4a';
            statusButton.style.color = 'white';
            break;
        default:
            statusButton.style.backgroundColor = '#6c757d';
            statusButton.style.color = 'white';
    }

    statusButton.style.border = 'none';
    statusButton.style.borderRadius = '4px';
    statusButton.style.padding = '8px 16px';
    statusButton.style.cursor = 'pointer';
    return statusButton;
}

// code to show hide table
var isHidden = true;

function myShowHide() {
    var elements = document.getElementsByClassName("showHideToggle");
    for (var i = 0; i < elements.length; i++) {
        var element = elements[i];
        if (isHidden) {
            element.style.display = "table-cell"; // Show the columns
        } else {
            element.style.display = "none"; // Hide the columns
        }
    }
    isHidden = !isHidden;
}

// Js For Fetch User To Transfer The Authority Of the Application (To Lower Levels)
document.addEventListener('DOMContentLoaded', function () {
    // Variable to hold the current appId
    let currentAppId = '';
    // Current Log In Username
    const username = document.getElementById('username').value;

    // From Level
    const chooseLevel = document.getElementById('chooseLevel');
    // To Level
    const rolesDropdown = document.getElementById('roles');
    // Transfer Button
    const transferButton = document.getElementById('transferButton');
    // Application ID Input Field
    const applicationIdInput = document.getElementById('applicationId');

    // Event listener to update appId and levelEndpoints based on button click
    document.addEventListener('click', function (event) {
        if (event.target && event.target.matches('button[data-agency-name]')) {
            const agencyName = event.target.getAttribute('data-agency-name');
            // Set the agency name in the modal input field
            applicationIdInput.value = agencyName;
            // Store the appId in a variable
            currentAppId = agencyName;

        }
    });

    // Event listener for the level dropdown change
    chooseLevel.addEventListener('change', function () {
        const selectedLevel = this.value;

        // Clear existing options in the roles dropdown
        rolesDropdown.innerHTML = '<option value="">Choose User</option>';

        // Define the levelEndpoints object with all possible endpoints
        const levelEndpoints = {
            'admin': '/data/fetchCe1List/' + currentAppId,
            'ce1': '/data/fetchCe2List/' + username + '/' + currentAppId,
            'ce2': '/data/fetchSeList/' + username + '/' + currentAppId,
            'se': '/data/fetchEeList/' + username + '/' + currentAppId
        };

        // Fetch data for the selected level if it has a corresponding endpoint
        const url = levelEndpoints[selectedLevel];
        if (url) {
            fetchRolesList(url);
        }
    });

    function fetchRolesList(url) {
        fetch(url)
            .then(response => response.json())
            .then(data => {
                // Clear the dropdown options except for the default option
                rolesDropdown.innerHTML = '<option value="">Choose User</option>';

                // Assuming data is a list of strings
                data.forEach(role => {
                    const option = document.createElement('option');
                    option.value = role; // Set the value attribute to the string
                    option.textContent = role; // Set the display text to the string
                    rolesDropdown.appendChild(option);
                });
            })
            .catch(error => console.error('Error fetching roles list:', error));
    }

    // Event listener for the transfer button click
    transferButton.addEventListener('click', function () {
        const selectedLevel = chooseLevel.value;
        const selectedRole = rolesDropdown.value;

        if (selectedLevel && selectedRole) {
            // Prepare the data to send to the backend
            const applicationId = applicationIdInput.value;
            const transferRemark = document.getElementById("transferRemark").value;

            console.log("Transfer Remark ",transferRemark);


            const requestData = new URLSearchParams({
                toUser: selectedRole,
                applicationId: applicationId,
                transferRemark : transferRemark
            }).toString();

            // Send the data to the backend using fetch
            fetch(`/data/applicationTransferToUsers`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: requestData
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(result => {
                    // Handle success
                    var modal = bootstrap.Modal.getInstance(document.getElementById('staticBackdrop'));
                    modal.hide();

                    // Show success toast using SweetAlert2
                    Swal.fire({
                        toast: true,
                        position: 'bottom-end',
                        icon: 'success',
                        title: 'Application transferred successfully!',
                        showConfirmButton: false,
                        timer: 2000,
                        timerProgressBar: true
                    }).then(() => {
                        // Reload the page after the toast disappears
                        window.location.reload();
                    });
                })
                .catch(error => console.error('Error:', error));
        } else {
            console.log('Please select both a level and a role before transferring.');
        }
    });
});

// Js For Fetch User To Transfer The Authority Of the Application (To Upper Levels)
document.addEventListener('DOMContentLoaded', function () {
    // Variable to hold the current appId
    let currentAppId = '';
    // Current Log In Username
    const username = document.getElementById('username').value;

    // From Level
    const chooseLevel = document.getElementById('chooseUpperLevel');
    // To Level
    const rolesDropdown = document.getElementById('upperRoles');
    // Transfer Button
    const transferButton = document.getElementById('transferButton');
    // Application ID Input Field
    const applicationIdInput = document.getElementById('applicationId');

    // Event listener to update appId and levelEndpoints based on button click
    document.addEventListener('click', function (event) {
        if (event.target && event.target.matches('button[data-agency-name]')) {
            const agencyName = event.target.getAttribute('data-agency-name');
            // Set the agency name in the modal input field
            applicationIdInput.value = agencyName;
            // Store the appId in a variable
            currentAppId = agencyName;

        }
    });

    // Event listener for the level dropdown change
    chooseLevel.addEventListener('change', function () {
        const selectedLevel = this.value;

        // Clear existing options in the roles dropdown
        rolesDropdown.innerHTML = '<option value="">Choose User</option>';

        // Define the levelEndpoints object with all possible endpoints
        const levelEndpoints = {
            'ee': `/data/presetSeFromApplicationByAppId/${currentAppId}`,
            'se': `/data/presetCe2FromApplicationByAppId/${currentAppId}`,
            'ce2': `/data/presetCe1FromApplicationByAppId/${currentAppId}`,
            'ce1': `/data/presetAdminFromApplicationByAppId/${currentAppId}`,
        };

        // Fetch data for the selected level if it has a corresponding endpoint
        const url = levelEndpoints[selectedLevel];
        if (url) {
            fetchRolesList(url);
        }
    });

    function fetchRolesList(url) {

        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.text(); // Parse response as text
            })
            .then(text => {
                // Split the text into an array based on the delimiter
                // Assuming roles are comma-separated
                const roles = text.split(',').map(role => role.trim());

                // Clear the dropdown options except for the default option
                rolesDropdown.innerHTML = '<option value="">Choose User</option>';

                // Populate the dropdown with roles
                roles.forEach(role => {
                    if (role) { // Check if role is not an empty string
                        const option = document.createElement('option');
                        option.value = role; // Set the value attribute to the role
                        option.textContent = role; // Set the display text to the role
                        rolesDropdown.appendChild(option);
                    }
                });
            })
            .catch(error => console.error('Error fetching roles list:', error));
    }


    // Event listener for the transfer button click
    transferButton.addEventListener('click', function () {
        const selectedLevel = chooseLevel.value;
        const selectedRole = rolesDropdown.value;

        if (selectedLevel && selectedRole) {
            // Prepare the data to send to the backend
            const applicationId = applicationIdInput.value;
            const transferRemark = document.getElementById("transferRemark").value;

            const requestData = new URLSearchParams({
                toUser: selectedRole,
                applicationId: applicationId,
                transferRemark : transferRemark
            }).toString();

            // Send the data to the backend using fetch
            fetch(`/data/applicationTransferToUsers`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: requestData
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(result => {
                    // Handle success
                    var modal = bootstrap.Modal.getInstance(document.getElementById('staticBackdrop'));
                    modal.hide();

                    // Show success toast using SweetAlert2
                    Swal.fire({
                        toast: true,
                        position: 'bottom-end',
                        icon: 'success',
                        title: 'Application transferred successfully!',
                        showConfirmButton: false,
                        timer: 2000,
                        timerProgressBar: true
                    }).then(() => {
                        // Reload the page after the toast disappears
                        window.location.reload();
                    });
                })
                .catch(error => console.error('Error:', error));
        } else {
            console.log('Please select both a level and a role before transferring.');
        }
    });
});

// Js For Check The Application Forwarded Status = Forwarding
document.addEventListener('DOMContentLoaded', function () {

    const role = document.getElementById('role').value;

    if (role !== "deputyAdmin") {
        let currentAppId = ''; // Initialize a variable to store the current application ID

        const deputyAdminSection = document.getElementById('deputyAdminSection');
        const deputyAdmin = document.getElementById("deputyAdmin");
        const applicationIdInput = document.getElementById('applicationId'); // Hidden input element for application ID

        // Event listener to update appId based on button click
        document.addEventListener('click', function (event) {
            if (event.target && event.target.matches('button[data-agency-name]')) {
                const agencyName = event.target.getAttribute('data-agency-name');
                // Store the application ID in the input field and the currentAppId variable
                applicationIdInput.value = agencyName;
                currentAppId = agencyName;

                // Check the application status after setting the current application ID
                checkApplicationStatus(currentAppId);
            }
        });

        function checkApplicationStatus(appId) {
            if (!appId) return; // If no application ID, don't proceed

            fetch(`/data/checkApplicationStatusForwarded/${appId}`) // Adjust the endpoint as needed
                .then(response => response.json())
                .then(data => {
                    if (data.data.isTransferred) { // Adjust based on your response structure
                        deputyAdminSection.style.display = 'block'; // Show the section
                    } else {
                        deputyAdminSection.style.display = 'none'; // Hide the section
                    }
                })
                .catch(error => console.error('Error fetching application status:', error));
        }


        // Initial check if application ID is already set on page load
        if (applicationIdInput.value) {
            currentAppId = applicationIdInput.value;
            checkApplicationStatus(currentAppId);
        }

        // Event listener for the transfer button click
        transferButton.addEventListener('click', function () {
            const selectedRole = deputyAdmin.value;

            if (selectedRole) {
                // Prepare the data to send to the backend
                const applicationId = applicationIdInput.value;
                const transferRemark = document.getElementById("transferRemark").value;

                const requestData = new URLSearchParams({
                    toUser: selectedRole,
                    applicationId: applicationId,
                    transferRemark : transferRemark
                }).toString();

                // Send the data to the backend using fetch
                fetch(`/data/applicationTransferToUsers`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: requestData
                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.json();
                    })
                    .then(result => {
                        // Handle success
                        var modal = bootstrap.Modal.getInstance(document.getElementById('staticBackdrop'));
                        modal.hide();

                        // Show success toast using SweetAlert2
                        Swal.fire({
                            toast: true,
                            position: 'bottom-end',
                            icon: 'success',
                            title: 'Application transferred successfully!',
                            showConfirmButton: false,
                            timer: 2000,
                            timerProgressBar: true
                        }).then(() => {
                            // Reload the page after the toast disappears
                            window.location.reload();
                        });
                    })
                    .catch(error => console.error('Error:', error));
            } else {
                console.log('Please select both a level and a role before transferring.');
            }
        });
    }

});

// JS For Transfer The Authority Of the Application (To Sub Admin)
document.addEventListener('DOMContentLoaded', function () {

    // Get role and username from the page
    const role = document.getElementById('role').value;
    const username = document.getElementById('username').value;

    if (role === "deputyAdmin") {
        // Variable to hold the current appId
        let currentAppId = '';

        // Transfer Button and Application ID Input Field
        const transferButton = document.getElementById('transferButton');
        const applicationIdInput = document.getElementById('applicationId');

        // Event listener to update appId based on button click
        document.addEventListener('click', function (event) {
            if (event.target && event.target.matches('button[data-agency-name]')) {
                const agencyName = event.target.getAttribute('data-agency-name');
                // Set the agency name in the modal input field and store the appId
                applicationIdInput.value = agencyName;
                currentAppId = agencyName;

            }
        });

        // Event listener for the transfer button click
        transferButton.addEventListener('click', function () {
            const subAdmin = document.getElementById('subAdmin').value;

            if (subAdmin) {
                // Prepare the data to send to the backend
                const applicationId = applicationIdInput.value;
                const transferRemark = document.getElementById("transferRemark").value;

                const requestData = new URLSearchParams({
                    toUser: subAdmin,
                    applicationId: applicationId,
                    transferRemark : transferRemark
                }).toString();

                // Send the data to the backend using fetch
                fetch(`/data/applicationTransferToUsers`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: requestData
                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.json();
                    })
                    .then(result => {
                        // Handle success
                        const modal = bootstrap.Modal.getInstance(document.getElementById('staticBackdrop'));
                        modal.hide();

                        // Show success toast using SweetAlert2
                        Swal.fire({
                            toast: true,
                            position: 'bottom-end',
                            icon: 'success',
                            title: 'Application transferred successfully!',
                            showConfirmButton: false,
                            timer: 2000,
                            timerProgressBar: true
                        }).then(() => {
                            // Reload the page after the toast disappears
                            window.location.reload();
                        });
                    })
                    .catch(error => console.error('Error:', error));
            } else {
                console.error('No sub-admin selected');
            }
        });
    } else {
        console.log('User is not a deputy admin.');
    }
});

// JS For Transfer The Authority Of the Application (From Sub Admin To Deputy Admin)
document.addEventListener('DOMContentLoaded', function () {

    // Get role and username from the page
    const role = document.getElementById('role').value;
    const username = document.getElementById('username').value;

    if (role === "subAdmin") {
        // Variable to hold the current appId
        let currentAppId = '';

        // Transfer Button and Application ID Input Field
        const transferButton = document.getElementById('transferButton');
        const applicationIdInput = document.getElementById('applicationId');

        // Event listener to update appId based on button click
        document.addEventListener('click', function (event) {
            if (event.target && event.target.matches('button[data-agency-name]')) {
                const agencyName = event.target.getAttribute('data-agency-name');
                // Set the agency name in the modal input field and store the appId
                applicationIdInput.value = agencyName;
                currentAppId = agencyName;

            }
        });

        // Event listener for the transfer button click
        transferButton.addEventListener('click', function () {

            const deputyAdmin = document.getElementById('deputyAdminValue').value;

            if (deputyAdmin) {
                // Prepare the data to send to the backend
                const applicationId = applicationIdInput.value;
                const transferRemark = document.getElementById("transferRemark").value;

                const requestData = new URLSearchParams({
                    toUser: deputyAdmin,
                    applicationId: applicationId,
                    transferRemark : transferRemark
                }).toString();

                // Send the data to the backend using fetch
                fetch(`/data/applicationTransferToUsers`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: requestData
                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.json();
                    })
                    .then(result => {
                        // Handle success
                        const modal = bootstrap.Modal.getInstance(document.getElementById('staticBackdrop'));
                        modal.hide();

                        // Show success toast using SweetAlert2
                        Swal.fire({
                            toast: true,
                            position: 'bottom-end',
                            icon: 'success',
                            title: 'Application transferred successfully!',
                            showConfirmButton: false,
                            timer: 2000,
                            timerProgressBar: true
                        }).then(() => {
                            // Reload the page after the toast disappears
                            window.location.reload();
                        });
                    })
                    .catch(error => console.error('Error:', error));
            }
        });
    }
});

// Js For Check The Application Forwarded Status = Cross Verification Or Not
document.addEventListener('DOMContentLoaded', function () {

    const role = document.getElementById('role').value;

    if (role === "deputyAdmin") {
        let currentAppId = ''; // Initialize a variable to store the current application ID

        const adminSection = document.getElementById('adminSection');
        const admin = document.getElementById("admin");
        const applicationIdInput = document.getElementById('applicationId'); // Hidden input element for application ID

        // Event listener to update appId based on button click
        document.addEventListener('click', function (event) {
            if (event.target && event.target.matches('button[data-agency-name]')) {
                const agencyName = event.target.getAttribute('data-agency-name');
                // Store the application ID in the input field and the currentAppId variable
                applicationIdInput.value = agencyName;
                currentAppId = agencyName;

                // Check the application status after setting the current application ID
                checkApplicationStatus(currentAppId);
            }
        });

        function checkApplicationStatus(appId) {
            if (!appId) return; // If no application ID, don't proceed

            fetch(`/data/checkApplicationStatusCrossVerification/${appId}`) // Adjust the endpoint as needed
                .then(response => response.json())
                .then(data => {
                    if (data.data.isTransferred) { // Adjust based on your response structure
                        adminSection.style.display = 'block'; // Show the section
                    } else {
                        adminSection.style.display = 'none'; // Hide the section
                    }
                })
                .catch(error => console.error('Error fetching application status:', error));
        }


        // Initial check if application ID is already set on page load
        if (applicationIdInput.value) {
            currentAppId = applicationIdInput.value;
            checkApplicationStatus(currentAppId);
        }

        // Event listener for the transfer button click
        transferButton.addEventListener('click', function () {
            const selectedRole = admin.value;

            if (selectedRole) {
                // Prepare the data to send to the backend
                const applicationId = applicationIdInput.value;
                const transferRemark = document.getElementById("transferRemark").value;

                const requestData = new URLSearchParams({
                    toUser: selectedRole,
                    applicationId: applicationId,
                    transferRemark : transferRemark
                }).toString();

                // Send the data to the backend using fetch
                fetch(`/data/applicationTransferToUsers`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: requestData
                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.json();
                    })
                    .then(result => {
                        // Handle success
                        var modal = bootstrap.Modal.getInstance(document.getElementById('staticBackdrop'));
                        modal.hide();

                        // Show success toast using SweetAlert2
                        Swal.fire({
                            toast: true,
                            position: 'bottom-end',
                            icon: 'success',
                            title: 'Application transferred successfully!',
                            showConfirmButton: false,
                            timer: 2000,
                            timerProgressBar: true
                        }).then(() => {
                            // Reload the page after the toast disappears
                            window.location.reload();
                        });
                    })
                    .catch(error => console.error('Error:', error));
            }
        });
    }
});

// Js For Check The Application Forwarded Status = CrossVerified Or Not
document.addEventListener('DOMContentLoaded', function () {

    const role = document.getElementById('role').value;

    const hodSection = document.getElementById('hodSection');
    const hodSectionToggle = document.getElementById('hodSectionToggle');

    if (role === "admin") {
        let currentAppId = ''; // Initialize a variable to store the current application ID

        const hod = document.getElementById("hod");
        const applicationIdInput = document.getElementById('applicationId'); // Hidden input element for application ID
        const transferButton = document.getElementById('transferButton');


        // Event listener to update appId based on button click
        document.addEventListener('click', function (event) {
            if (event.target && event.target.matches('button[data-agency-name]')) {
                const agencyName = event.target.getAttribute('data-agency-name');
                // Store the application ID in the input field and the currentAppId variable
                applicationIdInput.value = agencyName;
                currentAppId = agencyName;

                // Check the application status after setting the current application ID
                checkApplicationStatus(currentAppId);
            }
        });

        function checkApplicationStatus(appId) {
            if (!appId) return; // If no application ID, don't proceed

            fetch(`/data/checkApplicationStatusCrossVerified/${appId}`) // Adjust the endpoint as needed
                .then(response => response.json())
                .then(data => {

                    if (data.data.isTransferred) { // Adjust based on your response structure
                        hodSection.style.display = 'block'; // Show the section
                        hodSectionToggle.style.display = 'block'; // Show the section
                    } else {
                        hodSection.style.display = 'none'; // Hide the section
                        hodSectionToggle.style.display = 'none'; // Show the section
                    }
                })
                .catch(error => console.error('Error fetching application status:', error));
        }


        // Initial check if application ID is already set on page load
        if (applicationIdInput.value) {
            currentAppId = applicationIdInput.value;
            checkApplicationStatus(currentAppId);
        }

        // Event listener for the transfer button click
        transferButton.addEventListener('click', function () {
            const selectedRole = hod.value;

            if (selectedRole) {
                // Prepare the data to send to the backend
                const applicationId = applicationIdInput.value;
                const transferRemark = document.getElementById("transferRemark").value;

                const requestData = new URLSearchParams({
                    toUser: selectedRole,
                    applicationId: applicationId,
                    transferRemark : transferRemark
                }).toString();

                // Send the data to the backend using fetch
                fetch(`/data/applicationTransferToUsers`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: requestData
                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.json();
                    })
                    .then(result => {
                        // Handle success
                        var modal = bootstrap.Modal.getInstance(document.getElementById('staticBackdrop'));
                        modal.hide();

                        // Show success toast using SweetAlert2
                        Swal.fire({
                            toast: true,
                            position: 'bottom-end',
                            icon: 'success',
                            title: 'Application transferred successfully!',
                            showConfirmButton: false,
                            timer: 2000,
                            timerProgressBar: true
                        }).then(() => {
                            // Reload the page after the toast disappears
                            window.location.reload();
                        });
                    })
                    .catch(error => console.error('Error:', error));
            }
        });
    }
});

// JS For Transfer The Authority Of the Application (From HOD To Admin)
document.addEventListener('DOMContentLoaded', function () {

    // Get role and username from the page
    const role = document.getElementById('role').value;
    const username = document.getElementById('username').value;

    if (role === "hod") {
        // Variable to hold the current appId
        let currentAppId = '';

        // Transfer Button and Application ID Input Field
        const transferButton = document.getElementById('transferButton');
        const applicationIdInput = document.getElementById('applicationId');

        // Event listener to update appId based on button click
        document.addEventListener('click', function (event) {
            if (event.target && event.target.matches('button[data-agency-name]')) {
                const agencyName = event.target.getAttribute('data-agency-name');
                // Set the agency name in the modal input field and store the appId
                applicationIdInput.value = agencyName;
                currentAppId = agencyName;

            }
        });

        // Event listener for the transfer button click
        transferButton.addEventListener('click', function () {

            const admin = document.getElementById('admin').value;

            if (admin) {
                // Prepare the data to send to the backend
                const applicationId = applicationIdInput.value;
                const transferRemark = document.getElementById("transferRemark").value;

                const requestData = new URLSearchParams({
                    toUser: admin,
                    applicationId: applicationId,
                    transferRemark : transferRemark
                }).toString();

                // Send the data to the backend using fetch
                fetch(`/data/applicationTransferToUsers`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: requestData
                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.json();
                    })
                    .then(result => {
                        // Handle success
                        const modal = bootstrap.Modal.getInstance(document.getElementById('staticBackdrop'));
                        modal.hide();

                        // Show success toast using SweetAlert2
                        Swal.fire({
                            toast: true,
                            position: 'bottom-end',
                            icon: 'success',
                            title: 'Application transferred successfully!',
                            showConfirmButton: false,
                            timer: 2000,
                            timerProgressBar: true
                        }).then(() => {
                            // Reload the page after the toast disappears
                            window.location.reload();
                        });
                    })
                    .catch(error => console.error('Error:', error));
            }
        });
    }
});


// Js For Check The Application Status = Issuance And Noc Type = Jal Avantan - For Future Use

// document.addEventListener('DOMContentLoaded', function () {
//
//     const role = document.getElementById('role').value;
//
//     if (role === "admin") {
//         let currentAppId = ''; // Initialize a variable to store the current application ID
//
//         const administrationSection = document.getElementById('administrationSection');
//         const administration = document.getElementById("administration");
//         const applicationIdInput = document.getElementById('applicationId'); // Hidden input element for application ID
//
//         // Transfer Button and Application ID Input Field
//         const transferButton = document.getElementById('transferButton');
//
//         // Event listener to update appId based on button click
//         document.addEventListener('click', function (event) {
//             if (event.target && event.target.matches('button[data-agency-name]')) {
//                 const agencyName = event.target.getAttribute('data-agency-name');
//                 // Store the application ID in the input field and the currentAppId variable
//                 applicationIdInput.value = agencyName;
//                 currentAppId = agencyName;
//
//                 // Check the application status after setting the current application ID
//                 checkApplicationStatus(currentAppId);
//             }
//         });
//
//         function checkApplicationStatus(appId) {
//             if (!appId) return; // If no application ID, don't proceed
//
//             fetch(`/data/checkApplicationStatusIssuanceAndNocType/${appId}`) // Adjust the endpoint as needed
//                 .then(response => response.json())
//                 .then(data => {
//                     if (data.data.isTransferred) { // Adjust based on your response structure
//                         administrationSection.style.display = 'block'; // Show the section
//                     } else {
//                         administrationSection.style.display = 'none'; // Hide the section
//                     }
//                 })
//                 .catch(error => console.error('Error fetching application status:', error));
//         }
//
//
//         // Initial check if application ID is already set on page load
//         if (applicationIdInput.value) {
//             currentAppId = applicationIdInput.value;
//             checkApplicationStatus(currentAppId);
//         }
//
//         // Event listener for the transfer button click
//         transferButton.addEventListener('click', function () {
//             const selectedRole = administration.value;
//
//             if (selectedRole) {
//                 // Prepare the data to send to the backend
//                 const applicationId = applicationIdInput.value;
//                 const transferRemark = document.getElementById("transferRemark").value;
//
//                 const requestData = new URLSearchParams({
//                     toUser: selectedRole,
//                     applicationId: applicationId,
//                     transferRemark : transferRemark
//                 }).toString();
//
//                 // Send the data to the backend using fetch
//                 fetch(`/data/applicationTransferToUsers`, {
//                     method: 'POST',
//                     headers: {
//                         'Content-Type': 'application/x-www-form-urlencoded'
//                     },
//                     body: requestData
//                 })
//                     .then(response => {
//                         if (!response.ok) {
//                             throw new Error('Network response was not ok');
//                         }
//                         return response.json();
//                     })
//                     .then(result => {
//                         // Handle success
//                         var modal = bootstrap.Modal.getInstance(document.getElementById('staticBackdrop'));
//                         modal.hide();
//
//                         // Show success toast using SweetAlert2
//                         Swal.fire({
//                             toast: true,
//                             position: 'bottom-end',
//                             icon: 'success',
//                             title: 'Application transferred successfully!',
//                             showConfirmButton: false,
//                             timer: 2000,
//                             timerProgressBar: true
//                         }).then(() => {
//                             // Reload the page after the toast disappears
//                             window.location.reload();
//                         });
//                     })
//                     .catch(error => console.error('Error:', error));
//             }
//         });
//     }
// });


