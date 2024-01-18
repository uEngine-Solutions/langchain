--drop table BPM_RoleMapping;
--DROP TABLE BPM_WORKLIST;
--DROP TABLE BPM_PROCVAR;
--DROP TABLE BPM_PROCINST;
--DROP TABLE BPM_PROCDEFVER;
--DROP TABLE BPM_PROCDEF;
--
--drop sequence SEQ_BPM_workitem;
--drop sequence SEQ_BPM_PROCDEF;
--drop sequence SEQ_BPM_PROCDEFVER;
--drop sequence SEQ_BPM_PROCINST;
--drop sequence SEQ_BPM_PROCVAR;
--drop sequence SEQ_BPM_ROLEMAPPING;
--drop sequence SEQ_BPM_DOC_COMMENTS;

create sequence SEQ_BPM_workitem;
create sequence SEQ_BPM_PROCDEF;
create sequence SEQ_BPM_PROCDEFVER;
create sequence SEQ_BPM_PROCINST;
create sequence SEQ_BPM_PROCVAR;
create sequence SEQ_BPM_ROLEMAPPING;
create sequence SEQ_BPM_DOC_COMMENTS;

DROP TABLE BPM_TAG;
DROP TABLE BPM_TAGMAPPING;

DROP SEQUENCE SEQ_BPM_TAG;
 
-----------------/ SYSTEM TABLES /--------------------

create table bpm_procdef(
	defid		number(19),
	description	varchar2(255),
	parentFolder	number(19),
	isFolder	number(1) default 0,
	isAdhoc		number(1) default 0,
	isDeleted	number(1) default 0,
	ProdVerId	number(19),
	ProdVer		number(10) default 0,
	name		varchar2(255),
	modDate		date,
	OBJTYPE		varchar2(10),
	alias		varchar2(100) unique,
	ISVISIBLE	number(1) default 1,
CONSTRAINT PK_BPM_PROCDEF
  PRIMARY KEY ( defid )
);

create table bpm_procdefver(
	defverid	number(19),
	filePath	varchar2(255),
	ver		number(5),
	defId		number(19),
	defName		varchar2(255),
	modDate		date,
	prodStartDate	date,
	isDeleted	number(1),
  CONSTRAINT PK_BPM_PROCDEFVER
  PRIMARY KEY ( defverid )
);

create table bpm_procinst (
       instid             number(19),
       defVerId           number(19),
       defId              number(19),
       defName            varchar2(255),
       defPath            varchar2(255),
       defModDate	  date,
       startedDate        date,
       finishedDate       date,
       dueDate		  date,
       status             varchar2(20),
       info               varchar2(255),
       name               varchar2(255),
       isDeleted          number(1)		default 0,
       isAdhoc            number(1)		default 0,
       isArchive          number(1)		default 0,
       isSubProcess       number(1)		default 0,
       isEventHandler     number(1)		default 0,
       rootInstId         number(19),
       MainInstId	  number(19),
       mainDefVerId       number(19),
       mainActTrcTag      varchar2(20),
	   mainExecScope	  varchar2(20),
       absTrcPath         varchar2(200),
       dontReturn         number(1),
		modDate		  date,
		ext1		  varchar2(100),
  CONSTRAINT PK_BPM_PROCINST
  PRIMARY KEY ( instid )
);

create table bpm_procvar (
	varid		number(19),
	instId		number(19),
	dataType	number(2),
	valueString	varchar2(4000),
	valueLong	number(19),
	valueBoolean	number(1),
	valueDate	date,
	VarIndex	number(3),
	TrcTag		varchar2(20),
	IsProperty	number(1),
	ModDate		date,
	KeyName		varchar2(50),
	KeyString		varchar2(200),
  CONSTRAINT PK_BPM_PROCVAR
  PRIMARY KEY ( varID )
);

create table bpm_rolemapping (
	rolemappingid		number(19),
	instId		number(19),
	rootInstId	number(19),
	roleName	varchar2(20),
	endpoint	varchar2(255),
	value		varchar2(4000),
	resName		varchar2(200),
	assignType  number(2),
	assignParam1 varchar2(100),
	dispatchOption number(2),
	dispatchParam1 varchar2(100),
  CONSTRAINT PK_BPM_ROLEMAPPING
  PRIMARY KEY ( rolemappingID )
);


