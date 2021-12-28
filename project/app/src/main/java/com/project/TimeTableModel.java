package com.project;

public class TimeTableModel {
    String className;
    String description;
    String image;
    String id;

    public TimeTableModel() {
    }


    public void setClassName(String className, String id) {

        this.className = className;
        this.id = id;
    }

    public String getClassName() {

        return className;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public String getImage() {

        return image;
    }

    public void setImage(String image) {

        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {

        this.id = id;
    }
}
