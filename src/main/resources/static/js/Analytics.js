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
        fetch(`/data/AnalyticsForDepartment?financialYear=${finYear}`)  // Pass financial year as a query parameter
            .then(response => response.json())
            .then(response => {
                const data = response.data;
                console.log(`Dashboard`,data);

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
                                                console.log("Selected Category:", selectedCategory,finYear);
                                                updateCE2Dashboard(selectedCategory, finYear);
                                            });
                                        });
                                    });
                                }
                            }
                        },
                        title: {
                            text: 'CE 1 Regions'
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
                        },{
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
                            name: 'Process Applications',
                            data: processApplications
                        }
                    ]
                });
            })
            .catch(error => console.error('Error fetching dashboard stats:', error));

    }


    function updatePieChartweek(finYear){
        fetch(`/data/statsForDepartment?financialYear=${finYear}`)  // Pass financial year as a query parameter
            .then(response => response.json())
            .then(response => {
                const data = response.data;

                // console.log(data);

                Highcharts.chart('statusPieChartweek', {
                    chart: {
                        plotBackgroundColor: null,
                        plotBorderWidth: null,
                        plotShadow: false,
                        type: 'pie'
                    },
                    title: {
                        text: 'स्थिति वितरण: लंबित लंबित पिछले 7 दिन'
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
                        data: [
                            {
                                name: 'लंबित (पिछले 7 दिन)',
                                y: data.pending7Days || 0,  // Dynamic value from the API data
                                sliced: true,
                                selected: true
                            },

                            {
                                name: 'कार्रवाई नहीं की गई (पिछले 7 दिन)',
                                y: data.inProgressApplications || 0  // Dynamic value from the API data
                            },

                        ]
                    }]
                });

            })
            .catch(error => console.error('Error fetching dashboard stats:', error));
    }

    function updatePieChartquater(finYear){
        fetch(`/data/statsForDepartment?financialYear=${finYear}`)  // Pass financial year as a query parameter
            .then(response => response.json())
            .then(response => {
                const data = response.data;

                // console.log(data);

                Highcharts.chart('statusPieChartquater', {
                    chart: {
                        plotBackgroundColor: null,
                        plotBorderWidth: null,
                        plotShadow: false,
                        type: 'pie'
                    },
                    title: {
                        text: 'स्थिति वितरण: लंबित लंबित पिछले 15 दिन'
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
                        data: [

                            {
                                name: 'लंबित (पिछले 15 दिन)',
                                y: data.pending15Days || 0  // Dynamic value from the API data
                            },

                            {
                                name: 'कार्रवाई नहीं की गई (पिछले 15 दिन)',
                                y: data.rejectedApplications || 0  // Dynamic value from the API data
                            }
                        ]
                    }]
                });

            })
            .catch(error => console.error('Error fetching dashboard stats:', error));
    }

    // Initial dashboard update with the current financial year
    updateDashboard(selectElement.value);
    UpdateTotalTable(selectElement.value);
    updatePieChartweek(selectElement.value);
    updatePieChartquater(selectElement.value);

    // Add an event listener to update the dashboard when a new financial year is selected
    selectElement.addEventListener('change', (event) => {
        const selectedFinYear = event.target.value;
        updateDashboard(selectedFinYear);
        UpdateTotalTable(selectedFinYear);
        updatePieChartweek(selectedFinYear);
        updatePieChartquater(selectedFinYear);
    });
});


