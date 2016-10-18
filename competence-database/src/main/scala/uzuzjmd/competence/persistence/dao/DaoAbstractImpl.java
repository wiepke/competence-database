package uzuzjmd.competence.persistence.dao;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import uzuzjmd.competence.persistence.neo4j.CompetenceNeo4jQueryManagerImpl;
import uzuzjmd.competence.persistence.ontology.Edge;
import uzuzjmd.competence.persistence.ontology.Label;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by dehne on 11.01.2016.
 */
public abstract class DaoAbstractImpl implements Dao {

    private final String id;
    protected final CompetenceNeo4jQueryManagerImpl queryManager = new CompetenceNeo4jQueryManagerImpl();
    static Logger logger = LogManager.getLogger(DaoAbstractImpl.class.getName());
    private final Class clazz;
    protected HashSet<Dao> superClasses = new HashSet<>();
    protected HashSet<Dao> subClasses = new HashSet<>();



    public DaoAbstractImpl(String id) {
        this.id = id;
        this.clazz = this.getClass();
    }

    /**
     * TODO refactor should be usable for other classes other then ocmpetences
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T extends Dao> Set<T> getAllInstances(Class<T> clazz) throws Exception {
        CompetenceNeo4jQueryManagerImpl queryManager = new CompetenceNeo4jQueryManagerImpl();
        return queryManager.getAllDaos(Label.valueOf(clazz.getSimpleName()),clazz);
    }


    @Override
    public void setFullDao(HashMap<String, String> props) {
              logger.debug("Entering hashMapToIndividual with properties" + props);
        String logMessage = "Created/Updated Individual {";
        for (String key :
                props.keySet()) {
            logMessage += key + ":" + props.get(key) + "; ";
            try {
                Field f = getClass().getDeclaredField(key);
                String name = f.getType().toString();
                if (name.contains("java.lang.String")) {
                    f.set(this, props.get(f.getName()));
                } else {
                    try {
                        f.set(this, convert(f.get(this).getClass(), props.get(key)));
                    } catch (IllegalAccessException e) {
                        logger.warn("Can't convert a field from database to Individual");
                        logger.warn("fieldClass: " + f.get(this).getClass().getName() + " Property:" + props.get(key));
                    }
                }
            } catch (NoSuchFieldException e) {
                logger.trace("field does not exist");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Exception e ){
                logger.error("other error:" + e.getMessage());
            }
        }
        logger.trace(logMessage + "}");
        logger.trace("Leaving hashMapToIndividual");
    }

    @Override
    public <T extends Dao> T getFullDao(HashMap<String, String> props) {
        setFullDao(props);
        return (T) this;
    }

    @Override
    public <T extends Dao> T getFullDao() throws Exception {
        return (T) queryManager.getDao(id, this.getClass());
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Boolean exists() throws Exception {
        return queryManager.exists(id);
    }

    @Override
    public Label getLabel() {
        return Label.valueOf(this.getClass().getSimpleName());
    }

    @Override
    public void createEdgeWith(Edge edge, Dao range) throws Exception {
        if(!this.getId().equals(range.getId())) {
            queryManager.createRelationShip(this.getId(), edge, range.getId(), this.getClass(), range.getClass());
        }
    }

    @Override
    public void createEdgeWithAll(List<Dao> domain, Edge edge) throws Exception {
        for (Dao dao : domain) {
            createEdgeWith(dao,edge);
        }
    }

    @Override
    public void createEdgeWith(Dao domain, Edge edge) throws Exception {
        if(!this.getId().equals(domain.getId())) {
            queryManager.createRelationShip(domain.getId(), edge, this.getId(), domain.getClass(), this.getClass());
        }
    }

    @Override
    public void createEdgeWithAll(Edge edge, List<Dao> range) throws Exception {
        for (Dao dao : range) {
            createEdgeWith(edge,dao);
        }
    }

    @Override
    public void deleteEdgeWith(Dao otherNode, Edge edge) throws Exception {
        queryManager.deleteRelationShip(otherNode.getId(), this.getId(), edge);
        queryManager.deleteRelationShip(this.getId(), otherNode.getId(), edge);
    }

    @Override
    public Boolean hasEdge(Dao domain, Edge edge) throws Exception {
        return queryManager.existsRelationShip(domain.getId(), this.getId(), edge);
    }

    @Override
    public Boolean hasEdge(Edge edge, Dao range) throws Exception {
        return queryManager.existsRelationShip(this.getId(), range.getId(), edge);
    }

    @Override
    public Dao persist() throws Exception {
        HashMap<String, String> propHashMap = this.toHashMap();
        propHashMap.put("id", getId());
        propHashMap.put("clazz", getLabel().toString());
        queryManager.createOrUpdateUniqueNode(propHashMap);
        return this;
    }

    @Override
    public void delete() throws Exception {
        queryManager.deleteNode(getId());
    }

    @Override
    public <T extends Dao> List<T> getAssociatedDaosAsDomain(Edge edge, Class<T> clazz) throws Exception {
        List<String> nodeIds = queryManager.getAssociatedNodeIdsAsDomain(getId(), edge);
        return instantiateDaos(clazz, nodeIds);
    }

    @Override
    public <T extends Dao> List<T> getAssociatedDaosAsRange(Edge edge, Class<T> clazz) throws Exception {
        List<String> nodeIds = queryManager.getAssociatedNodeIdsAsRange(edge, getId());
        return instantiateDaos(clazz, nodeIds);
    }

    private <T extends Dao> List<T> instantiateDaos(Class<T> clazz, List<String> nodeIds) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (nodeIds == null || nodeIds.isEmpty()) {
            return new LinkedList<>();
        }

        List<T> resultList = new ArrayList<T>();
        for (String nodeId : nodeIds) {
            T result = clazz.getDeclaredConstructor(String.class).newInstance(nodeId);
            resultList.add(result);
        }
        return resultList;
    }

    @Override
    public List<String> getAssociatedDaoIdsAsDomain(Edge edge) throws Exception {
        return  queryManager.getAssociatedNodeIdsAsDomain(getId(), edge);
    }

    @Override
    public List<String> getAssociatedDaoIdsAsRange(Edge edge) throws Exception {
        return queryManager.getAssociatedNodeIdsAsRange(edge, getId());
    }

    protected HashMap<String, String> toHashMap() {
        logger.trace("Entering toHashMap");
        HashMap<String, String> result = new HashMap<String, String>();
        String logMes = "{";
        for (Field prop :
                getClass().getDeclaredFields()) {
            logMes += prop.getName() + ":";
            try {
                if (!(prop.get(this) == null)) {
                    if (!((prop.get(this).getClass().getName().contains("Neo4J")) || (prop.get(this).getClass().getName().contains("Logger")))) {
                        result.put(prop.getName(), prop.get(this).toString());
                        logMes += prop.get(this).toString() + ";";
                    }
                }
            } catch (IllegalAccessException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
        }
        logger.trace("Leaving toHashMap with HashMap:" + logMes + "}");
        return result;
    }


    static <T> T convert(Class<T> klazz, String arg) {
        logger.debug("Entering static convert with klazz:" + klazz.getName() + " arg:" + arg);
        Exception cause = null;
        T ret = null;
        try {
            ret = klazz.cast(
                    klazz.getDeclaredMethod("valueOf", String.class)
                            .invoke(null, arg)
            );
        } catch (NoSuchMethodException e) {
            cause = e;
        } catch (IllegalAccessException e) {
            cause = e;
        } catch (InvocationTargetException e) {
            cause = e;
        }
        if (cause == null) {
            return ret;
        } else {
            logger.error(cause.getMessage());
            throw new IllegalArgumentException(cause);
        }
    }

    public <T extends Dao> Set<T> listSuperClasses(Class<T> competenceClass) throws Exception {
        if (this.superClasses.isEmpty()) {
            this.superClasses = (HashSet<Dao>) queryManager.listSuperClasses(competenceClass, this.getId());
        }
        return (Set<T>) this.superClasses;
    }

    public <T extends Dao> Set<T> listSubClasses(Class<T> competenceClass) throws Exception {
        if (this.subClasses.isEmpty()) {
            this.subClasses = (HashSet<Dao>) queryManager.listSubClasses(competenceClass, this.getId());
        }
        return (Set<T>) this.subClasses;
    }

    @Override
    public boolean equals(Object o) {
        return ((Dao) o).getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public <P extends Dao> P getAssociatedDaoAsDomain(Edge edge, Class<P> clazz) throws Exception {
        List<P> result = getAssociatedDaosAsDomain(edge, clazz);
        if (result.isEmpty()) {
            throw new Exception("Did not find any associated node for: " + edge.name() + " and id: "+this.getId());
        }
        return result.iterator().next();
    }

    @Override
    public <T extends Dao> T getAssociatedDaoAsRange(Edge edge, Class<T> clazz) throws Exception {
        List<T> result = getAssociatedDaosAsRange(edge, clazz);
        if (result.isEmpty()) {
            throw new Exception("Did not find any associated node for: " + edge.name() + " and id: "+this.getId());
        }
        return result.iterator().next();
    }

    @Override
    public void updateId(String newId) throws Exception {
        queryManager.setPropertyInNode(getId(), "id", newId);
    }
}
