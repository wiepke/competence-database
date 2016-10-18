package uzuzjmd.competence.persistence.neo4j;

/**
 * Created by dehne on 24.02.2016.
 */
public class DBFactory {
    public static CompetenceNeo4jQueryManagerImpl getDB() {
        return new CompetenceNeo4jQueryManagerImpl();
    }
}
