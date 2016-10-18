package uzuzjmd.competence.api;

import datastructures.lists.StringList;
import uzuzjmd.competence.shared.activity.CommentData;
import uzuzjmd.competence.shared.competence.CompetenceData;
import datastructures.trees.HierarchyChangeSet;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;


/**
 * Created by dehne on 15.04.2016.
 */
public interface CompetenceApi {

    /**
     * returns either a list of string or a tree representation depending on the value of "asTree"
     *
     * @param selectedCatchwords (as a filter for the competences that a queried)
     * @param selectedOperators (as a filter for the competences that a queried)
     * @param textFilter (as a filter for the competences that a queried)
     * @param rootCompetence (as a filter for the competences that a queried)
     * @param course (as a filter for the competences that a queried)
     * @param asTree
     * @return
     */
    Response getCompetences(@QueryParam(value = "selectedCatchwords") java.util.List<String> selectedCatchwords,
                            @QueryParam(value = "selectedOperators") java.util.List<String> selectedOperators,
                            @QueryParam("textFilter") String textFilter, @QueryParam("rootCompetence") String rootCompetence, @QueryParam("courseId") String course, @QueryParam("asTree") Boolean asTree, @QueryParam("userId") String userId, @QueryParam("learningTemplate") String learningTemplate);

    /**
     * Add a competence to the model with the id (the competence string) and competence meta data as payload.
     *
     * @param competenceId
     * @param data
     * @return
     */
    Response addCompetence(@PathParam("competenceId") String competenceId, CompetenceData data);


    Response deleteCompetence(@PathParam("competenceId") String competenceId) throws Exception;

    /**
     * Delete a competence with competenceId from the database using DELETE
     *
     * if the user is added, the competence will NOT be deleted but hidden for the given user
     * @param competenceId
     * @return
     * @throws Exception
     */
    Response deleteCompetence(@PathParam("competenceId") String competenceId,  String userId) throws Exception;

 /*   *//**
     * Delete a acompetence with competenceID from the database using POST
     * @param competenceId
     * @return
     * @throws Exception
     *//*
    Response deleteCompetenceLegacy(@PathParam("competenceId") String competenceId) throws Exception;

    *//**
     * add competence with competenceId to the database using POST
     * @param competenceId
     * @param data
     * @return
     *//*
    Response addCompetenceLegacy(@PathParam("competenceId") String competenceId, CompetenceData data);
*/
    /**
     * update the competence hierarchy with the changeSet
     * @param changes
     * @return
     */

    Response updateHierarchy(HierarchyChangeSet changes);


  /*  *//**
     * create a comment using POST
     * @param competenceId
     * @param data
     * @return
     *//*
    Response addComment(@PathParam("competenceId") String competenceId, CommentData data) throws Exception;
*/

    /**
     * get all the comment for the competence with the id given
     * @param competenceId
     * @param commentId
     * @return
     */
    Response getComment(@PathParam("competenceId") String competenceId, @PathParam("commentId") String commentId) throws Exception;


    /**
     * get all the comment for the competence with the id given
     * @param competenceId
     * @param commentId
     * @return
     */
    Response deleteComment(@PathParam("competenceId") String competenceId, @PathParam("commentId") String commentId);


    /**
     * get all the comment for the competence with the id given
     * @param competenceId
     * @return
     */
    CommentData[] getComments(@PathParam("competenceId") String competenceId);


    /**
     * get all the comment for the competence with the id given
     * @param competenceId
     * @return
     */
    Boolean verifyCompetence(@PathParam("competenceId") String competenceId);


    /**
     * get all the comment for the competence with the id given
     * @param competenceId
     * @return
     */
    StringList similarCompetences(@PathParam("competenceId") String competenceId, Boolean firstRun) throws Exception;
}
