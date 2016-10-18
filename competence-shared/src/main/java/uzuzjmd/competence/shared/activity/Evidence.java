package uzuzjmd.competence.shared.activity;

import io.swagger.annotations.ApiModelProperty;
import uzuzjmd.competence.shared.moodle.MoodleEvidence;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * Diese Klasse modelliert einen Kompetenznachweis
 *
 * this class models an evidence for a competence found in an e-learning system
 * 
 * @author Julian Dehne
 * 
 */
@XmlSeeAlso(MoodleEvidence.class)
@XmlRootElement(name = "Evidence")
public class Evidence {

	public Evidence() {
		// TODO Auto-generated constructor stub
	}

	public Evidence(String printableName, String url,
					String user) {
		this.shortname = printableName;
		this.url = url;
		this.userId = user;
	}

	public Evidence(String shortname, String url,
					String userId, String changed) {
		this.shortname = shortname;
		this.url = url;
		this.userId = userId;
		this.changed = changed;
	}

	private String shortname; // zur Anzeige
	private String url; // die web referenzierbare url des Kompetenznachweises
	@ApiModelProperty(value = "the id (email) of the learner the who has evidenced the competence", required = true)
	private String userId; // eine Identifikation des Users (z.B. die ID des
							// users in Moodle)
	private String username; // der username zum anzeigen
	private String changed; // das datum, wann der Kompetenznachweis erbracht
							// wurde in Menschenlesbarer Form

	private String email;

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getChanged() {
		return changed;
	}

	public void setChanged(String changed) {
		this.changed = changed;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Evidence{" +
				"shortname='" + shortname + '\'' +
				", url='" + url + '\'' +
				", userId='" + userId + '\'' +
				", username='" + username + '\'' +
				", changed='" + changed + '\'' +
				", email='" + email + '\'' +
				'}';
	}
}
