package neo4j;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by dehne on 06.10.2016.
 */
public class GroupedRequestableImpl<T> extends RequestableImpl<T> {
    @Override
    protected T extractValues(
            LinkedHashMap<String, ArrayList<LinkedHashMap<String, ArrayList<LinkedHashMap<String, T>>>>> result) {
        ArrayList<LinkedHashMap<String, T>> temp1 = result.get("results").get(0).get("data");
        ArrayList<T> myResult = new ArrayList<T>();
        Boolean isMultiple = false;
        isMultiple = checkMultiple(temp1, myResult, isMultiple);
        if (isMultiple) {
            for (int i = 0; i < temp1.size(); i++) {
                myResult.add(temp1.get(i).get("row"));
            }
        }
        return (T) myResult;
    }
}
