package config;


import org.apache.log4j.Logger;

/**
 * contains the main configuration for the project which are defined in
 * evidenceserver.properties
 */
public class MagicStrings {

	private static Logger logger = Logger.getLogger(MagicStrings.class.getName());

	public static final String DB = "db";

	public static final String ROOTPATH = PropUtil
			.getProp("rootPath");

	public static final String MOODLEURL = PropUtil
			.getProp("moodleURL");
	//public static final String RESTURLCompetence = "http://172.20.10.10:8084";
	public static final String RESTURLCompetence = "http://localhost:8084";
	public static final String ICONPATHMOODLE = "icons/WindowsIcons-master/WindowsPhone/svg";
	public static final String EPOSLocation = PropUtil
			.getRelativeFileProp("eposfile");
	public static final String GERMANMODELLOCATION = "C:/Users/dehne/Desktop/Computerlinguistic/stanford-parser-full-2015-04-20/germanPCFG.ser.gz";
	public static final String thesaurusLogin = PropUtil
			.getProp("thesaurusLogin");
	public static final String thesaurusPassword = PropUtil
			.getProp("thesaurusPassword");
	public static final String thesaurusDatabaseName = PropUtil
			.getProp("thesaurusDatabaseName");
	public static final String thesaurusDatabaseUrl = PropUtil
			.getProp("thesaurusDatabaseUrl");
	public static final String NEO4JURL = PropUtil
			.getProp("neo4jURL");
	public static final String LOG4JLOCATION = PropUtil.getProp("log4jlocation");

	public static final String UNIVERSITIESDBNAME = PropUtil.getProp("universitiesDatabaseName");
	public static final String UNIVERITIESINITTABLE= PropUtil.getProp("universitiesInitTable");

/*	public static final String crawlerOutputDirPath = PropUtil.getProp("crawlerOutputDirPath");
	public static final String stichWortVarPath = crawlerOutputDirPath + "/stichUrl.csv";
	public static final String varMetaPath = crawlerOutputDirPath + "/varMeta.csv";
	public static final String dataPath = crawlerOutputDirPath + "/moodle_data.csv";
	public static final String stichWortPath = crawlerOutputDirPath + "/stichVar.csv";*/

	public static final String stichWortSuffix = PropUtil.getProp("stichWortSuffix");
	public static final String varMetaSuffix = PropUtil.getProp("varMetaSuffix");
	public static final String minPercentile = PropUtil.getProp("minPercentile");
	public static final String maxPercentile = PropUtil.getProp("maxPercentile");
	public static final String maxSolrDocs = PropUtil.getProp("maxSolrDocs");


}
