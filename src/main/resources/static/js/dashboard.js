// JavaScript to Populate Financial Year Dropdown
const selectElement = document.getElementById('financial-year-select');
const currentYear = new Date().getFullYear();
const currentMonth = new Date().getMonth();

// Determine the current financial year
const currentFinYear = currentMonth >= 3 ? `${currentYear}-${currentYear + 1}` : `${currentYear - 1}-${currentYear}`;

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

// JavaScript to Fetch and Update Dashboard Stats
document.addEventListener('DOMContentLoaded', () => {
    function updateDashboard(finYear) {
        fetch(`/data/statsForDepartment?financialYear=${finYear}`)  // Pass financial year as a query parameter
            .then(response => response.json())
            .then(response => {
                const data = response.data;

                // Update HTML elements with the data from the API
                document.getElementById('totalApplications').textContent = data.totalApplications;
                document.getElementById('inProgressApplications').textContent = data.inProgressApplications;
                document.getElementById('approvedApplications').textContent = data.approvedApplications;
                document.getElementById('rejectedApplications').textContent = data.rejectedApplications;
                document.getElementById('pending7Days').textContent = data.pending7Days;
                document.getElementById('pending15Days').textContent = data.pending15Days;

                // document.getElementById('reviewApplication').textContent = data.totalApplications;

            })
            .catch(error => console.error('Error fetching dashboard stats:', error));
    }

    function updateDashboard1(finYear) {

        async function getUserRole() {
            const response = await fetch('/data/currentUserRole'); // Endpoint to get the user role
            const data = await response.json();
            return data.role; // Assuming the response has a `role` field
        }

        getUserRole().then(role => {
            fetch(`/data/AnalyticsForDepartment?financialYear=${finYear}`)  // Pass financial year as a query parameter
                .then(response => response.json())
                .then(response => {
                    const data = response.data;
                    // console.log(data);

                    // Prepare the data for Highcharts
                    const categories = [];
                    const totalApplications = [];
                    const pending = [];
                    const inProcess = [];
                    const approved = [];
                    const rejected = [];

                    for (const [region, counts] of Object.entries(data)) {
                        categories.push(region);
                        totalApplications.push(counts.totalApplications);
                        pending.push(counts.pendingApplications);
                        inProcess.push(counts.inProgressApplications);
                        approved.push(counts.approvedApplications);
                        rejected.push(counts.rejectedApplications);
                    }

                    const chartTitle = (() => {
                        switch (role) {
                            case 'admin':
                                return 'CE 1 Regions';
                            case 'ce1':
                                return 'CE 2 Regions';
                            case 'ce2':
                                return 'SE Regions';
                            case 'se':
                                return 'EE Regions';
                            default:
                                return 'Regions'; // Default title
                        }
                    })();

                    if (document.getElementById('ce1')) {
                        Highcharts.chart('ce1', {
                            chart: {
                                type: 'column',
                                events: {
                                    load: function() {
                                        // Add click event listener to each point in the series
                                        const chart = this;
                                        chart.series.forEach(series => {
                                            series.points.forEach(point => {
                                                point.graphic.element.addEventListener('click', function() {
                                                    // Get the category of the clicked point
                                                    const selectedCategory = point.category;
                                                    console.log("Selected Category:", selectedCategory, finYear);
                                                    updateCE2Dashboard(selectedCategory, finYear);
                                                });
                                            });
                                        });
                                    }
                                }
                            },
                            title: {
                                text: chartTitle
                            },
                            xAxis: {
                                categories: categories,
                                title: {
                                    text: 'Regions',
                                    style: {
                                        fontSize: '16px'
                                    }
                                }
                            },
                            yAxis: {
                                min: 0,
                                title: {
                                    text: 'Values',
                                    style: {
                                        fontSize: '24px'
                                    }
                                }
                            },
                            series: [{
                                name: 'टोटल एप्लीकेशन',
                                data: totalApplications,
                                color: '#7cb5ec'
                            }, {
                                name: 'लंबित',
                                data: pending,
                                color: '#71717f'
                            }, {
                                name: 'प्रक्रिया',
                                data: inProcess,
                                color: '#ffa940'
                            }, {
                                name: 'स्वीकृत',
                                data: approved,
                                color: '#5aff77'
                            }, {
                                name: 'अस्वीकृत',
                                data: rejected,
                                color: '#ff6969'
                            }],
                            plotOptions: {
                                column: {
                                    pointPadding: 0.2,
                                    borderWidth: 0,
                                    groupPadding: 0.1
                                }
                            }
                        });
                    }
                })
                .catch(error => console.error('Error fetching dashboard stats:', error));
        }).catch(error => console.error('Error fetching user role:', error));
    }




    function UpdateTotalTable(finYear){

        fetch(`/data/AnalyticsForDepartmentTotalApplication?financialYear=${finYear}`)  // Pass financial year as a query parameter
            .then(response => response.json())
            .then(response => {
                const data = response.data;
                // console.log(`AnalyticsForDepartmentTotalApplication`,data);

                const categories = [];
                const approvedApplications = [];
                const pendingApplications = [];
                const rejectedApplications = [];
                const submittedApplications = [];
                const issuedApplications = [];
                const processApplications = [];

                // Iterate over the data object to populate the chart data dynamically
                for (const category in data) {
                    if (data.hasOwnProperty(category)) {
                        // Extract values or default to 0
                        const categoryData = data[category];
                        categories.push(category);
                        approvedApplications.push(categoryData.approvedApplications);
                        pendingApplications.push(categoryData.pendingApplications);
                        rejectedApplications.push(categoryData.rejectedApplications);
                        submittedApplications.push(categoryData.submittedApplications);
                        issuedApplications.push(categoryData.issuedApplications);
                        processApplications.push(categoryData.processApplications);
                    }
                }

                // Create the Highcharts chart with the dynamic data
                Highcharts.chart('nocTypesChart', {
                    chart: {
                        type: 'bar'
                    },
                    title: {
                        text: 'अनापत्ति प्रमाण पत्र'
                    },
                    xAxis: {
                        categories: categories, // Use categories extracted from the API response
                        title: {
                            text: 'अनापत्ति प्रमाण पत्र के प्रकार'
                        }
                    },
                    yAxis: {
                        min: 0,
                        title: {
                            text: 'कुल आवेदन',
                            align: 'high'
                        },
                        labels: {
                            overflow: 'justify'
                        }
                    },
                    tooltip: {
                        valueSuffix: ' आवेदन'
                    },
                    plotOptions: {
                        bar: {
                            dataLabels: {
                                enabled: true
                            }
                        }
                    },
                    legend: {
                        reversed: true
                    },
                    credits: {
                        enabled: false
                    },
                    series: [
                        {
                            name: 'Approved Applications',
                            data: approvedApplications
                        },
                        {
                            name: 'Pending Applications',
                            data: pendingApplications
                        },
                        {
                            name: 'Rejected Applications',
                            data: rejectedApplications
                        },
                        {
                            name: 'Submitted Applications',
                            data: submittedApplications
                        },
                        {
                            name: 'Issued Applications',
                            data: issuedApplications
                        },
                        {
                            name: 'Process Applications',
                            data: processApplications
                        }
                    ]
                });
            })
            .catch(error => console.error('Error fetching dashboard stats:', error));

    }

    // Initial dashboard update with the current financial year
    updateDashboard(selectElement.value);
    updateDashboard1(selectElement.value);
    UpdateTotalTable(selectElement.value);

    // Add an event listener to update the dashboard when a new financial year is selected
    selectElement.addEventListener('change', (event) => {
        const selectedFinYear = event.target.value;
        updateDashboard(selectedFinYear);
        updateDashboard1(selectedFinYear);
        UpdateTotalTable(selectedFinYear);
    });
});

