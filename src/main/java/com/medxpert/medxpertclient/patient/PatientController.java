package com.medxpert.medxpertclient.patient;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.medxpert.medxpertclient.booking.Booking;
import com.medxpert.medxpertclient.booking.BookingRepository;
import com.medxpert.medxpertclient.doctor.Doctor;
import com.medxpert.medxpertclient.doctor.DoctorRepository;
import com.medxpert.medxpertclient.doctor.DoctorService;
import com.medxpert.medxpertclient.medical.record.MedicalRecord;
import com.medxpert.medxpertclient.medical.record.MedicalRecordRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorService doctorService;

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/users/new")
    public String goToRegister() {
        return "register_page";
    }

    @GetMapping("/login")
    public String goToLogin() {
        return "login";
    }

    @GetMapping("/Dashboard/Page")
    public String goToDashboard(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession();
        Patient patient = (Patient) session.getAttribute("patient");
        if (patient != null) {
            model.addAttribute("patient", patient);
            return "dashboard";
        } else {
            // Add a flash attribute to indicate that the user needs to log in
            redirectAttributes.addFlashAttribute("message", "You must log in to access this page.");

            // Redirect to the login page
            return "redirect:/login";
        }
    }

    @GetMapping("/dashboard")
    public String goDashboard(RedirectAttributes redirectAttributes, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        Patient patient = (Patient) session.getAttribute("patient");

        if (patient != null) {
            session.setAttribute("patient", patient);

            // Add user to the model attributes
            model.addAttribute("patient", patient);

            // Return the template for adding a book
            return "dashboard";
        } else {
            // Add a flash attribute to indicate that the user needs to log in
            redirectAttributes.addFlashAttribute("message", "You must log in to access this page.");

            // Redirect to the login page
            return "redirect:/login";
        }
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("name") String name,
                               @RequestParam("surname") String surname,
                               @RequestParam("age") Integer age,
                               @RequestParam("gender") String gender,
                               @RequestParam("id_number") String id_number,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               @RequestParam("profilePicture") String profilePicture,
                               RedirectAttributes redirectAttributes) throws IOException, URISyntaxException {
        // Create a new user
        Patient patient = new Patient(name, surname, age, gender, id_number, email, password, profilePicture);

        // Save the user to the database
        patientRepository.save(patient);

        // Compose the email content
        String subject = "Welcome to Medxpert Health Management System";
        String message = "<p>Dear " + name + ",</p>"
                + "<p>Welcome to Medxpert Health Management System!</p>"
                + "<p>Thank you for creating an account with us. Medxpert allows you to discover amazing health services and book medical appointments with ease.</p>"
                + "<p>Start managing your health journey today and experience the convenience of our platform.</p>"
                + "<p>Best regards,<br>The Medxpert Team</p>";

        // Send the email
        emailService.sendEmailWithLogo(email, subject, message);

        // Add a flash attribute to display a success message on the login page
        redirectAttributes.addFlashAttribute("message", "Successfully Registered! An email has been sent to your registered email address with further instructions.");

        // Redirect the user to the login page
        return "redirect:/login";
    }

    @PostMapping("/Dashboard/Page")
    public String loginUser(@RequestParam("email") String email,
                            @RequestParam("password") String password,
                            Model model, RedirectAttributes redirectAttributes,
                            HttpServletRequest request) {
        Patient loggedInUser = patientService.getLoginUser(email, password);
        if (loggedInUser != null) {
            Patient patient = patientService.getUserAttibutesAfterLogIn(email);

            // Start session
            HttpSession session = request.getSession(true);
            session.setAttribute("patient", patient);

            model.addAttribute("patient", patient);
            return "dashboard";
        } else {
            redirectAttributes.addFlashAttribute("message", "Invalid email or password");
            return "redirect:/login";
        }
    }

    @GetMapping("/add/booking")
    public String addBook(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession();

        // Retrieve user from session
        Patient patient = (Patient) session.getAttribute("patient");

        if (patient != null) {
            // Add user to the model attributes
            model.addAttribute("patient", patient);

            // Return the template for adding a book
            return "add_booking_form";
        } else {
            // Add a flash attribute to indicate that the user needs to log in
            redirectAttributes.addFlashAttribute("message", "You must log in to make a booking.");

            // Redirect to the login page
            return "redirect:/login";
        }
    }

    @PostMapping("/addbooking")
    public String makeBooking(@RequestParam("care_center_type") String careCenterType,
                              @RequestParam("booking_type") String bookingType,
                              @RequestParam("booking_date") String bookingDateStr,
                              @RequestParam("booking_facility") String bookingFacility,
                              @RequestParam("doctor") Integer doctorId,
                              HttpServletRequest request,
                              RedirectAttributes redirectAttributes) throws IOException, URISyntaxException {

        HttpSession session = request.getSession();

        // Retrieve user from session
        Patient patient = (Patient) session.getAttribute("patient");

        Doctor doctor = doctorService.findDoctorById(doctorId);

        // Parse bookingDate from String to Date
        Date bookingDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            bookingDate = sdf.parse(bookingDateStr);
        } catch (ParseException e) {
            e.printStackTrace(); // Handle the exception as per your application's error handling strategy
        }

        Booking booking = new Booking(careCenterType, bookingType, bookingDate, bookingFacility, doctor, patient);

        bookingRepository.save(booking);

        MedicalRecord medicalRecord = new MedicalRecord(booking, "Pending Treatment", doctor, patient);

        medicalRecordRepository.save(medicalRecord);

        // Send confirmation email to the patient
        String subject = "Booking Confirmation";
        String message = "<p>Dear " + patient.getPatientName() + ",</p>"
                + "<p>Your booking has been confirmed successfully.</p>"
                + "<p>Booking Details:</p>"
                + "<p>Care Center Type: " + careCenterType + "</p>"
                + "<p>Booking Type: " + bookingType + "</p>"
                + "<p>Booking Date: " + sdf.format(bookingDate) + "</p>"
                + "<p>Booking Facility: " + bookingFacility + "</p>"
                + "<p>Doctor: " + doctor.getDocNames() + "</p>"
                + "<p>Thank you for choosing Medxpert Health Management System.</p>"
                + "<p>Best regards,<br>The Medxpert Team</p>";

        emailService.sendEmailWithLogo(patient.getPatientEmail(), subject, message);

        redirectAttributes.addFlashAttribute("message", "Booking confirmed successfully!");

        return "redirect:/dashboard";
    }


    @GetMapping("/view/medical/records")
    public String getMedicalRecords(@RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                    @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                    HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession();
        Patient patient = (Patient) session.getAttribute("patient");

        if (patient != null) {
            List<MedicalRecord> medicalRecords;

            if (startDate != null && endDate != null) {
                // Convert LocalDate to Timestamp
                Timestamp startTimestamp = Timestamp.valueOf(startDate.atStartOfDay());
                Timestamp endTimestamp = Timestamp.valueOf(endDate.atStartOfDay().plusDays(1)); // Plus one day to include end date

                medicalRecords = medicalRecordRepository.findByPatientAndBookingBookingDateBetween(patient, startTimestamp, endTimestamp);
            } else {
                medicalRecords = medicalRecordRepository.findByPatient(patient);
            }

            model.addAttribute("medicalRecords", medicalRecords);
            model.addAttribute("patient", patient);

            return "medical_records_page";
        } else {
            redirectAttributes.addFlashAttribute("message", "You must log in to access this page.");
            return "redirect:/login";
        }
    }

    @GetMapping("/medical/record/{recordId}/download")
    public void downloadMedicalRecord(@PathVariable int recordId, HttpServletResponse response) throws DocumentException, IOException, DocumentException {
        Optional<MedicalRecord> optionalRecord = medicalRecordRepository.findById(recordId);
        if (optionalRecord.isPresent()) {
            MedicalRecord medicalRecord = optionalRecord.get();

            // Set response properties
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=MedicalRecord_" + recordId + ".pdf");

            // Create PDF document
            Document document = new Document();
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            document.add(new Paragraph("Medical Record Details"));
            document.add(new Paragraph("Record ID: " + medicalRecord.getRecordId()));
            document.add(new Paragraph("Treatment Status: " + medicalRecord.getTreatmentStatus()));
            document.add(new Paragraph("Record Date: " + medicalRecord.getBooking().getBookingDate()));
            document.add(new Paragraph("Doctor: " + medicalRecord.getDoctor().getDocNames()));
            document.add(new Paragraph("Patient: " + medicalRecord.getPatient().getPatientName()));
            document.close();
        }
    }

    @GetMapping("/doctor/login")
    public String goToDoctorLogin() {
        return "doctor_login";
    }

    @GetMapping("/view/doctors")
    public String goToDoctors(Model model) {
        List<Doctor> doctors = doctorRepository.findAll();
        model.addAttribute("doctors", doctors);
        return "doctors";
    }

    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        redirectAttributes.addFlashAttribute("message", "Logout Successful!");
        return "redirect:/login";
    }

    @GetMapping("/delete/account")
    public String deleteAccount(RedirectAttributes redirectAttributes, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Patient patient = (Patient) session.getAttribute("patient");
        if (patient != null) {
            try {
                // Delete patient from database
                patientRepository.deleteById(patient.getPatientId());

                // Invalidate session after deletion
                session.invalidate();

                redirectAttributes.addFlashAttribute("message", "Your account has been successfully deleted.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "An error occurred while deleting your account.");
                e.printStackTrace(); // Log or handle the exception as needed
            }
        }
        return "redirect:/login";
    }
}

