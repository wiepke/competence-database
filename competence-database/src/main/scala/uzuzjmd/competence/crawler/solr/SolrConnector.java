package uzuzjmd.competence.crawler.solr;

import config.MagicStrings;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by carl on 07.01.16.
 */
public class SolrConnector {
    static private final Logger logger = LogManager.getLogger(SolrConnector.class.getName());
    private SolrClient client ;
    private final String serverUrl;
    private static int limit =1000000;
    public QueryResponse response;

    public SolrConnector(String serverUrl) {
        logger.debug("Entering SolrConnector Constructor with serverUrl:" + serverUrl);
        this.serverUrl = serverUrl;
        client = new HttpSolrClient(serverUrl);
        logger.debug("Leaving SolrConnector Constructor");
    }

    public SolrConnector(String serverUrl, int limit) {
        logger.debug("Entering SolrConnector Constructor with serverUrl:" + serverUrl);
        this.serverUrl = serverUrl;
        client = new HttpSolrClient(serverUrl);
        SolrConnector.limit = limit;
        logger.debug("Leaving SolrConnector Constructor");
    }
    static public int getLimit() {
        return limit;
    }

    public int getCountOfUni(String query) throws IOException, SolrServerException {

        SolrQuery solrQuery = new SolrQuery("content:*");
        solrQuery.set("indent", "true");
        solrQuery.setFields("id", "content", "title", "score", "url", "pageDepth");
        solrQuery.set("wt", "json");
        String domain = query.split("\\.")[1];
        //System.out.println(domain);
            solrQuery.set("fq", "id:*" + domain + "*");
            if (query.toLowerCase().matches("forsch[a-z]* lern[a-z]*") || query.toLowerCase().matches("entdeckend[a-z]* lern[a-z]*")) {
                solrQuery.set("defType", "edismax");
                solrQuery.set("qs", "10");
            }
            QueryResponse response = client.query(solrQuery);
            return (int) response.getResults().getNumFound();

    }
    public int getCountOfResults(String query) throws IOException, SolrServerException {

        SolrQuery solrQuery = new SolrQuery("content:" + query);
        solrQuery.set("indent", "true");
        solrQuery.setFields("id", "content", "title", "score", "url", "pageDepth");
        solrQuery.set("wt", "json");
        if (query.toLowerCase().matches("forsch[a-z]* lern[a-z]*") || query.toLowerCase().matches("entdeckend[a-z]* lern[a-z]*")) {
            solrQuery.set("defType", "edismax");
            solrQuery.set("qs", "10");
        }
        QueryResponse response = client.query(solrQuery);
        return (int) response.getResults().getNumFound();
    }
    public boolean connectToSolr(String query, int iterationCount) throws IOException, SolrServerException {
        //query = "content\"" + StringUtils.join(query.split(" "), "\" AND \"") + "\"";

        //TODO Hacky exception
        if (! query.matches("\\*")) {
            logger.info("Match");
            query = "\"" + query + "\"";
        }
        int numFound = getCountOfResults(query);
        int maxCount = Integer.parseInt(MagicStrings.maxSolrDocs) * (iterationCount + 1);

        logger.debug("Entering connectToSolr with query:" + query
                + " and iterationCount:" + iterationCount);
        logger.debug("Numbers of documents:" + numFound);
        //Build a query
        SolrQuery solrQuery = new SolrQuery("content:" + query);
        solrQuery.set("indent", "true");
        if ( numFound < maxCount) {
            solrQuery.set("rows", numFound);
        } else {
            solrQuery.set("rows", maxCount - 1);
        }
        solrQuery.set("start", iterationCount * Integer.parseInt(MagicStrings.maxSolrDocs));
        solrQuery.setFields("id", "title", "score", "url", "pageDepth");
        solrQuery.set("wt", "json");
        if (query.toLowerCase().matches("forsch[a-z]* lern[a-z]*") || query.toLowerCase().matches("entdeckend[a-z]* lern[a-z]*")) {
            solrQuery.set("defType", "edismax");
            solrQuery.set("qs", "10");
        }

        //send query
        response = client.query(solrQuery);
        logger.debug("Leaving connectToSolr");
        //TODO
        return (numFound > maxCount);
    }

    public String getServerUrl(){
        return serverUrl;
    }
    /*
    private LinkedHashMap<String, ArrayList<LinkedHashMap<String, ArrayList<LinkedHashMap<String, ArrayList<String>>>>>> issueAbstractRequest(String payload) {
        Client client2 = ClientBuilder.newClient();
        WebTarget target2 = client2.target(txUri);
        return target2.request(
                MediaType.APPLICATION_JSON).post(
                Entity.entity(payload,
                        MediaType.APPLICATION_JSON), LinkedHashMap.class);
    }
    */
}
