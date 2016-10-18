package uzuzjmd.competence.shared.activity;

/**
 * This class wraps a pair of a linkId and the validity given by a teacher to it
 */
public class LinkValidationData {

	private String linkId;
	private Boolean isValid;
	
	public LinkValidationData(String linkId, Boolean isValid) {
		this.setLinkId(linkId);
		this.setIsValid(isValid);
	}

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public Boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}
}
