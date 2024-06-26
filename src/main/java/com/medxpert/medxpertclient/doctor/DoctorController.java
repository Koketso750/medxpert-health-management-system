package com.medxpert.medxpertclient.doctor;

import com.medxpert.medxpertclient.doctor.Doctor;
import com.medxpert.medxpertclient.doctor.DoctorRepository;
import com.medxpert.medxpertclient.medical.record.MedicalRecord;
import com.medxpert.medxpertclient.medical.record.MedicalRecordRepository;
import com.medxpert.medxpertclient.patient.EmailService;
import com.medxpert.medxpertclient.patient.Patient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class DoctorController {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping("/doctor_login")
    public String showDoctorLoginPage() {
        return "doctor_login";
    }

    @GetMapping("/Doctor/Dashboard/Page")
    public String doctorDashboardPage(@RequestParam(name = "startDate", required = false) LocalDate startDate,
                                      @RequestParam(name = "endDate", required = false) LocalDate endDate,
                                      @RequestParam(name = "recordId", required = false) Integer recordId,
                                      HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession();
        Doctor doctor = (Doctor) session.getAttribute("doctor");

        if (doctor == null) {
            redirectAttributes.addFlashAttribute("message", "You need to login first!");
            return "redirect:/doctor_login";
        }

        List<MedicalRecord> medicalRecords;
        String pendingTreatmentStatus = "Pending Treatment";

        if (recordId != null) {
            // Search by Record ID
            MedicalRecord medicalRecord = medicalRecordRepository.findById(recordId).orElse(null);
            if (medicalRecord != null) {
                medicalRecords = Collections.singletonList(medicalRecord);
            } else {
                medicalRecords = Collections.emptyList();
                model.addAttribute("message", "No medical record found for ID: " + recordId);
            }
        } else if (startDate != null && endDate != null) {
            LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.MIN);
            LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MAX);
            medicalRecords = medicalRecordRepository.findByDoctorAndTreatmentStatusAndBookingBookingDateBetween(
                    doctor, pendingTreatmentStatus, Timestamp.valueOf(startDateTime), Timestamp.valueOf(endDateTime));
        } else {
            medicalRecords = medicalRecordRepository.findByDoctorAndTreatmentStatus(doctor, pendingTreatmentStatus);
        }

        model.addAttribute("doctor", doctor);
        model.addAttribute("medicalRecords", medicalRecords);

        return "DoctorDashboard";
    }




    @PostMapping("/Doctor/Dashboard/Page")
    public String handleDoctorLogin(@RequestParam("username") String username,
                                    @RequestParam("password") String password,
                                    HttpServletRequest request,
                                    RedirectAttributes redirectAttributes) {
        Optional<Doctor> doctorOptional = Optional.ofNullable(doctorRepository.findByDocUsernameAndDocPassword(username, password));

        if (doctorOptional.isPresent()) {
            Doctor doctor = doctorOptional.get();
            HttpSession session = request.getSession();
            session.setAttribute("doctor", doctor);
            return "redirect:/Doctor/Dashboard/Page";
        } else {
            redirectAttributes.addFlashAttribute("message", "Invalid credentials. Please try again.");
            return "redirect:/doctor_login";
        }
    }

    @PostMapping("/treat/patient")
    public String treatPatient(@RequestParam("recordId") Integer recordId,
                               RedirectAttributes redirectAttributes,
                               HttpServletRequest request) throws IOException, URISyntaxException {

        HttpSession session = request.getSession();
        Doctor doctor = (Doctor) session.getAttribute("doctor");

        if (doctor != null) {

            // Update treatment status
            medicalRecordRepository.updateTreatmentStatusByRecordIdAndDoctor(recordId, doctor, "Treatment Successful");

            // Retrieve patient associated with the medical record
            Optional<MedicalRecord> optionalMedicalRecord = medicalRecordRepository.findById(recordId);
            if (optionalMedicalRecord.isPresent()) {
                MedicalRecord medicalRecord = optionalMedicalRecord.get();
                Patient patient = medicalRecord.getPatient();

                // Compose the treatment update email content
                String subject = "Treatment Update";
                String message = "<p>Dear " + patient.getPatientName() + ",</p>"
                        + "<p>We are pleased to inform you that your treatment has been successfully completed.</p>"
                        + "<p>Please contact us if you have any questions or concerns.</p>"
                        + "<p>Best regards,<br>The Medxpert Team</p>"
                        + "<footer>"
                        + "<img src=\"cid:logo\" style=\"width: 100px; height: auto;\" alt=\"MedXpert Logo\">"
                        + "<div class=\"col-lg-6\">"
                        + "<p>Follow Us or Chat With Us:</p>"
                        + "<a href=\"https://wa.me/+27655109157\" class=\"hoverable\"><img src=\"cid:whatsapp_icon\" style=\"width: 50px; height: 50px\" alt=\"whatsapp\"></a>"
                        + "<a href=\"https://web.facebook.com/koketso.prince.65510\" class=\"hoverable\"><img src=\"cid:facebook_icon\" style=\"width: 50px; height: 50px\" alt=\"facebook\"></a>"
                        + "<a href=\"https://www.instagram.com/koketsomokgoko_ult/?hl=en\" class=\"hoverable\"><img src=\"cid:instagram_icon\" style=\"width: 50px; height: 50px\" alt=\"Instagram\"></a>"
                        + "</div>"
                        + "</footer>";

                // Send the email with inline images
                emailService.sendEmailWithLogo(patient.getPatientEmail(), subject, message);

            }

            redirectAttributes.addFlashAttribute("message", "Treatment Successful!");
            return "redirect:/Doctor/Dashboard/Page";

        } else {
            redirectAttributes.addFlashAttribute("message", "You need to login to access this page.");
            return "redirect:/doctor_login";
        }
    }

    @GetMapping("/doctor/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("message", "Logout Successful!");
        return "redirect:/doctor_login";
    }
}
