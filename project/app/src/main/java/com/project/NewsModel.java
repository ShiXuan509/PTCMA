package com.project;

public class NewsModel {
    String news;
    String teacherName1;
    String date1;
    String id;

    public NewsModel() {
    }

    public NewsModel(String news, String teacherName1, String date1, String id) {
        this.news = news;
        this.teacherName1 = teacherName1;
        this.date1 = date1;
        this.id = id;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public String getTeacherName1() {
        return teacherName1;
    }

    public void setTeacherName1(String teacherName1) {
        this.teacherName1 = teacherName1;
    }

    public String getDate1() {
        return date1;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
