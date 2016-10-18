package uzuzjmd.competence.tests;

import datastructures.lists.StringList;
import junit.framework.Assert;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import uzuzjmd.competence.evidence.service.rest.EvidenceServiceRestServerImpl;
import uzuzjmd.competence.persistence.dao.Competence;
import uzuzjmd.competence.persistence.dao.DBInitializer;
import uzuzjmd.competence.persistence.dao.SelfAssessment;
import uzuzjmd.competence.persistence.dao.User;
import uzuzjmd.competence.service.rest.*;
import uzuzjmd.competence.shared.activity.CommentData;
import uzuzjmd.competence.shared.assessment.AbstractAssessment;
import uzuzjmd.competence.shared.assessment.ReflectiveQuestionAnswerData;
import uzuzjmd.competence.shared.assessment.ReflectiveQuestionData;
import uzuzjmd.competence.shared.assessment.TypeOfSelfAssessment;
import uzuzjmd.competence.shared.competence.CompetenceData;
import uzuzjmd.competence.shared.competence.CompetenceLinksView;
import uzuzjmd.competence.shared.converter.SelfAssessmentAdapter;
import uzuzjmd.competence.shared.progress.UserCompetenceProgress;
import uzuzjmd.competence.shared.progress.UserProgress;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class ProgressApiTests extends JerseyTest {

    String competenceString = "Ich bin in der Lage eine Masterarbeit zu konzipieren";
    String user = "Mephisto";

    @Override
    protected javax.ws.rs.core.Application configure() {
        DBInitializer.init();
        return new ResourceConfig(LearningTemplateApiImpl.class, CompetenceApiImpl.class, UserApiImpl.class,
                CourseApiImpl.class, EvidenceApiImpl.class, EvidenceServiceRestServerImpl.class, ProgressApiImpl.class);
    }

    @Test
    public void createUserProgress() throws Exception {
        User userDao = new User(user);
        userDao.printableName = user;
        userDao.persist();

        // create evidence Link
        CompetenceLinksView competenceLinksView1 = new CompetenceLinksView();
        competenceLinksView1.setEvidenceTitel("Ich habe einen UI-Protoyp erstellt");
        competenceLinksView1.setEvidenceUrl("Http://meinefressewoistdasdenn");
        competenceLinksView1.setValidated(false);

        // add comments
        CommentData CommentData = new CommentData(user, "läuft gut", System.currentTimeMillis());
        CommentData CommentData2 = new CommentData(user, "läuft gut2", System.currentTimeMillis());
        ArrayList<CommentData> comments = new ArrayList<uzuzjmd.competence.shared.activity.CommentData>();
        comments.add(CommentData);
        comments.add(CommentData2);
        competenceLinksView1.setComments(comments);
        SelfAssessment assessment = new SelfAssessment(new Competence(competenceString), new User(user), 3, true,
                TypeOfSelfAssessment.ASSESSMENT);
        SelfAssessment assessment2 = new SelfAssessment(new Competence(competenceString), new User(user), 2, false,
                TypeOfSelfAssessment.MOTIVATION);
        SelfAssessment assessment3 = new SelfAssessment(new Competence(competenceString), new User(user), 2, false,
                TypeOfSelfAssessment.PROGRESS);

        LinkedList<AbstractAssessment> assessments = new LinkedList<AbstractAssessment>();
        SelfAssessmentAdapter selfAssessmentAdapter = new SelfAssessmentAdapter();
        assessments.add(selfAssessmentAdapter.unmarshal(assessment));
        assessments.add(selfAssessmentAdapter.unmarshal(assessment2));
        assessments.add(selfAssessmentAdapter.unmarshal(assessment3));

        UserCompetenceProgress userCompetenceProgress =
                new UserCompetenceProgress(competenceString, new CompetenceLinksView[]{competenceLinksView1},
                        assessments, null);
        Response response = target("/api1/progress/" + user + "/competences/" + competenceString).request()
                .put(Entity.entity(userCompetenceProgress, MediaType.APPLICATION_JSON));
        assertTrue(response.getStatus() == 200);

    }


    @Test
    public void getUserProgress() {
        UserCompetenceProgress response =
                target("/api1/progress/" + user + "/competences/" + competenceString).request()
                        .get(UserCompetenceProgress.class);
        assertFalse(response.getAbstractAssessment().isEmpty());
        assertTrue(response.getAbstractAssessment().size() == TypeOfSelfAssessment.values().length);
        assertTrue(response.getCompetence().equals(competenceString));
        assertTrue(response.getAbstractAssessment().iterator().next().getTypeOfSelfAssessment()
                .equals(TypeOfSelfAssessment.ASSESSMENT));
        assertFalse(response.getCompetenceLinksView().length == 0);
        assertFalse(response.getCompetenceLinksView()[0].getComments().isEmpty());
        assertFalse(response.getCompetenceLinksView()[0].getComments().get(0) == null);
        assertFalse(response.getCompetenceLinksView()[0].getComments().get(0).getCreated() == null);
        assertFalse(response.getCompetenceLinksView()[0].getComments().get(0).getUser() == null);

    }

    @Test
    public void getUserProgressGeneral() {
        UserProgress response = target("/api1/progress/" + user).request().get(UserProgress.class);
        UserCompetenceProgress assessment = response.getUserCompetenceProgressList().iterator().next();
        assertFalse(assessment.getAbstractAssessment().isEmpty());
        assertFalse(assessment.getCompetenceLinksView().length == 0);
        assertFalse(assessment.getCompetenceLinksView()[0].getComments().isEmpty());
        assertFalse(assessment.getCompetenceLinksView()[0].getComments().iterator().next() == null);
        assertFalse(assessment.getCompetenceLinksView()[0].getComments().size() == 2);
    }

    @Test
    public void getReflectiveQuestionsAnswersInProgress() throws Exception {
        // User anlegen
        User user = new User("Mephisto@stuff.com", "Mephisto", "Moodle", "university");
        user.persist();
        // Competence anlegen
        String competenceId = "progressen";
        CompetenceData competenceData =
                new CompetenceData("progress machen", Arrays.asList(new String[]{"progressen", "messen"}), null, null,
                        null, competenceId);
        Response r1 = target("/api1/competences/" + competenceId).request()
                .put(Entity.entity(competenceData, MediaType.APPLICATION_JSON));
        assertTrue(r1.getStatus() == 200);
        // Fragen anlegen
        String question = "Kannst du progressen?";
        String question2 = "Kannst du progressen2?";
        createQuestion(competenceId, question);
        createQuestion(competenceId, question2);
        StringList result = target("/api1/competences/" + competenceId + "/questions").request().get(StringList.class);
        assertFalse(result.getData().isEmpty());
        // Anwort anlegen
        String url3 = "/api1/competences/questions/answers";
        ReflectiveQuestionAnswerData reflectiveQuestionAnswerData =
                new ReflectiveQuestionAnswerData("ShizzleAnswer", user.getId(), question, null);
        Response response =
                target(url3).request().put(Entity.entity(reflectiveQuestionAnswerData, MediaType.APPLICATION_JSON));
        assertTrue(response.getStatus() == 200);
    }

    @Test
    public void getAnswersInProgress() throws Exception {

        getReflectiveQuestionsAnswersInProgress();

        // Progress abrufen
        User user = new User("Mephisto@stuff.com", "Mephisto", "Moodle", "university");
        String progressUrl = "/api1/progress/" + user.getId();
        UserProgress response1 = target(progressUrl).request(MediaType.APPLICATION_JSON).get(UserProgress.class);
        assertFalse(response1.getUserCompetenceProgressList().iterator().next().getReflectiveQuestionAnswerHolder()
                .getData().isEmpty());

    }

    private void createQuestion(String competenceId, String question) {
        ReflectiveQuestionData reflectiveQuestionData = new ReflectiveQuestionData(question, competenceId);
        Entity<ReflectiveQuestionData> payLoad;
        payLoad = Entity.entity(reflectiveQuestionData, MediaType.APPLICATION_JSON);
        Response response = target("/api1/competences/questions").request(MediaType.TEXT_PLAIN).post(payLoad);
        Assert.assertTrue(response.getStatus() == 200);
    }

}

