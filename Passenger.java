package com.railway.model;

public class Passenger {

    private int passengerId;
    private String name;
    private int age;
    private String gender;
    private String seatPreference;

    public Passenger() {
    }

    public Passenger(int passengerId, String name, int age,
            String gender, String seatPreference) {

        this.passengerId = passengerId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.seatPreference = seatPreference;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSeatPreference() {
        return seatPreference;
    }

    public void setSeatPreference(String seatPreference) {
        this.seatPreference = seatPreference;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "passengerId=" + passengerId +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", seatPreference='" + seatPreference + '\'' +
                '}';
    }
}