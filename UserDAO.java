package com.railway.dao;

import com.railway.model.User;
import com.railway.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO {

    // =========================
    // Register User
    // =========================

    public boolean addUser(User user) {

        String sql = "INSERT INTO users (name, email, password, phone) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getPhone());

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {

            System.out.println("Error while adding user: "
                    + e.getMessage());

            return false;
        }
    }

    // =========================
    // Find User By Email
    // =========================

    public Optional<User> findByEmail(String email) {

        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                User user = mapUser(rs);

                return Optional.of(user);
            }

        } catch (SQLException e) {

            System.out.println("Error while finding user: "
                    + e.getMessage());
        }

        return Optional.empty();
    }

    // =========================
    // Find User By ID
    // =========================

    public Optional<User> findById(int userId) {

        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return Optional.of(mapUser(rs));
            }

        } catch (SQLException e) {

            System.out.println("Error while finding user: "
                    + e.getMessage());
        }

        return Optional.empty();
    }

    // =========================
    // Get All Users
    // =========================

    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();

        String sql = "SELECT * FROM users";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                users.add(mapUser(rs));
            }

        } catch (SQLException e) {

            System.out.println("Error while fetching users: "
                    + e.getMessage());
        }

        return users;
    }

    // =========================
    // Update User
    // =========================

    public boolean updateUser(User user) {

        String sql = "UPDATE users SET name = ?, email = ?, " +
                "password = ?, phone = ? WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getPhone());
            ps.setInt(5, user.getUserId());

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {

            System.out.println("Error while updating user: "
                    + e.getMessage());

            return false;
        }
    }

    // =========================
    // Delete User
    // =========================

    public boolean deleteUser(int userId) {

        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            int rows = ps.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {

            System.out.println("Error while deleting user: "
                    + e.getMessage());

            return false;
        }
    }

    // =========================
    // Convert ResultSet → User
    // =========================

    private User mapUser(ResultSet rs) throws SQLException {

        User user = new User();

        user.setUserId(rs.getInt("user_id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setPhone(rs.getString("phone"));

        return user;
    }
}
