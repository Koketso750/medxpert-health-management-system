package com.medxpert.medxpertclient.medical.record;

import com.medxpert.medxpertclient.doctor.Doctor;
import com.medxpert.medxpertclient.patient.Patient;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

public interface MedicalRecordRepository extends CrudRepository<MedicalRecord, Integer> {
    List<MedicalRecord> findByPatient(Patient patient);
    List<MedicalRecord> findByDoctorAndTreatmentStatus(Doctor doctor, String treatmentStatus);

    @Modifying
    @Transactional
    @Query("UPDATE MedicalRecord mr SET mr.treatmentStatus = :treatmentStatus WHERE mr.recordId = :recordId AND mr.doctor = :doctor")
    void updateTreatmentStatusByRecordIdAndDoctor(Integer recordId, Doctor doctor, String treatmentStatus);
    List<MedicalRecord> findByPatientAndBookingBookingDateBetween(Patient patient, Timestamp startDate, Timestamp endDate);
    List<MedicalRecord> findByDoctorAndBookingBookingDateBetween(Doctor doctor, Timestamp timestamp, Timestamp timestamp1);
    List<MedicalRecord> findByDoctor(Doctor doctor);

    List<MedicalRecord> findByDoctorAndBookingBookingDateBetweenAndTreatmentStatus(Doctor doctor, Timestamp timestamp, Timestamp timestamp1, String treatmentPending);

    List<MedicalRecord> findByDoctorAndTreatmentStatusAndBookingBookingDateBetween(Doctor doctor, String pendingTreatmentStatus, Timestamp timestamp, Timestamp timestamp1);
}
