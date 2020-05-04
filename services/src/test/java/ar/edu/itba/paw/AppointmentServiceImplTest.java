package ar.edu.itba.paw;

import ar.edu.itba.paw.interfaces.daos.AppointmentDao;
import ar.edu.itba.paw.interfaces.services.exceptions.InvalidAppointmentStatusChangeException;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.AppointmentServiceImpl;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class AppointmentServiceImplTest {

    private static final String FIRST_NAME = "Firstname";
    private static final String SURNAME = "Surname";
    private static final String PASSWORD = new BCryptPasswordEncoder().encode("Password");
    private static final String EMAIL = "fsurname@testmail.com";
    private static final int USER_ID = 1;
    private static final int OFFICE_ID = 1;
    private static final String LOCALITY = "Localidad";
    private static final int LOCALITY_ID = 1;
    private static final String STREET = "Calle 123";
    private static final String PHONE = "1112345678";
    private static final String OFFICE_EMAIL = "oficina@testmail.com";
    private static final String OFFICE_NAME = "Oficina";
    private static final String URL = "www.oficinahospital.com";
    private static final int PATIENT_ID = 1;
    private static final int YEAR = 2020;
    private static final int MONTH = 1;
    private static final int DAY_OF_MONTH = 29;
    private static final String STAFF_PHONE = "1111223344";
    private static final String STAFF_EMAIL = "staffemail@testmail.com";
    private static final String STAFF_SURNAME = "StSurname";
    private static final String STAFF_FIRSTNAME = "StFirstname";
    private static final int REGISTRATION_NUMBER = 1;
    private static final int STAFF_ID = 1;
    private static final int APPOINTMENT_ID = 1;
    @InjectMocks
    private AppointmentServiceImpl appointmentService = new AppointmentServiceImpl();

    @Mock
    private AppointmentDao appointmentDao;

    @Test
    public void changeToPossibleStatusOfPendingAppointmentTest(){
        Appointment appointment = appointmentModel();
        AppointmentStatus status = AppointmentStatus.PENDING;
        AppointmentStatus changeToStatus = AppointmentStatus.CANCELLED;
        try{
            appointment.setAppointmentStatus(status.name());
            appointmentService.setStatus(appointment, changeToStatus);

            changeToStatus = AppointmentStatus.WAITING;
            appointment.setAppointmentStatus(AppointmentStatus.PENDING.name());
            appointmentService.setStatus(appointment, changeToStatus);
        }catch (Exception e){
            fail("Failed to set appointment to status:" + changeToStatus.name() + " because exception " + e.getClass().getName() + " was thrown with message:\n" + e.getMessage());
        }
    }

    @Test(expected = InvalidAppointmentStatusChangeException.class)
    public void changeToSeeingStatusOfPendingAppointmentTest(){
        Appointment appointment = appointmentModel();
        AppointmentStatus status = AppointmentStatus.PENDING;
        AppointmentStatus changeToStatus = AppointmentStatus.SEEN;
        appointment.setAppointmentStatus(status.name());
        appointmentService.setStatus(appointment, changeToStatus);
    }

    private User userModel(){
        User user = new User();
        user.setFirstName(FIRST_NAME);
        user.setSurname(SURNAME);
        user.setPassword(PASSWORD);
        user.setEmail(EMAIL);
        user.setId(USER_ID);
        return user;
    }

    private Locality localityModel(){
        Locality locality = new Locality();
        locality.setName(LOCALITY);
        locality.setId(LOCALITY_ID);
        return locality;
    }

    private Office officeModel(){
        Office office = new Office();
        office.setId(OFFICE_ID);
        office.setLocality(localityModel());
        office.setStreet(STREET);
        office.setPhone(PHONE);
        office.setEmail(OFFICE_EMAIL);
        office.setName(OFFICE_NAME);
        office.setUrl(URL);
        return office;
    }

    private Patient patientModel(){
        Patient patient = new Patient();
        patient.setUser(userModel());
        patient.setOffice(officeModel());
        patient.setId(PATIENT_ID);
        return patient;
    }

    private Staff staffModel(){
        Staff staff = new Staff();
        staff.setPhone(STAFF_PHONE);
        staff.setEmail(STAFF_EMAIL);
        staff.setSurname(STAFF_SURNAME);
        staff.setFirstName(STAFF_FIRSTNAME);
        staff.setRegistrationNumber(REGISTRATION_NUMBER);
        staff.setId(STAFF_ID);
        return staff;
    }

    private DateTime dateModel(){
        LocalDate localDate = LocalDate.of(YEAR, MONTH, DAY_OF_MONTH);
        return new DateTime(localDate);
    }

    private Appointment appointmentModel(){
        Appointment appointment = new Appointment();
        appointment.setFromDate(dateModel());
        appointment.setPatient(patientModel());
        appointment.setStaff(staffModel());
        appointment.setId(APPOINTMENT_ID);
        appointment.setAppointmentStatus(AppointmentStatus.PENDING.name());
        return appointment;
    }
}
