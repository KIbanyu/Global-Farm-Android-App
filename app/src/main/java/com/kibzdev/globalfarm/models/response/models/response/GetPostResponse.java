package com.kibzdev.globalfarm.models.response.models.response;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Itotia Kibanyu on 8/3/2020.
 */
public class GetPostResponse extends BaseResponse{


    private List<PostResponse> data;

    public List<PostResponse> getData() {
        return data;
    }

    public void setData(List<PostResponse> data) {
        this.data = data;
    }

    public static class PostResponse
    {
        private Long id;
        private String name;
        private String category;
        private BigDecimal price;
        private String quantity;
        private String description;
        private String location;
        private String phone;
        private String photosList;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPhotosList() {
            return photosList;
        }

        public void setPhotosList(String photosList) {
            this.photosList = photosList;
        }

    }

}
