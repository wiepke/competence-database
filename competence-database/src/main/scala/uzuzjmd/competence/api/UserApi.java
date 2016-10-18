package uzuzjmd.competence.api;

import uzuzjmd.competence.shared.user.UserData;
import uzuzjmd.competence.shared.moodle.UserCourseListResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by dehne on 15.04.2016.
 */
public interface UserApi {



    /**
     * returns the full user with the id given.
     *
     * Be careful that users who have not been added via this interface might only be persisted by id (normally email),
     * because they are a reference to another identity store such as a cms or a lms system.
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @Path("/users/{userId}")
    @GET
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response getUser(@PathParam("userId") String userId) throws Exception;


    @Path("/users/{userId}")
    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response addUser(@PathParam("userId") String userId, UserData data) throws Exception;

    @Path("/users/{userId}")
    @DELETE
    Response deleteUser(@PathParam("userId") String userId) throws Exception;


    /**
     * get the courses for a certain user
     *
     * it is necessary to query the password as well as we cannot assume the lms allows this to access without credentials
     * @param userId
     * @param password
     * @return
     */
    @Path("/users/{userId}/courses")
    @GET
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    UserCourseListResponse getCoursesForUser(@PathParam("userId") String userId, String password);

    /**
     * checks if user exists
     *
     * it is necessary to query the password as well as we cannot assume the lms allows this to access without credentials
     * @param userId
     * @param password
     * @return
     */
    @Path("/users/{userId}/exists")
    @GET
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Boolean checksIfUserExists(@PathParam("userId") String userId, String password) throws Exception;
}
