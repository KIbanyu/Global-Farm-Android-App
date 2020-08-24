package com.kibzdev.globalfarm.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by Itotia Kibanyu on 7/13/2020.
 */
public class AppUtils {

//    public static String ip = "http://192.168.100.4:4390/api/android/v1/global_farm/";
    public static String ip = "http://3.129.21.182:4390//api/android/v1/global_farm/";
    public static String uploadImage = "eliqour.000webhostapp.com/eliqour";
    public static final String UPLOAD_IMAGE = "http://" + uploadImage + "/uploadGlobalImages.php";


    public static boolean isPhoneValid(String phone) {
        boolean isValid = false;
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber number = phoneNumberUtil.parse(phone, Constants.REGION);
            isValid = phoneNumberUtil.isValidNumber(number);
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        return isValid;
    }

    public static String parsePhoneNumber(String phone) {
        String finalPhoneNumber = "";
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber number = phoneNumberUtil.parse(phone, Constants.REGION);

            finalPhoneNumber = phoneNumberUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);

        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        return finalPhoneNumber;
    }

    public static String convertDate(String datToFormat) throws ParseException {
        SimpleDateFormat sdfIn = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfOut = new SimpleDateFormat("dd-MM-yyyy");
        String input = datToFormat;
        Date date = sdfIn.parse(input);

        return sdfOut.format(date);
    }


    public static String generateRandomChars(String candidateChars, int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(candidateChars.charAt(random.nextInt(candidateChars
                    .length())));
        }

        return sb.toString();
    }


    public static String getTodayDate() {

        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);

        return formatter.format(calendar.getTime());
    }


    public static String getTodayYear() {

        DateFormat formatter = new SimpleDateFormat("yyyy");
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        return formatter.format(calendar.getTime());
    }

}
