package com.kibzdev.globalfarm.models;

import android.graphics.Bitmap;

/**
 * Created by Itotia Kibanyu on 8/26/2020.
 */
public class ImagesModel {
    private String title;
    private String imageType;
    private String headerOne;
    private String headerTwo;
    private String headerThree;
    private int id;
    private Bitmap image;

    public String getHeaderOne() {
        return headerOne;
    }

    public void setHeaderOne(String headerOne) {
        this.headerOne = headerOne;
    }

    public String getHeaderTwo() {
        return headerTwo;
    }

    public void setHeaderTwo(String headerTwo) {
        this.headerTwo = headerTwo;
    }

    public String getHeaderThree() {
        return headerThree;
    }

    public void setHeaderThree(String headerThree) {
        this.headerThree = headerThree;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
