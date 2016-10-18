package uzuzjmd.competence.service.rest;

import io.swagger.annotations.ApiOperation;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import uzuzjmd.competence.evidence.service.moodle.SimpleMoodleService;
import uzuzjmd.competence.mapper.rest.read.Ont2UserProgress;
import uzuzjmd.competence.mapper.rest.write.UserProgress2Ont;
import uzuzjmd.competence.shared.assessment.IAssessment;
import uzuzjmd.competence.shared.assessment.TypeOfSelfAssessment;
import uzuzjmd.competence.persistence.dao.User;
import uzuzjmd.competence.shared.assessment.AbstractAssessment;
import uzuzjmd.competence.shared.badges.BadgeDataList;
import uzuzjmd.competence.shared.progress.UserCompetenceProgress;
import uzuzjmd.competence.shared.progress.UserProgress;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Created by dehne on 30.05.2016.
 *
 *
 */
@Path("/api1")
public class ProgressApiImpl implements uzuzjmd.competence.api.ProgressApi {

    private Logger logger = LogManager.getLogger(getClass());

    @ApiOperation(value = "get the progress of a user")
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/progress/{userId}")
    @GET
    public UserProgress getUserProgress(@PathParam("userId") String userId, @QueryParam("courseId") String courseId) throws Exception {
        userExists(userId);
        if (courseId == null) {
            courseId = "university";
        }
        try {
            return Ont2UserProgress.convert2(userId, courseId);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new UserProgress();
    }


    @ApiOperation(value = "get the progress of a user for the competence given")
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/progress/{userId}/competences/{competenceId}")
    @GET
    public UserCompetenceProgress getUserCompetenceProgress(@PathParam("userId") String userId, @PathParam("competenceId") String competenceId) throws Exception {
        if (!userExists(userId)) {
            Response.serverError().entity("user does not exist in database");
        }
        UserCompetenceProgress result = Ont2UserProgress.convert(competenceId, userId);
        return result;
    }

    @ApiOperation(value = "update the progress of a user")
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/progress/{userId}")
    @PUT
    public Response updateOrCreateUserProgress(@PathParam("userId") String userId, UserProgress userProgress) throws Exception {
        if (!userExists(userId)) {
            return Response.serverError().entity("user does not exist in database").build();
        }
        if (!userProgress.getUserCompetenceProgressList().isEmpty()) {
            java.util.Collection<AbstractAssessment> assessments =
                    userProgress.getUserCompetenceProgressList().iterator().next().getAbstractAssessment();
            TypeOfSelfAssessment[] allowedValues = TypeOfSelfAssessment.values();
            java.util.List<String> allowedValuesConverted = new ArrayList<>();
            for (TypeOfSelfAssessment allowedValue : allowedValues) {
                allowedValuesConverted.add(allowedValue.toString());
            }
            for (IAssessment assessment : assessments) {
                if (!allowedValuesConverted.contains(assessment.getTypeOfSelfAssessment().toString())) {
                    return Response.serverError().entity("Identifier must be one of " + allowedValuesConverted.toString()).build();
                }
            }
        }
        UserProgress2Ont.convert(userProgress, (User) new User(userId).getFullDao());
        return Response.ok("progress updated").build();
    }

    @ApiOperation(value = "update the progress of a user for the competence given")
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/progress/{userId}/competences/{competenceId}")
    @PUT
    public Response updateOrCreateUserProgress(@PathParam("userId") String userId, @PathParam("competenceId") String competenceId, UserCompetenceProgress userProgress) throws Exception {
        if (!userExists(userId)) {
            return Response.serverError().entity("user does not exist").build();
        }
        UserProgress2Ont.convert(new UserProgress(userProgress), new User(userId));
        return Response.ok("progress updated").build();
    }

    private Boolean userExists(@PathParam("userId") String userId) throws Exception {
        if (!new User(userId).exists()) {
            //logger.error("user does not exist in database when trying to update user progress");
            return false;
        } return true;
    }

    @ApiOperation(value = "get the badges the user has been awarded in the lms")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/progress/badges")
    @GET
    public BadgeDataList getBadgesForUser(@QueryParam("userId") String username, @QueryParam("password") String password) {
        try {
            SimpleMoodleService simpleMoodleService = new SimpleMoodleService(username, password);
            return simpleMoodleService.getMoodleBadges(username);
        } catch (Exception e) {
            return new BadgeDataList();
        }
    }
}
