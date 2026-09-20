package service;

import model.PlacementDrive;
import model.Student;

import java.util.HashSet;
import java.util.Set;

/**
 * Service class responsible for evaluating student eligibility against job drive requirements.
 */
public class EligibilityService {

    /**
     * Checks if a student is eligible for a specific placement drive.
     * Criteria evaluated:
     * 1. CGPA >= drive.minCgpa
     * 2. Branch matches or drive permits "All"
     * 3. Student possesses required skills (case-insensitive)
     */
    public boolean isEligible(Student student, PlacementDrive drive) {
        if (student == null || drive == null) return false;

        // 1. CGPA Validation
        if (student.getCgpa() < drive.getMinCgpa()) {
            return false;
        }

        // 2. Branch Criteria Validation
        if (!drive.getEligibleBranch().equalsIgnoreCase("All") &&
            !drive.getEligibleBranch().equalsIgnoreCase(student.getBranch())) {
            return false;
        }

        // 3. Skill Criteria Validation
        return hasRequiredSkills(student, drive.getRequiredSkills());
    }

    private boolean hasRequiredSkills(Student student, String requiredSkillsStr) {
        if (requiredSkillsStr == null || requiredSkillsStr.trim().isEmpty()) {
            return true;
        }

        Set<String> studentSkillsLower = new HashSet<>();
        for (String skill : student.getSkills()) {
            studentSkillsLower.add(skill.trim().toLowerCase());
        }

        String[] required = requiredSkillsStr.split(",");
        for (String req : required) {
            String trimmedReq = req.trim().toLowerCase();
            if (!trimmedReq.isEmpty() && !studentSkillsLower.contains(trimmedReq)) {
                return false; // Missing at least one mandatory skill
            }
        }
        return true;
    }

    public void printEligibilityReport(Student student, PlacementDrive drive) {
        System.out.println(" ----------------ELIGIBILITY REPOT ----------------");
        System.out.println("Student Name: " + student.getName() + " (CGPA: " + student.getCgpa() + ", Branch: " + student.getBranch() + ")");
        System.out.println("Company: " + drive.getCompanyName() + " | Role: " + drive.getJobRole());
        System.out.println("Required Criteria: Min CGPA " + drive.getMinCgpa() + " | Branch: " + drive.getEligibleBranch());
        System.out.println("Required Skills: " + drive.getRequiredSkills());
        System.out.println("Student Skills: " + student.getSkillsAsString());
        System.out.println("----------------------------------------------------");

        boolean cgpaOk = student.getCgpa() >= drive.getMinCgpa();
        boolean branchOk = drive.getEligibleBranch().equalsIgnoreCase("All") || drive.getEligibleBranch().equalsIgnoreCase(student.getBranch());
        boolean skillsOk = hasRequiredSkills(student, drive.getRequiredSkills());

        System.out.println("[+] CGPA Check: " + (cgpaOk ? "PASSED" : "FAILED"));
        System.out.println("[+] Branch Check: " + (branchOk ? "PASSED" : "FAILED"));
        System.out.println("[+] Skills Check: " + (skillsOk ? "PASSED" : "FAILED"));

        if (cgpaOk && branchOk && skillsOk) {
            System.out.println(">> VERDICT: ELIGIBLE to apply!");
        } else {
            System.out.println(">> VERDICT: NOT ELIGIBLE.");
        }
        System.out.println("----------------------------------------------------");
    }
}
