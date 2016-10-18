package uzuzjmd.competence.shared.assessment;

import uzuzjmd.competence.shared.converter.SelfAssessmentAdapter;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Created by dehne on 23.09.2016.
 */
@XmlRootElement(name = "Assessment")
public interface IAssessment {
    void setMinValue(Integer minValue);
    void setMaxValue(Integer maxValue);
    void setTypeOfSelfAssessment(TypeOfSelfAssessment typeOfSelfAssessment);
    TypeOfSelfAssessment getTypeOfSelfAssessment();
    Integer getAssessmentIndex();
    Boolean getLearningGoal();
    void setAssessmentIndex(Integer assessmentIndex);
    void setLearningGoal(Boolean learningGoal);
    int getMinValue();
    int getMaxValue();
    String getAssessmentId();
    void setAssessmentId(String assessmentId);
}
