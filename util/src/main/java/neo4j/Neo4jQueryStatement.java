package neo4j;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import uzuzjmd.competence.util.Helper;

import java.text.MessageFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by carl on 17.12.15.
 */
public class Neo4jQueryStatement {
    private Neo4jQuery.queryType queryType;
    private String var;
    private String group = "";
    private List<Neo4jQueryStatement> multiStates;
    private HashMap<String, String> arguments = new HashMap<String, String>();
    private AbstractMap.SimpleEntry<String, String> relEntry;
    static private final Logger logger = LogManager.getLogger(Neo4jQueryStatement.class.getName());

    public Neo4jQueryStatement() {
        logger.debug("New Neo4jQueryStatement");
    }

    public Neo4jQueryStatement(Neo4jQuery.queryType queryType, String... arguments) {
        logger.debug("Entering Neo4jQueryStatement Constructor with queryType:" + queryType.toString()
            + " arguments " + Helper.implode(",", arguments));
        this.queryType = queryType;
        if (! Neo4jQuery.validateTypeArguments(queryType, arguments.length)) {
            logger.error("Not enough arguments for this queryType");
            //TODO Throw an Exception
        }
        logger.debug("Leaving Neo4jQueryStatement Constructor");
    }

    @Override
    public String toString() {
        logger.debug("Entering toString");
        String query = Neo4jQuery.getQuery(queryType);
        String helper;
        switch (queryType) {
            case MERGE:
            case MATCH:
                helper = Helper.implode(":", ",", arguments);
                query = MessageFormat.format(query, var, group, "{" + helper + "}");
                break;
            case MATCHNOGROUP:
                helper = Helper.implode(":", ",", arguments);
                query = MessageFormat.format(query, var, "{" + helper + "}");
                break;
            case MATCHMULTI:
                query += Helper.implode(",", multiStates, "MATCH ");
                break;
            case SET:
                if (arguments.size() != 1){
                    logger.error("Too many arguments");
                    //TODO Throw Exception
                }
                String queryKey = "";
                String queryValue = "";
                for (String key :
                        arguments.keySet()) {
                    queryKey = key;
                    queryValue = "'" + arguments.get(key) + "'";
                }
                query = MessageFormat.format(query, var, queryKey, queryValue);
                break;
            case CREATEREL:
                query = MessageFormat.format(query, relEntry.getKey(), var, group, relEntry.getValue());
                break;
            case DELETE:

                break;
            case RETURN:
                query = MessageFormat.format(query, var);
                break;
            default:
                //TODO Auto-generated
                break;
        }
        logger.debug("Leaving toString with String " + query);
        return query;
    }

    public Neo4jQuery.queryType getQueryType() {
        return queryType;
    }

    public void setQueryType(Neo4jQuery.queryType queryType) {
        this.queryType = queryType;
    }

    public void addArgument(String key, String value) {
        arguments.put(key, value);
    }


    public HashMap<String, String> getArguments() {
        return arguments;
    }

    public void setArguments(HashMap<String, String> arguments) {
        this.arguments = arguments;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group =  ":" + group;
    }

    public void addMultiState(Neo4jQueryStatement state) {
        logger.debug("Entering addMultiState");
        if (multiStates == null) {
            multiStates = new ArrayList<>();
        }
        multiStates.add(state);
        logger.debug("Leaving addMultiState");
    }

    public List<Neo4jQueryStatement> getMultiStates() {
        return multiStates;
    }

    public void setMultiStates(List<Neo4jQueryStatement> multiStates) {
        this.multiStates = multiStates;
    }


    public AbstractMap.SimpleEntry<String, String> getRelEntry() {
        return relEntry;
    }

    public void setRelEntry(String str1, String str2) {
        this.relEntry = new AbstractMap.SimpleEntry<>(str1, str2);
    }
}
