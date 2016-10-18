package uzuzjmd.competence.shared.assessment;

public class ReflectiveAssessmentChangeData {
	private String userName;
	private String groupId;
	private ReflectiveAssessmentsListHolder reflectiveAssessmentHolder;

	public ReflectiveAssessmentChangeData(String userName, String groupId, ReflectiveAssessmentsListHolder reflectiveAssessmentHolder) {
		super();
		this.userName = userName;
		this.groupId = groupId;
		this.reflectiveAssessmentHolder = reflectiveAssessmentHolder;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public ReflectiveAssessmentsListHolder getReflectiveAssessmentHolder() {
		return reflectiveAssessmentHolder;
	}

	public void setReflectiveAssessmentHolder(ReflectiveAssessmentsListHolder reflectiveAssessmentHolder) {
		this.reflectiveAssessmentHolder = reflectiveAssessmentHolder;
	}

}
