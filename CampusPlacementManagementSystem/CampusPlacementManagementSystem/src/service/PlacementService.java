package service;

import dao.ApplicationDAO;
import dao.CompanyDAO;
import dao.PlacementDriveDAO;
import dao.StudentDAO;
import model.Application;
import model.Company;
import model.PlacementDrive;
import model.Student;

import java.util.*;

/**
 * Main Business Logic Service. Demonstrates practical Data Structure usage:
 * - Queue (LinkedList): FIFO batch processing of job applications.
 * - HashMap: Generating aggregated stats and quick lookups.
 * - List (ArrayList): Collection handling.
 */
public class PlacementService {

    private final StudentDAO studentDAO = new StudentDAO();
    private final CompanyDAO companyDAO = new CompanyDAO();
    private final PlacementDriveDAO driveDAO = new PlacementDriveDAO();
    private final ApplicationDAO applicationDAO = new ApplicationDAO();

    // Queue data structure used for sequential background processing of applications
    private final Queue<Application> pendingApplicationQueue = new LinkedList<>();

    // Student Operations
    public boolean registerStudent(Student student) {
        return studentDAO.addStudent(student);
    }

    public List<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }

    public Student getStudentById(int id) {
        return studentDAO.getStudentById(id);
    }

    public boolean updateStudent(Student student) {
        return studentDAO.updateStudent(student);
    }

    public boolean deleteStudent(int id) {
        return studentDAO.deleteStudent(id);
    }

    // Company Operations
    public boolean registerCompany(Company company) {
        return companyDAO.addCompany(company);
    }

    public List<Company> getAllCompanies() {
        return companyDAO.getAllCompanies();
    }

    public Company getCompanyById(int id) {
        return companyDAO.getCompanyById(id);
    }

    public boolean deleteCompany(int id) {
        return companyDAO.deleteCompany(id);
    }

    // Drive Operations
    public boolean createPlacementDrive(PlacementDrive drive) {
        return driveDAO.addDrive(drive);
    }

    public List<PlacementDrive> getAllDrives() {
        return driveDAO.getAllDrives();
    }

    public PlacementDrive getDriveById(int id) {
        return driveDAO.getDriveById(id);
    }

    // Application Operations
    public boolean applyForDrive(int studentId, int driveId) {
        boolean success = applicationDAO.apply(studentId, driveId);
        if (success) {
            // Load application into Queue for subsequent automated shortlisting steps
            List<Application> apps = applicationDAO.getApplicationsByStudent(studentId);
            if (!apps.isEmpty()) {
                pendingApplicationQueue.offer(apps.get(apps.size() - 1));
            }
        }
        return success;
    }

    public List<Application> getAllApplications() {
        return applicationDAO.getAllApplications();
    }

    public List<Application> getApplicationsByStudent(int studentId) {
        return applicationDAO.getApplicationsByStudent(studentId);
    }

    public List<Application> getSelectedApplications() {
        return applicationDAO.getApplicationsByStatus("SELECTED");
    }

    public boolean updateApplicationStatus(int applicationId, String status) {
        return applicationDAO.updateStatus(applicationId, status);
    }

    /**
     * Practical Queue implementation: Processes the next pending application in FIFO order.
     * Auto-shortlists applicants whose CGPA >= 8.0.
     */
    public int processNextPendingApplicationInQueue() {
        if (pendingApplicationQueue.isEmpty()) {
            // Reload 'APPLIED' status applications into Queue if empty
            List<Application> appliedList = applicationDAO.getApplicationsByStatus("APPLIED");
            pendingApplicationQueue.addAll(appliedList);
        }

        int processedCount = 0;
        while (!pendingApplicationQueue.isEmpty()) {
            Application app = pendingApplicationQueue.poll(); // Queue poll (FIFO)
            Student student = studentDAO.getStudentById(app.getStudentId());

            if (student != null) {
                if (student.getCgpa() >= 8.0) {
                    applicationDAO.updateStatus(app.getApplicationId(), "SHORTLISTED");
                    System.out.println("Queue Processor: Auto-shortlisted " + student.getName() + " for App ID #" + app.getApplicationId());
                }
                processedCount++;
            }
        }
        return processedCount;
    }

    /**
     * Aggregates placement analytics using HashMap data structures.
     */
    public Map<String, Object> getPlacementStatistics() {
        Map<String, Object> stats = new HashMap<>();

        List<Student> students = studentDAO.getAllStudents();
        List<Company> companies = companyDAO.getAllCompanies();
        List<PlacementDrive> drives = driveDAO.getAllDrives();
        List<Application> applications = applicationDAO.getAllApplications();
        List<Application> selected = applicationDAO.getApplicationsByStatus("SELECTED");

        int totalStudents = students.size();
        int totalSelected = selected.size();

        double placementRatio = (totalStudents > 0) ? ((double) totalSelected / totalStudents) * 100.0 : 0.0;

        stats.put("totalStudents", totalStudents);
        stats.put("totalCompanies", companies.size());
        stats.put("totalDrives", drives.size());
        stats.put("totalApplications", applications.size());
        stats.put("totalSelected", totalSelected);
        stats.put("placementPercentage", placementRatio);

        return stats;
    }
}
