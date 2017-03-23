package com.sayarat.Models;

/**
 * Created by lenovo on 2/23/2017.
 */

public class Message_model {
    String  message , date;
        UserModel user_model ;

    public UserModel getUser_model() {
        return user_model;
    }

    public void setUser_model(UserModel user_model) {
        this.user_model = user_model;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
