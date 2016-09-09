package com.tech.petabyteboy.hisaab.Models;

/**
 * Created by petabyteboy on 23/07/16.
 */
public class DuesModel {

    private String DueName;
    private String DueID;
    private String DueImage;
    private String GroupID;
    private String Category;
    private String Comment;
    private String Date;
    private String Time;
    private String SharedUserID;
    private String SharedUserName;
    private String SharedUserImage;
    private String UserSharedAmt;
    private String UserPaidAmt;
    private Integer SharedAmongSize;
    private String TotalAmt;
    private String CreatorID;
    private Boolean DueSettle;

    public DuesModel() {


        DueName = "";
        DueID = "";
        DueImage = "";
        GroupID = "";
        Category = "";
        Comment = "";
        Date = "";
        Time = "";
        SharedUserID = "";
        SharedUserName = "";
        SharedUserImage = "";
        UserSharedAmt = "";
        UserPaidAmt = "";
        SharedAmongSize = 0;
        TotalAmt = "";
        CreatorID = "";
        DueSettle = false;
    }

    public String getDueName() {
        return DueName;
    }

    public void setDueName(String dueName) {
        DueName = dueName;
    }

    public String getDueID() {
        return DueID;
    }

    public void setDueID(String dueID) {
        DueID = dueID;
    }

    public String getDueImage() {
        return DueImage;
    }

    public void setDueImage(String dueImage) {
        DueImage = dueImage;
    }

    public String getGroupID() {
        return GroupID;
    }

    public void setGroupID(String groupID) {
        GroupID = groupID;
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

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getSharedUserID() {
        return SharedUserID;
    }

    public void setSharedUserID(String sharedUserID) {
        SharedUserID = sharedUserID;
    }

    public String getSharedUserName() {
        return SharedUserName;
    }

    public void setSharedUserName(String sharedUserName) {
        SharedUserName = sharedUserName;
    }

    public String getSharedUserImage() {
        return SharedUserImage;
    }

    public void setSharedUserImage(String sharedUserImage) {
        SharedUserImage = sharedUserImage;
    }

    public String getUserSharedAmt() {
        return UserSharedAmt;
    }

    public void setUserSharedAmt(String userSharedAmt) {
        UserSharedAmt = userSharedAmt;
    }

    public String getUserPaidAmt() {
        return UserPaidAmt;
    }

    public void setUserPaidAmt(String userPaidAmt) {
        UserPaidAmt = userPaidAmt;
    }

    public Integer getSharedAmongSize() {
        return SharedAmongSize;
    }

    public void setSharedAmongSize(Integer sharedAmongSize) {
        SharedAmongSize = sharedAmongSize;
    }

    public String getTotalAmt() {
        return TotalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        TotalAmt = totalAmt;
    }

    public String getCreatorID() {
        return CreatorID;
    }

    public void setCreatorID(String createrID) {
        CreatorID = createrID;
    }

    public Boolean getDueSettle() {
        return DueSettle;
    }

    public void setDueSettle(Boolean dueSettle) {
        DueSettle = dueSettle;
    }
}
