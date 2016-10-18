package neo4j;

import java.util.AbstractMap;

/**
 * Created by carl on 07.01.16.
 */
public class Neo4JConnector extends Neo4JQueryManager {

    public void queryMyStatements(String... statements) throws Exception {
        issueNeo4JRequestStrings(statements);
    }

    static public String mergeRelation(AbstractMap.SimpleEntry<String, String> id1,
                                       AbstractMap.SimpleEntry<String, String> id2,
                                       String relation) {
        return "MERGE (n:" + id1.getKey() + "{id:'" + id1.getValue() + "'}) "
                    + "MERGE (m:" + id2.getKey() + "{id:'" + id2.getValue() + "'})"
                    + "CREATE UNIQUE (m) -[t:" + relation + "]-> (n) ";

    }
}
