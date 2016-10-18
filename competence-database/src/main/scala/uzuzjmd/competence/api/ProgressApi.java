package uzuzjmd.competence.api;

import uzuzjmd.competence.shared.progress.UserCompetenceProgress;
import uzuzjmd.competence.shared.progress.UserProgress;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by dehne on 05.07.2016.
 */
public interface ProgressApi {

    /**
     * Get the progress of the user with the ID of the user given
     * The UserProgress is presented as a the object referenced below:
     * @see uzuzjmd.competence.shared.progress.UserProgress
     *
     *
     * @param userId
     * @param courseId
     * @return
     * @throws Exception
     */
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/progress/{userId}")
    @GET
    UserProgress getUserProgress(@PathParam("userId") String userId, @QueryParam("courseId") String courseId) throws Exception;

    /**
     * Get the progress of the user given his ID and the ID of the competence the progress is calculated for
     * The UserProgress is presented as a the object referenced below:
     * @see uzuzjmd.competence.shared.progress.UserCompetenceProgress
     *
     * @param userId
     * @param competenceId
     * @return
     * @throws Exception
     */
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/progress/{userId}/competences/{competenceId}")
    @GET
    UserCompetenceProgress getUserCompetenceProgress(@PathParam("userId") String userId, @PathParam("competenceId") String competenceId) throws Exception;

    /**
     * Update the progress of the user with the userId given
     * The UserProgress is presented as a the object referenced below:
     * @see uzuzjmd.competence.shared.progress.UserProgress
     *
     * @param userId
     * @param userProgress
     * @return
     * @throws Exception
     */
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/progress/{userId}")
    @PUT
    Response updateOrCreateUserProgress(@PathParam("userId") String userId, UserProgress userProgress) throws Exception;

    /**
     * Update the progress of the user with the userId given
     * The UserProgress is presented as a the object referenced below:
     * @see uzuzjmd.competence.shared.progress.UserCompetenceProgress
     *
     * @param userId
     * @param competenceId
     * @param userProgress
     * @return
     * @throws Exception
     */
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/progress/{userId}/competences/{competenceId}")
    @PUT
    Response updateOrCreateUserProgress(@PathParam("userId") String userId, @PathParam("competenceId") String competenceId, UserCompetenceProgress userProgress) throws Exception;
}
