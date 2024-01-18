DROP TABLE IF EXISTS bpm_rolemapping;
DROP TABLE IF EXISTS bpm_worklist;
DROP TABLE IF EXISTS bpm_procvar;
DROP TABLE IF EXISTS bpm_procinst;
DROP TABLE IF EXISTS bpm_procdefver;
DROP TABLE IF EXISTS bpm_procdef;
DROP TABLE IF EXISTS bpm_prfm_fact_2006;
DROP TABLE IF EXISTS bpm_rsrc_dim;
DROP TABLE IF EXISTS bpm_time_dim;

--  system tables


CREATE TABLE dual(
	 id varchar(100)
);
insert into dual values ('value');

CREATE TABLE bpm_procdef(
    defid        int(19) not null auto_increment,
    description  varchar(255),
    parentfolder int(19),
    isfolder     int(1) default 0,
    isadhoc      int(1) default 0,
    objtype		  VARCHAR (10),
    prodver      int(10),
    prodverid    int(19),
    name         varchar(255),
    alias        varchar(100),
    isdeleted    int(1) default 0,
    isvisible    int(1) default 1,
    moddate      datetime,
  constraint pk_bpm_procdef
  primary key ( defid )
);

CREATE TABLE bpm_procdefver(
    defverid          int(19) auto_increment,
    filepath          varchar(255),
    ver               varchar(100),
    defid             int(19),
    defname           varchar(255),
    moddate           datetime,
    prodstartdate     datetime,
    isdeleted         int(1) default 0,
  constraint pk_bpm_procdefver
  primary key ( defverid )
);

CREATE TABLE bpm_procinst (
    instid             int(19) auto_increment,
    defverid           int(19),
    defid              int(19),
    defname            varchar(255),
    defpath            varchar(255),
    defmoddate         datetime,
    starteddate        datetime,
    finisheddate       datetime,
    duedate            datetime,
    status             varchar(20),
    info               varchar(255),
    name               varchar(255),
    isdeleted          int(1)     default 0,
    isadhoc            int(1)     default 0,
    isarchive          int(1)     default 0,
    issubprocess       int(1)     default 0,
    iseventhandler     int(1)     default 0,
    rootinstid         int(19),
    maininstid         int(19),
    maindefverid       int(19),
    mainacttrctag      varchar(20),
    abstrcpath         varchar(200),
    dontreturn         int(1),
    moddate            datetime,
	 ext1			         varchar(100),
  constraint pk_bpm_procinst
  primary key ( instid )
);

CREATE TABLE bpm_procvar (
    varid         int(19) auto_increment,
    instid        int(19),
    datatype      int(2),
    valuestring   text,
    valuelong     int(19),
    valueboolean  int(1),
    valuedate     datetime,
    varindex      int(3),
    trctag        varchar(20),
    isproperty    int(1),
    moddate       datetime,
    keyname       varchar(50),
    keystring     varchar(200),
  constraint pk_bpm_procvar
  primary key  ( varid )
);

CREATE TABLE bpm_rolemapping (
    rolemappingid  int(19) auto_increment,
    instid         int(19),
    rootinstid     int(19),
    rolename       varchar(20),
    endpoint       varchar(255),
    value          text,
    resname        varchar(200),
    assigntype     int(2),
    assignparam1   varchar(100),
    dispatchoption int(2),
    dispatchparam1 varchar(100),
  constraint pk_bpm_rolemapping
  primary key ( rolemappingid ),
  key bpm_rolemapping_idx (rolename)
);

