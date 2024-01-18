drop table BPM_RoleMapping;
DROP TABLE BPM_WORKLIST;
DROP TABLE BPM_PROCVAR;
DROP TABLE BPM_PROCINST;
DROP TABLE BPM_PROCDEFVER;
DROP TABLE BPM_PROCDEF;

 
-----------------/ SYSTEM TABLES /--------------------

create table bpm_procdef(
	defid		INTEGER  NOT NULL,
	description	varchar(255),
	parentFolder	INTEGER,
	isFolder	SMALLINT,
	isAdhoc		SMALLINT,
	isDeleted	SMALLINT,
	ProdVerId	INTEGER,
	ProdVer		SMALLINT,
	name		varchar(255),
	modDate		TIMESTAMP,
CONSTRAINT PK_BPM_PROCDEF
  PRIMARY KEY ( DEFID )
);

create table bpm_procdefver(
	defverid		INTEGER  NOT NULL,
	filePath	varchar(255),
	ver			INTEGER,
	defId		INTEGER,
	defName		varchar(255),
	modDate		TIMESTAMP,
	prodStartDate	TIMESTAMP,
	isDeleted	SMALLINT,
  CONSTRAINT PK_BPM_PROCDEFVER
  PRIMARY KEY ( defverid )
);

create table bpm_procinst (
       instid             INTEGER  NOT NULL,
       defVerId           INTEGER,
       defId              INTEGER,
       defName            varchar(255),
       defPath            varchar(255),
       defModDate		  TIMESTAMP,
       startedDate        TIMESTAMP,
       finishedDate       TIMESTAMP,
       dueDate			  TIMESTAMP,
       status             varchar(20),
       info               varchar(255),
       name               varchar(255),
       isDeleted          SMALLINT		default 0,
       isAdhoc            SMALLINT		default 0,
       isArchive          SMALLINT		default 0,
       isSubProcess       SMALLINT		default 0,
       isEventHandler     SMALLINT		default 0,
       rootInstId         INTEGER,
       MainInstId		  INTEGER,
       mainDefVerId       INTEGER,
       mainActTrcTag      varchar(20),
       absTrcPath         varchar(200),
       dontReturn         SMALLINT,
		modDate			TIMESTAMP,
		ext1			varchar(100),
		ext2			varchar(100),
		extint1			INTEGER,
		extint2			INTEGER,
		
  CONSTRAINT PK_BPM_PROCINST
  PRIMARY KEY ( instid )
);

create table bpm_procvar (
--	varid		INTEGER  NOT NULL,
	instId		INTEGER NOT NULL,
	dataType	SMALLINT,
	valueString	LONG VARCHAR,
	valueLong	INTEGER,
	valueBoolean	SMALLINT,
	valueDate	TIMESTAMP,
	VarIndex	SMALLINT,
	TrcTag		varchar(20),
	IsProperty	SMALLINT,
	ModDate		TIMESTAMP,
	KeyName		varchar(50),
	KeyString		varchar(200) NOT NULL,
  CONSTRAINT PK_BPM_PROCVAR
  PRIMARY KEY ( instId, keyString )
);

create table bpm_rolemapping (
	instId		INTEGER NOT NULL,
	rootInstId	INTEGER,
	roleName	varchar(20) NOT NULL,
	endpoint	varchar(100) NOT NULL,
	value		long varchar,
	resName		varchar(200),
	assignType  SMALLINT,
	assignParam1 varchar(100),
	dispatchOption SMALLINT,
	dispatchParam1 varchar(100),
  CONSTRAINT PK_BPM_ROLEMAPPING
  PRIMARY KEY ( instID, roleName, endpoint )
);

CREATE TABLE BPM_WORKLIST(
  TASKID            INTEGER           NOT NULL,
  TITLE             VARCHAR (200),
  DESCRIPTION       VARCHAR (500),
  ENDPOINT          VARCHAR (200),
  STATUS            VARCHAR (100),
  PRIORITY          SMALLINT,
  STARTDATE         TIMESTAMP,
  ENDDATE           TIMESTAMP,
  DUEDATE           TIMESTAMP,
  INSTID   			INTEGER,
  DEFID 			INTEGER,
  DEFNAME 			VARCHAR (200),
  TRCTAG        	VARCHAR (100),
  TOOL              VARCHAR (200),
  PARAMETER         VARCHAR (1),
  GROUPID           INTEGER,
  GROUPNAME         VARCHAR (100),
  EXT1              VARCHAR (200),
  EXT2              VARCHAR (200),
  EXT3              VARCHAR (200),
  ISURGENT          SMALLINT,
  HASATTACHFILE     SMALLINT,
  HASCOMMENT        SMALLINT,
  DOCUMENTCATEGORY  VARCHAR (200),
  ISDELETED         SMALLINT,
  ROOTINSTID		INTEGER,
  DISPATCHOPTION	SMALLINT,
  DISPATCHPARAM1	VARCHAR (100),
  ROLENAME			VARCHAR (100),
  REFROLENAME		VARCHAR (100),
  RESNAME			VARCHAR (100),
  CONSTRAINT PK_WORKLIST
  PRIMARY KEY ( TASKID )
);

