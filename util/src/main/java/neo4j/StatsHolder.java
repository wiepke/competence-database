package neo4j;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by dehne on 19.01.2016.
 */
public class StatsHolder {
    public HashMap<Integer, Integer> requestHashMap = new HashMap<Integer,Integer>();

    public StatsHolder(HashMap<Integer, Integer> requestHashMap) {
        this.requestHashMap = requestHashMap;
    }

    public StatsHolder() {
        requestHashMap.put(0,0);
        requestHashMap.put(1,0);
        requestHashMap.put(2,0);
        requestHashMap.put(3,0);
        requestHashMap.put(4,0);
        requestHashMap.put(5,0);
        requestHashMap.put(6,0);
        requestHashMap.put(7,0);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<Integer, Integer>> iter = requestHashMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, Integer> entry = iter.next();
            sb.append(entry.getKey());
            sb.append('=').append('"');
            sb.append(entry.getValue());
            sb.append('"');
            if (iter.hasNext()) {
                sb.append(',').append(' ');
            }
        }
        return sb.toString();
    }
}