CREATE TABLE bpm_worklist (
  taskid            bigint(20) unsigned NOT NULL auto_increment,
  title             varchar (200),
  description       text,
  endpoint          varchar (200),
  status            varchar (100),
  priority          bigint(20) unsigned,
  startdate         datetime,
  enddate           datetime,
  duedate           datetime,
  instid            int (19),
  defid             int (19),
  defname           varchar (200),
  trctag            varchar (100),
  tool              varchar (200),
  parameter         text,
  groupid           bigint(20) unsigned,
  groupname         bigint(20) unsigned,
  ext1              varchar (200),
  ext2              varchar (200),
  ext3              varchar (200),
  ext4              varchar (200),
  ext5              varchar (200),
  ext6              varchar (200),
  ext7              varchar (200),
  ext8              varchar (200),
  ext9              varchar (200),
  ext10              varchar (200),
  isurgent          int (1),
  hasattachfile     int (1),
  hascomment        int (1),
  documentcategory  varchar (200),
  isdeleted         int (1),
  rootinstid        int (19),
  dispatchoption    int (2),
  dispatchparam1    varchar (100),
  rolename			 varchar (100),
  resname			 varchar (100),  
  refrolename		 varchar (100),
  execscope 		 varchar (5), 
  constraint pk_worklist
  primary key ( taskid )
);

-- olap starschema

DROP TABLE IF EXISTS  BPM_RSRC_DIM;
DROP TABLE IF EXISTS  BPM_TIME_DIM;
DROP TABLE IF EXISTS  BPM_PRFM_FACT_2006;

CREATE TABLE bpm_rsrc_dim(
  rsrc_id   varchar (20)   not null,
  rsrc_name varchar (100),
  dept_name varchar (100),
  dept_id   varchar (20),
  pstn_name varchar (50),
  pstn_id   varchar (20),
  title     varchar (50),
  gender    int (1),
  birthday  int (4),
  constraint pk_bpm_rsrc_dim
  primary key ( rsrc_id )
);

CREATE TABLE bpm_time_dim(
  time_id int (11)  not null,
  yr      int (4),
  mnth    int (2),
  qutr    int (1),
  day 	  INT (2),
  moddate datetime,
  constraint pk_bpm_time_dim
  primary key ( time_id )
);

CREATE TABLE bpm_prfm_fact_2006(
  inst_id     int (11),
  rootinst_id int (11),
  rsrc_id     varchar (50),
  time_id     int (11),
  act_id      varchar (100),
  act_name    varchar (100),
  def_id      int (11),
  def_name    varchar (200),
  prsngtime   int (11),
  trnrndtime  int (11),
  idletime    int (11),
  chovrtime   int (11),
  sspndtime   int (11),
  cost        int (11),
  sla_stftry  int (11),
  modtime	  datetime
 );
 
 
 -----------/ EXAMPLE ORGANIZATION CHART: Only for standalone version /-------------
 
DROP TABLE IF EXISTS emptable;
DROP TABLE IF EXISTS parttable;
DROP TABLE IF EXISTS roleTABLE;
DROP TABLE IF EXISTS roleUserTABLE;

create table emptable (
 empname varchar(100),
 empcode varchar(20),
 PASSWORD varchar(20),
 ISADMIN int(1),
 JIKNAME varchar(100),
 EMAIL varchar(100),
 PARTCODE varchar(20),
 GLobalCom varchar(20),
 CONSTRAINT PK_emptable
  PRIMARY KEY ( empcode )
)

CREATE TABLE PARTTABLE (
  GLobalCom varchar(20),
  PARTCODE varchar(20),
   PARTNAME       varchar(30),
   CONSTRAINT PK_PARTTABLE
  PRIMARY KEY ( PARTCODE )
)
   
CREATE TABLE ROLETABLE (
  rolecode       varchar(20),
  descr     varchar(225),
  CONSTRAINT PK_ROLETABLE
  PRIMARY KEY ( rolecode )
)

CREATE TABLE ROLEUSERTABLE (
  roleCode              varchar(20),
  empCode           varchar(20),
  CONSTRAINT PK_GROUPUSERTABLE
    PRIMARY KEY (roleCode, empCode)
)

insert into parttable (partcode, globalcom, partname)
values ('DEV', 'uEngine', 'Development');

