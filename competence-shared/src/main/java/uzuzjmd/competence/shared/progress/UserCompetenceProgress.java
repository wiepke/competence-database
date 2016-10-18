package uzuzjmd.competence.shared.progress;

import uzuzjmd.competence.shared.assessment.AbstractAssessment;
import uzuzjmd.competence.shared.assessment.ReflectiveQuestionAnswerHolder;
import uzuzjmd.competence.shared.competence.CompetenceLinksView;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by dehne on 30.05.2016.
 */
@XmlRootElement(name = "UserCompetenceProgress")
public class UserCompetenceProgress {

    // the competence the progress is referring to
    private String competence;
    // the activities linked to the competence with the given user
    private CompetenceLinksView[] competenceLinksView;
    // the self assessments of the user
    private java.util.List<AbstractAssessment> abstractAssessment;
    // the answers a user gives to reflective questions
    private ReflectiveQuestionAnswerHolder reflectiveQuestionAnswerHolder;

    public UserCompetenceProgress() {
        competenceLinksView = new CompetenceLinksView[0];
        abstractAssessment = new ArrayList<>();
        reflectiveQuestionAnswerHolder = new ReflectiveQuestionAnswerHolder();
    }

    public UserCompetenceProgress(String competence, CompetenceLinksView[] competenceLinksView,
                                  java.util.List<AbstractAssessment>
            abstractAssessment, ReflectiveQuestionAnswerHolder reflectiveQuestionAnswerHolder) {
        this.competence = competence;
        this.competenceLinksView = competenceLinksView;
        this.abstractAssessment = abstractAssessment;
        this.reflectiveQuestionAnswerHolder = reflectiveQuestionAnswerHolder;
    }


    public String getCompetence() {
        return competence;
    }

    public void setCompetence(String competence) {
        this.competence = competence;
    }

    @XmlElementWrapper(name="competenceLinksViewList")
    public CompetenceLinksView[] getCompetenceLinksView() {
        return competenceLinksView;
    }

    public void setCompetenceLinksView(CompetenceLinksView[] competenceLinksView) {
        this.competenceLinksView = competenceLinksView;
    }

    @XmlElementWrapper(name="abstractAssessmentList")
    public java.util.List<AbstractAssessment> getAbstractAssessment() {
        return this.abstractAssessment;
    }

    public void setAbstractAssessment(
            java.util.List<AbstractAssessment> abstractAssessment) {
        this.abstractAssessment = abstractAssessment;
    }

    public void setReflectiveQuestionAnswerHolder(
            ReflectiveQuestionAnswerHolder reflectiveQuestionAnswerHolder) {
        this.reflectiveQuestionAnswerHolder = reflectiveQuestionAnswerHolder;
    }

    public ReflectiveQuestionAnswerHolder getReflectiveQuestionAnswerHolder() {
        return reflectiveQuestionAnswerHolder;
    }
}
