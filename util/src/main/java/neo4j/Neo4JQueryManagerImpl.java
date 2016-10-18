package neo4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dehne on 04.12.2015.
 */
public class Neo4JQueryManagerImpl extends Neo4JQueryManager {

    public Neo4JQueryManagerImpl() {
    }

    public ArrayList<String> getLabelsForNode(String id) throws Exception {
        String query = "MATCH (e{id:'" + id + "'}) return labels(e)";
        ArrayList<ArrayList<String>> resultString = issueNeo4JRequestArrayListArrayList(query);
        if (resultString == null) {
            return new ArrayList<String>();
        }
        try {
            return resultString.iterator().next();
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * This is used for standard daos who have normal label
     *
     * @param id
     * @param labelName
     * @throws Exception
     */
    public void setLabelForNode(String id, String labelName) throws Exception {
        String query = "MATCH (e{id:'" + id + "'}) set e:" + labelName + " return e";
        ArrayList<String> resultString = issueSingleStatementRequest(new RequestableImpl<ArrayList<String>>(), query);
    }

    public ArrayList<HashMap<String, String>> createOrUpdateUniqueNode(HashMap<String, String> props) throws Exception {
        logger.debug("Entering createOrUpdateUniqueNode with props:" + implode(props));
        List<Neo4jQueryStatement> states = new ArrayList<Neo4jQueryStatement>();
        states.add(new Neo4jQueryStatement());
        states.get(states.size() - 1).setQueryType(Neo4jQuery.queryType.MERGE);
        states.get(states.size() - 1).setVar("n");
        if (props.containsKey("clazz") && props.get("clazz") != null) {
            states.get(states.size() - 1).setGroup(props.get("clazz"));
        }
        if (props.containsKey("id")) {
            states.get(states.size() - 1).addArgument("id", props.get("id"));
        }
        for (Map.Entry<String, String> kvp :
                props.entrySet()) {
            if (kvp.getKey().contains("clazz") || kvp.getKey().equals("id")) {
                continue;
            } else {
                states.add(new Neo4jQueryStatement());
                states.get(states.size() - 1).setQueryType(Neo4jQuery.queryType.SET);
                states.get(states.size() - 1).setVar("n");
                states.get(states.size() - 1).addArgument(kvp.getKey(), kvp.getValue());
            }
        }
        states.add(new Neo4jQueryStatement());
        states.get(states.size() - 1).setQueryType(Neo4jQuery.queryType.RETURN);
        states.get(states.size() - 1).setVar("n");

        logger.debug("Leaving createOrUpdateUniqueNode");
        return issueSingleStatementRequest(new RequestableImpl<ArrayList<HashMap<String, String>>>()
                , Neo4jQuery.statesToQuery(states));
    }


    private static String implode(Map<String, String> map) {

        boolean first = true;
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> e : map.entrySet()) {
            if (!first) sb.append(", ");
            sb.append(" " + e.getKey() + " : '" + e.getValue() + "' ");
            first = false;
        }

        return sb.toString();
    }





}
