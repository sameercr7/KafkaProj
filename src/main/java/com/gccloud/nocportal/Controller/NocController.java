package com.gccloud.nocportal.Controller;

import com.gccloud.nocportal.Entity.UserDetails;
import com.gccloud.nocportal.Entity.UsersSpring;
import com.gccloud.nocportal.Service.NocService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/noc")
public class NocController {

    @Autowired
    private NocService nocService;

    @GetMapping(path = "/portal")
    public String landingpage() {


        return "landingpage";
    }

    @GetMapping(path = "/dashboard")
    public String homePage(Model theModel) {


        return "department/dashboard";
    }

    @GetMapping(path = "/inprogress")
    public String inprogressPage() {


        return "department/inprogress_table";
    }

    @GetMapping(path = "/rejected")
    public String rejectedPage() {


        return "department/rejected_table";
    }

    @GetMapping(path = "/approved")
    public String approvedPage() {


        return "department/approved_table";
    }

    @GetMapping(path = "/flowchart")
    public String flowchartPage() {


        return "flowchart";
    }

    @GetMapping(path = "/flowcharttable")
    public String flowcharttablePage() {


        return "flowchart_table";
    }

    @GetMapping(path = "/changetable")
    public String changetablePage() {


        return "change_table";
    }

    @GetMapping(path = "/transfer")
    public String transferPage(Model theModel) {

        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().toArray()[0].toString();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        theModel.addAttribute("role", role);
        theModel.addAttribute("username", username);

        return "department/transfer_table";
    }

    @GetMapping(path = "/newapplication")
    public String newApplicationPage() {


        return "department/newApplication";
    }

    @GetMapping(path = "/agency")
    public String agencyDashboard() {


        return "agency/agency_dashboard";
    }

    @GetMapping(path = "/agency_application")
    public String agencyApplication() {


        return "agency/application_table";
    }

    @GetMapping(path = "/agency_track")
    public String agencyApplicationTrack() {


        return "agency/application_track";
    }

    @GetMapping(path = "/agency_grevances")
    public String agencyApplicationGrevances() {


        return "agency/save_Grevances";
    }

    @GetMapping(path = "/agency_resubmission")
    public String agencyApplicationResubmission() {


        return "agency/application_resubmission";
    }

    @GetMapping(path = "/agency_approved")
    public String agencyApplicationApproved() {


        return "agency/application_approved";
    }

    @GetMapping(path = "/agency_rejected")
    public String agencyApplicationRejected() {


        return "agency/application_rejected";
    }

    @GetMapping(path = "/agency_feedback")
    public String agencyFeedback() {


        return "agency/save_Feedback";
    }

    @GetMapping(path = "/agency_noc")
    public String agencyApplicationNoc(Model theModel) {

        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().toArray()[0].toString();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        theModel.addAttribute("role", role);
        theModel.addAttribute("username", username);

        return "agency/application_noc";
    }


    @GetMapping(path = "/login")
    public String login() {


        return "authentication/login";

    }

    @GetMapping(path = "/departmentlogin")
    public String departmentLogin() {


        return "authentication/departmentlogin";

    }

    @GetMapping(path = "/signup")
    public String signup() {

        return "authentication/signup";

    }

//    @PostMapping(path = "/signUpProcess")
//    public String signupProcess(@ModelAttribute UsersSpring usersSpring, @ModelAttribute UserDetails userDetails) {
//
//        nocService.saveAgencySignUpDetails(usersSpring, userDetails);
//
//        return "redirect:/noc/agency";
//
//    }

    @GetMapping(path = "/forgotpassword")
    public String forgotpassword() {

        return "authentication/forgotpassword";

    }

    @GetMapping(path = "/form")
    public String form(Model theModel, @RequestParam(value = "heading", required = false) String heading) {

//        System.out.println("heading "+heading);
        List<String> nocTypes = nocService.findListOfNoc(); // Fetch list of NOC Types

        theModel.addAttribute("nocTypes", nocTypes);

        return "form";

    }

    @GetMapping(path = "/view")
    public String view(Model theModel, @RequestParam(value = "heading", required = false) String heading) {

//        System.out.println("heading "+heading);
        List<String> nocTypes = nocService.findListOfNoc(); // Fetch list of NOC Types

        theModel.addAttribute("nocTypes", nocTypes);
        return "department/document_view";

    }

    @GetMapping(path = "/nocCards")
    public String nocCards() {


        return "Pages/nocCards";

    }

    @GetMapping(path = "/orders")
    public String orderPage() {


        return "orders";
    }

