package com.gccloud.nocportal.ServiceImpl;

import com.gccloud.nocportal.Entity.*;
import com.gccloud.nocportal.Repository.*;
import com.gccloud.nocportal.Service.NocService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import jakarta.transaction.Transactional;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.*;

@Service
@Transactional
public class NocServiceImpl implements NocService {

    @Autowired
    private NocListRepo nocListRepo;

    @Value("${upload.directory}")
    private String uploadDirectory;

    @Value("${default.directory}")
    private String defaultDirectory;

    @Autowired
    private DistrictListRepo districtListRepo;

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private CanalRepo canalRepo;
    @Autowired
    private CheckListRepo checkListRepo;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;


    @Autowired
    private AppCurrentStatusRepository appCurrentStatusRepository;

    @Autowired
    private LogsOfFileRepository logsOfFileRepository;

    @Autowired
    private LogsOfDocumentRepo logsOfDocumentRepo;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private CurrentDocumentRepo currentDocumentRepo;

    @Autowired
    private ListOfLettersRepository listOfLettersRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private GrievancesRepository grievancesRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;


    @Autowired
    private JavaMailSender javaMailSender;


    @Override
    public List<String> findListOfNoc() {

        List<String> nocLists = nocListRepo.findAllNocName();
        return nocLists;
    }

    @Override
    public List<String> findListOfDistrict() {
        List<String> districtList = districtListRepo.findAllDistrict();
        return districtList;
    }

    @Override
    public List<String> findListOfCanals() {
        List<String> canalList = canalRepo.findAllCanal();
        return canalList;
    }

    @Override
    public List<NocList> findAllNocListData() {
        return nocListRepo.findAll();
    }

    @Override
    public List<CheckList> fetchAllChecklistDataById(Long index) {
        List<CheckList> checkLists = checkListRepo.findAllCheckListById(index);
        return checkLists;
    }

    @Override
    public Long getTotalApplications() {

        return applicationRepository.count();

    }

    @Override
    public Long getApplicationsByStatus(String status) {

        return applicationRepository.countByApplicationStatus(status);

    }

    @Override
    public Long get7DaysPendingApplication(String pending) {

        return applicationRepository.get7DaysPendingApplication(pending);

    }

    @Override
    public Long get15DaysPendingApplication(String pending) {

        return applicationRepository.get15DaysPendingApplication(pending);

    }

    @Override
    public Long getTotalApplicationsForUser(String username, String financialYear) {
        // Extract the start and end years from the "yyyy-yyyy" format
        String[] years = financialYear.split("-");
        if (years.length != 2) {
            throw new IllegalArgumentException("Invalid financial year format");
        }
        int startYear = Integer.parseInt(years[0]);
        int endYear = Integer.parseInt(years[1]);

        // Create start and end dates for the financial year (April 1 to March 31)
        Calendar startCal = Calendar.getInstance();
        startCal.set(startYear, Calendar.APRIL, 1, 0, 0, 0); // April 1st
        startCal.set(Calendar.MILLISECOND, 0);
        Date startDate = new Date(startCal.getTimeInMillis());

        Calendar endCal = Calendar.getInstance();
        endCal.set(endYear, Calendar.MARCH, 31, 23, 59, 59); // March 31st
        endCal.set(Calendar.MILLISECOND, 999);
        Date endDate = new Date(endCal.getTimeInMillis());

        // Fetch user and userDetails
        UsersSpring user = usersRepository.findUserByUsername(username);
        Long userId = user.getId();

        UserDetails userDetails = userDetailsRepository.findByUserId(userId);
        Long userDetailsId = userDetails.getId();

        // Call repository method with the start and end dates and userDetailsId
        return applicationRepository.getApplicationCountForUser(userDetailsId, startDate, endDate);
    }

    @Override
    public Long getApplicationsByStatusForUser(String status, String username, String financialYear) {
        // Extract the start and end years from the "yyyy-yyyy" format
        String[] years = financialYear.split("-");
        if (years.length != 2) {
            throw new IllegalArgumentException("Invalid financial year format");
        }
        int startYear = Integer.parseInt(years[0]);
        int endYear = Integer.parseInt(years[1]);

        // Create start and end dates for the financial year (April 1 to March 31)
        Calendar startCal = Calendar.getInstance();
        startCal.set(startYear, Calendar.APRIL, 1, 0, 0, 0); // April 1st
        startCal.set(Calendar.MILLISECOND, 0);
        Date startDate = new Date(startCal.getTimeInMillis());

        Calendar endCal = Calendar.getInstance();
        endCal.set(endYear, Calendar.MARCH, 31, 23, 59, 59); // March 31st
        endCal.set(Calendar.MILLISECOND, 999);
        Date endDate = new Date(endCal.getTimeInMillis());

        // Fetch user and userDetails
        UsersSpring user = usersRepository.findUserByUsername(username);
        Long userId = user.getId();

        UserDetails userDetails = userDetailsRepository.findByUserId(userId);
        Long userDetailsId = userDetails.getId();

        // Call repository method with the start and end dates and userDetailsId
        return applicationRepository.countByApplicationStatusForUser(status, userDetailsId, startDate, endDate);
    }

    @Override
    public Long getPendingApplicationForUser(String status, String username, String financialYear) {
        // Extract the start and end years from the "yyyy-yyyy" format
        String[] years = financialYear.split("-");
        if (years.length != 2) {
            throw new IllegalArgumentException("Invalid financial year format");
        }
        int startYear = Integer.parseInt(years[0]);
        int endYear = Integer.parseInt(years[1]);

        // Create start and end dates for the financial year (April 1 to March 31)
        Calendar startCal = Calendar.getInstance();
        startCal.set(startYear, Calendar.APRIL, 1, 0, 0, 0); // April 1st
        startCal.set(Calendar.MILLISECOND, 0);
        Date startDate = new Date(startCal.getTimeInMillis());

        Calendar endCal = Calendar.getInstance();
        endCal.set(endYear, Calendar.MARCH, 31, 23, 59, 59); // March 31st
        endCal.set(Calendar.MILLISECOND, 999);
        Date endDate = new Date(endCal.getTimeInMillis());

        // Fetch user and userDetails
        UsersSpring user = usersRepository.findUserByUsername(username);
        Long userId = user.getId();

        UserDetails userDetails = userDetailsRepository.findByUserId(userId);
        Long userDetailsId = userDetails.getId();

        // Call repository method with the start and end dates and userDetailsId
        return applicationRepository.get15DaysPendingApplicationForUser(status, userDetailsId, startDate, endDate);
    }

    @Override
    public Long getCertificateIssuedForUser(String status, String username, String financialYear) {
        // Extract the start and end years from the "yyyy-yyyy" format
        String[] years = financialYear.split("-");
        if (years.length != 2) {
            throw new IllegalArgumentException("Invalid financial year format");
        }
        int startYear = Integer.parseInt(years[0]);
        int endYear = Integer.parseInt(years[1]);

        // Create start and end dates for the financial year (April 1 to March 31)
        Calendar startCal = Calendar.getInstance();
        startCal.set(startYear, Calendar.APRIL, 1, 0, 0, 0); // April 1st
        startCal.set(Calendar.MILLISECOND, 0);
        Date startDate = new Date(startCal.getTimeInMillis());

        Calendar endCal = Calendar.getInstance();
        endCal.set(endYear, Calendar.MARCH, 31, 23, 59, 59); // March 31st
        endCal.set(Calendar.MILLISECOND, 999);
        Date endDate = new Date(endCal.getTimeInMillis());

        // Fetch user and userDetails
        UsersSpring user = usersRepository.findUserByUsername(username);
        Long userId = user.getId();

        UserDetails userDetails = userDetailsRepository.findByUserId(userId);
        Long userDetailsId = userDetails.getId();

        // Call repository method with the start and end dates and userDetailsId
        return applicationRepository.getCertificateIssuedCountForUser(status, userDetailsId, startDate, endDate);
    }


