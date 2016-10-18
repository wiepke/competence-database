package uzuzjmd.competence.crawler.datatype;

import config.MagicStrings;
import mysql.MysqlConnect;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import uzuzjmd.competence.crawler.exception.NoDomainFoundException;
import uzuzjmd.competence.crawler.exception.NoHochschuleException;
import uzuzjmd.competence.crawler.exception.NoResultsException;
import uzuzjmd.competence.crawler.solr.SolrConnector;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by carl on 06.01.16.
 */
public class Model implements PersistenceModel {
    static private final Logger logger = LogManager.getLogger(Model.class.getName());
    private StichwortVar stichwortVar;
    private VarMeta varMeta;
    private HashMap<String, SolrDocumentList> stichwortResult;
    private String delimiter = ";";
    private Urls urls;
    private Boolean fileBased = false;
    private String database;

    private static final String connextionString = "jdbc:mysql://" + MagicStrings.thesaurusDatabaseUrl +
            "/" + MagicStrings.UNIVERSITIESDBNAME +
            "?user=" + MagicStrings.thesaurusLogin +
            "&password=" + MagicStrings.thesaurusPassword;

    public Model() throws NoResultsException {
        stichwortVar = new StichwortVar();
        varMeta = new VarMeta();
        stichwortResult = new HashMap<>();
        urls = new Urls();
        this.database = "";
    }

   public Model(String database) throws NoResultsException {
       stichwortVar = new StichwortVar();
       varMeta = new VarMeta();
       stichwortResult = new HashMap<>();
       urls = new Urls();
       this.database = database;
   }

    public void addDate(String stichwort, String variable){
        logger.debug("Entering addDate with stichwort " + stichwort + " variable " + variable
            );
        if (stichwort.length() > 0) {
            stichwortVar.addElement(stichwort, variable);
        }
        logger.debug("Leaving addDate");
    }

    public String[] toNeo4JQuery() {
        logger.debug("Entering toNeo4JQuery");

        String[] result = (String[]) ArrayUtils.addAll( new String[]{deleteModelInNeo4J()},  stichwortVar.toNeo4JQuery());
        logger.debug("Leaving toNeo4JQuery");
        return result;
    }

    public String deleteModelInNeo4J() {
        return "MATCH (n:Stichwort),(m:Meta),(o:Variable) DETACH DELETE m,n,o";
    }

    /**
     * not used right now */
    public void insertSynonyms() {
        logger.debug("Entering insertSynonyms");
        stichwortVar.insertSynonym();
        logger.debug("Leaving insertSynonyms");
    }

    /**
     * stichworte scored based on solr
     * @param connector
     * @param filepath
     * @throws IOException
     * @throws SolrServerException
     */
    public void scoreStichwort(SolrConnector connector, String filepath) throws IOException, SolrServerException, InterruptedException {
        logger.debug("Entering scoreStichwort with SolrConnector:" + connector.getServerUrl() + ", filepath: " + filepath);
        int i = 1;
        int size = stichwortVar.getElements().keySet().size();
        for (String key :
                stichwortVar.getElements().keySet()) {
            logger.debug("scoreStich Object " + i + " from " + size);
            if (Thread.interrupted()) {
                logger.warn("Thread has been interrupted");
                throw new InterruptedException("Thread interruption forced");
            }
            i++;
            boolean noMoreResponses = true;
            int iterationCount = 0;
            while (noMoreResponses) {
                noMoreResponses = connector.connectToSolr(key, iterationCount);

                if (fileBased) {
                    solrListToFile(key, connector.response.getResults(), filepath);
                } else {
                    solrListToDB(key, connector.response.getResults(), iterationCount);
                }
                connector.response = null;
                iterationCount++;
                System.gc();
            }
        }
        logger.debug("Leaving scoreStichwort");
    }


