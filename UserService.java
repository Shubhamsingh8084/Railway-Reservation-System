package com.railway.service;

import com.railway.dao.UserDAO;
import com.railway.exception.InvalidUserException;
import com.railway.model.User;

import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserDAO userDAO;

    // Constructor
    public UserService() {
        this.userDAO = new UserDAO();
    }

    // =========================================
    // Register User
    // =========================================

    public boolean registerUser(User user)
            throws InvalidUserException {

        validateUser(user);

        // Check whether email already exists
        Optional<User> existingUser = userDAO.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {

            throw new InvalidUserException(
                    "Email already registered!");
        }

        return userDAO.addUser(user);
    }

    // =========================================
    // Login User
    // =========================================

    public User login(String email, String password)
            throws InvalidUserException {

        if (email == null || email.trim().isEmpty()) {

            throw new InvalidUserException(
                    "Email cannot be empty!");
        }

        if (password == null || password.trim().isEmpty()) {

            throw new InvalidUserException(
                    "Password cannot be empty!");
        }

        Optional<User> optionalUser = userDAO.findByEmail(email);

        if (optionalUser.isEmpty()) {

            throw new InvalidUserException(
                    "User not found!");
        }

        User user = optionalUser.get();

        if (!user.getPassword().equals(password)) {

            throw new InvalidUserException(
                    "Invalid password!");
        }

        return user;
    }

    // =========================================
    // Find User By ID
    // =========================================

    public User getUserById(int userId)
            throws InvalidUserException {

        Optional<User> user = userDAO.findById(userId);

        if (user.isEmpty()) {

            throw new InvalidUserException(
                    "User not found with ID: " + userId);
        }

        return user.get();
    }

    // =========================================
    // Get All Users
    // =========================================

    public List<User> getAllUsers() {

        return userDAO.getAllUsers();
    }

    // =========================================
    // Update User
    // =========================================

    public boolean updateUser(User user)
            throws InvalidUserException {

        if (user.getUserId() <= 0) {

            throw new InvalidUserException(
                    "Invalid user ID!");
        }

        validateUser(user);

        return userDAO.updateUser(user);
    }

    // =========================================
    // Delete User
    // =========================================

    public boolean deleteUser(int userId)
            throws InvalidUserException {

        if (userId <= 0) {

            throw new InvalidUserException(
                    "Invalid user ID!");
        }

        Optional<User> user = userDAO.findById(userId);

        if (user.isEmpty()) {

            throw new InvalidUserException(
                    "User not found!");
        }

        return userDAO.deleteUser(userId);
    }

    // =========================================
    // Validate User
    // =========================================

    private void validateUser(User user)
            throws InvalidUserException {

        if (user == null) {

            throw new InvalidUserException(
                    "User cannot be null!");
        }

        if (user.getName() == null ||
                user.getName().trim().isEmpty()) {

            throw new InvalidUserException(
                    "Name cannot be empty!");
        }

        if (user.getEmail() == null ||
                user.getEmail().trim().isEmpty()) {

            throw new InvalidUserException(
                    "Email cannot be empty!");
        }

        if (!user.getEmail().contains("@")) {

            throw new InvalidUserException(
                    "Invalid email format!");
        }

        if (user.getPassword() == null ||
                user.getPassword().length() < 4) {

            throw new InvalidUserException(
                    "Password must contain at least 4 characters!");
        }

        if (user.getPhone() == null ||
                user.getPhone().length() != 10) {

            throw new InvalidUserException(
                    "Phone number must contain 10 digits!");
        }
    }
}