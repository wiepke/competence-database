package uzuzjmd.competence.util;

import neo4j.Neo4jQueryStatement;

import java.util.HashMap;
import java.util.List;

/**
 * Created by carl on 17.12.15.
 */
public class Helper {
    static public String implode(String delimiter, String... stringList) {
        String res = "";
        for (String str :
                stringList) {
            res += str + delimiter;
        }
        return res.substring(0, Math.max(res.length()-1, 0));
    }
    static public String implode(String delimiterHash,
                                 String delimiterArray,
                                 HashMap<String, String> hashMapList) {
        String res = "";
        for (String key :
                hashMapList.keySet()) {
            res += key + delimiterHash + "'" + hashMapList.get(key) + "'" + delimiterArray;
        }

        return res.substring(0, Math.max(res.length()-1, 0));
    }
    static public String implode(String delimiter, List<Neo4jQueryStatement> stringList, String without) {
        String res = "";
        for (Neo4jQueryStatement state :
                stringList) {
            res += state.toString().substring(without.length()) + delimiter;
        }
        return res.substring(0, Math.max(res.length()-1, 0));
    }
}
