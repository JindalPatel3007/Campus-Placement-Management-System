-- ========================================================
-- CAMPUS PLACEMENT MANAGEMENT SYSTEM - DATABASE SCHEMA
-- Target RDBMS: MySQL / MariaDB (XAMPP Compatible)
-- ========================================================

CREATE DATABASE IF NOT EXISTS campus_placement;
USE campus_placement;

-- Drop tables in reverse foreign key order to prevent dependency errors
DROP TABLE IF EXISTS selections;
DROP TABLE IF EXISTS applications;
DROP TABLE IF EXISTS student_skills;
DROP TABLE IF EXISTS skills;
DROP TABLE IF EXISTS placement_drives;
DROP TABLE IF EXISTS companies;
DROP TABLE IF EXISTS students;

-- 1. STUDENTS TABLE
CREATE TABLE students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(15) NOT NULL,
    branch VARCHAR(50) NOT NULL,
    cgpa DECIMAL(3,2) NOT NULL CHECK (cgpa >= 0.00 AND cgpa <= 10.00),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. COMPANIES TABLE
CREATE TABLE companies (
    company_id INT AUTO_INCREMENT PRIMARY KEY,
    company_name VARCHAR(100) NOT NULL UNIQUE,
    location VARCHAR(100) NOT NULL,
    hr_name VARCHAR(100) NOT NULL,
    hr_email VARCHAR(100) NOT NULL,
    hr_phone VARCHAR(15) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. PLACEMENT DRIVES TABLE
CREATE TABLE placement_drives (
    drive_id INT AUTO_INCREMENT PRIMARY KEY,
    company_id INT NOT NULL,
    job_role VARCHAR(100) NOT NULL,
    min_cgpa DECIMAL(3,2) NOT NULL DEFAULT 6.00,
    eligible_branch VARCHAR(50) NOT NULL,
    required_skills VARCHAR(255) NOT NULL,
    package_lpa DECIMAL(5,2) NOT NULL,
    drive_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    FOREIGN KEY (company_id) REFERENCES companies(company_id) ON DELETE CASCADE
);

-- 4. APPLICATIONS TABLE
CREATE TABLE applications (
    application_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    drive_id INT NOT NULL,
    applied_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'APPLIED' CHECK (status IN ('APPLIED', 'SHORTLISTED', 'SELECTED', 'REJECTED')),
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (drive_id) REFERENCES placement_drives(drive_id) ON DELETE CASCADE,
    UNIQUE KEY unique_student_drive (student_id, drive_id)
);

-- 5. SKILLS CATALOG TABLE
CREATE TABLE skills (
    skill_id INT AUTO_INCREMENT PRIMARY KEY,
    skill_name VARCHAR(50) NOT NULL UNIQUE
);

-- 6. STUDENT SKILLS (MANY-TO-MANY RELATIONSHIP)
CREATE TABLE student_skills (
    student_id INT NOT NULL,
    skill_id INT NOT NULL,
    PRIMARY KEY (student_id, skill_id),
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(skill_id) ON DELETE CASCADE
);

-- ========================================================
-- SAMPLE SEED DATA
-- ========================================================

-- Insert Skills
INSERT INTO skills (skill_name) VALUES 
('Java'), ('Python'), ('SQL'), ('C++'), ('Data Structures'), ('Web Development'), ('AWS');

-- Insert Students
INSERT INTO students (name, email, phone, branch, cgpa) VALUES 
('Rahul Sharma', 'rahul.sharma@example.com', '9876543210', 'Computer Engineering', 8.75),
('Priya Patel', 'priya.patel@example.com', '9876543211', 'Computer Engineering', 9.20),
('Amit Verma', 'amit.verma@example.com', '9876543212', 'Information Technology', 7.40),
('Neha Gupta', 'neha.gupta@example.com', '9876543213', 'Electronics Engineering', 6.80),
('Rohan Mehta', 'rohan.mehta@example.com', '9876543214', 'Computer Engineering', 8.10);

-- Map Skills to Students
INSERT INTO student_skills (student_id, skill_id) VALUES 
(1, 1), (1, 3), (1, 5), -- Rahul: Java, SQL, Data Structures
(2, 1), (2, 2), (2, 3), (2, 6), -- Priya: Java, Python, SQL, Web Dev
(3, 2), (3, 3), -- Amit: Python, SQL
(4, 4), (4, 3), -- Neha: C++, SQL
(5, 1), (5, 3), (5, 7); -- Rohan: Java, SQL, AWS

-- Insert Companies
INSERT INTO companies (company_name, location, hr_name, hr_email, hr_phone) VALUES 
('TCS Digital', 'Mumbai', 'Anil Deshmukh', 'hr@tcs.com', '9123456789'),
('Infosys Technologies', 'Bengaluru', 'Sunita Reddy', 'careers@infosys.com', '9123456790'),
('Google India', 'Hyderabad', 'David Miller', 'campus@google.com', '9123456791');

-- Insert Placement Drives
INSERT INTO placement_drives (company_id, job_role, min_cgpa, eligible_branch, required_skills, package_lpa, drive_date) VALUES 
(1, 'Software Engineer', 7.00, 'Computer Engineering', 'Java, SQL', 7.50, '2026-10-15'),
(2, 'Systems Engineer', 6.50, 'All', 'SQL', 4.50, '2026-10-20'),
(3, 'Associate Software Developer', 8.50, 'Computer Engineering', 'Java, Data Structures', 18.00, '2026-11-05');

-- Insert Initial Applications
INSERT INTO applications (student_id, drive_id, status) VALUES 
(1, 1, 'SHORTLISTED'),
(1, 3, 'SELECTED'),
(2, 1, 'SHORTLISTED'),
(2, 3, 'SELECTED'),
(5, 1, 'APPLIED');
