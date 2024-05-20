package com.medxpert.medxpertclient.patient;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface PatientRepository extends CrudRepository<Patient, Integer> {
    public Patient findByPatientEmailAndPatientPassword(String email, String password);
    public Patient findByPatientEmail(String email);

    @Transactional
    @Override
    void deleteById(Integer integer);
}
