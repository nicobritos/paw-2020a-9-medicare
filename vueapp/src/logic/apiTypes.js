class Country{
    id;
    name;
    /**
     * 
     * @param {Number} id 
     * @param {string} name 
     */
    constructor(id,name){
        this.id = id;
        this.name = name;
    }
}
class Province{
    id;
    name;
    country;
    /**
     * 
     * @param {Number} id 
     * @param {String} name 
     * @param {Country} country 
     */
    constructor(id,name,country){
        this.id = id;
        this.name = name;
        this.country = country;
    }
}
class Locality{
    id;
    name;
    province;
    /**
     * 
     * @param {Number} id 
     * @param {String} name 
     * @param {Province} province 
     */
    constructor(id,name,province){
        this.id = id;
        this.name = name;
        this.province = province;
    }
}
class User{
    id;
    email;
    firstName;
    surname;
    verified;
    phone;
    profilePictureId;
    /**
     * 
     * @param {Number} id 
     * @param {String} email 
     * @param {String} firstName 
     * @param {String} surname 
     * @param {Boolean} verified 
     * @param {String} phone 
     * @param {Number} profilePictureId 
     */
    constructor(id,email,firstName,surname,verified,phone,profilePictureId){
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.surname = surname;
        this.verified = verified;
        this.phone = phone;
        this.profilePictureId = profilePictureId;
    }
}
class UpdateUser {
    email;
    firstName;
    surname;
    phone;
    profilePictureId;
    /**
     * 
     * @param {String} email 
     * @param {String} firstName 
     * @param {String} surname 
     * @param {String} phone 
     * @param {Number} profilePictureId 
     */
    constructor(email,firstName,surname,phone,profilePictureId){
        this.email = email;
        this.firstName = firstName;
        this.surname = surname;
        this.phone = phone;
        this.profilePictureId = profilePictureId;
    }
}

class Office {
    id;
    phone;
    email;
    street;
    url;
    localityId;
    /**
     * 
     * @param {Number} id 
     * @param {String} phone 
     * @param {String} email 
     * @param {String} street 
     * @param {String} url 
     * @param {Number} localityId 
     */
    constructor(id,phone,email,street,url,localityId){
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.street = street;
        this.url = url;
        this.localityId = localityId;
    }
}

class Staff {
    id;
    phone;
    email;
    registrationNumber;
    user;
    office;
    staffSpecialtyIds;
    /**
     * 
     * @param {Number} id 
     * @param {String} phone 
     * @param {String} email 
     * @param {Number} registrationNumber 
     * @param {User} user 
     * @param {Office} office 
     * @param {[Numbers]} staffSpecialtyIds 
     */
    constructor(id,phone,email,registrationNumber,user,office,staffSpecialtyIds){
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.registrationNumber = registrationNumber;
        this.user = user;
        this.office = office;
        this.staffSpecialtyIds = staffSpecialtyIds;
    }
}
class StaffSpecialty {
    id;
    name;
    /**
     * 
     * @param {Number} id 
     * @param {String} name 
     */
    constructor(id,name){
        this.id = id;
        this.name = name;
    }
}

class UpdateStaff {
    phone;
    email;
    registrationNumber;
    staffSpecialtyIds;
    /**
     * 
     * @param {String} phone 
     * @param {String} email 
     * @param {Number} registrationNumber 
     * @param {[Number]} staffSpecialtyIds 
     */
    constructor(phone,email,registrationNumber,staffSpecialtyIds){
        this.phone = phone;
        this.email = email;
        this.registrationNumber = registrationNumber;
        this.staffSpecialtyIds = staffSpecialtyIds;
    }
}

class Patient {
    id;
    user;
    officeId;
    /**
     * 
     * @param {Number} id 
     * @param {User} user 
     * @param {Number} officeId 
     */
    constructor(id,user,officeId){
        this.id = id;
        this.user = user;
        this.officeId = officeId;
    }
}

