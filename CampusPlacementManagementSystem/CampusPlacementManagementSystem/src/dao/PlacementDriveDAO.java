package dao;

import database.DatabaseManager;
import model.PlacementDrive;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing Placement Drives.
 */
public class PlacementDriveDAO {

    public boolean addDrive(PlacementDrive drive) {
        String sql = "INSERT INTO placement_drives (company_id, job_role, min_cgpa, eligible_branch, required_skills, package_lpa, drive_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, drive.getCompanyId());
            stmt.setString(2, drive.getJobRole());
            stmt.setDouble(3, drive.getMinCgpa());
            stmt.setString(4, drive.getEligibleBranch());
            stmt.setString(5, drive.getRequiredSkills());
            stmt.setDouble(6, drive.getPackageLpa());
            stmt.setString(7, drive.getDriveDate());

            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) drive.setDriveId(rs.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<PlacementDrive> getAllDrives() {
        List<PlacementDrive> list = new ArrayList<>();
        String sql = "SELECT d.*, c.company_name FROM placement_drives d " +
                     "JOIN companies c ON d.company_id = c.company_id WHERE d.status = 'ACTIVE'";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(extractDrive(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public PlacementDrive getDriveById(int id) {
        String sql = "SELECT d.*, c.company_name FROM placement_drives d " +
                     "JOIN companies c ON d.company_id = c.company_id WHERE d.drive_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractDrive(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private PlacementDrive extractDrive(ResultSet rs) throws SQLException {
        PlacementDrive drive = new PlacementDrive();
        drive.setDriveId(rs.getInt("drive_id"));
        drive.setCompanyId(rs.getInt("company_id"));
        drive.setCompanyName(rs.getString("company_name"));
        drive.setJobRole(rs.getString("job_role"));
        drive.setMinCgpa(rs.getDouble("min_cgpa"));
        drive.setEligibleBranch(rs.getString("eligible_branch"));
        drive.setRequiredSkills(rs.getString("required_skills"));
        drive.setPackageLpa(rs.getDouble("package_lpa"));
        drive.setDriveDate(rs.getString("drive_date"));
        return drive;
    }
}
