package com.tech.petabyteboy.hisaab;

import java.util.ArrayList;

/**
 * Created by petabyteboy on 17/07/16.
 */
public class DuesSharedWithModel {

    public String addBtn;       //if its the button to add new contacts
    public String image;
    public boolean isSeleted;
    public String name;
    public String number;

    public DuesSharedWithModel() {
        addBtn = null;
        image = null;
        isSeleted = false;
        name = null;
        number = null;
    }

    public String getAddBtn() {
        return addBtn;
    }

    public void setAddBtn(String addBtn) {
        this.addBtn = addBtn;
    }

    public boolean isSeleted() {
        return isSeleted;
    }

    public void setSeleted(boolean seleted) {
        isSeleted = seleted;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public static String getName(String phone) {
        String name = "";
        String phoneNo;

        for (int i = 0; i < GlobalVariables.selectedContactList.size(); i++) {
            phoneNo = GlobalVariables.selectedContactList.get(i).getNumber();
            if (phoneNo != null && phoneNo.equalsIgnoreCase(phone)) {
                name = GlobalVariables.selectedContactList.get(i).getName();
            }
        }

        return name;
    }

    public static String getImage(String phone) {

        String image = "";

        for (int i = 0; i < GlobalVariables.selectedContactList.size(); i++) {
            if (GlobalVariables.selectedContactList.get(i).getNumber() != null && GlobalVariables.data.get(i).getNumber().equalsIgnoreCase(phone))
                image = GlobalVariables.selectedContactList.get(i).getImage();
        }

        return image;
    }
}
