package com.medxpert.medxpertclient.doctor;

import jakarta.persistence.*;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer doc_id;

    private String docPracticeNumber;

    private String docNames;

    private String docSurname;

    private Integer docAge;

    private String docGender;

    private String idNumber;

    private String docUsername;

    private String docPassword;

    public Doctor() {
    }

    public Doctor(String docPracticeNumber, String docNames, String docSurname, Integer docAge, String docGender, String idNumber, String docUsername, String docPassword) {
        this.docPracticeNumber = docPracticeNumber;
        this.docNames = docNames;
        this.docSurname = docSurname;
        this.docAge = docAge;
        this.docGender = docGender;
        this.idNumber = idNumber;
        this.docUsername = docUsername;
        this.docPassword = docPassword;
    }

    public String getDocPracticeNumber() {
        return docPracticeNumber;
    }

    public void setDocPracticeNumber(String docPracticeNumber) {
        this.docPracticeNumber = docPracticeNumber;
    }

    public Integer getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(Integer doc_id) {
        this.doc_id = doc_id;
    }

    public String getDocNames() {
        return docNames;
    }

    public void setDocNames(String docNames) {
        this.docNames = docNames;
    }

    public String getDocSurname() {
        return docSurname;
    }

    public void setDocSurname(String docSurname) {
        this.docSurname = docSurname;
    }

    public Integer getDocAge() {
        return docAge;
    }

    public void setDocAge(Integer docAge) {
        this.docAge = docAge;
    }

    public String getDocGender() {
        return docGender;
    }

    public void setDocGender(String docGender) {
        this.docGender = docGender;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getDocUsername() {
        return docUsername;
    }

    public void setDocUsername(String docUsername) {
        this.docUsername = docUsername;
    }

    public String getDocPassword() {
        return docPassword;
    }

    public void setDocPassword(String docPassword) {
        this.docPassword = docPassword;
    }
}
