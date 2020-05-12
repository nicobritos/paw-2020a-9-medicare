select setval('appointment_appointment_id_seq', max(appointment_id))
from appointment;
select setval('office_office_id_seq', max(office_id))
from office;
select setval('patient_patient_id_seq', max(patient_id))
from patient;
select setval('picture_picture_id_seq', max(picture_id))
from picture;
select setval('staff_staff_id_seq', max(staff_id))
from staff;
select setval('system_province_province_id_seq', max(province_id))
from system_province;
select setval('system_locality_locality_id_seq', max(specialty_id))
from system_staff_specialty;
select setval('users_users_id_seq', max(users_id))
from users;
select setval('workday_workday_id_seq', max(workday_id))
from workday;
