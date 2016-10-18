package uzuzjmd.competence.mapper.rest.read

import uzuzjmd.competence.persistence.neo4j.CompetenceNeo4jQueryManagerImpl
import uzuzjmd.competence.shared.assessment.ReflectiveQuestionAnswerHolder
import uzuzjmd.competence.util.LanguageConverter

/**
  * Created by dehne on 28.09.2016.
  */
object Ont2ReflectiveQuestionAnswer extends LanguageConverter {
  def convert(questionId: String, userId: String): ReflectiveQuestionAnswerHolder = {
    val db = new CompetenceNeo4jQueryManagerImpl;
    return db.getReflectiveQuestionAnswers(userId,questionId, null);
  }
}
