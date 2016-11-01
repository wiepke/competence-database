package uzuzjmd.competence.tests;

import datastructures.lists.StringList;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import uzuzjmd.competence.evidence.service.moodle.SimpleMoodleService;
import uzuzjmd.competence.evidence.service.rest.EvidenceServiceRestServerImpl;
import uzuzjmd.competence.persistence.dao.AbstractEvidenceLink;
import uzuzjmd.competence.persistence.dao.DBInitializer;
import uzuzjmd.competence.persistence.neo4j.CompetenceNeo4jQueryManagerImpl;
import uzuzjmd.competence.service.rest.*;
import uzuzjmd.competence.shared.activity.Evidence;
import uzuzjmd.competence.shared.activity.EvidenceData;
import uzuzjmd.competence.shared.badges.BadgeData;
import uzuzjmd.competence.shared.competence.CompetenceData;
import uzuzjmd.competence.shared.course.CourseData;
import uzuzjmd.competence.shared.moodle.SyncDataSet;
import uzuzjmd.competence.shared.user.UserData;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.junit.Assert.*;

/**
 * Created by dehne on 19.04.2016.
 */
public class CoreTests extends JerseyTest {


    @Override
    protected javax.ws.rs.core.Application configure() {
        DBInitializer.init();
        return new ResourceConfig(LearningTemplateApiImpl.class, CompetenceApiImpl.class, UserApiImpl.class,
                CourseApiImpl.class, EvidenceApiImpl.class, EvidenceServiceRestServerImpl.class);
    }

    @Test
    public void test() {
        final StringList hello = target("/api1/learningtemplates").request().get(StringList.class);
        assertFalse(hello == null);
    }

    //@Test
   // public List<String> createCompetences()  {
 /*       ArrayList<String> result = new ArrayList<String>();
        String competenceString = "Die Studierenden vergleichen zwei Sätze anhand ihrer Bausteine";
        result.add(competenceString);
        CompetenceData data =
                new CompetenceData("vergleichen", Arrays.asList(new String[]{"vergleichen", "Sätze", "Bausteine"}),
                        null, null, null, competenceString);
        Response post = target("/api1/competences/" + competenceString).request()
                .put(Entity.entity(data, MediaType.APPLICATION_JSON));
        assertTrue(post.getStatus() == 200);
        String competenceString2 = "Die Studierenden vergleichen drei Sätze anhand ihrer Bausteine";
        result.add(competenceString2);
        Response post1 = target("/api1/competences/" + competenceString2).request()
                .put(Entity.entity(data, MediaType.APPLICATION_JSON));
        assertTrue(post1.getStatus() == 200);*/
        //Thread.sleep(3000l);
//        java.util.List<String> result = target("/api1/competences/semblances/"+competenceString).request().get(java.util.List.class);
//        assertTrue(result.contains(competenceString2));
   //     return result;
    //}

    //@Test
    public void testLogin() {
        String response = target("/lms/user/exists").queryParam("user", "dehne").queryParam("password", "000")
                .queryParam("lmsSystem", "moodle").request().get(String.class);
        assertTrue(response.equals("true"));
    }


    //@Test
    public void testLoginApi1() {
        String response = target("/api1/users/dehne/exists").queryParam("password", "###").request().get(String.class);
        assertTrue(response.equals("true"));
    }


    @Test
    public void lmsSystems() {
        List response = target("/lms/systems").request().get(List.class);
        assertFalse(response.isEmpty());
    }


    @Test
    public void testCompetenceDeletionAndHiding() throws InterruptedException {
        String competenceString = "Die Studierenden vergleichen y Sätze anhand ihrer Bausteine";
        String userEmail = createUser();

        // creates competence
        CompetenceData data =
                new CompetenceData("vergleichen", Arrays.asList(new String[]{"vergleichen", "Sätze", "Bausteine"}),
                        null, null, null, competenceString);
        Response post = target("/api1/competences/" + competenceString).request()
                .put(Entity.entity(data, MediaType.APPLICATION_JSON));
        assertTrue(post.getStatus() == 200);

        Entity.entity(userEmail, MediaType.APPLICATION_JSON);
        // deletes the competence from the user's perspective
        Response delete = target("/api1/competences/" + competenceString + "/users/" + userEmail).request().delete();
        assertTrue(delete.getStatus() == 200);

        // assertions
        List get = target("/api1/competences").queryParam("courseId", "university").request().get(java.util.List.class);
        assertFalse(get.isEmpty());

        List get2 = target("/api1/competences").queryParam("courseId", "university").queryParam("userId", userEmail)
                .request().get(java.util.List.class);
        assertTrue(get2.isEmpty());
    }

