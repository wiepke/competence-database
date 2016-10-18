package uzuzjmd.competence.shared.activity;

import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 */
@XmlRootElement(name = "CommentData")
public class CommentData {
	// the date when the comment was created
	@ApiModelProperty(value = "the date created", required = false)
	private  Long created;
	// the id of the comment
	@ApiModelProperty(value = "the id of the comment; will be generated", required = false)
	private String commentId;
	// the id of the comment this comment is linked to
	@ApiModelProperty(value = "the id of the comment this comment references", required = false)
	private String commentedCommentId;
	// the id of the abstract link this comment is attached to
	@ApiModelProperty(value = "the id of the evidence the comment references", required = true)
	private String linkId;
	// the id of the user
	@ApiModelProperty(value = "the id of the user creating the comment", required = true)
	private String user;
	@ApiModelProperty(value = "the plain text of the comment", required = true)
	private String text;
	@ApiModelProperty(value = "the id of the course the link is attached to", required = false)
	private String courseContext;


	public CommentData() {
	}

	public CommentData(String userId, String text, Long dateCreated) {
		this.commentId = dateCreated + text;
		this.user = userId;
		this.text = text;
		this.created = dateCreated;
	}

	public CommentData(Long dateCreated, String linkId, String user, String text, String courseContext, String commentedCommentId) {
		super();
		this.commentId = dateCreated + text;
		this.linkId = linkId;
		this.user = user;
		this.text = text;
		this.courseContext = courseContext;
		this.created = dateCreated;
		this.commentedCommentId = commentedCommentId;
	}



	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}



	public String getText() {
		return text;
	}



	public void setText(String text) {
		this.text = text;
	}

	public String getCourseContext() {
		return courseContext;
	}

	public void setCourseContext(String courseContext) {
		this.courseContext = courseContext;
	}

	public String getCommentedCommentId() {
		return commentedCommentId;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public Long getCreated() {
		return created;
	}

	public void setCreated(Long created) {
		this.created = created;
	}
}
