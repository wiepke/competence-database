package uzuzjmd.competence.persistence.dao;


import uzuzjmd.competence.persistence.ontology.Edge;
import uzuzjmd.competence.shared.assessment.IReflectiveQuestionAnswerData;

/**
 * Created by dehne on 28.09.2016.
 */
public class ReflectiveQuestionAnswer extends AbstractReflectiveQuestionAnswer implements IReflectiveQuestionAnswerData{
    public String text;
    public String datecreated;

    public ReflectiveQuestionAnswer(String id) {
        super(id);
    }

    public ReflectiveQuestionAnswer(String text, User user, Long datecreated, ReflectiveQuestion question) {
        super(text+user.getId()+question.getId()+datecreated);
        this.text = text;
        super.user = user;
        this.datecreated = datecreated+"";
        this.question = question;
    }

    public Long getDatecreated() {
        return Long.parseLong(datecreated);
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public String getQuestionId() {
        if (question != null) {
            return question.getId();
        }else return null;
    }

    @Override
    public String getUserId() {
        if (question != null) {
            return user.getId();
        } else return  null;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void setQuestionId(String questionId) {
        this.question = new ReflectiveQuestion(questionId);
    }

    @Override
    public void setUserId(String userId) {
        this.user = new User(userId);
    }

    @Override
    public String getCompetenceId() {
        return "";
    }

    @Override
    public Dao persist() throws Exception {
        Dao result = super.persist();
        createEdgeWith(Edge.AnswerForReflectiveQuestion,question);
        createEdgeWith(user,Edge.UserOfReflectiveQuestionAnswer);
        return result;
    }
}
