package uzuzjmd.competence.mapper.rest.write

import uzuzjmd.competence.persistence.dao.{ReflectiveQuestion, ReflectiveQuestionAnswer, User}
import uzuzjmd.competence.shared.assessment.ReflectiveQuestionAnswerData

/**
  * Created by dehne on 28.09.2016.
  */
object ReflectiveQuestionAnswer2Ont {
   def convert(reflectiveQuestionAnswer: ReflectiveQuestionAnswerData ): Unit = {
     val text2: String = reflectiveQuestionAnswer.getText()
     val user: User = new User(reflectiveQuestionAnswer.getUserId());
     val datecreated: Long = System.currentTimeMillis();
     val question: ReflectiveQuestion = new ReflectiveQuestion(reflectiveQuestionAnswer.getQuestionId)
     val reflectiveQuestionAnswerResult = new ReflectiveQuestionAnswer(text2, user, datecreated, question)
     reflectiveQuestionAnswerResult.persist()
   }

}
