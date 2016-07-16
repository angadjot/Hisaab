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

    public Users(){

    }

    public Users(String strUserName, String strPhoneNo, String strDOB, String strGender, String strCity, String strEmailID){

        Username = strUserName;
        PhoneNumber = strPhoneNo;
        DateOfBirth = strDOB;
        Gender = strGender;
        City = strCity;
        EmailID = strEmailID;
    }

    public Users(String strUserName, String strPhoneNo, String strEmailID){

        Username = strUserName;
        PhoneNumber = strPhoneNo;
        DateOfBirth = null;
        Gender = null;
        City = null;
        EmailID = strEmailID;
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
