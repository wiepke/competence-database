package neo4j;

/**
 * Created by carl on 16.12.15.
 */
public interface Requestable<T> {
    T doRequest(String queryType) throws Exception;
}
