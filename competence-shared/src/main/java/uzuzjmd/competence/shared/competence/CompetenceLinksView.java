package uzuzjmd.competence.shared.competence;

import uzuzjmd.competence.shared.activity.CommentData;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;

/**
 * The class represents the link between the activity and the comments related to it for a specific class
 * The evidence link can be validated.
 */
@XmlRootElement(name = "competenceLinksView")
public class CompetenceLinksView {
	private String abstractLinkId;
	private String evidenceTitel;
	private String evidenceUrl;
	private List<CommentData> comments;
	private Boolean validated;

	public CompetenceLinksView() {
		this.comments = new LinkedList<CommentData>();
	}

	public CompetenceLinksView(String abstractLinkId, String evidenceTitel,
							   String evidenceUrl, List<CommentData> comments, Boolean validated) {
		this.abstractLinkId = abstractLinkId;
		this.evidenceTitel = evidenceTitel;
		this.evidenceUrl = evidenceUrl;
		this.comments = new LinkedList<CommentData>();
		if (comments != null) {
			this.comments.addAll(comments);
		}

		// for (CommentData CommentData : comments) {
		// comments.add(CommentData);
		// }
		this.validated = validated;
	}

	public String getEvidenceTitel() {
		return evidenceTitel;
	}

	public void setEvidenceTitel(String evidenceTitel) {
		this.evidenceTitel = evidenceTitel;
	}

	public String getEvidenceUrl() {
		return evidenceUrl;
	}

	public void setEvidenceUrl(String evidenceUrl) {
		this.evidenceUrl = evidenceUrl;
	}

	@XmlElementWrapper(name="commentList")
	public List<CommentData> getComments() {
		return comments;
	}

	public void setComments(List<CommentData> comments) {
		this.comments = comments;
	}

	public Boolean getValidated() {
		return validated;
	}

	public void setValidated(Boolean validated) {
		this.validated = validated;
	}

	public String getAbstractLinkId() {
		return abstractLinkId;
	}



	public int compareTo(Object arg0) {
		CompetenceLinksView toCompare = (CompetenceLinksView) arg0;
		if (toCompare.getEvidenceTitel().equals(this.getEvidenceTitel())) {
			return 0;
		}
		return toCompare.getEvidenceTitel().hashCode() > this
				.getEvidenceTitel().hashCode() ? 1 : -1;
	}

}
