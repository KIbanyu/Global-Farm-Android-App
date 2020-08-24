package com.kibzdev.globalfarm.models.response.models.requests;

/**
 * Created by Itotia Kibanyu on 7/13/2020.
 */
public class BaseRequest {
    private String appVersion;
    private String phoneModel;

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }
}
