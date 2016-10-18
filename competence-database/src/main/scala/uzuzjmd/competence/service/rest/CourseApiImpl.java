package uzuzjmd.competence.service.rest;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import uzuzjmd.competence.evidence.model.LMSSystems;
import uzuzjmd.competence.evidence.service.MoodleEvidenceRestServiceImpl;
import uzuzjmd.competence.evidence.service.rest.EvidenceServiceRestServerImpl;
import uzuzjmd.competence.mapper.rest.read.Ont2SelectedCompetencesForCourse;
import uzuzjmd.competence.persistence.dao.Competence;
import uzuzjmd.competence.persistence.dao.CourseContext;
import uzuzjmd.competence.persistence.ontology.Edge;
import uzuzjmd.competence.shared.course.CourseData;
import uzuzjmd.competence.shared.moodle.UserTree;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by dehne on 11.04.2016.
 */
@Path("/api1")
public class CourseApiImpl implements uzuzjmd.competence.api.CourseApi {

    @ApiOperation(value = "get courses stored in db", notes = "does not yield courses of the use /users api for that " +
            "as lms courses need authentication token of the user to be acquired")
    @Override
    @Path("/courses")
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<CourseData> getCourses(   @ApiParam(value = "the competence the course is connected to, competence " +
            "field will be null if no competence is specified since querying all competences is too much load",
            required = false) @QueryParam("competence") List<String> competences) throws Exception {
        if (competences == null || competences.isEmpty()) {
            List<CourseData> result = new ArrayList<>();
            Set<CourseContext> result1 = CourseContext.getAllInstances(CourseContext.class);
            convertCourseDaosToCourseData(result, Lists.newArrayList(result1), null);
            return result;
        }
        List<CourseData> result = new ArrayList<>();
        for (String competence : competences) {
            List<CourseContext> result1 = new Competence(competence).getAssociatedDaosAsRange(Edge
                    .CourseContextOfCompetence, CourseContext.class);
            List<String> competenceX = Arrays.asList(competence);
            convertCourseDaosToCourseData(result, result1, competenceX);
        }
        return result;
    }

    private void convertCourseDaosToCourseData(
            List<CourseData> result, List<CourseContext> result1, List<String> competenceX) {
        for (CourseContext courseContext : result1) {
            result.add(new CourseData(courseContext.getId(), courseContext.printableName, competenceX));
        }
    }

    @ApiOperation(value =
    "create local course in db", notes = "This will not create a course in the lms since this would require different" +
            " permissions and the interface is not implemented in the lms adaptor.")
    @Override
    @Path("/courses/{courseId}")
    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    public Response addCourse(@PathParam("courseId") String courseId, CourseData data) throws Exception {
        return addCourseIntern(courseId, data);
    }

    private Response addCourseIntern(@PathParam("courseId") String courseId, CourseData data) throws Exception {
        CourseContext courseContext = new CourseContext(courseId);
        courseContext.persist();
        if (data.getCompetences() != null && !data.getCompetences().isEmpty()) {
            for (String string: data.getCompetences()) {
                Competence competence = new Competence(string);
                competence.persist();
                competence.addCourseContext(courseContext);
            }
        }
        return Response.ok("courseId added").build();
    }

    @ApiOperation(value =
            "delete local course in db", notes = "This will not delete a course in the lms since this would require " +
            "different" +
            " permissions and the interface is not implemented in the lms adaptor. This api should be used carefully " +
            "since all the links to the course would also be deleted.")
    @Override
    @Path("/courses/{courseId}")
    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteCourse(@PathParam("courseId") String courseId) throws Exception {
        CourseContext courseContext = new CourseContext(courseId);
        courseContext.delete();
        return Response.ok("courseId deleted").build();
    }


    /**
     * the method can only be accessed if the authentication of a user in that courseId is provided for reasons
     * of privacy.
     *
     * @param courseId
     * @param userId
     * @param password
     * @return
     */
    @ApiOperation(value = "get activities registered for the course id in the lms")
    @Override
    @Path("/courses/{courseId}/activities")
    @GET
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public UserTree[] getActivitiesForUser(@PathParam("courseId") String courseId, @QueryParam("userId") String userId, @QueryParam("password") String password) {
        userId = EvidenceServiceRestServerImpl.checkLoginisEmail(userId);
        MoodleEvidenceRestServiceImpl moodleEvidenceRestService = new MoodleEvidenceRestServiceImpl();
        return moodleEvidenceRestService.getUserTree(courseId, LMSSystems.moodle.toString(), "university", userId, password);
    }
}
