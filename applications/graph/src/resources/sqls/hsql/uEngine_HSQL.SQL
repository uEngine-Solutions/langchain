DROP TABLE BPM_PROCDEF;
DROP TABLE BPM_PROCDEFVER;
DROP TABLE BPM_PROCINST;
DROP TABLE BPM_PROCVAR;
DROP TABLE BPM_ROLEMAPPING;
DROP TABLE BPM_WORKLIST;
DROP TABLE BPM_ACLTABLE;
DROP TABLE DUAL;

drop sequence SEQ_BPM_workitem;
drop sequence SEQ_BPM_PROCDEF;
drop sequence SEQ_BPM_PROCDEFVER;
drop sequence SEQ_BPM_PROCINST;
drop sequence SEQ_BPM_PROCVAR;
drop sequence SEQ_BPM_ROLEMAPPING;
drop sequence SEQ_BPM_ACLTABLE;

create sequence SEQ_BPM_WORKITEM;
create sequence SEQ_BPM_PROCDEF;
create sequence SEQ_BPM_PROCDEFVER;
create sequence SEQ_BPM_PROCINST;
create sequence SEQ_BPM_PROCVAR;
create sequence SEQ_BPM_ROLEMAPPING;
create sequence SEQ_BPM_ACLTABLE;

DROP TABLE BPM_TAG;
DROP TABLE BPM_TAGMAPPING;

DROP SEQUENCE SEQ_BPM_TAG;

-----------------/ SYSTEM TABLES /--------------------

CREATE TABLE DUAL(
    id varchar
);
insert into DUAL values ('value');

CREATE TABLE BPM_PROCDEF(
  DEFID                  INT    NOT NULL,
  DESCRIPTION            VARCHAR (255),
  PARENTFOLDER           INT,
  ISFOLDER               INT    default 0,
  ISADHOC                INT    default 0,
  OBJTYPE                VARCHAR (10),
  PRODVER                INT,
  PRODVERID              INT,
  NAME                   VARCHAR (255),
  ALIAS                  VARCHAR (100),
  ISDELETED              INT    default 0,
  ISVISIBLE              INT    default 1,
    modDate     timestamp,
  CONSTRAINT PK_BPM_PROCDEF
  PRIMARY KEY ( DEFID )
);


create table bpm_procdefver(
    defverid        INT,
    filePath    varchar(255),
    ver     varchar(100),
    defId       INT,
    defName     varchar(255),
    modDate     timestamp,
    prodStartDate   timestamp,
    isDeleted   INT default 0,
  CONSTRAINT PK_BPM_PROCDEFVER
  PRIMARY KEY ( defverid )
);


create table bpm_procinst (
    instid              INT,
    defVerId            INT,
    defId               INT,
    defName             varchar(255),
    defPath             varchar(255),
    defModDate          timestamp,
    startedDate         timestamp,
    finishedDate        timestamp,
    dueDate             timestamp,
    status              varchar(20),
    info                varchar(255),
    name                varchar(255),
    isDeleted           INT     default 0,
    isAdhoc             INT     default 0,
    isArchive           INT     default 0,
    isSubProcess        INT     default 0,
    isEventHandler      INT     default 0,
    rootInstId          INT,
    MainInstId          INT,
    mainDefVerId        INT,
    mainActTrcTag       varchar(20),
    mainExecScope       varchar(20),
    absTrcPath          varchar(200),
    dontReturn          INT,
    modDate             timestamp,
    ext1                varchar(100),
    initEp              varchar(100),
    initRSNM            varchar(100),
    currEp              varchar(100), 
    currRSNM            varchar(100),
  CONSTRAINT PK_BPM_PROCINST
  PRIMARY KEY ( instid )
)   

create table bpm_procvar (
    varid       int,
    instId      int,
    dataType    int,
    valueString varchar(4000),
    valueLong   int,
    valueBoolean    int,
    valueDate   timestamp,
    VarIndex    int,
    TrcTag      varchar(20),
    IsProperty  int,
    ModDate     timestamp,
    KeyName         varchar(100),
    KeyString   varchar(200),
  CONSTRAINT PK_BPM_PROCVAR
  PRIMARY KEY ( varID )
);


create table bpm_rolemapping (
    rolemappingid       int,
    instId      int,
    rootInstId  int,
    roleName    varchar(20),
    endpoint    varchar(255),
    value       varchar(4000),
    resName     varchar(200),
    assignType  int,
    assignParam1 varchar(100),
    dispatchOption int,
    dispatchParam1 varchar(100),
  CONSTRAINT PK_BPM_ROLEMAPPING
  PRIMARY KEY ( rolemappingID )
);


