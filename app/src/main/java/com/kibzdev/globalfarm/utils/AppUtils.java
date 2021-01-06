package com.kibzdev.globalfarm.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.io.ByteArrayOutputStream;
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

    public static String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte, Base64.DEFAULT);
    }


    public static String getTodayYear() {

        DateFormat formatter = new SimpleDateFormat("yyyy");
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        return formatter.format(calendar.getTime());
    }

    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    public static String getPathFromURI(Uri contentUri, Context context) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }

    public static Bitmap decodeSampledBitmapFromFile(String imagePath,
                                                     int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        //return Bitmap.createScaledBitmap(bt, reqWidth, reqHeight, false);
        return BitmapFactory.decodeFile(imagePath, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, generateRandomCode(5), null);
        return Uri.parse(path);
    }

    public static String getRealPathFromURI(Uri uri, Context context) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    public static String convertBitmapToBase64(Bitmap bitmap) {

        Bitmap scaledImage = getResizedBitmap(bitmap, 1000);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        scaledImage.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);

    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static String generateRandomCode(int length) {
        String AB = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        Random rnd = new Random();
        int len = rnd.nextInt(5) + 5;
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }


}
