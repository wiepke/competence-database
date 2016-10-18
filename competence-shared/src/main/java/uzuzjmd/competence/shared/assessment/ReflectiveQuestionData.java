package uzuzjmd.competence.shared.assessment;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by dehne on 21.09.2016.
 */
@XmlRootElement(name = "ReflectiveQuestionData")
public class ReflectiveQuestionData {
    private String question;
    private String competenceId;

    public ReflectiveQuestionData() {
    }

    public ReflectiveQuestionData(
            String question, String competenceId) {
        this.question = question;
        this.competenceId = competenceId;
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
}
