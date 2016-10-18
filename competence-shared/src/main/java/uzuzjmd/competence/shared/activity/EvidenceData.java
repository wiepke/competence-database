package uzuzjmd.competence.shared.activity;

import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "EvidenceData")
public class EvidenceData {


	private String courseId;
	@ApiModelProperty(value = "the id of the creator", required = true)
	private String creator;
	@ApiModelProperty(value = "the list of competences proven", required = true)
	private List<String> competences;
	@ApiModelProperty(value = "the activity (as in urls) that show the prove", required = true)
	private Evidence evidence;

	public EvidenceData() {

	}

	/**
	 * This class provides a wrapper for the service
	 * to exchange data necessary to like a competence to a user who has performed it.
	 *
	 * @param course      (the context of the acquirement)
	 * @param creator     the user who created the link
	 * @param competences the competences acquired
	 * @param evidence   the activity that stand as evidences in the form [url,
	 *                    speakingname]
	 *
	 */
	public EvidenceData(String course, String creator, List<String> competences, Evidence evidence) {
		super();
		this.courseId = course;
		this.creator = creator;
		this.competences = competences;
		this.evidence = evidence;
	}


	public static EvidenceData instance(String course, String creator,  List<String> competences, Evidence evidence) {
		return new EvidenceData(course, creator, competences, evidence);
	}


	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public List<String> getCompetences() {
		return competences;
	}

	public void setCompetences(List<String> competences) {
		this.competences = competences;
	}


	@XmlElement(name = "Evidence")
	public Evidence getEvidence() {
		return evidence;
	}

	public void setEvidence(Evidence evidence) {
		this.evidence = evidence;
	}
}
