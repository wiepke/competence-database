package uzuzjmd.competence.tests;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uzuzjmd.competence.evidence.service.rest.EvidenceServiceRestServerImpl;
import uzuzjmd.competence.persistence.dao.DBInitializer;
import uzuzjmd.competence.persistence.ontology.Contexts;
import uzuzjmd.competence.service.rest.*;
import uzuzjmd.competence.shared.competence.CompetenceData;
import uzuzjmd.competence.shared.learningtemplate.LearningTemplateData;
import datastructures.lists.StringList;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by dehne on 02.06.2016.
 */
public class LearningTemplateApi1Test extends JerseyTest {

    public static String testTemplate = "testtemplate";
    public static String testCompetence = "I know how to implement this api";

    @Override
    protected javax.ws.rs.core.Application configure() {
        DBInitializer.init();
        return new ResourceConfig(LearningTemplateApiImpl.class, CompetenceApiImpl.class, UserApiImpl.class, CourseApiImpl.class, EvidenceApiImpl.class, EvidenceServiceRestServerImpl.class);
    }

    @Before
    public void testCreateLearningtemplate() {
        String testTemplate = "testtemplate";
        LearningTemplateData learningTemplateData = new LearningTemplateData("testUser", Contexts.university.toString(), testTemplate);
        Response result = target("/api1/learningtemplates/" + testTemplate).request().put(Entity.entity(learningTemplateData, MediaType.APPLICATION_JSON));
        assertTrue(result.getStatus() == 200);
    }

    @Test
    public void testGetLearningtemplate() {
        LearningTemplateData learningTemplateData = getLearningTemplateData();
        StringList result = target("/api1/learningtemplates").request().get(StringList.class);
        assertFalse(result.getData().isEmpty());
        assertTrue(result.getData().contains(learningTemplateData.getSelectedTemplate()));
    }

    private LearningTemplateData getLearningTemplateData() {

        return new LearningTemplateData("testUser", Contexts.university.toString(), testTemplate);
    }

    @Test
    public void testGetSelectedLearningtemplate() {
        LearningTemplateData learningTemplateData = getLearningTemplateData();
        StringList result = target("/api1/learningtemplates").queryParam("userId", learningTemplateData.getUserName()).queryParam("courseId", learningTemplateData.getGroupId()).request().get(StringList.class);
        assertFalse(result.getData().isEmpty());
        assertTrue(result.getData().contains(learningTemplateData.getSelectedTemplate()));
        StringList result2 = target("/api1/learningtemplates").queryParam("userId", "stuffyKrempel").queryParam("courseId", learningTemplateData.getGroupId()).request().get(StringList.class);
        assertTrue(result2.getData().isEmpty());
    }

    @Test
    public void addCompetenceToLearningProjectTemplate() {
        CompetenceData data = new CompetenceData("testoperator", Arrays.asList(new String[] {"api", "implement"}),  new ArrayList<String>(), new ArrayList<String>(), testTemplate, testCompetence);
        Response response = target("/api1/competences/"+testCompetence).request().put(Entity.entity(data, MediaType.APPLICATION_JSON));
        assertTrue(response.getStatus() == 200);
        java.util.List<String> result = target("/api1/competences").queryParam("courseId", "university").queryParam("learningTemplate", testTemplate).queryParam("asTree", false).request().get(java.util.List.class);
        assertFalse(result.isEmpty());

        java.util.List<String> result2 = target("/api1/competences").queryParam("courseId", "university").queryParam("learningTemplate", "anderemudda").queryParam("asTree", false).request().get(java.util.List.class);
        assertTrue(result2.isEmpty());

        System.out.println(result);
    }



    @After
    public  void testDeleteLearningtemplate() {
        String testTemplate = "testtemplate";
        Response response =  target("/api1/learningtemplates/"+testTemplate).request().delete();
        assertTrue(response.getStatus() == 200);
    }
}
