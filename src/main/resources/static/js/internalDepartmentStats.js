
function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    sidebar.classList.toggle('closed');
}

function showTable(tableId) {

    const tables = document.querySelectorAll('.internalStats-table');
    tables.forEach(table => table.style.display = 'none');


    const tableToShow = document.getElementById(tableId);
    tableToShow.style.display = 'table';
}


document.addEventListener("DOMContentLoaded", function () {

    if(role==='admin'){
        showTable('chiefEngineer1Table');
    }

    else if(role==='ce1'){
        showTable('chiefEngineer1Table');
    }

    else if(role==='ce2'){
        showTable('chiefEngineer2Table');
    }

    else if(role==='se'){
        showTable('superintendentEngineerTable');

    }
    else if(role==='ee'){
        showTable('executiveEngineer');

    }

});



    document.addEventListener('DOMContentLoaded', () => {

        const url = '/data/internalDepartmentStatistics';

        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                // console.log('Data fetched successfully  data.data.ce2List :', data.data.eeList);
                // console.log('Data fetched successfully Length :', data.data.eeList.length);

                // for CE2 list


                if(role === "admin"){

                    showDataForCe1List(data.data.ce1List);
                    showDataForCe2List(data.data.ce2List);
                    showDataForSeList(data.data.seList)
                    showDataForeeList(data.data.eeList)
                }
                if(role === "ce1"){
                    showDataForCe1List(data.data.ce1List);
                    showDataForCe2List(data.data.ce2List);
                    showDataForSeList(data.data.seList)
                    showDataForeeList(data.data.eeList)
                }
                if(role === "ce2"){
                    showDataForCe2List(data.data.ce2List);
                    showDataForSeList(data.data.seList)
                    showDataForeeList(data.data.eeList)
                }

                if(role === "se"){
                    showDataForSeList(data.data.seList)
                    showDataForeeList(data.data.eeList)
                }

                if(role === "ee"){
                    showDataForeeList(data.data.eeList)
                }

            })
            .catch(error => {
                console.error('There was a problem with the fetch operation:', error);
            });
    });

        function showDataForCe1List(data){

            var ce1TableList = document.getElementById("cheifEngineer1Table").getElementsByTagName("tbody")[0];
            ce1TableList.innerHTML='';

            data.forEach(ce1List=>{

                var row = document.createElement('tr');

                row.innerHTML=`
                    <td>${ce1List.to_user}</td>
                     <td>${ce1List.ranked ? ce1List.ranked:'NOT APPLICABLE'}</td>
                    <td>${ce1List.avg_days_to_close}</td>
        
                    `;

                ce1TableList.appendChild(row);
            })


        }

        function showDataForCe2List(data){

            var ce2TableList = document.getElementById("cheifEngineer2Table").getElementsByTagName("tbody")[0];
            ce2TableList.innerHTML='';

            data.forEach(ceList=>{

                var row = document.createElement('tr');

                row.innerHTML=`
                    <td>${ceList.to_user}</td>
                     
                      <td>${ceList.ranked ? ceList.ranked:'NOT APPLICABLE'}</td>
                    <td>${ceList.avg_days_to_close}</td>
        
                    `;

                ce2TableList.appendChild(row);
            })


        }


        function showDataForSeList(data){

            var seTableList = document.getElementById("seTable").getElementsByTagName("tbody")[0];
            seTableList.innerHTML='';

            data.forEach(seList=>{

                var row = document.createElement('tr');

                row.innerHTML=`
                    <td>${seList.to_user}</td> 
                      <td>${seList.ranked ? seList.ranked:'NOT APPLICABLE'}</td>
                    <td>${seList.avg_days_to_close}</td>
        
                    `;

                seTableList.appendChild(row);
            })


        }


        function showDataForeeList(data){

            var eeTableList = document.getElementById("executiveEngineerTable").getElementsByTagName("tbody")[0];
            eeTableList.innerHTML='';

            data.forEach(eeList=>{

                var row = document.createElement('tr');

                row.innerHTML=`
                    <td>${eeList.to_user}</td> 
                      <td>${eeList.ranked ? eeList.ranked:'NOT APPLICABLE'}</td>
                    <td>${eeList.avg_days_to_close}</td>
        
                    `;

                eeTableList.appendChild(row);
            })


        }



    document.addEventListener('DOMContentLoaded', function () {
        const url = '/data/internalDepartmentStatistics';

        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                // Extract the lists from the response data
                const ce1List = data.data.ce1List;
                const ce2List = data.data.ce2List;
                const seList = data.data.seList;
                const eeList = data.data.eeList;

                // // Show data in tables (if needed)
                // showDataForCe2List(ce2List);
                // showDataForSeList(seList);
                // showDataForeeList(eeList);


                if(role === "admin"){

                    // Update Highcharts with fetched data
                    updateHighcharts('highchartsContainerce1', ce1List);
                    updateHighcharts('highchartsContainer', ce2List);
                    updateHighcharts('highchartsContainer1', seList);
                    updateHighcharts('highchartsContainer2', eeList);
                }
                if(role === "ce1"){
                    updateHighcharts('highchartsContainerce1', ce1List);
                    updateHighcharts('highchartsContainer', ce2List);
                    updateHighcharts('highchartsContainer1', seList);
                    updateHighcharts('highchartsContainer2', eeList);
                }
                if(role === "ce2"){
                    updateHighcharts('highchartsContainer', ce2List);
                    updateHighcharts('highchartsContainer1', seList);
                    updateHighcharts('highchartsContainer2', eeList);
                }

                if(role === "se"){
                    updateHighcharts('highchartsContainer1', seList);
                    updateHighcharts('highchartsContainer2', eeList);
                }

                if(role === "ee"){
                    updateHighcharts('highchartsContainer2', eeList);
                }




            })
            .catch(error => {
                console.error('There was a problem with the fetch operation:', error);
            });

        function updateHighcharts(containerId, dataList) {
            const categories = dataList.map(item => item.to_user);
            const data = dataList.map(item => item.avg_days_to_close);

            Highcharts.chart(containerId, {
                chart: {
                    type: 'column',
                    zoomType: 'x',
                    scrollablePlotArea: {
                        minWidth: 2000,
                        scrollPositionX: 0
                    }
                },
                title: {
                    text: 'Average Time Taken to Close Application'
                },
                xAxis: {
                    categories: categories,
                    crosshair: true,
                    scrollbar: {
                        enabled: true
                    }
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: 'Average Time (Days)'
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                    pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                        '<td style="padding:0"><b>{point.y} days</b></td></tr>',
                    footerFormat: '</table>',
                    shared: true,
                    useHTML: true
                },
                plotOptions: {
                    column: {
                        pointPadding: 0.2,
                        borderWidth: 0
                    }
                },
                series: [{
                    name: 'Avg Time Taken',
                    data: data,
                    color: 'green' // Set color as needed
                }]
            });
        }

    });

