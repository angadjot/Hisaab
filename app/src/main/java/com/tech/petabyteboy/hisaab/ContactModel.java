package com.tech.petabyteboy.hisaab;

/**
 * Created by petabyteboy on 17/07/16.
 */
public class ContactModel {

    String image_url;
    String name;
    String number;
    boolean selected;
    String selectedNo;

    public ContactModel(){

        image_url = null;
        name = null;
        number = null;
        selected = false;
        selectedNo = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getSelectedNo() {
        return selectedNo;
    }

    public void setSelectedNo(String selectedNo) {
        this.selectedNo = selectedNo;
    }
}
