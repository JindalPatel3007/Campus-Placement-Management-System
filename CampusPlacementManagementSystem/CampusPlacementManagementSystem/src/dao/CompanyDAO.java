package dao;

import database.DatabaseManager;
import model.Company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing recruiting Companies.
 */
public class CompanyDAO {

    public boolean addCompany(Company company) {
        String sql = "INSERT INTO companies (company_name, location, hr_name, hr_email, hr_phone) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, company.getCompanyName());
            stmt.setString(2, company.getLocation());
            stmt.setString(3, company.getHrName());
            stmt.setString(4, company.getHrEmail());
            stmt.setString(5, company.getHrPhone());

            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    company.setCompanyId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Company> getAllCompanies() {
        List<Company> list = new ArrayList<>();
        String sql = "SELECT * FROM companies";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Company(
                        rs.getInt("company_id"),
                        rs.getString("company_name"),
                        rs.getString("location"),
                        rs.getString("hr_name"),
                        rs.getString("hr_email"),
                        rs.getString("hr_phone")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Company getCompanyById(int id) {
        String sql = "SELECT * FROM companies WHERE company_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Company(
                        rs.getInt("company_id"),
                        rs.getString("company_name"),
                        rs.getString("location"),
                        rs.getString("hr_name"),
                        rs.getString("hr_email"),
                        rs.getString("hr_phone")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteCompany(int companyId) {
        String sql = "DELETE FROM companies WHERE company_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, companyId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
