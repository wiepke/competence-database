package uzuzjmd.competence.shared.user;

import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A simple Wrapper class for a userId, his courseContext and his role
 */
@XmlRootElement(name="UserData")
public class UserData {
	@ApiModelProperty(value = "the id (email) of the learner the who has evidenced the competence", required = true)
	private String userId;
	private String courseContext;
	@ApiModelProperty(value = "role can either be 'teacher' or 'student'", required = true)
	private String role;

	private String printableName;
	// the lms systems the userId is enrolled in can be inclusive "moodle" < db < "mobile"
	private String lmsSystems;

	public UserData() {
	}

	public UserData(String user, String printableName, String courseContext, String role, String lmsSystems) {
		super();
		this.userId = user;
		this.courseContext = courseContext;
		this.role = role;
		this.printableName = printableName;
		this.lmsSystems = lmsSystems;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCourseContext() {
		return courseContext;
	}

	public void setCourseContext(String courseContext) {
		this.courseContext = courseContext;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPrintableName() {
		return printableName;
	}

	public void setPrintableName(String printableName) {
		this.printableName = printableName;
	}

	public String getLmsSystems() {
		return lmsSystems;
	}

	public void setLmsSystems(String lmsSystems) {
		this.lmsSystems = lmsSystems;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		UserData userData = (UserData) o;

		if (!getUserId().equals(userData.getUserId()))
			return false;
		if (getCourseContext() != null ? !getCourseContext().equals(userData.getCourseContext()) : userData
				.getCourseContext() != null)
			return false;
		if (getRole() != null ? !getRole().equals(userData.getRole()) : userData.getRole() != null)
			return false;
		if (getPrintableName() != null ? !getPrintableName().equals(userData.getPrintableName()) : userData
				.getPrintableName() != null)
			return false;
		return getLmsSystems() != null ? getLmsSystems().equals(userData.getLmsSystems()) : userData
				.getLmsSystems() == null;

	}

	@Override
	public int hashCode() {
		int result = getUserId().hashCode();
		result = 31 * result + (getCourseContext() != null ? getCourseContext().hashCode() : 0);
		result = 31 * result + (getRole() != null ? getRole().hashCode() : 0);
		result = 31 * result + (getPrintableName() != null ? getPrintableName().hashCode() : 0);
		result = 31 * result + (getLmsSystems() != null ? getLmsSystems().hashCode() : 0);
		return result;
	}
}
