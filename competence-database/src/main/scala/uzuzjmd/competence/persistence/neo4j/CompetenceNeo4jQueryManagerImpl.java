package uzuzjmd.competence.persistence.neo4j;

import neo4j.GroupedRequestableImpl;
import uzuzjmd.competence.exceptions.DataFieldNotInitializedException;
import uzuzjmd.competence.persistence.dao.*;
import uzuzjmd.competence.persistence.dao.User;
import uzuzjmd.competence.persistence.ontology.Edge;
import uzuzjmd.competence.persistence.ontology.Label;
import uzuzjmd.competence.shared.assessment.ReflectiveQuestionAnswerData;
import uzuzjmd.competence.shared.assessment.ReflectiveQuestionAnswerHolder;
import uzuzjmd.competence.shared.assessment.TypeOfSelfAssessment;
import uzuzjmd.competence.shared.competence.CompetenceFilterData;
import uzuzjmd.competence.shared.course.CourseData;
import uzuzjmd.competence.shared.learningtemplate.LearningTemplateData;
import uzuzjmd.competence.shared.user.UserOverview;

import java.util.*;

/**
 * Created by dehne on 24.02.2016.
 */
public class CompetenceNeo4jQueryManagerImpl extends CompetenceNeo4JQueryManager {

    public void createRelationShip(
            String domainId, Edge edge, String rangeId, Class<? extends Dao> domainClass,
            Class<? extends Dao> rangeClass) throws Exception {
        String query = "MATCH (n:" + domainClass.getSimpleName() + " {id:'" + domainId + "'}), (n2:" + rangeClass
                .getSimpleName() + "{id:'" + rangeId + "'}) CREATE UNIQUE " +
                "(n)-[r:" + edge.toString() + "]->(n2) return n,r,n2";
        issueNeo4JRequestStrings(query);

    }

    public void createRelationShipWithWeight(
            String domainId, Edge edge, String rangeId, Double weight, Class<? extends Dao> domainClass,
            Class<? extends Dao> rangeClass) throws Exception {
        logger.info("calling create relationship with" + domainId + " " + edge + " " + rangeId + weight);
        String deleteQuery =
                "MATCH (n:" + domainClass.getSimpleName() + "{id:'" + domainId + "'})-[old:" + edge.toString() +
                        "{weight:'" + weight + "'}]->(n2:" + rangeClass
                        .getSimpleName() + "{id:'" + rangeId + "'}) DELETE old";
        issueNeo4JRequestStrings(deleteQuery);
        String query = "MATCH (n:" + domainClass.getSimpleName() + " {id:'" + domainId + "'}), (n2:" + rangeClass
                .getSimpleName() + "{id:'" + rangeId + "'}) CREATE UNIQUE (n)-[r:" + edge
                .toString() + "{weight:'" + weight + "'}]->(n2) return n,r,n2";
        issueNeo4JRequestStrings(query);
    }

    public ArrayList<String> getClosestEdges(String domainId, Edge edge) throws Exception {
        String query = "MATCH (n {id:'" + domainId + "'})-[r:" + edge
                .toString() + "]->(n2)  return n2.id ORDER BY (r.weight) LIMIT 10";
        return issueNeo4JRequestStrings(query);
    }


    public void deleteNode(String id) throws Exception {
        String query = "MATCH (n {id:'" + id + "'}) DETACH DELETE n";
        issueNeo4JRequestStrings(query);
    }

    /**
     * delete Relationship between domainID and RangeID
     *
     * @param domainId
     * @param rangeId
     * @param edge
     */
    public void deleteRelationShip(String domainId, String rangeId, Edge edge) throws Exception {
        String query =
                "MATCH (a{id:'" + domainId + "'})-[r:" + edge.toString() + "]->(b{id:'" + rangeId + "'}) DELETE r";
        issueNeo4JRequestStrings(query);
    }


