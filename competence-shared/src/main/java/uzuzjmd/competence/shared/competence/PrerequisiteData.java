package uzuzjmd.competence.shared.competence;

import java.util.List;

/**
 * This class wraps a courseId, the linked competence and a number of prerequisites
 */
public class PrerequisiteData {
	private String course;
	private String postCompetence;
	private List<String> prerequisiteCompetences;
	
	public PrerequisiteData(String course, String linkedCompetence, List<String> prerequisiteCompetences) {
		this.setCourse(course);
		this.setPostCompetence(linkedCompetence);
		this.setPrerequisiteCompetences(prerequisiteCompetences);
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getPostCompetence() {
		return postCompetence;
	}

	public void setPostCompetence(String postCompetence) {
		this.postCompetence = postCompetence;
	}

	public List<String> getPrerequisiteCompetences() {
		return prerequisiteCompetences;
	}

	public void setPrerequisiteCompetences(List<String> prerequisiteCompetences) {
		this.prerequisiteCompetences = prerequisiteCompetences;
	}
	
	public void addElementSelectedCompetences(String element) {
		this.prerequisiteCompetences.add(element);
	}

}
