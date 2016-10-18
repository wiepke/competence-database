package uzuzjmd.competence.service.rest;


import io.swagger.annotations.ApiOperation;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import scala.collection.immutable.List;
import uzuzjmd.competence.mapper.rest.write.Comment2Ont;
import uzuzjmd.competence.mapper.rest.write.Evidence2Ont;
import uzuzjmd.competence.mapper.rest.write.HandleLinkValidationInOnt;
import uzuzjmd.competence.persistence.dao.*;
import uzuzjmd.competence.persistence.dao.CourseContext;
import uzuzjmd.competence.persistence.dao.EvidenceActivity;
import uzuzjmd.competence.shared.activity.CommentData;
import uzuzjmd.competence.shared.activity.EvidenceData;
import uzuzjmd.competence.shared.activity.LinkValidationData;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Created by dehne on 11.04.2016.
 */
@Path("/api1")
public class EvidenceApiImpl implements uzuzjmd.competence.api.EvidenceApi {



    static private final Logger logger = LogManager.getLogger(EvidenceApiImpl.class.getName());

    @ApiOperation(value = "add an evidence for a competence and a user", notes = "the course and the user referenced " +
            "must exist in database beforehand")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @PUT
    @Path("/evidences/create")
    @Produces(MediaType.TEXT_PLAIN)
    public Response linkCompetencesToUser2(EvidenceData data) throws Exception {
        if (data.getEvidence() == null) {
            String message = "evidence is null";
            logger.warn(message);
            return Response.status(422).entity(message).build();
        }
        CourseContext courseDao = new CourseContext(data.getCourseId());
        if (data.getCourseId() == null || !(courseDao.exists())) {

            String message = "course does not exist in db "+ data.getCourseId();
            logger.warn(message);
            return Response.status(422).entity(message).build();
        }
        User user = new User(data.getCreator());
        if (!user.exists()) {
            String message = "user does not exist in db "+ data.getCreator();
            logger.warn(message);
            return Response.status(422).entity(message).build();
        }

        User user2 = new User(data.getEvidence().getUserId());
        if (!user2.exists()) {
            String message = "user does not exist in db "+ data.getEvidence().getUserId();
            logger.warn(message);
            return Response.status(422).entity(message).build();
        }
        return createEvidenceLink(data);
    }

    private Response createEvidenceLink( EvidenceData data) {

        Evidence2Ont.writeLinkToDatabase(data);
        return Response.ok(
                "competences linked to evidences \n activity persisted with id: " + data.getEvidence().getUrl())
                .build();
    }

    @ApiOperation(value = "comment an competence attribution", notes = "The evidence attributing the competence " +
            "acquisition to the activity is commented")
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    @POST
    @Path("/evidences/{evidenceId}/comments")
    public Response commentCompetence(@PathParam("evidenceId") String evidenceId, CommentData commentData) {
        commentData.setLinkId(evidenceId);
        Comment2Ont.convert(commentData);
        return Response.ok("link commented").build();
    }

    @Override
    public ArrayList<CommentData> getComments(String evidenceId) {
        EvidenceActivity evidence = new EvidenceActivity(evidenceId);
        try {
            return evidence.getComments();
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new WebApplicationException("error occurred");
    }

    @Override
    public List<CommentData> deleteComments(String evidenceId) {
        EvidenceActivity evidence = new EvidenceActivity(evidenceId);
        try {
            ArrayList<CommentData> comments = evidence.getComments();
            for (CommentData commentData : comments) {
                Comment comment = new Comment(commentData.getCommentId());
                comment.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @ApiOperation(value = "validate the evidence")
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    @POST
    @Path("/evidences/{evidenceId}/validate")
    public Response validateLink(
            @PathParam("evidenceId") String evidenceId) {
        Boolean isValid = true;
        return handleLinkValidation(evidenceId, isValid);
    }

    @ApiOperation(value = "invalidate the evidence")
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    @POST
    @Path("/evidences/{evidenceId}/invalidate")
    public Response inValidateLink(
            @PathParam("evidenceId") String evidenceId) {
        Boolean isValid = false;
        return handleLinkValidation(evidenceId, isValid);
    }

    @Override
    public CommentData getComment(String evidenceId, String commentId) {
        ArrayList<CommentData> comments = getComments(evidenceId);
        for (CommentData comment : comments) {
            if (comment.getCommentId().equals(commentId)) {
                return comment;
            }
        }
        return null;
    }

    @Override
    public Response deleteComment(String evidenceId, String commentId) {
        Comment comment = new Comment(commentId);
        try {
            comment.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new WebApplicationException("error occurred");
    }

    private Response handleLinkValidation(String linkId,
                                          Boolean isValid) {
        HandleLinkValidationInOnt
                .convert(new LinkValidationData(linkId,
                        isValid));
        return Response.ok("link updated").build();
    }
}