create unique index idx_bpm_pd on bpm_procdef(DEFID);
create unique index idx_bpm_pdv on bpm_procdefver(DEFVERID);
create unique index idx_bpm_pi on bpm_procinst(INSTID);
create unique index idx_bpm_wl on bpm_worklist(TASKID);

create unique index idx_bpm_procvar on bpm_procvar(INSTID, KEYSTRING);
create unique index idx_bpm_rmp on bpm_roleMapping(instid, roleName, endpoint);



-----------------/ OLAP StarSchema /---------------------

CREATE TABLE BPM_RSRC_DIM(
  RSRC_ID   VARCHAR (20)   NOT NULL,
  RSRC_NAME VARCHAR (100),
  DEPT_NAME VARCHAR (100),
  DEPT_ID   VARCHAR (20),
  PSTN_NAME VARCHAR (50),
  PSTN_ID   VARCHAR (20),
  TITLE     VARCHAR (50),
  GENDER    SMALLINT,
  BIRTHDAY  SMALLINT,
  CONSTRAINT PK_BPM_RSRC_DIM
  PRIMARY KEY ( RSRC_ID )
);

CREATE TABLE BPM_TIME_DIM(
  TIME_ID INTEGER  NOT NULL,
  YR      SMALLINT,
  MNTH    SMALLINT,
  QUTR    SMALLINT,
  MODDATE TIMESTAMP,
  CONSTRAINT PK_BPM_TIME_DIM
  PRIMARY KEY ( TIME_ID )
);

INSERT INTO BPM_TIME_DIM (TIME_ID, YR, MNTH, QUTR) VALUES (200606, 2006, 6, 2);
INSERT INTO BPM_TIME_DIM (TIME_ID, YR, MNTH, QUTR) VALUES (200607, 2006, 7, 3);
INSERT INTO BPM_TIME_DIM (TIME_ID, YR, MNTH, QUTR) VALUES (200608, 2006, 8, 3);
INSERT INTO BPM_TIME_DIM (TIME_ID, YR, MNTH, QUTR) VALUES (200609, 2006, 9, 3);
INSERT INTO BPM_TIME_DIM (TIME_ID, YR, MNTH, QUTR) VALUES (200610, 2006, 10, 4);
INSERT INTO BPM_TIME_DIM (TIME_ID, YR, MNTH, QUTR) VALUES (200611, 2006, 11, 4);
INSERT INTO BPM_TIME_DIM (TIME_ID, YR, MNTH, QUTR) VALUES (200612, 2006, 12, 4);


CREATE TABLE BPM_PRFM_FACT_2006(
  INST_ID     INTEGER,
  ROOTINST_ID INTEGER,
  RSRC_ID     VARCHAR (50),
  TIME_ID     INTEGER,
  ACT_ID      VARCHAR (100),
  ACT_NAME    VARCHAR (100),
  DEF_ID      INTEGER,
  DEF_NAME    VARCHAR (200),
  PRSNGTIME   INTEGER,
  TRNRNDTIME  INTEGER,
  IDLETIME    INTEGER,
  CHOVRTIME   INTEGER,
  SSPNDTIME   INTEGER,
  COST        INTEGER,
  SLA_STFTRY  INTEGER,
  MODTIME 	  DATE
 );
 
-----------------/ STORED PROCEDURES /--------------------

CREATE PROCEDURE bpm_pr_rsvwrkitm(
      INOUT var_taskId INTEGER,
      var_title VARCHAR(500),
      var_description VARCHAR(500),
      var_endpoint VARCHAR(500),
      var_status VARCHAR(500),
      var_priority SMALLINT,
      var_startdate TIMESTAMP,
      var_enddate TIMESTAMP,
      var_duedate TIMESTAMP,
      var_processInstance INTEGER,
      var_processDefinition INTEGER,
      var_processDefName VARCHAR(500),
      var_tracingTag VARCHAR(500),
      var_tool VARCHAR(500),
      var_dispatchOption SMALLINT,
      var_parameter VARCHAR(500)
    )    
    LANGUAGE SQL

P1: BEGIN

      if var_taskId is null then
        select NEXTVAL for seq_bpm_workitem into var_taskId from SYSIBM.SYSDUMMY1;
      else
        delete from bpm_worklist where taskId = var_taskId;
      end if;

      insert into bpm_worklist
        (taskId,        title,      description,        endpoint,       status,     priority,       startdate,          enddate,        duedate,        instId,                 defId,                  DefName,            trcTag,         tool,       dispatchOption,     parameter      )
      values
        (var_taskId,    var_title,  var_description,    var_endpoint,   var_status, var_priority,   current timestamp,  var_enddate,    var_duedate,    var_processInstance,    var_processDefinition,  var_processDefName, var_tracingTag, var_tool,   var_dispatchOption, var_parameter      );
END                               
    
    
CREATE procedure bpm_pr_arvProc(
      var_id INTEGER,
      var_defPath VARCHAR(500)
    )
    LANGUAGE SQL

P1: BEGIN
     update bpm_procinst set isArchive = 1, defPath=var_defPath where instid = var_id;
     delete from bpm_procvar where instId = var_id;
     delete from bpm_rolemapping where instId = var_id;
    end
         
-----------uE 2.1------------------------------------------
alter table BPM_PRFM_FACT_2006 add MODTIME DATE;
