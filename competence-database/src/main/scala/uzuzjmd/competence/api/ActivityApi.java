package uzuzjmd.competence.api;

import datastructures.lists.StringList;
import datastructures.trees.ActivityEntry;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by dehne on 05.07.2016.
 */
public interface ActivityApi {
    /**
     * Create an abstract activity in the competence database
     *
     * @param activity
     * @return
     * @throws Exception
     */
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @PUT
    @Path("/activities")
    @Produces(MediaType.TEXT_PLAIN)
    Response addActivity(ActivityEntry activity) throws Exception;

    /**
     * Link activity to the competence it is suggested for
     *
     * @param competenceId
     * @param activityUrl
     * @return
     * @throws Exception
     */
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @POST
    @Path("/activities/links/competences/{competenceId}")
    @Produces(MediaType.TEXT_PLAIN)
    Response addActivity(@PathParam("competenceId") String competenceId, ActivityEntry activityUrl) throws Exception;

    /**
     * Query competences that are linked to this activity
     *
     * @param activityId
     * @return
     * @throws Exception
     */
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("/activities/links/competences")
    @Produces(MediaType.APPLICATION_JSON)
    StringList getCompetencesForSuggestedActivity(@QueryParam("activityId") String activityId) throws Exception;

    /**
     * Query competences that are linked to this activity
     *
     * @return
     * @throws Exception
     */
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("/activities")
    @Produces(MediaType.APPLICATION_JSON)
    ActivityEntry[] getActivitiesSuggestedForCompetence(@QueryParam("competenceId") String competenceId) throws Exception;
}
