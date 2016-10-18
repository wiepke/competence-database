package uzuzjmd.competence.api;

import datastructures.maps.MapWrapper;
import uzuzjmd.competence.shared.activity.Evidence;
import uzuzjmd.competence.shared.moodle.UserCourseListItem;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;

/**
 * Created by dehne on 15.04.2016.
 */
public interface RecommenderApi {
    /**
     * returns all the competences recommended for a user given the users id (normally the userEmail)
     * @param userEmail
     * @param competenceToReach
     * @param courseId
     * @return
     */
    HashMap<String, Double> recommendCompetences( String userEmail, String competenceToReach,  String courseId);

    /**
     * returns all the activities recommended for a user given the users id (normally the userEmail)
     * @see uzuzjmd.competence.shared.activity.Evidence
     *
     * @param userEmail
     * @param competenceToReach
     * @param courseId
     * @return
     */
    HashMap<Evidence, Double> recommendActivities(String userEmail, String competenceToReach, String courseId, String password);

    /**
     * returns all the courses recommended for a user given the users id (normally the userEmail)
     * @param userEmail
     * @return
     */
    MapWrapper<UserCourseListItem, Double> recommendCourses(String userEmail);

    /**
     * Get competences linked to (courseId) context. (the moodle courseId Id)
     * <p/>
     * Returns all the competences recommended for a courseId context.
     *
     * @param courseId
     * @return
     */
    String[] getSuggestedCompetencesForCourse(
            @PathParam("courseId") String courseId) throws Exception;

    /**
     * Get competences linked to (courseId) context.
     * <p/>
     * Returns all the courses linked to a certain competence as a suggestions
     *
     * @param competenceId
     * @return
     */
    String[] getSuggestedCoursesForCompetence(
            @PathParam("competenceId") String competenceId) throws Exception;

    /**
     * The semantic of this interface is that an activity is linked to a competence as template.
     * This way the learner can navigate to the navigate if he/she wants to learn a specific set of competencies
     * @param competenceId should be the plain text string of the competence
     * @param activityId should be the url of the activity
     * @return
     * @throws Exception
     */
    Response createSuggestedActivityForCompetence(@PathParam("competenceId") String competenceId, @PathParam("activityId") String activityId) throws Exception;



    /**
     * Deletes the link between the courseId and the given competence
     * @param competence
     * @param course
     * @return
     */
    Response deleteSuggestedCourseForCompetence(@PathParam("competenceId") String competence, @PathParam("courseId") String course);

    /**
     * Deletes the link between the activity and the given competence
     * @param competenceId
     * @param activityId
     * @return
     */
    Response deleteSuggestedActivityForCompetence(@PathParam("competenceId") String competenceId, @PathParam("activityId") String activityId);
}
