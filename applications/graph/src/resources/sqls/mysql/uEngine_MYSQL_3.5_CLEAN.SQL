DROP TABLE IF EXISTS `bpm_acltable`;
CREATE TABLE  `bpm_acltable` (
  `acltableid` int(19) NOT NULL AUTO_INCREMENT,
  `defid` int(19) DEFAULT NULL,
  `comcode` varchar(20) DEFAULT NULL,
  `partcode` varchar(20) DEFAULT NULL,
  `empcode` varchar(20) DEFAULT NULL,
  `rolecode` varchar(20) DEFAULT NULL,
  `defaultuser` char(1) DEFAULT NULL,
  `permission` char(1) DEFAULT NULL,
  PRIMARY KEY (`acltableid`)
);

DROP TABLE IF EXISTS `bpm_deadln_dim`;
CREATE TABLE  `bpm_deadln_dim` (
  `DEADLN_ID` int(11) NOT NULL,
  `DEADLN_DESC` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`DEADLN_ID`)
);
INSERT INTO bpm_deadln_dim (DEADLN_ID, DEADLN_DESC) VALUES (0, 'Complete in Duedate');
INSERT INTO bpm_deadln_dim (DEADLN_ID, DEADLN_DESC) VALUES (1, 'Delay Work');

DROP TABLE IF EXISTS `bpm_prfm_fact_2006`;
CREATE TABLE  `bpm_prfm_fact_2006` (
  `inst_id` int(11) DEFAULT NULL,
  `rootinst_id` int(11) DEFAULT NULL,
  `rsrc_id` varchar(50) DEFAULT NULL,
  `time_id` int(11) DEFAULT NULL,
  `act_id` varchar(100) DEFAULT NULL,
  `act_name` varchar(100) DEFAULT NULL,
  `def_id` int(11) DEFAULT NULL,
  `def_name` varchar(200) DEFAULT NULL,
  `prsngtime` int(11) DEFAULT NULL,
  `trnrndtime` int(11) DEFAULT NULL,
  `idletime` int(11) DEFAULT NULL,
  `chovrtime` int(11) DEFAULT NULL,
  `sspndtime` int(11) DEFAULT NULL,
  `cost` int(11) DEFAULT NULL,
  `sla_stftry` int(11) DEFAULT NULL,
  `modtime` datetime DEFAULT NULL,
  `DEADLNHT` int(11) DEFAULT NULL
);

DROP TABLE IF EXISTS `bpm_procdef`;
CREATE TABLE  `bpm_procdef` (
  `defid` int(19) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `parentfolder` int(19) DEFAULT NULL,
  `isfolder` int(1) DEFAULT '0',
  `isadhoc` int(1) DEFAULT '0',
  `objtype` varchar(10) DEFAULT NULL,
  `prodver` int(10) DEFAULT NULL,
  `prodverid` int(19) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `alias` varchar(100) DEFAULT NULL,
  `isdeleted` int(1) DEFAULT '0',
  `isvisible` int(1) DEFAULT '1',
  `moddate` datetime DEFAULT NULL,
  `superdefid` int(19) DEFAULT '0',
  `comcode` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`defid`),
  UNIQUE KEY `DefUniqueAlias` (`alias`)
);

DROP TABLE IF EXISTS `bpm_procdefver`;
CREATE TABLE  `bpm_procdefver` (
  `defverid` int(19) NOT NULL AUTO_INCREMENT,
  `filepath` varchar(255) DEFAULT NULL,
  `ver` int(19) DEFAULT NULL,
  `defid` int(19) DEFAULT NULL,
  `defname` varchar(255) DEFAULT NULL,
  `moddate` datetime DEFAULT NULL,
  `prodstartdate` datetime DEFAULT NULL,
  `isdeleted` int(1) DEFAULT '0',
  PRIMARY KEY (`defverid`)
);

