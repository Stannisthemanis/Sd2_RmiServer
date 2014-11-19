--testes
SELECT * FROM action_item;
SELECT * FROM agenda_item;
SELECT * FROM invite;
SELECT * FROM meeting;
SELECT * FROM message;
SELECT * FROM user1;
SELECT * FROM user_group;
SELECT * FROM user_group_user;
SELECT * FROM USERS_ON_CHAT;

-- inserir dados
--user_
INSERT INTO user1 VALUES (1,'Ana','root',STR_TO_DATE('11-02-1984', '%d-%m-%Y'));
INSERT INTO user1 VALUES (2,'Beatriz','root',STR_TO_DATE('19-01-1994', '%d-%m-%Y'));
INSERT INTO user1 VALUES (3,'Catia','root',STR_TO_DATE('21-04-1988', '%d-%m-%Y'));
INSERT INTO user1 VALUES (4,'Diana','root',STR_TO_DATE('05-06-1992', '%d-%m-%Y'));
INSERT INTO user1 VALUES (5,'Eliana','root',STR_TO_DATE('04-05-1985', '%d-%m-%Y'));
INSERT INTO user1 VALUES (6,'Fabiana','root',STR_TO_DATE('25-09-1975', '%d-%m-%Y'));
INSERT INTO user1 VALUES (7,'Guida','root',STR_TO_DATE('30-02-1976', '%d-%m-%Y'));
INSERT INTO user1 VALUES (8,'Helena','root',STR_TO_DATE('11-12-2000', '%d-%m-%Y'));
INSERT INTO user1 VALUES (9,'Ina','root',STR_TO_DATE('11-01-1990', '%d-%m-%Y'));
INSERT INTO user1 VALUES (10,'Julia','root',STR_TO_DATE('12-11-1958', '%d-%m-%Y'));


INSERT INTO  user_group VALUES (1, 'Ethernot');
INSERT INTO  user_group VALUES (2, 'Fantasticos');
INSERT INTO  user_group VALUES (3, 'Coders');

INSERT INTO  user_group_user VALUES (1,1);
INSERT INTO  user_group_user VALUES (1,3);
INSERT INTO  user_group_user VALUES (1,5);
INSERT INTO  user_group_user VALUES (2,2);
INSERT INTO  user_group_user VALUES (2,4);
INSERT INTO  user_group_user VALUES (2,6);
INSERT INTO  user_group_user VALUES (3,7);
INSERT INTO  user_group_user VALUES (3,8);
INSERT INTO  user_group_user VALUES (3,9);
INSERT INTO  user_group_user VALUES (3,10);

INSERT INTO meeting VALUES (1,'rmi','responsaveis',1,'sala e4.1',to_Date('2014/11/13 10:20','yyyy/mm/dd hh24:mi'),
                            to_Date('2014/11/13 14:20' , 'yyyy/mm/dd hh24:mi'),240,1,1,4);
INSERT INTO meeting VALUES (2,'rmi','responsaveis',3,'sala e4.1',to_Date('2014/11/14 10:20','yyyy/mm/dd hh24:mi'),
                            to_Date('2014/11/13 14:20' , 'yyyy/mm/dd hh24:mi'),240,2,1,3);
INSERT INTO meeting VALUES (3,'rmi','responsaveis',6,'sala e4.1',to_Date('2014/11/15 10:20','yyyy/mm/dd hh24:mi'),
                            to_Date('2014/11/13 14:20' , 'yyyy/mm/dd hh24:mi'),240,3,1,2);

INSERT INTO agenda_item VALUES (1,'decidir rmi',1);
INSERT INTO agenda_item VALUES (2,'cliente web',1);
INSERT INTO agenda_item VALUES (3,'rede',2);
INSERT INTO agenda_item VALUES (4,'decidir api a usar',2);
INSERT INTO agenda_item VALUES (5,'novo pc servidor',3);


INSERT INTO message VALUES(1,1,STR_TO_DATE('2014/11/13 11:10', 'yyyy/mm/dd hh24:mi'),'ola \n td bem? \n como fica o assunto da rede?');
INSERT INTO message VALUES(2,2,STR_TO_DATE('2014/11/14 21:20', 'yyyy/mm/dd hh24:mi'),'oi \n td bem? \n sim e contigo?');
INSERT INTO message VALUES(3,3,STR_TO_DATE('2014/11/15 12:30', 'yyyy/mm/dd hh24:mi'),'vamos la? \n onde? \n a reuniao?');
INSERT INTO message VALUES(4,4,STR_TO_DATE('2014/11/12 11:40', 'yyyy/mm/dd hh24:mi'),'como fazemos? \n nao sei? \n como fica o assunto da rede?');
INSERT INTO message VALUES(5,5,STR_TO_DATE('2014/11/11 13:50', 'yyyy/mm/dd hh24:mi'),'ola \n td? \n como fica o router??');

INSERT INTO action_item VALUES (1,'comprar cabos',1,'n',1);
INSERT INTO action_item VALUES (2,'pagar contas da edp',10,'s',1);
INSERT INTO action_item VALUES (3,'fazer novo contrato',5,'n',2);
INSERT INTO action_item VALUES (4,'alterar o rmi',2,'n',3);
INSERT INTO action_item VALUES (5,'rever relacoes da bd',9,'s',3);
    
INSERT INTO invite VALUES(1,1,2);
INSERT INTO invite VALUES(2,1,9);
INSERT INTO invite VALUES(3,2,8);
INSERT INTO invite VALUES(4,2,7);
INSERT INTO invite VALUES(5,3,6);
INSERT INTO invite VALUES(6,3,5);
INSERT INTO invite VALUES(7,1,4);
INSERT INTO invite VALUES(8,2,3);
INSERT INTO invite VALUES(9,2,2);
INSERT INTO invite VALUES(10,3,1);

INSERT INTO USERS_ON_CHAT VALUES(1,5);
INSERT INTO USERS_ON_CHAT VALUES(2,4);
INSERT INTO USERS_ON_CHAT VALUES(3,3);
INSERT INTO USERS_ON_CHAT VALUES(4,2);
INSERT INTO USERS_ON_CHAT VALUES(5,1);

INSERT INTO MEETING_USER_GROUP VALUES(1,1);
INSERT INTO MEETING_USER_GROUP VALUES(1,2);
INSERT INTO MEETING_USER_GROUP VALUES(2,2);
INSERT INTO MEETING_USER_GROUP VALUES(2,3);
INSERT INTO MEETING_USER_GROUP VALUES(3,1);

















