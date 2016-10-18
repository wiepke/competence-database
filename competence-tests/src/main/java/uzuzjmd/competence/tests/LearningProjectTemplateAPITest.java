package uzuzjmd.competence.tests;

import datastructures.graph.Graph;
import datastructures.graph.GraphTriple;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import uzuzjmd.competence.evidence.service.rest.EvidenceServiceRestServerImpl;
import uzuzjmd.competence.persistence.dao.DBInitializer;
import uzuzjmd.competence.service.rest.*;
import uzuzjmd.competence.shared.learningtemplate.LearningTemplateResultSet;
import uzuzjmd.competence.shared.learningtemplate.LearningTemplateResultSetWrapper;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class LearningProjectTemplateAPITest extends JerseyTest {

    public static final String LABELNAME = "SuggestedCompetencePrerequisite";
    private static Graph graph;

    private static String learningTemplateName;
    private static LearningTemplateResultSet learningTemplateResultSet;
    private static LearningTemplateResultSetWrapper wrapper;
    private static LoggingFilter logginFilter;
    private static String templateName = "TheTemplateName"; // the name of the
    // tested for
    // single-case


    @Override
    protected javax.ws.rs.core.Application configure() {
        DBInitializer.init();
        return new ResourceConfig(LearningTemplateApiImpl.class, CompetenceApiImpl.class, UserApiImpl.class, CourseApiImpl.class, EvidenceApiImpl.class, EvidenceServiceRestServerImpl.class, ProgressApiImpl.class, CompetenceServiceRestJSON.class);
    }


    // learningTemplate

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        // TESTDATA

        graph = new Graph();
        graph.addTriple("using tags", "using JSP tags", LABELNAME, true);
        graph.addTriple("using JSP tags", "using primfaces tags", LABELNAME,
                true);
        graph.addTriple("using JSP tags", "using faces tags", LABELNAME, true);
        graph.addTriple("programming", "creating api", LABELNAME, true);
        graph.addTriple("creating api",
                "being the first person to generate a universal api",
                LABELNAME, true);

        GraphTriple first = new GraphTriple("using tags", "using JSP tags",
                LABELNAME, true);
        GraphTriple second = new GraphTriple("using JSP tags",
                "using primfaces tags", LABELNAME, true);
        GraphTriple third = new GraphTriple("programming", "creating api",
                LABELNAME, true);
        GraphTriple fourth = new GraphTriple("using JSP tags",
                "using faces tags", LABELNAME, true);
        GraphTriple fifth = new GraphTriple("creating api",
                "being the first person to generate a universal api",
                LABELNAME, true);

        HashMap<GraphTriple, String[]> map = new HashMap<GraphTriple, String[]>();
        map.put(first, new String[]{"programming", "jsp"});
        map.put(second, new String[]{"programming", "jsp"});
        map.put(fourth, new String[]{"programming", "jsp"});
        map.put(third, new String[]{"programming", "api"});
        map.put(fifth, new String[]{"programming", "api", "universality"});

        learningTemplateName = "TestLernprojekt";

        learningTemplateResultSet = new LearningTemplateResultSet(graph, map,
                learningTemplateName);
        wrapper = new LearningTemplateResultSetWrapper();
        wrapper.setLearningTemplateResultSet(learningTemplateResultSet);

        // set up logger
        Logger logger = Logger.getLogger(LearningProjectTemplateAPITest.class
                .getName());
        logger.setLevel(Level.ALL);

        logginFilter = new LoggingFilter(logger, true);


    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        graph = null;
        learningTemplateName = null;

        // TODO: Delete Templates
    }

    /**
     * creates a learningprojectTemplate
     */
    public void createLearningProjectTemplate() {

        Response response = target("/competences/learningtemplate/add").request()
                .post(Entity.entity(learningTemplateResultSet,
                        MediaType.APPLICATION_XML));
        assertTrue(response.getStatus() == 200);
    }

    /**
     * test if learning project template can be used
     */
    @Test
    public void testGetLearningProjectTemplate() {

        // add single learningTemplate
        addSingleRootLearningProjectTemplate();
        createLearningProjectTemplate();
        LearningTemplateResultSet result = target("/competences/learningtemplate/get/"
                + learningTemplateName).request(
                MediaType.APPLICATION_XML).get(
                LearningTemplateResultSet.class);
        assertNotNull(result);

    }

    /**
     * creates a learning project template with one element
     */
    public void addSingleRootLearningProjectTemplate() {

        Response result = target("/competences/addOne")
                .queryParam("competence",
                        "The ability to jump high distances")
                .queryParam("operator", "jump")
                .queryParam("catchwords", "ability")
                .queryParam("learningTemplateName", templateName).request(MediaType.APPLICATION_JSON)
                .post(null);
        assertTrue(result.getStatus() == 200);
    }

    /**
     * check if template with one element can be returned
     */
    @Test
    public void checkSingletonLearningTemplate() {
        // make client call
        addSingleRootLearningProjectTemplate();
        LearningTemplateResultSet result2 = target("/competences/learningtemplate/get/"
                + templateName).request(MediaType.APPLICATION_XML).get(
                LearningTemplateResultSet.class);
        assertNotNull(result2);
        assertEquals(
        result2.getRoot().getLabel().equals("The ability to jump high distances"), true);
    }
}
