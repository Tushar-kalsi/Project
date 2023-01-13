package com.tushar.project;

public class StudentModel {
    public static final int QIP_MODEL=1;
    public static final int NONQIP_MODEL=2;

    private int type;
    private int id;
    private String EN , Username , LastName , FirstName , DepartmentNumber , FatherName;
    private String Address;
    private String DOB;

    public String getAddress() {
        return Address;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEN(String EN) {
        this.EN = EN;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setDepartmentNumber(String departmentNumber) {
        DepartmentNumber = departmentNumber;
    }

    public void setFatherName(String fatherName) {
        FatherName = fatherName;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getDOB() {
        return DOB;
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getEN() {
        return EN;
    }

    public String getUsername() {
        return Username;
    }

    public String getLastName() {
        return LastName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getDepartmentNumber() {
        return DepartmentNumber;
    }

    public String getFatherName() {
        return FatherName;
    }


}
