package com.tech.petabyteboy.hisaab;

import java.util.ArrayList;

/**
 * Created by petabyteboy on 17/07/16.
 */
public class GlobalVariables {

    public static ArrayList<DuesSharedWithModel> data = null;
    public static ArrayList<String> groupContactImage = null;
    public static ArrayList<String> groupContactName = null;
    public static ArrayList<String> groupContactNumber = null;
    public static ArrayList<String> groupName = null;
    public static ArrayList<String> groupPicture = null;
    public static ArrayList<String> splitContactAmount;
    public static ArrayList<String> splitContactId;
    public static ArrayList<String> splitContactImage;
    public static ArrayList<String> splitContactName;
    public static ArrayList<String> splitContactNumber;
    public static String strRecentUsersArray;

    static {
        data = new ArrayList<>();
        splitContactId = new ArrayList();
        splitContactName = new ArrayList();
        splitContactNumber = new ArrayList();
        splitContactImage = new ArrayList();
        splitContactAmount = new ArrayList();
        groupContactName = new ArrayList();
        groupContactNumber = new ArrayList();
        groupContactImage = new ArrayList();
        groupName = new ArrayList();
        groupPicture = new ArrayList();
        strRecentUsersArray = null;
        
    }

}
