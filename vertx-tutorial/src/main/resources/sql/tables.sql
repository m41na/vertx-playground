CREATE TABLE IF NOT EXISTS `tbl_location` (
  `location_id` int(11) NOT NULL AUTO_INCREMENT,
  `loc_city` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'chicago',
  `loc_state` varchar(2) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'IL',
  `loc_zip` varchar(5) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `country_code` varchar(2) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'US',
  PRIMARY KEY (`location_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
