package com.medxpert.medxpertclient.doctor;

import com.medxpert.medxpertclient.patient.Patient;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DoctorRepository extends CrudRepository<Doctor, Integer> {
    public Doctor findByDocUsernameAndDocPassword(String username, String password);
    public Doctor findByDocUsername(String username);
    List<Doctor> findAll();
}
