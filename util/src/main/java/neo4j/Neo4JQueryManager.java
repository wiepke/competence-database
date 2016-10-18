package neo4j;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import neo4j.reasoning.RequestableImpl2;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by dehne on 07.01.2016.
 */
public abstract class Neo4JQueryManager  {
    protected static Logger logger = LogManager.getLogger(Neo4JQueryManager.class.getName());

    protected static StatsHolder statsHolder = new StatsHolder();

    public static LoadingCache<String, ArrayList<String>> cacheImpl = CacheBuilder
            .newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(2l,TimeUnit.SECONDS)
            .build(new CacheLoader<String, ArrayList<String>>() {
                @Override
                public ArrayList<String> load(String key) throws Exception {
                    ArrayList<String> result = Neo4JQueryManager.issueSingleStatementRequest(new RequestableImpl<ArrayList<String>>(),key);
                    if (result == null) {
                        return new ArrayList<String>();
                    }else {
                        return result;
                    }
                }
            });

    protected static <T> T issueSingleStatementRequest(Requestable<T> req, String query) throws Exception {
        statsHolder.requestHashMap.put(0, statsHolder.requestHashMap.get(0)+1);
        logger.debug(statsHolder.toString());
        logger.info(query);
        String payload = "{\"statements\" : [ {\"statement\" : \"" + query + "\"} ]}";
        return req.doRequest(payload);
    }

    private <T> T issueMultipleStatementRequest(Requestable<T> req, String... queries) throws Exception {
        statsHolder.requestHashMap.put(1, statsHolder.requestHashMap.get(1)+1);
        logger.debug(statsHolder.toString());
        String statements = "";
        for (int i = 0; i < queries.length; i++) {
            statements += "{\"statement\": \"" + queries[i] + "\"},";
        }
        statements = statements.substring(0, statements.length()-1);
        String payload = "{\"statements\" : [" + statements + "]}";
        return req.doRequest(payload);
    }

    protected ArrayList<String> issueNeo4JRequestStrings(final String payload) throws Exception {
        statsHolder.requestHashMap.put(2, statsHolder.requestHashMap.get(2)+1);
        logger.debug(statsHolder.toString());
        return issueSingleStatementRequest(new RequestableImpl<ArrayList<String>>() , payload);
        //return cacheImpl.get(payload);
    }

    protected ArrayList<HashMap<String, String>> issueNeo4JRequestHashMap(final String payload) throws Exception {
        statsHolder.requestHashMap.put(3, statsHolder.requestHashMap.get(3)+1);
        logger.debug(statsHolder.toString());
        return issueSingleStatementRequest(new RequestableImpl<ArrayList<HashMap<String, String>>>() , payload);
    }

    protected ArrayList<ArrayList<String>> issueNeo4JRequestArrayListArrayList(final String payload) throws Exception {
        //statsHolder.requestHashMap.put(4, statsHolder.requestHashMap.get(4)+1);
        //logger.debug(statsHolder.toString());
        ArrayList<ArrayList<String>> result = issueSingleStatementRequest(new RequestableImpl<ArrayList<ArrayList<String>>>(), payload);
        if (result == null || result.isEmpty()) {
            return new ArrayList<>();
        } else return result;
    }

    protected ArrayList<String> issueNeo4JRequestStrings(final String... queries) throws Exception {
        statsHolder.requestHashMap.put(5, statsHolder.requestHashMap.get(5)+1);
        logger.debug(statsHolder.toString());
        return issueMultipleStatementRequest(new RequestableImpl<ArrayList<String>>() , queries);
    }

    protected ArrayList<HashMap<String, String>> issueNeo4JRequestArrayOfHashMap(final String... queries) throws Exception {
        statsHolder.requestHashMap.put(6, statsHolder.requestHashMap.get(6)+1);
        logger.debug(statsHolder.toString());
        return issueMultipleStatementRequest(new RequestableImpl2<ArrayList<HashMap<String, String>>>() , queries);
    }
}
