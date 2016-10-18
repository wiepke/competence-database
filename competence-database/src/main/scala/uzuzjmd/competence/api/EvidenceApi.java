package uzuzjmd.competence.api;

import scala.collection.immutable.List;
import uzuzjmd.competence.shared.activity.CommentData;
import uzuzjmd.competence.shared.activity.EvidenceData;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * Created by dehne on 15.04.2016.
 */
public interface EvidenceApi {


    /**
     * Creates an evidence as a proof that competences have been acquired by the
     * user by certain activities
     *
     * @return
     */

    Response linkCompetencesToUser2(EvidenceData data) throws Exception;

    /**
     * Add a comment to an evidence link
     * <p/>
     * The comment is create to the evidence (not the competence)
     * <p/>
     * Have a look at @see linkCompetencesToUser in order to better
     * understand the model of a evidence link.
     *
     * @return
     */

    Response commentCompetence(@PathParam("evidenceId") String evidenceId, CommentData commentData);

    /**
     * Get all the comments for a  given evidence
     *
     * @param evidenceId
     * @return
     */

    ArrayList<CommentData> getComments(@PathParam("evidenceId") String evidenceId);


    /**
     * delete the comments to a given evidence
     *
     * @param evidenceId
     * @return
     */

    List<CommentData> deleteComments(@PathParam("evidenceId") String evidenceId);


    /**
     * Validate an evidence link.
     * <p/>
     * Have a look at for the nature of the
     * evidence link.
     * <p/>
     * This should only be done by teacher role (which should be checked in the
     * frontend)
     *
     * @param evidenceId
     * @return
     */

    Response validateLink(
            @PathParam("evidenceId") String evidenceId);

    /**
     * Validate an evidence link.
     * <p/>
     * Have a look at for the nature of the
     * evidence link.
     * <p/>
     * This should only be done by teacher role (which should be checked in the
     * frontend)
     *
     * @param evidenceId
     * @return
     */

    Response inValidateLink(
            @PathParam("evidenceId") String evidenceId);


    /**
     * get all the comment for the competence with the id given
     *
     * @param evidenceId
     * @param commentId
     * @return
     */

    CommentData getComment(@PathParam("evidenceId") String evidenceId, @PathParam("commentId") String commentId);


    /**
     * get all the comment for the competence with the id given
     *
     * @param evidenceId
     * @param commentId
     * @return
     */
    Response deleteComment(@PathParam("evidenceId") String evidenceId, @PathParam("commentId") String commentId);


}