    /**
     * @param domainId
     * @param rangeId
     * @param edge
     * @return
     */
    public Boolean existsRelationShip(String domainId, String rangeId, Edge edge) throws Exception {
        String query =
                "MATCH (a{id:'" + domainId + "'})-[r:" + edge.toString() + "]->(b{id:'" + rangeId + "'}) return r";
        return existMatches(query);
    }

    private Boolean existMatches(String query) throws Exception {
        ArrayList<String> result = issueNeo4JRequestStrings(query);
        if (result == null) {
            return false;
        }
        return !result.isEmpty();
    }


    /**
     * checks if relationship exists but a singleton classNode is given instead of the individual
     *
     * @param domainClassNodeId
     * @param rangeId
     * @param edge
     */
    public Boolean existsRelationShipWithSuperClassGiven(String domainClassNodeId, String rangeId, Edge edge)
            throws Exception {
        String query = "MATCH (a)-[r:individualOf]->(b{id:'" + domainClassNodeId + "'}), (a)-[r2:" + edge
                .toString() + "]->(c{id:'" + rangeId + "'}) return a";
        return existMatches(query);
    }


    public List<String> getAssociatedNodeIdsAsRange(Edge edge, String rangeIndividualId) throws Exception {
        String query2 = "MATCH (b)-[r:" + edge.toString() + "]->(a {id:'" + rangeIndividualId + "'}) RETURN b.id";
        return issueNeo4JRequestStrings(query2);
    }


    public List<String> getAssociatedNodeIdsAsDomain(String domainIndividual, Edge edge) throws Exception {
        String query2 = "MATCH (a {id:'" + domainIndividual + "'})-[r:" + edge.toString() + "]->(b) RETURN b.id";
        return issueNeo4JRequestStrings(query2);
    }


    /**
     * @param clazz
     * @return
     * @throws Exception
     */
    public List<String> getAllInstanceDefinitions(Label clazz) throws Exception {
        String query = "MATCH (a:" + clazz.name() + ") return a.id";
        ArrayList<String> result = issueNeo4JRequestStrings(query);
        return result;
    }

    /**
     * @param clazz
     * @return
     * @throws Exception
     */
    public <T extends Dao> Set<T> getAllDaos(Label clazzLabel, Class<T> clazz) throws Exception {
        String query = "MATCH (a:" + clazzLabel.name() + ") return a";
        ArrayList<HashMap<String, String>> result = issueNeo4JRequestArrayOfHashMap(query);
        Set<T> result2 = new HashSet<>();
        if (result == null) {
            return new HashSet<T>();
        }
        for (HashMap<String, String> hashMap : result) {
            //HashMap<String, String> props = new HashMap<String, String>();
            T tClass = clazz.getConstructor(String.class).newInstance(hashMap.get("id")).getFullDao(hashMap);
            result2.add(tClass);
        }
        return result2;
    }

    /**
     * @param start
     * @param end
     * @return
     * @throws Exception
     */
    public List<String> getShortestSubClassPath(String start, String end) throws Exception {
        String query =
                "MATCH p=(a{id:'" + start + "'})-[r:subClassOf*]->(b{id:'" + end + "'}) return EXTRACT (n IN nodes(p)|n.definition) AS ids";
        return issueNeo4JRequestStrings(query);
    }

    /**
     * @param start
     * @param end
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T extends Dao> List<T> getShortestSubClassPath(String start, String end, Class<T> clazz) throws Exception {
        String query =
                "MATCH p=(a{id:'" + start + "'})-[r:subClassOf*]->(b{id:'" + end + "'}) return EXTRACT n IN nodes(p)";
        List<T> resultDaos = new ArrayList<T>();
        ArrayList<HashMap<String, String>> result = issueNeo4JRequestArrayOfHashMap(query);
        for (HashMap<String, String> stringStringHashMap : result) {
            resultDaos.add((T) clazz.newInstance().getFullDao(stringStringHashMap));
        }
        return resultDaos;
    }


    /**
     * removes a propety in a node
     *
     * @param id
     * @param prop
     * @throws Exception
     */
    public void removePropertyInNode(String id, String prop) throws Exception {
        String query = "MATCH (n { id: '" + id + "' }) REMOVE n." + prop + "  RETURN n";
        issueNeo4JRequestStrings(query);
    }

