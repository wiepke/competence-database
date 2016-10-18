package uzuzjmd.competence.shared.assessment;

/**
 * Created by dehne on 28.09.2016.
 */
public interface IReflectiveQuestionAnswerData {
    String getText();
    String getQuestionId();
    String getUserId();
    void setText(String text);
    void setQuestionId(String questionid);
    void setUserId(String userId);
}
