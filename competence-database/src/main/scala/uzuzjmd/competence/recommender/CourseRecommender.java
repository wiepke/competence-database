package uzuzjmd.competence.recommender;

import uzuzjmd.competence.shared.moodle.UserCourseListItem;

import java.util.HashMap;

/**
 * Created by dehne on 20.01.2016.
 */
public interface CourseRecommender {
    /**
     * @param userEmail the email of the user for whom courses are recommended
     * @return the courseIds of the courses that are recommended and the normalized score for the recommendation (1.0 is perfect recommendation)
     */
    HashMap<UserCourseListItem, Double> recommendCourse(String userEmail);
}
