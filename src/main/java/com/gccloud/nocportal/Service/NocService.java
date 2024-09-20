package com.gccloud.nocportal.Service;


import com.gccloud.nocportal.Entity.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface NocService {

    List<String> findListOfNoc();

    List<String> findListOfDistrict();

    List<String> findListOfCanals();

    List<NocList> findAllNocListData();

    List<CheckList> fetchAllChecklistDataById(Long index);

    Long getTotalApplications();

    Long getApplicationsByStatus(String status);

    Long get7DaysPendingApplication(String pending);

    Long get15DaysPendingApplication(String pending);

    Long getTotalApplicationsForUser(String username,String financialYear);

    Long getApplicationsByStatusForUser(String status, String username,String financialYear);

    Long getPendingApplicationForUser(String status, String username,String financialYear);

    Long getCertificateIssuedForUser(String status, String username,String financialYear);


    List<String> fetchListOfEe();

    List<UsersSpring> fetchRespectiveSeCeOfEechListOfEe(String ee);

    Long getReSubmittedApplicationForUser(String username,String financialYear);

    String[] savingNocFromData(Application application, DocumentStatus documentStatus) throws IOException;

    List<Map<String, Object>> getAgencyTableDataForUser(String username,String financialYear);

    Long getTotalApplicationsByFinYear(String financialYear);

    Long getApplicationsByStatusByFinYear(String approved, String financialYear);

    Long get7DaysPendingApplicationByFinYear(String pending, String financialYear);

    Long get15DaysPendingApplicationByFinYear(String pending, String financialYear);

    List<String> fetchAllCe2(String ce1);

    List<String> getAllSeWhereCe2(String ce2);

    List<String> getAllEeWhereSe(String se);

    List<String> fetchAllCe1();

    List<Map<String, Object>> findLogsByAppCurrentStatusId(Long id);

    void saveAgencySignUpDetails(UsersSpring usersSpring, UserDetails userDetails);

    List<Map<String, Object>> getAllApplicationDataByUser(String financialYear);


    List<Map<String, Object>> getAllApplicationDataByApplicationId(Long id);

    void savingLetters(MultipartFile file,String content ,Long applicationId,String description,String nocType) throws IOException;

    List<Map<String, Object>> getApplicationDataByUserAndByOwnership(String financialYear);

    void updateAppCurrentStatus(Long applicationId, String toUser,String transferRemark);

    void savingTrialPdf(MultipartFile file) throws IOException;

    Long savingOrder(Orders orders);

    List<Orders> findallorder();

    byte[] fetchPdfOfCurrentDocumentById(Long id) throws IOException;


    void deleteorder(Long id);

    List<Map<String, Object>> fetchApplicationCountsForCe1Users(List<String> listOfCe1, String financialYear);

    List<Map<String, Object>> fetchApplicationCountsForClickCE2(List<String> listOfCe2, String finYear);

    List<String> fetchListOfEeforSE(String se);

    List<Map<String, Object>> fetchTotalApplicationCountForGraph(String username, String financialYear);

    void transferApplicationToAgency(Long id);

    String getPresetSeByEeAndAppId(Long appId);

    String presetCe2FromApplicationByAppId(Long appId);

    String presetCe1FromApplicationByAppId(Long appId);

    String presetAdminFromApplicationByAppId(Long appId);

    List<String> fetchCe1ListOrCe1(Long appId);

    List<String> fetchCe2ListOrCe2(String ce1,Long appId);

    List<String> fetchSeListOrSe(String ce2,Long appId);

    List<String> fetchEeListOrEe(String se,Long appId);

    List<Application> fetchAllDataOfAppUniqueId(String id);

    List<MovementStatus> fetchAllDataOfCurrAppId(String id);

    Long savingGrevance(Grievances grievances);

    List<Grievances> findallgrevance();

    void deletegrevance(Long id);

    Orders updateOrders(Orders orders);

    Orders findOrderById(Long id);

    List<Grievances> findallgrevanceByusername(String username);

    Grievances updateGrievances(Grievances grievances);

    Grievances findGrievancesById(Long id);

    Long savingfeedback(Feedback feedback);

    byte[] fetchPdfOfOrderById(Long id) throws IOException;

    List<Feedback> findallfeddback();

    byte[] fetchImageOfFeedbackById(Long id) throws IOException;

    void deletefeedback(Long id);

    boolean checkApplicationTransferredToEe(Long applicationId);


    boolean checkApplicationTrasferStatusCrossVerification(Long applicationId);

    boolean checkApplicationStatusApproval(Long applicationId);

    boolean checkApplicationStatusIssuanceAndNocType(Long applicationId);

    String savingAttachDocRowData(Long applicationId, String description, MultipartFile file, String remarks,String nocName,String attachedDocumentType) throws IOException;


    List<Map<String, Object>> fetchApplicationIDDetail(Long id);

    List<Map<String, Object>> getAgencyApplicationsWhereStatus(String username, String financialYear, String status);


    String[] savingReviewedNocFormData(Application application, DocumentStatus documentStatus);
    Feedback findFeedbackById(Long id);

    Feedback updateFeedback(Feedback feedback);

    String savingUplodadPdfToForm(Long applicationId, String description, MultipartFile file, String associationId,String nocName);

    List<Application> getAgencyApplicationsStatus(Long applicationId);

    List<ListOfLetters> fetchAllAllLettersFromAppCurrid(String id);

    byte[] fetchPdfOfLettersById(Long id) throws IOException;

    List<Map<String, Object>> getApplicationDataByFinYearAndStatus(String financialYear, String status);

    byte[] fetchCombinePdfOfCurrentDocumentById(Long id) throws IOException;

    List<Map<String, Object>> fetchApplicationCountsForClickSE(List<String> listOfSe, String financialYear);

    List<Map<String, Object>> fetchTotalApplicationCountForGraphforAdmin(String financialYear);

    NocList findIndexValueByNocTyp(String value);

    List<String> fetchSubAdmin();

    List<Map<String, Object>> findListOfCE2ForInternalDepartmentStats();

    List<Map<String, Object>> findListOfSeForInternalDepartmentStats();

    List<Map<String, Object>> findListOfeeForInternalDepartmentStats();

    List<Map<String, Object>> findListOfCEIforOfficerWiseReport();

    List<Map<String, Object>> findListOfCEIIforOfficerWiseReport();

    List<Map<String, Object>> findListOfSeforOfficerWiseReport();

    List<Map<String, Object>> findListOfExecutiveEngineerforOfficerWiseReport();

    void saveAttachedLetters(MultipartFile file, Long applicationId, String description, String nocType) throws IOException;

    boolean checkApplicationStatusApproved(Long applicationId);

    boolean checkApplicationStatusCrossVerified(Long applicationId);

    String fetchCe1NameWhereAppId(String appId);

    List<Map<String, Object>> fetchApplicationDetailsBySpecialId(String specialId);


    void saveIssuedCertificate(String specialId, MultipartFile file,String nocName,String agencyName) throws IOException;

    byte[] fetchCertificateBySpecialId(String id) throws IOException;

    List<Map<String, Object>> findListOfCEIIforOfficerWiseReportForParticularRoleCe1(List<String> listOfCe2);

    List<String> fetchListOfSeByCE1(String username);

    List<Map<String, Object>> findListOfSeforOfficerWiseReportForParticularRoleCe1(List<String> listOfSeByCE1);

    List<String> fetchListOfEEByCE1(String username);

    List<Map<String, Object>> findListOfExecutiveEngineerforOfficerWiseReportForParticularRoleCe1(List<String> listOfEEByCE1);

    List<String> fetchListOfEEByCE2(String username);

    List<Map<String, Object>> findListOfCE1ForInternalDepartmentStats();

    List<Map<String, Object>> fetchDataWhenCe1StatusApplied(String status, String ce1, String nocName);

    List<Map<String, Object>> fetchDataWhenCe2StatusApplied(String ce2, String status, String nocName);

    List<Map<String, Object>> fetchDataWhenSEStatusApplied(String se, String status, String nocName);

    List<Map<String, Object>> fetchDataWhenEEStatusApplied(String ee, String status, String nocName);

    List<Map<String, Object>> fetchDataForCe1AndNOCName(String ce1, String nocName);

    List<Map<String, Object>> fetchDataForCe2AndNOCName(String ce2, String nocName);

    List<Map<String, Object>> fetchDataForSEAndNOCName(String se, String nocName);

    List<Map<String, Object>> fetchDataForEEAndNOCName(String ee, String nocName);

    List<Map<String, Object>> fetchDataByCE1andStatus(String ce1, String status);

    List<Map<String, Object>> fetchDataByCE2andStatus(String ce2, String status);

    List<Map<String, Object>> fetchDataBySEandStatus(String se, String status);

    List<Map<String, Object>> fetchDataByEEandStatus(String ee, String status);

    List<Map<String, Object>> fetchDataByByCE1Name(String ce1);

    List<Map<String, Object>> fetchDataByByCE2Name(String ce2);

    List<Map<String, Object>> fetchDataByBySeName(String se);

    List<Map<String, Object>> fetchDataByByEEName(String ee);

    byte[] fetchPdfOfCurrentCertificateByAppId(String id) throws IOException;

    String  fetchUserIdFromUserSpring(String username);

    String fetchUserIdFromUserRepo(String username);

    Long getNoAction7DaysApplicationByFinYear(String status, String financialYear);

    boolean usernameExistsOrNot(String username);

}
