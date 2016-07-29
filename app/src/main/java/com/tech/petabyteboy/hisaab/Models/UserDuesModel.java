package com.tech.petabyteboy.hisaab.Models;

/**
 * Created by petabyteboy on 23/07/16.
 */
public class UserDuesModel {

    private String Category;
    private String Comment;
    private String Date_time;
    private String Due_name;
    private String Shared_User_id;
    private String Shared_User_Name;
    private String Shared_User_Image;
    private String User_Shared_Amt;
    private String User_Paid_amt;
    private Integer Shared_among_size;
    private String Total_amt;
    private String Who_created_id;
    private String due_image;
    private Boolean settle;

    public UserDuesModel(){
        Category = "";
        Comment = "";
        Date_time = "";
        Due_name = "";
        Shared_User_id = "";
        Shared_User_Name = "";
        Shared_User_Image = "";
        User_Shared_Amt = "";
        User_Paid_amt = "";
        Shared_among_size = 0;
        Total_amt = "";
        Who_created_id = "";
        due_image = "";
        settle = false;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getDate_time() {
        return Date_time;
    }

    public void setDate_time(String date_time) {
        Date_time = date_time;
    }

    public String getDue_name() {
        return Due_name;
    }

    public void setDue_name(String due_name) {
        Due_name = due_name;
    }

    public String getShared_User_id() {
        return Shared_User_id;
    }

    public void setShared_User_id(String shared_User_id) {
        Shared_User_id = shared_User_id;
    }

    public String getShared_User_Name() {
        return Shared_User_Name;
    }

    public void setShared_User_Name(String shared_User_Name) {
        Shared_User_Name = shared_User_Name;
    }

    public String getShared_User_Image() {
        return Shared_User_Image;
    }

    public void setShared_User_Image(String shared_User_Image) {
        Shared_User_Image = shared_User_Image;
    }

    public String getUser_Shared_Amt() {
        return User_Shared_Amt;
    }

    public void setUser_Shared_Amt(String user_Shared_Amt) {
        User_Shared_Amt = user_Shared_Amt;
    }

    public String getUser_Paid_amt() {
        return User_Paid_amt;
    }

    public void setUser_Paid_amt(String user_Paid_amt) {
        User_Paid_amt = user_Paid_amt;
    }

    public Integer getShared_among_size() {
        return Shared_among_size;
    }

    public void setShared_among_size(Integer shared_among_size) {
        Shared_among_size = shared_among_size;
    }

    public String getTotal_amt() {
        return Total_amt;
    }

    public void setTotal_amt(String total_amt) {
        Total_amt = total_amt;
    }

    public String getWho_created_id() {
        return Who_created_id;
    }

    public void setWho_created_id(String who_created_id) {
        Who_created_id = who_created_id;
    }

    public String getDue_image() {
        return due_image;
    }

    public void setDue_image(String due_image) {
        this.due_image = due_image;
    }

    public Boolean getSettle() {
        return settle;
    }

    public void setSettle(Boolean settle) {
        this.settle = settle;
    }
}
