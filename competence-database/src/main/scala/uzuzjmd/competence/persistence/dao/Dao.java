package uzuzjmd.competence.persistence.dao;

import uzuzjmd.competence.persistence.ontology.Edge;
import uzuzjmd.competence.persistence.ontology.Label;

import java.util.HashMap;
import java.util.List;

/**
 * Created by dehne on 11.01.2016.
 */
public interface Dao {
    /**
     * should be updated if already exists
     * @throws Exception
     */
    Dao persist() throws Exception;
    void delete() throws Exception;
    void createEdgeWith(Edge edge, Dao range) throws Exception;
    void createEdgeWith(Dao domain, Edge edge) throws Exception;
    void createEdgeWithAll(Edge edge, List<Dao> range) throws Exception;
    void createEdgeWithAll(List<Dao> domain, Edge edge) throws Exception;
    Boolean hasEdge(Edge edge, Dao range) throws Exception;
    Boolean hasEdge(Dao domain, Edge edge) throws Exception;
    void deleteEdgeWith(Dao domain, Edge edge) throws Exception;
    String getId();
    Boolean exists() throws Exception;
    Label getLabel();
    void setFullDao(HashMap<String, String> props);
    <T extends Dao> T getFullDao(HashMap<String, String> props);
    <T extends Dao> T getFullDao() throws Exception;
    void updateId(String newId) throws Exception;

    /**
     * THIS is the domain
     * @param edge
     * @return
     * @throws Exception
     */
    List<String> getAssociatedDaoIdsAsDomain(Edge edge) throws Exception;

    /**
     * THIS is the range
     * @param edge
     * @return
     * @throws Exception
     */
    List<String> getAssociatedDaoIdsAsRange(Edge edge) throws Exception;
    <P extends Dao> List<P> getAssociatedDaosAsDomain(Edge edge, Class<P> clazz) throws Exception;
    <T extends Dao> List<T> getAssociatedDaosAsRange(Edge edge, Class<T> clazz) throws Exception;
    <P extends Dao> P getAssociatedDaoAsDomain(Edge edge, Class<P> clazz) throws Exception;
    <T extends Dao> T getAssociatedDaoAsRange(Edge edge, Class<T> clazz) throws Exception;
}
