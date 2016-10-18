package uzuzjmd.competence.recommender;

import java.util.HashMap;

/**
 * Created by dehne on 31.03.2016.
 */
public class DummyCompetenceRecommender implements CompetenceRecommender {
    @Override
    public HashMap<String, Double> recommendCompetences(String email, String competenceToReach, String userEmail) {
        HashMap<String, Double> result = new HashMap<String, Double>();
        result.put("Wir können viewl", 1.0);
        result.put("Wir können wenig", 0.0);
        return result;
    }
}