document.addEventListener('DOMContentLoaded', function () {
    Highcharts.chart('nocTypesChart', {
        chart: {
            type: 'bar'
        },
        title: {
            text: 'अनापत्ति प्रमाण पत्र'
        },
        xAxis: {
            categories: [
                'गैस पाइप लाइन',
                'पेयजल पाइप लाइन',
                'पुल',
                'पुलिया',
                'मार्ग निर्माण'
            ],
            title: {
                text: 'अनापत्ति प्रमाण पत्र के प्रकार'
            }
        },
        yAxis: {
            min: 0,
            title: {
                text: 'कुल आवेदन',
                align: 'high'
            },
            labels: {
                overflow: 'justify'
            }
        },
        tooltip: {
            valueSuffix: 'अनापत्ति प्रमाण पत्र'
        },
        plotOptions: {
            bar: {
                dataLabels: {
                    enabled: true
                }
            }
        },
        legend: {
            reversed: true
        },
        credits: {
            enabled: false
        },
        series: [{
            name: 'आवेदन',
            data: [
                120, // Fire NOC
                80,  // Building NOC
                65,  // Pollution NOC
                40,  // Water NOC
                90   // Health NOC
            ]
        }]
    });
});

document.addEventListener('DOMContentLoaded', function () {
    Highcharts.chart('statusPieChart', {
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false,
            type: 'pie'
        },
        title: {
            text: 'स्थिति वितरण: लंबित और कार्रवाई नहीं की गई'
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        accessibility: {
            point: {
                valueSuffix: '%'
            }
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    format: '<b>{point.name}</b>: {point.percentage:.1f} %'
                }
            }
        },
        series: [{
            name: 'प्रतिशत',
            colorByPoint: true,
            data: [{
                name: 'लंबित (पिछले 7 दिन)',
                y: 30,
                sliced: true,
                selected: true
            }, {
                name: 'लंबित (पिछले 15 दिन)',
                y: 40
            }, {
                name: 'कार्रवाई नहीं की गई (पिछले 7 दिन)',
                y: 20
            }, {
                name: 'कार्रवाई नहीं की गई (पिछले 15 दिन)',
                y: 10
            }]
        }]
    });
});



// JavaScript for Dynamically Setting the Link with Current Dropdown Value of Fin Year
document.addEventListener('DOMContentLoaded', () => {
    const selectElement = document.getElementById('financial-year-select');
    const departmentLink = document.getElementById('department-application-link');

    // Function to construct the URL with the selected financial year
    function getLinkWithFinYear() {
        // Get the selected financial year from the dropdown
        const selectedFinYear = selectElement.value;

        // Check if a valid financial year is selected
        if (!selectedFinYear) {
            return null; // Return null if no valid financial year is selected
        }

        // Construct the URL with the financial year as a query parameter
        const baseUrl = '/noc/reviewApplications';
        const urlWithParams = `${baseUrl}?finYear=${encodeURIComponent(selectedFinYear)}`;

        return urlWithParams;
    }

    // Function to initialize the link
    function initializeLink() {
        // Set the initial href attribute of the link
        departmentLink.href = getLinkWithFinYear();
    }

    // Initialize the link with the current financial year on page load
    initializeLink();

    // Add an event listener to the dropdown to update the link when the value changes
    selectElement.addEventListener('change', () => {
        // Update the href attribute of the link when the dropdown value changes
        departmentLink.href = getLinkWithFinYear();
    });

    // Add an event listener to the link to log its href when clicked (for debugging)
    departmentLink.addEventListener('click', () => {
    });
});



