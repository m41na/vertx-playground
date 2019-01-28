INSERT INTO db_vertx_play.tbl_account (fk_player_id,username,pass_code,acc_status,acc_role,phone_num,mfa_token,mfa_method,rec_token) VALUES 
(1,'m41na','m41na','active','admin','+16082090384',NULL,'email',NULL)
,(2,'mainacell','mainacell','pending','player','+13094288418',NULL,'email',NULL)
;

INSERT INTO db_vertx_play.tbl_player (email_addr,first_name,`last_ name`,date_joined,wow_points,player_location) VALUES 
('m41na@yahoo.com','Steve','Mike','2018-06-23 00:00:00.000',0,1)
,('mainacell@gmail.com','Mike','Maina','2018-06-23 00:00:00.000',0,2)
;

INSERT INTO db_vertx_play.tbl_game (game_title,organizer,play_time,game_status,game_scope) VALUES 
('Morning Scrum',1,'2018-06-23 00:00:00.000','ready','general')
;

INSERT INTO db_vertx_play.tbl_game_content (fk_game_id,fk_que_id,fk_decor_id,content_group,show_duration,tick_interval,max_points,alt_content,delay_period) VALUES 
(1,1,1,'scrum',-1,-1,-1,NULL,-1)
;

INSERT INTO db_vertx_play.tbl_guardian (fk_guardian_id,fk_player_id,guardian_dob,player_dob,access_code,setup_date) VALUES 
(1,2,'1978-06-23','2010-06-23','m41na','2019-01-23 00:00:00.000')
;

INSERT INTO db_vertx_play.tbl_location (loc_city,loc_state,loc_zip,country_code) VALUES 
('New Lenox','IL','60451','US')
,('Madison','WI','53716','US')
;

INSERT INTO db_vertx_play.tbl_participant (fk_player_id,fk_game_id,screen_name,fk_team_title,fk_location_id) VALUES 
(1,1,'steve',NULL,1)
,(2,1,'mike',NULL,1)
;

INSERT INTO db_vertx_play.tbl_que_decor (decor_name,use_choices,use_clues,use_countdown,use_matches,is_numeric_ans,numeric_range) VALUES 
('quick_vote',0,0,0,0,1,0)
,('quick_note',0,0,0,0,0,0)
,('wipeout',1,0,1,0,0,-1)
,('match_pairs',1,0,1,0,0,-1)
,('survey',1,0,1,0,0,-1)
,('classmode',1,0,1,0,0,-1)
,('countdown',1,0,1,0,0,-1)
,('get_r_done',1,0,1,0,0,-1)
,('close_not_over',1,0,1,0,0,-1)
,('countdown_n_clue',1,0,1,0,0,-1)
;

INSERT INTO db_vertx_play.tbl_que_decor (decor_name,use_choices,use_clues,use_countdown,use_matches,is_numeric_ans,numeric_range) VALUES 
('wipeout_n_clues',1,0,1,0,0,-1)
;

INSERT INTO db_vertx_play.tbl_question (que_text,choice_1,choice_2,choice_3,choice_4,clue_1,clue_2,clue_3,clue_4,clue_5,answer,ans_details,asked_by,date_asked,match_1,match_2,match_3,match_4,match_5,choice_5) VALUES 
('Vote Now','8','5','3','2',NULL,NULL,NULL,NULL,NULL,'0','Fibonacci Sequence',1,'2018-06-23 00:00:00.000',NULL,NULL,NULL,NULL,NULL,'1')
;