    public void solrListToDB(String key, SolrDocumentList solrList, int iterationCount) throws InterruptedException {
        logger.debug("Entering solrListToDB with " + key);
        int sizeOfStichwortResult = Math.min(((int) solrList.getNumFound())
                - (Integer.parseInt(MagicStrings.maxSolrDocs) * iterationCount),
                Integer.parseInt(MagicStrings.maxSolrDocs) - 1);
        MysqlConnect connect = new MysqlConnect();
        connect.connect(connextionString);
        connect.issueInsertOrDeleteStatement("set global max_connections = 20000000000;");
        connect.issueInsertOrDeleteStatement("use " + MagicStrings.UNIVERSITIESDBNAME + ";");
        for (int i = 0; i < sizeOfStichwortResult; i++) {
            if (Thread.interrupted()) {
                logger.warn("Thread has been interrupted");
                throw new InterruptedException("Thread interruption forced");
            }
            String query = "INSERT INTO `" + database
                    + "_ScoreStich` (`Stichwort`, `id`, `SolrScore`) VALUES (?,?,?);";
            connect.issueInsertOrDeleteStatement(query,
                    key,  solrList.get(i).getFieldValue("id"), solrList.get(i).getFieldValue("score"));
        }
        connect.close();

        logger.debug("Leaving solrListToDB");
    }

    /**
     * write the first line based on a string
     * @param filepath
     * @param message
     * @param e
     * @param message2
     * @throws IOException
     */
    private void writeFirstLineOfKeyUrlFile(String filepath, String message, String e, String message2) throws IOException {
        logger.debug(message);
        List<String> lines = new ArrayList<String>();
        lines.add(e);
        Path file = Paths.get(filepath);
        Files.write(file, lines, Charset.forName("UTF-8"));
        logger.debug(message2);
    }


    private void issueStatement(String connextionString, String statement2) {
        MysqlConnect connect = new MysqlConnect();
        connect.connect(connextionString);
        connect.issueInsertOrDeleteStatement("set global max_connections = 20000000000;");
        connect.issueInsertOrDeleteStatement("use " + MagicStrings.UNIVERSITIESDBNAME + ";");
        connect.issueInsertOrDeleteStatement(statement2);
        connect.close();
    }


    public void solrListToFile(String key, SolrDocumentList solrList, String filepath) throws IOException {
        int sizeOfStichwortResult = Math.min((int) solrList.getNumFound(), SolrConnector.getLimit());
        logger.debug("Entering solrListToFile");
        List<String> lines = new ArrayList<String>();
        for (int i = 0; i < sizeOfStichwortResult; i++) {
            SolrDocument doc = solrList.get(i);
            lines.add(key + delimiter + doc.getFieldValue("id") + delimiter + doc.getFieldValue("score"));
        }
        Path file = Paths.get(filepath);
        Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);

