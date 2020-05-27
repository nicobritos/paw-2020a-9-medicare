alter table system_country
    add constraint check_country_length check (length(country_id) = 2 and country_id ~ '[A-Z]+');