    @GetMapping(path = "/postorder")
    public String departmentOrderPage() {


        return "department/save_Orders";
    }

    @GetMapping(path = "/reviewgrievances")
    public String reviewGrevancesPage() {


        return "department/reviewGrievances";
    }

    @GetMapping(path = "/reviewfeedback")
    public String reviewFeedbackPage() {


        return "department/reviewFeedback";
    }

    //    Method For Review Application By Department
    @GetMapping(path = "/reviewApplications")
    public String reviewApplicationsByDepartment() {



        return "department/reviewApplications";
    }

    //    Method For Review Application By Agency
    @GetMapping(path = "/agencyReviewApplication")
    public String reviewApplicationsByAgency() {


        return "agency/agencyReviewApplication";
    }

    @GetMapping(path = "/documentview")
    public String documentView() {


        return "department/documentView_table";
    }

    @GetMapping(path = "/applicationtrack")
    public String applicationTrackPage() {


        return "department/application_track";
    }

    @GetMapping(path = "/applicationresubmitted")
    public String applicationResumbittedPage() {


        return "department/application_resubmitted";
    }

    @GetMapping(path = "/applicationclosed")
    public String applicationClosedPage() {


        return "department/application_closed";
    }

    @GetMapping(path = "/applicationrejected")
    public String applicationRejectedPage() {


        return "department/application_rejected";
    }

    @GetMapping(path = "/applicationapproved")
    public String applicationApprovedPage() {


        return "department/application_approved";
    }

    @GetMapping(path = "/applicationletter")
    public String applicationLetterPage() {


        return "department/application_letter";
    }

    @GetMapping(path = "/nocissued")
    public String applicationIssuedPage() {


        return "department/application_issued";
    }

    @GetMapping(path = "/noccertificate")
    public String applicationCertificatePage(Model themodel) {

        return "department/application_certificate";
    }

    @GetMapping(path = "/departmentstats")
    public String departmentStatisticsPage() {


        return "department/departmentstats";
    }

    @GetMapping(path = "/analytics")
    public String applicationAnalyticsPage() {


        return "department/analytics";
    }


    @GetMapping(path = "/pdfview")
    public String pdfView() {


        return "department/pdfview_table";
    }

    //    Method For View Noc Application (With Action) By Department
    @GetMapping(path = "/viewApplication")
    public String viewApplicationsByDepartment(@RequestParam(value = "id") Long id, Model theModel) {

        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().toArray()[0].toString();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        theModel.addAttribute("role", role);
        theModel.addAttribute("username", username);

        theModel.addAttribute("applicationId", id);

        return "nocFormView";
    }

    //    Method For View Noc Application (Only View) By Department
    @GetMapping(path = "/viewOnlyApplication")
    public String viewApplicationsOnlyViewByDepartment(@RequestParam(value = "id") Long id, Model theModel) throws IOException {

        theModel.addAttribute("applicationId", id);

        return "department/viewApplicationForm";
    }


    //    Method For Transfet The Application To Agency
    @GetMapping(path = "/transferToAgency")
    public String transferToAgency(@RequestParam(value = "id") Long id, Model theModel) {

        nocService.transferApplicationToAgency(id);

        return "redirect:/noc/transfer";
    }

    //Method For Track Application Letter For Agency
    @GetMapping(path = "/trackAppForAgency")
    public String trackApplicationLetterForAgency() {


        return "agency/trackAgencyApplicationLetter";
    }

    //Method For Track Application Letter For Dep
    @GetMapping(path = "/trackAppForDep")
    public String trackApplicationLetterForDep() {

        return "department/trackDepartmentApplicationLetter";
    }

    @GetMapping("/officeWiseReport")
    public String showOfficeWiseReport(){

        return "department/officeWise_Report";
    }

