package dao;

import database.DatabaseManager;
import model.Application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for Applications and Status Updates.
 */
public class ApplicationDAO {

    public boolean apply(int studentId, int driveId) {
        String sql = "INSERT INTO applications (student_id, drive_id, status) VALUES (?, ?, 'APPLIED')";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            stmt.setInt(2, driveId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Application> getAllApplications() {
        List<Application> list = new ArrayList<>();
        String sql = "SELECT a.*, s.name as student_name, c.company_name, d.job_role " +
                     "FROM applications a " +
                     "JOIN students s ON a.student_id = s.student_id " +
                     "JOIN placement_drives d ON a.drive_id = d.drive_id " +
                     "JOIN companies c ON d.company_id = c.company_id";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(extractApplication(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Application> getApplicationsByStudent(int studentId) {
        List<Application> list = new ArrayList<>();
        String sql = "SELECT a.*, s.name as student_name, c.company_name, d.job_role " +
                     "FROM applications a " +
                     "JOIN students s ON a.student_id = s.student_id " +
                     "JOIN placement_drives d ON a.drive_id = d.drive_id " +
                     "JOIN companies c ON d.company_id = c.company_id " +
                     "WHERE a.student_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractApplication(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Application> getApplicationsByStatus(String status) {
        List<Application> list = new ArrayList<>();
        String sql = "SELECT a.*, s.name as student_name, c.company_name, d.job_role " +
                     "FROM applications a " +
                     "JOIN students s ON a.student_id = s.student_id " +
                     "JOIN placement_drives d ON a.drive_id = d.drive_id " +
                     "JOIN companies c ON d.company_id = c.company_id " +
                     "WHERE a.status = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractApplication(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateStatus(int applicationId, String status) {
        String sql = "UPDATE applications SET status = ? WHERE application_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, applicationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Application extractApplication(ResultSet rs) throws SQLException {
        Application app = new Application();
        app.setApplicationId(rs.getInt("application_id"));
        app.setStudentId(rs.getInt("student_id"));
        app.setDriveId(rs.getInt("drive_id"));
        app.setStudentName(rs.getString("student_name"));
        app.setCompanyName(rs.getString("company_name"));
        app.setJobRole(rs.getString("job_role"));
        app.setAppliedDate(rs.getString("applied_date"));
        app.setStatus(rs.getString("status"));
        return app;
    }
}
