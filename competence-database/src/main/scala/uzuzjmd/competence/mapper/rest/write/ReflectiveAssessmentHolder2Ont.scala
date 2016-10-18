package uzuzjmd.competence.mapper.rest.write

import javax.ws.rs.WebApplicationException

import uzuzjmd.competence.persistence.abstractlayer.WriteTransactional
import uzuzjmd.competence.persistence.dao._
import uzuzjmd.competence.shared.assessment.{TypeOfSelfAssessment, ReflectiveAssessment, ReflectiveAssessmentChangeData, ReflectiveAssessmentsListHolder}

import scala.collection.JavaConverters.asScalaBufferConverter

object ReflectiveAssessmentHolder2Ont extends WriteTransactional[ReflectiveAssessmentChangeData] {

  def convert(reflectiveAssement: ReflectiveAssessmentChangeData) {
    execute(convertHelper _, reflectiveAssement)
  }

  def convertHelper(reflectiveAssessment: ReflectiveAssessmentChangeData) {
    val context = new CourseContext(reflectiveAssessment.getGroupId);
    val user = new User(reflectiveAssessment.getUserName, Role.teacher.toString, null, null, context);
    if (!user.exists()) {
      throw new WebApplicationException(new Exception("User not known in database"));
    }
    if (!context.exists()) {
      throw new WebApplicationException(new Exception("Course or group not known in database"));
    }

    ReflectiveAssessmentHolder2Ont.convertAssessment(user, context, reflectiveAssessment
      .getReflectiveAssessmentHolder, TypeOfSelfAssessment.EPOSTYPE);
  }

  def convertAssessment(user: User, courseContext: CourseContext, reflectiveAssessmentHolder:
  ReflectiveAssessmentsListHolder, typeOfSelfAssessment: TypeOfSelfAssessment) {
    reflectiveAssessmentHolder.getReflectiveAssessmentList().asScala.foreach(updateSingleAssessment(_, user,
      courseContext, typeOfSelfAssessment))
  }

  def convertAssessmentStringToIndex(assessment: String): java.lang.Integer = {
    val enumMap = List("gar nicht" -> 0, "schlecht" -> 1, "mittel" -> 2, "gut" -> 3)
    val map = enumMap.toMap.get(assessment).get
    return map
  }

  def updateSingleAssessment(reflectiveAssessment: ReflectiveAssessment, user: User, courseContext: CourseContext,
                             typeOfSelfAssessment: TypeOfSelfAssessment) = {
    val competence = new Competence(reflectiveAssessment.getCompetenceDescription())
    val selfAssessment = new SelfAssessment(competence, user, convertAssessmentStringToIndex(reflectiveAssessment
      .getAssessment()), reflectiveAssessment.getIsLearningGoal(), typeOfSelfAssessment)
    selfAssessment.persist
  }
}