DROP TABLE IF EXISTS `bpm_procinst`;
CREATE TABLE  `bpm_procinst` (
  `instid` int(19) NOT NULL AUTO_INCREMENT,
  `defverid` int(19) DEFAULT NULL,
  `defid` int(19) DEFAULT NULL,
  `defname` varchar(255) DEFAULT NULL,
  `defpath` varchar(255) DEFAULT NULL,
  `defmoddate` datetime DEFAULT NULL,
  `starteddate` datetime DEFAULT NULL,
  `finisheddate` datetime DEFAULT NULL,
  `duedate` datetime DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `info` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `isdeleted` int(1) DEFAULT '0',
  `isadhoc` int(1) DEFAULT '0',
  `isarchive` int(1) DEFAULT '0',
  `issubprocess` int(1) DEFAULT '0',
  `iseventhandler` int(1) DEFAULT '0',
  `rootinstid` int(19) DEFAULT NULL,
  `maininstid` int(19) DEFAULT NULL,
  `maindefverid` int(19) DEFAULT NULL,
  `mainacttrctag` varchar(20) DEFAULT NULL,
  `abstrcpath` varchar(200) DEFAULT NULL,
  `dontreturn` int(1) DEFAULT NULL,
  `moddate` datetime DEFAULT NULL,
  `ext1` varchar(100) DEFAULT NULL,
  `mainExecScope` varchar(20) DEFAULT NULL,
  `INITEP` varchar(100) DEFAULT NULL,
  `INITRSNM` varchar(100) DEFAULT NULL,
  `CURREP` varchar(100) DEFAULT NULL,
  `CURRRSNM` varchar(100) DEFAULT NULL,
  `STRATEGYID` int(19),
  PRIMARY KEY (`instid`)
);

DROP TABLE IF EXISTS `bpm_procvar`;
CREATE TABLE  `bpm_procvar` (
  `varid` int(19) NOT NULL AUTO_INCREMENT,
  `instid` int(19) DEFAULT NULL,
  `datatype` int(2) DEFAULT NULL,
  `valuestring` text,
  `valuelong` int(19) DEFAULT NULL,
  `valueboolean` int(1) DEFAULT NULL,
  `valuedate` datetime DEFAULT NULL,
  `varindex` int(3) DEFAULT NULL,
  `trctag` varchar(20) DEFAULT NULL,
  `isproperty` int(1) DEFAULT NULL,
  `moddate` datetime DEFAULT NULL,
  `keyname` varchar(50) DEFAULT NULL,
  `keystring` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`varid`)
);

DROP TABLE IF EXISTS `bpm_rolemapping`;
CREATE TABLE  `bpm_rolemapping` (
  `rolemappingid` int(19) NOT NULL AUTO_INCREMENT,
  `instid` int(19) DEFAULT NULL,
  `rootinstid` int(19) DEFAULT NULL,
  `rolename` varchar(20) DEFAULT NULL,
  `endpoint` varchar(255) DEFAULT NULL,
  `value` text,
  `resname` varchar(200) DEFAULT NULL,
  `assigntype` int(2) DEFAULT NULL,
  `assignparam1` varchar(100) DEFAULT NULL,
  `dispatchoption` int(2) DEFAULT NULL,
  `dispatchparam1` varchar(100) DEFAULT NULL,
  `groupid` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`rolemappingid`),
  KEY `bpm_rolemapping_idx` (`rolename`)
);

DROP TABLE IF EXISTS `bpm_rsrc_dim`;
CREATE TABLE  `bpm_rsrc_dim` (
  `rsrc_id` varchar(20) NOT NULL,
  `rsrc_name` varchar(100) DEFAULT NULL,
  `dept_name` varchar(100) DEFAULT NULL,
  `dept_id` varchar(20) DEFAULT NULL,
  `pstn_name` varchar(50) DEFAULT NULL,
  `pstn_id` varchar(20) DEFAULT NULL,
  `title` varchar(50) DEFAULT NULL,
  `gender` int(1) DEFAULT NULL,
  `birthday` int(4) DEFAULT NULL,
  PRIMARY KEY (`rsrc_id`)
);

