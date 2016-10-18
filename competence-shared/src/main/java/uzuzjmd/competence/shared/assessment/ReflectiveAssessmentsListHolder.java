package uzuzjmd.competence.shared.assessment;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.xml.bind.annotation.XmlRootElement;

@ManagedBean(name = "ReflectiveAssessmentsListHolder")
@ViewScoped
@XmlRootElement
public class ReflectiveAssessmentsListHolder implements
		Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManagedProperty("#{reflectiveAssessmentList}")
	private List<ReflectiveAssessment> reflectiveAssessmentList;
	private String suggestedMetaCompetence;
	private Assessment assessment;

	public ReflectiveAssessmentsListHolder() {
		init();
	}

	public List<ReflectiveAssessment> getReflectiveAssessmentList() {
		return reflectiveAssessmentList;
	}

	public void setReflectiveAssessmentList(
			List<ReflectiveAssessment> reflectiveAssessmentList) {
		this.reflectiveAssessmentList = reflectiveAssessmentList;
	}

	public String getSuggestedMetaCompetence() {
		return suggestedMetaCompetence;
	}

	public void setSuggestedMetaCompetence(
			String suggestedMetaCompetence) {
		this.suggestedMetaCompetence = suggestedMetaCompetence;
	}

	@PostConstruct
	public void init() {
		reflectiveAssessmentList = new LinkedList<ReflectiveAssessment>();
	}

	public Assessment getAssessment() {
		return assessment;
	}

	public void setAssessment(Assessment assessment) {
		this.assessment = assessment;
	}

	@Override
	public String toString() {
		return getReflectiveAssessmentList().toString();
	}
}