insert into emptable (empname, empcode, partcode, globalcom, isadmin, password)
values ('Joe Bloggs', 'JB', 'DEV', 'uEngine', 1, 'test');
 
insert into roletable (rolecode, descr)
values ('TM', 'Trouble Manager');

insert into roleUserTable (rolecode, empCode)
values ('TM', 'JB');


--- uE 2.1_01 ------------------------------------------------
CREATE TABLE bpm_ex_acl(
	defid 		   INT			NOT NULL,
	accessibility 	VARCHAR(1),
	groupid		   VARCHAR(20),
	roleid		   VARCHAR(20),
	userid		   VARCHAR(20)
);

--- uE 2.3 ----------------------------------------------------
alter table bpm_worklist add savedate datetime;

CREATE TABLE document_log(
  instid		    INT      NOT NULL,
  downloadtime  timestamp,
  user		    VARCHAR (20),
  docid         VARCHAR (100),
  mailaddress   VARCHAR (50),
  CONSTRAINT PK_document_log
  PRIMARY KEY ( instid )
);

--- uE 3.0 ---------------------------------------------------

create unique index DefUniqueAlias on bpm_procdef(alias);

CREATE TABLE bpm_seq(
  seq          int (11),
  tbname       varchar(255),
  description  varchar(255),
  moddate      datetime
);

--- ApprovalLineActivity --------------------------------------
CREATE TABLE DOC_COMMENTS(  
  ID                     INT	NOT NULL,
  INSTANCE_ID            INT,  
  CONTENTS               VARCHAR (3000), 
  OPT_TYPE               VARCHAR (100), 
  EMPNO                  VARCHAR(20), 
  EMPNAME				 VARCHAR (100), 
  EMPTITLE				 VARCHAR (100), 
  TRACINGTAG      		 VARCHAR (10), 
  CREATED_DATE           datetime, 
  CREATED_BY			 VARCHAR(20), 
  UPDATED_DATE           datetime , 
  UPDATED_BY             VARCHAR(20), 
  CONSTRAINT DOC_COMMENTS
  PRIMARY KEY ( ID )
);


--- uE 3.5 ----------------------------------------------------
ALTER TABLE bpm_procinst add mainExecScope varchar(20);
ALTER TABLE roletable add comcode varchar(20);
UPDATE roletable SET comcode = 'uEngine'

CREATE TABLE COMTABLE (
	COMCODE 			varchar(20),
	COMNAME 			varchar(30),
	DESCRIPTION 		varchar(255),
	ISDELETED 			varchar(1) default '0',
	CONSTRAINT PK_COMTABLE
	PRIMARY KEY ( COMCODE )
);
insert into COMTABLE (COMCODE, COMNAME) values ('uEngine', 'UENGINE');

--create sequence SEQ_BPM_ACLTABLE;
drop table bpm_acltable;
create table bpm_acltable (
	acltableid int(19) auto_increment,
	defid int(19),
	comcode varchar(20),
	partcode varchar(20),
	empcode varchar(20),
	rolecode varchar(20),
	defaultuser char(1),
	permission char(1),
	CONSTRAINT PK_BPM_ACLTABLE
	PRIMARY KEY ( acltableid )
);

----------------- 생기원용 임시 컬럼 ----------------------
ALTER TABLE emptable add isConsortiumManager boolean default false;
ALTER TABLE comtable add comManager varchar(20);

--drop sequence SEQ_CONSORTIUMTABLE;
--create sequence SEQ_CONSORTIUMTABLE;
--drop table CONSORTIUMTABLE;
create table CONSORTIUMTABLE (
	consortiumid int(19) auto_increment,
	comcode varchar(20),
	consortium varchar(20),
	description varchar(255),
	CONSTRAINT PK_CONSORTIUMTABLE
	PRIMARY KEY ( consortiumid )
);
----------------------------------------------------------------


ALTER TABLE BPM_ROLEMAPPING add groupid varchar(30);


