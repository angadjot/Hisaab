package com.tech.petabyteboy.hisaab.Models;

/**
 * Created by petabyteboy on 23/07/16.
 */
public class UserModel {

    private String Username;
    private String PhoneNumber;
    private String DateOfBirth;
    private String Gender;
    private String City;
    private String EmailID;
    private String UserID;
    private String Image;

    public UserModel() {

        Username = "";
        Image = "";
        PhoneNumber = "";
        Gender = "";
        EmailID = "";
        City = "";
        DateOfBirth = "";
        UserID = "";
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String emailID) {
        EmailID = emailID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
