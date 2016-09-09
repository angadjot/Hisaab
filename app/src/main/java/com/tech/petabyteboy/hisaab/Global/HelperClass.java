package com.tech.petabyteboy.hisaab.Global;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.tech.petabyteboy.hisaab.Models.DuesSharedWithModel;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by petabyteboy on 17/07/16.
 */
public class HelperClass {

    public static ArrayList<DuesSharedWithModel> data = null;
    public static ArrayList<DuesSharedWithModel> selectedContactList = null;
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
        selectedContactList = new ArrayList<>();
        
    }

    public static String ConvertDouble(Double value) {
        String Formated = new DecimalFormat("#.00").format(value);
        if (value < 1.0d && value > 0.0d) {
            return "0.00";
        }
        if (Formated.contains("-") && Formated.length() == 4) {
            return "00.00";
        }
        if (Formated.equalsIgnoreCase(".00")) {
            return "0.00";
        }
        return Formated;
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Hisaab", null);
        return Uri.parse(path);
    }

}