    @GetMapping("/officerReviewApplication")
    public String showOfficerReviewPage(@RequestParam(value = "ce1",required = false) String ce1,
                                        @RequestParam(value = "ce2",required = false) String ce2,
                                        @RequestParam(value = "se",required = false) String se,
                                        @RequestParam(value = "ee",required = false) String ee,
                                        @RequestParam(value = "status",required = false) String status,
                                        @RequestParam(value = "nocName",required = false) String nocName
                                        ,Model theModel){
            //fetch for ce1
        if(status != null && ce1 != null && nocName != null && ce2 == null && se==null && ee == null ){

            List<Map<String,Object>>  reviewData = nocService.fetchDataWhenCe1StatusApplied(status,ce1,nocName);
            theModel.addAttribute("reviewData", reviewData);

        }
        //fetch for ce 2
        if(status != null && ce2 != null && nocName != null && ce1 == null && se==null  && ee == null ){

            List<Map<String,Object>> reviewData = nocService.fetchDataWhenCe2StatusApplied(ce2,status,nocName);
            theModel.addAttribute("reviewData", reviewData);

        }

        //        fetch for se
        if(status != null && se != null && nocName != null && ce1 == null && ce2 == null  && ee == null ){

            List<Map<String,Object>> reviewData = nocService.fetchDataWhenSEStatusApplied(se,status,nocName);
            theModel.addAttribute("reviewData", reviewData);

        }

//        fetch for ee
        if(status != null && se == null && nocName != null && ce1 == null && ce2 == null  && ee != null ){

            List<Map<String,Object>> reviewData = nocService.fetchDataWhenEEStatusApplied(ee,status,nocName);
            theModel.addAttribute("reviewData", reviewData);

        }

//        total for ce1 for every noc name
        if(status == null && se == null && nocName != null && ce1 != null && ce2 == null  && ee == null){

            List<Map<String,Object>> reviewData = nocService.fetchDataForCe1AndNOCName(ce1,nocName);
            theModel.addAttribute("reviewData", reviewData);

        }

//        total for ce2 for every noc name
        if(status == null && se == null && nocName != null && ce1 == null && ce2 != null  && ee == null){

            List<Map<String,Object>> reviewData = nocService.fetchDataForCe2AndNOCName(ce2,nocName);
            theModel.addAttribute("reviewData", reviewData);

        }

        //        total for se for every noc name
        if(status == null && se != null && nocName != null && ce1 == null && ce2 == null  && ee == null){

            List<Map<String,Object>> reviewData = nocService.fetchDataForSEAndNOCName(se,nocName);
            theModel.addAttribute("reviewData", reviewData);

        }

        //        total for ee for every noc name
        if(status == null && se == null && nocName != null && ce1 == null && ce2 == null  && ee != null){

            List<Map<String,Object>> reviewData = nocService.fetchDataForEEAndNOCName(ee,nocName);
            theModel.addAttribute("reviewData", reviewData);

        }

        //        total for CE1 according to status and ce1 name
        if(status != null && se == null && nocName == null && ce1 != null && ce2 == null  && ee == null){

            List<Map<String,Object>> reviewData = nocService.fetchDataByCE1andStatus(ce1,status);
            theModel.addAttribute("reviewData", reviewData);

        }

        //        total for CE1 according to status and CE2 name
        if(status != null && se == null && nocName == null && ce1 == null && ce2 != null  && ee == null){

            List<Map<String,Object>> reviewData = nocService.fetchDataByCE2andStatus(ce2,status);
            theModel.addAttribute("reviewData", reviewData);

        }

        //        total for CE1 according to status and se name
        if(status != null && se != null && nocName == null && ce1 == null && ce2 == null  && ee == null){

            List<Map<String,Object>> reviewData = nocService.fetchDataBySEandStatus(se,status);
            theModel.addAttribute("reviewData", reviewData);

        }

        //        total for CE1 according to status and se name
        if(status != null && se == null && nocName == null && ce1 == null && ce2 == null  && ee != null){

            List<Map<String,Object>> reviewData = nocService.fetchDataByEEandStatus(ee,status);
            theModel.addAttribute("reviewData", reviewData);

        }


        if(status == null && se == null && nocName == null && ce1 != null && ce2 == null  && ee == null){

            List<Map<String,Object>> reviewData = nocService.fetchDataByByCE1Name(ce1);
            theModel.addAttribute("reviewData", reviewData);

        }

        if(status == null && se == null && nocName == null && ce1 == null && ce2 != null  && ee == null){

            List<Map<String,Object>> reviewData = nocService.fetchDataByByCE2Name(ce2);
            theModel.addAttribute("reviewData", reviewData);

        }

        if(status == null && se != null && nocName == null && ce1 == null && ce2 == null  && ee == null){

            List<Map<String,Object>> reviewData = nocService.fetchDataByBySeName(se);
            theModel.addAttribute("reviewData", reviewData);

        }



        if(status == null && se == null && nocName == null && ce1 == null && ce2 == null  && ee != null){

            List<Map<String,Object>> reviewData = nocService.fetchDataByByEEName(ee);
            theModel.addAttribute("reviewData", reviewData);

        }





        return "department/officerView_reviewApplication";
    }


}
