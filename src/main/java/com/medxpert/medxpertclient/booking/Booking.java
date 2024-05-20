package com.medxpert.medxpertclient.booking;

import com.medxpert.medxpertclient.doctor.Doctor;
import com.medxpert.medxpertclient.patient.Patient;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer booking_id;

    private String careCenterType;

    private String bookingType;

    private Date bookingDate;

    private String bookingFacility;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    public Booking() {
    }

    public Booking(String careCenterType, String bookingType, Date bookingDate, String bookingFacility, Doctor doctor, Patient patient) {
        this.careCenterType = careCenterType;
        this.bookingType = bookingType;
        this.bookingDate = bookingDate;
        this.bookingFacility = bookingFacility;
        this.doctor = doctor;
        this.patient = patient;
    }

    public Integer getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(Integer booking_id) {
        this.booking_id = booking_id;
    }

    public String getCareCenterType() {
        return careCenterType;
    }

    public void setCareCenterType(String careCenterType) {
        this.careCenterType = careCenterType;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingFacility() {
        return bookingFacility;
    }

    public void setBookingFacility(String bookingFacility) {
        this.bookingFacility = bookingFacility;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
