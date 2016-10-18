package uzuzjmd.competence.service.rest;

import io.swagger.annotations.ApiOperation;
import uzuzjmd.competence.mapper.rest.read.Ont2LearningTemplates;
import uzuzjmd.competence.mapper.rest.read.Ont2SelectedLearningTemplate;
import uzuzjmd.competence.mapper.rest.write.DeleteTemplateInOnt;
import uzuzjmd.competence.mapper.rest.write.LearningTemplateToOnt;
import uzuzjmd.competence.persistence.dao.LearningProjectTemplate;
import uzuzjmd.competence.shared.learningtemplate.LearningTemplateData;
import datastructures.lists.StringList;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by dehne on 11.04.2016.
 */

@Path("/api1")
public class LearningTemplateApiImpl implements uzuzjmd.competence.api.LearningTemplateApi {


    @ApiOperation(value =  "get all " +
            "the learningtemplates." , notes = "If the user is " +
            "specified only the users learning templates are queried. \n Learning templates are" +
            " aggregations of the competences a user wants to learn and their " +
            "relationships. They can be looked at as learning trails as well as learning goals.")
    @Override
    @Path("/learningtemplates")
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public StringList getLearningTemplates(@QueryParam("userId") String userId) {
        if (userId != null) {
            return Ont2SelectedLearningTemplate.convert(new LearningTemplateData(userId, null, null));
        } else {
            StringList learningTemplates = Ont2LearningTemplates
                    .convert();
            return learningTemplates;
        }
    }

    @ApiOperation(value = "create a learningtemplate")
    @Override
    @Path("/learningtemplates/{learningtemplateId}")
    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    public Response addLearningTemplate(@PathParam("learningtemplateId") String learningtemplateId, LearningTemplateData data) {
        data.setSelectedTemplate(learningtemplateId);
        LearningTemplateToOnt.convert(data);
        return Response.ok().build();
    }

    @ApiOperation(value = "delete a learning template")
    @Override
    @Path("/learningtemplates/{learningtemplateId}")
    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteLearningTemplate(@PathParam("learningtemplateId") String learningtemplateId, @QueryParam(value = "userId") String userName) throws Exception {
        if (userName != null) {
            DeleteTemplateInOnt.convert(new LearningTemplateData(userName, null, learningtemplateId));
        }
        return deleteLearningTemplateIntern(learningtemplateId);
    }

    private Response deleteLearningTemplateIntern(@PathParam("learningtemplateId") String learningtemplateId) throws Exception {
        LearningProjectTemplate learningProjectTemplate = new LearningProjectTemplate(learningtemplateId);
        learningProjectTemplate.delete();
        return Response.ok().build();
    }




}
