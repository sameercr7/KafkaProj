document.addEventListener('DOMContentLoaded', () => {

    const url = '/data/officerWiseReportTable';


    console.log("role",role);

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            if(role === "ce1"){

                showChiefEngineer2Report(data.data.ce2TableList);
                showSuperIntendentReport(data.data.seTableList);
                showExecutiveEngineerReport(data.data.eeTableList);
            }
            if(role === "ce2"){
                // console.log(data.data.seTableList);
                showSuperIntendentReport(data.data.seTableList);
                showExecutiveEngineerReport(data.data.eeTableList);
            }

            if(role === "se"){
                console.log("data.data.eeTableList",data.data.eeTableList)
                showExecutiveEngineerReport(data.data.eeTableList);
            }
            else{
                showChiefEngineer1Report(data.data.ce1TableList);
                showChiefEngineer2Report(data.data.ce2TableList);
                showSuperIntendentReport(data.data.seTableList);
                showExecutiveEngineerReport(data.data.eeTableList);
            }

        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });
});


function showChiefEngineer1Report(data){
    var chiefEngineer1Report = document.getElementById("chiefEngineer1Table").getElementsByTagName("tbody")[0];
    chiefEngineer1Report.innerHTML ="";

    var serialNumber = 1;
    data.forEach(chiefEngineer1=>{

        var row = document.createElement("tr");

        row.innerHTML=`
                    <td >${serialNumber}</td>
                    <td >${chiefEngineer1.ce1}</td>
                    <!--   total  -->
                    <td>
                    <a style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}" >${chiefEngineer1.totalApplications}</a>  </td>
                     
                    
                    <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&status=PENDING" >${chiefEngineer1.totalPendingApplications}</a>  </td>
                    <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&status=PROCESS" >${chiefEngineer1.totalInProgressApplications}</a>  </td>
                    <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&status=APPROVED" >${chiefEngineer1.totalApprovedApplications}</a>  </td>
                    <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&status=REJECTED" >${chiefEngineer1.totalRejectedApplications}</a>  </td>
                    <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&status=ISSUED" >${chiefEngineer1.totalIssuedApplications}</a>  </td>
 


                    <td > <a  style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&nocName=गैस पाइप लाइन" >${chiefEngineer1.gasPipeTotalApplications}</a>             
                       </td>
                    
                    <td >
                    <a  style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&status=PENDING&nocName=गैस पाइप लाइन" >${chiefEngineer1.gasPipePendingApplications}</a>  
                     </td>
                     
                    <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&status=PROCESS&nocName=गैस पाइप लाइन" >${chiefEngineer1.gasPipeInProgressApplications}</a>  
                     </td>
                    
                    <td>  
                    <a style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&status=APPROVED&nocName=गैस पाइप लाइन" >${chiefEngineer1.gasPipeApprovedApplications}</a>       
                    </td>
                    
                    <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&status=REJECTED&nocName=गैस पाइप लाइन" >${chiefEngineer1.gasPipeRejectedApplications}</a>  
                    
                     </td>
                         <td >
                         <a style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&status=ISSUED&nocName=गैस पाइप लाइन" >${chiefEngineer1.gasPipeIssuedApplications}</a>                                              
                         </td>

                    <td > <a  style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&nocName=पेयजल पाइप लाइन" >${chiefEngineer1.waterPipeTotalApplications}</a>             
                       </td>
                 
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&status=PENDING&nocName=पेयजल पाइप लाइन" >${chiefEngineer1.waterPipePendingApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&status=PROCESS&nocName=पेयजल पाइप लाइन" >${chiefEngineer1.waterPipeInProgressApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&status=APPROVED&nocName=पेयजल पाइप लाइन" >${chiefEngineer1.waterPipeApprovedApplications}</a>  
                   </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&status=REJECTED&nocName=पेयजल पाइप लाइन" >${chiefEngineer1.waterPipeRejectedApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&status=ISSUED&nocName=पेयजल पाइप लाइन" >${chiefEngineer1.waterPipeIssuedApplications}</a>  
                    </td>


                    <td > <a  style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&nocName=पुल - पुलिया" >${chiefEngineer1.pulPuliyaTotalApplications}</a>             
                                           </td>
                    
                   
                     <td > <a style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&status=PENDING&nocName=पुल - पुलिया" >${chiefEngineer1.pulPuliyaPendingApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&status=PROCESS&nocName=पुल - पुलिया" >${chiefEngineer1.pulPuliyaInProgressApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&status=APPROVED&nocName=पुल - पुलिया" >${chiefEngineer1.pulPuliyaApprovedApplications}</a>  
                   </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&status=REJECTED&nocName=पुल - पुलिया" >${chiefEngineer1.pulPuliyaRejectedApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&status=ISSUED&nocName=पुल - पुलिया" >${chiefEngineer1.pulPuliyaIssuedApplications}</a>  
                    </td>
                    
       

                        <td > <a  style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&nocName=मार्ग निर्माण" >${chiefEngineer1.roadConstructionTotalApplications}</a>             
                       </td>
                     
                     <td > <a style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&status=PENDING&nocName=मार्ग निर्माण" >${chiefEngineer1.roadConstructionPendingApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&status=PROCESS&nocName=मार्ग निर्माण" >${chiefEngineer1.roadConstructionInProgressApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&status=APPROVED&nocName=मार्ग निर्माण" >${chiefEngineer1.roadConstructionApprovedApplications}</a>  
                   </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&status=REJECTED&nocName=मार्ग निर्माण" >${chiefEngineer1.roadConstructionRejectedApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&status=ISSUED&nocName=मार्ग निर्माण" >${chiefEngineer1.roadConstructionIssuedApplications}</a>  
                    </td>
                    
                    
       
                    <td > <a  style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&nocName=जल आवंटन"  >${chiefEngineer1.jalawantanTotalApplications}</a>             
                       </td>

                                      
                     <td > <a style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&status=PENDING&nocName=जल आवंटन" >${chiefEngineer1.jalawantanPendingApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&status=PROCESS&nocName=जल आवंटन" >${chiefEngineer1.jalawantanInProgressApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&status=APPROVED&nocName=जल आवंटन" >${chiefEngineer1.jalawantanApprovedApplications}</a>  
                   </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&status=REJECTED&nocName=जल आवंटन" >${chiefEngineer1.jalawantanRejectedApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce1=${chiefEngineer1.ce1}&status=ISSUED&nocName=जल आवंटन" >${chiefEngineer1.jalawantanIssuedApplications}</a>  
                    </td>
 

                        `

        chiefEngineer1Report.appendChild(row);
        serialNumber++;
    })
}

