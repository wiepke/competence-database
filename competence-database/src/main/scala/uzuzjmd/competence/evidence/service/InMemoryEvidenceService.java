package uzuzjmd.competence.evidence.service;

import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.Response;

import uzuzjmd.competence.shared.moodle.UserCourseListResponse;
import uzuzjmd.competence.shared.moodle.UserTree;

/**
 * Not used much could be used for testing evidence based aspects
 */
public class InMemoryEvidenceService extends AbstractEvidenceService {

	public HashMap<String, UserTree[]> courseUserTreeMap = new HashMap<String, UserTree[]>();
	public HashMap<String, UserCourseListResponse> userCoursesMap = new HashMap<String, UserCourseListResponse>();

	// public HashMap<String, HashMap<String, UserTree[]>> lmsCourseHashMap =
	// new HashMap<String, HashMap<String, UserTree[]>>;

	public InMemoryEvidenceService() {

	}

	@Override
	public UserTree[] getUserTree(String course, String lmsSystem, String organization, String username, String password) {
		if (!courseUserTreeMap.containsKey(course)) {
			throw new BadParameterException("courseId " + course + " wurde nicht in dem System " + lmsSystem + " gefunden");
		}
		return courseUserTreeMap.get(course);
	}

	@Override
	public Response getUserTreeCrossDomain(String course, String lmssystem, String organization, String username, String password) {
		throw new Error("docorator called");
	}

	@Override
	public UserCourseListResponse getCourses(String lmsSystem, String organization, String user, String password) {
		if (!courseUserTreeMap.containsKey(user)) {
			throw new BadParameterException("user " + user + " wurde nicht in dem System " + lmsSystem + " gefunden");
		}
		return userCoursesMap.get(user);
	}

	@Override
	public Boolean exists(String user, String password, String lmsSystem, String organization) {
		return !userCoursesMap.keySet().isEmpty();
	}

	@Override
	public void addUserTree(String course, List<UserTree> usertree, String lmssystem, String organization) {
		courseUserTreeMap.put(course, usertree.toArray(new UserTree[0]));
	}

	@Override
	public void addCourses(String user, UserCourseListResponse usertree, String lmssystem, String organization) {
		userCoursesMap.put(user, usertree);

	}

}
