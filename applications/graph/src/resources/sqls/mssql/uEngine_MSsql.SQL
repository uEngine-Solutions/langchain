/****** 개체:  Table [bpm_procdefver]    스크립트 날짜: 02/28/2010 13:03:21 ******/
DROP TABLE [bpm_procdefver]
GO
/****** 개체:  Table [bpm_procvar]    스크립트 날짜: 02/28/2010 13:03:21 ******/
DROP TABLE [bpm_procvar]
GO
/****** 개체:  Table [BPM_WORKLIST]    스크립트 날짜: 02/28/2010 13:03:22 ******/
DROP TABLE [BPM_WORKLIST]
GO
/****** 개체:  Table [bpm_seq]    스크립트 날짜: 02/28/2010 13:03:21 ******/
DROP TABLE [bpm_seq]
GO
/****** 개체:  Table [ROLEUSERTABLE]    스크립트 날짜: 02/28/2010 13:03:22 ******/
DROP TABLE [ROLEUSERTABLE]
GO
/****** 개체:  Table [SCHEDULE_TABLE]    스크립트 날짜: 02/28/2010 13:03:22 ******/
DROP TABLE [SCHEDULE_TABLE]
GO
/****** 개체:  Table [EMPTABLE]    스크립트 날짜: 02/28/2010 13:03:22 ******/
DROP TABLE [EMPTABLE]
GO
/****** 개체:  Table [COMTABLE]    스크립트 날짜: 02/28/2010 13:03:22 ******/
DROP TABLE [COMTABLE]
GO
/****** 개체:  Table [ROLETABLE]    스크립트 날짜: 02/28/2010 13:03:22 ******/
DROP TABLE [ROLETABLE]
GO
/****** 개체:  Table [BPM_PROCDEF]    스크립트 날짜: 02/28/2010 13:03:21 ******/
DROP TABLE [BPM_PROCDEF]
GO
/****** 개체:  Table [PARTTABLE]    스크립트 날짜: 02/28/2010 13:03:22 ******/
DROP TABLE [PARTTABLE]
GO
/****** 개체:  Table [BPM_PROCINST]    스크립트 날짜: 02/28/2010 13:03:21 ******/
DROP TABLE [BPM_PROCINST]
GO
/****** 개체:  Table [bpm_acltable]    스크립트 날짜: 02/28/2010 13:03:21 ******/
DROP TABLE [bpm_acltable]
GO
/****** 개체:  Table [bpm_rsrc_dim]    스크립트 날짜: 02/28/2010 13:03:21 ******/
DROP TABLE [bpm_rsrc_dim]
GO
/****** 개체:  Table [bpm_prfm_fact_2006]    스크립트 날짜: 02/28/2010 13:03:21 ******/
DROP TABLE [bpm_prfm_fact_2006]
GO
/****** 개체:  Table [BPM_ROLEMAPPING]    스크립트 날짜: 02/28/2010 13:03:21 ******/
DROP TABLE [BPM_ROLEMAPPING]
GO
/****** 개체:  Table [bpm_strinst]    스크립트 날짜: 02/28/2010 13:03:21 ******/
DROP TABLE [bpm_strinst]
GO
/****** 개체:  Table [bpm_strtg]    스크립트 날짜: 02/28/2010 13:03:22 ******/
DROP TABLE [bpm_strtg]
GO
/****** 개체:  Table [bpm_sch_mng]    스크립트 날짜: 04/25/2011 13:03:22 ******/
DROP TABLE [bpm_sch_mng]
GO
/****** 개체:  Table [BPM_STRTG_PARENTMAPPING]    스크립트 날짜: 05/11/2011 13:03:22 ******/
DROP TABLE [BPM_STRTG_PARENTMAPPING]
GO
/****** 개체:  Table [bpm_procdefver]    스크립트 날짜: 02/28/2010 13:03:21 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [bpm_procdefver](
	[defverid] [int] NOT NULL,
	[filePath] [nvarchar](255) NULL,
	[ver] [nvarchar](100) NULL,
	[defId] [int] NULL,
	[defName] [nvarchar](255) NULL,
	[modDate] [datetime] NULL,
	[prodStartDate] [datetime] NULL,
	[isDeleted] [int] NULL DEFAULT ((0)),
 CONSTRAINT [PK_BPM_PROCDEFVER] PRIMARY KEY CLUSTERED 
(
	[defverid] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** 개체:  Table [bpm_procvar]    스크립트 날짜: 02/28/2010 13:03:21 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [bpm_procvar](
    [varid] [int] NULL,
    [instId] [int] NULL,
    [dataType] [int] NULL,
    [valueString] [nvarchar](MAX) NULL,
    [valueLong] [int] NULL,
    [valueBoolean] [int] NULL,
    [valueDate] [datetime] NULL,
    [VarIndex] [int] NULL,
    [TrcTag] [nvarchar](20) NULL,
    [IsProperty] [int] NULL,
    [ModDate] [datetime] NULL,
    [KeyName] [nvarchar](100) NULL,
    [KeyString] [nvarchar](200) NULL,
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** 개체:  Table [BPM_WORKLIST]    스크립트 날짜: 02/28/2010 13:03:22 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [BPM_WORKLIST](
	[TASKID] [int] NOT NULL,
	[TITLE] [nvarchar](200) NULL,
	[DESCRIPTION] [nvarchar](500) NULL,
	[ENDPOINT] [nvarchar](200) NULL,
	[STATUS] [nvarchar](100) NULL,
	[PRIORITY] [int] NULL,
	[STARTDATE] [datetime] NULL,
	[ENDDATE] [datetime] NULL,
	[DUEDATE] [datetime] NULL,
	[INSTID] [int] NULL,
	[DEFID] [int] NULL,
	[DEFNAME] [nvarchar](200) NULL,
	[TRCTAG] [nvarchar](100) NULL,
	[TOOL] [nvarchar](200) NULL,
	[PARAMETER] [nvarchar](4000) NULL,
	[GROUPID] [int] NULL,
	[GROUPNAME] [int] NULL,
	[EXT1] [nvarchar](20) NULL,
	[EXT2] [nvarchar](20) NULL,
	[EXT3] [nvarchar](20) NULL,
	[EXT4] [nvarchar](20) NULL,
	[EXT5] [nvarchar](20) NULL,
	[EXT6] [nvarchar](20) NULL,
	[EXT7] [nvarchar](20) NULL,
	[EXT8] [nvarchar](20) NULL,
	[EXT9] [nvarchar](20) NULL,
	[EXT10] [nvarchar](20) NULL,
	[ISURGENT] [int] NULL,
	[HASATTACHFILE] [int] NULL,
	[HASCOMMENT] [int] NULL,
	[DOCUMENTCATEGORY] [nvarchar](200) NULL,
	[ISDELETED] [int] NULL,
	[ROOTINSTID] [int] NULL,
	[DISPATCHOPTION] [int] NULL,
	[DISPATCHPARAM1] [nvarchar](100) NULL,
	[ROLENAME] [nvarchar](100) NULL,
	[RESNAME] [nvarchar](100) NULL,
	[REFROLENAME] [nvarchar](100) NULL,
	[EXECSCOPE] [nvarchar](5) NULL,
	[SAVEDATE] [datetime] NULL,
 CONSTRAINT [PK_WORKLIST] PRIMARY KEY CLUSTERED 
(
	[TASKID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** 개체:  Table [bpm_seq]    스크립트 날짜: 02/28/2010 13:03:21 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [bpm_seq](
	[seq] [int] NULL,
	[tbname] [nvarchar](255) NULL,
	[description] [nvarchar](255) NULL,
	[moddate] [datetime] NULL
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** 개체:  Table [ROLEUSERTABLE]    스크립트 날짜: 02/28/2010 13:03:22 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [ROLEUSERTABLE](
	[roleCode] [nvarchar](20) NOT NULL,
	[empCode] [nvarchar](20) NOT NULL,
 CONSTRAINT [PK_GROUPUSERTABLE] PRIMARY KEY CLUSTERED 
(
	[roleCode] ASC,
	[empCode] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** 개체:  Table [SCHEDULE_TABLE]    스크립트 날짜: 02/28/2010 13:03:22 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [SCHEDULE_TABLE](
	[IDX] [int] NULL,
	[INSTID] [int] NULL,
	[TRCTAG] [nvarchar](100) NULL,
	[STARTDATE] [timestamp] NOT NULL
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** 개체:  Table [EMPTABLE]    스크립트 날짜: 02/28/2010 13:03:22 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [EMPTABLE](
	[EMPCODE] [nvarchar](20) NOT NULL,
	[PASSWORD] [nvarchar](20) NULL,
	[ISADMIN] [int] NULL,
	[JIKNAME] [nvarchar](128) NULL,
	[JIKNAME_KO] [nvarchar](100) NULL,
	[POSITION] [nvarchar](128) NULL,
    [POSITION_KO] [nvarchar](100) NULL,
	[EMAIL] [nvarchar](100) NULL,
	[PARTCODE] [nvarchar](64) NULL,
	[GLOBALCOM] [nvarchar](20) NULL,
	[ISDELETED] [nvarchar](1) NULL DEFAULT ('0'),
	[LOCALE] [nvarchar](10) NULL DEFAULT ('en'),
	[NATEON] [nvarchar](50) NULL,
	[MSN] [nvarchar](50) NULL,
	[MOBILECMP] [nvarchar](10) NULL,
	[MOBILENO] [nvarchar](15) NULL,
	[EMPNAME] [nvarchar](100) NULL,
	[EMPNAME_KO] [nvarchar](100) NULL,
	[WINS_ID] [nvarchar](100) NULL,
	[PHOTO_FILE_NAME] [nvarchar](100) NULL,
	[ORDERBY] [nvarchar](64) NULL,
	[ISVIEW] [char](1) NULL,
 CONSTRAINT [PK_EMPTABLE] PRIMARY KEY CLUSTERED 
(
	[EMPCODE] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** 개체:  Table [COMTABLE]    스크립트 날짜: 02/28/2010 13:03:22 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [COMTABLE](
	[COMCODE] [nvarchar](20) NOT NULL,
	[COMNAME] [nvarchar](30) NULL,
	[DESCRIPTION] [nvarchar](255) NULL,
	[ISDELETED] [nvarchar](1) NULL DEFAULT ('0'),
 CONSTRAINT [PK_COMTABLE] PRIMARY KEY CLUSTERED 
(
	[COMCODE] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** 개체:  Table [ROLETABLE]    스크립트 날짜: 02/28/2010 13:03:22 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [ROLETABLE](
	[ROLECODE] [nvarchar](20) NOT NULL,
	[NATION] [nvarchar](20) NULL,
	[AGENT] [nvarchar](20) NULL,
	[DEPOT] [nvarchar](20) NULL,
	[COMCODE] [nvarchar](20) NULL,
	[DESCR] [nvarchar](255) NULL,
	[ISDELETED] [nvarchar](1) NULL DEFAULT ('0'),
 CONSTRAINT [PK_ROLETABLE] PRIMARY KEY CLUSTERED 
(
	[ROLECODE] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** 개체:  Table [BPM_PROCDEF]    스크립트 날짜: 02/28/2010 13:03:21 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [BPM_PROCDEF](
	[DEFID] [int] NOT NULL,
	[DESCRIPTION] [nvarchar](255) NULL,
	[PARENTFOLDER] [int] NULL,
	[ISFOLDER] [int] NULL DEFAULT ((0)),
	[ISADHOC] [int] NULL DEFAULT ((0)),
	[OBJTYPE] [nvarchar](10) NULL,
	[PRODVER] [int] NULL DEFAULT ((0)),
	[PRODVERID] [int] NULL,
	[NAME] [nvarchar](255) NULL,
	[ISDELETED] [int] NULL DEFAULT ((0)),
	[MODDATE] [datetime],
	[ALIAS] [nvarchar](100) NULL,
	[COMCODE] [nvarchar](20) NULL,
	[ISVISIBLE] [int] NULL,
 CONSTRAINT [PK_BPM_PROCDEF] PRIMARY KEY CLUSTERED 
(
	[DEFID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** 개체:  Table [PARTTABLE]    스크립트 날짜: 02/28/2010 13:03:22 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [PARTTABLE](
	[GLOBALCOM] [nvarchar](20) NULL,
	[PARTCODE] [nvarchar](64) NOT NULL,
	[PARTNAME] [nvarchar](128) NULL,
	[PARTNAME_KO] [nvarchar](64) NULL,
	[PARENT_PARTCODE] [nvarchar](64) NULL,
	[ISDELETED] [nvarchar](1) NULL DEFAULT ('0'),
	[DESCRIPTION] [nvarchar](255) NULL,
	[ORDERBY] [nvarchar](64) NULL,
    [ISVIEW] [char](1) NULL,
 CONSTRAINT [PK_PARTTABLE] PRIMARY KEY CLUSTERED 
(
	[PARTCODE] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** 개체:  Table [BPM_PROCINST]    스크립트 날짜: 02/28/2010 13:03:21 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [BPM_PROCINST](
	[INSTID] [int] NOT NULL,
	[DEFVERID] [int] NULL,
	[DEFID] [int] NULL,
	[DEFNAME] [nvarchar](255) NULL,
	[DEFPATH] [nvarchar](255) NULL,
	[DEFMODDATE] [datetime] NULL,
	[STARTEDDATE] [datetime] NULL,
	[FINISHEDDATE] [datetime] NULL,
	[DUEDATE] [datetime] NULL,
	[STATUS] [nvarchar](20) NULL,
	[INFO] [nvarchar](255) NULL,
	[NAME] [nvarchar](255) NULL,
	[ISDELETED] [int] NULL DEFAULT ((0)),
	[ISADHOC] [int] NULL DEFAULT ((0)),
	[ISARCHIVE] [int] NULL DEFAULT ((0)),
	[ISSUBPROCESS] [int] NULL DEFAULT ((0)),
	[ISEVENTHANDLER] [int] NULL DEFAULT ((0)),
	[ROOTINSTID] [int] NULL,
	[MAININSTID] [int] NULL,
	[MAINDEFVERID] [int] NULL,
	[MAINACTTRCTAG] [nvarchar](20) NULL,
	[MAINEXECSCOPE] [nvarchar](20) NULL,
	[ABSTRCPATH] [nvarchar](200) NULL,
	[DONTRETURN] [int] NULL,
	[MODDATE] [datetime] NULL,
	[EXT1] [nvarchar](100) NULL,
	[INITEP] [nvarchar](100) NULL,
	[INITRSNM] [nvarchar](100) NULL,
	[CURREP] [nvarchar](100) NULL,
	[CURRRSNM] [nvarchar](100) NULL,
	/* [STRATEGYID] [int] NULL, */
 CONSTRAINT [PK_BPM_PROCINST] PRIMARY KEY CLUSTERED 
