package com.medxpert.medxpertclient.patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    @Autowired private  PatientRepository patientRepository;

    public void registerPatient(Patient patient){
        patientRepository.save(patient);
    }

    public Patient getLoginUser(String email, String password){
        Patient patient = patientRepository.findByPatientEmailAndPatientPassword(email, password);

        return patient;
    }

    public Patient getUserAttibutesAfterLogIn(String email){
        Patient patient = patientRepository.findByPatientEmail(email);

        return patient;
    }
}
