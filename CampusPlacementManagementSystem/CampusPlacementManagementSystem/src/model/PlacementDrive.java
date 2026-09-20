package model;

/**
 * Model class representing a Placement Drive posted by a company.
 */
public class PlacementDrive {
    private int driveId;
    private int companyId;
    private String companyName; // Joined property from Companies table
    private String jobRole;
    private double minCgpa;
    private String eligibleBranch;
    private String requiredSkills;
    private double packageLpa;
    private String driveDate;

    public PlacementDrive() {}

    public PlacementDrive(int companyId, String jobRole, double minCgpa, String eligibleBranch,
                          String requiredSkills, double packageLpa, String driveDate) {
        this.companyId = companyId;
        this.jobRole = jobRole;
        this.minCgpa = minCgpa;
        this.eligibleBranch = eligibleBranch;
        this.requiredSkills = requiredSkills;
        this.packageLpa = packageLpa;
        this.driveDate = driveDate;
    }

    // Getters and Setters
    public int getDriveId() { return driveId; }
    public void setDriveId(int driveId) { this.driveId = driveId; }

    public int getCompanyId() { return companyId; }
    public void setCompanyId(int companyId) { this.companyId = companyId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getJobRole() { return jobRole; }
    public void setJobRole(String jobRole) { this.jobRole = jobRole; }

    public double getMinCgpa() { return minCgpa; }
    public void setMinCgpa(double minCgpa) { this.minCgpa = minCgpa; }

    public String getEligibleBranch() { return eligibleBranch; }
    public void setEligibleBranch(String eligibleBranch) { this.eligibleBranch = eligibleBranch; }

    public String getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(String requiredSkills) { this.requiredSkills = requiredSkills; }

    public double getPackageLpa() { return packageLpa; }
    public void setPackageLpa(double packageLpa) { this.packageLpa = packageLpa; }

    public String getDriveDate() { return driveDate; }
    public void setDriveDate(String driveDate) { this.driveDate = driveDate; }
}
