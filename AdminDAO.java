package com.railway.dao;

import com.railway.model.Admin;
import com.railway.util.DBConnection;

import java.sql.*;
import java.util.Optional;

public class AdminDAO {

    // =========================================
    // Find Admin By Email
    // =========================================

    public Optional<Admin> findByEmail(String email) {

        String sql = "SELECT u.user_id, u.name, u.email, u.password, " +
                "u.phone, a.role " +
                "FROM users u " +
                "INNER JOIN admins a ON u.user_id = a.user_id " +
                "WHERE u.email = ?";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Admin admin = new Admin();

                admin.setUserId(
                        rs.getInt("user_id"));

                admin.setName(
                        rs.getString("name"));

                admin.setEmail(
                        rs.getString("email"));

                admin.setPassword(
                        rs.getString("password"));

                admin.setPhone(
                        rs.getString("phone"));

                admin.setRole(
                        rs.getString("role"));

                return Optional.of(admin);
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error while finding admin: "
                            + e.getMessage());
        }

        return Optional.empty();
    }

    // =========================================
    // Add Admin
    // =========================================

    public boolean addAdmin(int userId, String role) {

        String sql = "INSERT INTO admins (user_id, role) " +
                "VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, role);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error while adding admin: "
                            + e.getMessage());

            return false;
        }
    }

    // =========================================
    // Update Admin Role
    // =========================================

    public boolean updateRole(int userId, String role) {

        String sql = "UPDATE admins SET role = ? " +
                "WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, role);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error while updating admin role: "
                            + e.getMessage());

            return false;
        }
    }

    // =========================================
    // Delete Admin
    // =========================================

    public boolean deleteAdmin(int userId) {

        String sql = "DELETE FROM admins WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error while deleting admin: "
                            + e.getMessage());

            return false;
        }
    }

    // =========================================
    // Check Admin
    // =========================================

    public boolean isAdmin(int userId) {

        String sql = "SELECT user_id FROM admins " +
                "WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {

            System.out.println(
                    "Error while checking admin: "
                            + e.getMessage());

            return false;
        }
    }
}