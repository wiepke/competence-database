package uzuzjmd.competence.service.rest;

import config.MagicStrings;
import datastructures.lists.StringList;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.Logger;
import uzuzjmd.competence.evidence.model.LMSSystems;
import uzuzjmd.competence.evidence.service.MoodleEvidenceRestServiceImpl;
import uzuzjmd.competence.evidence.service.moodle.MoodleEvidenceList;
import uzuzjmd.competence.evidence.service.moodle.SimpleMoodleService;
import uzuzjmd.competence.evidence.service.rest.EvidenceServiceRestServerImpl;
import uzuzjmd.competence.mapper.rest.write.Moodle2Ont;
import uzuzjmd.competence.persistence.dao.Competence;
import uzuzjmd.competence.persistence.dao.CourseContext;
import uzuzjmd.competence.persistence.dao.Role;
import uzuzjmd.competence.persistence.dao.User;
import uzuzjmd.competence.persistence.neo4j.CompetenceNeo4jQueryManagerImpl;
import uzuzjmd.competence.persistence.ontology.Edge;
import uzuzjmd.competence.shared.moodle.MoodleEvidence;
import uzuzjmd.competence.shared.moodle.SyncDataSet;
import uzuzjmd.competence.shared.moodle.UserCourseListItem;
import uzuzjmd.competence.shared.moodle.UserCourseListResponse;
import uzuzjmd.competence.shared.user.UserData;
import uzuzjmd.competence.shared.user.UserOverview;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

/**
 * Created by dehne on 11.04.2016.
 */
@Path("/api1")
public class UserApiImpl implements uzuzjmd.competence.api.UserApi {
    private Logger logger = Logger.getLogger(UserApiImpl.class);