CREATE TABLE BPM_WORKLIST(
  TASKID            NUMBER           NOT NULL,
  TITLE             VARCHAR2 (200),
  DESCRIPTION       VARCHAR2 (500),
  ENDPOINT          VARCHAR2 (200),
  STATUS            VARCHAR2 (100),
  PRIORITY          NUMBER,
  STARTDATE         DATE,
  ENDDATE           DATE,
  DUEDATE           DATE,
  SAVEDATE			DATE,
  INSTID   			NUMBER (19),
  DEFID 			NUMBER (19),
  DEFNAME 			VARCHAR2 (200),
  TRCTAG        	VARCHAR2 (100),
  TOOL              VARCHAR2 (200),
  PARAMETER         VARCHAR2 (4000),
  GROUPID           NUMBER,
  GROUPNAME         NUMBER,
  EXT1              VARCHAR2 (200),
  EXT2              VARCHAR2 (200),
  EXT3              VARCHAR2 (200),
  EXT4              VARCHAR2 (200),
  EXT5              VARCHAR2 (200),
  EXT6              VARCHAR2 (200),
  EXT7              VARCHAR2 (200),
  EXT8              VARCHAR2 (200),
  EXT9              VARCHAR2 (200),
  EXT10             VARCHAR2 (200),
  ISURGENT          NUMBER (1),
  HASATTACHFILE     NUMBER (1),
  HASCOMMENT        NUMBER (1),
  DOCUMENTCATEGORY  VARCHAR2 (200),
  ISDELETED         NUMBER (1),
  ROOTINSTID		NUMBER (19),
  DISPATCHOPTION	NUMBER (2),
  DISPATCHPARAM1	VARCHAR2 (100),
  ROLENAME			VARCHAR2 (100),
  RESNAME			VARCHAR2 (100),
  REFROLENAME		VARCHAR2 (100),
  EXECSCOPE 		varchar2(5),
  CONSTRAINT PK_WORKLIST
  PRIMARY KEY ( TASKID )
);

create index idx_bpm_procvar on bpm_procvar(KEYSTRING);
create index idx_bpm_procvarinst on bpm_procvar(INSTID);
create index idx_bpm_rolemapping on bpm_roleMapping(roleName);



-----------------/ OLAP StarSchema /---------------------

CREATE TABLE BPM_RSRC_DIM(
  RSRC_ID   VARCHAR2 (20)   NOT NULL,
  RSRC_NAME VARCHAR2 (100),
  DEPT_NAME VARCHAR2 (100),
  DEPT_ID   VARCHAR2 (20),
  PSTN_NAME VARCHAR2 (50),
  PSTN_ID   VARCHAR2 (20),
  TITLE     VARCHAR2 (50),
  GENDER    NUMBER (1),
  BIRTHDAY  NUMBER (4),
  CONSTRAINT PK_BPM_RSRC_DIM
  PRIMARY KEY ( RSRC_ID )
);

CREATE TABLE BPM_TIME_DIM(
  TIME_ID NUMBER (11)  NOT NULL,
  YR      NUMBER (4),
  MNTH    NUMBER (2),
  QUTR    NUMBER (1),
  DAY	  NUMBER (2),
  MODDATE DATE,
  CONSTRAINT PK_BPM_TIME_DIM
  PRIMARY KEY ( TIME_ID )
);


CREATE TABLE BPM_PRFM_FACT_2006(
  INST_ID     NUMBER (11),
  ROOTINST_ID NUMBER (11),
  RSRC_ID     VARCHAR2 (50),
  TIME_ID     NUMBER (11),
  ACT_ID      VARCHAR2 (100),
  ACT_NAME    VARCHAR2 (100),
  DEF_ID      NUMBER (11),
  DEF_NAME    VARCHAR2 (200),
  PRSNGTIME   NUMBER (11),
  TRNRNDTIME  NUMBER (11),
  IDLETIME    NUMBER (11),
  CHOVRTIME   NUMBER (11),
  SSPNDTIME   NUMBER (11),
  COST        NUMBER (11),
  SLA_STFTRY  NUMBER (11),
  DEADLNHT	  NUMBER (2),
  MODTIME    DATE
 );