// Initialize the charts after the DOM is fully loaded
document.addEventListener('DOMContentLoaded', function () {
    updateDateTime();
    setInterval(updateDateTime, 1000); // Update every second

    // Initialize charts only if their respective containers are present in the DOM


    if (document.getElementById('ce2')) {
        Highcharts.chart('ce2', {
            chart: {
                type: 'column'
            },
            title: {
                text: 'CE2 Regions'
            },
            xAxis: {
                categories: [
                    'GANGA MEERUT', 'MADHYA GANGA ALIGARH', 'YAMUNA OKHLA'
                ],
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
                data: [5, 14, 11],
                color: '#7cb5ec'
            }, {
                name: 'इन प्रोग्रेस',
                data: [4, 15, 4],
                color: '#434348'
            }, {
                name: 'एप्रूव्ड',
                data: [1, 7, 3],
                color: '#90ed7d'
            }, {
                name: 'रिजेक्टेड',
                data: [0, 3, 3],
                color: '#f7a35c'
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

    if (document.getElementById('se')) {
        Highcharts.chart('se', {
            chart: {
                type: 'column'
            },
            title: {
                text: 'SE Regions'
            },
            xAxis: {
                categories: [
                    'IRRIGATION WORK I', 'GANGA CANAL OPERATION CIRCLE MEERUT', 'IRRIGATION WORK DIVISION MATHURA', 'DRAINAGE CIRCLE ALIGARH'
                ],
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
                data: [4, 3, 1],
                color: '#7cb5ec'
            }, {
                name: 'इन प्रोग्रेस',
                data: [12, 15, 4],
                color: '#434348'
            }, {
                name: 'एप्रूव्ड',
                data: [10, 7, 3],
                color: '#90ed7d'
            }, {
                name: 'रिजेक्टेड',
                data: [8, 3, 3],
                color: '#f7a35c'
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

    if (document.getElementById('ee')) {
        Highcharts.chart('ee', {
            chart: {
                type: 'column'
            },
            title: {
                text: 'EE Regions'
            },
            xAxis: {
                categories: [
                    'IRRIGATION WORK I', 'GANGA CANAL OPERATION CIRCLE MEERUT', 'IRRIGATION WORK DIVISION MATHURA', 'DRAINAGE CIRCLE ALIGARH'
                ],
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
                data: [4, 3, 1],
                color: '#7cb5ec'
            }, {
                name: 'इन प्रोग्रेस',
                data: [12, 15, 4],
                color: '#434348'
            }, {
                name: 'एप्रूव्ड',
                data: [10, 7, 3],
                color: '#90ed7d'
            }, {
                name: 'रिजेक्टेड',
                data: [8, 3, 3],
                color: '#f7a35c'
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


// to show data of CE 2 in admin
function updateCE2Dashboard(ce1Region, finYear) {
    console.log(`ce1Region finYear`,ce1Region ,finYear);

    fetch(`/data/AnalyticsForCE2?ce1Region=${ce1Region}&financialYear=${finYear}`)
        .then(response => response.json())
        .then(response => {
            const data = response.data;
            console.log("AnalyticsForCE2",data);

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

            if (document.getElementById('AnalyticsForCE2')) {
                document.getElementById("AnalyticsForCE2").style.display="block";
                Highcharts.chart('AnalyticsForCE2', {
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
                                            console.log("Selected Category:", selectedCategory,finYear);
                                            updateSeDashboard(selectedCategory, finYear);
                                        });
                                    });
                                });
                            }
                        }
                    },

                    title: {
                        text: `CE2 Regions for ${ce1Region}`
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
                    },{
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
        .catch(error => console.error('Error fetching CE2 data:', error));
}


function updateSeDashboard(ce2Region, finYear) {
    console.log(`ce1Region finYear`,ce2Region ,finYear);

    fetch(`/data/AnalyticsForSE?ce2Region=${ce2Region}&financialYear=${finYear}`)
        .then(response => response.json())
        .then(response => {
            const data = response.data;
            console.log("AnalyticsForCE2",data);

            const categories = [];
            const totalApplications = [];
            const inProgress = [];
            const approved = [];
            const rejected = [];

            for (const [region, counts] of Object.entries(data)) {
                categories.push(region);
                totalApplications.push(counts.totalApplications);
                inProgress.push(counts.reviewedApplications);
                approved.push(counts.rejectedReSubmitApplications);
                rejected.push(counts.rejectedCloseApplications);
            }

            if (document.getElementById('AnalyticsForSE')) {
                document.getElementById("AnalyticsForSE").style.display="block";
                Highcharts.chart('AnalyticsForSE', {
                    chart: {
                        type: 'column',
                        // events: {
                        //     load: function() {
                        //         // Add click event listener to each point in the series
                        //         const chart = this;
                        //         chart.series.forEach(series => {
                        //             series.points.forEach(point => {
                        //                 point.graphic.element.addEventListener('click', function() {
                        //                     // Get the category of the clicked point
                        //                     const selectedCategory = point.category;
                        //                     console.log("Selected Category:", selectedCategory,finYear);
                        //                     updateEEDashboard(selectedCategory, finYear);
                        //                 });
                        //             });
                        //         });
                        //     }
                        // }
                    },

                    title: {
                        text: `CE2 Regions for ${ce2Region}`
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
                        name: 'इन प्रोग्रेस',
                        data: inProgress,
                        color: '#434348'
                    }, {
                        name: 'एप्रूव्ड',
                        data: approved,
                        color: '#90ed7d'
                    }, {
                        name: 'रिजेक्टेड',
                        data: rejected,
                        color: '#f7a35c'
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
        .catch(error => console.error('Error fetching CE2 data:', error));
}





