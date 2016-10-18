package uzuzjmd.competence.shared.moodle;

import uzuzjmd.competence.shared.activity.Evidence;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * 
 * @author Julian Dehne Diese Klasse implementiert das EvidenzInterface für
 *         Moodle
 * 
 */
@XmlSeeAlso(Evidence.class)
@XmlRootElement
public class MoodleEvidence extends Evidence {

	private String course;
	private String activityTyp;

	public MoodleEvidence() {

	}

	public MoodleEvidence(String shortname, String string, String userId,
			String changed, String course, String activityTyp) {
		super(shortname, string, userId, changed);
		this.course = course;
		this.activityTyp = activityTyp;
	}

	public String getActivityTyp() {
		return activityTyp;
	}

	public void setActivityTyp(String activityTyp) {
		this.activityTyp = activityTyp;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}
}
