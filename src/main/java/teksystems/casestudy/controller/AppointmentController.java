package teksystems.casestudy.controller;
 
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import teksystems.casestudy.database.dao.AppointmentDAO;
import teksystems.casestudy.database.dao.ClinicianDAO;
import teksystems.casestudy.database.dao.PatientDAO;
import teksystems.casestudy.database.dao.UserDAO;
import teksystems.casestudy.database.entity.*;
import teksystems.casestudy.formbean.AppointmentEditorFormBean;
import teksystems.casestudy.formbean.PreAppointmentQuestionsFormBean;
import teksystems.casestudy.formbean.RegisterFormBean;
import teksystems.casestudy.formbean.SelectAppointmentScheduleFormBean;
import teksystems.casestudy.security.AuthenticationFacade;

import javax.validation.Valid;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Slf4j
@Controller
public class AppointmentController {

    private final String[] appointmentTimes = {"08:00", "08:30", "09:00", "09:30", "10:00",
            "10:30", "11:00", "11:30", "12:00", "01:00", "01:30",
            "02:00", "02:30", "03:00", "03:30", "04:00"};

    @Autowired
    private AppointmentDAO appointmentDao;

    @Autowired
    private AuthenticationFacade authentication;

    @Autowired
    private ClinicianDAO clinicianDao;

    @Autowired
    private PatientDAO patientDao;

    @Autowired
    private UserDAO userDao;



    @RequestMapping(value = "/user/schedule_appointment", method = RequestMethod.GET)
    public ModelAndView viewClinicianScheduleAsPatient(@RequestParam(required = false) Integer userId,
                                                       @RequestParam(required = false) String date) throws Exception {

        ModelAndView response = new ModelAndView();
        response.setViewName("user/schedule_appointment");

        if (userId != null) {
            log.info(userId.toString() + " THIS IS THE userID");
        } else {
            log.info("User Id is null");
        }

        Clinician clinician = (userId != null) ? clinicianDao.findByUserId(userId)
                : clinicianDao.findByClinicianId(4);

        User user = userDao.findByUserId(clinician.getUserId());

        Integer clinicianId = (userId != null) ? clinician.getClinicianId() : 4;

        //Date Formatting
        LocalDate currentDate = LocalDate.now();

        //if the inputted date is null, set the date to today's date
        LocalDate dateFormatted = (date != null) ?
                LocalDate.of(Integer.parseInt(date.split("-")[0]),
                Integer.parseInt(date.split("-")[1]),
                Integer.parseInt(date.split("-")[2])) : currentDate;

        //Getting variables to send to .jsp. With these Strings & Variables,
        // we can see: "Tuesday, April 19, 2022"
        String dayOfWeek = dateFormatted.getDayOfWeek().toString().substring(0, 1).toUpperCase() + dateFormatted.getDayOfWeek().toString().substring(1).toLowerCase();
        String monthName = dateFormatted.getMonth().toString().substring(0, 1).toUpperCase() + dateFormatted.getMonth().toString().substring(1).toLowerCase();
        Integer year = dateFormatted.getYear();
        String yearDate = year.toString();
        Integer dayDate = Integer.parseInt(dateFormatted.toString().split("-")[2]);

        List<Appointment> appointments = appointmentDao.findByClinicianClinicianIdAndDate(clinicianId, dateFormatted);

        Set<String> scheduledTime = new HashSet<>();

        for (Appointment appointment : appointments) {
            scheduledTime.add(appointment.getTime().toString());
        }

        List<Clinician> clins = clinicianDao.findAll();
        List<User> clinicianUsers = new ArrayList<>();

        for (Clinician clin : clins) {
            User clinUser = userDao.findByUserId(clin.getUserId());
            clinicianUsers.add(clinUser);
        }

        response.addObject("clinicianUsers", clinicianUsers);
        response.addObject("clinUser", user);
        response.addObject("scheduledTime", scheduledTime);
        response.addObject("localDate", dateFormatted);
        response.addObject("dayOfWeek", dayOfWeek);
        response.addObject("monthName", monthName);
        response.addObject("dayDate", dayDate);
        response.addObject("yearDate", yearDate);
        response.addObject("clinicianId", clinicianId);
        response.addObject("appointmentTimes", appointmentTimes);

        return response;
    }

