package uzuzjmd.competence.recommender;

import uzuzjmd.competence.shared.moodle.UserCourseListItem;

import java.util.HashMap;

/**
 * Created by dehne on 31.03.2016.
 */
public class DummyCourseRecommender implements CourseRecommender {
    @Override
    public HashMap<UserCourseListItem, Double> recommendCourse(String userEmail) {
        HashMap<UserCourseListItem, Double> result = new HashMap<UserCourseListItem, Double>();
        result.put(new UserCourseListItem(2l, "Testkurse1"), 1.0);
        result.put(new UserCourseListItem(3l, "Testkurse2"), 1.0);
        return result;
    }
}
