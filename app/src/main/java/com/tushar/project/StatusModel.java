package com.tushar.project;

public class StatusModel {
    String id;
    String name;
    String type;
    String username;
    String enrollment_number;
    int coursework, publication, rac, title , marksheet ,rdc , predefenceLetter , thesisSubmission, thesisAwarded , synopsis;


    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getMarksheet() {
        return marksheet;
    }

    public void setMarksheet(int marksheet) {
        this.marksheet = marksheet;
    }

    public int getRdc() {
        return rdc;
    }

    public void setRdc(int rdc) {
        this.rdc = rdc;
    }

    public int getPredefenceLetter() {
        return predefenceLetter;
    }

    public void setPredefenceLetter(int predefenceLetter) {
        this.predefenceLetter = predefenceLetter;
    }

    public int getThesisSubmission() {
        return thesisSubmission;
    }

    public void setThesisSubmission(int thesisSubmission) {
        this.thesisSubmission = thesisSubmission;
    }

    public int getThesisAwarded() {
        return thesisAwarded;
    }

    public void setThesisAwarded(int thesisAwarded) {
        this.thesisAwarded = thesisAwarded;
    }

    public int getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(int synopsis) {
        this.synopsis = synopsis;
    }

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
