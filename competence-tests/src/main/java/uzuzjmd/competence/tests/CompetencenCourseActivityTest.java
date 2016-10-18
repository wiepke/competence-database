package uzuzjmd.competence.tests;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

import uzuzjmd.competence.evidence.service.rest.EvidenceServiceRestServerImpl;
import uzuzjmd.competence.persistence.dao.DBInitializer;
import uzuzjmd.competence.service.rest.*;

public class CompetencenCourseActivityTest extends JerseyTest {

    private static final String course = "15";
    private static final String activityURL = "activityURL";

    @Override
    protected javax.ws.rs.core.Application configure() {
        DBInitializer.init();
        return new ResourceConfig(LearningTemplateApiImpl.class, CompetenceApiImpl.class, UserApiImpl.class, CourseApiImpl.class, EvidenceApiImpl.class, EvidenceServiceRestServerImpl.class, CompetenceServiceRestJSON.class);
    }

    @Test
    public void testCreateSuggestedCourseForCompetence() {
        final int status1 = addSuggestedCourseForCompetence("r1", course);
        final int status2 = addSuggestedCourseForCompetence("r2", course);
        final int status3 = addSuggestedCourseForCompetence("r3", course);

        Assert.assertEquals(200, status1);
        Assert.assertEquals(200, status2);
        Assert.assertEquals(200, status3);

        final List<String> competencues = getCompetenceFormSuggestedCourse(course);
        final List<String> tmp = Arrays.asList("r1", "r2", "r3");

        for (String c : competencues) {
            Assert.assertTrue(tmp.contains(c));
        }

        final List<String> courses = getSuggestedCourseForCompetence("r1");
        Assert.assertEquals("15", String.valueOf(courses.get(0)));
    }

    @Test
    public void testDeleteSuggestedCourseForCompetence() {
        final int status1 = deleteSuggestedCourseForCompetence("r1", course);
        final int status2 = deleteSuggestedCourseForCompetence("r2", course);
        final int status3 = deleteSuggestedCourseForCompetence("r3", course);

        Assert.assertEquals(200, status1);
        Assert.assertEquals(200, status2);
        Assert.assertEquals(200, status3);

        List<String> competencen = getCompetenceFormSuggestedCourse(course);

        Assert.assertEquals(0, competencen.size());
    }

    @Test
    public void testCreateSuggestedActivityForCompetence() {
        final int status1 = addSuggestedActivityForCompetence("t1", activityURL);
        final int status2 = addSuggestedActivityForCompetence("t2", activityURL);
        final int status3 = addSuggestedActivityForCompetence("t3", activityURL);

        Assert.assertEquals(200, status1);
        Assert.assertEquals(200, status2);
        Assert.assertEquals(200, status3);

        final List<String> competencen = getCompetenceFormSuggestedActivity(activityURL);
        final List<String> tmp = Arrays.asList("t1", "t2", "t3");

        for (String c : competencen) {
            Assert.assertTrue(tmp.contains(c));
        }
        
        final List<String> activities = getSuggestedActivityForCompetence("t1");
        
        Assert.assertEquals(activityURL, String.valueOf(activities.get(0)));
    }

    @Test
    public void testDeleteSuggestedActivityForCompetence() {
        final int status1 = deleteSuggestedActivityForCompetence("t1", activityURL);
        final int status2 = deleteSuggestedActivityForCompetence("t2", activityURL);
        final int status3 = deleteSuggestedActivityForCompetence("t3", activityURL);

        Assert.assertEquals(200, status1);
        Assert.assertEquals(200, status2);
        Assert.assertEquals(200, status3);
    }


    private List<String> getCompetenceFormSuggestedCourse(String course) {
    	String[] response = target("/competences/SuggestedCompetencesForCourse/" + course)
				.request().get(String[].class);
    	
        return Arrays.asList(response);
    }

    private List<String> getSuggestedCourseForCompetence(String competence) {
        String[] response = target("/competences/SuggestedCourseForCompetence/")
        		.queryParam("competence", competence)
				.request().get(String[].class);

        return Arrays.asList(response);
    }


    private List<String> getCompetenceFormSuggestedActivity(String activityURL) {
        String[] response = target("/competences/CompetencesForSuggestedActivity/get")
        		.queryParam("activityURL", activityURL)
				.request().get(String[].class);

        return Arrays.asList(response);
    }
    
    private List<String> getSuggestedActivityForCompetence(String competence) {
        String[] response = target("/competences/SuggestedActivityForCompetence/get").queryParam("competence", competence)
				.request().get(String[].class);

        return Arrays.asList(response);
    }

    private int addSuggestedCourseForCompetence(String competence, String course) {
        Response response = target("/competences/SuggestedCourseForCompetence/create")
        		.queryParam("competence", competence)
        		.queryParam("course", course)
				.request().post(null);

        return response.getStatus();
    }
    
    private int deleteSuggestedCourseForCompetence(String competence, String course) {
    	Response response = target("/competences/SuggestedCourseForCompetence/delete")
        		.queryParam("competence", competence)
        		.queryParam("course", course)
				.request().post(null);

        return response.getStatus();
    }
    
    private int addSuggestedActivityForCompetence(String competence, String activityUrl) {
    	Response response = target("/competences/SuggestedActivityForCompetence/create")
        		.queryParam("competence", competence)
        		.queryParam("activityUrl", activityUrl)
				.request().post(null);

        return response.getStatus();
    }
    
    private int deleteSuggestedActivityForCompetence(String competence, String activityUrl) {
    	Response response = target("/competences/SuggestedActivityForCompetence/delete")
        		.queryParam("competence", competence)
        		.queryParam("activityUrl", activityUrl)
				.request().post(null);

        return response.getStatus();
    }
}