function showChiefEngineer2Report(data){
    var chiefEngineer2Report = document.getElementById("chiefEngineer2Table").getElementsByTagName("tbody")[0];
    chiefEngineer2Report.innerHTML ="";

    var serialNumber = 1;
    data.forEach(chiefEngineer2=>{

        var row = document.createElement("tr");
        console.log("chiefEngineer2",chiefEngineer2)
        row.innerHTML=`
                    <td >${serialNumber}</td>
                    <td >${chiefEngineer2.ce2}</td>
                    <!--   total  -->
                     
                   
                       <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}" >${chiefEngineer2.totalApplications}</a>  </td>
                    <td >
                    
                    <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&status=PENDING" >${chiefEngineer2.totalPendingApplications}</a>  </td>
                    <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&status=PROCESS" >${chiefEngineer2.totalInProgressApplications}</a>  </td>
                    <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&status=APPROVED" >${chiefEngineer2.totalApprovedApplications}</a>  </td>
                    <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&status=REJECTED" >${chiefEngineer2.totalRejectedApplications}</a>  </td>
                    <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&status=ISSUED" >${chiefEngineer2.totalIssuedApplications}</a>  </td>
                    
 


                      <td > <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&nocName=गैस पाइप लाइन" >${chiefEngineer2.gasPipeTotalApplications}</a>             
                       </td>
                       
                        <td > 
                    <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&status=PENDING&nocName=गैस पाइप लाइन" >${chiefEngineer2.gasPipePendingApplications}</a>  
                     </td>
                     
                    <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&status=PROCESS&nocName=गैस पाइप लाइन" >${chiefEngineer2.gasPipeInProgressApplications}</a>  
                     </td>
                    
                    <td>  
                    <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&status=APPROVED&nocName=गैस पाइप लाइन" >${chiefEngineer2.gasPipeApprovedApplications}</a>       
                    </td>
                    
                    <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&status=REJECTED&nocName=गैस पाइप लाइन" >${chiefEngineer2.gasPipeRejectedApplications}</a>  
                    
                     </td>
                         <td >
                         <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&status=ISSUED&nocName=गैस पाइप लाइन" >${chiefEngineer2.gasPipeIssuedApplications}</a>                                              
                         </td>


                  
 <td > <a  style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&nocName=पेयजल पाइप लाइन" >${chiefEngineer2.waterPipeTotalApplications}</a>             
                       </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&status=PENDING&nocName=पेयजल पाइप लाइन" >${chiefEngineer2.waterPipePendingApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&status=PROCESS&nocName=पेयजल पाइप लाइन" >${chiefEngineer2.waterPipeInProgressApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&status=APPROVED&nocName=पेयजल पाइप लाइन" >${chiefEngineer2.waterPipeApprovedApplications}</a>  
                   </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&status=REJECTED&nocName=पेयजल पाइप लाइन" >${chiefEngineer2.waterPipeRejectedApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&status=ISSUED&nocName=पेयजल पाइप लाइन" >${chiefEngineer2.waterPipeIssuedApplications}</a>  
                    </td>


                  <td > <a  style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&nocName=पुल - पुलिया" >${chiefEngineer2.pulPuliyaTotalApplications}</a>             
                       </td>

                    
                     <td > <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&status=PENDING&nocName=पुल - पुलिया" >${chiefEngineer2.pulPuliyaPendingApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&status=PROCESS&nocName=पुल - पुलिया" >${chiefEngineer2.pulPuliyaInProgressApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&status=APPROVED&nocName=पुल - पुलिया" >${chiefEngineer2.pulPuliyaApprovedApplications}</a>  
                   </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&status=REJECTED&nocName=पुल - पुलिया" >${chiefEngineer2.pulPuliyaRejectedApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&status=ISSUED&nocName=पुल - पुलिया" >${chiefEngineer2.pulPuliyaIssuedApplications}</a>  
                    </td>



                   <td > <a  style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&nocName=मार्ग निर्माण" >${chiefEngineer2.roadConstructionTotalApplications}</a>             
                       </td>

                     <td > <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&status=PENDING&nocName=मार्ग निर्माण" >${chiefEngineer2.roadConstructionPendingApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&status=PROCESS&nocName=मार्ग निर्माण" >${chiefEngineer2.roadConstructionInProgressApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&status=APPROVED&nocName=मार्ग निर्माण" >${chiefEngineer2.roadConstructionApprovedApplications}</a>  
                   </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&status=REJECTED&nocName=मार्ग निर्माण" >${chiefEngineer2.roadConstructionRejectedApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&status=ISSUED&nocName=मार्ग निर्माण" >${chiefEngineer2.roadConstructionIssuedApplications}</a>  
                    </td>
   
   
     
                        <td > <a  style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&nocName=जल आवंटन"  >${chiefEngineer2.jalawantanTotalApplications}</a>             
                       </td>
 
                     <td > <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&status=PENDING&nocName=जल आवंटन" >${chiefEngineer2.jalawantanPendingApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&status=PROCESS&nocName=जल आवंटन" >${chiefEngineer2.jalawantanInProgressApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&status=APPROVED&nocName=जल आवंटन" >${chiefEngineer2.jalawantanApprovedApplications}</a>  
                   </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&status=REJECTED&nocName=जल आवंटन" >${chiefEngineer2.jalawantanRejectedApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ce2=${chiefEngineer2.ce2}&status=ISSUED&nocName=जल आवंटन" >${chiefEngineer2.jalawantanIssuedApplications}</a>  
                    </td>

                        `

        chiefEngineer2Report.appendChild(row);
        serialNumber++;
    })
}

