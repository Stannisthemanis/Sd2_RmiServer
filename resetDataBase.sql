DROP TABLE user_group_user;
DROP TABLE invite;
DROP TABLE meeting_user_group;
DROP TABLE meeting;
DROP TABLE action_item;
DROP TABLE users_on_chat;
DROP TABLE agenda_item;
DROP TABLE message;
DROP TABLE user_group;
DROP TABLE user1;

CREATE TABLE user1( 
    id_user INT NOT NULL,
    user_name       varchar(20)       CHECK (user_name IS NOT NULL),
    pass_word       varchar(20)       CHECK (pass_word IS NOT NULL), 
    dob             varchar(20)       CHECK (dob IS NOT NULL),
    PRIMARY KEY(id_user)
);

CREATE TABLE user_group( 
    id_user_group  INT NOT NULL,
    group_name     varchar(20)       CHECK (group_name  IS NOT NULL),
    PRIMARY KEY(id_user_group)
);

CREATE TABLE user_group_user( 
    id_user_group     INT NOT NULL,
    id_user           INT NOT NULL,
    FOREIGN KEY (id_user_group)    REFERENCES user_group(id_user_group),
    FOREIGN KEY (id_user)          REFERENCES user1(id_user),
    PRIMARY KEY (id_user_group,id_user)
);

CREATE TABLE message(   
    id_message   INT NOT NULL,
    mdate        DATE           CHECK(mdate IS NOT NULL), 
    messages     VARCHAR(1000)  CHECK(messages IS NOT NULL), 
    PRIMARY KEY(id_message)
);


CREATE TABLE agenda_item( 
    id_agenda_item       INT NOT NULL,
    item_to_discuss      varchar(20)      CHECK(item_to_discuss IS NOT NULL ),
    PRIMARY KEY(id_agenda_item)
);

CREATE TABLE chat(
    id_agenda_item      INT NOT NULL,
    id_message          INT NOT NULL,
    FOREIGN KEY (id_agenda_item)    REFERENCES agenda_item(id_agenda_item),
    FOREIGN KEY (id_message)        REFERENCES message(id_message),
    PRIMARY KEY (id_agenda_item, id_message)
);

CREATE TABLE action_item( 
    id_action_item   INT NOT NULL,
    name             VARCHAR(20)     CHECK(name IS NOT NULL ),
    id_user          INT NOT NULL,
    completed        VARCHAR(1)      CHECK(completed IS NOT NULL),
    FOREIGN KEY(id_user) REFERENCES user1(id_user),
    PRIMARY KEY(id_action_item)
);

CREATE TABLE meeting(
    id_meeting        INT NOT NULL, 
    title             VARCHAR(20)   CHECK(title IS NOT NULL),
    outcome           VARCHAR(20)   CHECK (outcome is not null),
    id_user           INT NOT NULL,
    local             VARCHAR(20)   CHECK(local is not null),
    s_date            DATE          CHECK (s_date is not null),
    e_date            DATE          CHECK (e_date is not null),
    duration          INT NOT NULL,
    id_user_group     INT NOT NULL,
    id_agenda_item    INT NOT NULL,
    id_action_item    INT NOT NULL,
    FOREIGN KEY(id_user)            REFERENCES user1(id_user),
    FOREIGN KEY(id_agenda_item)     REFERENCES agenda_item (id_agenda_item),
    FOREIGN KEY(id_user_group)      REFERENCES user_group(id_user_group),
    FOREIGN KEY(id_action_item)     REFERENCES action_item(id_action_item),
    PRIMARY KEY(id_meeting)
);

CREATE TABLE invite( 
    id_invite      INT NOT NULL,  
    id_meeting     INT NOT NULL,
    id_user        INT NOT NULL,
    FOREIGN KEY(id_meeting) REFERENCES meeting(id_meeting),
    FOREIGN KEY(id_user) REFERENCES user1(id_user),
    PRIMARY KEY(id_invite)
);

CREATE TABLE users_on_chat( 
    id_user          INT NOT NULL,
    id_agenda_item   INT NOT NULL,
    FOREIGN KEY(id_user)        REFERENCES user1(id_user),
    FOREIGN KEY(id_agenda_item) REFERENCES agenda_item(id_agenda_item)
);

CREATE TABLE meeting_user_group( 
    id_meeting       INT NOT NULL,
    id_user_group    INT NOT NULL,
    FOREIGN KEY(id_meeting) REFERENCES meeting(id_meeting),
    FOREIGN KEY(id_user_group) REFERENCES user_group(id_user_group)
);






















