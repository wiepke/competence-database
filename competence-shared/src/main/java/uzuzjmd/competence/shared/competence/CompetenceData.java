package uzuzjmd.competence.shared.competence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import uzuzjmd.competence.shared.assessment.ReflectiveQuestionAnswerData;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.ws.WebServiceException;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement(name = "CompetenceData")
public class CompetenceData {

    private String operator;
    private List<String> catchwords;
    private List<String> superCompetences;
    private List<String> subCompetences;
    private String learningProjectName;


    private String forCompetence;

    public CompetenceData() {
    }

    public CompetenceData(String operator,
                          List<String> catchwords,
                          List<String> superCompetences,
                          List<String> subCompetences,
                          String learningProjectName, String forCompetence) {
        super();
        this.operator = operator;
        this.catchwords = catchwords;
        this.superCompetences = superCompetences;
        this.subCompetences = subCompetences;
        this.learningProjectName = learningProjectName;
        this.forCompetence = forCompetence;

        if (catchwords == null || catchwords.isEmpty()) {
            throw new WebServiceException(new Exception("catchwords must be given"));
        }

        if (superCompetences == null) {
            this.superCompetences = new LinkedList<String>();
        }
        if (subCompetences == null) {
            this.subCompetences = new LinkedList<String>();
        }
    }


    @XmlTransient
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public String getForCompetence() {
        return forCompetence;
    }

    public void setForCompetence(String forCompetence) {
        this.forCompetence = forCompetence;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public List<String> getCatchwords() {
        return catchwords;
    }

    public void setCatchwords(List<String> catchwords) {
        this.catchwords = catchwords;
    }

    public List<String> getSuperCompetences() {
        return superCompetences;
    }

    public void setSuperCompetences(
            List<String> superCompetences) {
        this.superCompetences = superCompetences;
    }

    public List<String> getSubCompetences() {
        return subCompetences;
    }

    public void setSubCompetences(
            List<String> subCompetences) {
        this.subCompetences = subCompetences;
    }

    public String getLearningProjectName() {
        return learningProjectName;
    }

    public void setLearningProjectName(
            String learningProjectName) {
        this.learningProjectName = learningProjectName;
    }
}
