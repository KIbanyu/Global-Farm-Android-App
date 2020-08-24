package com.kibzdev.globalfarm.models.response.models.response;

/**
 * Created by Itotia Kibanyu on 7/13/2020.
 */
public class LoginResponse extends BaseResponse {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private boolean canBook;
    private long userId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isCanBook() {
        return canBook;
    }

    public void setCanBook(boolean canBook) {
        this.canBook = canBook;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
