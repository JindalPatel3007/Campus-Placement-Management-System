import database.DatabaseManager;
import model.Application;
import model.Company;
import model.PlacementDrive;
import model.Student;
import service.EligibilityService;
import service.PlacementService;
import util.InputUtil;

import java.util.List;
import java.util.Map;

/**
 * Main application class providing a menu-driven console user interface.
 * Serves as the central controller for the Campus Placement Management System.
 */
public class Main {

    private static final PlacementService placementService = new PlacementService();
    private static final EligibilityService eligibilityService = new EligibilityService();

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("       CAMPUS PLACEMENT MANAGEMENT SYSTEM       ");
        System.out.println("=================================================");

        // Test database connection at startup
        if (!DatabaseManager.testConnection()) {
            System.err.println("[CRITICAL ERROR] Cannot connect to MySQL Database!");
            System.err.println("Please check database/DatabaseManager.java credentials and ensure MySQL is running.");
            return;
        }

        boolean exit = false;
        while (!exit) {
            displayMainMenu();
            int choice = InputUtil.readInt("Enter your choice: ", 0, 7);

            switch (choice) {
                case 1:
                    studentManagementMenu();
                    break;
                case 2:
                    companyManagementMenu();
                    break;
                case 3:
                    placementDriveMenu();
                    break;
                case 4:
                    applicationMenu();
                    break;
                case 5:
                    checkEligibilityMenu();
                    break;
                case 6:
                    selectionMenu();
                    break;
                case 7:
                    displayStatistics();
                    break;
                case 0:
                    exit = true;
                    System.out.println(" Thank you for using Campus Placement Management System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println(" ---------------- MAIN MENU ----------------");
        System.out.println("1. Student Management");
        System.out.println("2. Company Management");
        System.out.println("3. Placement Drive Management");
        System.out.println("4. Application Management");
        System.out.println("5. Eligibility Check");
        System.out.println("6. Selection Management");
        System.out.println("7. Placement Statistics");
        System.out.println("0. Exit");
        System.out.println("-------------------------------------------");
    }

    // -------------------------------------------------------------
    // 1. STUDENT MANAGEMENT SUBMENU
    // -------------------------------------------------------------
    private static void studentManagementMenu() {
        while (true) {
            System.out.println(" --- STUDENT MANAGEMENT ---");
            System.out.println("1. Add Student");
            System.out.println("2. Display All Students");
            System.out.println("3. Search Student by ID");
            System.out.println("4. Update Student Details");
            System.out.println("5. Delete Student");
            System.out.println("0. Back to Main Menu");

            int choice = InputUtil.readInt("Select Option: ", 0, 5);
            if (choice == 0) break;

            switch (choice) {
                case 1:
                    String name = InputUtil.readString("Enter Student Name: ");
                    String email = InputUtil.readString("Enter Email: ");
                    String phone = InputUtil.readString("Enter Phone Number: ");
                    String branch = InputUtil.readString("Enter Branch (e.g., Computer Engineering): ");
                    double cgpa = InputUtil.readDouble("Enter CGPA (0.0 to 10.0): ", 0.0, 10.0);
                    String skillsStr = InputUtil.readString("Enter Skills (comma-separated, e.g. Java, SQL): ");

                    Student newStudent = new Student(name, email, phone, branch, cgpa);
                    for (String s : skillsStr.split(",")) {
                        if (!s.trim().isEmpty()) {
                            newStudent.addSkill(s.trim());
                        }
                    }

                    if (placementService.registerStudent(newStudent)) {
                        System.out.println("SUCCESS: Student added successfully with ID: " + newStudent.getStudentId());
                    } else {
                        System.out.println("ERROR: Failed to add student. Email might already exist.");
                    }
                    break;

                case 2:
                    List<Student> students = placementService.getAllStudents();
                    if (students.isEmpty()) {
                        System.out.println("No students found.");
                    } else {
                        System.out.printf(" %-6s %-20s %-25s %-22s %-6s %-25s ", "ID", "Name", "Email", "Branch", "CGPA", "Skills");
                        System.out.println("---------------------------------------------------------------------------------------------------------");
                        for (Student s : students) {
                            System.out.printf("%-6d %-20s %-25s %-22s %-6.2f %-25s ",
                                    s.getStudentId(), s.getName(), s.getEmail(), s.getBranch(), s.getCgpa(), s.getSkillsAsString());
                        }
                    }
                    break;

                case 3:
                    int searchId = InputUtil.readInt("Enter Student ID: ", 1, Integer.MAX_VALUE);
                    Student found = placementService.getStudentById(searchId);
                    if (found != null) {
                        System.out.println(" Student Profile Found:");
                        System.out.println("ID: " + found.getStudentId());
                        System.out.println("Name: " + found.getName());
                        System.out.println("Email: " + found.getEmail());
                        System.out.println("Phone: " + found.getPhone());
                        System.out.println("Branch: " + found.getBranch());
                        System.out.println("CGPA: " + found.getCgpa());
                        System.out.println("Skills: " + found.getSkillsAsString());
                    } else {
                        System.out.println("Student not found with ID: " + searchId);
                    }
                    break;

                case 4:
                    int updateId = InputUtil.readInt("Enter Student ID to Update: ", 1, Integer.MAX_VALUE);
                    Student existing = placementService.getStudentById(updateId);
                    if (existing != null) {
                        System.out.println("Updating details for: " + existing.getName());
                        existing.setName(InputUtil.readString("Enter New Name [" + existing.getName() + "]: "));
                        existing.setPhone(InputUtil.readString("Enter New Phone [" + existing.getPhone() + "]: "));
                        existing.setBranch(InputUtil.readString("Enter New Branch [" + existing.getBranch() + "]: "));
                        existing.setCgpa(InputUtil.readDouble("Enter New CGPA [" + existing.getCgpa() + "]: ", 0.0, 10.0));

                        if (placementService.updateStudent(existing)) {
                            System.out.println("SUCCESS: Student updated successfully.");
                        } else {
                            System.out.println("ERROR: Failed to update student.");
                        }
                    } else {
                        System.out.println("Student ID not found.");
                    }
                    break;

                case 5:
                    int deleteId = InputUtil.readInt("Enter Student ID to Delete: ", 1, Integer.MAX_VALUE);
                    if (placementService.deleteStudent(deleteId)) {
                        System.out.println("SUCCESS: Student record deleted.");
                    } else {
                        System.out.println("ERROR: Could not delete student or ID not found.");
                    }
                    break;
            }
        }
    }

    // -------------------------------------------------------------
    // 2. COMPANY MANAGEMENT SUBMENU
    // -------------------------------------------------------------
    private static void companyManagementMenu() {
        while (true) {
            System.out.println(" --- COMPANY MANAGEMENT ---");
            System.out.println("1. Add Company");
            System.out.println("2. Display All Companies");
            System.out.println("3. Search Company");
            System.out.println("4. Delete Company");
            System.out.println("0. Back to Main Menu");

            int choice = InputUtil.readInt("Select Option: ", 0, 4);
            if (choice == 0) break;

            switch (choice) {
                case 1:
                    String cName = InputUtil.readString("Enter Company Name: ");
                    String loc = InputUtil.readString("Enter Location: ");
                    String hrName = InputUtil.readString("Enter HR Contact Name: ");
                    String hrEmail = InputUtil.readString("Enter HR Email: ");
                    String hrPhone = InputUtil.readString("Enter HR Phone: ");

                    Company comp = new Company(cName, loc, hrName, hrEmail, hrPhone);
                    if (placementService.registerCompany(comp)) {
                        System.out.println("SUCCESS: Company added with ID: " + comp.getCompanyId());
                    } else {
                        System.out.println("ERROR: Failed to add company. Name may be duplicate.");
                    }
                    break;

                case 2:
                    List<Company> companies = placementService.getAllCompanies();
                    if (companies.isEmpty()) {
                        System.out.println("No registered companies found.");
                    } else {
                        System.out.printf(" %-6s %-25s %-15s %-20s %-25s ", "ID", "Company Name", "Location", "HR Name", "HR Email");
                        System.out.println("---------------------------------------------------------------------------------------------");
                        for (Company c : companies) {
                            System.out.printf("%-6d %-25s %-15s %-20s %-25s ",
                                    c.getCompanyId(), c.getCompanyName(), c.getLocation(), c.getHrName(), c.getHrEmail());
                        }
                    }
                    break;

                case 3:
                    int cId = InputUtil.readInt("Enter Company ID: ", 1, Integer.MAX_VALUE);
                    Company c = placementService.getCompanyById(cId);
                    if (c != null) {
                        System.out.println(" Company Details:");
                        System.out.println("ID: " + c.getCompanyId());
                        System.out.println("Name: " + c.getCompanyName());
                        System.out.println("Location: " + c.getLocation());
                        System.out.println("HR Contact: " + c.getHrName() + " (" + c.getHrPhone() + " / " + c.getHrEmail() + ")");
                    } else {
                        System.out.println("Company not found.");
                    }
                    break;

                case 4:
                    int delCId = InputUtil.readInt("Enter Company ID to Delete: ", 1, Integer.MAX_VALUE);
                    if (placementService.deleteCompany(delCId)) {
                        System.out.println("SUCCESS: Company removed.");
                    } else {
                        System.out.println("ERROR: Failed to remove company.");
                    }
                    break;
            }
        }
    }

    // -------------------------------------------------------------
    // 3. PLACEMENT DRIVE MANAGEMENT SUBMENU
    // -------------------------------------------------------------
    private static void placementDriveMenu() {
        while (true) {
            System.out.println(" --- PLACEMENT DRIVE MANAGEMENT ---");
            System.out.println("1. Create Placement Drive");
            System.out.println("2. Display All Active Drives");
            System.out.println("3. Search Placement Drive");
            System.out.println("0. Back to Main Menu");

            int choice = InputUtil.readInt("Select Option: ", 0, 3);
            if (choice == 0) break;

            switch (choice) {
                case 1:
                    int compId = InputUtil.readInt("Enter Company ID: ", 1, Integer.MAX_VALUE);
                    if (placementService.getCompanyById(compId) == null) {
                        System.out.println("ERROR: Company ID does not exist!");
                        break;
                    }
                    String role = InputUtil.readString("Enter Job Role: ");
                    double minCgpa = InputUtil.readDouble("Enter Min CGPA Criteria: ", 0.0, 10.0);
                    String branch = InputUtil.readString("Eligible Branch (or 'All'): ");
                    String reqSkills = InputUtil.readString("Required Skills (comma-separated, e.g. Java, SQL): ");
                    double pkg = InputUtil.readDouble("Enter Package (LPA): ", 0.0, 200.0);
                    String dateStr = InputUtil.readString("Enter Drive Date (YYYY-MM-DD): ");

                    PlacementDrive drive = new PlacementDrive(compId, role, minCgpa, branch, reqSkills, pkg, dateStr);
                    if (placementService.createPlacementDrive(drive)) {
                        System.out.println("SUCCESS: Placement drive created successfully with ID: " + drive.getDriveId());
                    } else {
                        System.out.println("ERROR: Failed to create placement drive.");
                    }
                    break;

                case 2:
                    List<PlacementDrive> drives = placementService.getAllDrives();
                    if (drives.isEmpty()) {
                        System.out.println("No active placement drives.");
                    } else {
                        System.out.printf(" %-6s %-20s %-20s %-10s %-22s %-10s %-12s ",
                                "DriveID", "Company", "Job Role", "Min CGPA", "Eligible Branch", "Package", "Date");
                        System.out.println("----------------------------------------------------------------------------------------------------");
                        for (PlacementDrive d : drives) {
                            System.out.printf("%-6d %-20s %-20s %-10.2f %-22s %-10.2f %-12s ",
                                    d.getDriveId(), d.getCompanyName(), d.getJobRole(), d.getMinCgpa(),
                                    d.getEligibleBranch(), d.getPackageLpa(), d.getDriveDate());
                        }
                    }
                    break;

                case 3:
                    int dId = InputUtil.readInt("Enter Drive ID: ", 1, Integer.MAX_VALUE);
                    PlacementDrive d = placementService.getDriveById(dId);
                    if (d != null) {
                        System.out.println(" Drive Information:");
                        System.out.println("Drive ID: " + d.getDriveId());
                        System.out.println("Company: " + d.getCompanyName());
                        System.out.println("Role: " + d.getJobRole());
                        System.out.println("Min CGPA: " + d.getMinCgpa());
                        System.out.println("Branch Criteria: " + d.getEligibleBranch());
                        System.out.println("Required Skills: " + d.getRequiredSkills());
                        System.out.println("Package: " + d.getPackageLpa() + " LPA");
                        System.out.println("Date: " + d.getDriveDate());
                    } else {
                        System.out.println("Placement drive not found.");
                    }
                    break;
            }
        }
    }

    // -------------------------------------------------------------
    // 4. APPLICATION MANAGEMENT SUBMENU
    // -------------------------------------------------------------
    private static void applicationMenu() {
        while (true) {
            System.out.println(" --- APPLICATION MANAGEMENT ---");
            System.out.println("1. Apply for Placement Drive");
            System.out.println("2. View All Applications");
            System.out.println("3. View Applications by Student");
            System.out.println("0. Back to Main Menu");

            int choice = InputUtil.readInt("Select Option: ", 0, 3);
            if (choice == 0) break;

            switch (choice) {
                case 1:
                    int studentId = InputUtil.readInt("Enter Student ID: ", 1, Integer.MAX_VALUE);
                    int driveId = InputUtil.readInt("Enter Drive ID: ", 1, Integer.MAX_VALUE);

                    Student s = placementService.getStudentById(studentId);
                    PlacementDrive d = placementService.getDriveById(driveId);

                    if (s == null || d == null) {
                        System.out.println("ERROR: Invalid Student ID or Drive ID.");
                        break;
                    }

                    // Perform eligibility check prior to application submission
                    if (!eligibilityService.isEligible(s, d)) {
                        System.out.println(" [APPLICATION REJECTED] Student is NOT eligible for this placement drive.");
                        System.out.println("Reason: CGPA, Branch, or required skills criteria not met.");
                    } else {
                        System.out.println(" [ELIGIBILITY PASSED] Student meets all drive requirements.");
                        if (placementService.applyForDrive(studentId, driveId)) {
                            System.out.println("SUCCESS: Application submitted successfully!");
                        } else {
                            System.out.println("ERROR: Could not submit application. (Already applied?)");
                        }
                    }
                    break;

                case 2:
                    List<Application> apps = placementService.getAllApplications();
                    displayApplicationList(apps);
                    break;

                case 3:
                    int sId = InputUtil.readInt("Enter Student ID: ", 1, Integer.MAX_VALUE);
                    List<Application> studentApps = placementService.getApplicationsByStudent(sId);
                    displayApplicationList(studentApps);
                    break;
            }
        }
    }

    // -------------------------------------------------------------
    // 5. ELIGIBILITY CHECK MODULE
    // -------------------------------------------------------------
    private static void checkEligibilityMenu() {
        System.out.println(" --- ELIGIBILITY CHECK ENGINE ---");
        int studentId = InputUtil.readInt("Enter Student ID: ", 1, Integer.MAX_VALUE);
        int driveId = InputUtil.readInt("Enter Drive ID: ", 1, Integer.MAX_VALUE);

        Student student = placementService.getStudentById(studentId);
        PlacementDrive drive = placementService.getDriveById(driveId);

        if (student == null) {
            System.out.println("ERROR: Student with ID " + studentId + " not found.");
            return;
        }
        if (drive == null) {
            System.out.println("ERROR: Drive with ID " + driveId + " not found.");
            return;
        }

        eligibilityService.printEligibilityReport(student, drive);
    }

    // -------------------------------------------------------------
    // 6. SELECTION MANAGEMENT SUBMENU
    // -------------------------------------------------------------
    private static void selectionMenu() {
        while (true) {
            System.out.println(" --- SELECTION MANAGEMENT ---");
            System.out.println("1. Update Single Application Status");
            System.out.println("2. Process Pending Application Queue (Batch Processing)");
            System.out.println("3. Display Selected Students");
            System.out.println("0. Back to Main Menu");

            int choice = InputUtil.readInt("Select Option: ", 0, 3);
            if (choice == 0) break;

            switch (choice) {
                case 1:
                    int appId = InputUtil.readInt("Enter Application ID: ", 1, Integer.MAX_VALUE);
                    System.out.println("Select Status: 1. SHORTLISTED | 2. SELECTED | 3. REJECTED");
                    int stChoice = InputUtil.readInt("Choice: ", 1, 3);

                    String status = "APPLIED";
                    if (stChoice == 1) status = "SHORTLISTED";
                    else if (stChoice == 2) status = "SELECTED";
                    else if (stChoice == 3) status = "REJECTED";

                    if (placementService.updateApplicationStatus(appId, status)) {
                        System.out.println("SUCCESS: Application status updated to " + status);
                    } else {
                        System.out.println("ERROR: Failed to update application status.");
                    }
                    break;

                case 2:
                    int count = placementService.processNextPendingApplicationInQueue();
                    if (count > 0) {
                        System.out.println("SUCCESS: Queue processed " + count + " pending application(s).");
                    } else {
                        System.out.println("Info: Application queue is empty.");
                    }
                    break;

                case 3:
                    List<Application> selected = placementService.getSelectedApplications();
                    System.out.println(" === SELECTED STUDENTS ===");
                    displayApplicationList(selected);
                    break;
            }
        }
    }

    // -------------------------------------------------------------
    // 7. STATISTICS MODULE
    // -------------------------------------------------------------
    private static void displayStatistics() {
        System.out.println(" =============================================");
        System.out.println("       CAMPUS PLACEMENT STATISTICS           ");
        System.out.println("=============================================");

        Map<String, Object> stats = placementService.getPlacementStatistics();

        System.out.println("Total Registered Students: " + stats.get("totalStudents"));
        System.out.println("Total Registered Companies: " + stats.get("totalCompanies"));
        System.out.println("Total Placement Drives Hosted: " + stats.get("totalDrives"));
        System.out.println("Total Applications Received: " + stats.get("totalApplications"));
        System.out.println("Total Placed/Selected Students: " + stats.get("totalSelected"));
        System.out.printf("Overall Placement Percentage: %.2f%% ", (Double) stats.get("placementPercentage"));
        System.out.println("=============================================");
    }

    private static void displayApplicationList(List<Application> apps) {
        if (apps.isEmpty()) {
            System.out.println("No application records found.");
            return;
        }
        System.out.printf(" %-6s %-20s %-20s %-15s %-12s ", "App ID", "Student Name", "Company", "Role", "Status");
        System.out.println("-----------------------------------------------------------------------------");
        for (Application app : apps) {
            System.out.printf("%-6d %-20s %-20s %-15s %-12s ",
                    app.getApplicationId(), app.getStudentName(), app.getCompanyName(),
                    app.getJobRole(), app.getStatus());
        }
    }
}
