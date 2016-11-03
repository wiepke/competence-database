package uzuzjmd.competence.service.rest;

import datastructures.lists.StringList;
import datastructures.trees.HierarchyChangeSet;
import edu.stanford.nlp.trees.GrammaticalRelation;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import uzuzjmd.competence.comparison.verification.CompetenceVerifierFactory;
import uzuzjmd.competence.mapper.rest.SimilaritiesUpdater;
import uzuzjmd.competence.mapper.rest.read.Ont2CompetenceTree;
import uzuzjmd.competence.mapper.rest.read.Ont2Competences;
import uzuzjmd.competence.mapper.rest.read.Ont2ReflectiveQuestionAnswer;
import uzuzjmd.competence.mapper.rest.write.Competence2Ont;
import uzuzjmd.competence.mapper.rest.write.HierarchieChangesToOnt;
import uzuzjmd.competence.mapper.rest.write.ReflectiveQuestionAnswer2Ont;
import uzuzjmd.competence.persistence.dao.Comment;
import uzuzjmd.competence.persistence.dao.Competence;
import uzuzjmd.competence.persistence.dao.ReflectiveQuestion;
import uzuzjmd.competence.persistence.dao.User;
import uzuzjmd.competence.persistence.ontology.Contexts;
import uzuzjmd.competence.persistence.ontology.Edge;
import uzuzjmd.competence.shared.activity.CommentData;
import uzuzjmd.competence.shared.assessment.ReflectiveQuestionAnswerData;
import uzuzjmd.competence.shared.assessment.ReflectiveQuestionAnswerHolder;
import uzuzjmd.competence.shared.assessment.ReflectiveQuestionData;
import uzuzjmd.competence.shared.competence.CompetenceData;
import uzuzjmd.competence.shared.competence.CompetenceFilterData;
import uzuzjmd.competence.shared.competence.CompetenceXMLTree;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dehne on 11.04.2016.
 */

@Path("/api1")
public class CompetenceApiImpl implements uzuzjmd.competence.api.CompetenceApi {


    /**
     * returns either a list of string or a tree representation depending on the value of "asTree"
     * <p/>
     * courseId  null should be at least 'university' as default"
     *
     * @param selectedCatchwords
     * @param selectedOperators
     * @param textFilter
     * @param rootCompetence
     * @param course
     * @param asTree
     * @return
     */
    @ApiOperation(value = "get competences based on filter", notes = "Get all the competence descriptions stored in the database with filters specified")
    @Path("/competences")
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getCompetences(
            @QueryParam(value = "selectedCatchwords") java.util.List<String> selectedCatchwords,
            @ApiParam(value = "the verbs",
                    required = false) @QueryParam(value = "selectedOperators") java.util.List<String> selectedOperators,
            @QueryParam("textFilter") String textFilter, @ApiParam(value = "the plain text string of the topcompetence",
            required = false) @QueryParam("rootCompetence") String rootCompetence,
            @QueryParam("courseId") String course, @QueryParam("asTree") Boolean asTree,
            @QueryParam("userId") String userId, @QueryParam("learningTemplate") String learningTemplate) {

        if (course == null) {
            course = Contexts.university.toString();
        }

        CompetenceFilterData data =
                new CompetenceFilterData(selectedCatchwords, selectedOperators, course, null, textFilter, userId,
                        learningTemplate, asTree, rootCompetence);
        if (data != null && data.getResultAsTree() != null && data.getResultAsTree()) {
            java.util.List<CompetenceXMLTree> result = Ont2CompetenceTree.getCompetenceTree(data);
            return Response.status(200).entity(result).build();
        } else {
            java.util.HashSet<String> result = Ont2Competences.convert(data);
            return Response.status(200).entity(result).build();
        }
    }

