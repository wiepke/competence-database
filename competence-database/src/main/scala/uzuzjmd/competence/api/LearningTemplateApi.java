package uzuzjmd.competence.api;

import uzuzjmd.competence.shared.learningtemplate.LearningTemplateData;
import datastructures.lists.StringList;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Created by dehne on 15.04.2016.
 */
public interface LearningTemplateApi {


    StringList getLearningTemplates(@QueryParam("userId") String userId);

    Response addLearningTemplate(@PathParam("learningtemplateId") String learningtemplateId, LearningTemplateData data);


    Response deleteLearningTemplate(@PathParam("learningtemplateId") String learningtemplateId, @QueryParam(value = "userId") String userName) throws Exception;


}
