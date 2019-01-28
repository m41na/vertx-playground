CREATE TABLE IF NOT EXISTS tbl_location (
    location_id INT IDENTITY,
    loc_city VARCHAR(100),
    loc_state VARCHAR(2),
    loc_zip VARCHAR(5),
    country_code VARCHAR(2),
    primary key (location_id)
);
    