package com.tech.petabyteboy.hisaab;

/**
 * Created by petabyteboy on 22/07/16.
 */
public class UserDuesModel {

    private String Category;
    private String Comment;
    private String Date_time;
    private String Group_id;
    private String Due_id;
    private String Self_amt;
    private String Shared_User_id;
    private String Shared_User_Name;
    private String Shared_User_Image;
    private String Shared_User_Amt;
    private String Shared_among;
    private String Total_amt;
    private String Who_paid_id;

    public UserDuesModel(){
        Category = "";
        Comment = "";
        Date_time = "";
        Group_id = "";
        Due_id = "";
        Self_amt = "";
        Shared_User_Amt = "";
        Shared_User_id = "";
        Shared_among = "";
        Shared_User_Name = "";
        Total_amt = "";
        Who_paid_id = "";
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getWho_paid_id() {
        return Who_paid_id;
    }

    public void setWho_paid_id(String who_paid_id) {
        Who_paid_id = who_paid_id;
    }

    public String getTotal_amt() {
        return Total_amt;
    }

    public void setTotal_amt(String total_amt) {
        Total_amt = total_amt;
    }

    public String getShared_among() {
        return Shared_among;
    }

    public void setShared_among(String shared_among) {
        Shared_among = shared_among;
    }

    public String getShared_User_Amt() {
        return Shared_User_Amt;
    }

    public void setShared_User_Amt(String shared_User_Amt) {
        Shared_User_Amt = shared_User_Amt;
    }

    public String getShared_User_Image() {
        return Shared_User_Image;
    }

    public void setShared_User_Image(String shared_User_Image) {
        Shared_User_Image = shared_User_Image;
    }

    public String getShared_User_Name() {
        return Shared_User_Name;
    }

    public void setShared_User_Name(String shared_User_Name) {
        Shared_User_Name = shared_User_Name;
    }

    public String getShared_User_id() {
        return Shared_User_id;
    }

    public void setShared_User_id(String shared_User_id) {
        Shared_User_id = shared_User_id;
    }

    public String getSelf_amt() {
        return Self_amt;
    }

    public void setSelf_amt(String self_amt) {
        Self_amt = self_amt;
    }

    public String getDue_id() {
        return Due_id;
    }

    public void setDue_id(String due_id) {
        Due_id = due_id;
    }

    public String getGroup_id() {
        return Group_id;
    }

    public void setGroup_id(String group_id) {
        Group_id = group_id;
    }

    public String getDate_time() {
        return Date_time;
    }

    public void setDate_time(String date_time) {
        Date_time = date_time;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }
}