function showSuperIntendentReport(data){
    var seReport = document.getElementById("superintendentEngineerTable").getElementsByTagName("tbody")[0];
    seReport.innerHTML ="";

    var serialNumber = 1;
    data.forEach(seData=>{

        var row = document.createElement("tr");

        row.innerHTML=`
                    <td >${serialNumber}</td>
                    <td >${seData.se}</td>
                    <!--   total  -->
                    
                      
                     
                      <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}" >${seData.totalApplications}</a>  </td>
                   
                      <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&status=PENDING" >${seData.totalPendingApplications}</a>  </td>
                    <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&status=PROCESS" >${seData.totalInProgressApplications}</a>  </td>
                    <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&status=APPROVED" >${seData.totalApprovedApplications}</a>  </td>
                    <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&status=REJECTED" >${seData.totalRejectedApplications}</a>  </td>
                    <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&status=ISSUED" >${seData.totalIssuedApplications}</a>  </td>
                    
 


                      <td > <a  style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&nocName=गैस पाइप लाइन" >${seData.gasPipeTotalApplications}</a>             
                       </td>

                                             <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&status=PENDING&nocName=गैस पाइप लाइन" >${seData.gasPipePendingApplications}</a>  
                     </td>
                     
                    <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&status=PROCESS&nocName=गैस पाइप लाइन" >${seData.gasPipeInProgressApplications}</a>  
                     </td>
                    
                    <td>  
                    <a style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&status=APPROVED&nocName=गैस पाइप लाइन" >${seData.gasPipeApprovedApplications}</a>       
                    </td>
                    
                    <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&status=REJECTED&nocName=गैस पाइप लाइन" >${seData.gasPipeRejectedApplications}</a>  
                    
                     </td>
                         <td >
                         <a style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&status=ISSUED&nocName=गैस पाइप लाइन" >${seData.gasPipeIssuedApplications}</a>                                              
                         </td>


                     <td > <a  style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&nocName=पेयजल पाइप लाइन" >${seData.waterPipeTotalApplications}</a>             
                       </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&status=PENDING&nocName=पेयजल पाइप लाइन" >${seData.waterPipePendingApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&status=PROCESS&nocName=पेयजल पाइप लाइन" >${seData.waterPipeInProgressApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&status=APPROVED&nocName=पेयजल पाइप लाइन" >${seData.waterPipeApprovedApplications}</a>  
                   </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&status=REJECTED&nocName=पेयजल पाइप लाइन" >${seData.waterPipeRejectedApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&status=ISSUED&nocName=पेयजल पाइप लाइन" >${seData.waterPipeIssuedApplications}</a>  
                    </td>


                    
                        <td > <a  style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&nocName=पुल - पुलिया" >${seData.pulPuliyaTotalApplications}</a>             
                       </td>
                    
                     <td > <a style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&status=PENDING&nocName=पुल - पुलिया" >${seData.pulPuliyaPendingApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&status=PROCESS&nocName=पुल - पुलिया" >${seData.pulPuliyaInProgressApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&status=APPROVED&nocName=पुल - पुलिया" >${seData.pulPuliyaApprovedApplications}</a>  
                   </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&status=REJECTED&nocName=पुल - पुलिया" >${seData.pulPuliyaRejectedApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&status=ISSUED&nocName=पुल - पुलिया" >${seData.pulPuliyaIssuedApplications}</a>  
                    </td>
                    
       


                     <td > <a  style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&nocName=मार्ग निर्माण" >${seData.roadConstructionTotalApplications}</a>             
                       </td>
                     <td > <a style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&status=PENDING&nocName=मार्ग निर्माण" >${seData.roadConstructionPendingApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&status=PROCESS&nocName=मार्ग निर्माण" >${seData.roadConstructionInProgressApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&status=APPROVED&nocName=मार्ग निर्माण" >${seData.roadConstructionApprovedApplications}</a>  
                   </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&status=REJECTED&nocName=मार्ग निर्माण" >${seData.roadConstructionRejectedApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&status=ISSUED&nocName=मार्ग निर्माण" >${seData.roadConstructionIssuedApplications}</a>  
                    </td>
                    
       


                   <td > <a  style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&nocName=जल आवंटन"  >${seData.jalawantanTotalApplications}</a>             
                       </td>                   
                    
                     <td > <a style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&status=PENDING&nocName=जल आवंटन" >${seData.jalawantanPendingApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&status=PROCESS&nocName=जल आवंटन" >${seData.jalawantanInProgressApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&status=APPROVED&nocName=जल आवंटन" >${seData.jalawantanApprovedApplications}</a>  
                   </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&status=REJECTED&nocName=जल आवंटन" >${seData.jalawantanRejectedApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?se=${seData.se}&status=ISSUED&nocName=जल आवंटन" >${seData.jalawantanIssuedApplications}</a>  
                    </td>
                        `

        seReport.appendChild(row);
        serialNumber++;
    })
}

