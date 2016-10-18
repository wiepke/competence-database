package uzuzjmd.competence.recommender

import java.lang.Double
import java.util
import uzuzjmd.competence.persistence.dao.{CourseContext, Competence, User}
import uzuzjmd.competence.util.LanguageConverter
import collection.JavaConverters._
import scala.collection.mutable

/**
  * Created by dehne on 31.03.2016.
  */
class CompetenceRecommenderImpl extends CompetenceRecommender with LanguageConverter{
  override def recommendCompetences(email: String, competenceToReach: String, courseId: String): util.HashMap[String, Double] = {
    val user = new User(email)
    val course  = new CourseContext(courseId)
    val result = new util.HashMap[String, Double]()
    if (competenceToReach == null) {
       getNextCompetences(user, course).asScala.foreach(x=>result.put(x.getDefinition, 1.0))
    } else {
      val competenceToReachDao: Competence = new Competence(competenceToReach)
      val path = getResultPathToCompetence(user, competenceToReachDao).asScala
      if (course != null) {
        path.filter(course.getLinkedCompetences.contains(_)).foreach(x=>result.put(x.getDefinition, 1.0))
      }
      path.foreach(x=>result.put(x.getDefinition, 1.0))
    }
    return result
  }

  def getResultPathToCompetence(user: User, competenceToReach: Competence) : util.List[Competence] = {
    val competences: util.List[Competence] = user.getCompetencesLearned;
    return competences.asScala.map(x=>x.getShortestSuggestedLearningPath(competenceToReach).asScala).flatten.asJava
  }

  def getNextCompetences(user: User, course: CourseContext): util.List[Competence] = {
      val competences: util.List[Competence] = user.getCompetencesLearned;
      val following: mutable.Buffer[Competence] =  competences.asScala.map(x=>x.getFollowingCompetences().asScala).flatten;
      val following1 =  following.filterNot(competences.contains(_))
      if (course != null) {
        return following1.filter(course.getLinkedCompetences.contains(_)).asJava
      }
    return following1.asJava
  }


}
