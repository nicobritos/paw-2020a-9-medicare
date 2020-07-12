class Country{
    id;
    name;
    constructor(id,name){
        this.id = id;
        this.name = name;
    }
}
class Province{
    id;
    name;
    country;
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
    constructor(start,end,day){
        this.start = start;
        this.end = end;
        this.day = day;
    }
}

class Appointment {
    id;
    status;
    from;
    message;
    motive;
    patientId;
    staffId;

    // TODO:check
    to;
    constructor(id,status,from,message,motive,patientId,staffId){
        this.id = id;
        this.status = status;
        this.from = from;
        this.message = message;
        this.motive = motive;
        this.patientId = patientId;
        this.staffId = staffId;
        // TODO:check
        this.to = new Date(from.valueof());
        this.to.setMinute(this.to.getMinute()+15);
    }
}

class CreateAppointment {
    id;
    status;
    from;
    message;
    motive;
    staffId;
    constructor(id,status,from,message,motive,staffId){
        this.id = id;
        this.status = status;
        this.from = from;
        this.message = message;
        this.motive = motive;
        this.staffId = staffId;
    }
}

class Error {
    code;
    message;
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