(
	[INSTID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** 개체:  Table [bpm_acltable]    스크립트 날짜: 02/28/2010 13:03:21 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [bpm_acltable](
	[acltableid] [int] IDENTITY(1,1) NOT NULL,
	[defid] [int] NULL,
	[comcode] [nvarchar](20) NULL,
	[partcode] [nvarchar](20) NULL,
	[empcode] [nvarchar](20) NULL,
	[rolecode] [nvarchar](20) NULL,
	[defaultuser] [char](1) NULL,
	[permission] [char](1) NULL,
 CONSTRAINT [PK_BPM_ACLTABLE] PRIMARY KEY CLUSTERED 
(
	[acltableid] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** 개체:  Table [bpm_rsrc_dim]    스크립트 날짜: 02/28/2010 13:03:21 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [bpm_rsrc_dim](
	[rsrc_id] [nvarchar](20) NOT NULL,
	[rsrc_name] [nvarchar](100) NULL DEFAULT (NULL),
	[dept_name] [nvarchar](100) NULL DEFAULT (NULL),
	[dept_id] [nvarchar](20) NULL DEFAULT (NULL),
	[pstn_name] [nvarchar](50) NULL DEFAULT (NULL),
	[pstn_id] [nvarchar](20) NULL DEFAULT (NULL),
	[title] [nvarchar](50) NULL DEFAULT (NULL),
	[gender] [int] NULL DEFAULT (NULL),
	[birthday] [int] NULL DEFAULT (NULL),
PRIMARY KEY CLUSTERED 
(
	[rsrc_id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** 개체:  Table [bpm_prfm_fact_2006]    스크립트 날짜: 02/28/2010 13:03:21 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [bpm_prfm_fact_2006](
	[inst_id] [int] NULL DEFAULT (NULL),
	[rootinst_id] [int] NULL DEFAULT (NULL),
	[rsrc_id] [nvarchar](50) NULL DEFAULT (NULL),
	[time_id] [int] NULL DEFAULT (NULL),
	[act_id] [nvarchar](100) NULL DEFAULT (NULL),
	[act_name] [nvarchar](100) NULL DEFAULT (NULL),
	[def_id] [int] NULL DEFAULT (NULL),
	[def_name] [nvarchar](200) NULL DEFAULT (NULL),
	[prsngtime] [int] NULL DEFAULT (NULL),
	[trnrndtime] [int] NULL DEFAULT (NULL),
	[idletime] [int] NULL DEFAULT (NULL),
	[chovrtime] [int] NULL DEFAULT (NULL),
	[sspndtime] [int] NULL DEFAULT (NULL),
	[cost] [int] NULL DEFAULT (NULL),
	[sla_stftry] [int] NULL DEFAULT (NULL),
	[modtime] [datetime] NULL DEFAULT (NULL),
	[DEADLNHT] [int] NULL DEFAULT (NULL)
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** 개체:  Table [BPM_ROLEMAPPING]    스크립트 날짜: 02/28/2010 13:03:21 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [BPM_ROLEMAPPING](
	[ROLEMAPPINGID] [int] IDENTITY(1,1) NOT NULL,
	[INSTID] [int] NULL,
	[ROOTINSTID] [int] NULL,
	[ROLENAME] [nvarchar](20) NULL,
	[ENDPOINT] [nvarchar](255) NULL,
	[VALUE] [nvarchar](4000) NULL,
	[RESNAME] [nvarchar](200) NULL,
	[ASSIGNTYPE] [int] NULL,
	[ASSIGNPARAM1] [nvarchar](100) NULL,
	[DISPATCHOPTION] [int] NULL,
	[DISPATCHPARAM1] [nvarchar](100) NULL,
	[GROUPID] [nvarchar](30) NULL,
 CONSTRAINT [PK_BPM_ROLEMAPPING] PRIMARY KEY CLUSTERED 
(
	[ROLEMAPPINGID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** 개체:  Table [bpm_strinst]    스크립트 날짜: 02/28/2010 13:03:21 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [bpm_strinst](
	[INSTID] [int] NULL DEFAULT (NULL),
	[STRTGID] [int] NULL DEFAULT (NULL),
	[ISDELETED] [nvarchar](1) NULL DEFAULT ('0')
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** 개체:  Table [bpm_strtg]    스크립트 날짜: 02/28/2010 13:03:22 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE "BPM_STRTG" (
   STRTGID INT,
   STRTGNM VARCHAR(100),
   TYPE VARCHAR(30),
   ISDELETED VARCHAR(1)DEFAULT '0',
   STATUS CHAR(10),
   INSTCNT int,
   CMPLTINSTCNT int,
   DESCRIPTION VARCHAR(255),
   COMCODE VARCHAR(20),
   STARTDATE date,
   ENDDATE date,
   PARTCODE VARCHAR(20),
   PARTNAME VARCHAR(100),
   CONSTRAINT PK_BPM_STRTG
   PRIMARY KEY ( STRTGID )
);

--CREATE TABLE [bpm_strtg](
--	[STRTGID] [int] NOT NULL,
--	[STRTGNM] [nvarchar](100) NULL DEFAULT (NULL),
--	[TYPE] [nvarchar](30) NULL DEFAULT (NULL),
--	[PARENTID] [int] NULL DEFAULT (NULL),
--	[ISDELETED] [nvarchar](1) NULL DEFAULT ('0'),
--	[STATUS] [char](10) NULL DEFAULT (NULL),
--	[INSTCNT] [int] NULL DEFAULT (NULL),
--	[CMPLTINSTCNT] [int] NULL DEFAULT (NULL),
--PRIMARY KEY CLUSTERED 
--(
--	[STRTGID] ASC
--)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
--) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO


CREATE TABLE "BPM_DEADLN_DIM"
(
   DEADLN_ID int NOT NULL,
   DEADLN_DESC varchar(255),
   CONSTRAINT PK_BPM_DEADLN_DIM
   PRIMARY KEY ( DEADLN_ID )
)
;

/****** 개체:  Table [BPM_TAG]    스크립트 날짜: 03/17/2011 16:03:22 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [BPM_TAG](
    [TAGID] [INT] NOT NULL,
    [NAME] [nvarchar](255) NOT NULL,
    [COMCODE] [nvarchar](20) NOT NULL,
 CONSTRAINT [PK_BPM_TAG] PRIMARY KEY CLUSTERED 
(
    [TAGID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

/****** 개체:  Table [BPM_TAGMAPPING]    스크립트 날짜: 03/17/2011 16:03:22 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [BPM_TAGMAPPING](
    [TAGID] [INT] NOT NULL,
    [INSTID] [INT] NOT NULL,
    [ROOTINSTID] [INT] NOT NULL
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

/****** 개체:  Table [BPM_STRTG_PARENTMAPPING]    스크립트 날짜: 05/11/2011 16:03:22 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [BPM_STRTG_PARENTMAPPING](
     [STRTGID] [INT]
    ,[PARENTID] [INT]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO

INSERT INTO BPM_STRTG (STRTGID, STRTGNM,TYPE,DESCRIPTION,COMCODE) VALUES ('0', 'uEngine','','','uEngine');
INSERT INTO BPM_DEADLN_DIM (DEADLN_ID, DEADLN_DESC) VALUES (0, 'Complete in Duedate');
INSERT INTO BPM_DEADLN_DIM (DEADLN_ID, DEADLN_DESC) VALUES (1, 'Delay Work');
insert into COMTABLE (COMCODE, COMNAME) values ('uEngine', 'UENGINE');
/**insert into emptable (empname, empcode, partcode, globalcom, isadmin, password) values ('Tester', 'test', 'DEV', 'uEngine', 1, 'test');
insert into parttable (partcode, globalcom, partname) values ('DEV', 'uEngine', 'Development');
insert into roletable (rolecode, descr, comcode) values ('TM', 'Trouble Manager', 'uEngine');
insert into roleUserTable (rolecode, empCode) values ('TM', 'test');**/
INSERT INTO BPM_STRTG_PARENTMAPPING (STRTGID, PARENTID) VALUES (0, -1);