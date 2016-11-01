package uzuzjmd.competence.tests;

import config.MagicStrings;
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
import java.util.List;

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


    public void createReflectiveQuestions() {
        // assumes user exists

        java.util.List<String> competences = createCompetences();
        String[] questions = new String[] { "eine wichtige Frage?" ,  "eine wichtige Frage?2"};
        ReflectiveQuestionData question = new ReflectiveQuestionData(questions[0],competences.get(0));
        ReflectiveQuestionData question2 = new ReflectiveQuestionData(questions[1],competences.get(0));
        ReflectiveQuestionData question3 = new ReflectiveQuestionData(questions[0],competences.get(1));
        ReflectiveQuestionData question4 = new ReflectiveQuestionData(questions[1],competences.get(1));

        // create questions
        String questionCreationUrl = "/api1/competences/questions";
        Response response = target(questionCreationUrl).request().post(Entity.entity(question, MediaType
                .APPLICATION_JSON));
        assertTrue(response.getStatus() == 200);

        Response response2 = target(questionCreationUrl).request().post(Entity.entity(question2, MediaType
                .APPLICATION_JSON));
        assertTrue(response2.getStatus() == 200);

        Response response3 = target(questionCreationUrl).request().post(Entity.entity(question3, MediaType
                .APPLICATION_JSON));
        assertTrue(response3.getStatus() == 200);

        Response response4 = target(questionCreationUrl).request().post(Entity.entity(question4, MediaType
                .APPLICATION_JSON));
        assertTrue(response4.getStatus() == 200);

        String[] answers = new String [] {"gute Antwort1" , "gute Antwort2"};
        // create answers
        ReflectiveQuestionAnswerData reflectiveQuestionAnswerData = new ReflectiveQuestionAnswerData(answers[0],
                user, questions[0], System.currentTimeMillis());

        ReflectiveQuestionAnswerData reflectiveQuestionAnswerData2 = new ReflectiveQuestionAnswerData(answers[1],
                user, questions[0], System.currentTimeMillis());

        ReflectiveQuestionAnswerData reflectiveQuestionAnswerData3 = new ReflectiveQuestionAnswerData(answers[0],
                user, questions[1], System.currentTimeMillis());

        ReflectiveQuestionAnswerData reflectiveQuestionAnswerData4 = new ReflectiveQuestionAnswerData(answers[1],
                user, questions[1], System.currentTimeMillis());

        java.util.List<ReflectiveQuestionAnswerData> answerDatas = new ArrayList<ReflectiveQuestionAnswerData>();
        answerDatas.add(reflectiveQuestionAnswerData);
        answerDatas.add(reflectiveQuestionAnswerData2);
        answerDatas.add(reflectiveQuestionAnswerData3);
        answerDatas.add(reflectiveQuestionAnswerData4);

        for (ReflectiveQuestionAnswerData answerData : answerDatas) {
            Response r = target("/api1/competences/questions/answers").request().put(Entity.entity(answerData, MediaType
                    .APPLICATION_JSON));
            assertTrue(r.getStatus() == 200);
        }

    }

    public List<String> createCompetences()  {
        ArrayList<String> result = new ArrayList<String>();
        String competenceString = "Die Studierenden vergleichen zwei Sätze anhand ihrer Bausteine";
        result.add(competenceString);
        CompetenceData data =
                new CompetenceData("vergleichen", Arrays.asList(new String[]{"vergleichen", "Sätze", "Bausteine"}),
                        null, null, null, competenceString);
        Response post = target("/api1/competences/" + competenceString).request()
                .put(Entity.entity(data, MediaType.APPLICATION_JSON));
        assertTrue(post.getStatus() == 200);
        String competenceString2 = "Die Studierenden vergleichen drei Sätze anhand ihrer Bausteine";
        result.add(competenceString2);
        Response post1 = target("/api1/competences/" + competenceString2).request()
                .put(Entity.entity(data, MediaType.APPLICATION_JSON));
        assertTrue(post1.getStatus() == 200);
        //Thread.sleep(3000l);
//        java.util.List<String> result = target("/api1/competences/semblances/"+competenceString).request().get(java.util.List.class);
//        assertTrue(result.contains(competenceString2));
        return result;
    }

    @Test
    public void createUserProgress() throws Exception {
        User userDao = new User(user);
        userDao.printableName = user;
        userDao.persist();

        // create questions and answers for user
        createReflectiveQuestions();

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


