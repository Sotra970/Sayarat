package com.sayarat.app;

import com.sayarat.Models.UserModel;

import java.io.Serializable;

/**
 * Created by sotra on 3/2/2017.
 */
public class CommentModel implements Serializable{
    String comment ,date  ;
    UserModel user ;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
