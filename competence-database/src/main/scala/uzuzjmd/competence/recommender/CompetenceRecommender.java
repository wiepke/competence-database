package uzuzjmd.competence.recommender;

import scala.collection.immutable.List;
import uzuzjmd.competence.persistence.dao.Competence;

import java.util.HashMap;

/**
 * Created by dehne on 31.03.2016.
 */
public interface CompetenceRecommender {

    /**
     * Produces recommended Competences based on information of the user
     * The double signifies the strength of the recommendation (1.0 for perfect recommendation)
     *
     * @param email
     * @param competenceToReach
     * @param courseId          @return
     */
    public HashMap<String, Double> recommendCompetences(String email, String competenceToReach, String courseId);
}