    /**
     * sets a propety in a node
     *
     * @param id
     * @param prop
     * @param value
     * @throws Exception
     */
    public void setPropertyInNode(String id, String prop, Object value) throws Exception {
        String query = "MATCH (n { id: '" + id + "' }) SET n." + prop + " = '" + value + "'  RETURN n";
        issueNeo4JRequestStrings(query);
    }

    public String getPropertyInNode(String id, String prop) throws Exception {
        String query = "MATCH (n { id: '" + id + "' })  RETURN n." + prop;
        if (issueNeo4JRequestStrings(query).isEmpty()) {
            throw new DataFieldNotInitializedException();
        }
        return issueNeo4JRequestStrings(query).get(0);
    }

    public Boolean exists(String id) throws Exception {
        String query = "MATCH (n{id:'" + id + "'}) return n";
        return !(issueNeo4JRequestStrings(query) == null || issueNeo4JRequestStrings(query).isEmpty());
    }

    public SelfAssessment getSelfAssessment(Competence competence, User user, TypeOfSelfAssessment typeOfSelfAssessment)
            throws Exception {
        String query = "MATCH (c:User{id:'" + user.getId() + "'}) MATCH (b:SelfAssessment " +
                "{typeOfSelfAssessment:'" + typeOfSelfAssessment.toString() + "'})" +
                "-[r1:AssessmentOfCompetence]->(a:Competence{id:'" + competence
                .getId() + "'}) MATCH (b)-[r2:AssessmentOfUser]->(c:User) return b";
        ArrayList<HashMap<String, String>> result = issueNeo4JRequestHashMap(query);
        if (result == null) {
            return new SelfAssessment(competence, user, 0, false, typeOfSelfAssessment);
        }
        HashMap<String, String> result2 = result.iterator().next();
        if (result2 == null) {
            return null;
        } else {
            return new SelfAssessment(result2.get("id")).getFullDao(result2);
        }
    }


    /**
     * returns a list in the form
     * 1 -> 2
     * 1 -> 3
     * 3 -> 4
     * 1 -> 3
     * []
     * []
     *
     * @param label
     * @return
     */
    public List<ArrayList<String>> getSubClassTriples(String label, CompetenceFilterData filterData) throws Exception {
        String courseId = filterData.getCourse();
        List<String> operators = filterData.getSelectedOperatorsArray();
        List<String> catchwords = filterData.getSelectedCatchwordArray();
        String futherMatches = "";
        String hiddenQuestion = "";
        for (String catchword : catchwords) {
            futherMatches += "MATCH (c:Catchword{id:'" + catchword + "'})-[r33:CatchwordOf]->(p)";
        }
        for (String operator : operators) {
            futherMatches += "MATCH (c:Operator{id:'" + operator + "'})-[r44:OperatorOf]->(p)";
        }
        if (filterData.getLearningTemplate() != null) {
            futherMatches += "MATCH (LPT:LearningProjectTemplate{id:'" + filterData
                    .getLearningTemplate() + "'})-[rLPT:LearningProjectTemplateOf]->(p)";
        }

        if (filterData.getUserId() != null && !filterData.getUserId().equals("")) {
            futherMatches += "MATCH (p)-[r32:HiddenFor]->(u:User{id:'" + filterData.getUserId() + "'})";
            hiddenQuestion += " WHERE r32 is null ";
        }

        if (filterData.getRootCompetence() != null) {
            futherMatches += "MATCH (p:" + label + ")-[:subClassOf*1..5]->(z:" + label + "{id:'" + filterData
                    .getRootCompetence() + "'})";
        }
        String query =
                "MATCH tree = (p:" + label + ")-[:subClassOf*1..5]->(c:" + label + ")" + futherMatches + "MATCH (x:CourseContext{id:'" + courseId + "'})-[r33:CourseContextOfCompetence]->(p)" + hiddenQuestion + "return extract(n IN filter(x in nodes(tree) WHERE length(nodes(tree)) = 2)|n.id) ORDER BY length(tree) ";
        return issueNeo4JRequestArrayListArrayList(query);
    }

