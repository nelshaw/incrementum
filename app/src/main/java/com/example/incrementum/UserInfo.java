package com.example.incrementum;

import android.app.Application;

public class UserInfo  extends Application {

    private String email;
    private String userName;
    private String userId;
    private String habitId;


    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHabitId() {
        return habitId;
    }

    public void setHabitId(String habitId) {
        this.habitId = habitId;
    }

    public void clear(){
        setUserId("");
        setEmail("");
        setUserName("");
        setHabitId("");
    }
}
