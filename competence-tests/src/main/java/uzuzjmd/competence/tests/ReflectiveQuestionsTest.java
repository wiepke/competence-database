package uzuzjmd.competence.tests;

import datastructures.lists.StringList;
import junit.framework.Assert;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import uzuzjmd.competence.evidence.service.rest.EvidenceServiceRestServerImpl;
import uzuzjmd.competence.persistence.dao.DBInitializer;
import uzuzjmd.competence.persistence.dao.User;
import uzuzjmd.competence.persistence.neo4j.CompetenceNeo4jQueryManagerImpl;
import uzuzjmd.competence.service.rest.*;
import uzuzjmd.competence.shared.assessment.ReflectiveQuestionAnswerData;
import uzuzjmd.competence.shared.assessment.ReflectiveQuestionAnswerHolder;
import uzuzjmd.competence.shared.assessment.ReflectiveQuestionData;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by dehne on 21.09.2016.
 */
public class ReflectiveQuestionsTest extends JerseyTest {
    @Override
    protected javax.ws.rs.core.Application configure() {
        DBInitializer.init();
        return new ResourceConfig(LearningTemplateApiImpl.class, CompetenceApiImpl.class, UserApiImpl.class,
                CourseApiImpl.class, EvidenceApiImpl.class, EvidenceServiceRestServerImpl.class);
    }

    @Test
    public void testCRUD() {
        ReflectiveQuestionData reflectiveQuestionData =
                new ReflectiveQuestionData("Kannst du barzilargisieren=?", "Kompetenz");
        Entity<ReflectiveQuestionData> payLoad;
        payLoad = Entity.entity(reflectiveQuestionData, MediaType.APPLICATION_JSON);
        Response response = target("/api1/competences/questions").request(MediaType.TEXT_PLAIN).post(payLoad);
        Assert.assertTrue(response.getStatus() == 200);
        StringList result =  target("/api1/competences/Kompetenz/questions").request().get(StringList.class);
        assertFalse(result.getData().isEmpty());
    }

    @Test
    public void testReflectiveQuestionAnswer() throws Exception {

        testCRUD();

        User user = new User("Mephisto@stuff.com", "Mephisto", "Moodle", "university");
        user.persist();
        StringList result =  target("/api1/competences/Kompetenz/questions").request().get(StringList.class);
        assertFalse(result.getData().isEmpty());
        String questionId = result.getData().get(0);


        String urlPart1 = "/api1/competences/questions/";
        String urlPart2 = "/answers/users/";
        String url3 = "/api1/competences/questions/answers";
        //questionId = "test";
        StringBuilder builder = new StringBuilder();
        builder.append(url3);
        System.out.println(builder.toString());
        ReflectiveQuestionAnswerData reflectiveQuestionAnswerData = new ReflectiveQuestionAnswerData("ShizzleAnswer",
                user.getId(), questionId, null,"Kompetenz");
        Response response  = target(builder.toString()).request().put(Entity.entity(reflectiveQuestionAnswerData,
                MediaType
                .APPLICATION_JSON));
        assertTrue(response.getStatus() == 200);
        StringBuilder builder2 = new StringBuilder();
        builder2.append(urlPart1).append(questionId).append(urlPart2).append(user.getId());
        System.out.print(builder2.toString());
        ReflectiveQuestionAnswerHolder response2 = target(builder2.toString()).request().get
                (ReflectiveQuestionAnswerHolder.class);
        assertTrue(response2.getData().get(0).getText().equals(reflectiveQuestionAnswerData.getText()));
    }

    @Test
    public void test() throws Exception{
        CompetenceNeo4jQueryManagerImpl competenceNeo4jQueryManager = new CompetenceNeo4jQueryManagerImpl();
        ReflectiveQuestionAnswerHolder result =
                competenceNeo4jQueryManager.getReflectiveQuestionAnswers("Mephisto@stuff.com", "Kannst du " +
                        "barzilargisieren=?", null);

        assertTrue(result.getData().size() > 0 );

    }
}
