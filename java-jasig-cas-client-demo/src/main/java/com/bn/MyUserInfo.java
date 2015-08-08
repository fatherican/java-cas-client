package com.bn;

import org.jasig.cas.client.validation.UserInfo;

/**
 * Created by kai on 2015/8/8.
 */
public class MyUserInfo extends UserInfo {
    private  String userId;

    public MyUserInfo() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
