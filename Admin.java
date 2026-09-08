package com.railway.model;

public class Admin extends User {

    private String role;

    public Admin() {
        super();
    }

    public Admin(int userId, String name, String email,
            String password, String phone, String role) {

        super(userId, name, email, password, phone);
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "userId=" + getUserId() +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
