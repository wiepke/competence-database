package uzuzjmd.competence.shared.assessment;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by dehne on 21.09.2016.
 */
@XmlRootElement(name = "ReflectiveQuestionData")
public class ReflectiveQuestionData {
    private String question;
    private String competenceId;
    private String questionId;

    public ReflectiveQuestionData() {
    }

    public ReflectiveQuestionData(
            String question, String competenceId, String questionId) {
        this.question = question;
        this.competenceId = competenceId;
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCompetenceId() {
        return competenceId;
    }

    public void setCompetenceId(String competenceId) {
        this.competenceId = competenceId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}
