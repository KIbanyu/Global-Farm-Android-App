package com.kibzdev.globalfarm.models;



import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Itotia Kibanyu on 8/2/2020.
 */
public class ProductModel implements Serializable {
    private String name;
    private int image;
    private String imageUrl;
    private BigDecimal price;
    private int numberOfImages;
    private String description;
    private String product_quantity;
    private String [] imagePosts;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getImagePosts() {
        return imagePosts;
    }

    public void setImagePosts(String[] imagePosts) {
        this.imagePosts = imagePosts;
    }


    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getNumberOfImages() {
        return numberOfImages;
    }

    public void setNumberOfImages(int numberOfImages) {
        this.numberOfImages = numberOfImages;
    }


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