DROP TABLE IF EXISTS `bpm_seq`;
CREATE TABLE  `bpm_seq` (
  `seq` int(11) DEFAULT NULL,
  `tbname` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `moddate` datetime DEFAULT NULL
);

DROP TABLE IF EXISTS `bpm_time_dim`;
CREATE TABLE  `bpm_time_dim` (
  `time_id` int(11) NOT NULL,
  `yr` int(4) DEFAULT NULL,
  `mnth` int(2) DEFAULT NULL,
  `qutr` int(1) DEFAULT NULL,
  `day` int(2) DEFAULT NULL,
  `moddate` datetime DEFAULT NULL,
  PRIMARY KEY (`time_id`)
);

DROP TABLE IF EXISTS `bpm_worklist`;
CREATE TABLE  `bpm_worklist` (
  `taskid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(200) DEFAULT NULL,
  `description` text,
  `endpoint` varchar(200) DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  `priority` bigint(20) unsigned DEFAULT NULL,
  `startdate` datetime DEFAULT NULL,
  `enddate` datetime DEFAULT NULL,
  `duedate` datetime DEFAULT NULL,
  `instid` int(19) DEFAULT NULL,
  `defid` int(19) DEFAULT NULL,
  `defname` varchar(200) DEFAULT NULL,
  `trctag` varchar(100) DEFAULT NULL,
  `tool` varchar(200) DEFAULT NULL,
  `parameter` text,
  `groupid` bigint(20) unsigned DEFAULT NULL,
  `groupname` bigint(20) unsigned DEFAULT NULL,
  `ext1` varchar(200) DEFAULT NULL,
  `ext2` varchar(200) DEFAULT NULL,
  `ext3` varchar(200) DEFAULT NULL,
  `ext4` varchar(200) DEFAULT NULL,
  `ext5` varchar(200) DEFAULT NULL,
  `ext6` varchar(200) DEFAULT NULL,
  `ext7` varchar(200) DEFAULT NULL,
  `ext8` varchar(200) DEFAULT NULL,
  `ext9` varchar(200) DEFAULT NULL,
  `ext10` varchar(200) DEFAULT NULL,
  `isurgent` int(1) DEFAULT NULL,
  `hasattachfile` int(1) DEFAULT NULL,
  `hascomment` int(1) DEFAULT NULL,
  `documentcategory` varchar(200) DEFAULT NULL,
  `isdeleted` int(1) DEFAULT NULL,
  `rootinstid` int(19) DEFAULT NULL,
  `dispatchoption` int(2) DEFAULT NULL,
  `dispatchparam1` varchar(100) DEFAULT NULL,
  `rolename` varchar(100) DEFAULT NULL,
  `resname` varchar(100) DEFAULT NULL,
  `refrolename` varchar(100) DEFAULT NULL,
  `execscope` varchar(5) DEFAULT NULL,
  `savedate` datetime DEFAULT NULL,
  PRIMARY KEY (`taskid`)
);

DROP TABLE IF EXISTS `comtable`;
CREATE TABLE  `comtable` (
  `COMCODE` varchar(20) NOT NULL DEFAULT '',
  `COMNAME` varchar(30) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `ISDELETED` varchar(1) DEFAULT '0',
  PRIMARY KEY (`COMCODE`)
);
insert into comtable (COMCODE, COMNAME) values ('uEngine', 'UENGINE');

DROP TABLE IF EXISTS `doc_comments`;
CREATE TABLE  `doc_comments` (
  `ID` int(11) NOT NULL,
  `INSTANCE_ID` int(11) DEFAULT NULL,
  `CONTENTS` varchar(3000) DEFAULT NULL,
  `OPT_TYPE` varchar(100) DEFAULT NULL,
  `EMPNO` varchar(20) DEFAULT NULL,
  `EMPNAME` varchar(100) DEFAULT NULL,
  `EMPTITLE` varchar(100) DEFAULT NULL,
  `TRACINGTAG` varchar(10) DEFAULT NULL,
  `CREATED_DATE` datetime DEFAULT NULL,
  `CREATED_BY` varchar(20) DEFAULT NULL,
  `UPDATED_DATE` datetime DEFAULT NULL,
  `UPDATED_BY` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
);

DROP TABLE IF EXISTS `emptable`;
CREATE TABLE  `emptable` (
  `empname` varchar(100) DEFAULT NULL,
  `empcode` varchar(20) NOT NULL DEFAULT '',
  `PASSWORD` varchar(20) DEFAULT NULL,
  `ISADMIN` int(1) DEFAULT NULL,
  `JIKNAME` varchar(100) DEFAULT NULL,
  `EMAIL` varchar(100) DEFAULT NULL,
  `PARTCODE` varchar(20) DEFAULT NULL,
  `GLobalCom` varchar(20) DEFAULT NULL,
  `ISDELETED` varchar(1) DEFAULT '0',
  `locale` varchar(10) DEFAULT 'en',
  `nateOn` varchar(50) DEFAULT NULL,
  `MSN` varchar(50) DEFAULT NULL,
  `MOBILECMP` varchar(50) DEFAULT NULL, 
  `MOBILENO` varchar(50) DEFAULT NULL,

  PRIMARY KEY (`empcode`)
);
insert into emptable (empname, empcode, partcode, globalcom, isadmin, password) values ('Tester', 'test', 'DEV', 'uEngine', 1, 'test');

DROP TABLE IF EXISTS `parttable`;
CREATE TABLE  `parttable` (
  `GLobalCom` varchar(20) DEFAULT NULL,
  `PARTCODE` varchar(20) NOT NULL DEFAULT '',
  `PARTNAME` varchar(30) DEFAULT NULL,
  `PARENT_PARTCODE` varchar(20) DEFAULT NULL,
  `ISDELETED` varchar(1) DEFAULT '0',
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`PARTCODE`)
);
insert into parttable (partcode, globalcom, partname) values ('DEV', 'uEngine', 'Development');

DROP TABLE IF EXISTS `roletable`;
CREATE TABLE  `roletable` (
  `rolecode` varchar(20) NOT NULL DEFAULT '',
  `descr` varchar(225) DEFAULT NULL,
  `comcode` varchar(20) DEFAULT NULL,
  `ISDELETED` varchar(1) DEFAULT '0',
  PRIMARY KEY (`rolecode`)
);
insert into roletable (rolecode, descr, comcode) values ('TM', 'Trouble Manager', 'uEngine');

DROP TABLE IF EXISTS `roleusertable`;
CREATE TABLE  `roleusertable` (
  `roleCode` varchar(20) NOT NULL DEFAULT '',
  `empCode` varchar(20) NOT NULL DEFAULT '',
  PRIMARY KEY (`roleCode`,`empCode`)
);
insert into roleusertable (rolecode, empCode) values ('TM', 'test');

DROP TABLE IF EXISTS `schedule_table`;
CREATE TABLE  `schedule_table` (
  `SCHE_IDX` int(11) NOT NULL DEFAULT '0',
  `INSTID` int(11) NOT NULL DEFAULT '0',
  `TRCTAG` varchar(100) NOT NULL DEFAULT '',
  `STARTDATE` timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
  --PRIMARY KEY (`INSTID`,`TRCTAG`)
);

update bpm_procdef set OBJTYPE='folder' where ISFOLDER='1';
update bpm_procdef set OBJTYPE='process' where OBJTYPE is NULL;

-------------------------------------------------------------
--TAG
DROP TABLE IF EXISTS `bpm_tag`;
CREATE TABLE `bpm_tag`(
`TAGID` INT NOT NULL AUTO_INCREMENT,
`NAME` VARCHAR (255) NOT NULL,
`COMCODE` VARCHAR (20) NOT NULL,
PRIMARY KEY (`TAGID`)
);

DROP TABLE IF EXISTS `bpm_tagmapping`;
CREATE TABLE `bpm_tagmapping`(
`TAGID` INT NOT NULL,
`INSTID` INT NOT NULL,
`ROOTINSTID` INT NOT NULL
);