    @ApiOperation(value = "add a competence to db")
    @Override
    @Path("/competences/{competenceId}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addCompetence(
            @ApiParam(value = "the plain text string of the competence",
                    required = true) @PathParam("competenceId") String competenceId, CompetenceData data) {
        data.setForCompetence(competenceId);
        if (data.getSubCompetences() == null) {
            data.setSubCompetences(new ArrayList<String>());
        }

        if (data.getSuperCompetences() == null) {
            data.setSuperCompetences(new ArrayList<String>());
        }

        String resultMessage = Competence2Ont.convert(data);
        return Response.ok(resultMessage).build();
    }


    @ApiOperation(value = "delete a db")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Override
    @Path("/competences/{competenceId}")
    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteCompetence(
            @ApiParam(value = "the plain text string of the competence",
                    required = true) @PathParam("competenceId") String competenceId) throws Exception {
        Competence competence = new Competence(competenceId);
        competence.delete();
        return Response.ok("competence deleted").build();
    }


    @ApiOperation(value = "hide competence for user")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Override
    @Path("/competences/{competenceId}/users/{userId}")
    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteCompetence(
            @ApiParam(value = "the plain text string of the competence to be hidden for the user",
                    required = true) @PathParam("competenceId") String competenceId, @PathParam("userId") String userId)
            throws Exception {
        Competence competence = new Competence(competenceId);
        if (userId != null) {
            User user = new User(userId);
            if (user.exists()) {
                competence.hideFor(user);
                return Response.ok("competence hidden").build();
            } else {
                throw new WebApplicationException("user does not exist");
            }
        } else {
            throw new WebApplicationException("userId is null");
        }
    }

