package uzuzjmd.competence.tests;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import uzuzjmd.competence.evidence.service.rest.EvidenceServiceRestServerImpl;
import uzuzjmd.competence.persistence.dao.DBInitializer;
import uzuzjmd.competence.service.rest.*;
import uzuzjmd.competence.shared.activity.Evidence;
import uzuzjmd.competence.shared.competence.CompetenceData;
import uzuzjmd.competence.shared.user.UserData;
import datastructures.lists.StringList;
import datastructures.trees.ActivityEntry;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by dehne on 16.06.2016.
 */
public class ActivityApiTest extends JerseyTest {

    //String activityUrl = "agazigaabuggat";
    String activityUrl = "http://meinewunderschöneAktivität";
    String competenceId = "Ich kann programmieren";
    String userEmail = "julian@stuff.com";


    @Override
    protected javax.ws.rs.core.Application configure() {
        DBInitializer.init();
        return new ResourceConfig(LearningTemplateApiImpl.class, CompetenceApiImpl.class, UserApiImpl.class, CourseApiImpl.class, EvidenceApiImpl.class, EvidenceServiceRestServerImpl.class, ActivityApiImpl.class, RecommenderApiImpl.class);
    }

    @Test
    public void createActivity() {
        ActivityEntry activityEntry = new ActivityEntry("AktivitätenAktivitäten", null, null, activityUrl);
        Response result = target("/api1/activities").request(MediaType.TEXT_PLAIN).put(Entity.entity(activityEntry, MediaType.APPLICATION_JSON));
        assertTrue(result.getStatus() == 200);
    }

   /* @Test
    public void linkActivityToCompetence() {
        // create competence first
        CompetenceData data = new CompetenceData("programmieren", Arrays.asList(new String[]{"programmieren", "tabalugatv"}), null, null, null, competenceId);
        Response result = target("/api1/competences/"+data.getForCompetence()).request(MediaType.TEXT_PLAIN).put(Entity.entity(data, MediaType.APPLICATION_JSON));
        assertTrue(result.getStatus() == 200);
        createActivity();
        // create Link
        Response result2 = target("/api1/activities/links/competences/" + competenceId).queryParam("activityId", activityUrl)
                .request(MediaType.TEXT_PLAIN)
                .post(null);
        assertTrue(result2.getStatus() == 200);
    }*/

    @Test
    public void linkUserToCompetence() {
        // creates user
        UserData userData = new UserData(userEmail, "Julian Dehne", null, "student", null);
        Response post1 = target("/api1/users/" + userEmail).request().put(Entity.entity(userData, MediaType.APPLICATION_JSON));
        assertTrue(post1.getStatus() == 200);

        Response r = target("/api1/users/"+userEmail+"/interests/competences/"+competenceId).request().post(Entity.entity(null, MediaType.APPLICATION_JSON));
        assertTrue(r.getStatus() == 200);

        StringList result = target("/api1/users/"+userEmail+"/competences").queryParam("interestedIn", true).request().get(StringList.class);
        assertTrue(!result.getData().isEmpty());
    }


    /*@Test
    public void recommendCompetence() {
        linkActivityToCompetence();
        StringList result2 = target("/api1/activities/links/competences").queryParam("activityId", activityUrl).request(MediaType.APPLICATION_JSON).get(StringList.class);
        assertFalse(result2.getData().isEmpty());
    }*/

    @Test
    public void createEvidence() {
        // see evidence api tests
    }

    @Test
    public void suggestActivity() {
        HashMap<Evidence, Double> result = target("/api1/recommendations/activities/"+userEmail).request().get(HashMap.class);
        assertNotNull(result);
        // user activity recommender
        assertFalse(result.isEmpty());
        System.out.println(result.toString());
    }


}
