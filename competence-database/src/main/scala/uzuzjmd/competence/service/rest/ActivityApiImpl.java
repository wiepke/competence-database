package uzuzjmd.competence.service.rest;

import datastructures.lists.StringList;
import datastructures.trees.ActivityEntry;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import uzuzjmd.competence.persistence.dao.Competence;
import uzuzjmd.competence.persistence.dao.EvidenceActivity;
import uzuzjmd.competence.persistence.ontology.Edge;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by dehne on 15.06.2016.
 */
//@Api(value = "/api1", description = "Activities")
@Path("/api1")
public class ActivityApiImpl implements uzuzjmd.competence.api.ActivityApi {

    @ApiOperation(value = "add activity to the ones stored in the db", notes = "can only store activities in" +
            " the db " +
            " and not the lms since the lms needs user credentials and no interface is implemented to add activities " +
            " to the lms indirectly. We assume that activities will appear in the lms automatically. If the " +
            " activities" +
            " in the lms are needed, the course api can provide these.")
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @PUT
    @Path("/activities")
    @Produces(MediaType.TEXT_PLAIN)
    public Response addActivity(ActivityEntry activity) throws Exception {
        EvidenceActivity activityDao = new EvidenceActivity(activity.getUrl(), activity.getName());
        activityDao.persist();
        return Response.ok("activity has been created with {id:'" + activityDao.getId() + "'}").build();
    }

    @ApiOperation(value = "link activity and competence", notes = "The activity will be suggested for the competence " +
            "if linked.")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @POST
    @Path("/activities/links/competences/{competenceId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response addActivity(
            @PathParam("competenceId") String competenceId,
            @ApiParam(value = "the id of the activity to link the competence to", required = true)
            ActivityEntry activityEntry) throws Exception {
        RecommenderApiImpl impl = new RecommenderApiImpl();
        impl.createSuggestedActivityForCompetence(competenceId, activityEntry.getUrl());
        return Response.ok("edge has been created").build();
    }
    @ApiOperation(value = "get all the linked competences", notes = "This is the inverse call to suggesting an " +
            "activity for a competence")
    @Override
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("/activities/links/competences")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public StringList getCompetencesForSuggestedActivity(
            @ApiParam(value = "the id of the activity queried", required = true) @QueryParam("activityId")
            String activityId) throws Exception {
        EvidenceActivity activity = new EvidenceActivity(activityId);
        if (activity.getAssociatedDaoIdsAsDomain(Edge.SuggestedActivityForCompetence) == null) {
            return new StringList();
        }
        List<String> result = activity.getAssociatedDaoIdsAsDomain(Edge.SuggestedActivityForCompetence);
        return new StringList(result);

    }

    @ApiOperation(value = "get the activity suggested for a competence")
    @Override
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("/activities/competences/{competenceId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ActivityEntry[] getActivitiesSuggestedForCompetence(
            @ApiParam(value = "the id of the competence the activities are suggested for", required = true)
            @PathParam("competenceId") String competenceId) throws Exception {
        Competence competence = new Competence(competenceId);
        if (competence.getAssociatedDaoIdsAsRange(Edge.SuggestedActivityForCompetence) == null) {
            return new ActivityEntry[0];
        }
        List<EvidenceActivity> result =
                competence.getAssociatedDaosAsRange(Edge.SuggestedActivityForCompetence, EvidenceActivity.class);
        int i = 0;
        ActivityEntry[] activityEntries = new ActivityEntry[result.size()];
        for (EvidenceActivity evidenceActivity : result) {
            activityEntries[i] = ((EvidenceActivity) evidenceActivity.getFullDao()).toActivityEntry();
            i++;
        }
        return activityEntries;
    }
}
