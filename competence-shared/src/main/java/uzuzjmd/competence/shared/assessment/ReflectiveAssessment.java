package uzuzjmd.competence.shared.assessment;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "ReflectiveAssessment")
@ViewScoped
public class ReflectiveAssessment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Boolean isLearningGoal;
	private String competenceDescription;
	private String assessment;

	public ReflectiveAssessment() {
		// TODO Auto-generated constructor stub
	}

	public ReflectiveAssessment(Boolean isLearningGoal,
			String competenceDescription, String assessment) {
		super();
		this.isLearningGoal = isLearningGoal;
		this.competenceDescription = competenceDescription;
		this.assessment = assessment;
	}

	public Boolean getIsLearningGoal() {
		return isLearningGoal;
	}

	public void setIsLearningGoal(Boolean isLearningGoal) {
		this.isLearningGoal = isLearningGoal;
	}

	public String getCompetenceDescription() {
		return competenceDescription;
	}

	public void setCompetenceDescription(
			String competenceDescription) {
		this.competenceDescription = competenceDescription;
	}

	public String getAssessment() {
		return assessment;
	}

	public void setAssessment(String assessment) {
		this.assessment = assessment;
	}

	@Override
	public String toString() {
		return "a: " + getAssessment();
	}

}