    /**
     * updates the competence hierarchy
     *
     * @param changes of type HierarchieChangeObject @see updateHierarchie2
     * @return
     */
    @ApiOperation(value = "change the superclass for a node selected.", notes =  "If the old class superclass is null" +
            " a new super" +
            "class will be added. This should not be used to add new competences to the database.")
    @Override
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/competences/hierarchy/update")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateHierarchy(HierarchyChangeSet changes) {
        try {
            HierarchieChangesToOnt.convert(changes);
        } catch (NullPointerException e) {
            return Response.serverError().build();
        }
        return Response.ok("updated taxonomy").build();
    }

  /*  @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/competences/{competenceId}/comments")
    @Produces(MediaType.TEXT_PLAIN)
    @Override
    public Response addComment(
            @ApiParam(value = "the plain text string of the competence", required = true) @PathParam("competenceId")
            String competenceId, CommentData data) throws Exception {
        Competence competence = new Competence(competenceId);
        String context = "university";
        if (data.getCourseContext() != null && !data.getCourseContext().isEmpty()) {
            context = data.getCourseContext();
        }
        CourseContext courseContext = new CourseContext(context);
        courseContext.persist();
        if (data.getText() == null || data.getText().isEmpty()) {
            throw new WebApplicationException(new Exception("text is not valid"));
        }
        User user = new User(data.getUserId());
        if (!user.exists()) {
            throw new WebApplicationException(new Exception("user does not exist in database"));
        }
        if (competence.exists()) {
            Comment comment = new Comment(data.getText(), user, System.currentTimeMillis());
            comment.persist();
            comment.createEdgeWith(Edge.CommentOfCourse, courseContext);
            comment.createEdgeWith(Edge.CommentOfCompetence, competence);
        } else {
            throw new WebApplicationException(new Exception("competence does not exist in database"));
        }
        return null;
    }*/

    @Override
    public Response getComment(String competenceId, String commentId) throws Exception {
        Competence competence = new Competence(competenceId);
        if (!competence.exists()) {
            throw new WebApplicationException(new Exception("competence does not exist in database"));
        }
        if (commentId.isEmpty()) {
            java.util.List<Comment> commentDatas =
                    competence.getAssociatedDaosAsRange(Edge.CommentOfCompetence, Comment.class);
            ArrayList<CommentData> results = new ArrayList<>();
            for (Comment comment : commentDatas) {
                CommentData data = comment.getData();
                results.add(data);
            }
            return Response.status(200).entity(commentDatas).build();
        } else {
            Comment comment = new Comment(commentId);
            if (!comment.exists()) {
                throw new WebApplicationException(new Exception("comment does not exist in database"));
            }
            CommentData commentData =
                    new CommentData(null, comment.getText(), comment.getDateCreated());
            return Response.status(200).entity(commentData).build();
        }
    }

    @Override
    public Response deleteComment(String competenceId, String commentId) {
        Comment comment = new Comment(commentId);
        try {
            comment.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.ok("competence deleted").build();
    }

    @Override
    public CommentData[] getComments(String competenceId) {
        // TODO implement
        throw new WebApplicationException("not implemented");
    }

    @Override
    public Boolean verifyCompetence(String competenceId) {
        return CompetenceVerifierFactory.getSimpleCompetenceVerifier(competenceId, GrammaticalRelation.Language.Any)
                .isCompetence2();
    }


    @ApiOperation(value = "get similar competences (to avoid redundancy)")
    @GET
    @Path("/competences/semblances/{competenceId}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Override
    public StringList similarCompetences(
            @ApiParam(value = "the plain text string of the competence",
                    required = true) @PathParam("competenceId") String competenceId,
            @ApiParam(value = "if this is the first query on semblances it should be called sequentially",
                    required = true) @QueryParam("firstRun") Boolean firstRun) throws Exception {
        final Competence competence = new Competence(competenceId);

        if (!firstRun) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    SimilaritiesUpdater.updateSimilarCompetencies(competence);
                }
            });
            t.start();
        } else {
            SimilaritiesUpdater.updateSimilarCompetencies(competence);
        }
        return new StringList(competence.getSimilarCompetences());
    }


    @ApiOperation(value = "persist a reflective question to the competence")
    @POST
    @Path("/competences/questions")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    public Response addReflectiveQuestionToCompetence(ReflectiveQuestionData data) throws Exception {
        try {
            ReflectiveQuestion reflectiveQuestion = new ReflectiveQuestion(new Competence(data.getCompetenceId()), data.getQuestion());
            reflectiveQuestion.persist();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
        return Response.ok("Reflective question persisted").build();
    }

    @ApiOperation(value = "get reflective questions stored for this competence")
    @GET
    @Path("competences/{competenceId}/questions")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ReflectiveQuestionData[] getReflectiveQuestionsForCompetence(@PathParam("competenceId") String competenceId) {
        Competence competence = new Competence(competenceId);
        try {
            List<ReflectiveQuestionData> datas = new ArrayList<>();
            List<ReflectiveQuestion> result = competence.getAssociatedDaosAsRange(Edge.ReflectiveQuestionForCompetence,
                    ReflectiveQuestion.class);
            for (ReflectiveQuestion reflectiveQuestion : result) {
                datas.add(new ReflectiveQuestionData(((ReflectiveQuestion)reflectiveQuestion.getFullDao()).question,
                        competenceId, reflectiveQuestion.getId()));
            }
            return datas.toArray(new ReflectiveQuestionData[0]);
        } catch (Exception e) {
            Response.serverError().entity(e.getMessage()).build();
        }
        return new ReflectiveQuestionData[0];
    }

    @ApiOperation(value = "get answers to this reflective question")
    @GET
    @Path("/competences/questions/{questionId}/answers/users/{userId}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ReflectiveQuestionAnswerHolder getReflectiveQuestionsAnswersForCompetence(
                                                                 @PathParam("questionId") String questionId,
                                                                 @PathParam("userId") String userId)
            throws Exception {
        return Ont2ReflectiveQuestionAnswer.convert(questionId, userId);
    }

    @ApiOperation(value = "add answers to this reflective question")
    @PUT
    @Path("/competences/questions/answers")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    public Response addReflectiveQuestionsAnswersForCompetence(ReflectiveQuestionAnswerData data) throws Exception {
        String userId = data.getUserId();
        if (!new User(userId).exists()) {
            return Response.serverError().entity("User does not exist in database, plz create it first!").build();
        }
        ReflectiveQuestionAnswer2Ont.convert(data);
        return Response.ok().entity("answers stored").build();
    }
}