    /**
     * @param userName
     * @param password
     * @return
     */
    @ApiOperation(value = "get full user details from local db and if provided with authentication from the lms")
    @Path("/users")
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public java.util.List<UserData> getUsers(@ApiParam(value = "the user name as in ...@uni-potsdam.de")
            @QueryParam("userName") String userName, @QueryParam("password") String password) throws Exception {
        HashSet<UserData> result = new HashSet<>();
        try {
            if (userName != null && password != null) {

                SimpleMoodleService simpleMoodleService = new SimpleMoodleService(userName, password);
                UserCourseListResponse moodleCourseList = simpleMoodleService.getMoodleCourseList();
                for (UserCourseListItem userCourseListItem : moodleCourseList) {
                    MoodleEvidenceList moodleEvidenceList =
                            simpleMoodleService.getMoodleEvidenceList(userCourseListItem.getCourseid() + "");
                    for (MoodleEvidence moodleEvidence : moodleEvidenceList) {
                        UserData userData = new UserData(moodleEvidence.getUserId(), moodleEvidence.getUsername(),
                                userCourseListItem.getCourseid() + "", "student", "moodle");
                        if (!result.contains(userData)) {
                            result.add(userData);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Set<User> allInstances = User.getAllInstances(User.class);
            for (User user : allInstances) {
                UserData userData =
                        new UserData(user.getId(), user.getPrintableName(), null, "student", MagicStrings.DB);
                result.add(userData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new LinkedList<UserData>(result);
    }


    @ApiOperation(value = "get full user details from local db")
    @Override
    @Path("/users/{userId}")
    @GET
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getUser(@ApiParam(value = "the user name as in ...@uni-potsdam.de") @PathParam("userId") String
                                        userId
    ) throws Exception {
        User user = new User(userId);
        if (!user.exists()) {
            return Response.status(200).entity(null).build();
        }
        User result = user.getFullDao();
        UserData data =
                new UserData(result.getId(), result.getPrintableName(), null, result.getRole().toString(), null);
        return Response.status(200).entity(data).build();

    }

    @ApiOperation(value = "add user to local db")
    @Override
    @Path("/users/{userId}")
    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    public Response addUser(@ApiParam(value = "the user name as in ...@uni-potsdam.de") @PathParam("userId") String
                                        userId, UserData data) throws Exception {
        return addUser(data);
    }

    private Response addUser(UserData data) throws Exception {
        Role role = null;
        if (data.getRole() != null) {
            role = Role.valueOf(data.getRole());
        }
        User user = new User(data.getUserId(), role.toString(), data.getPrintableName(), data.getLmsSystems(),
                new CourseContext(data.getCourseContext()));
        user.persist();
        return Response.ok("user added").build();
    }

    @ApiOperation(value = "delete user in local db")
    @Override
    @Path("/users/{userId}")
    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteUser(@PathParam("userId") String userId) throws Exception {
        User user = new User(userId);
        user.delete();
        return Response.ok("user deleted").build();
    }


    @ApiOperation(value = "get courses from user in lms")
    @Override
    @Path("/users/{userId}/courses")
    @GET
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public UserCourseListResponse getCoursesForUser(
            @PathParam("userId") String userId, @QueryParam("password") String password) {
        userId = EvidenceServiceRestServerImpl.checkLoginisEmail(userId);
        try {
            MoodleEvidenceRestServiceImpl moodleEvidenceRestService = new MoodleEvidenceRestServiceImpl();
            return moodleEvidenceRestService.getCourses(LMSSystems.moodle.toString(), "university", userId, password);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new UserCourseListResponse();
    }

    @ApiOperation(value = "check if user exists in lms")
    @Override
    @Path("/users/{userId}/exists")
    @GET
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean checksIfUserExists(@PathParam("userId") String userId, @QueryParam("password") String password)
            throws Exception {
        try {
            userId = EvidenceServiceRestServerImpl.checkLoginisEmail(userId);
            EvidenceServiceRestServerImpl evidenceServiceRestServer = new EvidenceServiceRestServerImpl();
            Boolean result = evidenceServiceRestServer.exists(userId, password, "moodle", null);
            return result;
        } catch (Exception e) {

        }
        return false;
    }

    /**
     * get competences the user has acquired or is interested in (interested in is boolean flag)
     *
     * @param userId
     * @param interestedIn
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "get competences user has learned or is interested in")
    @Path("/users/{userId}/competences")
    @GET
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public StringList getCompetencesForUser(
            @PathParam("userId") String userId, @QueryParam("interestedIn") Boolean interestedIn) throws Exception {
        List<String> result = new ArrayList<>();
        User user = new User(userId);
        if (interestedIn != null && interestedIn) {
            List<Competence> competencesInterestedIn = user.getCompetencesInterestedIn();
            for (Competence competence : competencesInterestedIn) {
                result.add(competence.getDefinition());
            }
        } else {
            List<Competence> competencesLearned = user.getCompetencesLearned();
            for (Competence competence : competencesLearned) {
                result.add(competence.getDefinition());
            }
        }
        return new StringList(result);
    }


    /**
     * set interested competences for user
     *
     * @param userId
     * @param competenceId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "add competences user is interested in")
    @Path("/users/{userId}/interests/competences/{competenceId}")
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    public Response setCompetencesForUser(
            @PathParam("userId") String userId, @PathParam("competenceId") String competenceId) throws Exception {
        User user = new User(userId);
        if (!user.exists()) {
            return Response.status(422).entity("User does not exist in database").build();
        }
        Competence competence1 = new Competence(competenceId);
        if (!competence1.exists()) {
            return Response.status(422).entity("competence does not exist in database").build();
        }
        user.createEdgeWith(Edge.InterestedIn, competence1);
        return Response.ok("interest link created").build();
    }

    @ApiOperation(value = "get lms user is enrolled in")
    @Path("/users/{userId}/systems")
    @GET
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public StringList getSystemsUserIsEnrolledIn(
            @PathParam("userId") String user, @QueryParam("password") String password) throws Exception {
        ArrayList<String> systems = new ArrayList<>();
        User userDao = new User(user);
        if (userDao.exists()) {
            systems.add(MagicStrings.DB);
        }
        if (checksIfUserExists(user, password)) {
            systems.add(LMSSystems.moodle.toString());
        }
        return new StringList(systems);
    }

    @ApiOperation(value = "get quick overview user about the user competences")
    @Path("/users/{userId}/overview")
    @GET
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public UserOverview getOverviewForUser(@PathParam("userId") String userId) throws Exception {
        CompetenceNeo4jQueryManagerImpl competenceNeo4jQueryManager = new CompetenceNeo4jQueryManagerImpl();
        return competenceNeo4jQueryManager.getUserOverview(userId);
    }

    @ApiOperation(value = "returns true if success, false if user does not exist in lms or complex requests do not " +
            "span 40 sec.")
    @Path("/users/sync/{lmssystem}")
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response syncUser(
            @PathParam("lmssystem") String lmsSystem, SyncDataSet dataSet) throws Exception {
        if (!lmsSystem.equals(LMSSystems.moodle.toString())) {
            return Response.serverError().entity("the lmssystem must be one of: " + LMSSystems.values()).build();
        }
        Boolean exists = checksIfUserExists(dataSet.getUserName(), dataSet.getPassword());
        if (!exists) {
           return Response.serverError().entity(false).build();
        }
        Boolean result = Moodle2Ont.sync(dataSet);
        return Response.ok(result).build();
    }
}
