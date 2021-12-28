package com.project;

public class ResultModel {

    String text;
    String image;
    String teacherName;
    String date;
    String id;

    public ResultModel() {
    }

    public ResultModel(String text, String image, String teacherName, String date, String id) {
        this.text = text;
        this.image = image;
        this.teacherName = teacherName;
        this.date = date;
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
