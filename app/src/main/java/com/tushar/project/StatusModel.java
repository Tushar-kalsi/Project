package com.tushar.project;

public class StatusModel {
    String id;
    String name;
    String type;
    String username;
    String enrollment_number;
    int coursework, publication, rac;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEnrollment_number(String enrollment_number) {
        this.enrollment_number = enrollment_number;
    }

    public void setCoursework(int coursework) {
        this.coursework = coursework;
    }

    public void setPublication(int publication) {
        this.publication = publication;
    }

    public void setRac(int rac) {
        this.rac = rac;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public String getEnrollment_number() {
        return enrollment_number;
    }

    public int getCoursework() {
        return coursework;
    }

    public int getPublication() {
        return publication;
    }

    public int getRac() {
        return rac;
    }
}
