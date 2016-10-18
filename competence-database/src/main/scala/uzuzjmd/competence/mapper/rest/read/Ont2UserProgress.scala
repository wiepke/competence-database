package uzuzjmd.competence.mapper.rest.read

import uzuzjmd.competence.persistence.dao._
import uzuzjmd.competence.persistence.neo4j.CompetenceNeo4jQueryManagerImpl
import uzuzjmd.competence.persistence.ontology.Edge
import uzuzjmd.competence.shared.assessment.{ReflectiveQuestionAnswerHolder, AbstractAssessment, TypeOfSelfAssessment}
import uzuzjmd.competence.shared.competence.CompetenceLinksMap
import uzuzjmd.competence.shared.converter.SelfAssessmentAdapter
import uzuzjmd.competence.shared.progress.{UserCompetenceProgress, UserProgress}
import uzuzjmd.competence.util.LanguageConverter

import scala.collection.JavaConverters._
import scala.collection.mutable


/**
  * Created by dehne on 30.05.2016.
  */
object Ont2UserProgress extends LanguageConverter {

  val manager = new CompetenceNeo4jQueryManagerImpl

  def convert(competence : String, userId: String) : UserCompetenceProgress = {
    val competenceDAO = new Competence(competence)
    val user = new User(userId)
    // get assessments
    val converter = new SelfAssessmentAdapter
    val assessments: List[AbstractAssessment] = TypeOfSelfAssessment.values().toList.map(x=> competenceDAO
      .getAssessment(user,x)).map(y=>converter.unmarshal(y))

    // get evidences
    val linksMap: CompetenceLinksMap = Ont2CompetenceLinkMap.convert(userId)
    val linksView = linksMap.getMapUserCompetenceLinks.get(competence)

    // get answered questions
    val reflectionQuestionHolder = manager.getReflectiveQuestionAnswers(userId, null, competence);

    //Ont2ReflectiveQuestionAnswer.convert()
    val result = new UserCompetenceProgress(competence, linksView, assessments.asJava, reflectionQuestionHolder)
    return result
  }

  def convert2(user : String, courseId: String) : UserProgress = {
    val course = new CourseContext(courseId)
    val progressList: mutable.Buffer[UserCompetenceProgress] =
      course.getLinkedCompetences.asScala
      .filterNot(_.getId.equals("Kompetenz"))
      .map(x=>x.getDefinition).map(convert(_, user))
      .filterNot(x=>x.getCompetenceLinksView == null && (x.getAbstractAssessment.isEmpty || x.getAbstractAssessment.asScala
        .forall(_.getAssessmentIndex == 0) && x.getReflectiveQuestionAnswerHolder.getData.isEmpty))
    val result = new UserProgress()
    if (!progressList.isEmpty) {
      result.setUserCompetenceProgressList(progressList.asJava)
    }
    return result
  }
}
