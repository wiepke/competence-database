package uzuzjmd.competence.shared.assessment;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by dehne on 28.09.2016.
 */
@XmlRootElement(name="ReflectiveQuestionAnswerData")
public class ReflectiveQuestionAnswerData {
    private String text;
    private String userId;
    private String questionId;
    private Long datecreated;

    public ReflectiveQuestionAnswerData() {
    }

    public ReflectiveQuestionAnswerData(String text, String userId, String questionId, Long datecreated) {
        this.text = text;
        this.userId = userId;
        this.questionId = questionId;
        this.datecreated = datecreated;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public Long getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(Long datecreated) {
        this.datecreated = datecreated;
    }
}
