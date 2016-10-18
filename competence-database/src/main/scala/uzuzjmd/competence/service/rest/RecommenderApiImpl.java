package uzuzjmd.competence.service.rest;

/**
 * Created by dehne on 31.03.2016.
 */

import uzuzjmd.competence.mapper.rest.write.SuggestedActivityForCompetence2Ont;
import uzuzjmd.competence.mapper.rest.write.SuggestedCourseForCompetence2Ont;
import uzuzjmd.competence.persistence.dao.Competence;
import uzuzjmd.competence.persistence.dao.CourseContext;
import uzuzjmd.competence.persistence.dao.EvidenceActivity;
import uzuzjmd.competence.persistence.ontology.Edge;
import uzuzjmd.competence.recommender.RecommenderFactory;
import datastructures.maps.MapWrapper;
import uzuzjmd.competence.shared.activity.Evidence;
import uzuzjmd.competence.shared.moodle.UserCourseListItem;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;

/**
 * Root resource (exposed at "competences" path)
 */
@Path("/api1")
public class RecommenderApiImpl implements uzuzjmd.competence.api.RecommenderApi {

    @Override
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/recommendations/competences/{userEmail}/competences")
    public HashMap<String, Double> recommendCompetences(@PathParam("userEmail") String userEmail, @QueryParam("competenceToReach") String competenceToReach, @QueryParam("courseId") String courseId) {
        return RecommenderFactory.createCompetenceRecommender().recommendCompetences(userEmail, competenceToReach, courseId);
    }

    @Override
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/recommendations/activities/{userEmail}")
    public HashMap<Evidence, Double> recommendActivities(@PathParam("userEmail") String userEmail, @QueryParam("competenceToReach") String competenceToReach, @QueryParam("courseId") String courseId, @QueryParam("password") String password) {
        return RecommenderFactory.createActivityRecommender().recommendActivities(userEmail, competenceToReach, courseId, password);
    }

    @Override
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/recommendations/courses/{userEmail}/courses")
    public MapWrapper<UserCourseListItem, Double> recommendCourses(@PathParam("userEmail") String userEmail) {
        return new MapWrapper<>(RecommenderFactory.createCourseRecommender().recommendCourse(userEmail));
    }


    @Override
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/recommendations/courses/{courseId}/competences")
    public String[] getSuggestedCompetencesForCourse(
            @PathParam("courseId") String courseId) throws Exception {
        CourseContext context = new CourseContext(courseId);
        if (context.getAssociatedDaoIdsAsDomain(Edge.CourseContextOfCompetence) == null) {
            return new String[0];
        }
        List<String> result = context.getAssociatedDaoIdsAsDomain(Edge.CourseContextOfCompetence);
        result.remove("Kompetenz");
        return result.toArray(new String[0]);
    }


    @Override
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/recommendations/competences/{competenceId}")
    public String[] getSuggestedCoursesForCompetence(
            @PathParam("competenceId") String competenceId) throws Exception {
        Competence context = new Competence(competenceId);

        if (context.getAssociatedDaoIdsAsRange(Edge.CourseContextOfCompetence) == null) {
            return new String[0];
        }
        List<String> result = context.getAssociatedDaoIdsAsRange(Edge.CourseContextOfCompetence);
        return result.toArray(new String[0]);
    }


    @Override
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/recommendations/competences/{competenceId}/activities/{activityId}")
    public Response createSuggestedActivityForCompetence(@PathParam("competenceId") String competenceId, @PathParam("activityId") String activityId) throws Exception {
        EvidenceActivity activity = new EvidenceActivity(activityId);
        if (!activity.exists()) {
            return Response.status(422).entity("activity does not exist in db").build();
        }
        Competence competence1 = new Competence(competenceId);
        if (!competence1.exists()) {
            return Response.status(422).entity("competence does not exist in db").build();
        }
        SuggestedActivityForCompetence2Ont.write(activityId, competenceId);
        return Response.ok("edge created").build();
    }



    @Override
    @Consumes(MediaType.APPLICATION_JSON)
    @DELETE
    @Path("/recommendations/competences/{competenceId}/courses/{courseId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteSuggestedCourseForCompetence(@PathParam("competenceId") String competence, @PathParam("courseId") String course) {
        SuggestedCourseForCompetence2Ont.delete(course, competence);
        return Response.ok("edge deleted").build();
    }

    @Override
    @DELETE
    @Path("/recommendations/competences/{competenceId}/activities/{activityId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteSuggestedActivityForCompetence(@PathParam("competenceId") String competenceId, @PathParam("activityId") String activityId) {
        SuggestedActivityForCompetence2Ont.delete(activityId, competenceId);
        return Response.ok("edge created").build();
    }





}