CREATE TABLE BPM_WORKLIST(
  TASKID            INT           NOT NULL,
  TITLE             VARCHAR (200),
  DESCRIPTION       VARCHAR (500),
  ENDPOINT          VARCHAR (200),
  STATUS            VARCHAR (100),
  PRIORITY          INT,
  STARTDATE         timestamp,
  ENDDATE           timestamp,
  DUEDATE           timestamp,
  INSTID            INT,
  DEFID             INT,
  DEFNAME           VARCHAR (200),
  TRCTAG            VARCHAR (100),
  TOOL              VARCHAR (200),
  PARAMETER         VARCHAR (4000),
  GROUPID           INT,
  GROUPNAME         INT,
  EXT1              VARCHAR (200),
  EXT2              VARCHAR (200),
  EXT3              VARCHAR (200),
  EXT4              VARCHAR (200),
  EXT5              VARCHAR (200),
  EXT6              VARCHAR (200),
  EXT7              VARCHAR (200),
  EXT8              VARCHAR (200),
  EXT9              VARCHAR (200),
  EXT10              VARCHAR (200),
  ISURGENT          INT,
  HASATTACHFILE     INT,
  HASCOMMENT        INT,
  DOCUMENTCATEGORY  VARCHAR (200),
  ISDELETED         INT,
  ROOTINSTID        INT,
  DISPATCHOPTION    INT,
  DISPATCHPARAM1    VARCHAR (100),
  ROLENAME          VARCHAR (100),
  RESNAME           VARCHAR (100),
  REFROLENAME       VARCHAR (100),
  EXECSCOPE         varchar(5),
  CONSTRAINT PK_WORKLIST
  PRIMARY KEY ( TASKID )
);


-----------------/ OLAP StarSchema /---------------------

DROP TABLE BPM_RSRC_DIM;
DROP TABLE BPM_TIME_DIM;
DROP TABLE BPM_DEADLN_DIM;
DROP TABLE BPM_PRFM_FACT_2006;

CREATE TABLE BPM_RSRC_DIM(
  RSRC_ID   VARCHAR (20)   NOT NULL,
  RSRC_NAME VARCHAR (100),
  DEPT_NAME VARCHAR (100),
  DEPT_ID   VARCHAR (20),
  PSTN_NAME VARCHAR (50),
  PSTN_ID   VARCHAR (20),
  TITLE     VARCHAR (50),
  GENDER    INT,
  BIRTHDAY  INT,
  CONSTRAINT PK_BPM_RSRC_DIM
  PRIMARY KEY ( RSRC_ID )
);

CREATE TABLE BPM_TIME_DIM(
  TIME_ID INT  NOT NULL,
  YR      INT,
  MNTH    INT,
  QUTR    INT,
  DAY     INT,
  MODDATE timestamp,
  CONSTRAINT PK_BPM_TIME_DIM
  PRIMARY KEY ( TIME_ID )
)

CREATE TABLE BPM_DEADLN_DIM(
  DEADLN_ID INT  NOT NULL,
  DEADLN_DESC VARCHAR(255),
  CONSTRAINT PK_BPM_DEADLN_DIM
  PRIMARY KEY ( DEADLN_ID )
)

INSERT INTO BPM_DEADLN_DIM (DEADLN_ID, DEADLN_DESC) VALUES (0, 'Complete in Duedate');
INSERT INTO BPM_DEADLN_DIM (DEADLN_ID, DEADLN_DESC) VALUES (1, 'Delay Work');

CREATE TABLE BPM_PRFM_FACT_2006(
  INST_ID     INT,
  ROOTINST_ID INT,
  RSRC_ID     VARCHAR (50),
  TIME_ID     INT,
  ACT_ID      VARCHAR (100),
  ACT_NAME    VARCHAR (100),
  DEF_ID      INT,
  DEF_NAME    VARCHAR (200),
  DEADLNHT    INT,
  PRSNGTIME   INT,
  TRNRNDTIME  INT,
  IDLETIME    INT,
  CHOVRTIME   INT,
  SSPNDTIME   INT,
  COST        INT,
  SLA_STFTRY  INT,
  MODTIME     DATE
 );

 
-----------/ EXAMPLE ORGANIZATION CHART: Only for standalone version /-------------
 
