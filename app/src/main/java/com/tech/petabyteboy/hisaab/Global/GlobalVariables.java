package com.tech.petabyteboy.hisaab.Global;

import com.tech.petabyteboy.hisaab.Models.DuesSharedWithModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by petabyteboy on 17/07/16.
 */
public class GlobalVariables {

    public static ArrayList<DuesSharedWithModel> data = null;
    public static ArrayList<DuesSharedWithModel> selectedContactList = null;
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

    static {
        data = new ArrayList<>();
        splitContactId = new ArrayList<>();
        splitContactName = new ArrayList<>();
        splitContactNumber = new ArrayList<>();
        splitContactImage = new ArrayList<>();
        splitContactAmount = new ArrayList<>();
        groupContactName = new ArrayList<>();
        groupContactNumber = new ArrayList<>();
        groupContactImage = new ArrayList<>();
        groupName = new ArrayList<>();
        groupPicture = new ArrayList<>();
        selectedContactList = new ArrayList<>();
        
    }

    public static String ConvertDouble(Double value) {
        String angleFormated = new DecimalFormat("#.00").format(value);
        if (value < 1.0d && value > 0.0d) {
            return "0.00";
        }
        if (angleFormated.contains("-") && angleFormated.length() == 4) {
            return "00.00";
        }
        if (angleFormated.equalsIgnoreCase(".00")) {
            return "0.00";
        }
        return angleFormated;
    }

}