CREATE TABLE BPM_DEADLN_DIM(
  DEADLN_ID NUMBER(2)  NOT NULL,
  DEADLN_DESC VARCHAR2(255),
  CONSTRAINT PK_BPM_DEADLN_DIM
  PRIMARY KEY ( DEADLN_ID )
);

INSERT INTO BPM_DEADLN_DIM (DEADLN_ID, DEADLN_DESC) VALUES (0, 'Complete in Duedate');
INSERT INTO BPM_DEADLN_DIM (DEADLN_ID, DEADLN_DESC) VALUES (1, 'Delay Work');


-----------/ EXAMPLE ORGANIZATION CHART: Only for standalone version /-------------
 
--drop table emptable;
--drop table parttable;
--DROP TABLE roleTABLE;
--DROP TABLE roleUserTABLE;

create table emptable (
 empname varchar2(100),
 empcode varchar2(20),
 PASSWORD varchar2(20),
 ISADMIN NUMBER,
 JIKNAME varchar2(100),
 EMAIL varchar2(100),
 PARTCODE varchar2(20),
 GLobalCom varchar2(20),
 MOBILECMP VARCHAR2(10),
 MOBILENO VARCHAR2(15),
 ISDELETED varchar2(1) default '0',
 CONSTRAINT PK_emptable
  PRIMARY KEY ( empcode )
);

CREATE TABLE PARTTABLE (
  GLobalCom varchar2(20),
  PARTCODE varchar2(20),
  PARTNAME       varchar2(30),
  DESCRIPTION	varchar2(255),
  ISDELETED varchar2(1) default '0',
   CONSTRAINT PK_PARTTABLE
  PRIMARY KEY ( PARTCODE )
);
   
CREATE TABLE ROLETABLE (
  rolecode       varchar2(20)     NOT NULL,
  descr     varchar2(225),
  ISDELETED varchar2(1) default '0',
  CONSTRAINT PK_ROLETABLE
  PRIMARY KEY ( rolecode )
);

CREATE TABLE ROLEUSERTABLE (
  roleCode              varchar2(20)              NOT NULL,
  empCode           varchar2(20)      NOT NULL,
  CONSTRAINT PK_GROUPUSERTABLE
    PRIMARY KEY (roleCode, empCode)
);

insert into parttable (partcode, globalcom, partname)
values ('DEV', 'uEngine', 'Development');

insert into emptable (empname, empcode, partcode, globalcom, isadmin, password)
values ('Joe Bloggs', 'JB', 'DEV', 'uEngine', 1, 'test');
 
insert into roletable (rolecode, descr)
values ('TM', 'Trouble Manager');

insert into roleUserTable (rolecode, empCode)
values ('TM', 'JB');

        
-------uE 2.1---------------------------------------------------
--alter table BPM_PRFM_FACT_2006 add MODTIME DATE;
--alter table BPM_TIME_DIM add DAY number(2);
--alter table BPM_PROCDEF add alias varchar2(100);
--alter table BPM_PROCDEF ADD ISVISIBLE number(1) default 1;
--ALTER TABLE bpm_procdef ADD CONSTRAINT uq_alias UNIQUE (alias);


-------uE 3.0---------------------------------------------------

--alter table bpm_prfm_fact_2006 add DEADLNHT	  NUMBER(2);

--CREATE TABLE BPM_DEADLN_DIM(
--  DEADLN_ID NUMBER(2)  NOT NULL,
--  DEADLN_DESC VARCHAR2(255),
--  CONSTRAINT PK_BPM_DEADLN_DIM
--  PRIMARY KEY ( DEADLN_ID )
--);

INSERT INTO BPM_DEADLN_DIM (DEADLN_ID, DEADLN_DESC) VALUES (0, 'Complete in Duedate');
INSERT INTO BPM_DEADLN_DIM (DEADLN_ID, DEADLN_DESC) VALUES (1, 'Delay Work');

--- uE 3.0_final_standalone -----------------------------------
alter table emptable add locale varchar2(10) default 'en';
alter table emptable add nateOn varchar2(50);
alter table emptable add MSN varchar2(50);