DROP TABLE emptable;
DROP TABLE parttable;
DROP TABLE comtable;
DROP TABLE roleTABLE;
DROP TABLE roleUserTABLE;

create table emptable (
 empname varchar(100),
 empcode varchar(20),
 PASSWORD varchar(20),
 ISADMIN integer,
 JIKNAME varchar(100),
 EMAIL varchar(100),
 PARTCODE varchar(20),
 GLobalCom varchar(20),
 CONSTRAINT PK_emptable
  PRIMARY KEY ( empcode )
);

CREATE TABLE PARTTABLE (
  GLobalCom varchar(20),
  PARTCODE varchar(20),
  PARTNAME       varchar(30),
  DESCRIPTION   varchar(255),
  ISDELETED varchar(1) default '0',
   CONSTRAINT PK_PARTTABLE
  PRIMARY KEY ( PARTCODE )
);

CREATE TABLE COMTABLE (
    COMCODE             varchar(20),
    COMNAME             varchar(30),
    DESCRIPTION         varchar(255),
    ISDELETED           varchar(1) default '0',
    CONSTRAINT PK_COMTABLE
    PRIMARY KEY ( COMCODE )
);
insert into COMTABLE (COMCODE, COMNAME) values ('uengine', 'UENGINE');
   
CREATE TABLE ROLETABLE (
  rolecode       varchar(20)     NOT NULL,
  comcode       varchar(20),
  descr     varchar(225),
  ISDELETED varchar(1) default '0',
  CONSTRAINT PK_ROLETABLE
  PRIMARY KEY ( rolecode )
);

CREATE TABLE ROLEUSERTABLE (
  roleCode              varchar(20)              NOT NULL,
  empCode           varchar(20)      NOT NULL,
  CONSTRAINT PK_GROUPUSERTABLE
    PRIMARY KEY (roleCode, empCode)
)

create sequence SEQ_BPM_ACLTABLE;
create table bpm_acltable (
    aclid int,
    defid int,
    comcode varchar(20),
    partcode varchar(20),
    empcode varchar(20),
    rolecode varchar(20),
    permission char(1),
    CONSTRAINT PK_BPM_ACLTABLE
    PRIMARY KEY ( aclid )
);

insert into parttable (partcode, globalcom, partname)
values ('DEV', 'uEngine', 'Development');

insert into emptable (empname, empcode, partcode, globalcom, isadmin, password)
values ('Joe Bloggs', 'JB', 'DEV', 'uEngine', 1, 'test');
 
insert into roletable (rolecode, descr)
values ('TM', 'Trouble Manager');

insert into roleUserTable (rolecode, empCode)
values ('TM', 'JB');



----- uE 2.1 -------------------------------------------------

ALTER TABLE BPM_PRFM_FACT_2006 add MODTIME DATE;
ALTER TABLE BPM_TIME_DIM add DAY int;
ALTER TABLE BPM_PROCDEF add alias varchar(100);
ALTER TABLE BPM_PROCDEF alter column ISFOLDER set null
ALTER TABLE BPM_PROCDEF alter column ISADHOC set null
ALTER TABLE BPM_PROCDEF alter column PRODVER set null
ALTER TABLE BPM_PROCDEF alter column ISDELETED set null
ALTER TABLE BPM_PROCDEF alter column ISFOLDER set default '0'
ALTER TABLE BPM_PROCDEF alter column ISADHOC set default '0'
ALTER TABLE BPM_PROCDEF alter column PRODVER set default '0'
ALTER TABLE BPM_PROCDEF alter column ISDELETED set default '0'
ALTER TABLE BPM_PROCDEF ADD ISVISIBLE int;
ALTER TABLE bpm_procdef ADD CONSTRAINT uq_alias UNIQUE (alias);
ALTER TABLE BPM_TIME_DIM add day int


--- uE 2.1_01 ------------------------------------------------
ALTER TABLE BPM_PROCDEF alter column ISVISIBLE set default 1

CREATE TABLE BPM_EX_ACL(
    DEFID           INT         NOT NULL,
    ACCESSIBILITY   VARCHAR(1),
    GROUPID         VARCHAR(20),
    ROLEID          VARCHAR(20),
    USERID          VARCHAR(20)
);
--- uE 2.3 ----------------------------------------------------
ALTER TABLE BPM_WORKLIST add SAVEDATE DATE;
ALTER TABLE BPM_procinst add initEp varchar(100);
ALTER TABLE BPM_procinst add initRSNM varchar(100);
ALTER TABLE BPM_procinst add currEp varchar(100);
ALTER TABLE BPM_procinst add currRSNM varchar(100);
ALTER TABLE bpm_procdef DROP CONSTRAINT uq_alias;
ALTER TABLE bpm_procdef ADD CONSTRAINT uq_alias UNIQUE (alias, isdeleted);

