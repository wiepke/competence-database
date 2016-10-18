package uzuzjmd.competence.persistence.dao;

import uzuzjmd.competence.persistence.ontology.Edge;
import uzuzjmd.competence.shared.assessment.IAssessment;
import uzuzjmd.competence.shared.assessment.TypeOfSelfAssessment;

/**
 * Created by dehne on 11.01.2016.
 */
public class SelfAssessment extends AbstractSelfAssessment implements Cascadable, IAssessment {

    public Integer assessmentIndex = 0;
    public Boolean learningGoal = false;
    public String typeOfSelfAssessment;
    public Integer minValue = 0;
    public Integer maxValue = 0;



    public SelfAssessment(String id) {
        super(id);
    }


    public SelfAssessment(
            Competence competence, User user, Integer assessmentIndex, Boolean learningGoal,
            TypeOfSelfAssessment typeOfSelfAssessment) {
        super(user.getId() + competence.getId() + typeOfSelfAssessment.toString());
        this.assessmentIndex = assessmentIndex;
        this.learningGoal = learningGoal;
        this.user = user;
        this.competence = competence;
        this.typeOfSelfAssessment = typeOfSelfAssessment.toString();
    }

    public void setMinValue(Integer minValue) {
        this.minValue = minValue;
    }

    public void setMaxValue(Integer maxValue) {
        this.maxValue = maxValue;
    }

    public void setTypeOfSelfAssessment(TypeOfSelfAssessment typeOfSelfAssessment) {
        this.typeOfSelfAssessment = typeOfSelfAssessment.toString();
    }

    public TypeOfSelfAssessment getTypeOfSelfAssessment() {
        return TypeOfSelfAssessment.valueOf(typeOfSelfAssessment);
    }

    @Override
    public void persistMore() throws Exception {
        this.persist();
        createEdgeWith(Edge.AssessmentOfCompetence, competence);
        createEdgeWith(Edge.AssessmentOfUser, user);
    }

    public Integer getAssessmentIndex() {
        return assessmentIndex;
    }

    public Boolean getLearningGoal() {
        return learningGoal;
    }

    public void setAssessmentIndex(Integer assessmentIndex) {
        this.assessmentIndex = assessmentIndex;
    }

    public void setLearningGoal(Boolean learningGoal) {
        this.learningGoal = learningGoal;
    }

    @Override
    public int getMinValue() {
        return this.minValue;
    }

    @Override
    public int getMaxValue() {
        return this.maxValue;
    }

    @Override
    public String getAssessmentId() {
        return this.getId();
    }

    @Deprecated
    @Override
    public void setAssessmentId(String assessmentId) {
        setAssessmentId(assessmentId);
    }

    public void addCompetenceToAssessment(Competence competence) throws Exception {
        createEdgeWith(Edge.AssessmentOfCompetence, competence);
    }

    public void addUserToAssessment(User user) throws Exception {
        createEdgeWith(Edge.AssessmentOfUser, user);
    }

    @Override
    public Dao persist() throws Exception {
        super.persist();
        if (this.user != null) {
            addUserToAssessment(this.user);
        }
        if (this.competence != null) {
            addCompetenceToAssessment(competence);
        }
        return this;
    }
}
