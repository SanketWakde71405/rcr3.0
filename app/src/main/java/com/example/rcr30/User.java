package com.example.rcr30;

public class User {
    String Name;
    String Email;
    String Branch;

    public User() {
    }

    String Year;
    String Domain;
    String ProfilePic;

    public String getName() {
        return Name;
    }

    public User(String name, String email, String branch, String year, String domain) {
        Name = name;
        Email = email;
        Branch = branch;
        Year = year;
        Domain = domain;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getBranch() {
        return Branch;
    }

    public void setBranch(String branch) {
        Branch = branch;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getDomain() {
        return Domain;
    }

    public void setDomain(String domain) {
        Domain = domain;
    }

    public String getProfilePic() {
        return ProfilePic;
    }

    public void setProfilePic(String profilePic) {
        ProfilePic = profilePic;
    }

    public User(String name, String email, String branch, String year, String domain, String profilePic) {
        Name = name;
        Email = email;
        Branch = branch;
        Year = year;
        Domain = domain;
        ProfilePic = profilePic;
    }
}
