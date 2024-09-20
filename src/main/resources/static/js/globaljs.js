// Function for current date and time
function formatDateTime(date) {
    const options = {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        hour12: true
    };
    return new Intl.DateTimeFormat('en-US', options).format(date);
}

// Update current date and time in all cards
function updateDateTime() {
    const dateTimeElements = document.querySelectorAll('.current-date-time');
    const now = new Date();
    const formattedDateTime = formatDateTime(now);

    dateTimeElements.forEach(element => {
        element.textContent = formattedDateTime;
    });
}

// // Initialize the charts after the DOM is fully loaded
// document.addEventListener('DOMContentLoaded', function () {
//     updateDateTime();
//     setInterval(updateDateTime, 1000); // Update every second
//
//     // Initialize charts only if their respective containers are present in the DOM
//     if (document.getElementById('ce1')) {
//         Highcharts.chart('ce1', {
//             chart: {
//                 type: 'column'
//             },
//             title: {
//                 text: 'CE1 Regions'
//             },
//             xAxis: {
//                 categories: [
//                     'WEST MEERUT', 'SOUTH JHANSI', 'VINDHYACHAL PRAYAGRAJ', 'CENTRAL LUCKNOW',
//                     'EAST GORAKHPUR', 'ROHILKHAND BAREILLY', 'D R LUCKNOW', 'PLANNING LUCKNOW',
//                     'TA PACT LUCKNOW', 'CENTRAL LUCKNOW'
//                 ],
//                 title: {
//                     text: 'Regions',
//                     style: {
//                         fontSize: '16px'
//                     }
//                 }
//             },
//             yAxis: {
//                 min: 0,
//                 title: {
//                     text: 'Values',
//                     style: {
//                         fontSize: '24px'
//                     }
//                 }
//             },
//             series: [{
//                 name: 'टोटल एप्लीकेशन',
//                 data: [30, 25, 10, 15, 20, 18, 45, 50, 12, 22],
//                 color: '#7cb5ec'
//             }, {
//                 name: 'इन प्रोग्रेस',
//                 data: [12, 15, 4, 5, 8, 7, 20, 25, 5, 10],
//                 color: '#434348'
//             }, {
//                 name: 'एप्रूव्ड',
//                 data: [10, 7, 3, 7, 6, 6, 18, 20, 4, 8],
//                 color: '#90ed7d'
//             }, {
//                 name: 'रिजेक्टेड',
//                 data: [8, 3, 3, 3, 6, 5, 7, 5, 3, 4],
//                 color: '#f7a35c'
//             }],
//             plotOptions: {
//                 column: {
//                     pointPadding: 0.2,
//                     borderWidth: 0,
//                     groupPadding: 0.1
//                 }
//             }
//         });
//     }
//
//     if (document.getElementById('ce2')) {
//         Highcharts.chart('ce2', {
//             chart: {
//                 type: 'column'
//             },
//             title: {
//                 text: 'CE2 Regions'
//             },
//             xAxis: {
//                 categories: [
//                     'GANGA MEERUT', 'MADHYA GANGA ALIGARH', 'YAMUNA OKHLA'
//                 ],
//                 title: {
//                     text: 'Regions',
//                     style: {
//                         fontSize: '16px'
//                     }
//                 }
//             },
//             yAxis: {
//                 min: 0,
//                 title: {
//                     text: 'Values',
//                     style: {
//                         fontSize: '24px'
//                     }
//                 }
//             },
//             series: [{
//                 name: 'टोटल एप्लीकेशन',
//                 data: [5, 14, 11],
//                 color: '#7cb5ec'
//             }, {
//                 name: 'इन प्रोग्रेस',
//                 data: [4, 15, 4],
//                 color: '#434348'
//             }, {
//                 name: 'एप्रूव्ड',
//                 data: [1, 7, 3],
//                 color: '#90ed7d'
//             }, {
//                 name: 'रिजेक्टेड',
//                 data: [0, 3, 3],
//                 color: '#f7a35c'
//             }],
//             plotOptions: {
//                 column: {
//                     pointPadding: 0.2,
//                     borderWidth: 0,
//                     groupPadding: 0.1
//                 }
//             }
//         });
//     }
//
//     if (document.getElementById('se')) {
//         Highcharts.chart('se', {
//             chart: {
//                 type: 'column'
//             },
//             title: {
//                 text: 'SE Regions'
//             },
//             xAxis: {
//                 categories: [
//                     'IRRIGATION WORK I', 'GANGA CANAL OPERATION CIRCLE MEERUT', 'IRRIGATION WORK DIVISION MATHURA', 'DRAINAGE CIRCLE ALIGARH'
//                 ],
//                 title: {
//                     text: 'Regions',
//                     style: {
//                         fontSize: '16px'
//                     }
//                 }
//             },
//             yAxis: {
//                 min: 0,
//                 title: {
//                     text: 'Values',
//                     style: {
//                         fontSize: '24px'
//                     }
//                 }
//             },
//             series: [{
//                 name: 'टोटल एप्लीकेशन',
//                 data: [4, 3, 1],
//                 color: '#7cb5ec'
//             }, {
//                 name: 'इन प्रोग्रेस',
//                 data: [12, 15, 4],
//                 color: '#434348'
//             }, {
//                 name: 'एप्रूव्ड',
//                 data: [10, 7, 3],
//                 color: '#90ed7d'
//             }, {
//                 name: 'रिजेक्टेड',
//                 data: [8, 3, 3],
//                 color: '#f7a35c'
//             }],
//             plotOptions: {
//                 column: {
//                     pointPadding: 0.2,
//                     borderWidth: 0,
//                     groupPadding: 0.1
//                 }
//             }
//         });
//     }
//
//     if (document.getElementById('ee')) {
//         Highcharts.chart('ee', {
//             chart: {
//                 type: 'column'
//             },
//             title: {
//                 text: 'EE Regions'
//             },
//             xAxis: {
//                 categories: [
//                     'IRRIGATION WORK I', 'GANGA CANAL OPERATION CIRCLE MEERUT', 'IRRIGATION WORK DIVISION MATHURA', 'DRAINAGE CIRCLE ALIGARH'
//                 ],
//                 title: {
//                     text: 'Regions',
//                     style: {
//                         fontSize: '16px'
//                     }
//                 }
//             },
//             yAxis: {
//                 min: 0,
//                 title: {
//                     text: 'Values',
//                     style: {
//                         fontSize: '24px'
//                     }
//                 }
//             },
//             series: [{
//                 name: 'टोटल एप्लीकेशन',
//                 data: [4, 3, 1],
//                 color: '#7cb5ec'
//             }, {
//                 name: 'इन प्रोग्रेस',
//                 data: [12, 15, 4],
//                 color: '#434348'
//             }, {
//                 name: 'एप्रूव्ड',
//                 data: [10, 7, 3],
//                 color: '#90ed7d'
//             }, {
//                 name: 'रिजेक्टेड',
//                 data: [8, 3, 3],
//                 color: '#f7a35c'
//             }],
//             plotOptions: {
//                 column: {
//                     pointPadding: 0.2,
//                     borderWidth: 0,
//                     groupPadding: 0.1
//                 }
//             }
//         });
//     }
// });