--- uE 3.0 ----------------------------------------------------
ALTER TABLE bpm_prfm_fact_2006 add DEADLNHT   INT;

CREATE TABLE BPM_DEADLN_DIM(
  DEADLN_ID INT  NOT NULL,
  DEADLN_DESC VARCHAR(255),
  CONSTRAINT PK_BPM_DEADLN_DIM
  PRIMARY KEY ( DEADLN_ID )
)

INSERT INTO BPM_DEADLN_DIM (DEADLN_ID, DEADLN_DESC) VALUES (0, 'Complete in Duedate');
INSERT INTO BPM_DEADLN_DIM (DEADLN_ID, DEADLN_DESC) VALUES (1, 'Delay Work');

--- uE 3.0 standalone -----------------------------------------
ALTER TABLE emptable add isdeleted varchar(1) default '0';
ALTER TABLE parttable add isdeleted varchar(1) default '0';
ALTER TABLE roletable add isdeleted varchar(1) default '0';
ALTER TABLE parttable add description varchar(255);

--- uE 3.0_final_standalone -----------------------------------
ALTER TABLE emptable add locale varchar(10) default 'en';
ALTER TABLE emptable add nateOn varchar(50);
ALTER TABLE emptable add MSN varchar(50);
ALTER TABLE emptable add MOBILENO varchar(20);
ALTER TABLE emptable add MOBILECMP varchar(5);

create sequence SEQ_BPM_DOC_COMMENTS;

--- ApprovalLineActivity --------------------------------------
CREATE TABLE DOC_COMMENTS(  
  ID                     INT    NOT NULL,
  INSTANCE_ID            INT,  
  CONTENTS               VARCHAR (3000), 
  OPT_TYPE               VARCHAR (100), 
  EMPNO                  VARCHAR(20), 
  EMPNAME                VARCHAR (100), 
  EMPTITLE               VARCHAR (100), 
  TRACINGTAG             VARCHAR (10), 
  CREATED_DATE           timestamp, 
  CREATED_BY             VARCHAR(20), 
  UPDATED_DATE           timestamp, 
  UPDATED_BY             VARCHAR(20), 
  CONSTRAINT DOC_COMMENTS
  PRIMARY KEY ( ID )
);



--- uE 3.5 ----------------------------------------------------
ALTER TABLE bpm_procinst add mainExecScope varchar(20);
ALTER TABLE roletable add comcode varchar(20);
UPDATE roletable SET comcode = 'uEngine'

CREATE TABLE COMTABLE (
    COMCODE             varchar(20),
    COMNAME             varchar(30),
    DESCRIPTION         varchar(255),
    ISDELETED           varchar(1) default '0',
    CONSTRAINT PK_COMTABLE
    PRIMARY KEY ( COMCODE )
);
insert into COMTABLE (COMCODE, COMNAME) values ('uEngine', 'UENGINE');

