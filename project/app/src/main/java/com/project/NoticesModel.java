package com.project;

public class NoticesModel {
    String notices;
    String teacherName2;
    String date2;
    String id;

    public NoticesModel() {
    }

    public NoticesModel(String notices, String teacherName2, String date2, String id) {
        this.notices = notices;
        this.teacherName2 = teacherName2;
        this.date2 = date2;
        this.id = id;
    }

    public String getNotices() {
        return notices;
    }

    public void setNotices(String notices) {
        this.notices = notices;
    }

    public String getTeacherName2() {
        return teacherName2;
    }

    public void setTeacherName2(String teacherName2) {
        this.teacherName2 = teacherName2;
    }

    public String getDate2() {
        return date2;
    }

    public void setDate2(String date2) {
        this.date2 = date2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
