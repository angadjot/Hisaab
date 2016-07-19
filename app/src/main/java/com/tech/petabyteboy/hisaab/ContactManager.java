package com.tech.petabyteboy.hisaab;

import java.util.ArrayList;

/**
 * Created by petabyteboy on 17/07/16.
 */
public class ContactManager {

    public static ContactManager contactManager;
    ArrayList<ContactModel> contactList;

    public ContactManager() {
        contactList = new ArrayList<>();
    }

    public static ContactManager getInstance() {

        if (contactManager != null)
            return contactManager;
        else {
            contactManager = new ContactManager();
            return contactManager;
        }
    }

    public static ContactManager getContactManager() {
        return contactManager;
    }

    public static void setContactManager(ContactManager contactManager) {
        ContactManager.contactManager = contactManager;
    }

    public ArrayList<ContactModel> getContactList() {
        return contactList;
    }

    public void setContactList(ArrayList<ContactModel> contactList) {
        this.contactList = contactList;
    }

    public String getName(String phone) {
        String name = null;
        String phoneNo;

        for (int i = 0; i < contactList.size(); i++) {
            ArrayList<String> ContactList = new ArrayList<>();
            phoneNo = contactList.get(i).getNumber();
            if (phoneNo != null) {
                if (phoneNo.contains(",")) {
                    String[] number = phoneNo.split(",");
                    for (Object add : number)
                        ContactList.add((String) add);
                }
                else
                    ContactList.add(phoneNo);
            }
            if (ContactList.contains(phone))
                name = contactList.get(i).getName();
        }

        return name;
    }

    public String getImage(String phone){

        String image = null;

        for (int i=0;i<contactList.size();i++){
            if ( contactList.get(i).getNumber() != null && contactList.get(i).getNumber().contains(phone) )
                image = contactList.get(i).getImage_url();
        }

        return image;
    }
}
