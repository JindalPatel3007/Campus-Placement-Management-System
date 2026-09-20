package model;

/**
 * Model class representing a Student's Job Application.
 */
public class Application {
    private int applicationId;
    private int studentId;
    private int driveId;
    private String studentName;
    private String companyName;
    private String jobRole;
    private String appliedDate;
    private String status; // APPLIED, SHORTLISTED, SELECTED, REJECTED

    public Application() {}

    public Application(int studentId, int driveId) {
        this.studentId = studentId;
        this.driveId = driveId;
        this.status = "APPLIED";
    }

    // Getters and Setters
    public int getApplicationId() { return applicationId; }
    public void setApplicationId(int applicationId) { this.applicationId = applicationId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getDriveId() { return driveId; }
    public void setDriveId(int driveId) { this.driveId = driveId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getJobRole() { return jobRole; }
    public void setJobRole(String jobRole) { this.jobRole = jobRole; }

    public String getAppliedDate() { return appliedDate; }
    public void setAppliedDate(String appliedDate) { this.appliedDate = appliedDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
