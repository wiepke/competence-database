package uzuzjmd.competence.shared.competence;

import java.util.List;

public class CompetenceLinkedToCourseData {
	private String course;
	private List<String> competences;
	private Boolean compulsoryBoolean;
	private String requirements;

	public CompetenceLinkedToCourseData(String course, List<String> competences, Boolean compulsoryBoolean, String requirements) {
		super();
		this.course = course;
		this.competences = competences;
		this.compulsoryBoolean = compulsoryBoolean;
		this.requirements = requirements;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public List<String> getCompetences() {
		return competences;
	}

	public void setCompetences(List<String> competences) {
		this.competences = competences;
	}

	public Boolean getCompulsoryBoolean() {
		return compulsoryBoolean;
	}

	public void setCompulsoryBoolean(Boolean compulsoryBoolean) {
		this.compulsoryBoolean = compulsoryBoolean;
	}

	public String getRequirements() {
		return requirements;
	}

	public void setRequirements(String requirements) {
		this.requirements = requirements;
	}

}
