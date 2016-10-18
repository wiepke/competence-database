package uzuzjmd.competence.service.rest;

import datastructures.graph.Graph;
import datastructures.graph.GraphFilterData;
import datastructures.trees.HierarchyChangeSet;
import uzuzjmd.competence.main.EposImporter;
import uzuzjmd.competence.mapper.rest.read.*;
import uzuzjmd.competence.mapper.rest.write.*;
import uzuzjmd.competence.persistence.dao.*;
import uzuzjmd.competence.persistence.ontology.Edge;
import uzuzjmd.competence.shared.*;
import uzuzjmd.competence.shared.activity.CommentData;
import uzuzjmd.competence.shared.activity.EvidenceData;
import uzuzjmd.competence.shared.activity.LinkValidationData;
import uzuzjmd.competence.shared.assessment.ReflectiveAssessmentChangeData;
import uzuzjmd.competence.shared.assessment.ReflectiveAssessmentsListHolder;
import datastructures.lists.StringList;
import uzuzjmd.competence.shared.competence.*;
import uzuzjmd.competence.shared.learningtemplate.LearningTemplateData;
import uzuzjmd.competence.shared.learningtemplate.LearningTemplateResultSet;
import uzuzjmd.competence.shared.learningtemplate.SuggestedCompetenceGrid;
import uzuzjmd.competence.shared.course.CourseData;
import uzuzjmd.competence.shared.epos.EPOSTypeWrapper;
import uzuzjmd.competence.shared.progress.ProgressMap;
import uzuzjmd.competence.shared.user.UserData;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;


/**
 * Root resource (exposed at "competences" path)
 */
@Path("/competences")
public class CompetenceServiceRestJSON {

    public CompetenceServiceRestJSON() {
        DBInitializer.init();
    }

    /**
     * use /updateHierarchie2 instead
     *
     * @param changes
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/updateHierarchie")
    @Deprecated
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateHierarchie(
            @QueryParam("changes") List<String> changes) {

        HierarchyChangeSet changeSet = new HierarchyChangeSet()
                .convertListToModel(changes);
        HierarchieChangesToOnt.convert(changeSet);
        return Response.ok("updated taxonomy").build();
    }


    /**
     * Get the GUI Competence TREE
     *
     * @param course             the (courseId) context of the competences ("university") for no
     *                           filter
     * @param selectedCatchwords
     * @param selectedOperators
     * @return
     */
    @Deprecated
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @GET
    @Path("/competencetree/{context}")
    public List<CompetenceXMLTree> getCompetenceTree(
            @PathParam("context") String course,
            @QueryParam(value = "selectedCatchwords") List<String> selectedCatchwords,
            @QueryParam(value = "selectedOperators") List<String> selectedOperators,
            @QueryParam("textFilter") String textFilter, @QueryParam("rootCompetence") String rootCompetence, @QueryParam("learningTemplate") String learningTemplate) {

        CompetenceFilterData data = new CompetenceFilterData(selectedCatchwords, selectedOperators, course, null, textFilter, null, learningTemplate, true, rootCompetence);
        List<CompetenceXMLTree> result = Ont2CompetenceTree.getCompetenceTree(data);
        return result;
    }


