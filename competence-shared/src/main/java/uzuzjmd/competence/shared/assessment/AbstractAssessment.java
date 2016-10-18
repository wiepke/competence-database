package uzuzjmd.competence.shared.assessment;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by dehne on 30.05.2016.
 */
@XmlRootElement(name="abstractAssessment")
public class AbstractAssessment implements IAssessment {
    private String identifier;
    private int assessmentIndex;
    private String[] scale;
    private int minValue;
    private int maxValue;
    private String  assessmentId;
    private Boolean learningGoal;

    public AbstractAssessment() {
    }

    public AbstractAssessment(String assessmentId) {
        this.assessmentId = assessmentId;
    }

    public AbstractAssessment(
            String identifier, int assessmentIndex, String[] scale, int minValue, int maxValue, String assessmentId) {
        this.identifier = identifier;
        this.assessmentIndex = assessmentIndex;
        this.scale = scale;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.assessmentId = assessmentId;
    }




    @Override
    public void setMinValue(Integer minValue) {
        this.minValue = minValue;
    }

    @Override
    public void setMaxValue(Integer maxValue) {
        this.maxValue = maxValue;
    }

    @Override
    public void setTypeOfSelfAssessment(TypeOfSelfAssessment typeOfSelfAssessment) {
        this.identifier = typeOfSelfAssessment.toString();
    }

    @Override
    public TypeOfSelfAssessment getTypeOfSelfAssessment() {
        return TypeOfSelfAssessment.valueOf(identifier);
    }

    @Override
    public Integer getAssessmentIndex() {
        return this.assessmentIndex;
    }

    @Override
    public Boolean getLearningGoal() {
        return this.learningGoal;
    }

    @Override
    public void setAssessmentIndex(Integer assessmentIndex) {
        this.assessmentIndex = assessmentIndex;
    }

    @Override
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
    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }

    @Override
    public String getAssessmentId() {
        return assessmentId;
    }
}