class Workday {
    id;
    start;
    end;
    day;
    staffId;
    /**
     * 
     * @param {Number} id 
     * @param {{Hour:Number,Minute:Number}} start 
     * @param {{Hour:Number,Minute:Number}} end 
     * @param {"MONDAY"|"TUESDAY"|"WEDNESDAY"|"THURSDAY"|"FRIDAY"|"SATURDAY"|"SUNDAY"} day 
     * @param {Number} staffId 
     */
    constructor(id,start,end,day,staffId){
        this.id = id;
        this.start = start;
        this.end = end;
        this.day = day;
        this.staffId = staffId;
    }
}

class CreateWorkday {
    start;
    end;
    day;
    /**
     * 
     * @param {{Hour:Number,Minute:Number}} start 
     * @param {{Hour:Number,Minute:Number}} end 
     * @param {"MONDAY"|"TUESDAY"|"WEDNESDAY"|"THURSDAY"|"FRIDAY"|"SATURDAY"|"SUNDAY"} day 
     */
    constructor(start,end,day){
        this.start = start;
        this.end = end;
        this.day = day;
    }
}

class Appointment {
    id;
    status;
    date_from;
    message;
    motive;
    patientId;
    staffId;

    // TODO:check
    to;
    /**
     * 
     * @param {Number} id 
     * @param {"PENDING"|"COMPLETE"|"CANCELLED"|"WAITING"|"SEEN"} status 
     * @param {Number} date_from in epoch milliseconds
     * @param {String} message 
     * @param {String} motive 
     * @param {Number} patientId 
     * @param {Number} staffId 
     */
    constructor(id,status,date_from,message,motive,patientId,staffId){
        this.id = id;
        this.status = status;
        this.date_from = date_from;
        this.message = message;
        this.motive = motive;
        this.patientId = patientId;
        this.staffId = staffId;

        //TODO:check
        //basically add fifteen minutes in milliseconds
        this.to = date_from + 15 * 60 * 1000;
    }
}

class CreateAppointment {
    id;
    status;
    date_from;
    message;
    motive;
    staffId;
    /**
     * 
     * @param {Number} id 
     * @param {"PENDING"|"COMPLETE"|"CANCELLED"|"WAITING"|"SEEN"} status 
     * @param {Number} date_from in epoch milliseconds
     * @param {String} message 
     * @param {String} motive 
     * @param {Number} staffId 
     */
    constructor(id,status,date_from,message,motive,staffId){
        this.id = id;
        this.status = status;
        this.date_from = date_from;
        this.message = message;
        this.motive = motive;
        this.staffId = staffId;
    }
}

class AppointmentTimeslot{
    hour;
    minute;
    duration;
    /**
     * 
     * @param {Number} hour 
     * @param {Number} minute 
     * @param {Number} duration 
     */
    constructor(hour,minute,duration){
        this.hour = hour;
        this.minute = minute;
        this.duration = duration;
    }
     
}

class AppointmentTimeslotDate{
    date;
    timeslots;
    /**
     * 
     * @param {{day:Number,month:Number,year:Number}} date 
     * @param {AppointmentTimeslot} timeslots 
     */
    constructor(date,timeslots){
        this.date = date;
        this.timeslots = timeslots;
    }
}

AppointmentTimeslot;
AppointmentTimeslotDate;

class Error {
    code;
    message;
    /**
     * 
     * @param {Number} code 
     * @param {String} message 
     */
    constructor(code,message){
        this.code = code;
        this.message = message;
    }
}
export default {
    Country: Country,
    Province: Province,
    Locality: Locality,
    User: User,
    UpdateUser: UpdateUser,
    Office: Office,
    Staff: Staff,
    StaffSpecialty: StaffSpecialty,
    UpdateStaff : UpdateStaff,
    Patient: Patient,
    Workday: Workday,
    CreateWorkday: CreateWorkday,
    Appointment: Appointment,
    CreateAppointment: CreateAppointment,
    Error: Error
}