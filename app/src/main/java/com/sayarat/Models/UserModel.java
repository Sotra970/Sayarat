package com.sayarat.Models;

import java.io.Serializable;

/**
 * Created by sotra on 9/16/2016.
 */


public class UserModel implements Serializable{

    String user_id , user_name , user_phone  ,pass , email , img ,token  ;

    public UserModel(String user_id, String user_name, String user_phone , String email , String image) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_phone = user_phone;
        this.email = email ;
        img = image ;


    }

    public UserModel(String user_id, String user_name, String user_phone , String email , String image , String pass) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_phone = user_phone;
        this.email = email ;
        img = image ;
        this.pass = pass ;


    }

    public UserModel() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }





}
