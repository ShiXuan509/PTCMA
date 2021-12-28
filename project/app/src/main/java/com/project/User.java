package com.project;

public class User {
    String name,title, email, password, uid;

    User(){

    }

    public User(String name, String title, String email, String password, String uid) {
        this.name = name;
        this.title = title;
        this.email = email;
        this.password = password;
        this.uid = uid;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        this.title = title;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

