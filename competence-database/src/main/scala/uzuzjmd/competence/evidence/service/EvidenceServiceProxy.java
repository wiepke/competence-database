package uzuzjmd.competence.evidence.service;

import java.util.List;

import javax.ws.rs.core.Response;

import uzuzjmd.competence.evidence.model.LMSSystems;
import config.PropUtil;
import uzuzjmd.competence.shared.moodle.UserCourseListResponse;
import uzuzjmd.competence.shared.moodle.UserTree;

/**
 * a proxy that routes the requests to the lms implemention provided
 */
public class EvidenceServiceProxy implements EvidenceService {

	private EvidenceProviderMap evidenceProviderMap;

	public EvidenceServiceProxy() {
		this.evidenceProviderMap = new EvidenceProviderMap();

		if (PropUtil.getProp("moodleEnabled").equals("true")) {
			try {
				EvidenceService evidenceRestServiceImpl = new MoodleEvidenceRestServiceImpl();
				evidenceProviderMap.evidenceMap.put(LMSSystems.moodle.toString(), evidenceRestServiceImpl);
			} catch (NullPointerException e) {
				System.err.println("moodle configuration mistake");
			}
		}

	}

	@Override
	public String[] getOrganizations() {
		return new String[] { "university" };
	}

	@Override
	public String[] getLMSSystems() {
		return evidenceProviderMap.evidenceMap.keySet().toArray(new String[0]);
	}

	@Override
	public UserCourseListResponse getCourses(String lmsSystem, String organization, String user, String password) {
		if (!evidenceProviderMap.evidenceMap.containsKey(lmsSystem)) {
			throw new BadParameterException("Anwendungsplattform " + lmsSystem + " wurde nicht konfiguriert");
		}
		return evidenceProviderMap.evidenceMap.get(lmsSystem).getCourses(lmsSystem, organization, user, password);
	}

	@Override
	public UserTree[] getUserTree(String course, String lmssystem, String organization, String username, String password) {
		if (!evidenceProviderMap.evidenceMap.containsKey(lmssystem)) {
			throw new BadParameterException("Anwendungsplattform " + lmssystem + " wurde nicht konfiguriert");
		}

		return evidenceProviderMap.evidenceMap.get(lmssystem).getUserTree(course, lmssystem, organization, username, password);
	}

	@Override
	public Response getUserTreeCrossDomain(String course, String lmssystem, String organization, String username, String password) {
		throw new Error("decorator called");
	}

	@Override
	public Boolean exists(String user, String password, String lmsSystem, String organization) {
		if (lmsSystem == null) {
			throw new BadParameterException("Anwendungsplattform " + lmsSystem + " wurde nicht konfiguriert");
		}
		if (lmsSystem.equals("all")) {
			for (String key : evidenceProviderMap.evidenceMap.keySet()) {
				if (!evidenceProviderMap.evidenceMap.get(key).exists(user, password, lmsSystem, organization)) {
					return false;
				}
			}
			return true;
		} else if (!evidenceProviderMap.evidenceMap.containsKey(lmsSystem)) {
			throw new BadParameterException("Anwendungsplattform " + lmsSystem + " wurde nicht konfiguriert");
		}

		return evidenceProviderMap.evidenceMap.get(lmsSystem).exists(user, password, lmsSystem, organization);
	}

	@Override
	public void addUserTree(String course, List<UserTree> usertree, String lmssystem, String organization) {
		if (!evidenceProviderMap.evidenceMap.containsKey(lmssystem)) {
			throw new BadParameterException("Anwendungsplattform " + lmssystem + " wurde nicht konfiguriert");
		}
		this.evidenceProviderMap.evidenceMap.put(lmssystem, new InMemoryEvidenceService());
		evidenceProviderMap.evidenceMap.get(lmssystem).addUserTree(course, usertree, lmssystem, organization);
	}

	@Override
	public void addCourses(String user, UserCourseListResponse usertree, String lmssystem, String organization) {
		if (!evidenceProviderMap.evidenceMap.containsKey(lmssystem)) {
			throw new BadParameterException("Anwendungsplattform " + lmssystem + " wurde nicht konfiguriert");
		}
		this.evidenceProviderMap.evidenceMap.put(lmssystem, new InMemoryEvidenceService());
		evidenceProviderMap.evidenceMap.get(lmssystem).addCourses(user, usertree, lmssystem, organization);
	}

}
