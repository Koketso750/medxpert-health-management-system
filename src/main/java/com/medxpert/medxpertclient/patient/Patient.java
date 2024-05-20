package com.medxpert.medxpertclient.patient;

import jakarta.persistence.*;

@Entity
@Table(name="patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer patientId;

    private String patientName;

    private String patientSurname;

    private Integer patientAge;

    private String patientGender;

    private String idNumber;

    private String patientEmail;

    private String patientPassword;

    private String patientPicture;

    public Patient() {
    }

    public Patient(String patientName, String patientSurname, Integer patientAge, String patientGender, String idNumber, String patientEmail, String patientPassword, String patientPicture) {
        this.patientName = patientName;
        this.patientSurname = patientSurname;
        this.patientAge = patientAge;
        this.patientGender = patientGender;
        this.idNumber = idNumber;
        this.patientEmail = patientEmail;
        this.patientPassword = patientPassword;
        this.patientPicture = patientPicture;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientSurname() {
        return patientSurname;
    }

    public void setPatientSurname(String patientSurname) {
        this.patientSurname = patientSurname;
    }

    public Integer getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(Integer patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getPatientPassword() {
        return patientPassword;
    }

    public void setPatientPassword(String patientPassword) {
        this.patientPassword = patientPassword;
    }

    public String getPatientPicture() {
        return patientPicture;
    }

    public void setPatientPicture(String patientPicture) {
        this.patientPicture = patientPicture;
    }
}
