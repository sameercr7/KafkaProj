package com.gccloud.nocportal.Controller;

import com.gccloud.nocportal.Entity.*;
import com.gccloud.nocportal.Service.NocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/data")
public class DataController {

    @Autowired
    private NocService nocService;

    @GetMapping("/nocList")
    public ResponseEntity<ApiReponse> getNocList() {

        try {
            List<String> nocList = nocService.findListOfNoc();
            List<String> districtList = nocService.findListOfDistrict();
            List<String> canalList = nocService.findListOfCanals();

            List<NocList> nocMappedList = nocService.findAllNocListData();

            HashMap<String, Object> response = new HashMap<>();

            response.put("success", true);
            response.put("message", "All  list fetched successfully");
            response.put("nocList", nocList);
            response.put("districtList", districtList);
            response.put("canalList", canalList);
            response.put("nocMappedList", nocMappedList);


            ApiReponse apiResponse = ApiReponse.builder().message("Data Fetched Successfully!").statusCode(HttpStatus.OK.value()).flag("Success").data(response).build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder().message("Failed to Save  Data").statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).flag("Error").build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }


    }

    @GetMapping("/fetchDataByNoc/{index}")
    public ResponseEntity<ApiReponse> getNocMappedData(@PathVariable(value = "index") Long index) {

        try {

            List<CheckList> fetchAllChecklistData = nocService.fetchAllChecklistDataById(index);

            HashMap<String, Object> response = new HashMap<>();

            response.put("success", true);
            response.put("message", "All  list fetched successfully");
            response.put("fetchAllChecklistData", fetchAllChecklistData);

            ApiReponse apiResponse = ApiReponse.builder().message("Data Fetched Successfully!").statusCode(HttpStatus.OK.value()).flag("Success").data(response).build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder().message("Failed to Save  Data").statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).flag("Error").build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/fetchListOfEe")
    public ResponseEntity<ApiReponse> getListOfEE() {

        try {

            List<String> fetchListOfEe = nocService.fetchListOfEe();

            HashMap<String, Object> response = new HashMap<>();

            response.put("success", true);
            response.put("message", "All  list fetched successfully");
            response.put("fetchListOfEe", fetchListOfEe);

            ApiReponse apiResponse = ApiReponse.builder().message("Data Fetched Successfully!").statusCode(HttpStatus.OK.value()).flag("Success").data(response).build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder().message("Failed to Save  Data").statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).flag("Error").build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @GetMapping("/fetchRespectiveSeCeOfEe/{ee}")
    public ResponseEntity<ApiReponse> getRespectiveSeCeOfEe(@PathVariable(value = "ee") String ee) {

        try {

            List<UsersSpring> fetchRespectiveSeCeOfEe = nocService.fetchRespectiveSeCeOfEechListOfEe(ee);

            HashMap<String, Object> response = new HashMap<>();

            response.put("success", true);
            response.put("message", "All  Respective SECE fetched successfully");
            response.put("fetchRespectiveSeCeOfEe", fetchRespectiveSeCeOfEe);

            ApiReponse apiResponse = ApiReponse.builder().message("Data Fetched Successfully!").statusCode(HttpStatus.OK.value()).flag("Success").data(response).build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder().message("Failed to Save  Data").statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).flag("Error").build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    //    This Method For Get Stats Data For Landing Page
    @GetMapping("/statsForLandingPage")
    public ResponseEntity<ApiReponse> getLandingPageDashboardStats() {

        try {

            Map<String, Long> stats = new HashMap<>();
            stats.put("totalApplications", nocService.getTotalApplications());
            stats.put("inProgressApplications", nocService.getApplicationsByStatus("IN_PROGRESS"));
            stats.put("approvedApplications", nocService.getApplicationsByStatus("APPROVED"));
            stats.put("rejectedApplications", nocService.getApplicationsByStatus("REJECTED"));
            stats.put("pending7Days", nocService.get7DaysPendingApplication("PENDING"));
            stats.put("pending15Days", nocService.get15DaysPendingApplication("PENDING"));

            ApiReponse apiResponse = ApiReponse.builder()
                    .message("Data Fetched Successfully!")
                    .statusCode(HttpStatus.OK.value())
                    .flag("Success")
                    .data(stats)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder()
                    .message("Failed to Fetch Data")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .flag("Error")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    //    This Method For Get Stats Data For Dashboard For Department And Admin
    @GetMapping("/statsForDepartment")
    public ResponseEntity<ApiReponse> getDashboardStats(@RequestParam String financialYear) {

        try {

            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            Map<String, Long> stats = new HashMap<>();
            stats.put("totalApplications", nocService.getTotalApplicationsByFinYear(financialYear));
            stats.put("inProgressApplications", nocService.getApplicationsByStatusByFinYear("IN_PROGRESS", financialYear));
            stats.put("approvedApplications", nocService.getApplicationsByStatusByFinYear("APPROVED", financialYear));
            stats.put("rejectedApplications", nocService.getApplicationsByStatusByFinYear("REJECTED", financialYear));
            stats.put("pending7Days", nocService.get7DaysPendingApplicationByFinYear("PENDING", financialYear));
            stats.put("pending15Days", nocService.get15DaysPendingApplicationByFinYear("PENDING", financialYear));


//            stats.put("noAction7Days", nocService.getNoAction7DaysApplicationByFinYear("IN_PROGRESS", financialYear));
//            stats.put("noAction15Days", nocService.getNoAction15DaysApplicationByFinYear("IN_PROGRESS", financialYear));


            ApiReponse apiResponse = ApiReponse.builder()
                    .message("Data Fetched Successfully!")
                    .statusCode(HttpStatus.OK.value())
                    .flag("Success")
                    .data(stats)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder()
                    .message("Failed to Fetch Data")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .flag("Error")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    //    This Method For Get Stats Data For Dashboard For Agency
    @GetMapping("/statsForAgency")
    public ResponseEntity<ApiReponse> getDashboardStatsForAgency(@RequestParam String financialYear) {

        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            // This method retrieves count data for the stats
            Map<String, Long> stats = new HashMap<>();
            stats.put("totalApplications", nocService.getTotalApplicationsForUser(username, financialYear));
            stats.put("inProgressApplications", nocService.getApplicationsByStatusForUser("IN_PROGRESS", username, financialYear));
            stats.put("approvedApplications", nocService.getApplicationsByStatusForUser("APPROVED", username, financialYear));
            stats.put("rejectedApplications", nocService.getApplicationsByStatusForUser("REJECTED", username, financialYear));
            stats.put("pendingApplications", nocService.getPendingApplicationForUser("PENDING", username, financialYear));
            stats.put("certificateIssued", nocService.getCertificateIssuedForUser("APPROVED", username, financialYear));
            stats.put("reSubmitted", nocService.getReSubmittedApplicationForUser(username, financialYear));

            List<Map<String, Object>> agencyTableData = nocService.getAgencyTableDataForUser(username, financialYear);

            // Combine the stats and agency table data into one response map
            Map<String, Object> response = new HashMap<>();
            response.put("agencyTableData", agencyTableData);

            response.put("stats", stats);

            // Build and return the API response
            ApiReponse apiResponse = ApiReponse.builder()
                    .message("Data Fetched Successfully!")
                    .statusCode(HttpStatus.OK.value())
                    .flag("Success")
                    .data(response)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder()
                    .message("Failed to Fetch Data")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .flag("Error")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @PostMapping(value = "/savingNocFormData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiReponse> savingNocFromData(@ModelAttribute Application application, @ModelAttribute DocumentStatus documentStatus) {

        try {

            // List<UsersSpring> fetchRespectiveSeCeOfEe = nocService.fetchRespectiveSeCeOfEechListOfEe();
            String[] result = nocService.savingNocFromData(application, documentStatus);
            String spcId = result[0];
            String message = result[1];
            HashMap<String, Object> response = new HashMap<>();

            response.put("success", true);
            response.put("message", message);
            response.put("spcId", spcId);
            //response.put("fetchRespectiveSeCeOfEe", fetchRespectiveSeCeOfEe);
            ApiReponse apiResponse = ApiReponse.builder().message("Data Fetched Successfully!").statusCode(HttpStatus.OK.value()).flag("Success").data(response).build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder().message("Failed to Save  Data").statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).flag("Error").build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

    }


    //This method  for saving the reviewed Data
    @PostMapping(value = "/savingReviewedNocFormData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiReponse> savingReviewedNocFormData(@ModelAttribute Application application, @ModelAttribute DocumentStatus documentStatus) {

        try {


            String[] result = nocService.savingReviewedNocFormData(application, documentStatus);

            HashMap<String, Object> response = new HashMap<>();

            response.put("success", true);

            ApiReponse apiResponse = ApiReponse.builder().message("Data Fetched Successfully!").statusCode(HttpStatus.OK.value()).flag("Success").data(response).build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder().message("Failed to Save  Data").statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).flag("Error").build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

    }


    @GetMapping(path = "/ce1")
    public List<String> fetchCe1() {

        List<String> listOfCe1 = nocService.fetchAllCe1();

        return listOfCe1;
    }

//    Method For Get Sub Admin List

    @GetMapping(path = "/subAdmin")
    public List<String> fetchSubAdmin() {

        List<String> listOfSubAdmin = nocService.fetchSubAdmin();

        return listOfSubAdmin;
    }

    @GetMapping(path = "/ce2/{ce1}")
    public List<String> fetchCe2(@PathVariable(value = "ce1") String ce1) {

        List<String> listOfCe = nocService.fetchAllCe2(ce1);

        return listOfCe;
    }

    @GetMapping(path = "/se/{ce2}")
    public List<String> fetchSeWhereCe2(@PathVariable(value = "ce2") String ce2) {

        List<String> listOfSe = nocService.getAllSeWhereCe2(ce2);

        return listOfSe;
    }

    @GetMapping(path = "/ee/{se}")
    public List<String> fetchEeWhereSe(@PathVariable(value = "se") String se) {

        List<String> listOfEe = nocService.getAllEeWhereSe(se);
        //intln(listOfEe);

        return listOfEe;
    }

    //    This Method For Get Logs Data For Current Agency
    @GetMapping("/logsOfFile")
    public ResponseEntity<ApiReponse> getLogsDataForCurrentAgency(@RequestParam Long id) {
        try {
            Map<String, Object> response = new HashMap<>();

            List<Map<String, Object>> logs = nocService.findLogsByAppCurrentStatusId(id);

            response.put("logs", logs);

            // Build and return the API response
            ApiReponse apiResponse = ApiReponse.builder()
                    .message("Data Fetched Successfully!")
                    .statusCode(HttpStatus.OK.value())
                    .flag("Success")
                    .data(response)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder()
                    .message("Failed to Fetch Data")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .flag("Error")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    //    This Method For Get Review Applications Data
    @GetMapping("/fetchReviewApplications")
    public ResponseEntity<ApiReponse> getApplicationDataByUser(@RequestParam String financialYear) {

        try {

            List<Map<String, Object>> applicationData = nocService.getAllApplicationDataByUser(financialYear);

            // Combine the stats and agency table data into one response map
            Map<String, Object> response = new HashMap<>();
            response.put("applicationData", applicationData);

            // Build and return the API response
            ApiReponse apiResponse = ApiReponse.builder()
                    .message("Data Fetched Successfully!")
                    .statusCode(HttpStatus.OK.value())
                    .flag("Success")
                    .data(response)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder()
                    .message("Failed to Fetch Data")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .flag("Error")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    //    This Method For Get Application Form Data by using Application Id
    @GetMapping("/fetchApplicationDataById")
    public ResponseEntity<ApiReponse> fetchApplicationDataById(@RequestParam("id") Long id) {

        try {

            List<Map<String, Object>> applicationData = nocService.getAllApplicationDataByApplicationId(id);

            // Combine the stats and agency table data into one response map
            Map<String, Object> response = new HashMap<>();
            response.put("applicationData", applicationData);

            // Build and return the API response
            ApiReponse apiResponse = ApiReponse.builder()
                    .message("Data Fetched Successfully!")
                    .statusCode(HttpStatus.OK.value())
                    .flag("Success")
                    .data(response)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder()
                    .message("Failed to Fetch Data")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .flag("Error")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    //    Saving Letter PDF Document To Table
    @PostMapping(value = "/saveLetters")
    public ResponseEntity<ApiReponse> savingLetters(@RequestParam("file") MultipartFile file, @RequestParam("content") String content, @RequestParam("applicationId") Long applicationId, @RequestParam("description") String description, @RequestParam("nocType") String nocType) {

        try {

            nocService.savingLetters(file, content, applicationId, description, nocType);

            HashMap<String, Object> response = new HashMap<>();

            response.put("success", true);

            ApiReponse apiResponse = ApiReponse.builder().message("Data Fetched Successfully!").statusCode(HttpStatus.OK.value()).flag("Success").data(response).build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder().message("Failed to Save  Data").statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).flag("Error").build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

    }


    //    Saving Letter PDF Document To Table
    @PostMapping(value = "/saveAttachedLetters")
    public ResponseEntity<ApiReponse> saveAttachedLetters(@RequestParam("file") MultipartFile file, @RequestParam("applicationId") Long applicationId, @RequestParam("description") String description,
                                                          @RequestParam("nocType") String nocType) {

        try {

            nocService.saveAttachedLetters(file, applicationId, description, nocType);

            HashMap<String, Object> response = new HashMap<>();

            response.put("success", true);

            ApiReponse apiResponse = ApiReponse.builder().message("Data Fetched Successfully!").statusCode(HttpStatus.OK.value()).flag("Success").data(response).build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder().message("Failed to Save  Data").statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).flag("Error").build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

    }

    @PostMapping(value = "/uploadPdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiReponse> savingData(@RequestParam("pdfFile") MultipartFile file) {

        try {

            nocService.savingTrialPdf(file);
            HashMap<String, Object> response = new HashMap<>();

            response.put("success", true);
            response.put("message", "fetched successfully");
//            response.put("fetchRespectiveSeCeOfEe", fetchRespectiveSeCeOfEe);

            ApiReponse apiResponse = ApiReponse.builder().message("Data Fetched Successfully!").statusCode(HttpStatus.OK.value()).flag("Success").data(response).build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder().message("Failed to Save  Data").statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).flag("Error").build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

    }


    //    This Method For Get Review Applications Data
    @GetMapping("/fetchReviewApplicationsUnderOwnerShip")
    public ResponseEntity<ApiReponse> getApplicationDataByUserAndByOwnership(@RequestParam String financialYear) {

        try {

            List<Map<String, Object>> applicationData = nocService.getApplicationDataByUserAndByOwnership(financialYear);

            // Combine the stats and agency table data into one response map
            Map<String, Object> response = new HashMap<>();
            response.put("applicationData", applicationData);

            // Build and return the API response
            ApiReponse apiResponse = ApiReponse.builder()
                    .message("Data Fetched Successfully!")
                    .statusCode(HttpStatus.OK.value())
                    .flag("Success")
                    .data(response)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder()
                    .message("Failed to Fetch Data")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .flag("Error")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    // Transfering The Application Authority To Other User
    @PostMapping(value = "/applicationTransferToUsers", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<ApiReponse> applicationTransferToUsers(@RequestParam("applicationId") Long applicationId, @RequestParam("toUser") String toUser, @RequestParam("transferRemark") String transferRemark) {

        try {

            nocService.updateAppCurrentStatus(applicationId, toUser, transferRemark);

            HashMap<String, Object> response = new HashMap<>();
            response.put("success", true);

            ApiReponse apiResponse = ApiReponse.builder()
                    .message("Data Fetched Successfully!")
                    .statusCode(HttpStatus.OK.value())
                    .flag("Success")
                    .data(response)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder()
                    .message("Failed to Save Data")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .flag("Error")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping(value = "/savingorder", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiReponse> savingOrder(@ModelAttribute Orders orders) {

        try {

            Long messages = nocService.savingOrder(orders);
            HashMap<String, Object> response = new HashMap<>();

            response.put("success", true);

            ApiReponse apiResponse = ApiReponse.builder().message("Data Fetched Successfully!").statusCode(HttpStatus.OK.value()).flag("Success").data(response).build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder().message("Failed to Save  Data").statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).flag("Error").build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    //    Method For Get Preset Admin By AppId
    @GetMapping(path = "/presetAdminFromApplicationByAppId/{appId}")
    public String presetAdminFromApplicationByAppId(@PathVariable(value = "appId") Long appId) {

        String se = nocService.presetAdminFromApplicationByAppId(appId);

        return se;
    }

    @GetMapping("/orderlist")
    public ResponseEntity<ApiReponse> getOrderList() {

        try {
            List<Orders> orderList = nocService.findallorder();

            HashMap<String, Object> response = new HashMap<>();

            response.put("success", true);
            response.put("orders", orderList);


            ApiReponse apiResponse = ApiReponse.builder().message("Data Fetched Successfully!").statusCode(HttpStatus.OK.value()).flag("Success").data(response).build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder().message("Failed to Save  Data").statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).flag("Error").build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    //    List Of Ce1 Names Where Status (For Transfer Application)
//    @GetMapping(path = "/ce1WhereStatus")
//    public List<String> fetchCe1WhereStatus() {
//
//        List<String> listOfCe1 = nocService.fetchCe1WhereStatus();
//
//        return listOfCe1;
//    }
    @PutMapping("/order/update")

    public ResponseEntity<ApiReponse> updateDailyData(@ModelAttribute Orders orders) {
        try {
            Orders updatedOrders = nocService.updateOrders(orders);

            HashMap<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("ipdateorders", updatedOrders);

            ApiReponse apiResponse = ApiReponse.builder().message("Data Updated Successfully!").statusCode(HttpStatus.OK.value()).flag("Success").data(response).build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder().message("Failed to Update  Data").statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).flag("Error").build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

    }

    @GetMapping("/order/{id}")
    public ResponseEntity<ApiReponse> getOrderListById(@PathVariable Long id) {

        try {
            Orders order = nocService.findOrderById(id);

            if (order == null) {
                ApiReponse apiResponse = ApiReponse.builder()
                        .message("Order not found")
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .flag("Error")
                        .build();

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
            }

            HashMap<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("order", order);

            ApiReponse apiResponse = ApiReponse.builder()
                    .message("Data Fetched Successfully!")
                    .statusCode(HttpStatus.OK.value())
                    .flag("Success")
                    .data(response)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();

            ApiReponse error = ApiReponse.builder()
                    .message("Failed to Fetch Data")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .flag("Error")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping(value = "/savinggrevance")
    public ResponseEntity<ApiReponse> savingGrevance(@ModelAttribute Grievances grievances) {

        try {

            Long messages = nocService.savingGrevance(grievances);
            HashMap<String, Object> response = new HashMap<>();

            response.put("success", true);

            ApiReponse apiResponse = ApiReponse.builder().message("Data Posted Successfully!").statusCode(HttpStatus.OK.value()).flag("Success").data(response).build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder().message("Failed to Save  Data").statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).flag("Error").build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

    }

    @GetMapping("/grevancelist")
    public ResponseEntity<ApiReponse> getGrevanceList() {

        try {
            List<Grievances> grevanceList = nocService.findallgrevance();

            HashMap<String, Object> response = new HashMap<>();

            response.put("success", true);
            response.put("orders", grevanceList);


            ApiReponse apiResponse = ApiReponse.builder().message("Data Fetched Successfully!").statusCode(HttpStatus.OK.value()).flag("Success").data(response).build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder().message("Failed to Fetched  Data").statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).flag("Error").build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/grevanceuser/{username}")
    public ResponseEntity<ApiReponse> getGrevanceListByUsername() {

        try {
            // Get the username of the logged-in user
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            List<Grievances> grevanceList = nocService.findallgrevanceByusername(username);

            HashMap<String, Object> response = new HashMap<>();

            response.put("success", true);
            response.put("orders", grevanceList);


            ApiReponse apiResponse = ApiReponse.builder().message("Data Fetched Successfully!").statusCode(HttpStatus.OK.value()).flag("Success").data(response).build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder().message("Failed to Fetched  Data").statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).flag("Error").build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/grevance/update")

    public ResponseEntity<ApiReponse> updateGrevance(@ModelAttribute Grievances grievances) {
        try {
            Grievances updatedOrders = nocService.updateGrievances(grievances);

            HashMap<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("ipdateorders", updatedOrders);

            ApiReponse apiResponse = ApiReponse.builder().message("Data Updated Successfully!").statusCode(HttpStatus.OK.value()).flag("Success").data(response).build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder().message("Failed to Update  Data").statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).flag("Error").build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

    }

    @GetMapping("/grevance/{id}")
    public ResponseEntity<ApiReponse> getGrevanceById(@PathVariable Long id) {

        try {
            Grievances grievances = nocService.findGrievancesById(id);

            if (grievances == null) {
                ApiReponse apiResponse = ApiReponse.builder()
                        .message("Order not found")
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .flag("Error")
                        .build();

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
            }

            HashMap<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("order", grievances);

            ApiReponse apiResponse = ApiReponse.builder()
                    .message("Data Fetched Successfully!")
                    .statusCode(HttpStatus.OK.value())
                    .flag("Success")
                    .data(response)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();

            ApiReponse error = ApiReponse.builder()
                    .message("Failed to Fetch Data")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .flag("Error")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/deletegrevance/{id}")
    public ResponseEntity<ApiReponse> deleteGrevanceList(@PathVariable("id") Long id) {

        try {
            nocService.deletegrevance(id);

            HashMap<String, Object> response = new HashMap<>();

            response.put("success", true);


            ApiReponse apiResponse = ApiReponse.builder().message("Data Deleted Successfully!").statusCode(HttpStatus.OK.value()).flag("Success").data(response).build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            ApiReponse error = ApiReponse.builder().message("Failed to Delete  Data").statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).flag("Error").build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }


    }

    @PostMapping(value = "/savingfeedback")
    public ResponseEntity<ApiReponse> savingFeedback(@ModelAttribute Feedback feedback) {

        try {

            Long messages = nocService.savingfeedback(feedback);
            HashMap<String, Object> response = new HashMap<>();

            response.put("success", true);

            ApiReponse apiResponse = ApiReponse.builder().message("Data Posted Successfully!").statusCode(HttpStatus.OK.value()).flag("Success").data(response).build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder().message("Failed to Save  Data").statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).flag("Error").build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

    }

    @GetMapping("/feedbacklist")
    public ResponseEntity<ApiReponse> getFeedbackList() {

        try {
            List<Feedback> feedbackList = nocService.findallfeddback();

            HashMap<String, Object> response = new HashMap<>();

            response.put("success", true);
            response.put("feddbacks", feedbackList);


            ApiReponse apiResponse = ApiReponse.builder().message("Data Fetched Successfully!").statusCode(HttpStatus.OK.value()).flag("Success").data(response).build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder().message("Failed to Fetched  Data").statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).flag("Error").build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    //    List Of Ce1 Names Where Status (For Transfer Application)
//    @GetMapping(path = "/ce1WhereStatus")
//    public List<String> fetchCe1WhereStatus() {
//
//        List<String> listOfCe1 = nocService.fetchCe1WhereStatus();
//
//        return listOfCe1;
//    }
    @GetMapping(path = "/viewIndividualDocument/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> fetchPdfOfCurrentDocumentById(@PathVariable(value = "id") Long id) {

        try {
            byte[] pdfData = nocService.fetchPdfOfCurrentDocumentById(id);

            // Check if the PDF data is not null and has content
            if (pdfData == null || pdfData.length == 0) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No PDF data found for the provided ID.".getBytes());
            }

            // Create the success response with PDF data
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_PDF) // Set the content type to PDF
                    .body(pdfData);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch PDF data.".getBytes());
        }
    }

    @GetMapping(path = "/viewIndividualCertificate/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> fetchPdfOfCurrentCertificateByAppId(@PathVariable(value = "id") String id) {

        try {
            byte[] pdfData = nocService.fetchPdfOfCurrentCertificateByAppId(id);

            // Check if the PDF data is not null and has content
            if (pdfData == null || pdfData.length == 0) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No PDF data found for the provided ID.".getBytes());
            }

            // Create the success response with PDF data
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_PDF) // Set the content type to PDF
                    .body(pdfData);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch PDF data.".getBytes());
        }
    }


    @GetMapping(path = "/viewIndividualLetters/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> fetchPdfOfLettersById(@PathVariable(value = "id") Long id) {

        try {
            byte[] pdfData = nocService.fetchPdfOfLettersById(id);

            // Check if the PDF data is not null and has content
            if (pdfData == null || pdfData.length == 0) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No PDF data found for the provided ID.".getBytes());
            }

            // Create the success response with PDF data
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_PDF) // Set the content type to PDF
                    .body(pdfData);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch PDF data.".getBytes());
        }
    }


    @DeleteMapping("/deleteorder/{id}")
    public ResponseEntity<ApiReponse> deleteOrderList(@PathVariable("id") Long id) {

        try {
            nocService.deleteorder(id);

            HashMap<String, Object> response = new HashMap<>();

            response.put("success", true);


            ApiReponse apiResponse = ApiReponse.builder().message("Data Deleted Successfully!").statusCode(HttpStatus.OK.value()).flag("Success").data(response).build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            ApiReponse error = ApiReponse.builder().message("Failed to Save  Data").statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).flag("Error").build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }


    }


    //    Method For Get Preset Se By AppId
    @GetMapping(path = "/presetSeFromApplicationByAppId/{appId}")
    public String getPresetSeByEeAndAppId(@PathVariable(value = "appId") Long appId) {

        String se = nocService.getPresetSeByEeAndAppId(appId);

        return se;
    }


    //    Method For Get Preset Ce2 By AppId
    @GetMapping(path = "/presetCe2FromApplicationByAppId/{appId}")
    public String presetCe2FromApplicationByAppId(@PathVariable(value = "appId") Long appId) {

        String se = nocService.presetCe2FromApplicationByAppId(appId);

        return se;
    }

    //    Method For Get Preset Ce1 By AppId
    @GetMapping(path = "/presetCe1FromApplicationByAppId/{appId}")
    public String presetCe1FromApplicationByAppId(@PathVariable(value = "appId") Long appId) {

        String se = nocService.presetCe1FromApplicationByAppId(appId);

        return se;
    }


    @GetMapping("/AnalyticsForDepartment")
    public ResponseEntity<ApiReponse> getDepartmentDashboardStats(@RequestParam String financialYear) {
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().toArray()[0].toString();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            if (role.equalsIgnoreCase("admin")) {

                // Fetch the list of Ce1 users
                List<String> listOfCe1 = nocService.fetchAllCe1();
                List<Map<String, Object>> queryResult = nocService.fetchApplicationCountsForCe1Users(listOfCe1, financialYear);

                Map<String, Map<String, Long>> resultMap = new HashMap<>();

                for (Map<String, Object> row : queryResult) {
                    String toUser = (String) row.get("toUser");
                    Long totalApplications = ((Number) row.get("totalApplications")).longValue();
                    Long pendingApplications = ((Number) row.get("pendingApplications")).longValue();
                    Long inProgressApplications = ((Number) row.get("inProgressApplications")).longValue();
                    Long approvedApplications = ((Number) row.get("approvedApplications")).longValue();
                    Long rejectedApplications = ((Number) row.get("rejectedApplications")).longValue();

                    Map<String, Long> countsMap = new HashMap<>();
                    countsMap.put("totalApplications", totalApplications);
                    countsMap.put("pendingApplications", pendingApplications);
                    countsMap.put("inProgressApplications", inProgressApplications);
                    countsMap.put("approvedApplications", approvedApplications);
                    countsMap.put("rejectedApplications", rejectedApplications);

                    resultMap.put(toUser, countsMap);
                }

                ApiReponse success = ApiReponse.builder()
                        .message("Data fetched successfully")
                        .statusCode(HttpStatus.OK.value())
                        .flag("Success")
                        .data(resultMap)
                        .build();

                return ResponseEntity.ok(success);
            }

            if (role.equalsIgnoreCase("ce1")) {

                // Fetch the list of Ce1 users
                List<String> listOfCe2 = nocService.fetchAllCe2(username);
                List<Map<String, Object>> queryResult = nocService.fetchApplicationCountsForCe1Users(listOfCe2, financialYear);


                Map<String, Map<String, Long>> resultMap = new HashMap<>();

                for (Map<String, Object> row : queryResult) {
                    String toUser = (String) row.get("toUser");
                    Long totalApplications = ((Number) row.get("totalApplications")).longValue();
                    Long pendingApplications = ((Number) row.get("pendingApplications")).longValue();
                    Long inProgressApplications = ((Number) row.get("inProgressApplications")).longValue();
                    Long approvedApplications = ((Number) row.get("approvedApplications")).longValue();
                    Long rejectedApplications = ((Number) row.get("rejectedApplications")).longValue();

                    Map<String, Long> countsMap = new HashMap<>();
                    countsMap.put("totalApplications", totalApplications);
                    countsMap.put("pendingApplications", pendingApplications);
                    countsMap.put("inProgressApplications", inProgressApplications);
                    countsMap.put("approvedApplications", approvedApplications);
                    countsMap.put("rejectedApplications", rejectedApplications);


                    resultMap.put(toUser, countsMap);
                }

                ApiReponse success = ApiReponse.builder()
                        .message("Data fetched successfully")
                        .statusCode(HttpStatus.OK.value())
                        .flag("Success")
                        .data(resultMap)
                        .build();

                return ResponseEntity.ok(success);
            }

            if (role.equalsIgnoreCase("ce2")) {


                // Fetch the list of Ce1 users
                List<String> listOfSe = nocService.getAllSeWhereCe2(username);
                List<Map<String, Object>> queryResult = nocService.fetchApplicationCountsForCe1Users(listOfSe, financialYear);


                Map<String, Map<String, Long>> resultMap = new HashMap<>();

                for (Map<String, Object> row : queryResult) {
                    String toUser = (String) row.get("toUser");
                    Long totalApplications = ((Number) row.get("totalApplications")).longValue();
                    Long pendingApplications = ((Number) row.get("pendingApplications")).longValue();
                    Long inProgressApplications = ((Number) row.get("inProgressApplications")).longValue();
                    Long approvedApplications = ((Number) row.get("approvedApplications")).longValue();
                    Long rejectedApplications = ((Number) row.get("rejectedApplications")).longValue();

                    Map<String, Long> countsMap = new HashMap<>();
                    countsMap.put("totalApplications", totalApplications);
                    countsMap.put("pendingApplications", pendingApplications);
                    countsMap.put("inProgressApplications", inProgressApplications);
                    countsMap.put("approvedApplications", approvedApplications);
                    countsMap.put("rejectedApplications", rejectedApplications);

                    resultMap.put(toUser, countsMap);
                }

                ApiReponse success = ApiReponse.builder()
                        .message("Data fetched successfully")
                        .statusCode(HttpStatus.OK.value())
                        .flag("Success")
                        .data(resultMap)
                        .build();

                return ResponseEntity.ok(success);
            }

            if (role.equalsIgnoreCase("se")) {


                // Fetch the list of Ce1 users
                List<String> fetchListOfEe = nocService.fetchListOfEeforSE(username);
                List<Map<String, Object>> queryResult = nocService.fetchApplicationCountsForCe1Users(fetchListOfEe, financialYear);


                Map<String, Map<String, Long>> resultMap = new HashMap<>();

                for (Map<String, Object> row : queryResult) {
                    String toUser = (String) row.get("toUser");
                    Long totalApplications = ((Number) row.get("totalApplications")).longValue();
                    Long pendingApplications = ((Number) row.get("pendingApplications")).longValue();
                    Long inProgressApplications = ((Number) row.get("inProgressApplications")).longValue();
                    Long approvedApplications = ((Number) row.get("approvedApplications")).longValue();
                    Long rejectedApplications = ((Number) row.get("rejectedApplications")).longValue();

                    Map<String, Long> countsMap = new HashMap<>();
                    countsMap.put("totalApplications", totalApplications);
                    countsMap.put("pendingApplications", pendingApplications);
                    countsMap.put("inProgressApplications", inProgressApplications);
                    countsMap.put("approvedApplications", approvedApplications);
                    countsMap.put("rejectedApplications", rejectedApplications);

                    resultMap.put(toUser, countsMap);
                }

                ApiReponse success = ApiReponse.builder()
                        .message("Data fetched successfully")
                        .statusCode(HttpStatus.OK.value())
                        .flag("Success")
                        .data(resultMap)
                        .build();

                return ResponseEntity.ok(success);
            }


            return null;

        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder()
                    .message("Failed to Fetch Data")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .flag("Error")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @GetMapping("/AnalyticsForCE2")
    public ResponseEntity<ApiReponse> getDepartmentDashboardStatsforonclickCE2(@RequestParam String ce1Region, @RequestParam String financialYear) {

        try {

            List<String> listOfCe2 = nocService.fetchAllCe2(ce1Region);

            List<Map<String, Object>> queryResult = nocService.fetchApplicationCountsForClickCE2(listOfCe2, financialYear);

            Map<String, Map<String, Long>> resultMap = new HashMap<>();

            for (Map<String, Object> row : queryResult) {
                String toUser = (String) row.get("toUser");
                Long totalApplications = ((Number) row.get("totalApplications")).longValue();
                Long pendingApplications = ((Number) row.get("pendingApplications")).longValue();
                Long inProgressApplications = ((Number) row.get("inProgressApplications")).longValue();
                Long approvedApplications = ((Number) row.get("approvedApplications")).longValue();
                Long rejectedApplications = ((Number) row.get("rejectedApplications")).longValue();

                Map<String, Long> countsMap = new HashMap<>();
                countsMap.put("totalApplications", totalApplications);
                countsMap.put("pendingApplications", pendingApplications);
                countsMap.put("inProgressApplications", inProgressApplications);
                countsMap.put("approvedApplications", approvedApplications);
                countsMap.put("rejectedApplications", rejectedApplications);


                resultMap.put(toUser, countsMap);
            }

            ApiReponse success = ApiReponse.builder()
                    .message("Data fetched successfully")
                    .statusCode(HttpStatus.OK.value())
                    .flag("Success")
                    .data(resultMap)
                    .build();

            return ResponseEntity.ok(success);

        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder()
                    .message("Failed to Fetch Data")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .flag("Error")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @GetMapping("/AnalyticsForSE")
    public ResponseEntity<ApiReponse> getDepartmentDashboardStatsforonclickSE(@RequestParam String ce2Region, @RequestParam String financialYear) {

        try {

            List<String> listOfSe = nocService.getAllSeWhereCe2(ce2Region);

            List<Map<String, Object>> queryResult = nocService.fetchApplicationCountsForClickSE(listOfSe, financialYear);

            Map<String, Map<String, Long>> resultMap = new HashMap<>();

            for (Map<String, Object> row : queryResult) {
                String toUser = (String) row.get("toUser");
                Long totalApplications = ((Number) row.get("totalApplications")).longValue();
                Long pendingApplications = ((Number) row.get("pendingApplications")).longValue();
                Long inProgressApplications = ((Number) row.get("inProgressApplications")).longValue();
                Long approvedApplications = ((Number) row.get("approvedApplications")).longValue();
                Long rejectedApplications = ((Number) row.get("rejectedApplications")).longValue();

                Map<String, Long> countsMap = new HashMap<>();
                countsMap.put("totalApplications", totalApplications);
                countsMap.put("pendingApplications", pendingApplications);
                countsMap.put("inProgressApplications", inProgressApplications);
                countsMap.put("approvedApplications", approvedApplications);
                countsMap.put("rejectedApplications", rejectedApplications);

                resultMap.put(toUser, countsMap);
            }

            ApiReponse success = ApiReponse.builder()
                    .message("Data fetched successfully")
                    .statusCode(HttpStatus.OK.value())
                    .flag("Success")
                    .data(resultMap)
                    .build();

            return ResponseEntity.ok(success);

        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder()
                    .message("Failed to Fetch Data")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .flag("Error")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


//    @GetMapping("/AnalyticsForDepartmentTotalApplication")
//    public ResponseEntity<ApiReponse> getDepartmentDashboardStatsforTotalApplication(@RequestParam String financialYear) {
//        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().toArray()[0].toString();
//
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        try {
//            List<Map<String, Object>> queryResult = nocService.fetchTotalApplicationCountForGraph(username, financialYear);
//
//
//            Map<String, Map<String, Long>> resultMap = new HashMap<>();
//
//            for (Map<String, Object> row : queryResult) {
//                String toUser = (String) row.get("noc_name");
//                Long totalApplications = ((Number) row.get("applied_for_count")).longValue();
//
//                Map<String, Long> countsMap = new HashMap<>();
//                countsMap.put("totalApplications", totalApplications);
//
//                resultMap.put(toUser, countsMap);
//            }
//
//            ApiReponse success = ApiReponse.builder()
//                    .message("Data fetched successfully")
//                    .statusCode(HttpStatus.OK.value())
//                    .flag("Success")
//                    .data(resultMap)
//                    .build();
//
//            return ResponseEntity.ok(success);
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            ApiReponse error = ApiReponse.builder()
//                    .message("Failed to Fetch Data")
//                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                    .flag("Error")
//                    .build();
//
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
//        }
//    }

    @GetMapping("/AnalyticsForDepartmentTotalApplication")
    public ResponseEntity<ApiReponse> getDepartmentDashboardStatsforTotalApplication(@RequestParam String financialYear) {
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().toArray()[0].toString();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
//            List<Map<String, Object>> queryResult = nocService.fetchTotalApplicationCountForGraph(username, financialYear);
            if (role.equalsIgnoreCase("admin")) {
                List<Map<String, Object>> queryResult = nocService.fetchTotalApplicationCountForGraphforAdmin(financialYear);


                Map<String, Map<String, Long>> resultMap = new HashMap<>();

                for (Map<String, Object> row : queryResult) {
                    String toUser = (String) row.get("noc_name");
                    // Handle null values directly
                    Long totalApplications = (row.get("applied_for_count") != null) ? ((Number) row.get("applied_for_count")).longValue() : 0;
                    Long approvedApplications = (row.get("approved_count") != null) ? ((Number) row.get("approved_count")).longValue() : 0;
                    Long pendingApplications = (row.get("pending_count") != null) ? ((Number) row.get("pending_count")).longValue() : 0;
                    Long rejectedApplications = (row.get("rejected_count") != null) ? ((Number) row.get("rejected_count")).longValue() : 0;
                    Long processApplications = (row.get("process_count") != null) ? ((Number) row.get("process_count")).longValue() : 0;

                    Map<String, Long> countsMap = new HashMap<>();
                    countsMap.put("totalApplications", totalApplications);
                    countsMap.put("approvedApplications", approvedApplications);
                    countsMap.put("pendingApplications", pendingApplications);
                    countsMap.put("rejectedApplications", rejectedApplications);
                    countsMap.put("processApplications", processApplications);

                    resultMap.put(toUser, countsMap);
                }

                ApiReponse success = ApiReponse.builder()
                        .message("Data fetched successfully")
                        .statusCode(HttpStatus.OK.value())
                        .flag("Success")
                        .data(resultMap)
                        .build();

                return ResponseEntity.ok(success);
            } else {
                List<Map<String, Object>> queryResult = nocService.fetchTotalApplicationCountForGraph(username, financialYear);


                Map<String, Map<String, Long>> resultMap = new HashMap<>();

                for (Map<String, Object> row : queryResult) {
                    String toUser = (String) row.get("noc_name");
                    // Handle null values directly
                    Long totalApplications = (row.get("applied_for_count") != null) ? ((Number) row.get("applied_for_count")).longValue() : 0;
                    Long approvedApplications = (row.get("approved_count") != null) ? ((Number) row.get("approved_count")).longValue() : 0;
                    Long pendingApplications = (row.get("pending_count") != null) ? ((Number) row.get("pending_count")).longValue() : 0;
                    Long rejectedApplications = (row.get("rejected_count") != null) ? ((Number) row.get("rejected_count")).longValue() : 0;
                    Long processApplications = (row.get("process_count") != null) ? ((Number) row.get("process_count")).longValue() : 0;

                    Map<String, Long> countsMap = new HashMap<>();
                    countsMap.put("totalApplications", totalApplications);
                    countsMap.put("approvedApplications", approvedApplications);
                    countsMap.put("pendingApplications", pendingApplications);
                    countsMap.put("rejectedApplications", rejectedApplications);
                    countsMap.put("processApplications", processApplications);

                    resultMap.put(toUser, countsMap);
                }

                ApiReponse success = ApiReponse.builder()
                        .message("Data fetched successfully")
                        .statusCode(HttpStatus.OK.value())
                        .flag("Success")
                        .data(resultMap)
                        .build();

                return ResponseEntity.ok(success);
            }

        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder()
                    .message("Failed to Fetch Data")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .flag("Error")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping(path = "/findAplicationId/{id}")
    public List<Application> findApplicationId(@PathVariable(value = "id") String id) {

        List<Application> fetchedRow = nocService.fetchAllDataOfAppUniqueId(id);

        return fetchedRow;
    }

    @GetMapping(path = "/findAppCurrStatusId/{id}")
    public List<MovementStatus> findAppCurrStatusId(@PathVariable(value = "id") String id) {

        List<MovementStatus> fetchedRow = nocService.fetchAllDataOfCurrAppId(id);


        return fetchedRow;
    }


    @GetMapping(path = "/findAllLettersFromAppCurrid/{id}")
    public List<ListOfLetters> findAllLettersFromAppCurrid(@PathVariable(value = "id") String id) {

        List<ListOfLetters> fetchedRow = nocService.fetchAllAllLettersFromAppCurrid(id);


        return fetchedRow;
    }


    @GetMapping(path = "/fetchCe1List/{appId}")
    public List<String> fetchCe1ListOrCe1(@PathVariable(value = "appId") Long appId) {

        List<String> listOfCe1 = nocService.fetchCe1ListOrCe1(appId);

        return listOfCe1;
    }

    @GetMapping(path = "/fetchCe2List/{ce1}/{appId}")
    public List<String> fetchCe2ListOrCe2(@PathVariable(value = "ce1") String ce1, @PathVariable(value = "appId") Long appId) {

        List<String> listOfCe = nocService.fetchCe2ListOrCe2(ce1, appId);

        return listOfCe;
    }

    @GetMapping(path = "/fetchSeList/{ce2}/{appId}")
    public List<String> fetchSeListOrSe(@PathVariable(value = "ce2") String ce2, @PathVariable(value = "appId") Long appId) {

        List<String> listOfSe = nocService.fetchSeListOrSe(ce2, appId);

        return listOfSe;
    }

    @GetMapping(path = "/fetchEeList/{se}/{appId}")
    public List<String> fetchEeListOrEe(@PathVariable(value = "se") String se, @PathVariable(value = "appId") Long appId) {

        List<String> listOfEe = nocService.fetchEeListOrEe(se, appId);

        return listOfEe;
    }


    @GetMapping(path = "/vieworderpdf/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> fetchPdfOfOrder(@PathVariable(value = "id") Long id) {

        try {
            byte[] pdfData = nocService.fetchPdfOfOrderById(id);

            // Check if the PDF data is not null and has content
            if (pdfData == null || pdfData.length == 0) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No PDF data found for the provided ID.".getBytes());
            }

            // Create the success response with PDF data
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_PDF) // Set the content type to PDF
                    .body(pdfData);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch PDF data.".getBytes());
        }
    }

    @GetMapping(path = "/viewfeedbackimage/{id}", produces = MediaType.ALL_VALUE)
    public ResponseEntity<byte[]> fetchImageOfFeedback(@PathVariable(value = "id") Long id) {

        try {
            byte[] imageData = nocService.fetchImageOfFeedbackById(id);

            // Check if the PDF data is not null and has content
            if (imageData == null || imageData.length == 0) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No PDF data found for the provided ID.".getBytes());
            }

            // Create the success response with PDF data
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_PDF) // Set the content type to PDF
                    .body(imageData);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch PDF data.".getBytes());
        }
    }


    //    This Method For Checking Application Transfer Status = Forwarding
    @GetMapping("/checkApplicationStatusForwarded/{applicationId}")
    public ResponseEntity<ApiReponse> checkApplicationTransferredToEe(@PathVariable("applicationId") Long applicationId) {
        try {
            boolean isTransferred = nocService.checkApplicationTransferredToEe(applicationId);

            HashMap<String, Object> response = new HashMap<>();
            response.put("isTransferred", isTransferred);

            ApiReponse apiResponse = ApiReponse.builder()
                    .message("Data Fetched Successfully!")
                    .statusCode(HttpStatus.OK.value())
                    .flag("Success")
                    .data(response)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder()
                    .message("Failed to Fetch Data")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .flag("Error")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    //    This Method For Checking Application Transfer Status = Cross Verification
    @GetMapping("/checkApplicationStatusCrossVerification/{applicationId}")
    public ResponseEntity<ApiReponse> checkApplicationStatusCrossVerification(@PathVariable("applicationId") Long applicationId) {
        try {
            boolean isTransferred = nocService.checkApplicationTrasferStatusCrossVerification(applicationId);

            HashMap<String, Object> response = new HashMap<>();
            response.put("isTransferred", isTransferred);

            ApiReponse apiResponse = ApiReponse.builder()
                    .message("Data Fetched Successfully!")
                    .statusCode(HttpStatus.OK.value())
                    .flag("Success")
                    .data(response)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder()
                    .message("Failed to Fetch Data")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .flag("Error")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    //    This Method For Checking Application Transfer Status = Cross Verified
//    @GetMapping("/checkApplicationStatusCrossVerified/{applicationId}")
//    public ResponseEntity<ApiReponse> checkApplicationStatusApproval(@PathVariable("applicationId") Long applicationId) {
//        try {
//            boolean isTransferred = nocService.checkApplicationStatusApproval(applicationId);
//
//            HashMap<String, Object> response = new HashMap<>();
//            response.put("isTransferred", isTransferred);
//
//            ApiReponse apiResponse = ApiReponse.builder()
//                    .message("Data Fetched Successfully!")
//                    .statusCode(HttpStatus.OK.value())
//                    .flag("Success")
//                    .data(response)
//                    .build();
//
//            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
//        } catch (Exception e) {
//            e.printStackTrace();
//            ApiReponse error = ApiReponse.builder()
//                    .message("Failed to Fetch Data")
//                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                    .flag("Error")
//                    .build();
//
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
//        }
//    }

    //    This Method For Checking Application Transfer Status = Cross Verified
    @GetMapping("/checkApplicationStatusCrossVerified/{applicationId}")
    public ResponseEntity<ApiReponse> checkApplicationStatusCrossVerified(@PathVariable("applicationId") Long applicationId) {
        try {
            boolean isTransferred = nocService.checkApplicationStatusCrossVerified(applicationId);

            HashMap<String, Object> response = new HashMap<>();
            response.put("isTransferred", isTransferred);

            ApiReponse apiResponse = ApiReponse.builder()
                    .message("Data Fetched Successfully!")
                    .statusCode(HttpStatus.OK.value())
                    .flag("Success")
                    .data(response)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder()
                    .message("Failed to Fetch Data")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .flag("Error")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    //    This Method For Checking Application Transfer Status = APPROVAL
    @GetMapping("/checkApplicationStatusApproval/{applicationId}")
    public ResponseEntity<ApiReponse> checkApplicationStatusApproved(@PathVariable("applicationId") Long applicationId) {
        try {
            boolean isTransferred = nocService.checkApplicationStatusApproved(applicationId);

            HashMap<String, Object> response = new HashMap<>();
            response.put("isTransferred", isTransferred);

            ApiReponse apiResponse = ApiReponse.builder()
                    .message("Data Fetched Successfully!")
                    .statusCode(HttpStatus.OK.value())
                    .flag("Success")
                    .data(response)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder()
                    .message("Failed to Fetch Data")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .flag("Error")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    //    This Method For Checking Application Transfer Status = Issuance And Noc Type = Jal Avantan
    @GetMapping("/checkApplicationStatusIssuanceAndNocType/{applicationId}")
    public ResponseEntity<ApiReponse> checkApplicationStatusIssuanceAndNocType(@PathVariable("applicationId") Long applicationId) {
        try {
            boolean isTransferred = nocService.checkApplicationStatusIssuanceAndNocType(applicationId);

            HashMap<String, Object> response = new HashMap<>();
            response.put("isTransferred", isTransferred);

            ApiReponse apiResponse = ApiReponse.builder()
                    .message("Data Fetched Successfully!")
                    .statusCode(HttpStatus.OK.value())
                    .flag("Success")
                    .data(response)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder()
                    .message("Failed to Fetch Data")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .flag("Error")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    //    Saving Attached Row Data  in Curr & Logs of Document
    @PostMapping(value = "/submitAttachedDocRowData")
    public ResponseEntity<ApiReponse> savingAttachDocRowData(
            @RequestParam("applicationId") Long applicationId,
            @RequestParam("description") String description,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("remarks") String remarks, @RequestParam("nocName") String nocName, @RequestParam("attachedDocumentType") String attachedDocumentType) {

        try {

            String specialMessage = nocService.savingAttachDocRowData(applicationId, description, file, remarks, nocName, attachedDocumentType);

            HashMap<String, Object> response = new HashMap<>();
            response.put("specialMesaage", specialMessage);
            response.put("success", true);

            ApiReponse apiResponse = ApiReponse.builder().message("Data Fetched Successfully!").statusCode(HttpStatus.OK.value()).flag("Success").data(response).build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder().message("Failed to Save  Data").statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).flag("Error").build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

    }

    //    Saving Generated Letter   in Curr & Logs of Document
    @PostMapping(value = "/uploadPdfToForm")
    public ResponseEntity<ApiReponse> savingUplodadPdfToForm(
            @RequestParam("applicationId") Long applicationId,
            @RequestParam("description") String description,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("associationId") String associationId, @RequestParam("nocName") String nocName) {

        try {

            String specialMessage = nocService.savingUplodadPdfToForm(applicationId, description, file, associationId, nocName);

            HashMap<String, Object> response = new HashMap<>();
            response.put("specialMesaage", specialMessage);
            response.put("success", true);

            ApiReponse apiResponse = ApiReponse.builder().message("Data Fetched Successfully!").statusCode(HttpStatus.OK.value()).flag("Success").data(response).build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder().message("Failed to Save  Data").statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).flag("Error").build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

    }

    //    This Method For Get Agency Approved Applications
    @GetMapping("/agencyApplicationsByStatus")
    public ResponseEntity<ApiReponse> getAgencyApplicationsByStatus(@RequestParam String financialYear, @RequestParam String status) {

        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            List<Map<String, Object>> agencyTableData = nocService.getAgencyApplicationsWhereStatus(username, financialYear, status);

            // Combine the stats and agency table data into one response map
            Map<String, Object> response = new HashMap<>();
            response.put("agencyTableData", agencyTableData);

            // Build and return the API response
            ApiReponse apiResponse = ApiReponse.builder()
                    .message("Data Fetched Successfully!")
                    .statusCode(HttpStatus.OK.value())
                    .flag("Success")
                    .data(response)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder()
                    .message("Failed to Fetch Data")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .flag("Error")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    //    This Method For Get Application Details For Admin
    @GetMapping("/applicationStatus")
    public ResponseEntity<ApiReponse> getApplicationsByStatus(@RequestParam String financialYear, @RequestParam String status) {

        try {
            // Fetch the data using the service
            List<Map<String, Object>> agencyTableData = nocService.getApplicationDataByFinYearAndStatus(financialYear, status);

            // Combine the stats and agency table data into a response map
            Map<String, Object> response = new HashMap<>();
            response.put("agencyTableData", agencyTableData);

            // Build and return the API response
            ApiReponse apiResponse = ApiReponse.builder()
                    .message("Data Fetched Successfully!")
                    .statusCode(HttpStatus.OK.value())
                    .flag("Success")
                    .data(response)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse errorResponse = ApiReponse.builder()
                    .message("Failed to Fetch Data")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .flag("Error")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    //
    //    This Method For Get Status of  Current Applications
    @GetMapping("/getStatusOfCurrApp/{applicationId}")
    public ResponseEntity<ApiReponse> getStatusOfCurrApplication(@PathVariable("applicationId") Long applicationId) {

        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            List<Application> status = nocService.getAgencyApplicationsStatus(applicationId);

            // Combine the stats and agency table data into one response map
            Map<String, Object> response = new HashMap<>();
            response.put("status", status);

            // Build and return the API response
            ApiReponse apiResponse = ApiReponse.builder()
                    .message("Data Fetched Successfully!")
                    .statusCode(HttpStatus.OK.value())
                    .flag("Success")
                    .data(response)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder()
                    .message("Failed to Fetch Data")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .flag("Error")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/feedback/update")

    public ResponseEntity<ApiReponse> updateFeedback(@ModelAttribute Feedback feedback) {
        try {
            Feedback updatedFeedback = nocService.updateFeedback(feedback);

            HashMap<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("ipdatefeedback", updatedFeedback);

            ApiReponse apiResponse = ApiReponse.builder().message("Data Updated Successfully!").statusCode(HttpStatus.OK.value()).flag("Success").data(response).build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder().message("Failed to Update  Data").statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).flag("Error").build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

    }

    @GetMapping("/feedback/{id}")
    public ResponseEntity<ApiReponse> getFeedbackListById(@PathVariable Long id) {

        try {
            Feedback feedback = nocService.findFeedbackById(id);

            if (feedback == null) {
                ApiReponse apiResponse = ApiReponse.builder()
                        .message("Feedback not found")
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .flag("Error")
                        .build();

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
            }

            HashMap<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("feedback", feedback);

            ApiReponse apiResponse = ApiReponse.builder()
                    .message("Data Fetched Successfully!")
                    .statusCode(HttpStatus.OK.value())
                    .flag("Success")
                    .data(response)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();

            ApiReponse error = ApiReponse.builder()
                    .message("Failed to Fetch Data")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .flag("Error")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    //code for Combine PDF view
    @GetMapping(path = "/viewCombinedDocument/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> fetchCombinePdfOfCurrentDocumentById(@PathVariable(value = "id") Long id) {
        System.out.println("id" + id);
        try {
            byte[] pdfData = nocService.fetchCombinePdfOfCurrentDocumentById(id);

            // Check if the PDF data is not null and has content
            if (pdfData == null || pdfData.length == 0) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No PDF data found for the provided ID.".getBytes());
            }

            // Create the success response with PDF data
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_PDF) // Set the content type to PDF
                    .body(pdfData);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch PDF data.".getBytes());
        }
    }

    //    get the index
    @GetMapping("/getIndexValueByNocType/{value}")
    public ResponseEntity<ApiReponse> getIndexValueByNocType(@PathVariable String value) {

        try {
            NocList nocListData = nocService.findIndexValueByNocTyp(value);

            if (nocListData == null) {
                ApiReponse apiResponse = ApiReponse.builder()
                        .message("Order not found")
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .flag("Error")
                        .build();

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
            }

            HashMap<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("nocListData", nocListData);

            ApiReponse apiResponse = ApiReponse.builder()
                    .message("Data Fetched Successfully!")
                    .statusCode(HttpStatus.OK.value())
                    .flag("Success")
                    .data(response)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();

            ApiReponse error = ApiReponse.builder()
                    .message("Failed to Fetch Data")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .flag("Error")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @GetMapping("/internalDepartmentStatistics")
    public ResponseEntity<ApiReponse> getInternalDepartmentStatistics() {
        HashMap<String, Object> response = new HashMap<>();
        try {

            //            for cheif engineer 1
            List<Map<String, Object>> ce1List = nocService.findListOfCE1ForInternalDepartmentStats();

//            for cheif engineer 2
            List<Map<String, Object>> ce2List = nocService.findListOfCE2ForInternalDepartmentStats();

//            for se
            List<Map<String, Object>> seList = nocService.findListOfSeForInternalDepartmentStats();

//            for ee
            List<Map<String, Object>> eeList = nocService.findListOfeeForInternalDepartmentStats();


            response.put("success", true);
            response.put("message", "All  list fetched successfully");
            response.put("ce1List", ce1List);
            response.put("ce2List", ce2List);
            response.put("seList", seList);
            response.put("eeList", eeList);


            ApiReponse apiResponse = ApiReponse.builder().message("Data Fetched Successfully!").
                    statusCode(HttpStatus.OK.value()).
                    flag("Success").
                    data(response).build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();

            ApiReponse error = ApiReponse.builder()
                    .message("Failed to Fetch Data")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .flag("Error")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

    }

    @GetMapping("/officerWiseReportTable")
    public ResponseEntity<ApiReponse> getOfficerTableStats() {

        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().toArray()[0].toString();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        HashMap<String, Object> response = new HashMap<>();
        try {

            if (role.equalsIgnoreCase("admin")) {

                //            for cheif engineer I
                List<Map<String, Object>> ce1TableList = nocService.findListOfCEIforOfficerWiseReport();

                //            for cheif engineer II
                List<Map<String, Object>> ce2TableList = nocService.findListOfCEIIforOfficerWiseReport();

                //            for SuperIntendent
                List<Map<String, Object>> seTableList = nocService.findListOfSeforOfficerWiseReport();

                //            Executive Engineer
                List<Map<String, Object>> eeTableList = nocService.findListOfExecutiveEngineerforOfficerWiseReport();

                response.put("success", true);
                response.put("message", "All  list fetched successfully");
                response.put("ce1TableList", ce1TableList);
                response.put("ce2TableList", ce2TableList);
                response.put("seTableList", seTableList);
                response.put("eeTableList", eeTableList);


                ApiReponse apiResponse = ApiReponse.builder().message("Data Fetched Successfully!").
                        statusCode(HttpStatus.OK.value()).
                        flag("Success").
                        data(response).build();

                return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
            }

            if (role.equalsIgnoreCase("ce1")) {

                List<String> listOfCe2 = nocService.fetchAllCe2(username);

                System.out.println("listOfCe2 " + listOfCe2);
                //            for cheif engineer II
                List<Map<String, Object>> ce2TableList = nocService.findListOfCEIIforOfficerWiseReportForParticularRoleCe1(listOfCe2);

                //            for SuperIntendent

//                Fetch List of SE By Ce1 Name
                List<String> listOfSeByCE1 = nocService.fetchListOfSeByCE1(username);
                List<Map<String, Object>> seTableList = nocService.findListOfSeforOfficerWiseReportForParticularRoleCe1(listOfSeByCE1);

                //            Executive Engineer
                List<String> listOfEEByCE1 = nocService.fetchListOfEEByCE1(username);
                List<Map<String, Object>> eeTableList = nocService.findListOfExecutiveEngineerforOfficerWiseReportForParticularRoleCe1(listOfEEByCE1);

                response.put("success", true);
                response.put("message", "All  list fetched successfully");

                response.put("ce2TableList", ce2TableList);
                response.put("seTableList", seTableList);
                response.put("eeTableList", eeTableList);

                ApiReponse apiResponse = ApiReponse.builder().message("Data Fetched Successfully!").
                        statusCode(HttpStatus.OK.value()).
                        flag("Success").
                        data(response).build();

                return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
            }

            if (role.equalsIgnoreCase("ce2")) {


                List<String> listOfSe = nocService.getAllSeWhereCe2(username);

                List<Map<String, Object>> seTableList = nocService.findListOfSeforOfficerWiseReportForParticularRoleCe1(listOfSe);

                //            Executive Engineer
                List<String> listOfEEByCE2 = nocService.fetchListOfEEByCE2(username);
                List<Map<String, Object>> eeTableList = nocService.findListOfExecutiveEngineerforOfficerWiseReportForParticularRoleCe1(listOfEEByCE2);

                response.put("success", true);
                response.put("message", "All  list fetched successfully");


                response.put("seTableList", seTableList);
                response.put("eeTableList", eeTableList);

                ApiReponse apiResponse = ApiReponse.builder().message("Data Fetched Successfully!").
                        statusCode(HttpStatus.OK.value()).
                        flag("Success").
                        data(response).build();

                return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
            }

            if (role.equalsIgnoreCase("se")) {


                //            Executive Engineer
                List<String> listOfEe = nocService.getAllEeWhereSe(username);
                List<Map<String, Object>> eeTableList = nocService.findListOfExecutiveEngineerforOfficerWiseReportForParticularRoleCe1(listOfEe);


                response.put("success", true);
                response.put("message", "All  list fetched successfully");


                response.put("eeTableList", eeTableList);

                ApiReponse apiResponse = ApiReponse.builder().message("Data Fetched Successfully!").
                        statusCode(HttpStatus.OK.value()).
                        flag("Success").
                        data(response).build();

                return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
            }


            return null;
        } catch (Exception e) {
            e.printStackTrace();

            ApiReponse error = ApiReponse.builder()
                    .message("Failed to Fetch Data")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .flag("Error")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    //    This Method For Get Ce1 Name in Application
    @GetMapping("/getCe1NameWhereAppId/{appId}")
    public ResponseEntity<ApiReponse> getCe1NameWhereAppId(@PathVariable("appId") String appId) {

        try {

            String ce1Names = nocService.fetchCe1NameWhereAppId(appId);

            // Combine the stats and agency table data into one response map
            Map<String, Object> response = new HashMap<>();
            response.put("ce1Names", ce1Names);

            // Build and return the API response
            ApiReponse apiResponse = ApiReponse.builder()
                    .message("Data Fetched Successfully!")
                    .statusCode(HttpStatus.OK.value())
                    .flag("Success")
                    .data(response)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder()
                    .message("Failed to Fetch Data")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .flag("Error")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    //    This Method For Get Application Data By Using Special Id Where transfer Status = FOR_ISSUANCE
    @GetMapping("/fetchApplicationDetails/{specialId}")
    public ResponseEntity<ApiReponse> fetchApplicationDetails(@PathVariable("specialId") String specialId) {

        try {

            List<Map<String, Object>> application = nocService.fetchApplicationDetailsBySpecialId(specialId);

            // Combine the stats and agency table data into one response map
            Map<String, Object> response = new HashMap<>();
            response.put("application", application);

            // Build and return the API response
            ApiReponse apiResponse = ApiReponse.builder()
                    .message("Data Fetched Successfully!")
                    .statusCode(HttpStatus.OK.value())
                    .flag("Success")
                    .data(response)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);

        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder()
                    .message("Failed to Fetch Data")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .flag("Error")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // Method For Ce1 Certificate Issued Pdf Save To Local Storage
    @PostMapping(value = "/certificateIssued")
    public ResponseEntity<ApiReponse> applicationTransferToUsers(@RequestParam("specialId") String specialId, @RequestParam("pdfFile") MultipartFile file,
                                                                 @RequestParam("nocName") String nocName, @RequestParam("agencyName") String agencyName) {

        try {

            nocService.saveIssuedCertificate(specialId, file, nocName, agencyName);

            HashMap<String, Object> response = new HashMap<>();
            response.put("success", true);

            ApiReponse apiResponse = ApiReponse.builder()
                    .message("Data Fetched Successfully!")
                    .statusCode(HttpStatus.OK.value())
                    .flag("Success")
                    .data(response)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder()
                    .message("Failed to Save Data")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .flag("Error")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

//    For View Certificate By using Special Id

    @GetMapping(path = "/fetchCertificateBySpecialId/{applicationId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> fetchCertificateBySpecialId(@PathVariable(value = "applicationId") String applicationId) {

        try {
            byte[] pdfData = nocService.fetchCertificateBySpecialId(applicationId);

            // Check if the PDF data is not null and has content
            if (pdfData == null || pdfData.length == 0) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("No PDF data found for the provided ID.".getBytes());
            }

            // Create the success response with PDF data
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_PDF) // Set the content type to PDF
                    .body(pdfData);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch PDF data.".getBytes());
        }
    }


    @GetMapping("/fetchUserIdFromUserSpring/{username}")
    public ResponseEntity<ApiReponse> fetchUserIdFromUserSpring(@PathVariable(value = "username") String username) {

        try {
            String userId = nocService.fetchUserIdFromUserSpring(username);
            String agencyName = nocService.fetchUserIdFromUserRepo(username);
            HashMap<String, Object> response = new HashMap<>();

            response.put("success", true);
            response.put("userId", userId);
            response.put("agencyName", agencyName);


            ApiReponse apiResponse = ApiReponse.builder().message("Data Fetched Successfully!").statusCode(HttpStatus.OK.value()).flag("Success").data(response).build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder().message("Failed to Fetched  Data").statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).flag("Error").build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

//    Sign Up Process
    @PostMapping(value = "/signUpProcess")
    public ResponseEntity<ApiReponse> signUpProcess(@ModelAttribute UsersSpring usersSpring,@ModelAttribute UserDetails userDetails) {

        try {

            nocService.saveAgencySignUpDetails(usersSpring, userDetails);

            HashMap<String, Object> response = new HashMap<>();
            response.put("success", true);

            ApiReponse apiResponse = ApiReponse.builder()
                    .message("Data Fetched Successfully!")
                    .statusCode(HttpStatus.OK.value())
                    .flag("Success")
                    .data(response)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiReponse error = ApiReponse.builder()
                    .message("Failed to Save Data")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .flag("Error")
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/checkUsername")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam String username) {

        boolean exists = nocService.usernameExistsOrNot(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/currentUserRole")
    public ResponseEntity<Map<String, String>> getCurrentUserRole() {

        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().toArray()[0].toString();
        Map<String, String> response = new HashMap<>();
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

}
