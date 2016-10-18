package uzuzjmd.competence.mapper.rest.write;

import datastructures.trees.ActivityEntry;
import uzuzjmd.competence.persistence.dao.Badge;
import uzuzjmd.competence.persistence.dao.CourseContext;
import uzuzjmd.competence.persistence.dao.EvidenceActivity;
import uzuzjmd.competence.persistence.dao.User;
import uzuzjmd.competence.persistence.ontology.Edge;
import uzuzjmd.competence.service.rest.CourseApiImpl;
import uzuzjmd.competence.service.rest.ProgressApiImpl;
import uzuzjmd.competence.service.rest.RecommenderApiImpl;
import uzuzjmd.competence.service.rest.UserApiImpl;
import uzuzjmd.competence.shared.activity.ActivityTyp;
import uzuzjmd.competence.shared.badges.BadgeData;
import uzuzjmd.competence.shared.badges.BadgeDataList;
import uzuzjmd.competence.shared.moodle.SyncDataSet;
import uzuzjmd.competence.shared.moodle.UserCourseListItem;
import uzuzjmd.competence.shared.moodle.UserCourseListResponse;
import uzuzjmd.competence.shared.moodle.UserTree;
import java.util.List;

/**
 * Created by dehne on 07.10.2016.
 */
public class Moodle2Ont {


    private static UserApiImpl userApi = new UserApiImpl();
    private static CourseApiImpl courseApiImpl = new CourseApiImpl();
    private static ProgressApiImpl progressApi = new ProgressApiImpl();

    private static long lastUpdated = 0;

    public static Boolean sync(SyncDataSet dataSet) throws Exception {

        // sync user
        User user = new User(dataSet.getUserName());
        if (dataSet.getSyncUsers()) {
            if(!user.exists()) {
                user.persist();
            }
        }

        if (checkLastUpdate() && (dataSet.getSyncActivities() || dataSet.getSyncCourses() || dataSet.getSyncBadges())) {
            return false;
        }

        if(dataSet.getSyncActivities()) {
            syncActivities(user,dataSet.getPassword());
        } else if (dataSet.getSyncCourses()) {
            syncCourses(user, dataSet.getPassword());
        }

        if (dataSet.getSyncBadges()) {
            syncBadges(user,dataSet.getPassword());
        }

        return true;
    }

    private static boolean checkLastUpdate() {
        long now = System.currentTimeMillis();
        if (now-lastUpdated < 40000) {
            return true;
        } else {
            lastUpdated = now;
        }
        return false;
    }

    private static void syncBadges(User user, String password) throws Exception {
        BadgeDataList badgesForUser = progressApi.getBadgesForUser(user.getId(), password);
        for (BadgeData badgeData : badgesForUser) {
            Badge badge = new Badge(badgeData.getBadgeId()+"", badgeData.getName(), badgeData.getDescription(),
                    badgeData.getPng(), badgeData.getIssued());
            badge.persist();
            badge.addUser(user);
        }
       }

    private static void syncActivities(User user, String password) throws Exception {
        //recommenderApiImpl.createSuggestedActivityForCompetence()
        UserCourseListResponse userCourseListItems = syncCourses(user, password);
        for (UserCourseListItem userCourseListItem : userCourseListItems) {
            CourseContext courseContext = new CourseContext(userCourseListItem.getCourseid()+"");
            //List<Competence> linkedCompetences = courseContext.getLinkedCompetences();
            UserTree[] activitiesForUser =
                    courseApiImpl.getActivitiesForUser(userCourseListItem.getCourseid() + "", user.getId(), password);
            for (UserTree userTree : activitiesForUser) {
                List<ActivityTyp> activityTypes = userTree.getActivityTypes();
                for (ActivityTyp activityType : activityTypes) {
                    List<ActivityEntry> activities = activityType.getActivities();
                    for (ActivityEntry activity : activities) {
                        EvidenceActivity evidenceActivity = new EvidenceActivity(activity.getUrl(), activity.getName());
                        evidenceActivity.persist();
                       /* for (Competence linkedCompetence : linkedCompetences) {
                            recommenderApiImpl.createSuggestedActivityForCompetence(linkedCompetence.getId(),
                                    evidenceActivity.getId());
                        }*/
                        //Edge.CourseCourseContext.
                    }
                }
            }
        }

    }

    private static UserCourseListResponse syncCourses(User user, String password) throws Exception {
        UserCourseListResponse coursesForUser = userApi.getCoursesForUser(user.getId(), password);
        for (UserCourseListItem userCourseListItem : coursesForUser) {
            CourseContext courseContext = new CourseContext(userCourseListItem.getCourseid()+"", userCourseListItem
                    .getName());
            courseContext.persist();
            courseContext.createEdgeWith(Edge.CourseContextOfUser, user);
        }
        return coursesForUser;
    }
}