    @Override
    public Long getReSubmittedApplicationForUser(String username, String financialYear) {
        // Extract the start and end years from the "yyyy-yyyy" format
        String[] years = financialYear.split("-");
        if (years.length != 2) {
            throw new IllegalArgumentException("Invalid financial year format");
        }
        int startYear = Integer.parseInt(years[0]);
        int endYear = Integer.parseInt(years[1]);

        // Create start and end dates for the financial year (April 1 to March 31)
        Calendar startCal = Calendar.getInstance();
        startCal.set(startYear, Calendar.APRIL, 1, 0, 0, 0); // April 1st
        startCal.set(Calendar.MILLISECOND, 0);
        Date startDate = new Date(startCal.getTimeInMillis());

        Calendar endCal = Calendar.getInstance();
        endCal.set(endYear, Calendar.MARCH, 31, 23, 59, 59); // March 31st
        endCal.set(Calendar.MILLISECOND, 999);
        Date endDate = new Date(endCal.getTimeInMillis());

        // Fetch user and userDetails
        UsersSpring user = usersRepository.findUserByUsername(username);
        Long userId = user.getId();

        UserDetails userDetails = userDetailsRepository.findByUserId(userId);
        Long userDetailsId = userDetails.getId();

        // Call repository method with the start and end dates and userDetailsId
        return applicationRepository.getReSubmittedApplicationForUser(userDetailsId, startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getAgencyTableDataForUser(String username, String financialYear) {

        // Extract the start and end years from the "yyyy-yyyy" format
        String[] years = financialYear.split("-");
        if (years.length != 2) {
            throw new IllegalArgumentException("Invalid financial year format");
        }
        int startYear = Integer.parseInt(years[0]);
        int endYear = Integer.parseInt(years[1]);

        // Create start and end dates for the financial year (April 1 to March 31)
        Calendar startCal = Calendar.getInstance();
        startCal.set(startYear, Calendar.APRIL, 1, 0, 0, 0); // April 1st
        startCal.set(Calendar.MILLISECOND, 0);
        Date startDate = new Date(startCal.getTimeInMillis());

        Calendar endCal = Calendar.getInstance();
        endCal.set(endYear, Calendar.MARCH, 31, 23, 59, 59); // March 31st
        endCal.set(Calendar.MILLISECOND, 999);
        Date endDate = new Date(endCal.getTimeInMillis());

        UsersSpring user = usersRepository.findUserByUsername(username);

        Long id = user.getId();

        UserDetails userDetails = userDetailsRepository.findByUserId(id);

        Long userDetailsId = userDetails.getId();

        return applicationRepository.findAllApplicationsOfCurrentUser(userDetailsId, startDate, endDate);

    }

    public Long getTotalApplicationsByFinYear(String financialYear) {

        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().toArray()[0].toString();

        // Extract the start and end years from the "yyyy-yyyy" format
        String[] years = financialYear.split("-");
        if (years.length != 2) {
            throw new IllegalArgumentException("Invalid financial year format");
        }
        int startYear = Integer.parseInt(years[0]);
        int endYear = Integer.parseInt(years[1]);

        // Create start and end dates for the financial year (April 1 to March 31)
        Calendar startCal = Calendar.getInstance();
        startCal.set(startYear, Calendar.APRIL, 1, 0, 0, 0); // April 1st
        startCal.set(Calendar.MILLISECOND, 0);
        Date startDate = new Date(startCal.getTimeInMillis());

        Calendar endCal = Calendar.getInstance();
        endCal.set(endYear, Calendar.MARCH, 31, 23, 59, 59); // March 31st
        endCal.set(Calendar.MILLISECOND, 999);
        Date endDate = new Date(endCal.getTimeInMillis());

        if (role.equalsIgnoreCase("admin")) {

            // Call repository method with start and end dates
            return applicationRepository.countByDateRange(startDate, endDate);

        } else if (!role.equalsIgnoreCase("admin")) {

            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            // Call repository method with start and end dates
            return applicationRepository.countByDateRangeWhereToUser(username, startDate, endDate);
        }
//        else if (role.equalsIgnoreCase("ce2")) {
//
//            return applicationRepository.findAllApplicationDataByUserCe2AndByOwnership(username,startDate,endDate);
//
//        } else if (role.equalsIgnoreCase("se")) {
//
//            return applicationRepository.findAllApplicationDataByUserSeAndByOwnership(username,startDate,endDate);
//
//        } else if (role.equalsIgnoreCase("ee")) {
//
//            return applicationRepository.findAllApplicationDataByUserEeAndByOwnership(username,startDate,endDate);
//
//        }

        return null;
    }

    @Override
    public Long getApplicationsByStatusByFinYear(String status, String financialYear) {

        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().toArray()[0].toString();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Extract the start and end years from the "yyyy-yyyy" format
        String[] years = financialYear.split("-");
        if (years.length != 2) {
            throw new IllegalArgumentException("Invalid financial year format");
        }
        int startYear = Integer.parseInt(years[0]);
        int endYear = Integer.parseInt(years[1]);

        // Create start and end dates for the financial year (April 1 to March 31)
        Calendar startCal = Calendar.getInstance();
        startCal.set(startYear, Calendar.APRIL, 1, 0, 0, 0); // April 1st
        startCal.set(Calendar.MILLISECOND, 0);
        Date startDate = new Date(startCal.getTimeInMillis());

        Calendar endCal = Calendar.getInstance();
        endCal.set(endYear, Calendar.MARCH, 31, 23, 59, 59); // March 31st
        endCal.set(Calendar.MILLISECOND, 999);
        Date endDate = new Date(endCal.getTimeInMillis());

        if (role.equalsIgnoreCase("admin")) {
            // Call repository method with status and date range
            return applicationRepository.countByApplicationStatusAndDateRange(status, startDate, endDate);

        } else if (!role.equalsIgnoreCase("admin")) {

            // Call repository method with start and end dates
            return applicationRepository.countByApplicationStatusAndDateRangeWhereToUser(username, status, startDate, endDate);
        }
//        else if (role.equalsIgnoreCase("ce2")) {
//
//            String username = SecurityContextHolder.getContext().getAuthentication().getName();
//
//            // Call repository method with start and end dates
//            return applicationRepository.countByDateRangeWhereCe2(username, startDate, endDate);
//        } else if (role.equalsIgnoreCase("se")) {
//
//            String username = SecurityContextHolder.getContext().getAuthentication().getName();
//
//            // Call repository method with start and end dates
//            return applicationRepository.countByDateRangeWhereSe(username, startDate, endDate);
//        } else if (role.equalsIgnoreCase("ee")) {
//
//            String username = SecurityContextHolder.getContext().getAuthentication().getName();
//
//            // Call repository method with start and end dates
//            return applicationRepository.countByDateRangeWhereEe(username, startDate, endDate);
//        }

        return null;
    }

    @Override
    public Long get7DaysPendingApplicationByFinYear(String pending, String financialYear) {

        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().toArray()[0].toString();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Extract the start and end years from the "yyyy-yyyy" format
        String[] years = financialYear.split("-");
        if (years.length != 2) {
            throw new IllegalArgumentException("Invalid financial year format");
        }
        int startYear = Integer.parseInt(years[0]);
        int endYear = Integer.parseInt(years[1]);

        // Create start and end dates for the financial year (April 1 to March 31)
        Calendar startCal = Calendar.getInstance();
        startCal.set(startYear, Calendar.APRIL, 1, 0, 0, 0); // April 1st
        startCal.set(Calendar.MILLISECOND, 0);
        Date startDate = new Date(startCal.getTimeInMillis());

        Calendar endCal = Calendar.getInstance();
        endCal.set(endYear, Calendar.MARCH, 31, 23, 59, 59); // March 31st
        endCal.set(Calendar.MILLISECOND, 999);
        Date endDate = new Date(endCal.getTimeInMillis());

        // Calculate the date 7 days before the current date
        Calendar fifteenDaysBack = Calendar.getInstance();
        fifteenDaysBack.add(Calendar.DAY_OF_MONTH, -7);
        Date sevenDaysAgo = new Date(fifteenDaysBack.getTimeInMillis());

        if (role.equalsIgnoreCase("admin")) {

            // Call repository method with status, date range, and 15-day interval
            return applicationRepository.get7DaysPendingApplicationByFinYear(pending, startDate, endDate, sevenDaysAgo);
        } else if (!role.equalsIgnoreCase("admin")) {

            // Call repository method with status, date range, and 15-day interval
            return applicationRepository.get7DaysPendingApplicationByFinYearWhereToUser(username, pending, startDate, endDate, sevenDaysAgo);
        }
//        else if (role.equalsIgnoreCase("ce2")) {
//
//            return applicationRepository.findAllApplicationDataByUserCe2(username, startDate, endDate);
//
//        } else if (role.equalsIgnoreCase("se")) {
//
//            return applicationRepository.findAllApplicationDataByUserSe(username, startDate, endDate);
//
//        } else if (role.equalsIgnoreCase("ee")) {
//
//            return applicationRepository.findAllApplicationDataByUserEe(username, startDate, endDate);
//
//        }

        return null;


    }

    @Override
    public Long get15DaysPendingApplicationByFinYear(String pending, String financialYear) {

        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().toArray()[0].toString();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Extract the start and end years from the "yyyy-yyyy" format
        String[] years = financialYear.split("-");
        if (years.length != 2) {
            throw new IllegalArgumentException("Invalid financial year format");
        }
        int startYear = Integer.parseInt(years[0]);
        int endYear = Integer.parseInt(years[1]);

        // Create start and end dates for the financial year (April 1 to March 31)
        Calendar startCal = Calendar.getInstance();
        startCal.set(startYear, Calendar.APRIL, 1, 0, 0, 0); // April 1st
        startCal.set(Calendar.MILLISECOND, 0);
        Date startDate = new Date(startCal.getTimeInMillis());

        Calendar endCal = Calendar.getInstance();
        endCal.set(endYear, Calendar.MARCH, 31, 23, 59, 59); // March 31st
        endCal.set(Calendar.MILLISECOND, 999);
        Date endDate = new Date(endCal.getTimeInMillis());

        // Calculate the date 15 days before the current date
        Calendar thirtyDaysBack = Calendar.getInstance();
        thirtyDaysBack.add(Calendar.DAY_OF_MONTH, -15);
        Date fifteenDaysAgo = new Date(thirtyDaysBack.getTimeInMillis());

        if (role.equalsIgnoreCase("admin")) {

            // Call repository method with status, date range, and 15-day interval
            return applicationRepository.get15DaysPendingApplicationByFinYear(pending, startDate, endDate, fifteenDaysAgo);

        } else if (!role.equalsIgnoreCase("admin")) {

            // Call repository method with status, date range, and 15-day interval
            return applicationRepository.get15DaysPendingApplicationByFinYearWhereToUser(username, pending, startDate, endDate, fifteenDaysAgo);

        }
//        else if (role.equalsIgnoreCase("ce2")) {
//
//            // Call repository method with status, date range, and 15-day interval
//            return applicationRepository.get7DaysPendingApplicationByFinYearWhereCe2(username, pending, startDate, endDate, sevenDaysAgo);
//        } else if (role.equalsIgnoreCase("se")) {
//
//            // Call repository method with status, date range, and 15-day interval
//            return applicationRepository.get7DaysPendingApplicationByFinYearWhereSe(username, pending, startDate, endDate, sevenDaysAgo);
//        } else if (role.equalsIgnoreCase("ee")) {
//
//            // Call repository method with status, date range, and 15-day interval
//            return applicationRepository.get7DaysPendingApplicationByFinYearWhereEe(username, pending, startDate, endDate, sevenDaysAgo);
//        }

        return null;

    }

//    private String saveFile(String uploadDir, MultipartFile file) {
//        try {
//            Path filePath = Paths.get(uploadDir + file.getOriginalFilename());
//            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//            return filePath.toString();
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
//        }
//    }


    private String saveFile(String uploadDir, MultipartFile file, boolean replaceExisting, String nocName, String agencyName, String applicationId, String subDirectory, String docName) {
        try {
//        int no)fcount=0;
            // Get the original file name

//        String checkListRepo.findDocNameByNocname(nocName);

            String fileName = applicationId + "_" + docName + "_1" + ".pdf";

            // Build the relative path with the NOC name, agency name, application ID, and subdirectory
            String relativePath = Paths.get("NOC", nocName, agencyName, applicationId, subDirectory, fileName).toString().replace("\\", "/");

            // Build the full path for storage
            Path filePath = Paths.get(uploadDir, relativePath);

            // Create the NOC directory if it doesn't exist
            Files.createDirectories(Paths.get(uploadDir, "NOC"));

            // Create directories for the file path if they don't exist
            Files.createDirectories(filePath.getParent());

            // Copy the file with or without replacing the existing file
            if (replaceExisting) {
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.copy(file.getInputStream(), filePath);
            }

            // Return the relative path
            return relativePath;
        } catch (IOException e) {
            // Print the stack trace for debugging purposes
            e.printStackTrace();
            throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }


//        try {
//        // Creating a simple mail message
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//
//        // Setting up necessary details
//        mailMessage.setFrom("no.reply.gccloud@gmail.com"); // You can replace with your sender's email
//        mailMessage.setTo("abhinav017@gmail.com");
//        mailMessage.setText(
//                "You have received a new inquiry:\n\n" +
//                        "Full Name: " + inquiry.getFullName() + "\n" +
//                        "Company Name: " + inquiry.getCompanyName() + "\n" +
//                        "Designation: " + inquiry.getDesignation() + "\n" +
//                        "Area of Interest: " + inquiry.getAreOfInterest() + "\n" +
//                        "Phone: " + inquiry.getPhone() + "\n" +
//                        "Email: " + inquiry.getEmail() + "\n\n" +
//                        "Message:\n" + inquiry.getMessage()
//        );
//        mailMessage.setSubject("Inquiry about Shivaasha");
//
//        // Sending the email
//        javaMailSender.send(mailMessage);


    private List<ListOfLetters> transferFirstSubmittedLetterToListOfLetters(Application application, DocumentStatus documentStatus, String agencyLetterPath) throws IOException {

        List<ListOfLetters> lettersList = new ArrayList<>();
        long applicationLastId = applicationRepository.getLastApplicationId(); // Assuming this method exists
        long applicationId = applicationLastId + 1;

        boolean firstDocumentProcessed = false; // Flag to track the first document

        for (DocumentStatus doc : application.getDocumentStatus()) {
            if (firstDocumentProcessed) {
                break; // Exit the loop after processing the first document
            }

            // Set description, associationId, and paths
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            // Get the NOC name from the application (appliedFor)
            String nocType = application.getAppliedFor();


            firstDocumentProcessed = true;


            String fileName = "app_" + applicationId + "_AppliedBy_" + username + ".pdf";
            String absoluteBasePath = uploadDirectory + "NOC/ListOfLetters/" + nocType + "/app_" + applicationId + "/AppliedNocLetter";
            String absoluteFilePath = absoluteBasePath + "/" + fileName;
            String relativeFilePath = "NOC/ListOfLetters/" + nocType + "/app_" + applicationId + "/AppliedNocLetter/" + fileName;

            // Ensure the necessary directories exist
            ensureDirectoryExists(uploadDirectory + "NOC");
            ensureDirectoryExists(uploadDirectory + "NOC/ListOfLetters/" + nocType);
            ensureDirectoryExists(absoluteBasePath);

            // Move the temporary file to the final destination
            Files.copy(Path.of(agencyLetterPath), Path.of(absoluteFilePath), StandardCopyOption.REPLACE_EXISTING);

            // Create and set details for ListOfLetters entity
            ListOfLetters listOfLetters = new ListOfLetters();
            listOfLetters.setDocPath(relativeFilePath);  // Saving relative path in the database
            listOfLetters.setIssuedBy(username);
            listOfLetters.setDescription(doc.getDescription());
            listOfLetters.setApplication(application);

            // Save the ListOfLetters entity
            lettersList.add(listOfLetters);

        }
        return lettersList;
    }

    // Utility method to ensure directory exists
    private void ensureDirectoryExists(String directoryPath) throws IOException {
        Path path = Path.of(directoryPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    @Override
    public String[] savingNocFromData(Application application, DocumentStatus documentStatus) throws IOException {

        try {

            String spcId = UUID.randomUUID().toString();
            application.setSpc_Id(spcId);
            String emailBody = String.format(
                    "You have received your application UNIQUE ID to track your application's whereabouts:\n\n" +
                            "Unique ID: %s\n\n" +
                            "Thank you for your application.",
                    spcId
            );
            //For Creating Application Instance in DB and Setting its attributes
            long timestampMillis = Instant.now().toEpochMilli();
            application.setTimestamp(String.valueOf(timestampMillis));

            Date currentDate = new Date(System.currentTimeMillis());
            application.setDate(currentDate);

            //
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            UsersSpring user = usersRepository.findUserByUsername(username);
            Long id = user.getId();
            UserDetails userDetails = userDetailsRepository.findByUserId(id);

            String agencyFullName = userDetails.getAgencyName();

            application.setAdmin("CHIEF WATER RESOURCE (ADMIN)");
            application.setCe1(application.getCe1());
            application.setCe2(application.getCe2());
            application.setSe(application.getSe());
            application.setEe(application.getEe());
            //Details About Application Submitted From

            application.setUserDetails(userDetails);

            // Create a list to hold CurrentDocument objects
            List<DocumentStatus> documentStatusList = new ArrayList<>();
            List<LogsOfDocument> logsOfDocumentList = new ArrayList<>();

            List<String> pdfPaths = new ArrayList<>(); // List to hold paths of all PDFs

            // Get the agency name from SecurityContextHolder
            String agencyName = SecurityContextHolder.getContext().getAuthentication().getName();

            // Get the application last ID and calculate the next ID (assuming this is manually incremented)
            long applicationLastId = applicationRepository.getLastApplicationId(); // Assuming this method exists
            String applicationId = "app_" + (applicationLastId + 1);

            // Get the NOC name from the application (appliedFor)
            String nocName = application.getAppliedFor();

            NocList noc = nocListRepo.findByNocName(nocName);

            List<CheckList> checkListVariable = checkListRepo.findByNocName(noc.getId());

            // Define subdirectories for CD and PD
            String cdSubDirectory = "CurrDoc";
            String pdSubDirectory = "PrevDoc";
            String agencyLetterPath = "";
            Long index = 0L;

            for (DocumentStatus doc : application.getDocumentStatus()) {

                String cdPath = "";
                String pdPath = "";
                CheckList checkListItem = null;


                MultipartFile file = doc.getFile();
                if (file != null && !file.isEmpty()) {
                    String description = doc.getDescription();

                    checkListItem = checkListVariable.stream()
                            .filter(item -> description.equals(item.getCheckListTask()))  // Filter the list to match the description
                            .map(item -> item)  // Map the matching item to its docName
                            .findFirst()  // Get the first matching item, if any
                            .orElse(null);


                    String docName = checkListVariable.stream()
                            .filter(item -> description.equals(item.getCheckListTask()))  // Filter the list to match the description
                            .map(item -> item.getDocName())  // Map the matching item to its docName
                            .findFirst()  // Get the first matching item, if any
                            .orElse(null);
                    // If docName is null, generate a UUID-based file name
                    if (docName == null) {
                        docName = UUID.randomUUID().toString();  // Generate a unique identifier
                    }

                    // Save file in both CD (with replace) and PD (without replace) directories
                    cdPath = saveFile(uploadDirectory, file, true, nocName, agencyName, applicationId, cdSubDirectory, docName);
                    pdPath = saveFile(uploadDirectory, file, false, nocName, agencyName, applicationId, pdSubDirectory, docName);
                    pdfPaths.add(cdPath);
                } else if (doc.getInputValue() != null) {
                    // Use the same input value for both doc and logDoc
                    cdPath = doc.getInputValue();
                    pdPath = doc.getInputValue();
                }

                if (index == 0) {
                    agencyLetterPath = uploadDirectory + cdPath;
                }


                // Set description, associationId, and paths
                doc.setDescription(doc.getDescription());
                doc.setAssociationId(doc.getAssociationId());
                doc.setApplication(application);
                doc.setDataField(cdPath); // Save the relative CD path
                doc.setCheckList(checkListItem);
                doc.setCurrStatus("submitted");


                LogsOfDocument logDoc = LogsOfDocument.builder()
                        .description(doc.getDescription())
                        .associationId(doc.getAssociationId())
                        .application(application)
                        .documentStatus(doc)
                        .currStatus("submitted")
                        .dataField(pdPath) // Save the relative PD path
                        .build();

                documentStatusList.add(doc);
                logsOfDocumentList.add(logDoc);

                index++;
            }

            List<ListOfLetters> letters = transferFirstSubmittedLetterToListOfLetters(application, documentStatus, agencyLetterPath);

            String admin = application.getAdmin();
            String ce1 = application.getCe1();
            String ce2 = application.getCe2();
            String se = application.getSe();
            String ee = application.getEe();

            String[] departmentLevels = {agencyFullName, admin, ce1, ce2, se, ee};

            //Creating Application Movement Status Instance
            MovementStatus movementStatus = new MovementStatus();
            movementStatus.setTimestamp(String.valueOf(timestampMillis));
            movementStatus.setDate(currentDate);
            movementStatus.setApplication(application);
            movementStatus.setStatus("PENDING");
            movementStatus.setFromUser(username);
            movementStatus.setToUser("CHIEF WATER RESOURCE (ADMIN)");

            List<LogsOfMovement> movementLogList = new ArrayList<>();

            for (int i = 0; i < departmentLevels.length - 1; i++) {
                //Creating Application Movement Instance Log
                LogsOfMovement logsOfMovement = new LogsOfMovement();
                logsOfMovement.setMovementStatus(movementStatus);
                logsOfMovement.setFromDepartmentUser(departmentLevels[i]);
                logsOfMovement.setToDepartmentUser(departmentLevels[i + 1]);
                logsOfMovement.setTimestamp(String.valueOf(timestampMillis));
                logsOfMovement.setDate(currentDate);
                logsOfMovement.setTransferRemark("Application Forwarded To Concerned Officer");
                logsOfMovement.setTransferStatus(application.getTransferStatus());
                logsOfMovement.setApplication(application);

                movementLogList.add(logsOfMovement);

                //Setting Other Related Instances With Application
                application.setDocumentStatus(documentStatusList);
                application.setLogsOfDocuments(logsOfDocumentList);
                application.setMovementStatus(movementStatus);
                application.setLogsOfMovements(movementLogList);
            }

            // Save the application object to the repository
            applicationRepository.save(application);

            MovementStatus movementStatusOld = appCurrentStatusRepository.findById(movementStatus.getId()).get();

            Long appCurrentStatusId = movementStatusOld.getId();

            LogsOfMovement logsOfMovement = logsOfFileRepository.findLatestLogDetails(appCurrentStatusId);

            movementStatus.setToUser(logsOfMovement.getToDepartmentUser());

            appCurrentStatusRepository.save(movementStatusOld);


            // Create the directory structure for the merged PDF
            File baseDir = new File(uploadDirectory + "/NOC");
            if (!baseDir.exists()) {
                baseDir.mkdirs(); // Create base NOC directory if it doesn't exist
            }

            File nocDir = new File(baseDir, nocName);
            if (!nocDir.exists()) {
                nocDir.mkdirs(); // Create NOC directory if it doesn't exist
            }

            File agencyDir = new File(nocDir, agencyName);
            if (!agencyDir.exists()) {
                agencyDir.mkdirs(); // Create agency directory if it doesn't exist
            }

            File applicationDir = new File(agencyDir, applicationId);
            if (!applicationDir.exists()) {
                applicationDir.mkdirs(); // Create application directory if it doesn't exist
            }

            File combinedPdfsDir = new File(applicationDir, "CombinedPdfs");
            if (!combinedPdfsDir.exists()) {
                combinedPdfsDir.mkdirs(); // Create CombinedPdfs directory if it doesn't exist
            }

            // Generate the full path for saving the merged PDF
            String fullPath = combinedPdfsDir.getAbsolutePath() + "/" + applicationId + "_combined.pdf";

            // Merge all collected PDF files into a single PDF
            mergePDFs(pdfPaths, fullPath);

            // Generate the relative path for storing in the database
            String relativePath = Paths.get("NOC", nocName, agencyName, applicationId, "CombinedPdfs", applicationId + "_combined.pdf").toString().replace("\\", "/");

            // Save the merged PDF path in the application entity
            application.setPdfPath(relativePath);

            application.setListOfLetters(letters);

            //Application Being Updated for Combined PDF File
            applicationRepository.save(application); // Save the updated application with merged PDF path

////        // Creating a simple mail message
//            SimpleMailMessage mailMessage = new SimpleMailMessage();
//
//            // Setting up necessary details
//            mailMessage.setFrom("no.reply.gccloud@gmail.com"); // You can replace with your sender's email
//            mailMessage.setTo("physics05072001@gmail.com");
//            mailMessage.setSubject("Application Special ID");
//            mailMessage.setText(emailBody);
//
//
//            // Sending the email
//            javaMailSender.send(mailMessage);


            return new String[]{spcId, "Application and documents saved successfully for Noc Name :" + application.getAppliedFor()};
        } catch (Exception e) {
            e.printStackTrace();
            return new String[]{null, "An error occurred while saving the application: " + e.getMessage()};
        }
    }


    // Method to reorder PDF paths
    // DSA - Implace is used just for reference ,Optimized for space Complexity
    private static void reorderPdfPaths(List<String> pdfPaths) {
        //       Just for the base case
        if (pdfPaths == null || pdfPaths.size() < 2) {
            return;
        }

        // Find the index of the longest path
        int longestIndex = -1;
        int maxLength = -1;
        for (int i = 0; i < pdfPaths.size(); i++) {
            String path = pdfPaths.get(i);
            if (path != null && path.length() > maxLength) {
                maxLength = path.length();
                longestIndex = i;
            }
        }

        // If the longest path is not already at index 1, reorder
        if (longestIndex != -1 && longestIndex != 1) {
            String longestPath = pdfPaths.get(longestIndex);

            // Remove the longest path from its current position
            pdfPaths.remove(longestIndex);

            // Add it to index 1
            pdfPaths.add(1, longestPath);
        }
    }

    // Method to merge multiple PDFs into one using iText 5
    private void mergePDFs(List<String> pdfPaths, String outputPdfPath) throws IOException {

        reorderPdfPaths(pdfPaths);
//        System.out.println("pdf paths : " + pdfPaths);
        Document document = null;
        PdfCopy copy = null;
        try {
            document = new Document();
            copy = new PdfCopy(document, new FileOutputStream(outputPdfPath));
            document.open();

            for (String relativePdfPath : pdfPaths) {
                // Construct the absolute path for the PDF file
                String absolutePdfPath = uploadDirectory + "/" + relativePdfPath;
//                System.out.println("Merging file: " + absolutePdfPath);

                File file = new File(absolutePdfPath);
                if (!file.exists()) {
                    throw new IOException("File not found: " + absolutePdfPath);
                }

                PdfReader reader = new PdfReader(absolutePdfPath);
                for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                    copy.addPage(copy.getImportedPage(reader, i));
                }
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("An error occurred while merging PDFs: " + e.getMessage(), e);
        } finally {
            if (document != null) {
                document.close();
            }
            if (copy != null) {
                copy.close();
            }
        }
    }


    private String saveFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Path targetLocation = Paths.get(uploadDirectory).resolve(fileName).normalize();
        Files.copy(file.getInputStream(), targetLocation);
        return targetLocation.toString();
    }

    @Override
    public List<String> fetchAllCe2(String ce1) {
        return usersRepository.fetchAllCe2(ce1);
    }

    @Override
    public List<String> getAllSeWhereCe2(String ce2) {
        return usersRepository.fetchAllSeWhereCe2(ce2);

    }

    @Override
    public List<String> getAllEeWhereSe(String se) {
        return usersRepository.fetchAllEeWhereSe(se);
    }

    @Override
    public List<String> fetchAllCe1() {
        return usersRepository.fetchAllCe1();
    }

    @Override
    public List<Map<String, Object>> findLogsByAppCurrentStatusId(Long id) {

        return logsOfFileRepository.findLogsDetailsByAppCurrentStatusId(id);

    }

    @Override
    public void saveAgencySignUpDetails(UsersSpring usersSpring, UserDetails userDetails) {

        usersSpring.setUsername(usersSpring.getUsername().trim());

        usersSpring.setLoginId(usersSpring.getUsername().trim());

        usersSpring.setRole("agency");

        String enCodPwd = passwordEncoder.encode(usersSpring.getPassword());

        usersSpring.setPassword(enCodPwd);

        usersRepository.save(usersSpring);

        UsersSpring usersSpringData = usersRepository.findById(usersSpring.getId()).get();

        userDetails.setUsers(usersSpringData);

        userDetails.setFullName(userDetails.getFullName().trim());

        userDetailsRepository.save(userDetails);

    }

    @Override
    public List<Map<String, Object>> getAllApplicationDataByUser(String financialYear) {

        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().toArray()[0].toString();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Extract the start and end years from the "yyyy-yyyy" format
        String[] years = financialYear.split("-");
        if (years.length != 2) {
            throw new IllegalArgumentException("Invalid financial year format");
        }
        int startYear = Integer.parseInt(years[0]);
        int endYear = Integer.parseInt(years[1]);

        // Create start and end dates for the financial year (April 1 to March 31)
        Calendar startCal = Calendar.getInstance();
        startCal.set(startYear, Calendar.APRIL, 1, 0, 0, 0); // April 1st
        startCal.set(Calendar.MILLISECOND, 0);
        Date startDate = new Date(startCal.getTimeInMillis());

        Calendar endCal = Calendar.getInstance();
        endCal.set(endYear, Calendar.MARCH, 31, 23, 59, 59); // March 31st
        endCal.set(Calendar.MILLISECOND, 999);
        Date endDate = new Date(endCal.getTimeInMillis());

        if (role.equalsIgnoreCase("admin")) {

            return applicationRepository.findAllApplicationDataByUser(username, startDate, endDate);

        } else if (role.equalsIgnoreCase("ce1")) {

            return applicationRepository.findAllApplicationDataByCe1(username, startDate, endDate);

        } else if (role.equalsIgnoreCase("ce2")) {

            return applicationRepository.findAllApplicationDataByCe2(username, startDate, endDate);

        } else if (role.equalsIgnoreCase("se")) {

            return applicationRepository.findAllApplicationDataBySe(username, startDate, endDate);

        } else if (role.equalsIgnoreCase("ee")) {

            return applicationRepository.findAllApplicationDataByEe(username, startDate, endDate);

        } else if (!role.equalsIgnoreCase("admin")) {

            return applicationRepository.findAllApplicationDataByToUser(username, startDate, endDate);

        }
//        else if (role.equalsIgnoreCase("ce2")) {
//
//            // Call repository method with status, date range, and 15-day interval
//            return applicationRepository.get15DaysPendingApplicationByFinYearWhereCe2(username, pending, startDate, endDate, fifteenDaysAgo);
//
//        } else if (role.equalsIgnoreCase("se")) {
//
//            // Call repository method with status, date range, and 15-day interval
//            return applicationRepository.get15DaysPendingApplicationByFinYearWhereSe(username, pending, startDate, endDate, fifteenDaysAgo);
//
//        } else if (role.equalsIgnoreCase("ee")) {
//
//            // Call repository method with status, date range, and 15-day interval
//            return applicationRepository.get15DaysPendingApplicationByFinYearWhereEe(username, pending, startDate, endDate, fifteenDaysAgo);
//
//        }

        return null;

    }

    @Override
    public List<Map<String, Object>> getAllApplicationDataByApplicationId(Long id) {

        return applicationRepository.findApplicationDataById(id);

    }

    // New Code For Saving Generated Letters
    @Override
    public void savingLetters(MultipartFile file, String content, Long applicationId, String description, String nocType) throws IOException {
        // Parse HTML content using Jsoup
        org.jsoup.nodes.Document htmlDocument = Jsoup.parse(content);

        // Get the username from security context
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Fetch the application entity
        Optional<Application> applicationOpt = applicationRepository.findById(applicationId);
        if (applicationOpt.isEmpty()) {
            throw new IllegalArgumentException("Application not found");
        }
        Application application = applicationOpt.get();

        // Define the new file name format as "app_applicationId_letter.pdf"
        String fileName = "app_" + applicationId + "_GeneratedLetterBy_" + username + ".pdf";

        // Base directory path (absolute path) where files will be saved
        String absoluteBasePath = uploadDirectory + "NOC/ListOfLetters/" + nocType + "/app_" + applicationId + "/GeneratedLetter";

        // Complete absolute path where the file will be saved
        String absoluteFilePath = absoluteBasePath + "/" + fileName;

        // Relative path to store in the database
        String relativeFilePath = "NOC/ListOfLetters/" + nocType + "/app_" + applicationId + "/GeneratedLetter/" + fileName;

        // Ensure the necessary directories exist
        createDirectoriesIfNotExist(uploadDirectory + "NOC");
        createDirectoriesIfNotExist(uploadDirectory + "NOC/ListOfLetters/" + nocType);
        createDirectoriesIfNotExist(absoluteBasePath);

        // Generate the PDF from the modified HTML content and save it at the specified absolute path
        generatePdfFromHtml(content, absoluteFilePath);

        // Create and set details for ListOfLetters entity
        ListOfLetters listOfLetters = new ListOfLetters();
        listOfLetters.setDocPath(relativeFilePath);  // Saving relative path in the database
        listOfLetters.setIssuedBy(username);
        listOfLetters.setDescription(description);
        listOfLetters.setApplication(application);

        // Save the ListOfLetters entity
        listOfLettersRepository.save(listOfLetters);

        // Paths for combining PDFs
        String absoluteLetterPdfPath = uploadDirectory + relativeFilePath;
        String oldCombinedpdfPath = uploadDirectory + application.getPdfPath();

        System.out.println("absoluteLetterPdfPath: " + absoluteLetterPdfPath);
        System.out.println("oldCombinedpdfPath: " + oldCombinedpdfPath);

        // Combine the new PDF with the existing combined PDF
        combinePdfWithExisting(applicationId, absoluteLetterPdfPath, oldCombinedpdfPath, application.getAppliedFor(), application.getCreatedBy());
    }

    private void createDirectoriesIfNotExist(String directoryPath) throws IOException {
        Path path = Path.of(directoryPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    private void generatePdfFromHtml(String htmlContent, String outputPath) throws IOException {
        Document document = new Document();

        try {
            // Create PdfWriter instance
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputPath));

            // Open the document for writing
            document.open();

            // Specify the path to the font file that supports Hindi (or other languages)
            String fontPath = defaultDirectory + "static" + File.separator + "fonts" + File.separator + "Mukta" + File.separator + "Mukta-Regular.ttf";
            BaseFont baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font regularFont = new Font(baseFont, 12);
            Font boldFont = new Font(baseFont, 12, Font.BOLD);
            Font italicFont = new Font(baseFont, 12, Font.ITALIC);

            // Parse the HTML content using Jsoup
            org.jsoup.nodes.Document htmlDoc = Jsoup.parse(htmlContent);

            // Process the body of the HTML
            Elements bodyElements = htmlDoc.body().children();
            for (Element element : bodyElements) {
                if (element.tagName().equals("p")) {
                    Paragraph paragraph = new Paragraph();
                    processParagraphElement(element, paragraph, regularFont, boldFont, italicFont);
                    document.add(paragraph);
                } else if (element.tagName().equals("br")) {
                    // Add a line break
                    document.add(Chunk.NEWLINE);
                }
            }

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            throw new IOException("Failed to generate PDF from HTML: " + e.getMessage(), e);
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }
    }

    private void processParagraphElement(Element element, Paragraph paragraph, Font regularFont, Font boldFont, Font italicFont) {
        for (Node childNode : element.childNodes()) {
            if (childNode instanceof TextNode) {
                // Append regular text
                paragraph.add(new Chunk(((TextNode) childNode).text(), regularFont));
            } else if (childNode instanceof Element) {
                Element childElement = (Element) childNode;

                // Handle <strong> or <b> for bold text
                if (childElement.tagName().equals("strong") || childElement.tagName().equals("b")) {
                    paragraph.add(new Chunk(childElement.text(), boldFont));
                }

                // Handle <i> for italic text
                else if (childElement.tagName().equals("i")) {
                    paragraph.add(new Chunk(childElement.text(), italicFont));
                }

                // Handle <br> for line break
                else if (childElement.tagName().equals("br")) {
                    paragraph.add(Chunk.NEWLINE);
                }

                // Recursively process nested elements
                else {
                    processParagraphElement(childElement, paragraph, regularFont, boldFont, italicFont);
                }
            }
        }
    }

    // Old Code
//    @Override
//    public void savingLetters(MultipartFile file, String content, Long applicationId, String description, String nocType) throws IOException {
//        // Parse HTML content using Jsoup
//        org.jsoup.nodes.Document htmlDocument = Jsoup.parse(content);
//        // Get the username from security context
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        MultipartFile multipartFile = file;
//
//        if (multipartFile != null && !multipartFile.isEmpty()) {
//        // Fetch the application entity
//        Optional<Application> applicationOpt = applicationRepository.findById(applicationId);
//        if (applicationOpt.isEmpty()) {
//            throw new IllegalArgumentException("Application not found");
//        }
//        Application application = applicationOpt.get();
//
//        // Define the new file name format as "app_applicationId_letter.pdf"
//        String fileName = "app_" + applicationId + "_GeneratedLetterBy_" + username + ".pdf";
//
//        // Base directory path (absolute path) where files will be saved
//        String absoluteBasePath = uploadDirectory + "NOC/ListOfLetters/" + nocType + "/app_" + applicationId + "/GeneratedLetter";
//
//        // Complete absolute path where the file will be saved
//        String absoluteFilePath = absoluteBasePath + "/" + fileName;
//
//        // Relative path to store in the database
//        String relativeFilePath = "NOC/ListOfLetters/" + nocType + "/app_" + applicationId + "/GeneratedLetter/" + fileName;
//
//        // Ensure the necessary directories exist
//        createDirectoriesIfNotExist(uploadDirectory + "NOC");
//        createDirectoriesIfNotExist(uploadDirectory + "NOC/ListOfLetters/" + nocType);
//        createDirectoriesIfNotExist(absoluteBasePath);
//
//        // Generate the PDF and save it at the specified absolute path
//        generatePdfFromHtml(content, absoluteFilePath);
//
//
//            // Check if the file already exists, and if so, delete it
//        File existingFile = new File(absoluteFilePath);
//        if (existingFile.exists()) {
//            existingFile.delete();
//        }
//        // Save the file to the absolute path
//        multipartFile.transferTo(new File(absoluteFilePath));
//
//        // Create and set details for ListOfLetters entity
//        ListOfLetters listOfLetters = new ListOfLetters();
//        listOfLetters.setDocPath(relativeFilePath);  // Saving relative path in the database
//        listOfLetters.setIssuedBy(username);
//        listOfLetters.setDescription(description);
//        listOfLetters.setApplication(application);
//
//        // Save the ListOfLetters entity
//        listOfLettersRepository.save(listOfLetters);
//
//        String absoluteLetterPdfPath = uploadDirectory + relativeFilePath;
//        String oldCombinedpdfPath = uploadDirectory + application.getPdfPath();
//
//        System.out.println("absoluteLetterPdfPath" + absoluteLetterPdfPath);
//        System.out.println("oldCombinedpdfPath" + oldCombinedpdfPath);
//
//        // Combine the new PDF with the existing combined PDF
//        combinePdfWithExisting(applicationId, absoluteLetterPdfPath, oldCombinedpdfPath,application.getAppliedFor(),application.getCreatedBy());
//
//
//        }
// }
//
//    private void createDirectoriesIfNotExist(String directoryPath) throws IOException {
//        Path path = Path.of(directoryPath);
//        if (!Files.exists(path)) {
//            Files.createDirectories(path);
//        }
//    }
//
//    private void generatePdfFromHtml(String htmlContent, String outputPath) throws IOException {
//        Document document = new Document();
//
//        try {
//            // Create PdfWriter instance
//            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputPath));
//
//            // Open the document for writing
//            document.open();
//
//            // Specify the path to the font file that supports Hindi (or other languages)
//            String fontPath = defaultDirectory + "static" + File.separator + "fonts" + File.separator + "Mukta" + File.separator + "Mukta-Regular.ttf";
//            BaseFont baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//            Font regularFont = new Font(baseFont, 12);
//            Font boldFont = new Font(baseFont, 12, Font.BOLD);
//            Font italicFont = new Font(baseFont, 12, Font.ITALIC);
//
//            // Parse the HTML content using Jsoup
//            org.jsoup.nodes.Document htmlDoc = Jsoup.parse(htmlContent);
//
//            // Process the body of the HTML
//            Elements bodyElements = htmlDoc.body().children();
//            for (Element element : bodyElements) {
//                if (element.tagName().equals("p")) {
//                    Paragraph paragraph = new Paragraph();
//                    processParagraphElement(element, paragraph, regularFont, boldFont, italicFont);
//                    document.add(paragraph);
//                } else if (element.tagName().equals("br")) {
//                    // Add a line break
//                    document.add(Chunk.NEWLINE);
//                }
//            }
//
//        } catch (DocumentException | IOException e) {
//            e.printStackTrace();
//            throw new IOException("Failed to generate PDF from HTML: " + e.getMessage(), e);
//        } finally {
//            if (document.isOpen()) {
//                document.close();
//            }
//        }
//    }
//
//
//    private void processParagraphElement(Element element, Paragraph paragraph, Font regularFont, Font boldFont, Font italicFont) {
//        for (Node childNode : element.childNodes()) {
//            if (childNode instanceof TextNode) {
//                // Append regular text
//                paragraph.add(new Chunk(((TextNode) childNode).text(), regularFont));
//            } else if (childNode instanceof Element) {
//                Element childElement = (Element) childNode;
//
//                // Handle <strong> or <b> for bold text
//                if (childElement.tagName().equals("strong") || childElement.tagName().equals("b")) {
//                    paragraph.add(new Chunk(childElement.text(), boldFont));
//                }
//
//                // Handle <i> for italic text
//                else if (childElement.tagName().equals("i")) {
//                    paragraph.add(new Chunk(childElement.text(), italicFont));
//                }
//
//                // Handle <br> for line break
//                else if (childElement.tagName().equals("br")) {
//                    paragraph.add(Chunk.NEWLINE);
//                }
//
//                // Recursively process nested elements
//                else {
//                    processParagraphElement(childElement, paragraph, regularFont, boldFont, italicFont);
//                }
//            }
//        }
//    }

    //Commmon Pdf Function And Used itext5 PDF old comibined m ethod
//    private void combinePdfWithExisting(Long applicationId, String absoluteLetterPdfPath, String oldCombinedPdfPath, String nocName, String agencyName) {
//        Document document = new Document();
//        try {
//            // Create the directory structure for the merged PDF
//            File baseDir = new File(uploadDirectory + "/NOC");
//            if (!baseDir.exists()) {
//                baseDir.mkdirs(); // Create base NOC directory if it doesn't exist
//            }
//
//            File nocDir = new File(baseDir, nocName);
//            if (!nocDir.exists()) {
//                nocDir.mkdirs(); // Create NOC directory if it doesn't exist
//            }
//
//            File agencyDir = new File(nocDir, agencyName);
//            if (!agencyDir.exists()) {
//                agencyDir.mkdirs(); // Create agency directory if it doesn't exist
//            }
//
//            // Add "app_" prefix to the application directory name
////            String appApplicationId = "app_" + applicationId;
//            File applicationDir = new File(agencyDir, String.valueOf(applicationId));
//            if (!applicationDir.exists()) {
//                applicationDir.mkdirs(); // Create application directory if it doesn't exist
//            }
//
//            File combinedPdfsDir = new File(applicationDir, "CombinedPdfs");
//            if (!combinedPdfsDir.exists()) {
//                combinedPdfsDir.mkdirs(); // Create CombinedPdfs directory if it doesn't exist
//            }
//
//            // Generate the full path for saving the merged PDF
//            String fullPath = combinedPdfsDir.getAbsolutePath() + "/" + applicationId + "_combined.pdf";
//
//            // Create PdfCopy for the combined PDF
//            PdfCopy copy = new PdfCopy(document, new FileOutputStream(fullPath));
//            document.open();
//
//            // Add the old combined PDF
//            PdfReader oldPdfReader = new PdfReader(oldCombinedPdfPath);
//            int nOldPages = oldPdfReader.getNumberOfPages();
//            for (int i = 1; i <= nOldPages; i++) {
//                PdfImportedPage page = copy.getImportedPage(oldPdfReader, i);
//                copy.addPage(page);
//            }
//            oldPdfReader.close();
//
//            // Add the new PDF
//            PdfReader newPdfReader = new PdfReader(absoluteLetterPdfPath);
//            int nNewPages = newPdfReader.getNumberOfPages();
//            for (int i = 1; i <= nNewPages; i++) {
//                PdfImportedPage page = copy.getImportedPage(newPdfReader, i);
//                copy.addPage(page);
//            }
//            newPdfReader.close();
//
//            // Close the document
//            document.close();
//
//            // Generate the relative path for storing in the database
//            String relativePath = Paths.get("NOC", nocName, agencyName, applicationId.toString(), "CombinedPdfs", applicationId + "_combined.pdf").toString().replace("\\", "/");
//
//            // Update the Application with the new combined PDF path
//            Application application = applicationRepository.findById(applicationId).orElseThrow();
//            application.setPdfPath(relativePath);
//            applicationRepository.save(application);
//
//        } catch (IOException | DocumentException e) {
//            // Handle exceptions
//            e.printStackTrace();
//        } finally {
//            // Ensure the document is closed even if an exception occurs
//            if (document.isOpen()) {
//                document.close();
//            }
//        }
//    }

    //    code for combinedPDF
// Common PDF Function and Used itext5 PDF
    private void combinePdfWithExisting(Long applicationId, String absoluteLetterPdfPath, String oldCombinedPdfPath, String nocName, String agencyName) {
        Document document = new Document();
        try {
            String applicationFolder = "app_" + applicationId;
            // Build the path for the combined PDF directory
            Path combinedPdfsDir = Paths.get(uploadDirectory, "NOC", nocName, agencyName, applicationFolder, "CombinedPdfs");
            Path relativeCombinedPdfsDir = Paths.get("NOC", nocName, agencyName, applicationFolder, "CombinedPdfs");

            // Create the directory structure if it doesn't exist
            if (!Files.exists(combinedPdfsDir)) {
                Files.createDirectories(combinedPdfsDir); // Recursively creates directories
            }

            // Generate paths for the temp and final combined PDFs
            Path tempFullPath = combinedPdfsDir.resolve(applicationId + "_temp_combined.pdf");
            Path fullPath = combinedPdfsDir.resolve(applicationId + "_combined.pdf");

            // Create PdfCopy for the combined PDF
            PdfCopy copy = new PdfCopy(document, new FileOutputStream(tempFullPath.toFile()));
            document.open();


            // Add the new PDF
            PdfReader newPdfReader = new PdfReader(absoluteLetterPdfPath);
            int nNewPages = newPdfReader.getNumberOfPages();
            for (int i = 1; i <= nNewPages; i++) {
                PdfImportedPage page = copy.getImportedPage(newPdfReader, i);
                copy.addPage(page);
            }
            newPdfReader.close();


            // Add the old combined PDF if it exists
            if (Files.exists(Paths.get(oldCombinedPdfPath))) {

                Path tempDirectory = Files.createTempDirectory("myTempDir_"); // Create temp directory

                // Assuming oldCombinedPdfPath is a Path object or you convert it to Path
                Path sourceFile = Paths.get(oldCombinedPdfPath); // The source file path
                Path targetFile = tempDirectory.resolve(sourceFile.getFileName()); // Target file inside temp directory

                // Copy the source file to the temp directory
                Files.copy(sourceFile, targetFile, StandardCopyOption.COPY_ATTRIBUTES);

                PdfReader oldPdfReader = new PdfReader(Files.newInputStream(targetFile));
                int nOldPages = oldPdfReader.getNumberOfPages();
                for (int i = 1; i <= nOldPages; i++) {
                    PdfImportedPage page = copy.getImportedPage(oldPdfReader, i);
                    copy.addPage(page);
                }
                oldPdfReader.close();
            }



            // Close the document and the PdfCopy
            document.close();
            copy.close();

            Files.deleteIfExists(Paths.get(oldCombinedPdfPath));

            // Move the temp file to the final destination (renaming)
            Files.move(tempFullPath, fullPath, StandardCopyOption.REPLACE_EXISTING);

            // Generate the relative path for storing in the database
            String relativePath = relativeCombinedPdfsDir.resolve(applicationId + "_combined.pdf").toString().replace("\\", "/");

            // Update the Application with the new combined PDF path
            Application application = applicationRepository.findById(applicationId).orElseThrow();
            application.setPdfPath(relativePath);
            applicationRepository.save(application);

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        } finally {
            // Ensure the document is closed even if an exception occurs
            try {
                if (document.isOpen()) {
                    document.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


//    @Override
//    public void savingLetters(MultipartFile file, String content, Long applicationId, String description, String nocType) throws IOException {
//        // Parse HTML content using Jsoup
//        org.jsoup.nodes.Document htmlDocument = Jsoup.parse(content);
//
//        System.out.println("Html Data: " + htmlDocument);
//
//        // Initialize a StringBuilder to accumulate plain text content
//        StringBuilder plainTextContent = new StringBuilder();
//
//        // Process paragraphs
//        Elements paragraphs = htmlDocument.select("p");
//        for (Element paragraph : paragraphs) {
//            if (plainTextContent.length() > 0) {
//                plainTextContent.append("\n");  // Add a line break between paragraphs
//            }
//            plainTextContent.append(paragraph.text());  // Append text of each paragraph
//        }
//
//        // Process <br> tags
//        Elements lineBreaks = htmlDocument.select("br");
//        for (Element lineBreak : lineBreaks) {
//            if (plainTextContent.length() > 0) {
//                plainTextContent.append("\n");  // Add a line break for each <br> tag
//            }
//            // The lineBreak.text() is usually empty, so it is not appended.
//        }
//
//        // Debugging: Check content before passing it to PDF generation
//        System.out.println("Final plain text content for PDF: " + plainTextContent.toString());
//
//        // Log raw and plain text content
//        System.out.println("Raw content with line breaks: " + content);
//        System.out.println("Plain text content: " + plainTextContent.toString());
//
//        // Get the username from security context
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        // Fetch the application entity
//        Optional<Application> applicationOpt = applicationRepository.findById(applicationId);
//        if (applicationOpt.isEmpty()) {
//            throw new IllegalArgumentException("Application not found");
//        }
//        Application application = applicationOpt.get();
//
//        // Generate a timestamp for the filename
//        long timestamp = System.currentTimeMillis();
//
//        // Generate the PDF file path in the Letters folder, including timestamp in the filename
//        String generatedPdfFilePath = uploadDirectory + "/Letters/Letter_" + timestamp + ".pdf";
//
//        // Generate the PDF and save it
//        generatePdfFromText(plainTextContent.toString(), generatedPdfFilePath);
//
//        // Save the PDF details in ListOfLetters entity
//        ListOfLetters listOfLetters = new ListOfLetters();
//        listOfLetters.setDocPath(generatedPdfFilePath);  // Path to the generated PDF
//        listOfLetters.setIssuedBy(username);  // The user who issued it
//        listOfLetters.setDescription(description);  // Description for the letter
//        listOfLetters.setApplication(application);  // The associated application
//
//        // Save the ListOfLetters entity to the repository
//        listOfLettersRepository.save(listOfLetters);
//    }
//
//    private void generatePdfFromText(String textContent, String outputPath) throws IOException {
//        Document document = new Document();  // iText 5 Document class
//
//        try {
//            // Create a PdfWriter instance
//            PdfWriter.getInstance(document, new FileOutputStream(outputPath));
//
//            // Open the document for writing
//            document.open();
//
//            // Specify the path to the font file that supports Hindi
//            String fontPath = defaultDirectory + "static" + File.separator + "fonts" + File.separator + "Mukta" + File.separator + "Mukta-Regular.ttf";
//            BaseFont baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//            Font font = new Font(baseFont, 12);  // Set the font size (12 in this case)
//
//            // Split the text content by new lines and add each line as a paragraph with the specified font
//            String[] lines = textContent.split("\\r?\\n");
//            for (String line : lines) {
//                if (!line.trim().isEmpty()) {  // Avoid adding empty lines
//                    Paragraph paragraph = new Paragraph(line, font);  // Create a paragraph for each line using the font
//                    document.add(paragraph);  // Add the paragraph to the document
//                    System.out.println("Added line to PDF: " + line);  // Log each line added to the PDF
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new IOException("Failed to generate PDF: " + e.getMessage(), e);
//        } finally {
//            // Close the document to finalize the PDF
//            if (document.isOpen()) {
//                document.close();
//            }
//        }
//    }


    @Override
    public void savingTrialPdf(MultipartFile file) throws IOException {
        MultipartFile multipartFile = file;

        if (multipartFile != null && !multipartFile.isEmpty()) {

            String fileName = multipartFile.getOriginalFilename();

            String filePath = uploadDirectory + "/" + "Letters/" + fileName;

            Files.createDirectories(Path.of(filePath).getParent());
            multipartFile.transferTo(new File(filePath));

            ListOfLetters listOfLetters = new ListOfLetters();

            listOfLetters.setDocPath(filePath);
//            listOfLetters.setIssuedBy(username);
//
//
//            Application application = applicationRepository.findById(applicationId).get();

//            listOfLetters.setApplication(application);

            listOfLettersRepository.save(listOfLetters);

        }
    }

    @Override
    public Long savingOrder(Orders orders) {
        try {

            Instant timestamp = Instant.now();
            long timestampMillis = timestamp.toEpochMilli();

            MultipartFile multipartFile = orders.getUploadDoc();

            if (multipartFile != null && !multipartFile.isEmpty()) {

                String fileName = multipartFile.getOriginalFilename();
                String[] name = fileName.split("\\.");
                int n = name.length - 1;
                String originalName = orders.getId() + "(" + timestampMillis + ")" + "." + name[n];
                String filePath = uploadDirectory + "Orders/" + originalName;
                Files.createDirectories(Path.of(filePath).getParent());
                multipartFile.transferTo(new File(filePath));

                orders.setUploadDocPath(originalName);

                Path parentDirectory = Paths.get(uploadDirectory);
                if (!Files.exists(parentDirectory)) {
                    System.out.println("Parent directory does not exist. Creating parent directory.");
                    Files.createDirectories(parentDirectory);
                }

            }

            ordersRepository.save(orders);


            Long orderCreated = Long.valueOf(orders.getId());

            return orderCreated;

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        }
    }

    @Override
    public List<Orders> findallorder() {


        return ordersRepository.findAll();
    }

    @Override
    public void deleteorder(Long id) {
        ordersRepository.deleteById(id);
    }

    @Override
    public Long savingGrevance(Grievances grievances) {
        try {

            grievancesRepository.save(grievances);


            Long grevancesCreated = Long.valueOf(grievances.getId());

            return grevancesCreated;

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        }
    }

    @Override
    public List<Grievances> findallgrevance() {
        return grievancesRepository.findAll();
    }

    @Override
    public void deletegrevance(Long id) {
        grievancesRepository.deleteById(id);
    }

    @Override
    public Orders updateOrders(Orders newOrders) {

        Optional<Orders> optionalOrders = ordersRepository.findById(newOrders.getId());

        if (optionalOrders.isPresent()) {
            Orders existingOrders = optionalOrders.get();

            if (newOrders.getDescription() != null) {
                existingOrders.setDescription(newOrders.getDescription());
            }

            return ordersRepository.save(existingOrders);
        } else {
            return null;
        }
    }

    @Override
    public Orders findOrderById(Long id) {

        Optional<Orders> optionalOrders = ordersRepository.findById(id);
        return optionalOrders.orElse(null);
    }

    @Override
    public List<Grievances> findallgrevanceByusername(String username) {
        return grievancesRepository.findByusername(username);
    }

    @Override
    public Grievances updateGrievances(Grievances newGrievances) {
        Optional<Grievances> optionalGrievances = grievancesRepository.findById(newGrievances.getId());

        if (optionalGrievances.isPresent()) {
            Grievances existingGrievances = optionalGrievances.get();

            if (newGrievances.getReply() != null) {
                existingGrievances.setReply(newGrievances.getReply());
            }

            return grievancesRepository.save(existingGrievances);
        } else {
            return null;
        }
    }

    @Override
    public Grievances findGrievancesById(Long id) {
        Optional<Grievances> optionalGrievances = grievancesRepository.findById(id);
        return optionalGrievances.orElse(null);
    }

//    @Override
//    public Long savingfeedback(Feedback feedback) {
//        try {
//
//            Instant timestamp = Instant.now();
//            long timestampMillis = timestamp.toEpochMilli();
//
//            feedback.setStatus("hide");
//
//            MultipartFile multipartFile = feedback.getUploadImage();
//
//            if (multipartFile != null && !multipartFile.isEmpty()) {
//
//                String fileName = multipartFile.getOriginalFilename();
//                String[] name = fileName.split("\\.");
//                int n = name.length - 1;
//                String originalName = feedback.getAgencyName() + "(" + timestampMillis + ")" + "." + name[n];
//
//
//                String filePath = uploadDirectory + "Feedback/" + originalName;
//                Files.createDirectories(Path.of(filePath).getParent());
//                multipartFile.transferTo(new File(filePath));
//
//                feedback.setUploadImagePath(originalName);
//
//                Path parentDirectory = Paths.get(uploadDirectory);
//                if (!Files.exists(parentDirectory)) {
//                    System.out.println("Parent directory does not exist. Creating parent directory.");
//                    Files.createDirectories(parentDirectory);
//                }
//
//            }
//
//            feedbackRepository.save(feedback);
//
//
//            Long feedbackCreated = Long.valueOf(feedback.getId());
//
//            return feedbackCreated;
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//            return null;
//
//        }
//    }


    @Override
    public Long savingfeedback(Feedback feedback) {
        try {
            Instant timestamp = Instant.now();
            long timestampMillis = timestamp.toEpochMilli();

            feedback.setStatus("hide");

            MultipartFile multipartFile = feedback.getUploadImage();

            if (multipartFile != null && !multipartFile.isEmpty()) {
                String fileName = multipartFile.getOriginalFilename();

                // Extract the extension (ensure correct mapping for jpeg/jpg/png)
                String extension = "";
                if (fileName != null) {
                    int index = fileName.lastIndexOf('.');
                    if (index > 0 && index < fileName.length() - 1) {
                        extension = fileName.substring(index + 1).toLowerCase();
                    }
                }


                String finalFileName = feedback.getAgencyName() + "_feedback_" + "." + extension;


                String directoryPath = uploadDirectory + "/NOC/" + "Feedback/" + feedback.getAgencyName() + "/";
                String absolutePath = directoryPath + finalFileName;
                String relativePath = "NOC/" + "Feedback/" + feedback.getAgencyName() + "/" + finalFileName;


                // Set the relative path in feedback
                feedback.setUploadImagePath(relativePath);

                // Save the file to the absolute path
                File fileToSave = new File(absolutePath);
                fileToSave.getParentFile().mkdirs(); // Create directories if they don't exist
                multipartFile.transferTo(fileToSave); // Save the file to disk

                // Save feedback to the database (assuming you have a repository to save feedback)
                feedbackRepository.save(feedback);

                // Return success or feedback ID
                return feedback.getId();
            }

        } catch (IOException e) {
            e.printStackTrace(); // Log error
        }

        return null; // In case of failure
    }

    @Override
    public byte[] fetchPdfOfOrderById(Long id) throws IOException {
        String baseDirectory = uploadDirectory + "Orders/";

        // Get the relative PDF path from the document
        String relativePdfPath = ordersRepository.findPathByOrderId(id);

        // Create the full path by appending the relative path to the base directory
        Path fullPath = Paths.get(baseDirectory, relativePdfPath);

        // Check if the file exists before trying to read it
        if (!Files.exists(fullPath)) {
            throw new FileNotFoundException("PDF file not found at path: " + fullPath.toString());
        }

        // Read the PDF file into a byte array
        byte[] pdfData = Files.readAllBytes(fullPath);

        return pdfData;
    }

    @Override
    public List<Feedback> findallfeddback() {
        return feedbackRepository.findAll();
    }

    @Override
    public byte[] fetchImageOfFeedbackById(Long id) throws IOException {
        String baseDirectory = uploadDirectory;

        // Get the relative PDF path from the document
        String relativePdfPath = feedbackRepository.findPathByfeedbackId(id);

        // Create the full path by appending the relative path to the base directory
        Path fullPath = Paths.get(baseDirectory, relativePdfPath);

        // Check if the file exists before trying to read it
        if (!Files.exists(fullPath)) {
            throw new FileNotFoundException("PDF file not found at path: " + fullPath.toString());
        }

        // Read the PDF file into a byte array
        byte[] pdfData = Files.readAllBytes(fullPath);

        return pdfData;
    }

    @Override
    public void deletefeedback(Long id) {
        feedbackRepository.deleteById(id);
    }

    @Override
    public boolean checkApplicationTransferredToEe(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // Retrieve the transfer status
        String transferStatus = application.getTransferStatus();

        // Return true if the status is not "FORWARDING"
        return !transferStatus.equalsIgnoreCase("FORWARDING");
    }

    @Override
    public boolean checkApplicationTrasferStatusCrossVerification(Long applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // Retrieve the transfer status
        String transferStatus = application.getTransferStatus();

        // Return true if the status is "CROSS_VERIFICATION"
        return transferStatus.equalsIgnoreCase("CROSS_VERIFICATION");
    }

    @Override
    public boolean checkApplicationStatusApproval(Long applicationId) {

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // Retrieve the transfer status
        String transferStatus = application.getTransferStatus();

        // Return true if the status is "CROSS_VERIFIED"
        return transferStatus.equalsIgnoreCase("CROSS_VERIFIED");


    }

    @Override
    public boolean checkApplicationStatusIssuanceAndNocType(Long applicationId) {

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // Retrieve the transfer status and NOC type
        String transferStatus = application.getTransferStatus();
        String nocType = application.getAppliedFor();

        // Return true if the transfer status is "ISSUANCE" and the NOC type is "jal avantan"
        return transferStatus.equalsIgnoreCase("ISSUANCE") && nocType.equalsIgnoreCase("जल आवंटन");
    }


    private String saveFileForAttach(String uploadDir, MultipartFile file, boolean replaceExisting, String nocName, String agencyName, String applicationId, String subDirectory, String originalFileName) {
        try {

            String fileName = applicationId + "_" + originalFileName + "_1" + ".pdf";
            // Build the relative path using NOC name, agency name, application ID, and subdirectory
            String relativePath = Paths.get("NOC", nocName, agencyName, applicationId, subDirectory, fileName).toString().replace("\\", "/");

            // Build the full path for storage
            Path filePath = Paths.get(uploadDir, relativePath);

            // Create the NOC directory and subdirectories if they don't exist
            Files.createDirectories(filePath.getParent());

            // Copy the file to the target location, with or without replacing existing files
            if (replaceExisting) {
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                Files.copy(file.getInputStream(), filePath);
            }

            // Return the relative path for saving in the database
            return relativePath;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }


    private List<ListOfLetters> methodToSavedInAtatchLetter(Long applicationId, Application application, String description, MultipartFile file, String remarks, String nocName, String agencyLetterPath) throws IOException {

        List<ListOfLetters> lettersList = new ArrayList<>();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Define the new file name format as "app_applicationId_AttachedLetterBy_username.pdf"
        String fileName = "app_" + applicationId + "_AttachedLetterBy_" + username + ".pdf";

        // Base directory path (absolute path) where files will be saved
        String absoluteBasePath = uploadDirectory + "NOC/ListOfLetters/" + nocName + "/app_" + applicationId + "/AttachedDocumentLetter";

        // Complete absolute path where the file will be saved
        String absoluteFilePath = absoluteBasePath + "/" + fileName;

        // Relative path to store in the database
        String relativeFilePath = "NOC/ListOfLetters/" + nocName + "/app_" + applicationId + "/AttachedDocumentLetter/" + fileName;

        // Ensure the "NOC" directory exists
        Path nocDirectoryPath = Path.of(uploadDirectory + "NOC");
        if (!Files.exists(nocDirectoryPath)) {
            Files.createDirectories(nocDirectoryPath);
        }

        // Ensure the "nocName" directory exists
        Path nocNameDirectoryPath = Path.of(uploadDirectory + "NOC/ListOfLetters/" + nocName);
        if (!Files.exists(nocNameDirectoryPath)) {
            Files.createDirectories(nocNameDirectoryPath);
        }

        // Ensure the "app_applicationId/AttachedDocumentLetter" directory exists
        Path applicationDirectoryPath = Path.of(absoluteBasePath);
        if (!Files.exists(applicationDirectoryPath)) {
            Files.createDirectories(applicationDirectoryPath);
        }

        // Check if the file already exists, and if so, delete it
        File existingFile = new File(absoluteFilePath);
        if (existingFile.exists()) {
            existingFile.delete();
        }

        // Use Files.copy to copy the file for reuse
        Files.copy(Path.of(agencyLetterPath), Path.of(absoluteFilePath), StandardCopyOption.REPLACE_EXISTING);


        // Create and set details for ListOfLetters entity
        ListOfLetters listOfLetters = new ListOfLetters();
        listOfLetters.setDocPath(relativeFilePath);  // Saving relative path in the database
        listOfLetters.setIssuedBy(username);
        listOfLetters.setDescription(description);
        listOfLetters.setApplication(application);
        listOfLettersRepository.save(listOfLetters);
        lettersList.add(listOfLetters);
        // Save the ListOfLetters entity

        return lettersList;

    }


    @Override
    public String savingAttachDocRowData(Long applicationId, String description, MultipartFile file, String remarks, String nocName,
                                         String attachedDocumentType) throws IOException {


        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<String> pdfPaths = new ArrayList<>();
        // Get the agency name from SecurityContextHolder
        String agencyName = SecurityContextHolder.getContext().getAuthentication().getName();

        String agencynameFromApplicationtable = applicationRepository.findByAppId(applicationId);

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // Get the application last ID and calculate the next ID (assuming this is manually incremented)
//        long applicationLastId = applicationRepository.getLastApplicationId(); // Assuming this method exists
        String applicationNewId = "app_" + applicationId;

        String originalFileName = file.getOriginalFilename();
        // Define subdirectories for CD and PD
        String cdSubDirectory = "CurrDoc";
        String pdSubDirectory = "PrevDoc";
        String agencyLetterPath = "";


        // Save the file using its original name in the CD and PD directories
        String cdPath = saveFileForAttach(uploadDirectory, file, true, nocName, agencynameFromApplicationtable, applicationNewId.toString(), cdSubDirectory, originalFileName);
        String pdPath = saveFileForAttach(uploadDirectory, file, true, nocName, agencynameFromApplicationtable, applicationNewId.toString(), pdSubDirectory, originalFileName);
        agencyLetterPath = uploadDirectory + cdPath;
        pdfPaths.add(cdPath);

        // Add the new document at the last row position
        DocumentStatus newDocumentStatus = new DocumentStatus();
        newDocumentStatus.setApplication(application); // Set the application reference
        newDocumentStatus.setDescription(description);
        newDocumentStatus.setDataField(cdPath);
//        newDocumentStatus.setCurrStatus(currStatus);
        newDocumentStatus.setRemarks(remarks);
        currentDocumentRepo.save(newDocumentStatus);


        // Log the same in LogsOfDocument
        LogsOfDocument newLogsOfDocument = new LogsOfDocument();
        newLogsOfDocument.setApplication(application); // Set the application reference
        newLogsOfDocument.setDocumentStatus(newDocumentStatus);
        newLogsOfDocument.setDescription(description);
        newLogsOfDocument.setDataField(pdPath);
//        newLogsOfDocument.setCurrStatus(currStatus);
        newLogsOfDocument.setRemarks(remarks);
        logsOfDocumentRepo.save(newLogsOfDocument);

//        this function has to be updated
        if (attachedDocumentType.equalsIgnoreCase("Letter")) {
            List<ListOfLetters> letters = methodToSavedInAtatchLetter(applicationId, application, description, file, remarks, nocName, agencyLetterPath);

        }

        // Paths for combining PDFs
        String absoluteLetterPdfPath = uploadDirectory + cdPath;
        String oldCombinedpdfPath = uploadDirectory + application.getPdfPath();

        System.out.println("absoluteLetterPdfPath: " + absoluteLetterPdfPath);
        System.out.println("oldCombinedpdfPath: " + oldCombinedpdfPath);

        // Combine the new PDF with the existing combined PDF
        combinePdfWithExisting(applicationId, absoluteLetterPdfPath, oldCombinedpdfPath,
                application.getAppliedFor(), application.getCreatedBy());


        return "Document data saved successfully";
    }


    @Override
    public List<Map<String, Object>> fetchApplicationIDDetail(Long id) {
        return List.of();
    }

    @Override
    public List<Map<String, Object>> getAgencyApplicationsWhereStatus(String username, String financialYear, String status) {

        // Extract the start and end years from the "yyyy-yyyy" format
        String[] years = financialYear.split("-");
        if (years.length != 2) {
            throw new IllegalArgumentException("Invalid financial year format");
        }
        int startYear = Integer.parseInt(years[0]);
        int endYear = Integer.parseInt(years[1]);

        // Create start and end dates for the financial year (April 1 to March 31)
        Calendar startCal = Calendar.getInstance();
        startCal.set(startYear, Calendar.APRIL, 1, 0, 0, 0); // April 1st
        startCal.set(Calendar.MILLISECOND, 0);
        Date startDate = new Date(startCal.getTimeInMillis());

        Calendar endCal = Calendar.getInstance();
        endCal.set(endYear, Calendar.MARCH, 31, 23, 59, 59); // March 31st
        endCal.set(Calendar.MILLISECOND, 999);
        Date endDate = new Date(endCal.getTimeInMillis());

        UsersSpring user = usersRepository.findUserByUsername(username);

        Long id = user.getId();

        UserDetails userDetails = userDetailsRepository.findByUserId(id);

        Long userDetailsId = userDetails.getId();

        return applicationRepository.findAllApplicationWhereStatus(userDetailsId, status, startDate, endDate);

    }

    @Override
    public String[] savingReviewedNocFormData(Application application, DocumentStatus documentStatus) {

        // Fetch the application data from the repository
        Application applicationList = applicationRepository.findApplicationDataByAppId(application.getId());

        String currentRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().toArray()[0].toString();

//        Logic For Setting Movement Status = REJECTED_APPLICATION_CLOSED  If Current User is Rejected

        if (currentRole.equalsIgnoreCase("ce1")) {
            // Check if the CE1 status is exactly "REJECTED_APPLICATION_CLOSED"
            String ce1Status = applicationList.getCe1Status();
            if ("REJECTED_APPLICATION_CLOSED".equalsIgnoreCase(ce1Status)) {

                // Fetch the MovementStatus by application ID
                MovementStatus movementStatus = appCurrentStatusRepository.findByApplicationId(applicationList.getId());

                // Check if movementStatus is not null before setting the status
                if (movementStatus != null) {
                    // Update the status with the CE1 status
                    movementStatus.setStatus(ce1Status);

                    // Save the updated MovementStatus
                    appCurrentStatusRepository.save(movementStatus);
                } else {
                    // Handle the case when movementStatus is null (optional logging or exception)
                    System.out.println("MovementStatus not found for application ID: " + applicationList.getId());
                }
            }
        }

        if (currentRole.equalsIgnoreCase("ce2")) {
            // Check if the CE2 status is exactly "REJECTED_APPLICATION_CLOSED"
            String ce2Status = applicationList.getCe2Status();
            if ("REJECTED_APPLICATION_CLOSED".equalsIgnoreCase(ce2Status)) {

                // Fetch the MovementStatus by application ID
                MovementStatus movementStatus = appCurrentStatusRepository.findByApplicationId(applicationList.getId());

                // Check if movementStatus is not null before setting the status
                if (movementStatus != null) {
                    // Update the status with the CE2 status
                    movementStatus.setStatus(ce2Status);

                    // Save the updated MovementStatus
                    appCurrentStatusRepository.save(movementStatus);
                } else {
                    // Optional: Handle the case when movementStatus is null (log or throw an exception)
                    System.out.println("MovementStatus not found for application ID: " + applicationList.getId());
                }
            }
        }

        if (currentRole.equalsIgnoreCase("se")) {
            // Check if the SE status is exactly "REJECTED_APPLICATION_CLOSED"
            String seStatus = applicationList.getSeStatus();
            if ("REJECTED_APPLICATION_CLOSED".equalsIgnoreCase(seStatus)) {

                // Fetch the MovementStatus by application ID
                MovementStatus movementStatus = appCurrentStatusRepository.findByApplicationId(applicationList.getId());

                // Check if movementStatus is not null before setting the status
                if (movementStatus != null) {
                    // Update the status with the SE status
                    movementStatus.setStatus(seStatus);

                    // Save the updated MovementStatus
                    appCurrentStatusRepository.save(movementStatus);
                } else {
                    // Handle the case when movementStatus is null (if needed)
                    // You can log a warning or throw an exception if this case is unexpected
                    System.out.println("MovementStatus not found for application ID: " + applicationList.getId());
                }
            }
        }

        if (currentRole.equalsIgnoreCase("ee")) {
            // Check if the EE status is exactly "REJECTED_APPLICATION_CLOSED"
            String eeStatus = applicationList.getEeStatus();
            if ("REJECTED_APPLICATION_CLOSED".equalsIgnoreCase(eeStatus)) {

                // Fetch the MovementStatus by application ID
                MovementStatus movementStatus = appCurrentStatusRepository.findByApplicationId(applicationList.getId());

                // Check if movementStatus is not null before setting the status
                if (movementStatus != null) {
                    // Update the status with the EE status
                    movementStatus.setStatus(eeStatus);

                    // Save the updated MovementStatus
                    appCurrentStatusRepository.save(movementStatus);
                } else {
                    // Optional: Handle the case when movementStatus is null (log or throw an exception)
                    System.out.println("MovementStatus not found for application ID: " + applicationList.getId());
                }
            }
        }

// Assuming you want to update the status of the first application in the list


        String rejectionStatus = application.getHodStatus();
        String role = application.getAdminStatus();
//        System.out.println("role" + role);
//        System.out.println("rejectionStatus" + rejectionStatus);

        // Update the status based on the role
        switch (role) {
            case "admin":
                applicationList.setAdminStatus(rejectionStatus);
                break;
            case "ce1":
                applicationList.setCe1Status(rejectionStatus);
                break;
            case "ce2":
                applicationList.setCe2Status(rejectionStatus);
                break;
            case "se":
                applicationList.setSeStatus(rejectionStatus);
                break;
            case "ee":
                applicationList.setEeStatus(rejectionStatus);
                break;
            case "deputyAdmin":
                applicationList.setDeputyAdminStatus(rejectionStatus);
                break;
            case "subAdmin":
                applicationList.setSubAdminStatus(rejectionStatus);
                break;
            case "hod":
                applicationList.setHodStatus(rejectionStatus);
                break;
            case "administration":
                applicationList.setAdministrationStatus(rejectionStatus);
                break;
            default:
                // Handle unknown roles if necessary
                System.out.println("Unknown role: " + role);
                break;
        }

        applicationRepository.save(applicationList);


        List<DocumentStatus> documentStatusList = currentDocumentRepo.findAllDataByApplicatioId(application.getId());
        List<LogsOfDocument> logsOfDocumentList = logsOfDocumentRepo.findAllDataByApplicatioId(application.getId());


        for (DocumentStatus doc : application.getDocumentStatus()) {
            // Print out the associationId, description, and other relevant fields
//            System.out.println("Association ID: " + doc.getAssociationId());
//            System.out.println("Description: " + doc.getDescription());
//            System.out.println("Current Status: " + doc.getCurrStatus());
//            System.out.println("Remark: " + doc.getRemarks());

            DocumentStatus existingDocumentStatus = documentStatusList.stream()
                    .filter(d -> d.getAssociationId().equals(doc.getAssociationId()))
                    .findFirst()
                    .orElse(null);

            if (existingDocumentStatus != null) {
                // Update currStatus if new value is provided and it's not null or empty
                if (doc.getCurrStatus() != null && !doc.getCurrStatus().trim().isEmpty()) {
                    existingDocumentStatus.setCurrStatus(doc.getCurrStatus());
                }

                // Update remarks if new value is provided and it's not null or empty
                if (doc.getRemarks() != null && !doc.getRemarks().trim().isEmpty()) {
                    existingDocumentStatus.setRemarks(doc.getRemarks());
                }

                // Save the updated DocumentStatus
                currentDocumentRepo.save(existingDocumentStatus);
            } else {
                System.out.println("No matching DocumentStatus found for Association ID: " + doc.getAssociationId());
            }
            // Now update LogsOfDocument for the matched DocumentStatus
            LogsOfDocument log = logsOfDocumentList.stream()
                    .filter(l -> l.getAssociationId().equals(doc.getAssociationId()))
                    .findFirst()
                    .orElse(null);

            if (log != null) {
                // Update currStatus in LogsOfDocument if new value is provided and it's not null or empty
                if (doc.getCurrStatus() != null && !doc.getCurrStatus().trim().isEmpty()) {
                    log.setCurrStatus(doc.getCurrStatus());
                }

                // Update remarks in LogsOfDocument if new value is provided and it's not null or empty
                if (doc.getRemarks() != null && !doc.getRemarks().trim().isEmpty()) {
                    log.setRemarks(doc.getRemarks());
                }

                // Save the updated LogsOfDocument
                logsOfDocumentRepo.save(log);
            } else {
                System.out.println("No matching Logs Of document found for Association ID: " + doc.getAssociationId());
            }


        }

        return null;
    }

    @Override
    public Feedback findFeedbackById(Long id) {
        Optional<Feedback> optionalFeedback = feedbackRepository.findById(id);
        return optionalFeedback.orElse(null);
    }

    @Override
    public Feedback updateFeedback(Feedback newfeedback) {
        Optional<Feedback> optionalFeedback = feedbackRepository.findById(newfeedback.getId());

        if (optionalFeedback.isPresent()) {
            Feedback existingFeedback = optionalFeedback.get();

            existingFeedback.setStatus("show");

            return feedbackRepository.save(existingFeedback);
        } else {
            return null;
        }
    }


//    private String saveFileForGeneratedLetterAttach(String uploadDir, MultipartFile file, boolean replaceExisting, String nocName, String agencyName, String applicationId, String subDirectory, String originalFileName) {
//        try {
//
//            String fileName = applicationId + "_" + originalFileName + "_1" + ".pdf";
//            // Build the relative path using NOC name, agency name, application ID, and subdirectory
//            String relativePath = Paths.get("NOC", nocName, agencyName, applicationId, subDirectory, fileName).toString().replace("\\", "/");
//
//            // Build the full path for storage
//            Path filePath = Paths.get(uploadDir, relativePath);
//
//            // Create the NOC directory and subdirectories if they don't exist
//            Files.createDirectories(filePath.getParent());
//
//            // Copy the file to the target location, with or without replacing existing files
//            if (replaceExisting) {
//                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//            } else {
//                Files.copy(file.getInputStream(), filePath);
//            }
//
//            // Return the relative path for saving in the database
//            return relativePath;
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
//        }
//    }

//    private String saveFileForGeneratedLetterAttach(String uploadDir, MultipartFile file, boolean replaceExisting, String nocName, String agencyName, String applicationId, String subDirectory, String originalFileName) {
//        try {
//            // Construct the filename
//            String fileName = applicationId + "_" + originalFileName + "_1" + ".pdf";
//
//            // Build the relative path using NOC name, agency name, application ID, and subdirectory
//            String relativePath = Paths.get("NOC", nocName, agencyName, applicationId, subDirectory, fileName).toString().replace("\\", "/");
//
//            // Build the full path for storage
//            Path filePath = Paths.get(uploadDir, relativePath);
//
//            // Create the NOC directory and subdirectories if they don't exist
//            Files.createDirectories(filePath.getParent());
//
//            // Check if the file already exists
//            if (Files.exists(filePath)) {
//                if (replaceExisting) {
//                    // Delete the existing file
//                    Files.delete(filePath);
//                    System.out.println("Existing file deleted: " + filePath.toString());
//                } else {
//                    // If not replacing, throw an exception
//                    throw new IOException("File already exists and replaceExisting is set to false.");
//                }
//            }
//
//            // Copy the file to the target location
//            try (InputStream inputStream = file.getInputStream()) {
//                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
//                System.out.println("File successfully saved: " + filePath.toString());
//            }
//
//            // Return the relative path for saving in the database
//            return relativePath;
//        } catch (IOException e) {
//            // Log more detailed error information
//            System.err.println("Error details: " + e.getMessage());
//            throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
//        }
//    }

    private String saveFileForGeneratedLetterAttach(String uploadDir, MultipartFile file, boolean replaceExisting, String nocName, String agencyName, String applicationId, String subDirectory, String originalFileName) {
        try {
            // Construct the filename
            String fileName = applicationId + "_" + originalFileName + "_1" + ".pdf";

            // Build the relative path using NOC name, agency name, application ID, and subdirectory
            String relativePath = Paths.get("NOC", nocName, agencyName, applicationId, subDirectory, fileName).toString().replace("\\", "/");

            // Build the full path for storage
            Path filePath = Paths.get(uploadDir, relativePath);

            // Create the NOC directory and subdirectories if they don't exist
            Files.createDirectories(filePath.getParent());

            // Check if the file already exists
            if (Files.exists(filePath)) {
                if (replaceExisting) {
                    // Delete the existing file
                    Files.delete(filePath);
                    System.out.println("Existing file deleted: " + filePath.toString());
                } else {
                    // If not replacing, throw an exception
                    throw new IOException("File already exists and replaceExisting is set to false: " + filePath.toString());
                }
            }

            // Copy the file to the target location
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File successfully saved: " + filePath.toString());
            }

            // Return the relative path for saving in the database
            return relativePath;
        } catch (IOException e) {
            // Log more detailed error information
            System.err.println("Error details: " + e.getMessage());
            throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);

        }
    }


    @Override
    public String savingUplodadPdfToForm(Long applicationId, String description, MultipartFile file, String associationId, String nocName) {

        // Get the agency name from SecurityContextHolder
        String agencyName = SecurityContextHolder.getContext().getAuthentication().getName();

        String agencynameFromApplicationtable = applicationRepository.findByAppId(applicationId);
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // Get the application last ID and calculate the next ID (assuming this is manually incremented)
//        long applicationLastId = applicationRepository.getLastApplicationId(); // Assuming this method exists
        String applicationNewId = "app_" + applicationId;

        String originalFileName = file.getOriginalFilename();
        // Define subdirectories for CD and PD
        String cdSubDirectory = "CurrDoc";
        String pdSubDirectory = "PrevDoc";


        // Save the file using its original name in the CD and PD directories
        String cdPath = saveFileForGeneratedLetterAttach(uploadDirectory, file, true, nocName, agencynameFromApplicationtable, applicationNewId.toString(), cdSubDirectory, originalFileName);
        String pdPath = saveFileForGeneratedLetterAttach(uploadDirectory, file, true, nocName, agencynameFromApplicationtable, applicationNewId.toString(), pdSubDirectory, originalFileName);


        // Add the new document at the last row position
        DocumentStatus newDocumentStatus = new DocumentStatus();
        newDocumentStatus.setApplication(application); // Set the application reference
        newDocumentStatus.setDescription(description);
        newDocumentStatus.setAssociationId(associationId);
        newDocumentStatus.setDataField(cdPath);
        currentDocumentRepo.save(newDocumentStatus);


        // Log the same in LogsOfDocument
        LogsOfDocument newLogsOfDocument = new LogsOfDocument();
        newLogsOfDocument.setApplication(application); // Set the application reference
        newLogsOfDocument.setDocumentStatus(newDocumentStatus);
        newLogsOfDocument.setDescription(description);
        newLogsOfDocument.setAssociationId(associationId);
        newLogsOfDocument.setDataField(pdPath);
        logsOfDocumentRepo.save(newLogsOfDocument);


        return "Document data saved successfully";
    }

    @Override
    public List<Map<String, Object>> getApplicationDataByFinYearAndStatus(String financialYear, String status) {

        // Extract the start and end years from the "yyyy-yyyy" format
        String[] years = financialYear.split("-");
        if (years.length != 2) {
            throw new IllegalArgumentException("Invalid financial year format");
        }
        int startYear = Integer.parseInt(years[0]);
        int endYear = Integer.parseInt(years[1]);

        // Create start and end dates for the financial year (April 1 to March 31)
        Calendar startCal = Calendar.getInstance();
        startCal.set(startYear, Calendar.APRIL, 1, 0, 0, 0); // April 1st
        startCal.set(Calendar.MILLISECOND, 0);
        Date startDate = new Date(startCal.getTimeInMillis());

        Calendar endCal = Calendar.getInstance();
        endCal.set(endYear, Calendar.MARCH, 31, 23, 59, 59); // March 31st
        endCal.set(Calendar.MILLISECOND, 999);
        Date endDate = new Date(endCal.getTimeInMillis());

        return applicationRepository.findAllApplicationDataWhereStatus(status, startDate, endDate);

    }

    @Override
    public byte[] fetchCombinePdfOfCurrentDocumentById(Long id) throws IOException {
        // Base directory where PDFs are stored
        String baseDirectory = uploadDirectory;

        // Get the relative PDF path from the document
        String relativePdfPath = applicationRepository.findCombinePathByCurrentDocumentId(id);

        // Create the full path by appending the relative path to the base directory
        Path fullPath = Paths.get(baseDirectory, relativePdfPath);

        // Check if the file exists before trying to read it
        if (!Files.exists(fullPath)) {
            throw new FileNotFoundException("PDF file not found at path: " + fullPath.toString());
        }

        // Read the PDF file into a byte array
        byte[] pdfData = Files.readAllBytes(fullPath);

        System.out.println("pdfData" + pdfData);
        return pdfData;
    }

    @Override
    public NocList findIndexValueByNocTyp(String value) {
        NocList nocListData = nocListRepo.findByNocName(value);
        return nocListData;
    }

    @Override
    public List<String> fetchSubAdmin() {

        List<String> subAdminList = usersRepository.finAllSubAdmin();

        return subAdminList;

    }

    @Override
    public List<Map<String, Object>> findListOfCE2ForInternalDepartmentStats() {
//        fetch data for Ce2 list
        return logsOfDocumentRepo.fetchInteralDepartmentStatsForCe2();
    }

    @Override
    public List<Map<String, Object>> findListOfSeForInternalDepartmentStats() {
        return logsOfDocumentRepo.fetchInternalDepartmentStatsForSE();
    }

    @Override
    public List<Map<String, Object>> findListOfeeForInternalDepartmentStats() {
        return logsOfDocumentRepo.fetchInternalDepartmentStatsForEE();
    }

    @Override
    public List<Map<String, Object>> findListOfCEIforOfficerWiseReport() {
        return applicationRepository.fetchAllOfficerWiseReportForCE1();
    }

    @Override
    public List<Map<String, Object>> findListOfCEIIforOfficerWiseReport() {
        return applicationRepository.fetchAllOfficerReportForCeII();
    }

    @Override
    public List<Map<String, Object>> findListOfSeforOfficerWiseReport() {
        return applicationRepository.fetchOfficerReportForSE();
    }

    @Override
    public List<Map<String, Object>> findListOfExecutiveEngineerforOfficerWiseReport() {
        return applicationRepository.fetchOfficerReportForExecutiveEngineer();
    }

    @Override
    public void saveAttachedLetters(MultipartFile file, Long applicationId, String description, String nocType) throws IOException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        MultipartFile multipartFile = file;

        if (multipartFile != null && !multipartFile.isEmpty()) {


            // Define the new file name format as "app_applicationId_letter.pdf"
            String fileName = "app_" + applicationId + "_AttachedLetterBy_" + username + ".pdf";

            // Base directory path (absolute path) where files will be saved
            String absoluteBasePath = uploadDirectory + "NOC/ListOfLetters/" + nocType + "/app_" + applicationId + "/AttachedLetter";

            // Complete absolute path where the file will be saved
            String absoluteFilePath = absoluteBasePath + "/" + fileName;

            //  Relative path to store in the database
            String relativeFilePath = "NOC/ListOfLetters/" + nocType + "/app_" + applicationId + "/AttachedLetter/" + fileName;

            // Ensure the "NOC" directory exists
            Path nocDirectoryPath = Path.of(uploadDirectory + "NOC");
            if (!Files.exists(nocDirectoryPath)) {
                Files.createDirectories(nocDirectoryPath);
            }

            // Ensure the "nocType" directory exists
            Path nocTypeDirectoryPath = Path.of(uploadDirectory + "NOC/ListOfLetters/" + nocType);
            if (!Files.exists(nocTypeDirectoryPath)) {
                Files.createDirectories(nocTypeDirectoryPath);
            }

            // Ensure the "app_applicationId" directory exists
            Path applicationDirectoryPath = Path.of(absoluteBasePath);
            if (!Files.exists(applicationDirectoryPath)) {
                Files.createDirectories(applicationDirectoryPath);
            }

            // Check if the file already exists, and if so, delete it
            File existingFile = new File(absoluteFilePath);
            if (existingFile.exists()) {
                existingFile.delete();
            }
            // Save the file to the absolute path
            multipartFile.transferTo(new File(absoluteFilePath));

            // Get the application object
            Application application = applicationRepository.findById(applicationId).get();

            // Create and set details for ListOfLetters entity
            ListOfLetters listOfLetters = new ListOfLetters();
            listOfLetters.setDocPath(relativeFilePath);  // Saving relative path in the database
            listOfLetters.setIssuedBy(username);
            listOfLetters.setDescription(description);
            listOfLetters.setApplication(application);

            // Save the ListOfLetters entity
            listOfLettersRepository.save(listOfLetters);

            String absoluteLetterPdfPath = uploadDirectory + relativeFilePath;
            String oldCombinedpdfPath = uploadDirectory + application.getPdfPath();

            System.out.println("absoluteLetterPdfPath" + absoluteLetterPdfPath);
            System.out.println("oldCombinedpdfPath" + oldCombinedpdfPath);

            // Combine the new PDF with the existing combined PDF
            combinePdfWithExisting(applicationId, absoluteLetterPdfPath, oldCombinedpdfPath, application.getAppliedFor(), application.getCreatedBy());
        }
    }

    @Override
    public List<Map<String, Object>> findListOfCEIIforOfficerWiseReportForParticularRoleCe1(List<String> listOfCe2) {
        return applicationRepository.fetchCE2forParticularUsername(listOfCe2);
    }

    @Override
    public List<String> fetchListOfSeByCE1(String username) {
        return usersRepository.fetchSEbyCE1Name(username);
    }

    @Override
    public List<Map<String, Object>> findListOfSeforOfficerWiseReportForParticularRoleCe1(List<String> listOfSeByCE1) {
        return applicationRepository.fetchListOfSeByCe1Name(listOfSeByCE1);
    }

    @Override
    public List<String> fetchListOfEEByCE1(String username) {
        return usersRepository.fetchEEbyCE1Name(username);
    }

    @Override
    public List<Map<String, Object>> findListOfExecutiveEngineerforOfficerWiseReportForParticularRoleCe1(List<String> listOfEEByCE1) {
        return applicationRepository.fetchListOfEEByCe1Name(listOfEEByCE1);
    }

    @Override
    public List<String> fetchListOfEEByCE2(String username) {
        return usersRepository.fetchEEbyCE2Name(username);
    }

    @Override
    public List<Map<String, Object>> findListOfCE1ForInternalDepartmentStats() {
        return logsOfDocumentRepo.fetchInteralDepartmentStatsForCe1();
    }

    @Override
    public List<Map<String, Object>> fetchDataWhenCe1StatusApplied(String status, String ce1, String nocName) {
        return applicationRepository.fetchDataForCe1StatusNocName(status, ce1, nocName);
    }

    @Override
    public List<Map<String, Object>> fetchDataWhenCe2StatusApplied(String ce2, String status, String nocName) {
        return applicationRepository.fetchDataForCe2StatusNocName(status, ce2, nocName);
    }

    @Override
    public List<Map<String, Object>> fetchDataWhenSEStatusApplied(String se, String status, String nocName) {
        return applicationRepository.fetchDataForSEStatusNOCName(se, status, nocName);
    }

    @Override
    public List<Map<String, Object>> fetchDataWhenEEStatusApplied(String ee, String status, String nocName) {
        return applicationRepository.fetchDataForEEStatusNOCName(ee, status, nocName);
    }

    @Override
    public List<Map<String, Object>> fetchDataForCe1AndNOCName(String ce1, String nocName) {
        return applicationRepository.fetchDataForCE1AndNocName(ce1, nocName);
    }

    @Override
    public List<Map<String, Object>> fetchDataForCe2AndNOCName(String ce2, String nocName) {
        return applicationRepository.fetchDataForCE2AndNocName(ce2, nocName);
    }

    @Override
    public List<Map<String, Object>> fetchDataForSEAndNOCName(String se, String nocName) {
        return applicationRepository.fetchDataForSEAndNocName(se, nocName);
    }

    @Override
    public List<Map<String, Object>> fetchDataForEEAndNOCName(String ee, String nocName) {
        return applicationRepository.fetchDataForEEAndNocName(ee, nocName);
    }

    @Override
    public List<Map<String, Object>> fetchDataByCE1andStatus(String ce1, String status) {
        return applicationRepository.fetchDataForCe1andStatus(ce1, status);
    }

    @Override
    public List<Map<String, Object>> fetchDataByCE2andStatus(String ce2, String status) {
        return applicationRepository.fetchDataForCe2andStatus(ce2, status);
    }

    @Override
    public List<Map<String, Object>> fetchDataBySEandStatus(String se, String status) {
        return applicationRepository.fetchDataForSEandStatus(se, status);
    }

    @Override
    public List<Map<String, Object>> fetchDataByEEandStatus(String ee, String status) {
        return applicationRepository.fetchDataForEEandStatus(ee, status);
    }

    @Override
    public List<Map<String, Object>> fetchDataByByCE1Name(String ce1) {
        return applicationRepository.fetchDataForTotalOFCe1(ce1);
    }

    @Override
    public List<Map<String, Object>> fetchDataByByCE2Name(String ce2) {
        return applicationRepository.fetchDataForTotalOFCe2(ce2);
    }

    @Override
    public List<Map<String, Object>> fetchDataByBySeName(String se) {
        return applicationRepository.fetchDataForTotalOFSE(se);
    }

    @Override
    public List<Map<String, Object>> fetchDataByByEEName(String ee) {
        return applicationRepository.fetchDataForTotalOFEE(ee);
    }

    @Override
    public byte[] fetchPdfOfCurrentCertificateByAppId(String id) throws IOException {
        // Base directory where PDFs are stored
        String baseDirectory = uploadDirectory;
        // Fetch the relative PDF path from the repository
        String relativePdfPath = applicationRepository.findCertificatePathBySpecialId(id);

        // Create the full path by appending the relative path to the base directory
        Path fullPath = Paths.get(baseDirectory, relativePdfPath);

        // Check if the file exists before trying to read it
        if (!Files.exists(fullPath)) {
            throw new FileNotFoundException("PDF file not found at path: " + fullPath.toString());
        }

        // Read the PDF file into a byte array
        byte[] pdfData = Files.readAllBytes(fullPath);

        return pdfData;
    }

    @Override
    public String fetchUserIdFromUserSpring(String username) {
        String usersId = usersRepository.findByUserName(username);
        return usersId;
    }

    @Override
    public String fetchUserIdFromUserRepo(String username) {
        return userDetailsRepository.findByUserSpringId(username);
    }

    @Override
    public Long getNoAction7DaysApplicationByFinYear(String status, String financialYear) {
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().toArray()[0].toString();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Extract the start and end years from the "yyyy-yyyy" format
        String[] years = financialYear.split("-");
        if (years.length != 2) {
            throw new IllegalArgumentException("Invalid financial year format");
        }
        int startYear = Integer.parseInt(years[0]);
        int endYear = Integer.parseInt(years[1]);

        // Create start and end dates for the financial year (April 1 to March 31)
        Calendar startCal = Calendar.getInstance();
        startCal.set(startYear, Calendar.APRIL, 1, 0, 0, 0); // April 1st
        startCal.set(Calendar.MILLISECOND, 0);
        Date startDate = new Date(startCal.getTimeInMillis());

        Calendar endCal = Calendar.getInstance();
        endCal.set(endYear, Calendar.MARCH, 31, 23, 59, 59); // March 31st
        endCal.set(Calendar.MILLISECOND, 999);
        Date endDate = new Date(endCal.getTimeInMillis());

        // Calculate the date 15 days before the current date
        Calendar thirtyDaysBack = Calendar.getInstance();
        thirtyDaysBack.add(Calendar.DAY_OF_MONTH, -7);
        Date sevenDaysAgo = new Date(thirtyDaysBack.getTimeInMillis());

        if (role.equalsIgnoreCase("admin")) {

            return applicationRepository.getNoAction7DaysApplicationByFinYear(status, startDate, endDate, sevenDaysAgo);

        }
//        else if (!role.equalsIgnoreCase("admin")) {
//
//            return applicationRepository.getNoAction7DaysApplicationByFinYear(username, status, startDate, endDate, sevenDaysAgo);
//
//        }

        return null;
    }

    @Override
    public boolean usernameExistsOrNot(String username) {

        boolean exists = usersRepository.existsByUsername(username);

        return exists;
    }


    @Override
    public boolean checkApplicationStatusApproved(Long applicationId) {

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // Retrieve the transfer status
        String transferStatus = application.getTransferStatus();

        // Return true if the status is "APPROVAL"
        return transferStatus.equalsIgnoreCase("APPROVAL");

    }

    @Override
    public boolean checkApplicationStatusCrossVerified(Long applicationId) {

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        // Retrieve the transfer status
        String transferStatus = application.getTransferStatus();

        // Return true if the status is "CROSS VERIFIED"
        return transferStatus.equalsIgnoreCase("CROSS_VERIFIED");

    }

    @Override
    public String fetchCe1NameWhereAppId(String appId) {

        String ce1NameFromApplication = applicationRepository.fetchCe1NameWhereAppId(appId);

        return ce1NameFromApplication;

    }

    @Override
    public List<Map<String, Object>> fetchApplicationDetailsBySpecialId(String specialId) {

        return applicationRepository.fetchApplicationDetailsBySpecialId(specialId);

    }

    @Override
    public void saveIssuedCertificate(String specialId, MultipartFile file, String nocName, String agencyName) throws IOException {

        Application application = applicationRepository.fetchApplicationBySpecialId(specialId);

        application.setTransferStatus("ISSUED");

        MovementStatus movementStatus = appCurrentStatusRepository.findByApplicationId(application.getId());

        movementStatus.setStatus("ISSUED");

        appCurrentStatusRepository.save(movementStatus);

        applicationRepository.save(application);

        MultipartFile multipartFile = file;

        if (multipartFile != null && !multipartFile.isEmpty()) {

            String applicationId = String.valueOf(application.getId());
            // Define the new file name format as "app_applicationId_letter.pdf"
            String fileName = "app_" + applicationId + "_Certificate_" + agencyName + ".pdf";

            // Base directory path (absolute path) where files will be saved
            String absoluteBasePath = uploadDirectory + "NOC/Certificates/" + nocName + "/app_" + applicationId;

            // Complete absolute path where the file will be saved
            String absoluteFilePath = absoluteBasePath + "/" + fileName;

            //  Relative path to store in the database
            String relativeFilePath = "NOC/Certificates/" + nocName + "/app_" + applicationId + "/" + fileName;

            // Ensure the "NOC" directory exists
            Path nocDirectoryPath = Path.of(uploadDirectory + "NOC");
            if (!Files.exists(nocDirectoryPath)) {
                Files.createDirectories(nocDirectoryPath);
            }

            // Ensure the "nocType" directory exists
            Path nocTypeDirectoryPath = Path.of(uploadDirectory + "NOC/Certificates/" + nocName);
            if (!Files.exists(nocTypeDirectoryPath)) {
                Files.createDirectories(nocTypeDirectoryPath);
            }

            // Ensure the "app_applicationId" directory exists
            Path applicationDirectoryPath = Path.of(absoluteBasePath);
            if (!Files.exists(applicationDirectoryPath)) {
                Files.createDirectories(applicationDirectoryPath);
            }

            // Check if the file already exists, and if so, delete it
            File existingFile = new File(absoluteFilePath);
            if (existingFile.exists()) {
                existingFile.delete();
            }
            // Save the file to the absolute path
            multipartFile.transferTo(new File(absoluteFilePath));


            Certificate certificate = new Certificate();

            certificate.setPdfPath(relativeFilePath);
            certificate.setSpecialId(specialId);

            certificateRepository.save(certificate);

            String agencynameFromApplicationtable = applicationRepository.findByAppId(application.getId());


            String applicationNewId = "app_" + applicationId;

            String originalFileName = file.getOriginalFilename();
            // Define subdirectories for CD and PD
            String cdSubDirectory = "CurrDoc";
            String pdSubDirectory = "PrevDoc";
            String description = "Certificate Issued to" + agencynameFromApplicationtable + "for NOC name" + nocName;
            String certificateRelativePath = "";
            certificateRelativePath = uploadDirectory + relativeFilePath;

            Application newApplication = applicationRepository.findById(application.getId()).get();
            // Save the file using its original name in the CD and PD directories
            String cdPath = saveFileForCertificate(uploadDirectory, file, true, nocName, agencynameFromApplicationtable, applicationNewId.toString(), cdSubDirectory, originalFileName, certificateRelativePath);
            String pdPath = saveFileForCertificate(uploadDirectory, file, true, nocName, agencynameFromApplicationtable, applicationNewId.toString(), pdSubDirectory, originalFileName, certificateRelativePath);

            // Add the new document at the last row position
            DocumentStatus newDocumentStatus = new DocumentStatus();
            newDocumentStatus.setApplication(newApplication); // Set the application reference
            newDocumentStatus.setDescription(description);
            newDocumentStatus.setDataField(cdPath);
//        newDocumentStatus.setCurrStatus(currStatus);
            currentDocumentRepo.save(newDocumentStatus);


            // Log the same in LogsOfDocument
            LogsOfDocument newLogsOfDocument = new LogsOfDocument();
            newLogsOfDocument.setApplication(newApplication); // Set the application reference
            newLogsOfDocument.setDocumentStatus(newDocumentStatus);
            newLogsOfDocument.setDescription(description);
            newLogsOfDocument.setDataField(pdPath);
//        newLogsOfDocument.setCurrStatus(currStatus);
            logsOfDocumentRepo.save(newLogsOfDocument);

            // Paths for combining PDFs
            String absoluteLetterPdfPath = uploadDirectory + relativeFilePath;
            String oldCombinedpdfPath = uploadDirectory + application.getPdfPath();

            System.out.println("absoluteLetterPdfPath: " + absoluteLetterPdfPath);
            System.out.println("oldCombinedpdfPath: " + oldCombinedpdfPath);

            // Combine the new PDF with the existing combined PDF
            combinePdfWithExisting(application.getId(), absoluteLetterPdfPath, oldCombinedpdfPath, nocName, application.getCreatedBy());


        }


    }


    private String saveFileForCertificate(String uploadDir, MultipartFile file, boolean replaceExisting, String nocName, String agencyName, String applicationId, String subDirectory, String originalFileName, String certificateRelativePath) {

        try {

            String fileName = applicationId + "_" + originalFileName + "_1" + ".pdf";
            // Build the relative path using NOC name, agency name, application ID, and subdirectory
            String relativePath = Paths.get("NOC", nocName, agencyName, applicationId, subDirectory, fileName).toString().replace("\\", "/");

            // Build the full path for storage
            Path filePath = Paths.get(uploadDir, relativePath);

            // Create the NOC directory and subdirectories if they don't exist
            Files.createDirectories(filePath.getParent());


            // Move the temporary file to the final destination
            Files.copy(Path.of(certificateRelativePath), filePath, replaceExisting ?
                    StandardCopyOption.REPLACE_EXISTING : StandardCopyOption.COPY_ATTRIBUTES);


            // Return the relative path for saving in the database
            return relativePath;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public byte[] fetchCertificateBySpecialId(String id) throws IOException {

        // Fetch the relative PDF path from the repository
        String relativePdfPath = uploadDirectory + applicationRepository.findCertificatePathBySpecialId(id);

        // Check if the relative path is null or empty
        if (relativePdfPath == null || relativePdfPath.isEmpty()) {
            throw new FileNotFoundException("No PDF path found for the given special ID: " + id);
        }

        // Ensure the file exists before attempting to read it
        if (!Files.exists(Path.of(relativePdfPath)) || !Files.isRegularFile(Path.of(relativePdfPath))) {
            throw new FileNotFoundException("PDF file not found at path: " + relativePdfPath.toString());
        }

        // Read the PDF file as a byte array
        return Files.readAllBytes(Path.of(relativePdfPath));
    }


    @Override
    public List<Map<String, Object>> fetchApplicationCountsForClickSE(List<String> listOfSe, String
            financialYear) {

// Extract the start and end years from the "yyyy-yyyy" format
        String[] years = financialYear.split("-");

        if (years.length != 2) {
            throw new IllegalArgumentException("Invalid financial year format");
        }

        int startYear = Integer.parseInt(years[0]);
        int endYear = Integer.parseInt(years[1]);

// Create start and end dates for the financial year (April 1 to March 31)
        Calendar startCal = Calendar.getInstance();
        startCal.set(startYear, Calendar.APRIL, 1, 0, 0, 0); // April 1st
        startCal.set(Calendar.MILLISECOND, 0);
        Date startDate = new Date(startCal.getTimeInMillis());

        Calendar endCal = Calendar.getInstance();
        endCal.set(endYear, Calendar.MARCH, 31, 23, 59, 59); // March 31st
        endCal.set(Calendar.MILLISECOND, 999);
        Date endDate = new Date(endCal.getTimeInMillis());

        // Fetch the application counts from the repository
        List<Map<String, Object>> queryResult = applicationRepository.getApplicationCountsBySEUsers(listOfSe, startDate, endDate);

        // Initialize a map with all CE1 users and zero counts
        Map<String, Map<String, Long>> resultMap = new HashMap<>();

        for (String ce1User : listOfSe) {
            Map<String, Long> countsMap = new HashMap<>();
            countsMap.put("totalApplications", 0L);
            countsMap.put("pendingApplications", 0L);
            countsMap.put("inProgressApplications", 0L);
            countsMap.put("approvedApplications", 0L);
            countsMap.put("rejectedApplications", 0L);
            resultMap.put(ce1User, countsMap);
        }

        // Overwrite default values with actual data if available
        for (Map<String, Object> row : queryResult) {
            String toUser = (String) row.get("toUser");
            if (resultMap.containsKey(toUser)) {
                Map<String, Long> countsMap = resultMap.get(toUser);
                countsMap.put("totalApplications", ((Number) row.get("totalApplications")).longValue());
                countsMap.put("pendingApplications", ((Number) row.get("pendingApplications")).longValue());
                countsMap.put("inProgressApplications", ((Number) row.get("inProgressApplications")).longValue());
                countsMap.put("approvedApplications", ((Number) row.get("approvedApplications")).longValue());
                countsMap.put("rejectedApplications", ((Number) row.get("rejectedApplications")).longValue());
            }
        }

        // Convert resultMap to List<Map<String, Object>> for response
        List<Map<String, Object>> resultWithDefaults = new ArrayList<>();
        for (Map.Entry<String, Map<String, Long>> entry : resultMap.entrySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("toUser", entry.getKey());
            map.putAll(entry.getValue());
            resultWithDefaults.add(map);
        }

        return resultWithDefaults;
    }

    @Override
    public List<Map<String, Object>> fetchTotalApplicationCountForGraphforAdmin(String financialYear) {

        String[] years = financialYear.split("-");

        if (years.length != 2) {
            throw new IllegalArgumentException("Invalid financial year format");
        }

        int startYear = Integer.parseInt(years[0]);
        int endYear = Integer.parseInt(years[1]);

        // Create start and end dates for the financial year (April 1 to March 31)
        Calendar startCal = Calendar.getInstance();
        startCal.set(startYear, Calendar.APRIL, 1, 0, 0, 0); // April 1st
        startCal.set(Calendar.MILLISECOND, 0);
        Date startDate = new Date(startCal.getTimeInMillis());

        Calendar endCal = Calendar.getInstance();
        endCal.set(endYear, Calendar.MARCH, 31, 23, 59, 59); // March 31st
        endCal.set(Calendar.MILLISECOND, 999);
        Date endDate = new Date(endCal.getTimeInMillis());
        return applicationRepository.fetchTheTotalCountOfGraphAdmin(startDate, endDate);
    }

    @Override
    public List<Application> getAgencyApplicationsStatus(Long applicationId) {

        List<Application> appStatus = applicationRepository.findStatusById(applicationId);
        return appStatus;
    }

    @Override
    public List<ListOfLetters> fetchAllAllLettersFromAppCurrid(String id) {

        List<ListOfLetters> fetchedDataList = listOfLettersRepository.findByApplicationId(id);
        return fetchedDataList;
    }

    @Override
    public byte[] fetchPdfOfLettersById(Long id) throws IOException {
        // Base directory where PDFs are stored
//        String baseDirectory = uploadDirectory;

//        // Get the relative PDF path from the document
//        String relativePdfPath = listOfLettersRepository.findPathByAppId(id);
//
//        // Create the full path by appending the relative path to the base directory
//        Path fullPath = Paths.get(relativePdfPath);
//
//        // Check if the file exists before trying to read it
//        if (!Files.exists(fullPath)) {
//            throw new FileNotFoundException("PDF file not found at path: " + fullPath.toString());
//        }
//
//        // Read the PDF file into a byte array
//        byte[] pdfData = Files.readAllBytes(fullPath);
//
//        return pdfData;


        // Base directory where PDFs are stored
        String baseDirectory = uploadDirectory;

        // Get the relative PDF path from the document
        String relativePdfPath = listOfLettersRepository.findPathByAppId(id);

        // Create the full path by appending the relative path to the base directory
        Path fullPath = Paths.get(baseDirectory, relativePdfPath);

        // Check if the file exists before trying to read it
        if (!Files.exists(fullPath)) {
            throw new FileNotFoundException("PDF file not found at path: " + fullPath.toString());
        }

        // Read the PDF file into a byte array
        byte[] pdfData = Files.readAllBytes(fullPath);

        System.out.println("pdfData" + pdfData);
        return pdfData;


    }

    @Override
    public List<Application> fetchAllDataOfAppUniqueId(String id) {
        return applicationRepository.findByUniqueId(id);
    }

    @Override
    public List<MovementStatus> fetchAllDataOfCurrAppId(String id) {
        return appCurrentStatusRepository.findAppCurrIdByAppplicationId(id);
    }

    @Override
    public List<Map<String, Object>> fetchApplicationCountsForCe1Users(List<String> listOfCe1, String
            financialYear) {

        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().toArray()[0].toString();

        if (role.equalsIgnoreCase("admin")) {

            // Extract the start and end years from the "yyyy-yyyy" format
            String[] years = financialYear.split("-");

            if (years.length != 2) {
                throw new IllegalArgumentException("Invalid financial year format");
            }

            int startYear = Integer.parseInt(years[0]);
            int endYear = Integer.parseInt(years[1]);

            // Create start and end dates for the financial year (April 1 to March 31)
            Calendar startCal = Calendar.getInstance();
            startCal.set(startYear, Calendar.APRIL, 1, 0, 0, 0); // April 1st
            startCal.set(Calendar.MILLISECOND, 0);
            Date startDate = new Date(startCal.getTimeInMillis());

            Calendar endCal = Calendar.getInstance();
            endCal.set(endYear, Calendar.MARCH, 31, 23, 59, 59); // March 31st
            endCal.set(Calendar.MILLISECOND, 999);
            Date endDate = new Date(endCal.getTimeInMillis());

            // Fetch the application counts from the repository getApplicationCountsByCe1Users  getApplicationCountsByCe2Dashboard
            List<Map<String, Object>> queryResult = applicationRepository.getApplicationCountsByCe1Users(listOfCe1, startDate, endDate);

            // Initialize a map with all CE1 users and zero counts
            List<Map<String, Object>> resultWithDefaults = new ArrayList<>();

            for (String ce1User : listOfCe1) {
                Map<String, Object> countsMap = new HashMap<>();
                countsMap.put("toUser", ce1User);
                countsMap.put("totalApplications", 0);
                countsMap.put("pendingApplications", 0);
                countsMap.put("inProgressApplications", 0);
                countsMap.put("approvedApplications", 0);
                countsMap.put("rejectedApplications", 0);
                resultWithDefaults.add(countsMap);
            }

            // Overwrite default values with actual data if available
            for (Map<String, Object> row : queryResult) {
                String toUser = (String) row.get("toUser");

                for (Map<String, Object> defaultRow : resultWithDefaults) {
                    if (defaultRow.get("toUser").equals(toUser)) {
                        defaultRow.put("totalApplications", ((Number) row.get("totalApplications")).longValue());
                        defaultRow.put("pendingApplications", ((Number) row.get("pendingApplications")).longValue());
                        defaultRow.put("inProgressApplications", ((Number) row.get("inProgressApplications")).longValue());
                        defaultRow.put("approvedApplications", ((Number) row.get("approvedApplications")).longValue());
                        defaultRow.put("rejectedApplications", ((Number) row.get("rejectedApplications")).longValue());
                    }
                }
            }

            return resultWithDefaults;
        }

        if (role.equalsIgnoreCase("ce1")) {

            // Extract the start and end years from the "yyyy-yyyy" format
            String[] years = financialYear.split("-");

            if (years.length != 2) {
                throw new IllegalArgumentException("Invalid financial year format");
            }

            int startYear = Integer.parseInt(years[0]);
            int endYear = Integer.parseInt(years[1]);

            // Create start and end dates for the financial year (April 1 to March 31)
            Calendar startCal = Calendar.getInstance();
            startCal.set(startYear, Calendar.APRIL, 1, 0, 0, 0); // April 1st
            startCal.set(Calendar.MILLISECOND, 0);
            Date startDate = new Date(startCal.getTimeInMillis());

            Calendar endCal = Calendar.getInstance();
            endCal.set(endYear, Calendar.MARCH, 31, 23, 59, 59); // March 31st
            endCal.set(Calendar.MILLISECOND, 999);
            Date endDate = new Date(endCal.getTimeInMillis());

            // Fetch the application counts from the repository
            List<Map<String, Object>> queryResult = applicationRepository.getApplicationCountsByCe2Dashboard(listOfCe1, startDate, endDate);

            // Initialize a map with all CE1 users and zero counts
            List<Map<String, Object>> resultWithDefaults = new ArrayList<>();

            for (String ce1User : listOfCe1) {
                Map<String, Object> countsMap = new HashMap<>();
                countsMap.put("toUser", ce1User);
                countsMap.put("totalApplications", 0);
                countsMap.put("pendingApplications", 0);
                countsMap.put("inProgressApplications", 0);
                countsMap.put("approvedApplications", 0);
                countsMap.put("rejectedApplications", 0);
                resultWithDefaults.add(countsMap);
            }

            // Overwrite default values with actual data if available
            for (Map<String, Object> row : queryResult) {
                String toUser = (String) row.get("toUser");

                for (Map<String, Object> defaultRow : resultWithDefaults) {
                    if (defaultRow.get("toUser").equals(toUser)) {
                        defaultRow.put("totalApplications", ((Number) row.get("totalApplications")).longValue());
                        defaultRow.put("pendingApplications", ((Number) row.get("pendingApplications")).longValue());
                        defaultRow.put("inProgressApplications", ((Number) row.get("inProgressApplications")).longValue());
                        defaultRow.put("approvedApplications", ((Number) row.get("approvedApplications")).longValue());
                        defaultRow.put("rejectedApplications", ((Number) row.get("rejectedApplications")).longValue());
                    }
                }
            }

            return resultWithDefaults;
        }

        if (role.equalsIgnoreCase("ce2")) {

            // Extract the start and end years from the "yyyy-yyyy" format
            String[] years = financialYear.split("-");

            if (years.length != 2) {
                throw new IllegalArgumentException("Invalid financial year format");
            }

            int startYear = Integer.parseInt(years[0]);
            int endYear = Integer.parseInt(years[1]);

            // Create start and end dates for the financial year (April 1 to March 31)
            Calendar startCal = Calendar.getInstance();
            startCal.set(startYear, Calendar.APRIL, 1, 0, 0, 0); // April 1st
            startCal.set(Calendar.MILLISECOND, 0);
            Date startDate = new Date(startCal.getTimeInMillis());

            Calendar endCal = Calendar.getInstance();
            endCal.set(endYear, Calendar.MARCH, 31, 23, 59, 59); // March 31st
            endCal.set(Calendar.MILLISECOND, 999);
            Date endDate = new Date(endCal.getTimeInMillis());

            // Fetch the application counts from the repository
//            System.out.println("listOfCe1"+listOfCe1);
            List<Map<String, Object>> queryResult = applicationRepository.getApplicationCountsBySeDashboard(listOfCe1, startDate, endDate);

            // Initialize a map with all CE1 users and zero counts
            List<Map<String, Object>> resultWithDefaults = new ArrayList<>();

            for (String ce1User : listOfCe1) {
                Map<String, Object> countsMap = new HashMap<>();
                countsMap.put("toUser", ce1User);
                countsMap.put("totalApplications", 0);
                countsMap.put("pendingApplications", 0);
                countsMap.put("inProgressApplications", 0);
                countsMap.put("approvedApplications", 0);
                countsMap.put("rejectedApplications", 0);
                resultWithDefaults.add(countsMap);
            }

            // Overwrite default values with actual data if available
            for (Map<String, Object> row : queryResult) {
                String toUser = (String) row.get("toUser");

                for (Map<String, Object> defaultRow : resultWithDefaults) {
                    if (defaultRow.get("toUser").equals(toUser)) {
                        defaultRow.put("totalApplications", ((Number) row.get("totalApplications")).longValue());
                        defaultRow.put("pendingApplications", ((Number) row.get("pendingApplications")).longValue());
                        defaultRow.put("inProgressApplications", ((Number) row.get("inProgressApplications")).longValue());
                        defaultRow.put("approvedApplications", ((Number) row.get("approvedApplications")).longValue());
                        defaultRow.put("rejectedApplications", ((Number) row.get("rejectedApplications")).longValue());
                    }
                }
            }

            return resultWithDefaults;
        }

        if (role.equalsIgnoreCase("se")) {

            // Extract the start and end years from the "yyyy-yyyy" format
            String[] years = financialYear.split("-");

            if (years.length != 2) {
                throw new IllegalArgumentException("Invalid financial year format");
            }

            int startYear = Integer.parseInt(years[0]);
            int endYear = Integer.parseInt(years[1]);

            // Create start and end dates for the financial year (April 1 to March 31)
            Calendar startCal = Calendar.getInstance();
            startCal.set(startYear, Calendar.APRIL, 1, 0, 0, 0); // April 1st
            startCal.set(Calendar.MILLISECOND, 0);
            Date startDate = new Date(startCal.getTimeInMillis());

            Calendar endCal = Calendar.getInstance();
            endCal.set(endYear, Calendar.MARCH, 31, 23, 59, 59); // March 31st
            endCal.set(Calendar.MILLISECOND, 999);
            Date endDate = new Date(endCal.getTimeInMillis());

            // Fetch the application counts from the repository
//            EE List pass
            List<Map<String, Object>> queryResult = applicationRepository.getApplicationCountsByEEDashboard(listOfCe1, startDate, endDate);

            // Initialize a map with all CE1 users and zero counts
            List<Map<String, Object>> resultWithDefaults = new ArrayList<>();

            for (String ce1User : listOfCe1) {
                Map<String, Object> countsMap = new HashMap<>();
                countsMap.put("toUser", ce1User);
                countsMap.put("totalApplications", 0);
                countsMap.put("pendingApplications", 0);
                countsMap.put("inProgressApplications", 0);
                countsMap.put("approvedApplications", 0);
                countsMap.put("rejectedApplications", 0);
                resultWithDefaults.add(countsMap);
            }

            // Overwrite default values with actual data if available
            for (Map<String, Object> row : queryResult) {
                String toUser = (String) row.get("toUser");

                for (Map<String, Object> defaultRow : resultWithDefaults) {
                    if (defaultRow.get("toUser").equals(toUser)) {
                        defaultRow.put("totalApplications", ((Number) row.get("totalApplications")).longValue());
                        defaultRow.put("pendingApplications", ((Number) row.get("pendingApplications")).longValue());
                        defaultRow.put("inProgressApplications", ((Number) row.get("inProgressApplications")).longValue());
                        defaultRow.put("approvedApplications", ((Number) row.get("approvedApplications")).longValue());
                        defaultRow.put("rejectedApplications", ((Number) row.get("rejectedApplications")).longValue());
                    }
                }
            }

            return resultWithDefaults;
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> fetchApplicationCountsForClickCE2(List<String> listOfCe2, String finYear) {
// Extract the start and end years from the "yyyy-yyyy" format
        String[] years = finYear.split("-");

        if (years.length != 2) {
            throw new IllegalArgumentException("Invalid financial year format");
        }

        int startYear = Integer.parseInt(years[0]);
        int endYear = Integer.parseInt(years[1]);

// Create start and end dates for the financial year (April 1 to March 31)
        Calendar startCal = Calendar.getInstance();
        startCal.set(startYear, Calendar.APRIL, 1, 0, 0, 0); // April 1st
        startCal.set(Calendar.MILLISECOND, 0);
        Date startDate = new Date(startCal.getTimeInMillis());

        Calendar endCal = Calendar.getInstance();
        endCal.set(endYear, Calendar.MARCH, 31, 23, 59, 59); // March 31st
        endCal.set(Calendar.MILLISECOND, 999);
        Date endDate = new Date(endCal.getTimeInMillis());

        // Fetch the application counts from the repository
        List<Map<String, Object>> queryResult = applicationRepository.getApplicationCountsByCe2Users(listOfCe2, startDate, endDate);

        // Initialize a map with all CE1 users and zero counts
        Map<String, Map<String, Long>> resultMap = new HashMap<>();

        for (String ce1User : listOfCe2) {
            Map<String, Long> countsMap = new HashMap<>();
            countsMap.put("totalApplications", 0L);
            countsMap.put("pendingApplications", 0L);
            countsMap.put("inProgressApplications", 0L);
            countsMap.put("approvedApplications", 0L);
            countsMap.put("rejectedApplications", 0L);
            resultMap.put(ce1User, countsMap);
        }

        // Overwrite default values with actual data if available
        for (Map<String, Object> row : queryResult) {
            String toUser = (String) row.get("toUser");
            if (resultMap.containsKey(toUser)) {
                Map<String, Long> countsMap = resultMap.get(toUser);
                countsMap.put("totalApplications", ((Number) row.get("totalApplications")).longValue());
                countsMap.put("pendingApplications", ((Number) row.get("pendingApplications")).longValue());
                countsMap.put("inProgressApplications", ((Number) row.get("inProgressApplications")).longValue());
                countsMap.put("approvedApplications", ((Number) row.get("approvedApplications")).longValue());
                countsMap.put("rejectedApplications", ((Number) row.get("rejectedApplications")).longValue());
            }
        }

        // Convert resultMap to List<Map<String, Object>> for response
        List<Map<String, Object>> resultWithDefaults = new ArrayList<>();
        for (Map.Entry<String, Map<String, Long>> entry : resultMap.entrySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("toUser", entry.getKey());
            map.putAll(entry.getValue());
            resultWithDefaults.add(map);
        }

        return resultWithDefaults;
    }

    @Override
    public List<String> fetchListOfEeforSE(String se) {
        return usersRepository.fetchEEforSE(se);
    }

    @Override
    public List<Map<String, Object>> fetchTotalApplicationCountForGraph(String username, String financialYear) {

        String[] years = financialYear.split("-");

        if (years.length != 2) {
            throw new IllegalArgumentException("Invalid financial year format");
        }

        int startYear = Integer.parseInt(years[0]);
        int endYear = Integer.parseInt(years[1]);

        // Create start and end dates for the financial year (April 1 to March 31)
        Calendar startCal = Calendar.getInstance();
        startCal.set(startYear, Calendar.APRIL, 1, 0, 0, 0); // April 1st
        startCal.set(Calendar.MILLISECOND, 0);
        Date startDate = new Date(startCal.getTimeInMillis());

        Calendar endCal = Calendar.getInstance();
        endCal.set(endYear, Calendar.MARCH, 31, 23, 59, 59); // March 31st
        endCal.set(Calendar.MILLISECOND, 999);
        Date endDate = new Date(endCal.getTimeInMillis());
        return applicationRepository.fetchTheTotalCountOfGraph(username, startDate, endDate);


    }


    @Override
    public void transferApplicationToAgency(Long id) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        long timestampMillis = Instant.now().toEpochMilli();
        Date currentDate = new Date(System.currentTimeMillis());

        Application application = applicationRepository.findById(id).get();

        MovementStatus movementStatus = appCurrentStatusRepository.findByApplicationId(application.getId());

        movementStatus.setFromUser(username);

        movementStatus.setToUser(application.getCreatedBy());
        movementStatus.setDate(currentDate);
        movementStatus.setTimestamp(String.valueOf(timestampMillis));
        movementStatus.setReSubmission("true");

        appCurrentStatusRepository.save(movementStatus);

        LogsOfMovement logsOfMovement = new LogsOfMovement();
        logsOfMovement.setDate(currentDate);
        logsOfMovement.setTimestamp(String.valueOf(timestampMillis));
        logsOfMovement.setFromDepartmentUser(username);
        logsOfMovement.setToDepartmentUser(application.getCreatedBy());
        logsOfMovement.setApplication(application);
        logsOfMovement.setMovementStatus(movementStatus);

        logsOfFileRepository.save(logsOfMovement);

    }

    @Override
    public String getPresetSeByEeAndAppId(Long appId) {

        Application application = applicationRepository.findById(appId).get();

        String seName = application.getSe();

        return seName;

    }

    @Override
    public String presetCe2FromApplicationByAppId(Long appId) {

        Application application = applicationRepository.findById(appId).get();

        String ce2Name = application.getCe2();

        return ce2Name;

    }

    @Override
    public String presetCe1FromApplicationByAppId(Long appId) {

        Application application = applicationRepository.findById(appId).get();

        String ce1Name = application.getCe1();

        return ce1Name;
    }

    @Override
    public String presetAdminFromApplicationByAppId(Long appId) {

        Application application = applicationRepository.findById(appId).get();

        String adminName = application.getAdmin();

        return adminName;

    }

    @Override
    public List<String> fetchCe1ListOrCe1(Long appId) {
        // Fetch the application using the appId
        Application application = applicationRepository.findById(appId).get();

        // Initialize the list to return
        List<String> ce1List = new ArrayList<>();

        String ce1 = application.getCe1();

        // Check if ce1 is empty or null
        if (ce1 == null || ce1.isEmpty()) {

            ce1List = usersRepository.fetchAllCe1();
        } else {

            ce1List.add(ce1);
        }

        return ce1List;
    }


    @Override
    public List<String> fetchCe2ListOrCe2(String ce1, Long appId) {

        // Fetch the application using the appId
        Application application = applicationRepository.findById(appId).get();

        // Initialize the list to return
        List<String> ce2List = new ArrayList<>();

        String ce2 = application.getCe2();

        // Check if ce1 is empty or null
        if (ce2 == null || ce2.isEmpty()) {

            ce2List = usersRepository.fetchAllCe2(ce1);
        } else {

            ce2List.add(ce2);
        }

        return ce2List;
    }

    @Override
    public List<String> fetchSeListOrSe(String ce2, Long appId) {

        // Fetch the application using the appId
        Application application = applicationRepository.findById(appId).get();

        // Initialize the list to return
        List<String> seList = new ArrayList<>();

        String se = application.getSe();

        // Check if ce1 is empty or null
        if (se == null || se.isEmpty()) {

            seList = usersRepository.fetchAllSeWhereCe2(ce2);
        } else {

            seList.add(se);
        }

        return seList;

    }

    @Override
    public List<String> fetchEeListOrEe(String se, Long appId) {

        // Fetch the application using the appId
        Application application = applicationRepository.findById(appId).get();

        // Initialize the list to return
        List<String> eeList = new ArrayList<>();

        String ee = application.getEe();

        // Check if ce1 is empty or null
        if (ee == null || ee.isEmpty()) {

            eeList = usersRepository.fetchAllEeWhereSe(se);
        } else {

            eeList.add(ee);
        }

        return eeList;
    }

    @Override
    public byte[] fetchPdfOfCurrentDocumentById(Long id) throws IOException {
        // Base directory where PDFs are stored
        String baseDirectory = uploadDirectory;

        // Get the relative PDF path from the document
        String relativePdfPath = currentDocumentRepo.findPathByCurrentDocumentId(id);

        // Create the full path by appending the relative path to the base directory
        Path fullPath = Paths.get(baseDirectory, relativePdfPath);

        // Check if the file exists before trying to read it
        if (!Files.exists(fullPath)) {
            throw new FileNotFoundException("PDF file not found at path: " + fullPath.toString());
        }

        // Read the PDF file into a byte array
        byte[] pdfData = Files.readAllBytes(fullPath);

        return pdfData;
    }


    @Override
    public List<Map<String, Object>> getApplicationDataByUserAndByOwnership(String financialYear) {

        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().toArray()[0].toString();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Extract the start and end years from the "yyyy-yyyy" format
        String[] years = financialYear.split("-");
        if (years.length != 2) {
            throw new IllegalArgumentException("Invalid financial year format");
        }
        int startYear = Integer.parseInt(years[0]);
        int endYear = Integer.parseInt(years[1]);

        // Create start and end dates for the financial year (April 1 to March 31)
        Calendar startCal = Calendar.getInstance();
        startCal.set(startYear, Calendar.APRIL, 1, 0, 0, 0); // April 1st
        startCal.set(Calendar.MILLISECOND, 0);
        Date startDate = new Date(startCal.getTimeInMillis());

        Calendar endCal = Calendar.getInstance();
        endCal.set(endYear, Calendar.MARCH, 31, 23, 59, 59); // March 31st
        endCal.set(Calendar.MILLISECOND, 999);
        Date endDate = new Date(endCal.getTimeInMillis());

        if (role.equalsIgnoreCase("admin")) {

            return applicationRepository.findAllApplicationDataByUserAndByOwnership(username, startDate, endDate);

        } else if (!role.equalsIgnoreCase("admin")) {

            return applicationRepository.findAllApplicationDataByToUserAndByOwnership(username, startDate, endDate);
        }
//        else if (role.equalsIgnoreCase("ce2")) {
//
//            // Call repository method with start and end dates
//            return applicationRepository.countByApplicationStatusAndDateRangeWhereCe2(username, status, startDate, endDate);
//        } else if (role.equalsIgnoreCase("se")) {
//
//            // Call repository method with start and end dates
//            return applicationRepository.countByApplicationStatusAndDateRangeWhereSe(username, status, startDate, endDate);
//        } else if (role.equalsIgnoreCase("ee")) {
//
//            // Call repository method with start and end dates
//            return applicationRepository.countByApplicationStatusAndDateRangeWhereEe(username, status, startDate, endDate);
//        }

        return null;
    }

    @Override
    public void updateAppCurrentStatus(Long applicationId, String toUser, String transferRemark) {

        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().toArray()[0].toString();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        long timestampMillis = Instant.now().toEpochMilli();
        Date currentDate = new Date(System.currentTimeMillis());

        MovementStatus movementStatus = appCurrentStatusRepository.findByApplicationId(applicationId);

        movementStatus.setFromUser(username);
        movementStatus.setToUser(toUser);
        movementStatus.setDate(currentDate);
        movementStatus.setTimestamp(String.valueOf(timestampMillis));
        movementStatus.setStatus("IN_PROGRESS");

        Application application = applicationRepository.findById(applicationId).get();

//        Setting Transfer Status To VERIFICATION When EE Reviewed The Application

        if (role.equalsIgnoreCase("ee") && application.getEeStatus() != null && !application.getEeStatus().isEmpty()) {
            application.setTransferStatus("VERIFICATION");
        }

//        Logic To Check Application Transferred To Ee Or Not (If Transferred to Ee Level Then Dont Run This)
//        Initial Transfer Status is = FORWARDING (When User Applying For NOC)
//        if ("FORWARDING".equals(application.getTransferStatus())) {
//            if (role.equalsIgnoreCase("admin")) {
//                application.setCe1(toUser);
//            } else if (role.equalsIgnoreCase("ce1")) {
//                application.setCe2(toUser);
//            } else if (role.equalsIgnoreCase("ce2")) {
//                application.setSe(toUser);
//            } else if (role.equalsIgnoreCase("se")) {
//                application.setEe(toUser);
//                //Setting Transfer Status = VERIFICATION When File is Moved To Se To Ee
//                application.setTransferStatus("VERIFICATION");
//            }
//        }

//       When Admin Transferring the Application To Deputy Admin (And Status is VERIFICATION) then Run This
        if ("VERIFICATION".equals(application.getTransferStatus())) {
            if (role.equalsIgnoreCase("admin") && toUser.equalsIgnoreCase("deputyAdmin")) {
                application.setTransferStatus("CROSS_VERIFICATION");
            }
        }

        //       When Deputy Admin Transfer To Admin (And Status is CROSS_VERIFICATION) Then Run This
        if ("CROSS_VERIFICATION".equals(application.getTransferStatus())) {
            if (role.equalsIgnoreCase("deputyAdmin") && toUser.equalsIgnoreCase(application.getAdmin())) {
                application.setTransferStatus("CROSS_VERIFIED");
            }
        }

        //       When Admin Transfer To HOD (And Status is CROSS_VERIFIED) Then Run This
        if ("CROSS_VERIFIED".equals(application.getTransferStatus())) {
            if (role.equalsIgnoreCase("admin") && toUser.equalsIgnoreCase("hod")) {
                application.setTransferStatus("FOR_APPROVAL");
            }
        }

        //       When HOD Transfer To ADMIN (And Status is APPROVAL) Then Run This
        if ("FOR_APPROVAL".equals(application.getTransferStatus())) {
            if (role.equalsIgnoreCase("hod") && toUser.equalsIgnoreCase(application.getAdmin())) {
                application.setTransferStatus("APPROVAL");
                movementStatus.setStatus("APPROVED");
            }
        }

        //       When HOD Transfer To ADMIN (And Status is APPROVAL) Then Run This
        if ("APPROVAL".equals(application.getTransferStatus())) {
            if (role.equalsIgnoreCase("admin") && toUser.equalsIgnoreCase(application.getCe1())) {
                application.setTransferStatus("FOR_ISSUANCE");
            }
        }

        appCurrentStatusRepository.save(movementStatus);

        applicationRepository.save(application);

        LogsOfMovement logsOfMovement = new LogsOfMovement();

        logsOfMovement.setFromDepartmentUser(username);
        logsOfMovement.setToDepartmentUser(toUser);
        logsOfMovement.setDate(currentDate);
        logsOfMovement.setTimestamp(String.valueOf(timestampMillis));
        logsOfMovement.setTransferRemark(transferRemark);
        logsOfMovement.setTransferStatus(application.getTransferStatus());
        logsOfMovement.setMovementStatus(movementStatus);
        logsOfMovement.setApplication(application);

        logsOfFileRepository.save(logsOfMovement);

    }

    @Override
    public List<String> fetchListOfEe() {
        List<String> listOfEe = usersRepository.findAllEe();
        return listOfEe;
    }

    @Override
    public List<UsersSpring> fetchRespectiveSeCeOfEechListOfEe(String ee) {
        List<UsersSpring> fetchRespectiveSeCeOfEechListOfEe = usersRepository.findRespectiveSeCeByEe(ee);
        return fetchRespectiveSeCeOfEechListOfEe;
    }


}
