package com.kibzdev.globalfarm.models.response.models.requests;

/**
 * Created by Itotia Kibanyu on 7/13/2020.
 */
public class BookServiceRequest {
    private long serviceId;
    private long userId;
    private String bookedAt;

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getBookedAt() {
        return bookedAt;
    }

    public void setBookedAt(String bookedAt) {
        this.bookedAt = bookedAt;
    }
}