    public String createUser() {
        String userEmail = "julian@stuff.com";

        // creates user
        UserData userData = new UserData(userEmail, "Julian Dehne", null, "student", null);
        Response post1 =
                target("/api1/users/" + userEmail).request().put(Entity.entity(userData, MediaType.APPLICATION_JSON));
        assertTrue(post1.getStatus() == 200);
        return userEmail;
    }


    @Test
    public void testGetCompetencesUserHasAcquired() {
        String userEmail = "julian@stuff2.com";
        String courseName = "TestkursA";
        String courseId = "44";
        String url = "http://hasstschegut";
        Evidence evidence = new Evidence("gut gemacht", url, userEmail);

        // create course context
        // creates a course for the test case which the competence is linked to
        CourseData data = new CourseData("44", courseName);
        Entity<CourseData> courseDataEntity = Entity.entity(data, APPLICATION_JSON);
        Response post0 = target("/api1/courses/" + data.getCourseId()).request().put(courseDataEntity);
        assertTrue(post0.getStatus() == 200);

        // creates user
        // creates the user who has performed the competence
        UserData userData = new UserData(userEmail, "Julian Dehne", data.getCourseId(), "student", "mobile");
        Entity<UserData> userEntity = Entity.entity(userData, MediaType.APPLICATION_JSON);
        Response post1 = target("/api1/users/" + userEmail).request().put(userEntity);
        assertTrue(post1.getStatus() == 200);

        // create evidence
        // creates the evidence that the user rly has performed the competence
        EvidenceData evidenceData =
                new EvidenceData(courseId, userEmail, Arrays.asList(new String[]{"kann jetzt linken"}), evidence);
        Entity<EvidenceData> evidenceDataEntity = Entity.entity(evidenceData, APPLICATION_JSON);


     /*   Boolean works = target("/api1/evidences").request().get(Boolean.class);
        assertTrue(works);*/

        url = URLEncoder.encode(url);

        Response post2 = target("/api1/evidences/create").request().put(evidenceDataEntity);
        System.out.print("status is: " + post2.getStatus() + " message is :");
        assertTrue(post2.getStatus() == 200);

        // assertions
        // asserts that the competence is listed as one of the competences the user has accomplished
        StringList get = target("/api1/users/" + userEmail + "/competences").queryParam("courseId", courseId).request()
                .get(StringList.class);
        assertNotNull(get);
        assertFalse(get.getData().isEmpty());

    }

    //@Test
    public void testGetAllUsersInCourse() {
        String userEmail = "julian@stuff2.com";
        String courseName = "TestkursA";

        CourseData data = new CourseData("44", courseName);
        Entity<CourseData> courseDataEntity = Entity.entity(data, MediaType.APPLICATION_JSON);
        Response post0 = target("/api1/courses/" + data.getCourseId()).request().put(courseDataEntity);
        assertTrue(post0.getStatus() == 200);

        // creates user
        UserData userData = new UserData(userEmail, "Julian Dehne", data.getCourseId(), "student", "mobile");
        Entity<UserData> userEntity = Entity.entity(userData, MediaType.APPLICATION_JSON);
        Response post1 = target("/api1/users/" + userEmail).request().put(userEntity);
        assertTrue(post1.getStatus() == 200);

        // assertions
        //List get = target("/api1/users/").queryParam("courseId", data.getCourseId()).request().get(java.util.List.class);
        //assertFalse(get.isEmpty());

    }


    @Test
    public void getUserAssessments() throws Exception {
        // tested in ProgressApiTests
    }

    @Test
    public void getBadges() {
       /* SimpleMoodleService simpleMoodleService = new SimpleMoodleService("teststudent", "voyager1;A");
        java.util.List<BadgeData> root = simpleMoodleService.getMoodleBadges("teststudent");
        System.out.print(root.get(0).getDescription().toString());
        assertTrue(root != null);*/
    }

    @Test
    public void sync() {
        SyncDataSet syncDataSet = new SyncDataSet("teststudent", "voyager1;A", true, true, true, true);
        Response post = target("/api1/users/sync/moodle").request().post(Entity.entity(syncDataSet, MediaType
                .APPLICATION_JSON));
        assertTrue(post.getStatus() == 200);
    }

}
