package neo4j.reasoning;

import neo4j.RequestableImpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by dehne on 15.01.2016.
 */
public class RequestableImpl2<T> extends RequestableImpl<T> {
    @Override
    protected T extractValues(LinkedHashMap<String, ArrayList<LinkedHashMap<String, ArrayList<LinkedHashMap<String, T>>>>> result) {
        ArrayList<LinkedHashMap<String, T>> temp1 = result.get("results").get(0).get("data");

        ArrayList<T> myResult = new ArrayList<T>();
        for (LinkedHashMap<String, T> stringTLinkedHashMap : temp1) {
            LinkedHashMap<String, ArrayList<T>> temp2 = (LinkedHashMap<String, ArrayList<T>>)  stringTLinkedHashMap;
            myResult.add(temp2.get("row").get(0));
        }
        return (T) myResult;
    }


}