--- uE 3.5 ----------------------------------------------------
ALTER TABLE bpm_procinst add INITEP varchar(100);
ALTER TABLE bpm_procinst add INITRSNM varchar(100);
ALTER TABLE bpm_procinst add CURREP varchar(100);
ALTER TABLE bpm_procinst add CURRRSNM varchar(100);

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
  CREATED_DATE           timestamp, 
  CREATED_BY			 VARCHAR(20), 
  UPDATED_DATE           timestamp, 
  UPDATED_BY             VARCHAR(20), 
  CONSTRAINT DOC_COMMENTS
  PRIMARY KEY ( ID )
);

--ALTER TABLE bpm_procinst add mainExecScope varchar(20);

CREATE TABLE COMTABLE (
	COMCODE 			varchar(20),
	COMNAME 			varchar(30),
	DESCRIPTION 		varchar(255),
	ISDELETED 			varchar(1) default '0',
	CONSTRAINT PK_COMTABLE
	PRIMARY KEY ( COMCODE )
);
insert into COMTABLE (COMCODE, COMNAME) values ('uEngine', 'UENGINE');

create sequence SEQ_BPM_ACLTABLE;
--drop table bpm_acltable;
create table bpm_acltable (
	acltableid INT,
	defid INT,
	comcode varchar(20),
	partcode varchar(20),
	empcode varchar(20),
	rolecode varchar(20),
	permission char(1),
	CONSTRAINT PK_BPM_ACLTABLE
	PRIMARY KEY ( acltableid )
);
ALTER TABLE bpm_procdef add permission char(1);
UPDATE bpm_procdef SET permission = 'N';


--- uE 3.5 ----------------------------------------------------
--ALTER TABLE bpm_procinst add mainExecScope varchar(20);
ALTER TABLE roletable add comcode varchar(20);
UPDATE roletable SET comcode = 'uEngine';

--create sequence SEQ_BPM_ACLTABLE;
drop table bpm_acltable;
create table bpm_acltable (
	aclid int,
	defid int,
	comcode varchar(20),
	partcode varchar(20),
	empcode varchar(20),
	rolecode varchar(20),
	defaultuser char(1),
	permission char(1),
	CONSTRAINT PK_BPM_ACLTABLE
	PRIMARY KEY ( aclid )
);

----------------- 생기원----------------------
ALTER TABLE emptable add isConsortiumManager boolean default false;
ALTER TABLE comtable add comManager varchar(20);
ALTER TABLE bpm_procdef ADD superdefid int;

--drop sequence SEQ_CONSORTIUMTABLE;
create sequence SEQ_CONSORTIUMTABLE;
--drop table CONSORTIUMTABLE;
create table CONSORTIUMTABLE (
	consortiumid int,
	comcode varchar(20),
	consortium varchar(20),
	description varchar(255),
	CONSTRAINT PK_CONSORTIUMTABLE
	PRIMARY KEY ( consortiumid )
);
----------------------------------------------------------------
update bpm_procdef set OBJTYPE='folder' where ISFOLDER=1;
update bpm_procdef set OBJTYPE='process' where OBJTYPE is NULL;

ALTER TABLE bpm_procdef add comcode varchar(20);
UPDATE bpm_procdef SET comcode = 'uEngine';

ALTER TABLE roletable add comcode varchar(20);
UPDATE roletable SET comcode = 'uEngine';

ALTER TABLE parttable add parent_partcode varchar(20);

ALTER TABLE BPM_WORKLIST add CO2EMISSION int;

ALTER TABLE BPM_ROLEMAPPING add GROUPID VARCHAR(30);

---------------2011.03.07. TAG
CREATE SEQUENCE SEQ_BPM_TAG;

CREATE TABLE BPM_TAG(
TAGID NUMBER NOT NULL,
NAME VARCHAR2 (255) NOT NULL,
COMCODE VARCHAR2 (20) NOT NULL,
CONSTRAINT PK_BPM_TAG PRIMARY KEY (TAGID)
);

CREATE TABLE BPM_TAGMAPPING(
TAGID NUMBER NOT NULL,
INSTID NUMBER(19) NOT NULL,
ROOTINSTID NUMBER(19) NOT NULL
);