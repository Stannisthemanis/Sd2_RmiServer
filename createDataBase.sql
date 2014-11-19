 --drop table user; 
 CREATE TABLE user1
( id_user             Numeric(3)        CHECK(id_user IS NOT NULL) PRIMARY KEY(id_user),
  user_name       varchar(20)       CHECK (user_name IS NOT NULL),
  pass_word       varchar(20)       CHECK (pass_word IS NOT NULL), 
  dob                   varchar(20)       CHECK (dob IS NOT NULL)
);

drop table user_group;
CREATE TABLE user_group
( id_user_group  Numeric(3)        CHECK(id_user_group IS NOT NULL) PRIMARY KEY,
  group_name     varchar(20)       CHECK (group_name  IS NOT NULL)
);

CREATE TABLE user_group_user
( id_ug Numeric(3)        CHECK(id_ug IS NOT NULL),
  id_u Numeric(3)        CHECK(id_u IS NOT NULL),
  FOREIGN KEY (id_ug) REFERENCES user_group(id_user_group),
  FOREIGN KEY (id_u) REFERENCES user1(id_user)
);

CREATE TABLE message
( id_message NUMERIC(3)         CHECK(id_message IS NOT NULL) PRIMARY KEY,
  mdate            DATE                    CHECK(mdate IS NOT NULL), 
  messages     VARCHAR(1000) CHECK(messages IS NOT NULL) 
);

--drop table agenda_item;
CREATE TABLE agenda_item
( id_agenda_item      Numeric(3)        CHECK(id_agenda_item IS NOT NULL) PRIMARY KEY,
  item_to_discuss      varchar(20)      CHECK(item_to_discuss IS NOT NULL ),
  chat                           Numeric(3)       CHECK(chat IS NOT NULL),
  FOREIGN KEY(chat) REFERENCES Message(id_message)
);

--drop table action_item;
CREATE TABLE action_item
( id_action_item   NUMERIC(3)        CHECK(id_action_item IS NOT NULL) PRIMARY KEY,
  name                  VARCHAR(20)     CHECK(name IS NOT NULL ),
  user_resp           NUMERIC(3)       CHECK(user_resp IS NOT NULL ),
  completed          VARCHAR(1)        CHECK(completed IS NOT NULL),
  FOREIGN KEY(user_resp) REFERENCES user1(id_user)
  );

--drop table meeting
CREATE TABLE meeting
( id_meeting   NUMERIC(3)       CHECK(id_meeting IS NOT NULL) PRIMARY KEY, 
  title                 VARCHAR(20)  CHECK(title IS NOT NULL),
  outcome        VARCHAR(20)  CHECK (outcome is not null),
  user_resp      NUMERIC(3)  CHECK(user_resp is not null),
  local              VARCHAR(20)   CHECK(local is not null),
  s_date            DATE                 CHECK (s_date is not null),
  e_date            DATE                 CHECK (e_date is not null),
  duration        NUMERIC(3)     CHECK (duration IS NOT NULL),
  user_group    NUMERIC(3)     CHECK(user_group IS NOT NULL),
  agenda_item NUMERIC(3)      CHECK(agenda_item IS NOT NULL),
  action_item    NUMERIC(3)     CHECK(action_item IS NOT NULL),
  FOREIGN KEY (user_resp)      REFERENCES user1(id_user),
  FOREIGN KEY(agenda_item)REFERENCES agenda_item (id_agenda_item),
  FOREIGN KEY(user_group)   REFERENCES user_group(id_user_group),
  FOREIGN KEY(agenda_item) REFERENCES agenda_item(id_agenda_item),
  FOREIGN KEY(action_item)    REFERENCES action_item(id_action_item)
);

--drop table invite;
CREATE TABLE invite
( id_invite       NUMERIC(3)      CHECK(id_invite IS NOT NULL)PRIMARY KEY,  
  id_meeting  NUMERIC(3)      CHECK(id_meeting IS NOT NULL),
  id_user        NUMERIC(3)      CHECK(id_user IS NOT NULL),
  FOREIGN KEY(id_meeting) REFERENCES meeting(id_meeting),
  FOREIGN KEY(id_user) REFERENCES user1(id_user)
);

CREATE TABLE users_on_chat
( user_on_chat  NUMERIC(3)      CHECK(user_on_chat IS NOT NULL),
  agenda_item   NUMERIC(3)      CHECK(agenda_item IS NOT NULL),
  FOREIGN KEY(user_on_chat) REFERENCES user1(id_user),
  FOREIGN KEY(agenda_item) REFERENCES agenda_item(id_agenda_item)
);

CREATE TABLE meeting_user_group
( id_meeting       NUMERIC(3)      CHECK(id_meeting IS NOT NULL),
  id_user_group  NUMERIC(3)      CHECK(id_user_group IS NOT NULL),
  FOREIGN KEY(id_meeting) REFERENCES meeting(id_meeting),
  FOREIGN KEY(id_user_group) REFERENCES user_group(id_user_group)
);

COMMIT;






















