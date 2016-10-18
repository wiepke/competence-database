package neo4j;

/**
 * Created by dehne on 09.12.2015.
 */
public interface Fetchable<T> {
    /**
     * returns the individual from the database or null
     * if it does not exist
     * @return
     */
    public T fetchIfExists() throws Exception;
    public T create() throws Exception;
    public T update() throws Exception;
    public void delete() throws Exception;
}
