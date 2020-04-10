INSERT INTO public.office (office_id, name, street, province_id, phone, email, street_number) VALUES (1, 'TEst', 'tt', 1, '1', 'test@test.com', 11);
INSERT INTO public.staff (staff_id, office_id, first_name, surname, phone, email, registration_number) VALUES (1, 1, 'asd', 'asda', '123', 'asda', 1231);
INSERT INTO public.staff (staff_id, office_id, first_name, surname, phone, email, registration_number) VALUES (2, 1, 'dasda', 'asd', '12', 'sad', 123);
INSERT INTO public.system_country (country_id, name) VALUES ('AR', 'Argentina');
INSERT INTO public.system_province (province_id, country_id, name) VALUES (1, 'AR', 'Buenos Aires');