    @PostMapping(value = "/user/schedule_appointmentSubmit")
    public ModelAndView appointmentSubmit(@Valid SelectAppointmentScheduleFormBean form) throws Exception {
        ModelAndView response = new ModelAndView();

        Appointment appointment = new Appointment();
        Clinician clinician = clinicianDao.findByUserId(form.getUserId());
        appointment.setClinician(clinician);

        //converting String date and String time to Date, Time objects
        appointment.setDate(LocalDate.parse(form.getDate()));
        appointment.setTime(LocalTime.parse(form.getTime()));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        if (!StringUtils.equals("anonymousUser", currentPrincipalName)) {
            appointmentDao.save(appointment);
            User user = userDao.findByEmail(currentPrincipalName);
            Patient patient = patientDao.findByUserId(user.getUserId());
            appointment.setPatient(patient);
            appointmentDao.save(appointment);
            log.info(user.toString());
            response.setViewName("redirect:/user/my_schedule/" + user.getUserId());
        }

        return response;
    }

//    @PreAuthorize("hasAuthority('CLINICIAN')")
//    @RequestMapping(value = "/clinician/my_clinician_schedule", method = RequestMethod.GET)
//    public ModelAndView navToMyClinicianSchedule() {
//        ModelAndView response = new ModelAndView();
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String currentPrincipalName = authentication.getName();
//
//        if(!StringUtils.equals("anonymousUser", currentPrincipalName)){
//            User user = userDao.findByEmail(currentPrincipalName);
//            response.setViewName("redirect:/clinician/my_clinician_schedule/" + user.getUserId());
//        }
//
//        return response;
//    }

    @PreAuthorize("hasAnyAuthority('CLINICIAN','PATIENT')")
    @RequestMapping(value = "/user/my_schedule", method = RequestMethod.GET)
    public ModelAndView navToMyAppointmentsAsPatient() {
        ModelAndView response = new ModelAndView();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        if(!StringUtils.equals("anonymousUser", currentPrincipalName)){
            User user = userDao.findByEmail(currentPrincipalName);
            response.setViewName("redirect:/user/my_schedule/" + user.getUserId());
        }

        return response;
    }

    @PreAuthorize("hasAnyAuthority('CLINICIAN','PATIENT')")
    @RequestMapping(value = "/user/my_schedule/{userId}", method = RequestMethod.GET)
    public ModelAndView viewMyAppointments(@PathVariable("userId") Integer userId) {
        ModelAndView response = new ModelAndView();
        response.setViewName("user/my_schedule");

        Integer patientId = patientDao.findByUserId(userId).getPatientId();
        List<Appointment> unsortedAppointments = appointmentDao.findByPatientPatientId(patientId);
        List<Appointment> timeSortedAppointments = timeSortAppointments(unsortedAppointments, patientId);

        // List<Appointment> appointmentsOrdered = new ArrayList<>();
        // log.info("=========== APPOINTMENTS ORDERED ===========");
        // log.info(appointmentsOrdered.toString() + "========");
        // log.info("=========== APPOINTMENTS ORDERED END ===========");
        timeSortedAppointments.sort((app1, app2)
                -> app1.getDate().compareTo(
                app2.getDate()));
        // log.info(appointments.toString());
        // log.info(userId.toString());

        List<Integer> daysOfMonth = new ArrayList<>();
        List<String> months = new ArrayList<>();
        List<Integer> years = new ArrayList<>();
        List<User> clinUsers = new ArrayList<>();

        for(Appointment appointment : timeSortedAppointments) {
            daysOfMonth.add(appointment.getDate().getDayOfMonth());
            months.add(appointment.getDate().getMonth().toString().substring(0,1).toUpperCase() + appointment.getDate().getMonth().toString().substring(1).toLowerCase());
            years.add(appointment.getDate().getYear());
            User clinUser = userDao.findByUserId(appointment.getClinician().getUserId());
            clinUsers.add(clinUser);
        }

        User user = userDao.findByUserId(userId);
        Patient patient = patientDao.findByUserId(userId);
        response.addObject("appointments", timeSortedAppointments);
        response.addObject("user", user);
        response.addObject("patient", patient);
        response.addObject("daysOfMonth", daysOfMonth);
        response.addObject("months", months);
        response.addObject("years", years);
        response.addObject("clinUsers", clinUsers);
        return response;
    }

