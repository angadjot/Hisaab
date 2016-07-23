package com.tech.petabyteboy.hisaab;

import android.location.Geocoder;

/**
 * Created by petabyteboy on 14/07/16.
 */
public class Users {

    private String Username;
    private String PhoneNumber;
    private String DateOfBirth;
    private String Gender;
    private String City;
    private String EmailID;
    private String UserID;
    private String Image;
    private boolean exists;

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public Users() {

        Username = "";
        Image = "";
        PhoneNumber = "";
        Gender = "";
        EmailID = "";
        City = "";
        DateOfBirth = "";
        UserID = "";
        exists = false;
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

}
