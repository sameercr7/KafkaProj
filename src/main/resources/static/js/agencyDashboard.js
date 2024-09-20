// JavaScript to Populate Financial Year Dropdown
const selectElement = document.getElementById('financial-year-select');
const currentYear = new Date().getFullYear();
const currentMonth = new Date().getMonth();

// Determine the current financial year
const currentFinYear = currentMonth >= 3 ? `${currentYear}-${currentYear + 1}` : `${currentYear - 1}-${currentYear}`;

// Populate dropdown with financial years
for (let i = 0; i < 5; i++) {
    const startYear = currentYear - i - (currentMonth >= 3 ? 0 : 1);
    const endYear = startYear + 1;
    const finYear = `${startYear}-${endYear}`;

    const optionElement = document.createElement('option');
    optionElement.value = finYear;
    optionElement.textContent = finYear;

    if (finYear === currentFinYear) {
        optionElement.selected = true;
    }

    selectElement.appendChild(optionElement);
}

// JavaScript for Get Stats Details For Dashboard For Agency
document.addEventListener('DOMContentLoaded', () => {

    function updateDashboard(finYear) {
        fetch(`/data/statsForAgency?financialYear=${finYear}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(response => {
                const data = response.data;
                const stats = data.stats;

                // Update HTML elements with the stats data
                document.getElementById('totalApplicationsAgency').textContent = stats.totalApplications;
                document.getElementById('inProgressApplicationsAgency').textContent = stats.inProgressApplications;
                document.getElementById('approvedApplicationsAgency').textContent = stats.approvedApplications;
                document.getElementById('rejectedApplicationsAgency').textContent = stats.rejectedApplications;
                document.getElementById('pendingApplicationsAgency').textContent = stats.pendingApplications;
                document.getElementById('certificateIssuedAgency').textContent = stats.certificateIssued;
                document.getElementById('resubmittedApplicationsAgency').textContent = stats.reSubmitted;

                Highcharts.chart('applicationsChart', {
                    chart: {
                        type: 'column'
                    },
                    title: {
                        text: 'आवेदन'
                    },
                    xAxis: {
                        categories: [
                            'कुल आवेदन',
                            'प्रगति में',
                            'स्वीकृत',
                            'लंबित',
                            'प्रमाणपत्र प्रदान',
                            'पुनः दाखिल',
                            'अस्वीकृत'
                        ],
                        crosshair: true,
                        labels: {
                            style: {
                                fontWeight: 'bold',
                                fontSize: '14px'
                            }
                        }
                    },
                    yAxis: {
                        min: 0,
                        title: {
                            text: 'आवेदन',
                            style: {
                                fontWeight: 'bold',
                                fontSize: '14px'
                            }
                        },
                        labels: {
                            formatter: function () {
                                return this.value; // Display labels without decimals
                            }
                        }
                    },
                    tooltip: {
                        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                        pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                            '<td style="padding:0"><b>{point.y:.0f}</b></td></tr>', // No decimal places
                        footerFormat: '</table>',
                        shared: true,
                        useHTML: true
                    },
                    plotOptions: {
                        column: {
                            pointPadding: 0.2,
                            borderWidth: 0,
                            dataLabels: {
                                enabled: true,
                                format: '{y:.0f}' // No decimal places in data labels
                            }
                        }
                    },
                    series: [{
                        name: 'आवेदन',
                        data: [
                            stats.totalApplications, // Total Applications
                            stats.inProgressApplications, // In Progress
                            stats.approvedApplications, // Approved
                            stats.pendingApplications, // Pending
                            stats.certificateIssued, // Certificate Issued
                            stats.reSubmitted, // Resubmitted
                            stats.rejectedApplications // Rejected
                        ]
                    }]
                });


            })
            .catch(error => console.error('Error fetching dashboard stats:', error));
    }

    // Initial dashboard update with the current financial year
    updateDashboard(selectElement.value);

    // Add an event listener to update the dashboard when a new financial year is selected
    selectElement.addEventListener('change', (event) => {
        const selectedFinYear = event.target.value;
        updateDashboard(selectedFinYear);
    });
});

// JavaScript for Dynamically Setting the Link with Current Dropdown Value of Fin Year
document.addEventListener('DOMContentLoaded', () => {
    const selectElement = document.getElementById('financial-year-select');
    const agencyLink = document.getElementById('agency-application-link');

    // Function to construct the URL with the selected financial year
    function getLinkWithFinYear() {
        // Get the selected financial year from the dropdown
        const selectedFinYear = selectElement.value;

        // Construct the URL with the financial year as a query parameter
        const baseUrl = '/noc/agency_application';
        const urlWithParams = `${baseUrl}?finYear=${encodeURIComponent(selectedFinYear)}`;

        return urlWithParams;
    }

    // Function to initialize the link
    function initializeLink() {
        // Set the initial href attribute of the link
        agencyLink.href = getLinkWithFinYear();
    }

    // Initialize the link with the current financial year on page load
    initializeLink();

    // Add an event listener to the dropdown to update the link when the value changes
    selectElement.addEventListener('change', () => {
        // Update the href attribute of the link when the dropdown value changes
        agencyLink.href = getLinkWithFinYear();
    });

    // Add an event listener to the link to log its href when clicked (for debugging)
    agencyLink.addEventListener('click', () => {
    });
});