create sequence SEQ_BPM_ACLTABLE;
drop table bpm_acltable;
create table bpm_acltable (
    acltableid int,
    defid int,
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

-- 2009.10.20 activity 단위별 acl 설정을 위한 컬럼 추가
ALTER TABLE bpm_acltable add tracingtag varchar(10);

-- 2009.10.26 consortium 을 위한 임의 id 생성
insert into comtable (comcode, comname, description) values ('kitech', '생기원', '협업관리회사')
insert into parttable (globalcom, partcode, partname, description) values ('kitech', 'consortium', '관리부', '협업관리부서')
insert into emptable (empname, empcode, password, jikname, globalcom, partcode) values ('컨소시엄', 'consortium', 'test', 'ConsortiumManager', 'kitech', 'consortium')
----------------------------------------------------------------

----------------- 2009.08.21. used in wait activity  -----------
--drop table SCHEDULE_TABLE;
create table SCHEDULE_TABLE (
    INSTID          INT,
    TRCTAG          VARCHAR (100),
    STARTDATE       timestamp,
    --SCHEXP            VARCHAR (100),
    --SAVEDUSER     VARCHAR (100),
    --SAVEDTIME     VARCHAR (100),
    --description       varchar (255),
    CONSTRAINT PK_SCHEDULE_TABLE
    --PRIMARY KEY ( INSTID, TRCTAG )
);
----------------------------------------------------------------

---------------- 2009. 09. 09 ----------------------------------
update bpm_procdef set OBJTYPE='folder' where ISFOLDER=1;
update bpm_procdef set OBJTYPE='process' where OBJTYPE is NULL;
----------------------------------------------------------------

---------------- 2009. 11. 24 PartTable을 트리구조로 전환
ALTER TABLE parttable add parent_partcode varchar(20);


---------------- 2009. 12. 16 roleTable에 comcode 컬럼추가
ALTER TABLE roletable add comcode varchar(20);
UPDATE roletable SET comcode = 'uEngine';

---------------- 2009. 12. 16 SCHEDULE_TABLE ADD STARTDATE COLUMN
ALTER TABLE SCHEDULE_TABLE add STARTDATE timestamp;
ALTER TABLE SCHEDULE_TABLE add SCHE_IDX int;
ALTER TABLE bpm_procdef add comcode varchar(20);
UPDATE bpm_procdef SET comcode = 'uEngine';

---------------- 2010. 1. 11 전략맵 추가
ALTER TABLE bpm_procinst add STRATEGYID varchar(20);

DROP SEQUENCE SEQ_BPM_STRTG;
CREATE SEQUENCE SEQ_BPM_STRTG;

DROP TABLE IF EXISTS "BPM_STRTG";
CREATE TABLE "BPM_STRTG" (
   STRTGID INT,
   STRTGNM VARCHAR(100),
   TYPE VARCHAR(30),
   PARENTID INT,
   ISDELETED VARCHAR(1)DEFAULT '0',
   CONSTRAINT PK_BPM_STRTG
   PRIMARY KEY ( STRTGID )
);

DROP TABLE IF EXISTS "BPM_STRINST";
CREATE TABLE BPM_STRINST (
   INSTID INT,
   STRTGID INT,
   ISDELETED VARCHAR(1) DEFAULT '0'
);

---------------2010. 1. 21. SMS Activity for EMPTABLE 
ALTER TABLE EMPTABLE add MOBILECMP VARCHAR(10);
ALTER TABLE EMPTABLE add MOBILENO VARCHAR(15);


ALTER TABLE BPM_ROLEMAPPING add GROUPID VARCHAR(30);

---------------2010. 1. 25. for Strategy Map 
ALTER TABLE BPM_STRTG add STATUS CHAR(10);
ALTER TABLE BPM_STRTG add INSTCNT int;
ALTER TABLE BPM_STRTG add CMPLTINSTCNT int;
ALTER TABLE BPM_STRTG add DESCRIPTION VARCHAR(255);
ALTER TABLE BPM_STRTG add COMCODE VARCHAR(20);
ALTER TABLE BPM_STRTG add STARTDATE timestamp;
ALTER TABLE BPM_STRTG add ENDDATE timestamp;
ALTER TABLE BPM_STRTG drop PARENTID ;

INSERT INTO BPM_STRTG (STRTGID, STRTGNM,TYPE,DESCRIPTION,COMCODE) VALUES ('0', 'uEngine','','','uEngine');

DROP TABLE IF EXISTS "BPM_STRTG_PARENTMAPPING";
CREATE TABLE BPM_STRTG_PARENTMAPPING (
   STRTGID INT,
   PARENTID INT
);

INSERT INTO BPM_STRTG_PARENTMAPPING (STRTGID, PARENTID) VALUES (0,-1);
ALTER TABLE BPM_STRTG add PARTCODE VARCHAR(20);
ALTER TABLE BPM_STRTG add PARTNAME VARCHAR(100);

---------------2010. 4. 21. for EMS demo 
ALTER TABLE BPM_WORKLIST add CO2EMISSION int;

ALTER TABLE DOC_COMMENTS add APPRTITLE VARCHAR(100);

ALTER TABLE SCHEDULE_TABLE add SCHE_IDX int;


---------------2011.03.07. TAG
CREATE SEQUENCE SEQ_BPM_TAG;

CREATE TABLE BPM_TAG(
TAGID INT NOT NULL,
NAME VARCHAR (255) NOT NULL,
COMCODE VARCHAR (20) NOT NULL,
CONSTRAINT PK_BPM_TAG PRIMARY KEY (TAGID)
);

CREATE TABLE BPM_TAGMAPPING(
TAGID INT NOT NULL,
INSTID INT NOT NULL,
ROOTINSTID INT NOT NULL
);