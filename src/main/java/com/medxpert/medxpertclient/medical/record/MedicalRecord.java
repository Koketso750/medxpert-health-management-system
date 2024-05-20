package com.medxpert.medxpertclient.medical.record;

import com.medxpert.medxpertclient.booking.Booking;
import com.medxpert.medxpertclient.doctor.Doctor;
import com.medxpert.medxpertclient.patient.Patient;
import jakarta.persistence.*;

@Entity
@Table(name = "medical_records")
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int recordId;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    private String treatmentStatus;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public MedicalRecord() {

    }

    public MedicalRecord(Booking booking, String treatmentStatus, Doctor doctor, Patient patient) {
        this.booking = booking;
        this.treatmentStatus = treatmentStatus;
        this.doctor = doctor;
        this.patient = patient;
    }

    // Getters and setters

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public String getTreatmentStatus() {
        return treatmentStatus;
    }

    public void setTreatmentStatus(String treatmentStatus) {
        this.treatmentStatus = treatmentStatus;
    }

}
