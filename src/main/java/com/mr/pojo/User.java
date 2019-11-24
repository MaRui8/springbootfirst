package com.mr.pojo;

public class User {

    private String userName;
    private String password;

    private byte browserType;

    public User() {
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte getBrowserType() {
        return browserType;
    }

    public void setBrowserType(byte browserType) {
        this.browserType = browserType;
    }
}
