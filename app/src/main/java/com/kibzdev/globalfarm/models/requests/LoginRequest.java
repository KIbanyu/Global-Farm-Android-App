package com.kibzdev.globalfarm.models.requests;

/**
 * Created by Itotia Kibanyu on 7/13/2020.
 */
public class LoginRequest {
    private String phoneNumber;
    private String password;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
