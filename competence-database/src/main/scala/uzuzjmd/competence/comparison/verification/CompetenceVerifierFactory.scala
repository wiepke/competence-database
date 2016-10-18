package uzuzjmd.competence.comparison.verification

import comparison.verification.SimpleCompetenceVerifier
import edu.stanford.nlp.trees.GrammaticalRelation.Language

/**
  * Created by dehne on 02.05.2016.
  */
object CompetenceVerifierFactory {
  def getSimpleCompetenceVerifier(input: String, language: Language ): SimpleCompetenceVerifier = {
    return new SimpleCompetenceVerifier(input, language)
  }

}