    public HashSet<Competence> getSuggestedCompetenceRequirements(
            String competenceId, Class<Competence> competenceClass, LearningProjectTemplate learningProject)
            throws Exception {
        String query = "MATCH (b:LearningProjectTemplate{id:'" + learningProject
                .getId() + "'})-[r1:LearningProjectTemplateOf]->(c) MATCH (c:Competence)-[r2:SuggestedCompetencePrerequisiteOf]->(a:Competence{id:'" + competenceId + "'}) return c.id";
        ArrayList<String> result = issueNeo4JRequestStrings(query);
        if (result == null || result.isEmpty()) {
            return new HashSet<>();
        }
        HashSet<Competence> result2 = new HashSet<>();
        for (String s : result) {
            result2.add(new Competence(s, learningProject));
        }
        return result2;
    }

    /**
     * a->b and all catchwords c where c->a and c->b
     *
     * @param learningTemplate
     * @return
     * @throws Exception
     */
    public List<ArrayList<String>> getPrerequisiteTriples(LearningProjectTemplate learningTemplate) throws Exception {
        String query = "MATCH (l:LearningProjectTemplate{id:'" + learningTemplate
                .getDefinition() + "'})-[r4:LearningProjectTemplateOf]->(a) MATCH (l)-[r5:LearningProjectTemplateOf]->(b) MATCH (a:Competence)-[r1:SuggestedCompetencePrerequisiteOf]->(b:Competence) MATCH (c)-[x22:CatchwordOf]->(a) MATCH (c)-[x33:CatchwordOf]->(b) return a.id, b.id, c.id";
        return issueNeo4JRequestArrayListArrayList(query);
    }


