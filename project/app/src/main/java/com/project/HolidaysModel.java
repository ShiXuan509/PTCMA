package com.project;

public class HolidaysModel {

    String studentName;
    String reason;
    String date;
    String id;

    public HolidaysModel() {
    }

    public HolidaysModel(String studentName, String reason, String date, String id) {
        this.studentName = studentName;
        this.reason = reason;
        this.date = date;
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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
