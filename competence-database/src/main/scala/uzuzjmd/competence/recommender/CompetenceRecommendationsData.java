package uzuzjmd.competence.recommender;

import uzuzjmd.competence.shared.activity.Evidence;

import java.util.HashMap;
import java.util.List;

/**
 * Created by dehne on 20.01.2016.
 */
public class CompetenceRecommendationsData {
    /**
     * The courseIds available
     */
    private List<String> courseIds;
    /**
     * The courseIds available and the Activities related
     */
    private HashMap<String, List<Evidence>> courseActivitiesMap;
    /**
     * the id of the user
     */
    private String userId;
    /**
     * the id of the user and the ids of the activities performed (the url)
     */
    private HashMap<String, List<String>> userHasPerformedActivityMap;
    /**
     * the courses (values) the user (key) is enrolled in
     */
    private HashMap<String, List<String>> userIsEnrolledInCourseMap;
    /**
     * the competences (values) the user (key) is enrolled in
     */
    private HashMap<String, List<String>> userHasCompetenceMap;

    public CompetenceRecommendationsData(List<String> courseIds, HashMap<String, List<Evidence>> courseActivitiesMap, String userId, HashMap<String, List<String>> userHasPerformedActivityMap, HashMap<String, List<String>> userIsEnrolledInCourseMap, HashMap<String, List<String>> userHasCompetenceMap) {
        this.courseIds = courseIds;
        this.courseActivitiesMap = courseActivitiesMap;
        this.userId = userId;
        this.userHasPerformedActivityMap = userHasPerformedActivityMap;
        this.userIsEnrolledInCourseMap = userIsEnrolledInCourseMap;
        this.userHasCompetenceMap = userHasCompetenceMap;
    }

    public List<String> getCourseIds() {
        return courseIds;
    }

    public void setCourseIds(List<String> courseIds) {
        this.courseIds = courseIds;
    }

    public HashMap<String, List<Evidence>> getCourseActivitiesMap() {
        return courseActivitiesMap;
    }

    public void setCourseActivitiesMap(HashMap<String, List<Evidence>> courseActivitiesMap) {
        this.courseActivitiesMap = courseActivitiesMap;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public HashMap<String, List<String>> getUserHasPerformedActivityMap() {
        return userHasPerformedActivityMap;
    }

    public void setUserHasPerformedActivityMap(HashMap<String, List<String>> userHasPerformedActivityMap) {
        this.userHasPerformedActivityMap = userHasPerformedActivityMap;
    }

    public HashMap<String, List<String>> getUserIsEnrolledInCourseMap() {
        return userIsEnrolledInCourseMap;
    }

    public void setUserIsEnrolledInCourseMap(HashMap<String, List<String>> userIsEnrolledInCourseMap) {
        this.userIsEnrolledInCourseMap = userIsEnrolledInCourseMap;
    }

    public HashMap<String, List<String>> getUserHasCompetenceMap() {
        return userHasCompetenceMap;
    }

    public void setUserHasCompetenceMap(HashMap<String, List<String>> userHasCompetenceMap) {
        this.userHasCompetenceMap = userHasCompetenceMap;
    }
}
