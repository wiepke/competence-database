package uzuzjmd.competence.recommender;

import uzuzjmd.competence.shared.activity.Evidence;

import java.util.HashMap;

/**
 * Created by dehne on 31.03.2016.
 */
public interface ActivityRecommender {
    /**
     * Produces recommended Competences based on information of the user
     * The double signifies the strength of the recommendation (1.0 for perfect recommendation)

     * @param email
     * @param competenceToReach
     * @param courseId
     * @return
     */
    public HashMap<Evidence, Double> recommendActivities(String email, String competenceToReach, String courseId, String password);
}
