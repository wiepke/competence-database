package uzuzjmd.competence.recommender

import java.lang.Double
import java.util

import uzuzjmd.competence.persistence.dao.{EvidenceActivity, CourseContext, Competence}
import uzuzjmd.competence.shared.moodle.UserCourseListItem

/**
  * Created by dehne on 31.03.2016.
  */
object CourseRecommenderImpl extends CourseRecommender{
  override def recommendCourse(userEmail: String): util.HashMap[UserCourseListItem, Double] = ???
  def getRecommendedCompetences() : List[Competence] = ???
  def getRecommendedCoursesForCompetences(input: List[Competence]): List[CourseContext] = ???
  def getRecommendedActivitiesForCompetences(input: List[Competence]) : List[EvidenceActivity] = ???
}
