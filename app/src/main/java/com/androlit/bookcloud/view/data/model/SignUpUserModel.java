package com.androlit.bookcloud.view.data.model;

/**
 * Created by ${farhanarnob} on ${06-Oct-16}.
 */

public class SignUpUserModel {

    private String email;
    private String password;
    private String fullName;

    public SignUpUserModel(String email, String passwor, String fullName) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
    }

    public SignUpUserModel() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
