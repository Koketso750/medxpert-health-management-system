package com.medxpert.medxpertclient.doctor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    // Method to find a doctor by ID
    public Doctor findDoctorById(Integer doctorId) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);
        return optionalDoctor.orElseThrow(() -> new IllegalArgumentException("Doctor not found with id: " + doctorId));
    }

}

