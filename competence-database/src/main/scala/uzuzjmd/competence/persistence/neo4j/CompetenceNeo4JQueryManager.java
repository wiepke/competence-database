package uzuzjmd.competence.persistence.neo4j;

import neo4j.Neo4JQueryManagerImpl;
import uzuzjmd.competence.persistence.dao.Dao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dehne on 24.02.2016.
 */
public class CompetenceNeo4JQueryManager extends Neo4JQueryManagerImpl {


    protected <T extends Dao> HashSet<T> getDaoList(Class<T> clazz, String query) throws Exception {
        ArrayList result = issueNeo4JRequestArrayOfHashMap(query);
        if (result == null || result.isEmpty()) {
            return new HashSet<>();
        }
        HashSet<T> resultDaos = new HashSet<>();
        getHashMap(clazz, result, resultDaos);
        return resultDaos;
    }

    public <T extends Dao> T getDao(String id, Class<T> clazz) throws Exception {
        //super.statsHolder.requestHashMap.put(7, statsHolder.requestHashMap.get(7)+1);
        logger.debug(statsHolder.toString());
        String query = "MATCH (a{id:'"+id+"'}) return a";
        ArrayList result = issueNeo4JRequestHashMap(query);
        HashMap<String, String> hashMap = ((HashMap<String,String>)result.get(0));
        T tClass = clazz.getConstructor(String.class).newInstance(hashMap.get("id"));
        tClass.setFullDao(hashMap);
        return tClass;
    }

    private <T extends Dao> void getHashMap(Class<T> clazz, ArrayList<ArrayList<HashMap<String, String>>> result, HashSet<T> resultDaos) throws InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException {
        for (ArrayList<HashMap<String, String>> hashMaps : result) {
            for (HashMap<String, String> hashMap : hashMaps) {
                if (hashMap != null) {
                    T tClass = clazz.getConstructor(String.class).newInstance(hashMap.get("id"));
                    resultDaos.add((T) tClass.getFullDao(hashMap));
                }
            }
        }
    }

    public <T extends Dao> Set<T> listSuperClasses(Class<T> competenceClass, String id) throws Exception {

        String className = competenceClass.getSimpleName();
        String query = "MATCH z = (n:"+className+"{id:'"+id+"'})-[r:subClassOf*]->(p:"+className+") return filter(x IN nodes(z) WHERE NOT(x.id = n.id)) ";
        return getDaoList(competenceClass, query);
    }

    public <T extends Dao> Set<T> listSubClasses(Class<T> competenceClass, String id) throws Exception {
        String className = competenceClass.getSimpleName();
        String query = "MATCH z = (n:"+className+")-[r:subClassOf*]->(p:"+className+"{id:'"+id+"'}) return filter(x IN nodes(z) WHERE NOT(x.id = p.id)) ";
        return getDaoList(competenceClass, query);
    }
}