    /**
     * TODO: check if order is preserved
     *
     * @param fromNode
     * @param toNode
     * @param suggestedCompetencePrerequisiteOf
     * @param returnType
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T extends Dao> HashSet<T> getShortestPath(
            String fromNode, String toNode, Edge suggestedCompetencePrerequisiteOf, Class<T> returnType)
            throws Exception {
        String query = "MATCH p = shortestPath((a:" + returnType
                .getSimpleName() + "{id:'" + fromNode + "'})-[:" + suggestedCompetencePrerequisiteOf + "*]->(b:" + returnType
                .getSimpleName() + "{id:'" + toNode + "'})) return nodes(p)";
        return getDaoList(returnType, query);
    }


    public ReflectiveQuestionAnswerHolder getReflectiveQuestionAnswers(
            String userId, String questionId, String competenceId) throws Exception {
        StringBuilder builder = new StringBuilder();
        if (questionId != null) {
            builder.append(
                    "MATCH (q:ReflectiveQuestion{id:'" + questionId + "'})-[r1:ReflectiveQuestionForCompetence]->(n:Competence) ");
        }
        if (competenceId != null) {
            builder.append(" MATCH (q:ReflectiveQuestion)-[r1:ReflectiveQuestionForCompetence]->(n:Competence{id:'" +
                    competenceId + "'}) ");
        }
        if (questionId == null && competenceId == null) {
            builder.append("MATCH (q:ReflectiveQuestion)-[r1:ReflectiveQuestionForCompetence]->(n:Competence)");
        }
        builder.append(" MATCH (a:ReflectiveQuestionAnswer)-[r2:AnswerForReflectiveQuestion]->(q)");
        builder.append(" MATCH (u:User{id:'" + userId + "'})-[r3:UserOfReflectiveQuestionAnswer]->(a)");
        builder.append(" RETURN a.id,a.text,q.id,a.datecreated,n.id");

        String exampleQuery = builder.toString();
        ArrayList<ArrayList<String>> arrayLists = issueNeo4JRequestArrayListArrayList(builder.toString());


        ReflectiveQuestionAnswerHolder reflectiveQuestionAnswerHolder = new ReflectiveQuestionAnswerHolder();
        if (!arrayLists.isEmpty() && !(arrayLists.get(0) instanceof ArrayList)) {
            ArrayList<String> actualList = (ArrayList) arrayLists;
            reflectiveQuestionAnswerHolder.getData()
                    .add(new ReflectiveQuestionAnswerData(actualList.get(1), userId, actualList.get(2),
                            Long.parseLong(actualList.get(3)),actualList.get(4)));
        } else {
            for (ArrayList o : arrayLists) {
                ReflectiveQuestionAnswerData reflectiveQuestionData =
                        new ReflectiveQuestionAnswerData(o.get(1).toString(), userId, o.get(0).toString(),
                                Long.parseLong(o.get(3).toString()),o.get(4).toString());
                reflectiveQuestionAnswerHolder.getData().add(reflectiveQuestionData);
            }
        }

        return reflectiveQuestionAnswerHolder;
    }


    public UserOverview getUserOverview(String userId) throws Exception {
        List<CourseData> courses = new ArrayList<>();
        List<LearningTemplateData> learningTemplates = new ArrayList<>();
        UserOverview userOverview = new UserOverview();


        String userMatch = "(u:User{id:'" + userId + "'})";

        StringBuilder builder = new StringBuilder();
        builder.append("MATCH (cc:CourseContext)-[r1:CourseContextOfUser]->");
        builder.append(userMatch);
        builder.append("OPTIONAL MATCH (cc)-[r2:CourseContextOfCompetence]->(c:Competence)");
        builder.append("where cc.id <> 'university' ");
        builder.append("return cc.id,cc.printableName,collect(c.id)");

        ArrayList<ArrayList<Object>> result =
                issueSingleStatementRequest(new GroupedRequestableImpl<ArrayList<ArrayList<Object>>>(),
                        builder.toString());
        if (result != null) {
            for (ArrayList<Object> arrayLists : result) {
                String courseId = (String) arrayLists.get(0);
                String courseName = (String) arrayLists.get(1);
                ArrayList<String> competences = (ArrayList<String>) arrayLists.get(2);
                competences.remove("Kompetenz");
                courses.add(new CourseData(courseId, courseName, competences));
            }
        }
        StringBuilder builder2 = new StringBuilder("");
        builder2.append("MATCH ");
        builder2.append(userMatch + "-[r3:UserOfLearningProjectTemplate]->(l)");
        builder2.append("OPTIONAL MATCH (l)-[r4:LearningProjectTemplateOf]->(c2:Competence)");
        builder2.append("return l.id, collect(c2.id)");

        String test = builder2.toString();
        ArrayList<ArrayList<Object>> result2 =
                issueSingleStatementRequest(new GroupedRequestableImpl<ArrayList<ArrayList<Object>>>(),
                        builder2.toString());

        if (result2 != null) {
            for (Object arrayLists : result2) {
                if (arrayLists instanceof String) {
                    LearningTemplateData l = new LearningTemplateData(userId, null, arrayLists.toString());
                }
                if (arrayLists instanceof ArrayList) {
                    ArrayList<Object> actualList = (ArrayList<Object>) arrayLists;
                    LearningTemplateData l = new LearningTemplateData(userId, null, actualList.get(0).toString());
                    l.setCompetences((List<String>) actualList.get(1));
                    l.getCompetences().remove("Kompetenz");
                    learningTemplates.add(l);
                }
            }
        }
        userOverview.setCourses(courses);
        userOverview.setLearningTemplates(learningTemplates);

        return userOverview;
    }
}
