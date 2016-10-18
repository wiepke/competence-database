package uzuzjmd.competence.evidence.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import uzuzjmd.competence.evidence.model.LMSSystems;
import uzuzjmd.competence.evidence.service.moodle.MoodleContentResponseList;
import uzuzjmd.competence.shared.moodle.MoodleEvidence;
import uzuzjmd.competence.evidence.service.moodle.SimpleMoodleService;
import uzuzjmd.competence.evidence.service.rest.mapper.Evidence2Tree;
import uzuzjmd.competence.shared.moodle.UserCourseListResponse;
import uzuzjmd.competence.shared.moodle.UserTree;

import javax.jws.WebService;
import javax.ws.rs.core.Response;

import java.util.List;

/**
 * Webservice interface for the moodle service
 */
@WebService(endpointInterface = "uzuzjmd.competence.evidence.service.EvidenceService")
public class MoodleEvidenceRestServiceImpl extends
        AbstractEvidenceService {

    public static LoadingCache<String, UserTree[]> cacheImpl;
    Logger logger = LogManager
            .getLogger(MoodleEvidenceRestServiceImpl.class
                    .getName());
    private Thread cacheThread;

    public MoodleEvidenceRestServiceImpl() {
        initCache();
    }

    /*public UserTree[] getCachedUserTree(String courseId) {
        try {
            return cacheImpl.get(courseId);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        throw new Error("Cache not working");
    }*/

    public MoodleContentResponseList getCourseContents(
            String courseId, String username,
            String password) {

        SimpleMoodleService moodleService = new SimpleMoodleService(
                username, password);
        return moodleService.getMoodleContents(courseId);
    }

    @Override
    public UserTree[] getUserTree(String course,
                                  String lmsSystem, String organization,
                                  String username, String password) {

        SimpleMoodleService simpleService = new SimpleMoodleService(
                username, password);
        MoodleEvidence[] moodleEvidences = simpleService
                .getMoodleEvidenceList(course).toArray(
                        new MoodleEvidence[0]);
        if (moodleEvidences.length == 0) {
            logger.debug("could not load moodle evidences from upcompetence plugin for courseId: "
                    + course);
        }
        MoodleContentResponseList listMoodleContent = this
                .getCourseContents(course, username,
                        password);

        Evidence2Tree mapper = new Evidence2Tree(
                listMoodleContent, moodleEvidences);
        UserTree[] result = mapper.getUserTrees().toArray(
                new UserTree[0]);
        return result;
    }

    private void initCache() {
        if (cacheImpl == null) {
            cacheImpl = CacheBuilder
                    .newBuilder()
                    .maximumSize(1000)
                    .build(new CacheLoader<String, UserTree[]>() {

                        public UserTree[] load(
                                final String key) {
                            return getUserTree(key, null,
                                    null, null, null);
                        }
                    });
        }
        if (cacheThread == null) {
            cacheThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        for (String key : cacheImpl.asMap()
                                .keySet()) {
                            cacheImpl.refresh(key);
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            cacheThread.start();
        }

    }




    @Override
    public UserCourseListResponse getCourses(
            String lmsSystem, String organization,
            String user, String userPassword) {
        if (!LMSSystems.moodle.toString().equals(lmsSystem)) {
            return new UserCourseListResponse();
        }
        SimpleMoodleService simpleService = new SimpleMoodleService(
                user, userPassword);
        UserCourseListResponse result = simpleService
                .getMoodleCourseList();
        return result;
    }

    @Override
    public Boolean exists(String username, String password,
                          String lmsSystem, String organization) {
        try {
            SimpleMoodleService simpleService = new SimpleMoodleService(
                    username, password);
            simpleService.toString();
            if (simpleService.wasError()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void addUserTree(String course,
                            List<UserTree> usertree, String lmssystem,
                            String organization) {

        throw new Error("not implemented");
    }

    @Override
    public Response getUserTreeCrossDomain(String course, String lmssystem, String organization, String username, String password) {
        return null;
    }

    @Override
    public void addCourses(String user,
                           UserCourseListResponse usertree,
                           String lmssystem, String organization) {
        throw new Error("not implemented");
    }

}