    public List<Appointment> timeSortAppointments(List<Appointment> unsortedAppointments, Integer patientId) {
        List<Appointment> morningAppointments;
        List<Appointment> afternoonAppointments;
        List<LocalDate> localDates = new ArrayList<>();

        // create a list of dates included in appointments
        for(Appointment appointment : unsortedAppointments) {
            if(!localDates.contains(appointment.getDate())) {
                localDates.add(appointment.getDate());
            }
        }

        List<Appointment> sortedAppointments = new ArrayList<>();;
        for(LocalDate date : localDates) {
            morningAppointments = appointmentDao.findByDateAndTimeLessThanEqualAndTimeGreaterThanEqualAndPatientPatientId(date, LocalTime.of(12, 59), LocalTime.of(8,00), patientId);
            morningAppointments.sort((app1, app2) -> app1.getTime().compareTo(app2.getTime()));
            afternoonAppointments = appointmentDao.findByDateAndTimeLessThanEqualAndTimeGreaterThanEqualAndPatientPatientId(date, LocalTime.of(4, 00), LocalTime.of(1,00), patientId);
            afternoonAppointments.sort((app1, app2) -> app1.getTime().compareTo(app2.getTime()));

            for(Appointment mornApt : morningAppointments) {
                sortedAppointments.add(mornApt);
            }
            for(Appointment aftApt : afternoonAppointments) {
                sortedAppointments.add(aftApt);
            }
        }
        return sortedAppointments;
    }

    @PreAuthorize("hasAnyAuthority('CLINICIAN','PATIENT')")
    @RequestMapping(value = "/user/paq", method = RequestMethod.GET)
    public ModelAndView navigateToPaq(@RequestParam(required = false) Integer userId) {
        ModelAndView response = new ModelAndView();
        response.setViewName("user/paq");

        response.setViewName("redirect:/user/paq");
        return response;
    }

    @PreAuthorize("hasAuthority('CLINICIAN')")
    @RequestMapping(value = "/clinician/my_clinician_schedule/edit/{appointmentId}", method = RequestMethod.GET)
    public ModelAndView editAppointment(@PathVariable("appointmentId") Integer appointmentId) {
        ModelAndView response = new ModelAndView();
        response.setViewName("clinician/appointment_editor");
        //TODO FIX

        AppointmentEditorFormBean form = new AppointmentEditorFormBean();

        Appointment appointment = appointmentDao.getById(appointmentId);
        form.setDate(appointment.getDate().toString());
        form.setTime(appointment.getTime().toString());
        form.setClinicianId(appointment.getClinician().getClinicianId().toString());
        form.setPatientId(appointment.getPatient().getPatientId().toString());
        if(appointment.getChiefComplaint() != null) {
            form.setChiefComplaint(appointment.getChiefComplaint());
        }
        if(appointment.getPaqId() != null) {
            form.setPaqId(appointment.getPaqId().toString());
        }

        Integer userId = appointment.getPatient().getUserId();
        form.setUserId(userId);

        User thisClinicianUser = userDao.findByUserId(appointment.getClinician().getUserId());

        List<Clinician> allClinicians = clinicianDao.findAll();
        List<User> allClinUsers = new ArrayList<>();
        for (Clinician clinician : allClinicians) {
            allClinUsers.add(userDao.findByUserId(clinician.getUserId()));
        }

        // response.addObject("allClinicians", allClinicians);
        response.addObject("allClinUsers", allClinUsers);
        response.addObject("thisClinicianUser", thisClinicianUser);
        response.addObject("form", form);
        response.addObject("appointmentId", appointment.getAppointmentId());
        response.addObject("appointmentTimes", appointmentTimes);

        return response;
    }

//    @RequestMapping(value = "/ajaxAppointment", method = RequestMethod.GET)
//    public ResponseEntity<String> ajaxRequest(String timeDate) throws Exception {
//        String[] timeDateArray = timeDate.split("&");
//        String time = timeDateArray[0];
//        String date = timeDateArray[1];
//        LocalTime timeFormatted = LocalTime.parse(time); 
//        LocalDate dateFormatted = LocalDate.parse(date); 
//        List<Appointment> appointments = appointmentDao.findByDateAndTime(dateFormatted, timeFormatted);
//        appointments.stream().filter(apt -> apt.getPatient() == null);

//        List<Integer> availableClinicianIds = new ArrayList<>();
//        for (Appointment appt : appointments) {
//             if(!availableClinicianIds.contains(appt.getClinician().getClinicianId())){
//                 availableClinicianIds.add(appt.getClinician().getClinicianId());
//             }
//        }

//        String payload = new Gson().toJson(availableClinicianIds);
//        return new ResponseEntity(payload, HttpStatus.OK);
//    }

