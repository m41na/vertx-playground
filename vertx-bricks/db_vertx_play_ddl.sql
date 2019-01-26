CREATE TABLE `tbl_account` (
  `fk_player_id` int(11) NOT NULL,
  `username` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `pass_code` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL,
  `acc_status` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending',
  `acc_role` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'guest',
  `phone_num` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mfa_token` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mfa_method` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'email',
  `rec_token` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`fk_player_id`,`username`),
  UNIQUE KEY `tbl_acc_username_UN` (`username`),
  CONSTRAINT `tbl_account_tbl_player_FK` FOREIGN KEY (`fk_player_id`) REFERENCES `tbl_player` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `tbl_app_event` (
  `event_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `event_origin` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `event_dest` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `event_payload` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `event_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `source_id` int(11) DEFAULT NULL COMMENT 'value of id for record which event may be associted with',
  `source_ref` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'name of table_and_column that source_id is associated with'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `tbl_enum_col` (
  `column_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `enum_value` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `value_descr` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  UNIQUE KEY `tbl_enum_col_UN` (`column_name`,`enum_value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `tbl_game` (
  `game_id` int(11) NOT NULL AUTO_INCREMENT,
  `game_title` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `organizer` int(11) NOT NULL,
  `play_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `game_status` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'construction',
  `game_scope` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT 'public',
  PRIMARY KEY (`game_id`),
  KEY `tbl_game_tbl_player_FK` (`organizer`),
  CONSTRAINT `tbl_game_tbl_player_FK` FOREIGN KEY (`organizer`) REFERENCES `tbl_player` (`player_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `tbl_game_content` (
  `fk_game_id` int(11) NOT NULL,
  `fk_que_id` int(11) NOT NULL,
  `fk_decor_id` int(11) NOT NULL,
  `content_group` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'home-run',
  `show_duration` int(5) NOT NULL DEFAULT '10000' COMMENT 'length of time in milliseconds to display content',
  `tick_interval` int(4) NOT NULL DEFAULT '250' COMMENT 'clock interval in milliseconds',
  `max_points` int(4) NOT NULL DEFAULT '1000' COMMENT 'max assigned points associated with fk_que',
  `alt_content` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'if present, this value is used instead of the content from fk_que',
  `delay_period` int(4) NOT NULL DEFAULT '5000' COMMENT 'duration before countdown begins, if scheduled',
  UNIQUE KEY `tbl_game_question_UN` (`fk_game_id`,`fk_que_id`),
  KEY `tbl_game_content_tbl_game_phase_FK` (`fk_decor_id`),
  KEY `tbl_game_content_tbl_question_FK` (`fk_que_id`),
  CONSTRAINT `tbl_game_content_tbl_game_FK` FOREIGN KEY (`fk_game_id`) REFERENCES `tbl_game` (`game_id`),
  CONSTRAINT `tbl_game_content_tbl_game_phase_FK` FOREIGN KEY (`fk_decor_id`) REFERENCES `tbl_que_decor` (`decor_id`),
  CONSTRAINT `tbl_game_content_tbl_question_FK` FOREIGN KEY (`fk_que_id`) REFERENCES `tbl_question` (`que_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `tbl_guardian` (
  `fk_guardian_id` int(11) NOT NULL,
  `fk_player_id` int(11) NOT NULL,
  `guardian_dob` date NOT NULL,
  `player_dob` date DEFAULT NULL,
  `access_code` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL,
  `setup_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY `tbl_guardian_tbl_player_FK` (`fk_guardian_id`),
  KEY `tbl_guardian_tbl_player_FK_1` (`fk_player_id`),
  CONSTRAINT `tbl_guardian_tbl_player_FK` FOREIGN KEY (`fk_guardian_id`) REFERENCES `tbl_player` (`player_id`),
  CONSTRAINT `tbl_guardian_tbl_player_FK_1` FOREIGN KEY (`fk_player_id`) REFERENCES `tbl_player` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `tbl_location` (
  `location_id` int(11) NOT NULL AUTO_INCREMENT,
  `loc_city` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'chicago',
  `loc_state` varchar(2) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'IL',
  `loc_zip` varchar(5) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `country_code` varchar(2) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'US',
  PRIMARY KEY (`location_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `tbl_msg_confirm_signup` (
  `player_email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `msg_body` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL,
  `send_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `confirm_status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'sent',
  KEY `tbl_msg_confirm_signup_tbl_player_FK` (`player_email`),
  CONSTRAINT `tbl_msg_confirm_signup_tbl_player_FK` FOREIGN KEY (`player_email`) REFERENCES `tbl_player` (`email_addr`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `tbl_msg_invite_to_play` (
  `invite_from` int(11) NOT NULL,
  `invitee_email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fk_game_title` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `invite_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `msg_body` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL,
  `invite_status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending',
  KEY `tbl_msg_game_invite_tbl_team_FK` (`fk_game_title`),
  KEY `tbl_msg_invite_to_play_tbl_player_FK` (`invite_from`),
  CONSTRAINT `tbl_msg_game_invite_tbl_team_FK` FOREIGN KEY (`fk_game_title`) REFERENCES `tbl_team` (`team_title`),
  CONSTRAINT `tbl_msg_invite_to_play_tbl_player_FK` FOREIGN KEY (`invite_from`) REFERENCES `tbl_player` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `tbl_msg_join_a_team` (
  `fk_player_id` int(11) NOT NULL,
  `fk_team_title` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `request_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `accepted_on` datetime DEFAULT NULL,
  `teamup_status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'sent',
  KEY `tbl_msg_teamup_request_tbl_team_FK` (`fk_team_title`),
  KEY `tbl_msg_join_a_team_tbl_player_FK` (`fk_player_id`),
  CONSTRAINT `tbl_msg_join_a_team_tbl_player_FK` FOREIGN KEY (`fk_player_id`) REFERENCES `tbl_player` (`player_id`),
  CONSTRAINT `tbl_msg_teamup_request_tbl_team_FK` FOREIGN KEY (`fk_team_title`) REFERENCES `tbl_team` (`team_title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `tbl_participant` (
  `fk_player_id` int(11) NOT NULL,
  `fk_game_id` int(11) NOT NULL,
  `screen_name` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fk_team_title` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fk_location_id` int(11) DEFAULT NULL,
  KEY `tbl_participants_tbl_location_FK` (`fk_location_id`),
  KEY `tbl_participants_tbl_game_FK` (`fk_game_id`),
  KEY `tbl_participants_tbl_team_FK` (`fk_team_title`),
  KEY `tbl_participant_tbl_player_FK` (`fk_player_id`),
  CONSTRAINT `tbl_participant_tbl_player_FK` FOREIGN KEY (`fk_player_id`) REFERENCES `tbl_player` (`player_id`),
  CONSTRAINT `tbl_participants_tbl_game_FK` FOREIGN KEY (`fk_game_id`) REFERENCES `tbl_game` (`game_id`),
  CONSTRAINT `tbl_participants_tbl_location_FK` FOREIGN KEY (`fk_location_id`) REFERENCES `tbl_location` (`location_id`),
  CONSTRAINT `tbl_participants_tbl_team_FK` FOREIGN KEY (`fk_team_title`) REFERENCES `tbl_team` (`team_title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `tbl_player` (
  `email_addr` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `first_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_ name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `date_joined` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `wow_points` int(11) NOT NULL DEFAULT '0',
  `player_location` int(11) DEFAULT NULL,
  `player_id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`player_id`),
  UNIQUE KEY `tbl_player_email_UN` (`email_addr`),
  KEY `tbl_player_tbl_location_FK` (`player_location`),
  CONSTRAINT `tbl_player_tbl_location_FK` FOREIGN KEY (`player_location`) REFERENCES `tbl_location` (`location_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `tbl_que_decor` (
  `decor_id` int(11) NOT NULL AUTO_INCREMENT,
  `decor_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `use_choices` tinyint(1) NOT NULL DEFAULT '1',
  `use_clues` tinyint(1) NOT NULL DEFAULT '0',
  `use_countdown` tinyint(1) NOT NULL DEFAULT '1',
  `use_matches` tinyint(1) NOT NULL DEFAULT '0',
  `is_numeric_ans` tinyint(1) NOT NULL DEFAULT '0',
  `numeric_range` tinyint(1) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`decor_id`),
  UNIQUE KEY `tbl_game_phase_UN` (`decor_name`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `tbl_question` (
  `que_id` int(11) NOT NULL AUTO_INCREMENT,
  `que_text` varchar(127) COLLATE utf8mb4_unicode_ci NOT NULL,
  `choice_1` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `choice_2` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `choice_3` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `choice_4` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `clue_1` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `clue_2` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `clue_3` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `clue_4` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `clue_5` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `answer` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ans_details` varchar(127) COLLATE utf8mb4_unicode_ci NOT NULL,
  `asked_by` int(11) NOT NULL,
  `date_asked` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `match_1` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `match_2` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `match_3` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `match_4` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `match_5` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `choice_5` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`que_id`),
  KEY `tbl_question_tbl_player_FK` (`asked_by`),
  CONSTRAINT `tbl_question_tbl_player_FK` FOREIGN KEY (`asked_by`) REFERENCES `tbl_player` (`player_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `tbl_score_sheet` (
  `fk_player_id` int(11) NOT NULL,
  `fk_game_id` int(11) NOT NULL,
  `fk_que_id` int(11) NOT NULL,
  `points_score` int(4) NOT NULL,
  `millis_left` int(4) DEFAULT NULL,
  `submit_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY `tbl_score_sheet_tbl_game_content_FK` (`fk_game_id`,`fk_que_id`),
  KEY `tbl_score_sheet_tbl_player_FK` (`fk_player_id`),
  CONSTRAINT `tbl_score_sheet_tbl_game_content_FK` FOREIGN KEY (`fk_game_id`, `fk_que_id`) REFERENCES `tbl_game_content` (`fk_game_id`, `fk_que_id`),
  CONSTRAINT `tbl_score_sheet_tbl_player_FK` FOREIGN KEY (`fk_player_id`) REFERENCES `tbl_player` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `tbl_team` (
  `team_title` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `date_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `captain` int(11) NOT NULL,
  `location` int(11) NOT NULL,
  PRIMARY KEY (`team_title`),
  KEY `tbl_team_tbl_location_FK` (`location`),
  KEY `tbl_team_tbl_player_FK` (`captain`),
  CONSTRAINT `tbl_team_tbl_location_FK` FOREIGN KEY (`location`) REFERENCES `tbl_location` (`location_id`),
  CONSTRAINT `tbl_team_tbl_player_FK` FOREIGN KEY (`captain`) REFERENCES `tbl_player` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
