package dao;

import database.DatabaseManager;
import model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for handling Student CRUD database operations.
 */
public class StudentDAO {

    public boolean addStudent(Student student) {
        String sqlStudent = "INSERT INTO students (name, email, phone, branch, cgpa) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlStudent, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, student.getName());
            stmt.setString(2, student.getEmail());
            stmt.setString(3, student.getPhone());
            stmt.setString(4, student.getBranch());
            stmt.setDouble(5, student.getCgpa());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        student.setStudentId(rs.getInt(1));
                    }
                }
                // Save skills mapping
                saveStudentSkills(conn, student);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void saveStudentSkills(Connection conn, Student student) throws SQLException {
        if (student.getSkills().isEmpty()) return;

        for (String skillName : student.getSkills()) {
            int skillId = getOrCreateSkillId(conn, skillName.trim());
            String sqlMap = "INSERT IGNORE INTO student_skills (student_id, skill_id) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlMap)) {
                stmt.setInt(1, student.getStudentId());
                stmt.setInt(2, skillId);
                stmt.executeUpdate();
            }
        }
    }

    private int getOrCreateSkillId(Connection conn, String skillName) throws SQLException {
        String selectSql = "SELECT skill_id FROM skills WHERE skill_name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(selectSql)) {
            stmt.setString(1, skillName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("skill_id");
        }

        String insertSql = "INSERT INTO skills (skill_name) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, skillName);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        }
        return -1;
    }

    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE is_active = TRUE";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Student s = extractStudentFromResultSet(rs);
                s.setSkills(getSkillsByStudentId(conn, s.getStudentId()));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE student_id = ? AND is_active = TRUE";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Student s = extractStudentFromResultSet(rs);
                s.setSkills(getSkillsByStudentId(conn, s.getStudentId()));
                return s;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET name=?, phone=?, branch=?, cgpa=? WHERE student_id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getName());
            stmt.setString(2, student.getPhone());
            stmt.setString(3, student.getBranch());
            stmt.setDouble(4, student.getCgpa());
            stmt.setInt(5, student.getStudentId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteStudent(int studentId) {
        String sql = "UPDATE students SET is_active = FALSE WHERE student_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private List<String> getSkillsByStudentId(Connection conn, int studentId) throws SQLException {
        List<String> skills = new ArrayList<>();
        String sql = "SELECT sk.skill_name FROM skills sk " +
                     "JOIN student_skills ss ON sk.skill_id = ss.skill_id " +
                     "WHERE ss.student_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                skills.add(rs.getString("skill_name"));
            }
        }
        return skills;
    }

    private Student extractStudentFromResultSet(ResultSet rs) throws SQLException {
        return new Student(
                rs.getInt("student_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("branch"),
                rs.getDouble("cgpa")
        );
    }
}
