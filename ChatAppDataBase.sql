/*==============================================================*/
/* Nom de SGBD :  MySQL 5.0                                     */
/* Date de création :  10/03/2024 12:56:13                      */
/*==============================================================*/


drop table if exists BELONGS;

drop table if exists CONNECTED;

drop table if exists CONTACTS;

drop table if exists MESSAGEROOM;

drop table if exists MESSAGETO;

drop table if exists ROOM;

drop table if exists USER;

/*==============================================================*/
/* Table : BELONGS                                              */
/*==============================================================*/
create table BELONGS
(
   GROUPEUID            int not null,
   UID                  int not null,
   USERNAME             varchar(20) not null,
   primary key (GROUPEUID, UID, USERNAME)
);

/*==============================================================*/
/* Table : CONNECTED                                            */
/*==============================================================*/
create table CONNECTED
(
   UID                  int not null,
   USERNAME             varchar(20) not null,
   ROOMID               int not null,
   ROOMNAME             varchar(20) not null,
   primary key (UID, USERNAME, ROOMID, ROOMNAME)
);

/*==============================================================*/
/* Table : CONTACTS                                             */
/*==============================================================*/
create table CONTACTS
(
   GROUPEUID            int not null,
   primary key (GROUPEUID)
);

/*==============================================================*/
/* Table : MESSAGEROOM                                          */
/*==============================================================*/
create table MESSAGEROOM
(
   MESSAGEROOMID        int not null,
   ROOMID               int not null,
   ROOMNAME             varchar(20) not null,
   UID                  int not null,
   USERNAME             varchar(20) not null,
   MESSAGE              varchar(30),
   DATE                 date,
   ISDELETED            bool,
   primary key (MESSAGEROOMID)
);

/*==============================================================*/
/* Table : MESSAGETO                                            */
/*==============================================================*/
create table MESSAGETO
(
   MESSAGEID            int not null,
   UID                  int not null,
   USERNAME             varchar(20) not null,
   USE_UID              int not null,
   USE_USERNAME         varchar(20) not null,
   MESSAGE              varchar(30),
   DATE                 date,
   ISDELETED            bool,
   primary key (MESSAGEID)
);

/*==============================================================*/
/* Table : ROOM                                                 */
/*==============================================================*/
create table ROOM
(
   ROOMID               int not null,
   ROOMNAME             varchar(20) not null,
   IMAGE                varchar(30),
   primary key (ROOMID, ROOMNAME)
);

/*==============================================================*/
/* Table : USER                                                 */
/*==============================================================*/
create table USER
(
   UID                  int not null,
   HASHPASSWORD         varchar(10),
   USERNAME             varchar(20) not null,
   IMAGE                varchar(30),
   ISADMIN              bool,
   primary key (UID, USERNAME)
);

alter table BELONGS add constraint FK_BELONGS foreign key (GROUPEUID)
      references CONTACTS (GROUPEUID) on delete restrict on update restrict;

alter table BELONGS add constraint FK_BELONGS2 foreign key (UID, USERNAME)
      references USER (UID, USERNAME) on delete restrict on update restrict;

alter table CONNECTED add constraint FK_CONNECTED foreign key (UID, USERNAME)
      references USER (UID, USERNAME) on delete restrict on update restrict;

alter table CONNECTED add constraint FK_CONNECTED2 foreign key (ROOMID, ROOMNAME)
      references ROOM (ROOMID, ROOMNAME) on delete restrict on update restrict;

alter table MESSAGEROOM add constraint FK_CONTAINS foreign key (ROOMID, ROOMNAME)
      references ROOM (ROOMID, ROOMNAME) on delete restrict on update restrict;

alter table MESSAGEROOM add constraint FK_SENDROOM foreign key (UID, USERNAME)
      references USER (UID, USERNAME) on delete restrict on update restrict;

alter table MESSAGETO add constraint FK_RECEIVE foreign key (UID, USERNAME)
      references USER (UID, USERNAME) on delete restrict on update restrict;

alter table MESSAGETO add constraint FK_SEND foreign key (USE_UID, USE_USERNAME)
      references USER (UID, USERNAME) on delete restrict on update restrict;

