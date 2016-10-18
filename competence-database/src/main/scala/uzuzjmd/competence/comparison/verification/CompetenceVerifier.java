package uzuzjmd.competence.comparison.verification;

import edu.stanford.nlp.trees.GrammaticalRelation;

/**
 * Created by dehne on 14.03.2016.
 */
public interface CompetenceVerifier {
    public java.lang.Boolean isCompetence(String sentence, GrammaticalRelation.Language language);
}