function showExecutiveEngineerReport(data){
    var eeReport = document.getElementById("executiveEngineerTable").getElementsByTagName("tbody")[0];
    eeReport.innerHTML ="";

    var serialNumber = 1;
    data.forEach(executiveData=>{

        var row = document.createElement("tr");

        row.innerHTML=`
                    <td >${serialNumber}</td>
                    <td >${executiveData.ee}</td>
                    <!--   total  -->
                 
                      <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}" >${executiveData.totalApplications}</a>  </td>
                    
                    
                       <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&status=PENDING" >${executiveData.totalPendingApplications}</a>  </td>
                    <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&status=PROCESS" >${executiveData.totalInProgressApplications}</a>  </td>
                    <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&status=APPROVED" >${executiveData.totalApprovedApplications}</a>  </td>
                    <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&status=REJECTED" >${executiveData.totalRejectedApplications}</a>  </td>
                    <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&status=ISSUED" >${executiveData.totalIssuedApplications}</a>  </td>
 

                         <td > <a  style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&nocName=गैस पाइप लाइन" >${executiveData.gasPipeTotalApplications}</a>             
                       </td>
                       
                       <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?se=${executiveData.ee}&status=PENDING&nocName=गैस पाइप लाइन" >${executiveData.gasPipePendingApplications}</a>  
                     </td>
                     
                    <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&status=PROCESS&nocName=गैस पाइप लाइन" >${executiveData.gasPipeInProgressApplications}</a>  
                     </td>
                    
                    <td>  
                    <a style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&status=APPROVED&nocName=गैस पाइप लाइन" >${executiveData.gasPipeApprovedApplications}</a>       
                    </td>
                    
                    <td >
                    <a style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&status=REJECTED&nocName=गैस पाइप लाइन" >${executiveData.gasPipeRejectedApplications}</a>  
                    
                     </td>
                         <td >
                         <a style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&status=ISSUED&nocName=गैस पाइप लाइन" >${executiveData.gasPipeIssuedApplications}</a>                                              
                         </td>


                    
                    <td > <a  style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&nocName=पेयजल पाइप लाइन" >${executiveData.waterPipeTotalApplications}</a>             
                       </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&status=PENDING&nocName=पेयजल पाइप लाइन" >${executiveData.waterPipePendingApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&status=PROCESS&nocName=पेयजल पाइप लाइन" >${executiveData.waterPipeInProgressApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&status=APPROVED&nocName=पेयजल पाइप लाइन" >${executiveData.waterPipeApprovedApplications}</a>  
                   </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&status=REJECTED&nocName=पेयजल पाइप लाइन" >${executiveData.waterPipeRejectedApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&status=ISSUED&nocName=पेयजल पाइप लाइन" >${executiveData.waterPipeIssuedApplications}</a>  
                    </td>


                    <td > <a  style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&nocName=पुल - पुलिया" >${executiveData.pulPuliyaTotalApplications}</a>             
                       </td>
                    
                     <td > <a style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&status=PENDING&nocName=पुल - पुलिया" >${executiveData.pulPuliyaPendingApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&status=PROCESS&nocName=पुल - पुलिया" >${executiveData.pulPuliyaInProgressApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&status=APPROVED&nocName=पुल - पुलिया" >${executiveData.pulPuliyaApprovedApplications}</a>  
                   </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&status=REJECTED&nocName=पुल - पुलिया" >${executiveData.pulPuliyaRejectedApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&status=ISSUED&nocName=पुल - पुलिया" >${executiveData.pulPuliyaIssuedApplications}</a>  
                    </td>
                    
       


                     <td > <a  style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&nocName=मार्ग निर्माण" >${executiveData.roadConstructionTotalApplications}</a>             
                       </td>
                     <td > <a style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&status=PENDING&nocName=मार्ग निर्माण" >${executiveData.roadConstructionPendingApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&status=PROCESS&nocName=मार्ग निर्माण" >${executiveData.roadConstructionInProgressApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&status=APPROVED&nocName=मार्ग निर्माण" >${executiveData.roadConstructionApprovedApplications}</a>  
                   </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&status=REJECTED&nocName=मार्ग निर्माण" >${executiveData.roadConstructionRejectedApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&status=ISSUED&nocName=मार्ग निर्माण" >${executiveData.roadConstructionIssuedApplications}</a>  
                    </td>
                    
       


                    <td > <a  style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&nocName=जल आवंटन"  >${executiveData.jalawantanTotalApplications}</a>             
                       </td>
                    
                    
                     <td > <a style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&status=PENDING&nocName=जल आवंटन" >${executiveData.jalawantanPendingApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&status=PROCESS&nocName=जल आवंटन" >${executiveData.jalawantanInProgressApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&status=APPROVED&nocName=जल आवंटन" >${executiveData.jalawantanApprovedApplications}</a>  
                   </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&status=REJECTED&nocName=जल आवंटन" >${executiveData.jalawantanRejectedApplications}</a>  
                    </td>
                    <td > <a style="color: blue" href="/noc/officerReviewApplication?ee=${executiveData.ee}&status=ISSUED&nocName=जल आवंटन" >${executiveData.jalawantanIssuedApplications}</a>  
                    </td>

                        `

        eeReport.appendChild(row);
        serialNumber++;
    })
}