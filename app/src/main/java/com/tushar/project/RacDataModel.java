package com.tushar.project;

import java.io.Serializable;

public class RacDataModel implements Serializable {


    String name;
    String enrollment_number;
    String dorDate;
    String batch;
    String supervisor;
    String coSuperVisor;
    String documentLink;
    int hodDoc;
    String hodDocUrl;
    String departmentName;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnrollment_number() {
        return enrollment_number;
    }

    public void setEnrollment_number(String enrollment_number) {
        this.enrollment_number = enrollment_number;
    }

    public String getDorDate() {
        return dorDate;
    }

    public void setDorDate(String dorDate) {
        this.dorDate = dorDate;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getCoSuperVisor() {
        return coSuperVisor;
    }

    public void setCoSuperVisor(String coSuperVisor) {
        this.coSuperVisor = coSuperVisor;
    }

    public String getDocumentLink() {
        return documentLink;
    }

    public void setDocumentLink(String documentLink) {
        this.documentLink = documentLink;
    }

    public int getHodDoc() {
        return hodDoc;
    }

    public void setHodDoc(int hodDoc) {
        this.hodDoc = hodDoc;
    }

    public String getHodDocUrl() {
        return hodDocUrl;
    }

    public void setHodDocUrl(String hodDocUrl) {
        this.hodDocUrl = hodDocUrl;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
