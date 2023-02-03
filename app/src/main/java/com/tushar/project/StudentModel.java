package com.tushar.project;

public class StudentModel {
    public static final int QIP_MODEL=1;
    public static final int NONQIP_MODEL=2;
    public static final int RACMODEL=3;
    public static final int COURSEWORK=4;
    public static final int PUBLICARION=5;


    String marksheetUrl , rdcUrl , pdlUrl ,thesisub, thesisawa, synopsis;
    private int type;
    private String journal;
    private String dop;
    private String journal_type;
    private String title;


    public String getMarksheetUrl() {
        return marksheetUrl;
    }

    public void setMarksheetUrl(String marksheetUrl) {
        this.marksheetUrl = marksheetUrl;
    }

    public String getRdcUrl() {
        return rdcUrl;
    }

    public void setRdcUrl(String rdcUrl) {
        this.rdcUrl = rdcUrl;
    }

    public String getPdlUrl() {
        return pdlUrl;
    }

    public void setPdlUrl(String pdlUrl) {
        this.pdlUrl = pdlUrl;
    }

    public String getThesisub() {
        return thesisub;
    }

    public void setThesisub(String thesisub) {
        this.thesisub = thesisub;
    }

    public String getThesisawa() {
        return thesisawa;
    }

    public void setThesisawa(String thesisawa) {
        this.thesisawa = thesisawa;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getDop() {
        return dop;
    }

    public void setDop(String dop) {
        this.dop = dop;
    }

    public String getJournal_type() {
        return journal_type;
    }

    public void setJournal_type(String journal_type) {
        this.journal_type = journal_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private int id;
    private String fullName;
    private String subjecCode;
    private String subjectName;

    private String EN , Username , LastName , FirstName , DepartmentNumber , FatherName;
    private String Address;
    private String DOB;
    private String DOR;
    private String batch;
    private String SuperVisor;
    private String coSuperVisor;
    private String document;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDOR() {
        return DOR;
    }

    public void setDOR(String DOR) {
        this.DOR = DOR;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getSuperVisor() {
        return SuperVisor;
    }

    public void setSuperVisor(String superVisor) {
        SuperVisor = superVisor;
    }

    public String getCoSuperVisor() {
        return coSuperVisor;
    }

    public void setCoSuperVisor(String coSuperVisor) {
        this.coSuperVisor = coSuperVisor;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

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

    public String getSubjecCode() {
        return subjecCode;
    }

    public void setSubjecCode(String subjecCode) {
        this.subjecCode = subjecCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