    //submit edits
    @PreAuthorize("hasAuthority('CLINICIAN')")
    @RequestMapping(value = "/clinician/my_clinician_scheduleSubmit/{appointmentId}", method = RequestMethod.POST)
    public ModelAndView submitAppointmentEdits(@PathVariable("appointmentId") Integer appointmentId,
                                          @Valid AppointmentEditorFormBean form,
                                           BindingResult bindingResult) {
        ModelAndView response = new ModelAndView();

        //check for errors in form
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = new ArrayList<>();

            for(ObjectError error : bindingResult.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
                log.info( ((FieldError) error).getField() + " " + error.getDefaultMessage());
            }

            response.addObject("form", form);

            response.addObject("bindingResult", bindingResult);

            response.setViewName("clinician/appointment_editor");
            return response;
        }

        //find appointment by id
        Appointment appointment = appointmentDao.findByAppointmentId(appointmentId);

        //convert date from String date to LocalDate date
        String[] dateArray = form.getDate().split("-");

        Integer year = Integer.parseInt(dateArray[0]);
        Integer month = Integer.parseInt(dateArray[1]);
        Integer day = Integer.parseInt(dateArray[2]);

        LocalDate dateFormatted = LocalDate.of(year, month, day);

        appointment.setDate(dateFormatted);

        //convert time from String to LocalTime time
        String[] timeArray = form.getTime().split(":");

        Integer hour = Integer.parseInt(timeArray[0]);
        Integer minute = Integer.parseInt(timeArray[1]);

        LocalTime timeFormatted = LocalTime.of(hour, minute);
        appointment.setTime(timeFormatted);

        Integer parsedClinicianUserId = form.getUserId();
        // Integer parsedClinicianUserId = Integer.parseInt(form.getUserId());
        Integer patientId = appointment.getPatient().getPatientId();

        //Retrieving a clinician based on the clinicianId entered in the form
        Clinician clinician = clinicianDao.findByUserId(parsedClinicianUserId);
        appointment.setClinician(clinician);

        //Retrieving a patient based on the patientId from the form
        Patient patient = patientDao.findByPatientId(patientId);
        appointment.setPatient(patient);
        log.info(patient.toString());

//        appointment.setChiefComplaint(form.getChiefComplaint());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        if(!StringUtils.equals("anonymousUser", currentPrincipalName)){
            appointmentDao.save(appointment);
            User user = userDao.findByEmail(currentPrincipalName);
            response.setViewName("redirect:/clinician/my_clinician_schedule");
        }

        return response;
    }

    @PreAuthorize("hasAuthority('CLINICIAN')")
    @RequestMapping(value = "/clinician/my_clinician_schedule/cancel/{appointmentId}", method = RequestMethod.POST)
    public ModelAndView clearAppointmentFields(@PathVariable("appointmentId") Integer appointmentId) {
        ModelAndView response = new ModelAndView();

        Appointment appointment = appointmentDao.findByAppointmentId(appointmentId);

        appointment.setClinician(null);
        appointment.setPatient(null);
        appointment.setPaqId(null);
        appointment.setChiefComplaint(null);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        if(!StringUtils.equals("anonymousUser", currentPrincipalName)){
            appointmentDao.save(appointment);
            User user = userDao.findByEmail(currentPrincipalName);
            response.setViewName("redirect:/clinician/my_clinician_schedule");
        }

        return response;
    }
}