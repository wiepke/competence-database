package neo4j;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import java.util.List;

/**
 * Created by carl on 17.12.15.
 */
public class Neo4jQuery {
    static private final Logger logger = LogManager.getLogger(Neo4jQuery.class.getName());

    static public String getQuery() {
        return "";
    }
    static public String getQuery(queryType qt) {
        if (qt == null) {
            return "";
        }
        return queries[qt.ordinal()];
    }

    public enum queryType {
        MERGE, SET, RETURN, MATCH, MATCHMULTI, MATCHNOGROUP, CREATEREL, DELETE
    }

    //Please if you update queries insert the size of needed arguments in sizeOfArguments
    static private final String[] queries = new String[]{
            "MERGE ({0}{1} {2})",
            "SET {0}.{1}={2}",
            "RETURN {0}",
            "MATCH ({0}{1} {2})",
            "MATCH ",
            "MATCH ({0} {1})",
            "CREATE ({0})-[{1}:{2}]->({3})",
            "DETACH DELETE {0}"
    };

    private static final int[] sizeOfArguments = new int[]{
            3, 3, 1, 3, 0, 2, 4, 1
    };

    static public boolean validateTypeArguments(queryType qt, int size) {
        return sizeOfArguments[qt.ordinal()] == size;
    }

    static public String statesToQuery(List<Neo4jQueryStatement> states) {
        logger.debug("getMyQuery size of states:" + String.valueOf(states.size()));
        String retString="";
        for (Neo4jQueryStatement state :
                states) {
            retString += state.toString() + " ";
        }
        return retString;
    }


}
