package uzuzjmd.competence.shared.learningtemplate;

import uzuzjmd.competence.shared.assessment.ReflectiveAssessmentsListHolder;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ViewScoped
@ManagedBean(name = "SuggestedCompetenceColumn", eager = true)
public class SuggestedCompetenceColumn implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SuggestedCompetenceColumn() {
	}

	private ReflectiveAssessmentsListHolder reflectiveAssessmentListHolder;

	@ManagedProperty("#{testOutput}")
	private String testOutput;

	@ManagedProperty("#{progressInPercent}")
	private int progressInPercent;

	public String getTestOutput() {
		return testOutput;
	}

	public void setTestOutput(String testOutput) {
		this.testOutput = testOutput;
	}

	public int getProgressInPercent() {
		return progressInPercent;
	}

	public void setProgressInPercent(int progressInPercent) {
		this.progressInPercent = progressInPercent;
	}

	public ReflectiveAssessmentsListHolder getReflectiveAssessmentListHolder() {
		return reflectiveAssessmentListHolder;
	}

	public void setReflectiveAssessmentListHolder(
			ReflectiveAssessmentsListHolder reflectiveAssessmentListHolder) {
		this.reflectiveAssessmentListHolder = reflectiveAssessmentListHolder;
	}

/*	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "(percent: "
				+ this.progressInPercent
				+ "Assessment: "
				+ this.getReflectiveAssessmentListHolder()
						.toString() + ")";
	}*/
}