create table SCHEDULE_TABLE (
	INSTID			int(19),
	TRCTAG        	VARCHAR(100),
	STARTDATE		timestamp,
	--SCHEXP			VARCHAR (100),
	--SAVEDUSER		VARCHAR (100),
	--SAVEDTIME		VARCHAR (100),
	--description		varchar (255),
	CONSTRAINT PK_SCHEDULE_TABLE
	--PRIMARY KEY ( INSTID, TRCTAG )
);



ALTER TABLE EMPTABLE add MOBILECMP VARCHAR(10);
ALTER TABLE EMPTABLE add MOBILENO VARCHAR(15);
ALTER TABLE EMPTABLE add ISDELETED VARCHAR(1) default '0';
ALTER TABLE EMPTABLE add LOCALE VARCHAR(10) default 'en';
ALTER TABLE EMPTABLE add NATEON VARCHAR(50);
ALTER TABLE EMPTABLE add MSN VARCHAR(50);

ALTER TABLE PARTTABLE add ISDELETED VARCHAR(1) default'0';
ALTER TABLE PARTTABLE add DESCRIPTION VARCHAR(255);
ALTER TABLE PARTTABLE add PARENT_PARTCODE VARCHAR(20);

ALTER TABLE ROLETABLE add ISDELETED VARCHAR(1) default '0';

ALTER TABLE COMTABLE add COMMANAGER varchar(20);

ALTER TABLE BPM_PROCDEF add PERMISSION CHAR(1);
ALTER TABLE BPM_PROCDEF add COMCODE varchar(20);

ALTER TABLE BPM_PROCINST add INITEP varchar(100);
ALTER TABLE BPM_PROCINST add INITRSNM varchar(100);
ALTER TABLE BPM_PROCINST add CURREP varchar(100);
ALTER TABLE BPM_PROCINST add CURRRSNM varchar(100);


CREATE TABLE BPM_STRTG (
   STRTGID int(19),
   STRTGNM VARCHAR(100),
   TYPE VARCHAR(30),
   PARENTID int(19),
   ISDELETED VARCHAR(1) DEFAULT '0',
   CONSTRAINT PK_BPM_STRTG
   PRIMARY KEY ( STRTGID )
);

CREATE TABLE BPM_STRINST (
   INSTID int(19),
   STRTGID int(19),
   ISDELETED VARCHAR(1) DEFAULT '0'
);

ALTER TABLE BPM_STRTG add STATUS CHAR(10);
ALTER TABLE BPM_STRTG add INSTCNT int(19);
ALTER TABLE BPM_STRTG add CMPLTINSTCNT int(19);
ALTER TABLE BPM_STRTG add DESCRIPTION VARCHAR(255);
ALTER TABLE BPM_STRTG add COMCODE VARCHAR(20);
ALTER TABLE BPM_STRTG add STARTDATE date;
ALTER TABLE BPM_STRTG add ENDDATE date;
ALTER TABLE BPM_STRTG drop PARENTID ;

INSERT INTO BPM_STRTG (STRTGID, STRTGNM,TYPE,DESCRIPTION,COMCODE) VALUES ('0', 'uEngine','','','uEngine');

CREATE TABLE BPM_STRTG_PARENTMAPPING (
   STRTGID INT(19),
   PARENTID INT(19)
);

INSERT INTO BPM_STRTG_PARENTMAPPING (STRTGID, PARENTID) VALUES (0,-1);


-------------------------------------------------------------
--TAG
CREATE TABLE BPM_TAG(
TAGID INT NOT NULL AUTO_INCREMENT,
NAME VARCHAR (255) NOT NULL,
COMCODE VARCHAR (20) NOT NULL,
CONSTRAINT PK_BPM_TAG PRIMARY KEY (TAGID)
);

CREATE TABLE BPM_TAGMAPPING(
TAGID INT NOT NULL,
INSTID INT NOT NULL,
ROOTINSTID INT NOT NULL
);