    /**
     * updates the competence hierarchy
     *
     * @param changes of type HierarchieChangeObject @see updateHierarchie2
     * @return
     */
    @Deprecated
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/updateHierarchie2")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateHierarchie2(
            @QueryParam("changes") HierarchyChangeSet changes) {
        HierarchieChangesToOnt.convert(changes);
        return Response.ok("updated taxonomy").build();
    }

    /**
     * This is an example for the format needed for updating the hierarchie
     * <p/>
     * DON'T use this as a productive interface
     *
     * @param changes
     * @return
     */
    @Deprecated
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/updateHierarchie2/example")
    @Produces(MediaType.APPLICATION_JSON)
    public HierarchyChangeSet updateHierarchieExample(
            @QueryParam("changes") HierarchyChangeSet changes) {
        return changes;
    }

    /**
     * @param user should be email-address or other unique identifier
     * @param role can be "student or teacher"
     * @return
     */
    @Deprecated
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @Path("/user/create/{user}/{role}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(
            @PathParam("user") String user,
            @PathParam("role") String role,
            @QueryParam("groupId") String courseContext) throws Exception {
        if (role == null) {
            throw new WebApplicationException("Role not given");
        }
        if (user == null) {
            throw new WebApplicationException("userId not given");
        }
        CourseContext courseContextDao = new CourseContext(courseContext);
        courseContextDao.persist();
        UserData data = new UserData(user, courseContext,
                role, null, null);
        User2Ont.convert(data);
        return Response.ok("user created").build();
    }

    /**
     * Deletes the courseId context.
     * <p/>
     * All competences linked to this context will be removed from it. This
     * should be used as a companion with coursecontext/create
     *
     * @param course
     * @return
     */
    @Deprecated
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/coursecontext/delete/{course}")
    public Response deleteCourseContext(
            @PathParam("course") String course) throws Exception {
        CourseContext courseContext = new CourseContext(course);
        courseContext.delete();
        return Response
                .ok("competences deleted from courseId:"
                        + course).build();
    }

    /**
     * Get the description of requirements for the courseId.
     * <p/>
     * The requirement string specifying why this subset of competences was
     * selected for the courseId is returned.
     *
     * @param course the context of the competences
     * @return the requirement string
     */
    @Deprecated
    @Produces(MediaType.TEXT_PLAIN)
    @GET
    @Path("/coursecontext/requirements/{course}")
    public String getRequirements(
            @PathParam("course") String course) throws Exception {
        CourseContext context = new CourseContext(course);
        return context.getFullDao().getRequirement();
    }




    /**
     * Add a comment to an evidence link
     * <p/>
     * Have a look at @see linkCompetencesToUser in order to better
     * understand the model of a evidence link.
     *
     * @param linkId        the id of the link
     * @param user          the user who creates the comment
     * @param text          the text of the comment
     * @param courseContext the courseId context the comment is created in
     * @param role          the role of the user
     * @return
     */
    @Deprecated
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Path("/link/comment/{linkId}/{user}/{courseContext}/{role}")
    public Response commentCompetence(
            @PathParam("linkId") String linkId,
            @PathParam("user") String user,
            @QueryParam("text") String text,
            @PathParam("courseContext") String courseContext,
            @PathParam("role") String role) {
        UserData userData = new UserData(user,
                courseContext, role, null, null);
        User2Ont.convert(userData);
        CommentData commentData = new CommentData(System.currentTimeMillis(), linkId,
                user, text, courseContext, role);
        Comment2Ont.convert(commentData);
        return Response.ok("link commented").build();
    }

    /**
     * Validate an evidence link.
     * <p/>
     * Have a look at for the nature of the
     * evidence link.
     * <p/>
     * This should only be done by teacher role (which should be checked in the
     * frontend)
     *
     * @param linkId
     * @return
     */
    @Deprecated
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @Path("/link/validate/{linkId}")
    public Response validateLink(
            @PathParam("linkId") String linkId) {
        Boolean isValid = true;
        return handleLinkValidation(linkId, isValid);
    }

    /**
     * Invalidate a link
     * <p/>
     * (this should only be done by teacher role (which should be checked in the
     * frontend)
     *
     * @param linkId
     * @return
     */
    @Deprecated
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @Path("/link/invalidate/{linkId}")
    public Response invalidateLink(
            @PathParam("linkId") String linkId) {
        Boolean isValid = false;
        return handleLinkValidation(linkId, isValid);
    }

    /**
     * Delete an evidence link
     *
     * @param linkId the id of the link to be deleted
     * @return
     */
    @Deprecated
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @Path("/link/delete/{linkId}")
    public Response deleteLink(
            @PathParam("linkId") String linkId) {
        AbstractEvidenceLink2Ont.convert(linkId);
        return Response.ok("link deleted").build();
    }


    /**
     * Deletes one or more competences
     *
     * @param competences
     * @return
     */
    @Deprecated
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @Path("/competence/delete")
    public Response deleteCompetence(
            @QueryParam("competences") List<String> competences) {

        DeleteCompetenceInOnt.convert(competences);
        return Response.ok("competences deleted").build();
    }

    /**
     * Deletes competences and all their subcompetences
     *
     * @param competences the competences to be deleted
     * @return
     */
    @Deprecated
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @Path("/competence/deleteTree")
    public Response deleteCompetenceTree(
            @QueryParam("competences") List<String> competences) {
        DeleteCompetenceTreeInOnt.convert(competences);
        return Response.ok("competences deleted").build();
    }

    /**
     * returns a map competence->evidences
     * <p/>
     * Returns all the evidences for a user in a form that they can be presented
     *
     * @param user the user who has acquired the competences
     * @return
     */
    @Deprecated
    @GET
    @Path("/link/overview/{user}")
    @Produces(MediaType.APPLICATION_JSON)
    public CompetenceLinksMap getCompetenceLinksMap(
            @PathParam("user") String user) {
        return Ont2CompetenceLinkMap.convert(user);
    }

    /**
     * Shows overview of the progress a user has made in a courseId
     *
     * @param course              the courseId the overview is generated for
     * @param selectedCompetences a filter: the percentate of acquired competences is calculated
     *                            taking into account the competences visible to the user
     * @return
     */
    @Deprecated
    @GET
    @Path("/link/progress/{course}")
    @Produces(MediaType.APPLICATION_JSON)
    public ProgressMap getProgressM(
            @PathParam("course") String course,
            @QueryParam("competences") List<String> selectedCompetences) {
        return GetProgressMinOnt.convert(new CourseData(
                course, selectedCompetences));
    }

    /**
     * This creates a "prerequisite" relation between the
     * selectedCompetences->linkedCompetences
     *
     * @param course              the courseId context the link is created in (may be "university"
     *                            for global context")
     * @param linkedCompetence    the pre competences
     * @param selectedCompetences the post competences
     */
    @Deprecated
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @Path("/prerequisite/create/{course}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPrerequisite(
            @PathParam("course") String course,
            @QueryParam("linkedCompetence") String linkedCompetence,
            @QueryParam("selectedCompetences") List<String> selectedCompetences) {
        CreatePrerequisiteInOnt
                .convert(new PrerequisiteData(course,
                        linkedCompetence,
                        selectedCompetences));

        return Response.ok("prerequisite created").build();
    }

    /**
     * Deletes the "prerequisite" link between the competences
     *
     * @param course              the courseId context the link is created in (may be "university"
     *                            for global context")
     * @param linkedCompetence    the pre competences
     * @param selectedCompetences the post competences
     * @return
     */
    @Deprecated
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @Path("/prerequisite/delete/{course}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePrerequisite(
            @PathParam("course") String course,
            @QueryParam("linkedCompetence") String linkedCompetence,
            @QueryParam("competences") List<String> selectedCompetences) {
        DeletePrerequisiteInOnt
                .convert(new PrerequisiteData(course,
                        linkedCompetence,
                        selectedCompetences));
        return Response.ok("prerequisite deleted").build();
    }

    /**
     * returns the whole prerequisite graph (given filter)
     *
     * @param selectedCompetences the competences that selected that filter the graph
     * @param course
     * @return
     */
    @Deprecated
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("/prerequisite/graph/{course}")
    @Produces(MediaType.APPLICATION_JSON)
    public Graph getPrerequisiteGraph(
            @QueryParam("selectedCompetences") List<String> selectedCompetences,
            @PathParam("course") String course) {
        GraphFilterData graphFilterData = null;
        if (selectedCompetences != null) {
            String[] selectedCompetencesArray = selectedCompetences.toArray(new String[0]);
            graphFilterData = new GraphFilterData(course, selectedCompetencesArray);
        } else {
            graphFilterData = new GraphFilterData(course, new String[0]);
        }
        Graph result = Ont2CompetenceGraph.convert(graphFilterData);
        return result;
    }

    /**
     * gets the prerequisites for the given competence
     *
     * @param forCompetence
     * @return
     */
    @Deprecated
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("/prerequisite/required/{course}")
    @Produces(MediaType.APPLICATION_JSON)
    public String[] getRequiredCompetences(
            @QueryParam("competence") String forCompetence) {
        return GetRequiredCompetencesInOnt
                .convert(forCompetence);
    }

    /**
     * GET the operator for a given competence
     *
     * @param forCompetence
     * @return
     */
    @Deprecated
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("/operator")
    public String getOperatorForCompetence(
            @QueryParam("competence") String forCompetence) {
        // Ont2Operator.
        return Ont2Operator.convert(forCompetence);
    }

    /**
     * Get all the catchwords for a given competence
     *
     * @param forCompetence
     * @return
     */
    @Deprecated
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("/catchwords")
    public String getCatchwordsForCompetence(
            @QueryParam("competence") String forCompetence) {
        return Ont2Catchwords.convert(forCompetence);
    }

    /**
     * add a competence to the model
     *
     * @param forCompetence        the name of the competences as a String (necessary)
     * @param operator             the verb of the competence (necessary)
     * @param catchwords           (at least one)
     * @param superCompetences     (optional)
     * @param subCompetences       (optional)
     * @param learningTemplateName (optional) the name of the learningTemplate it is associated
     *                             with
     * @return
     */
    @Deprecated
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @Path("/addOne")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCompetenceToModel(
            @QueryParam("competence") String forCompetence,
            @QueryParam("operator") String operator,
            @QueryParam("catchwords") List<String> catchwords,
            @QueryParam("superCompetences") List<String> superCompetences,
            @QueryParam("subCompetences") List<String> subCompetences,
            @QueryParam("learningTemplateName") String learningTemplateName) {
        CompetenceData competenceData = new CompetenceData(
                operator, catchwords, superCompetences,
                subCompetences, learningTemplateName,
                forCompetence);
        String resultMessage = Competence2Ont
                .convert(competenceData);
        return Response.ok(resultMessage).build();
    }

    /**
     * The semantic of this interface is that a courseId is linked to a competence as template.
     * This way the learner can navigate to the courseId if he/she wants to learn a specific set of competencies
     * @param competence
     * @param course
     * @return
     * @throws Exception
     */
    @Deprecated
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @Path("/SuggestedCourseForCompetence/create")
    public Response createSuggestedCourseForCompetence(@QueryParam("competence") String competence, @QueryParam("course") String course) throws Exception {
        //SuggestedCourseForCompetence2Ont.write(courseId, competence);
        Competence competenceDAO = new Competence(competence);
        competenceDAO.persist();
        competenceDAO.addCourseContext((CourseContext) new CourseContext(course).persist());
        return Response.ok("edge created").build();
    }


    /**
     * Get competences linked to (courseId) context. (the moodle courseId Id)
     * <p/>
     * Returns all the competences linked to a courseId context.
     *
     * @param courseId
     * @return
     */
    @Deprecated
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/SuggestedCompetencesForCourse/{courseId}")
    public String[] getSuggestedCompetencesForCourse(
            @PathParam("courseId") String courseId) throws Exception {
        CourseContext context = new CourseContext(courseId);

        if (context.getAssociatedDaoIdsAsDomain(Edge.CourseContextOfCompetence) == null) {
            return new String[0];
        }
        List<String> result = context.getAssociatedDaoIdsAsDomain(Edge.CourseContextOfCompetence);
        result.remove("Kompetenz");
        return result.toArray(new String[0]);
    }


    /**
     * Get competences linked to (courseId) context.
     * <p/>
     * Returns all the courses linked to a certain competence as a suggestions
     *
     * @param competence
     * @return
     */
    @Deprecated
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    @Path("/SuggestedCourseForCompetence")
    public String[] getSuggestedCoursesForCompetence(
            @QueryParam("competence") String competence) throws Exception {
        Competence context = new Competence(competence);

        if (context.getAssociatedDaoIdsAsRange(Edge.CourseContextOfCompetence) == null) {
            return new String[0];
        }
        List<String> result = context.getAssociatedDaoIdsAsRange(Edge.CourseContextOfCompetence);
        return result.toArray(new String[0]);
    }


    /**
     * The semantic of this interface is that an activity is linked to a competence as template.
     * This way the learner can navigate to the navigate if he/she wants to learn a specific set of competencies
     * @param competence
     * @param activityUrl
     * @return
     * @throws Exception
     */
    @Deprecated
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @Path("/SuggestedActivityForCompetence/create")
    public Response createSuggestedActivityForCompetence(@QueryParam("competence") String competence, @QueryParam("activityUrl") String activityUrl) throws Exception {
        EvidenceActivity activity = new EvidenceActivity(activityUrl);
        activity.persist();
        Competence competence1 = new Competence(competence);
        competence1.persist();
        SuggestedActivityForCompetence2Ont.write(activityUrl, competence);
        return Response.ok("edge created").build();
    }

    /**
     * This returns a list of competencies linked to certain activity
     * @param activityUrl
     * @return
     * @throws Exception
     */
    @Deprecated
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("/CompetencesForSuggestedActivity/get")
    @Produces(MediaType.APPLICATION_JSON)
    public String[] getCompetencesForSuggestedActivity(@QueryParam("activityUrl") String activityUrl) throws Exception {

        EvidenceActivity activity = new EvidenceActivity(activityUrl);

        if (activity.getAssociatedDaoIdsAsDomain(Edge.SuggestedActivityForCompetence) == null) {
            return new String[0];
        }
        List<String> result = activity.getAssociatedDaoIdsAsDomain(Edge.SuggestedActivityForCompetence);
        return result.toArray(new String[0]);

    }



    /**
     * This returns a list of competencies linked to certain activity
     * @param competence
     * @return
     * @throws Exception
     */
    @Deprecated
    @Consumes(MediaType.APPLICATION_JSON)
    @GET
    @Path("/SuggestedActivityForCompetence/get")
    @Produces(MediaType.APPLICATION_JSON)
    public String[] getSuggestedActivityForCompetence(@QueryParam("competence") String competence) throws Exception {

        Competence competence1 = new Competence(competence);

        if (competence1.getAssociatedDaoIdsAsRange(Edge.SuggestedActivityForCompetence) == null) {
            return new String[0];
        }
        List<String> result = competence1.getAssociatedDaoIdsAsRange(Edge.SuggestedActivityForCompetence);
        return result.toArray(new String[0]);

    }

    /**
     * Deletes the link between the courseId and the given competence
     * @param competence
     * @param course
     * @return
     */
    @Deprecated
    @Consumes(MediaType.APPLICATION_JSON)
    @POST
    @Path("/SuggestedCourseForCompetence/delete")
    public Response deleteSuggestedCourseForCompetence(@QueryParam("competence") String competence, @QueryParam("course") String course) {
        SuggestedCourseForCompetence2Ont.delete(course, competence);
        return Response.ok("edge deleted").build();
    }

    /**
     * Deletes the link between the activity and the given competence
     * @param competence
     * @param activityURL
     * @return
     */
    @Deprecated
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @POST
    @Path("/SuggestedActivityForCompetence/delete")
    public Response deleteSuggestedActivityForCompetence(@QueryParam("competence") String competence, @QueryParam("activityUrl") String activityURL) {
        SuggestedActivityForCompetence2Ont.delete(activityURL, competence);
        return Response.ok("edge created").build();
    }

    private Response handleLinkValidation(String linkId,
                                          Boolean isValid) {
        HandleLinkValidationInOnt
                .convert(new LinkValidationData(linkId,
                        isValid));
        return Response.ok("link updated").build();
    }

    /**
     * Get the GUI operator tree
     * <p/>
     * It has the same format as the competence tree
     *
     * @param course             the (courseId) context of the competences ("university") for no
     *                           filter
     * @param selectedCatchwords
     * @param selectedOperators
     * @return
     */
    @Deprecated
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @GET
    @Path("/operatortree/{course}")
    public List<OperatorXMLTree> getOperatorTree(
            @PathParam("course") String course,
            @QueryParam(value = "selectedCatchwords") List<String> selectedCatchwords,
            @QueryParam(value = "selectedOperators") List<String> selectedOperators, @QueryParam("learningTemplate") String learningTemplate) {

        CompetenceFilterData data = new CompetenceFilterData(selectedCatchwords, selectedOperators, course, null, null, null, learningTemplate, true, null);
        List<OperatorXMLTree> result = Ont2CompetenceTree.getOperatorXMLTree(data);
        return result;
    }

    /**
     * Get the GUI Catchword Tree
     * <p/>
     * It has the same format as the competence tree
     *
     * @param course             the (courseId) context of the competences ("university") for no
     *                           filter
     * @param selectedCatchwords
     * @param selectedOperators
     * @return
     */
    @Deprecated
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @GET
    @Path("/catchwordtree/{course}/{cache}")
    public List<CatchwordXMLTree> getCatchwordTree(
            @PathParam("course") String course,
            @QueryParam(value = "selectedCatchwords") List<String> selectedCatchwords,
            @QueryParam(value = "selectedOperators") List<String> selectedOperators, @QueryParam("learningTemplate") String learningTemplate) {
        CompetenceFilterData data = new CompetenceFilterData(selectedCatchwords, selectedOperators, course, null, null, null, learningTemplate, true, null);
        List<CatchwordXMLTree> result = Ont2CompetenceTree.getCatchwordXMLTree(data);
        return result;

    }

    /**
     * Get all the learning templates
     *
     * @return
     */
    @Deprecated
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @GET
    @Path("/learningtemplates")
    public StringList getAllLearningTemplates() {
        StringList learningTemplates = Ont2LearningTemplates
                .convert();
        return learningTemplates;
    }


    /**
     * A user selects a learning template as his project in portfolio
     *
     * @param userName         the use selecting the template
     * @param groupId          (or courseId context) is the context the learning template is
     *                         selected
     * @param selectedTemplate the learning template selected
     * @return
     */
    @Deprecated
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @POST
    @Path("/learningtemplates/selected/add")
    public Response addLearningTemplateSelection(
            @QueryParam(value = "userId") String userName,
            @QueryParam(value = "groupId") String groupId,
            @QueryParam(value = "selectedTemplate") String selectedTemplate) {
        LearningTemplateData data = new LearningTemplateData(
                userName, groupId, selectedTemplate);
        LearningTemplateToOnt.convert(data);
        return Response.ok("templateSelection updated")
                .build();
    }


    /**
     * @param learningTemplateResultSet
     * @return
     * @throws ContainsCircleException
     */
    @Deprecated
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @POST
    @Path("/learningtemplate/add")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response addLearningTemplateSelection(LearningTemplateResultSet learningTemplateResultSet) throws ContainsCircleException {
        LearningTemplateToOnt.convertLearningTemplateResultSet(learningTemplateResultSet);
        return Response.ok("templateSelection updated")
                .build();
    }

    /**
     * get the selected learning templates for a given user
     *
     * @param userName
     * @param groupId
     * @return
     */
    @Deprecated
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @GET
    @Path("/learningtemplates/selected")
    public Response getSelectedLearningTemplates(
            @QueryParam(value = "userId") String userName,
            @QueryParam(value = "groupId") String groupId) {
        LearningTemplateData data = new LearningTemplateData(
                userName, groupId, null);
        StringList result = Ont2SelectedLearningTemplate
                .convert(data);
        return RestUtil.buildCachedResponse(result, false);
    }


    /**
     * Delete the selection of a learning template of a user.
     *
     * @param userName
     * @param groupId
     * @param selectedTemplate
     * @return
     */
    @Deprecated
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @POST
    @Path("/learningtemplates/selected/delete")
    public Response deleteSelectedLearningTemplate(
            @QueryParam(value = "userId") String userName,
            @QueryParam(value = "groupId") String groupId,
            @QueryParam(value = "selectedTemplate") String selectedTemplate) {

        LearningTemplateData data = new LearningTemplateData(
                userName, groupId, selectedTemplate);
        DeleteTemplateInOnt.convert(data);
        return Response.ok(
                "templateSelection updated after delete")
                .build();
    }

    /**
     * Returns a gridview of learning templates (mainly used in epos port).
     * Users and their selfevaluation are presented.
     *
     * @param userName
     * @param groupId          (or courseId context)
     * @param selectedTemplate
     * @return
     */
    @Deprecated
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @GET
    @Path("learningtemplates/gridview")
    public SuggestedCompetenceGrid getGridView(
            @QueryParam(value = "userId") String userName,
            @QueryParam(value = "groupId") String groupId,
            @QueryParam(value = "selectedTemplate") String selectedTemplate) {

        LearningTemplateData data = new LearningTemplateData(
                userName, groupId, selectedTemplate);
        SuggestedCompetenceGrid result = Ont2SuggestedCompetenceGrid
                .convert(data);
        return result;
    }


    /**
     * Allows to persist the users self evaluation (persisting the complete grid
     * view that was changed in the ui)
     *
     * @param userName                   the user who self-evaluated
     * @param groupId                    or courseId context (depending on the evidence source)
     * @param reflectiveAssessmentHolder
     * @return
     */
    @Deprecated
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @POST
    @Path("learningtemplates/gridview/update")
    public Response updateGridView(
            @QueryParam(value = "userId") String userName,
            @QueryParam(value = "groupId") String groupId,
            ReflectiveAssessmentsListHolder reflectiveAssessmentHolder) {

        ReflectiveAssessmentChangeData assessmentChangeData = new ReflectiveAssessmentChangeData(
                userName, groupId,
                reflectiveAssessmentHolder);
        ReflectiveAssessmentHolder2Ont
                .convert(assessmentChangeData);
        return Response.ok("reflexion updated").build();
    }

    /**
     * @param wrapper
     * @return
     */
    @Deprecated
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @POST
    @Path("/learningtemplates/addEpos")
    public Response importEpos(EPOSTypeWrapper wrapper) {
        EposImporter.importEposCompetences(Arrays
                .asList(wrapper.getEposCompetences()));
        return Response.ok("epos templates updated")
                .build();
    }


    /**
     *
     * @param learningTemplateName
     * @return
     */
    @Deprecated
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @GET
    @Path("/learningtemplate/get/{learningTemplateName}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public LearningTemplateResultSet getLearningTemplate(
            @PathParam("learningTemplateName") String learningTemplateName) {
        // OntologyWriter.convert();
        LearningTemplateResultSet result = Ont2LearningTemplateResultSet
                .convert(learningTemplateName);
        return result;
    }

    /**
     *
     * @param learningTemplateName
     * @return
     */
    @Deprecated
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @POST
    @Path("/learningtemplate/delete/{learningTemplateName}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response deleteLearningTemplate(
            @PathParam("learningTemplateName") String learningTemplateName) {
        DeleteLearningTemplateinOnt
                .convert(learningTemplateName);

        // change for testcommit to gitup
        return Response.ok("learningTemplate deleted")
                .build();
    }


    /**
     * This is a generic interface to update a concept by it's id
     * @param clazz
     * @param oldId
     * @param newId
     * @return
     * @throws Exception
     */
    @Deprecated
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @POST
    @Path("/update/{clazz}/{oldId}/{newId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response updateCatchword(@PathParam("clazz") String clazz, @PathParam("oldId") String oldId, @PathParam("newId") String newId) throws Exception {
        Dao o = (Dao) Class.forName("uzuzjmd.competence.persistence.dao." + clazz).getConstructor(String.class).newInstance(oldId);
        o.updateId(newId);
        return Response.ok("updated")
                .build();
    }




}