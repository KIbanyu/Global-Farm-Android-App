package com.kibzdev.globalfarm.models.response;

/**
 * Created by Itotia Kibanyu on 7/13/2020.
 */
public class BaseResponse {
    private String status;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
