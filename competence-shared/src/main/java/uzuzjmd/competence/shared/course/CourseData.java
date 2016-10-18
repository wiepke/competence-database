package uzuzjmd.competence.shared.course;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * This wraps a pair of courseId and competences in order to exchange competences linked to a courseId
 */
@XmlRootElement(name = "CourseData")
public class CourseData {
	private String courseId;
	private String printableName;
	private List<String> competences;

	public CourseData() {
	}

	public CourseData(String courseId, String printableName, List<String> competences) {
        super();
		this.courseId = courseId;
		this.printableName = printableName;
		this.competences = competences;
	}

	
	public CourseData(String courseId, List<String> competences) {
        super();
		this.courseId = courseId;
		this.competences = competences;
	}

	public CourseData(String courseId, String printableName) {
        super();
		this.courseId = courseId;
		this.printableName = printableName;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public List<String> getCompetences() {
		return competences;
	}

	public void setCompetences(List<String> competences) {
		this.competences = competences;
	}

	public String getPrintableName() {
		return printableName;
	}

	public void setPrintableName(String printableName) {
		this.printableName = printableName;
	}
}
