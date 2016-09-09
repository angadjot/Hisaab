package com.tech.petabyteboy.hisaab.Models;

/**
 * Created by petabyteboy on 12/08/16.
 */
public class GroupModel {

    private String GroupName;
    private String GroupComment;
    private String GroupImage;
    private String GroupMemberName;
    private String GroupMemberNumber;
    private String GroupMemberImage;
    private String GroupID;
    private String Date;
    private String Time;

    public GroupModel(){
        GroupName = "";
        GroupComment = "";
        GroupImage = "";
        GroupMemberName = "";
        GroupMemberNumber = "";
        GroupMemberImage = "";
        GroupID = "";
        Date = "";
        Time = "";
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

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getGroupComment() {
        return GroupComment;
    }

    public void setGroupComment(String groupComment) {
        GroupComment = groupComment;
    }

    public String getGroupImage() {
        return GroupImage;
    }

    public void setGroupImage(String groupImage) {
        GroupImage = groupImage;
    }

    public String getGroupMemberName() {
        return GroupMemberName;
    }

    public void setGroupMemberName(String groupMemberName) {
        GroupMemberName = groupMemberName;
    }

    public String getGroupMemberNumber() {
        return GroupMemberNumber;
    }

    public void setGroupMemberNumber(String groupMemberNumber) {
        GroupMemberNumber = groupMemberNumber;
    }

    public String getGroupMemberImage() {
        return GroupMemberImage;
    }

    public void setGroupMemberImage(String groupMemberImage) {
        GroupMemberImage = groupMemberImage;
    }

    public String getGroupID() {
        return GroupID;
    }

    public void setGroupID(String groupID) {
        GroupID = groupID;
    }
}
