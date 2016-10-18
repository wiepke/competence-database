package uzuzjmd.competence.recommender

import java.lang.Double
import java.util

import uzuzjmd.competence.persistence.dao._
import uzuzjmd.competence.persistence.ontology.Edge
import uzuzjmd.competence.service.rest.CourseApiImpl
import uzuzjmd.competence.shared.activity.Evidence
import uzuzjmd.competence.util.LanguageConverter

import scala.collection.JavaConverters._
import scala.collection.mutable

/**
  * Created by dehne on 31.03.2016.
  */
class ActivityRecommenderImpl extends ActivityRecommender with LanguageConverter {
  override def recommendActivities(email: String, competenceToReach: String, courseId: String, password: String): util.HashMap[Evidence, Double] = {
    val competence = new Competence(competenceToReach);
    val user = new User(email);
    val course = new CourseContext(courseId)
    val result = new util.HashMap[Evidence, Double];

    // TODO if course is given and the user credentials, activities of the course can be queried
    if (email != null && competenceToReach != null && user.exists() && competence.exists()) {
      val result2: util.HashMap[Evidence, Double] = getActivitiesForCompetence(competence)
      competence.getRequiredCompetences.foreach(x => result.putAll(getActivitiesForCompetence(x)))
      result.putAll(result2)
    }

    if (email != null) {
      val activities: mutable.Buffer[EvidenceActivity] = user.getCompetencesInterestedIn.asScala.map(x => x.getAssociatedDaosAsRange(Edge.SuggestedActivityForCompetence, classOf[EvidenceActivity])).flatten
      val result3 = activityToMap(result, activities.map(x => (x :: Nil).asJava))
      result.putAll(result3)
    }
    if (courseId != null && password != null) {
      val courseApiImpl = new CourseApiImpl
      val courseData = courseApiImpl.getActivitiesForUser(courseId, email, password)
      val activities = courseData.toList.map(x=>x.getActivityTypes.asScala.map(y=>y.getActivities.asScala)).flatten.flatten.map(z=>new Evidence(z.getName, z.getUrl, email))
      val result4 = new util.HashMap[Evidence, Double]
      activities.foreach(result4.put(_, 3.0))
    }
    // todo add query activities from lms here


    return result
  }


  // find out intermediary competences
  // recommend activities that are suggested for intermediary competences directly
  // recommend activities that are linked to courses that are linked to intermediary competences




/**
  * get activities for the competence given
  *
  * @param competence
  * @return
  */
def getActivitiesForCompetence (competence: Competence): util.HashMap[Evidence, Double] = {
  val result = new util.HashMap[Evidence, Double] ()
  // recommend activities that are suggested for the competence
  val activitiesFromDirectLink: util.List[EvidenceActivity] = competence.getAssociatedDaosAsDomain (Edge.SuggestedActivityForCompetence, classOf[EvidenceActivity] )
  activitiesFromDirectLink.foreach (x => result.put (new Evidence (x.getPrintableName, x.getUrl, null), 1.0) )
  // recommend activities that are linked to a course which in turn is linked to the competence
  val activities = competence.getAssociatedDaosAsDomain (Edge.linksCompetence, classOf[AbstractEvidenceLink] ).map (x => x.getAllActivities)
  activityToMap (result, activities)
}

  def activityToMap (result: util.HashMap[Evidence, Double], activities: mutable.Buffer[util.List[EvidenceActivity]] ): util.HashMap[Evidence, Double] = {
  activities.foreach (_.foreach (x => result.put (new Evidence (x.getPrintableName, x.getUrl, null), 2.0) ) )
  result
}

  def findSuggestedActivities (competence: Competence): List[EvidenceActivity] = {
  val evidences = competence.getAssociatedDaosAsDomain (Edge.SuggestedActivityForCompetence, classOf[EvidenceActivity] )
  return evidences.asScala.toList;
}

  /*def findSuggestedActivitiesBasedOnCourse(competence: Competence) : List[EvidenceActivity] = {
      val courses: util.List[CourseContext] = competence.getAssociatedDaosAsDomain(Edge.CourseContextOfCompetence, classOf[CourseContext])
      courses.asScala.map(x=>x.getAssociatedDaoAsRange(Edge))
  }*/
}