        logger.debug("Leaving solrListToFile");
    }

    public void scoreVariable(SolrConnector connector, String filepath) throws IOException, SolrServerException, URISyntaxException, InterruptedException {
        logger.debug("Entering scoreVariable with SolrConnector:" + connector.getServerUrl());
        HashMap<String, String> varStich = stichwortVar.toVarStichwort();
        int i = 1;
        int size = varStich.keySet().size();
        for (String key: varStich.keySet()) {
            logger.debug("scoreVar Object " + i + " from " + size);
            if (Thread.interrupted()) {
                logger.warn("Thread has been interrupted");
                throw new InterruptedException("Thread interruption forced");
            }
            i++;

            boolean noMoreResponses = true;
            int iterationCount = 0;
            while (noMoreResponses) {
                noMoreResponses = connector.connectToSolr(varStich.get(key), iterationCount);
                varMetaToCsv(key, connector.response.getResults(), filepath,
                        StringUtils.join(varStich.get(key).split("\" OR \""), ", "), iterationCount);
                iterationCount++;
                connector.response = null;
                System.gc();
            }
        }
        logger.debug("Leaving scoreVariable");
    }


    /******************************* StichVar *************************************/
    /**
     * initialize the keyword-url file or table
     * @param filepath
     * @throws IOException
     */
    @Override
    public void initStichFile(String filepath) throws IOException {
        if (fileBased) {
            writeFirstLineOfKeyUrlFile(filepath, "Entering initStichFile", "Stichwort" + delimiter + "URL" + delimiter + "SolrScore", "Leaving initStichFile");
        } else {
            issueStatement(connextionString, "CREATE TABLE IF NOT EXISTS `" + database + "_ScoreStich` (Stichwort TEXT, id TEXT, SolrScore DOUBLE);");
            issueStatement(connextionString, "TRUNCATE TABLE `" + database + "_ScoreStich`;");
        }
    }

    @Override
    public void stichwortVarToCsv(String filepath) throws IOException {
        if (fileBased) {
            logger.debug("Entering stichwortVarToCsv with filepath:" + filepath);
            List<String> lines = new ArrayList<String>();
            lines.add("Stichwort" + delimiter + "Variable");
            for (String key : stichwortVar.getElements().keySet()) {
                lines.add(key + delimiter + stichwortVar.getElements().get(key));
            }
            Path file = Paths.get(filepath);
            Files.write(file, lines, Charset.forName("UTF-8"));

            logger.debug("Leaving stichwortVarToCsv");
        } else {
            for (String key : stichwortVar.getElements().keySet()) {
                MysqlConnect connect = new MysqlConnect();
                connect.connect(connextionString);
                connect.issueInsertOrDeleteStatement("use " + MagicStrings.UNIVERSITIESDBNAME+ ";");
                //TODO change table name to dynamic topic
                connect.issueInsertOrDeleteStatement("CREATE TABLE IF NOT EXISTS stichVariable (Stichworte TEXT, Variable TEXT);");
                connect.issueInsertOrDeleteStatement("INSERT INTO stichVariable (Stichworte, Variable) VALUES (?,?);", key.replaceAll("'", "" ), stichwortVar.getElements().get(key).replaceAll("'", "" ));
                connect.close();
            }
        }
    }

    public int stichwortVarSize() {
        return stichwortVar.getElements().size();
    }

    /************************** VarMeta *****************************************/
    /**
     * create first line or table of varMetaFile
     * @param filepath
     * @throws IOException
     */
    @Override
    public void initVarMetaFile(String filepath) throws IOException {
        if (fileBased) {
            writeFirstLineOfKeyUrlFile(filepath, "Entering initVarMetaFile", "Variable" + delimiter + "Stichworte"
                    + delimiter + "Hochschule" + delimiter
                    + "Content" + delimiter + "SolrScore" + delimiter + "URL" + delimiter +
                    "Depth" + delimiter + "Lat" + delimiter + "Lon", "Leaving initVarMetaFile");
        } else {
            issueStatement(connextionString, "CREATE TABLE IF NOT EXISTS `" + database + "_"
                    + MagicStrings.varMetaSuffix + "` (Variable TEXT, Stichworte TEXT,"
                    + "Hochschule TEXT, Content TEXT, SolrScore TEXT, URL TEXT, Depth TEXT, Lat DOUBLE, Lon Double);");
            issueStatement(connextionString, "TRUNCATE TABLE `" + database + "_" + MagicStrings.varMetaSuffix + "`;");
        }
    }


    @Override
    public void varMetaToCsv(String key, SolrDocumentList solrList, String filepath, String stich, int iterationCount)
            throws IOException, URISyntaxException, InterruptedException {
        List<String> lines = new ArrayList<>();
        int sizeOfStichwortResult = Math.min(((int) solrList.getNumFound())
                        - (Integer.parseInt(MagicStrings.maxSolrDocs) * iterationCount),
                Integer.parseInt(MagicStrings.maxSolrDocs) - 1);
        for (int i = 0; i < sizeOfStichwortResult; i++) {
            if (Thread.interrupted()) {
                logger.warn("Thread has been interrupted");
                throw new InterruptedException("Thread interruption forced");
            }
            try {
                if ( i == 55561)
                    logger.debug(i + " from " + sizeOfStichwortResult + " " +  stich);
                writeLine(key, solrList, filepath, stich, lines, i);
            } catch (NoResultsException e) {
                logger.error("No Results");
                e.printStackTrace();
            }
        }
    }

    private void writeLine(String key, SolrDocumentList solrList, String filepath, String stich, List<String> lines, int i) throws URISyntaxException, NoResultsException {
        SolrDocument doc = solrList.get(i);
        URI uri = new URI("");

        //Get Domain
        try {
            uri = new URI(((String) doc.getFieldValue("url")).replace(" ", "").replace("[", "").replace("]", "").replace("`", "").split("\\?")[0]);

        } catch (URISyntaxException e) {
            logger.error(((String) doc.getFieldValue("url")).replace(" ", "").replace("[", "").replace("]", "").replace("`", "").split("\\?")[0]);
            logger.error(e);
            for ( String part : ((String) doc.getFieldValue("url")).split("/") ) {
                if ( part.contains("www")) {
                    try {
                        uri = new URI("http://" + part);
                        break;
                    } catch (URISyntaxException use) {
                        logger.error(part);
                        logger.error(use);
                        throw use;
                    }
                }
            }
        }

        if (uri.getHost() == null) {
            return;
        }
        String domain = uri.getHost();
        String[] tempStrings = domain.split("\\.");
        if (tempStrings.length >= 0) {
            domain = tempStrings[tempStrings.length - 2];
        } else {
            logger.debug(uri.getHost() + " Has no dot");
        }
        urls.addDomain(domain, uri.getHost());


        //concatenate result
        try {
            if (
                // (domain.contains("qualitaetspakt-lehre")) ||
                    urls.isHochschule(domain) ) {
                String hochschulname;
                String latLon = "";
                try {
                    UrlHochschule urlh = urls.getHochschule(domain);
                    hochschulname = urlh.Hochschulname;
                    latLon = urlh.lat + delimiter + urlh.lon;
                } catch (NoDomainFoundException e) {
                    hochschulname = "";
                } catch (NoHochschuleException e) {
                    hochschulname = domain;
                }

                Double score = Double.valueOf(doc.getFieldValue("score").toString()) * 1000;
                if (score < 0.0000001) {
                    score = 0.0;
                }

                String col1 = key.replaceAll(delimiter, ":").replaceAll("'", "");
                //String col2 = StringUtils.join(varMeta.getElements().get(key).metaVar, ";").replaceAll(delimiter, ":").replaceAll(delimiter, ":").replaceAll("'", "");
                String col3 = stich.replaceAll(delimiter, ":").replaceAll("'", "");
                String col4 = hochschulname.replaceAll(delimiter, ":").replaceAll("'", "");
                //TODO Wieder den Content einfügen
                //String col5 = doc.getFieldValue("content").toString().replaceAll(delimiter, ":" ).replaceAll("'", "") ;
                String col5 = "";
                String col6 = score.toString().replaceAll(delimiter, ":").replaceAll("'", "");
                String col7 = doc.getFieldValue("url").toString().replaceAll(delimiter, ":").replaceAll("'", "");
                String col8 = doc.getFieldValue("pageDepth").toString().replaceAll(delimiter, ":").replaceAll("'", "");

                if (fileBased) {
                    writeVarMetaToFile(filepath, lines, latLon, col1, col3, col4, col5, col6, col7, col8);
                } else {
                    writeVarMetaToDB(latLon, col1, col3, col4, col5, col6, col7, col8);
                }
            }
        } catch (Exception e) {
            logger.warn("Something went wrong " + domain + " pageDepth " + doc.getFieldValue("pageDepth") + " so far");
            logger.warn(e);
        }

    }

    private void writeVarMetaToDB(String latLon, String col1, String col3, String col4, String col5, String col6, String col7, String col8) {
        MysqlConnect connect = new MysqlConnect();
        if (! latLon.contains(delimiter)) {
            return;
        }
        if ( latLon.contains("0;0") || latLon.contains("0.0;0.0")){
            //logger.debug("is " + latLon);
            return;
        }
        connect.connect(connextionString);
        connect.issueInsertOrDeleteStatement("use " + MagicStrings.UNIVERSITIESDBNAME + ";");
        connect.issueInsertOrDeleteStatement("INSERT INTO `" + database + "_"
                + MagicStrings.varMetaSuffix + "` (`Variable`, `Stichworte`, `Hochschule`, `Content`,"
                + "`SolrScore`, `URL`, `Depth`, `Lat`, `Lon`) VALUES (?,?,?,?,?,?,?,?,?)"
                , col1, col3, col4, col5, col6, col7, col8, latLon.split(delimiter)[0]
                , latLon.split(delimiter)[1]);
        connect.close();
    }

    private void writeVarMetaToFile(String filepath, List<String> lines, String latLon, String col1, String col3, String col4, String col5, String col6, String col7, String col8) throws IOException {
        logger.debug("Entering varMetaToCsv");
        lines.add(col1 + delimiter
                + col3 + delimiter
                + col4 + delimiter
                + col5 + delimiter
                + col6 + delimiter
                + col7 + delimiter
                + col8+ delimiter
                + latLon
        );
        Path file = Paths.get(filepath);
        Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
        logger.debug("Leaving varMetaToCsv");
    }


    public int varMetaSize() {
        return varMeta.getElements().size();
    }

    public VarMeta getVarMeta() {
        return varMeta;
    }
}
