package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing a Student entity.
 */
public class Student {
    private int studentId;
    private String name;
    private String email;
    private String phone;
    private String branch;
    private double cgpa;
    private List<String> skills;

    // Constructors
    public Student() {
        this.skills = new ArrayList<>();
    }

    public Student(String name, String email, String phone, String branch, double cgpa) {
        this();
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.branch = branch;
        this.cgpa = cgpa;
    }

    public Student(int studentId, String name, String email, String phone, String branch, double cgpa) {
        this(name, email, phone, branch, cgpa);
        this.studentId = studentId;
    }

    // Getters and Setters
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public double getCgpa() { return cgpa; }
    public void setCgpa(double cgpa) { this.cgpa = cgpa; }

    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }

    public void addSkill(String skill) {
        if (!this.skills.contains(skill)) {
            this.skills.add(skill);
        }
    }

    public String getSkillsAsString() {
        return String.join(", ", skills);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + studentId +
                ", name='" + name +
                ", branch='" + branch +
                ", cgpa=" + cgpa +
                '}';
    }
}
