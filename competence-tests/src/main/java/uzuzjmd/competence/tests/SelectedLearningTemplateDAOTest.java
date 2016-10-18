package uzuzjmd.competence.tests;


import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;
import uzuzjmd.competence.persistence.dao.DBInitializer;
import uzuzjmd.competence.service.rest.*;
import uzuzjmd.competence.shared.assessment.Assessment;
import uzuzjmd.competence.shared.assessment.ReflectiveAssessmentsListHolder;
import datastructures.lists.StringList;
import uzuzjmd.competence.shared.learningtemplate.SuggestedCompetenceGrid;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SelectedLearningTemplateDAOTest extends JerseyTest {

    private final static String user = "xunguyen";

    @Override
    protected javax.ws.rs.core.Application configure() {
        DBInitializer.init();
        return new ResourceConfig(LearningTemplateApiImpl.class, CompetenceApiImpl.class, UserApiImpl.class, CourseApiImpl.class, EvidenceApiImpl.class, CompetenceServiceRestJSON.class);
    }


    @Test
    public void createTestUser() {
        Response post = target("/competences/user/create/" + user + "/teacher").queryParam("groupId", "user").request(MediaType.APPLICATION_JSON).post(Entity.json(null));
        assertTrue(post.getStatus() == 200);

    }


    @Test
    public void testSelectTemplate() throws InterruptedException {
        createTestUser();
        Response post = target("/competences/learningtemplates/selected/add").queryParam("userId", user)
                .queryParam("groupId", "user")
                .queryParam("selectedTemplate", LearningTemplateDaoTest.learningTemplateName).request(MediaType.APPLICATION_XML)
                .post(null);
        assertTrue(post.getStatus() == 200);

    }

    /**
     * tests that interface does not through exception may not return data if
     * user has not previously selected learning templates for himself to learn
     */
    @Test
    public void testSelectedTemplates() throws InterruptedException {
        testSelectTemplate();
        WebTarget target = target("/competences/learningtemplates/selected");

        StringList result = target.queryParam("userId", user)
                .queryParam("groupId", "user").request()
                .accept(MediaType.APPLICATION_XML).get(StringList.class);

        assertFalse(result.getData().isEmpty());
        Assert.assertNotNull(result);
    }

    @Test
    public void testUpdateReflexion() throws InterruptedException {
        // setup data
        testSelectedTemplates();
        LearningTemplateDaoTest learningTemplateDaoTest = new LearningTemplateDaoTest();
        learningTemplateDaoTest.initTestGraph();
        // TODO Insert this part again
        /**
        learningTemplateDaoTest.testCreateTemplateWithGraph();
        SuggestedCompetenceGrid result = testFetchingGrid();
        Assert.assertNotNull(result);
        testSendingNewGrid(result);
        */
    }


    private SuggestedCompetenceGrid testFetchingGrid() {
        WebTarget target = target("/competences/learningtemplates/gridview");
        SuggestedCompetenceGrid result = target
                    .queryParam("userId", user)
                    .queryParam("groupId", "user")
                    .queryParam("selectedTemplate", LearningTemplateDaoTest.learningTemplateName).request()
                    .accept(MediaType.APPLICATION_XML)
                    .get(SuggestedCompetenceGrid.class);
        return result;
    }

    private void testSendingNewGrid(SuggestedCompetenceGrid result) {
        System.out.println(result.getSuggestedCompetenceRows().get(0).getSuggestedCompetenceRowHeader());

        ReflectiveAssessmentsListHolder holder = result.getSuggestedCompetenceRows().get(0).getSuggestedCompetenceColumns().get(0).getReflectiveAssessmentListHolder();
        holder.getReflectiveAssessmentList().get(0).setIsLearningGoal(true);
        holder.getReflectiveAssessmentList().get(0).setAssessment(new Assessment().getItems().get(1));
        System.out.println("updating: " + holder.getSuggestedMetaCompetence());

        WebTarget target = target("/competences/learningtemplates/gridview/update");
        Response post = target.queryParam("userId", user)
                .queryParam("groupId", "user").request()
                .post(Entity.entity(holder, MediaType.APPLICATION_JSON));
        assertTrue(post.getStatus() == 200);

    }
}
