package com.medxpert.medxpertclient.booking;

import com.medxpert.medxpertclient.patient.Patient;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookingRepository extends CrudRepository<Booking, Integer> {

    List<Booking> findByPatient(Patient patient);
}
