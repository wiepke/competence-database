package uzuzjmd.competence.mapper.rest.read

import java.{lang, util}

import uzuzjmd.competence.persistence.dao.{Competence, CourseContext, Role}
import uzuzjmd.competence.shared.progress.ProgressMap

import scala.collection.JavaConverters._
import scala.collection.mutable

class Ont2ProgressMap(val course: String, val selectedCompetences: java.util.List[String]) {
  val courseDao = new CourseContext(course)
  val linkedCompetences: mutable.Buffer[Competence] = courseDao.getLinkedCompetences.asScala
  val userOfCourseContext = courseDao.getLinkedUser.asScala.filter(x => x.getRole.equals(Role.student))

  def getProgressMap(): ProgressMap = {
    val numberOfLinks = getNumberOfCompetencesLinkedToCourse
    if (numberOfLinks == 0) {
      return new ProgressMap();
    }
    val resultScala: util.Map[String, lang.Double] = getUserNumberOfLinksMap.mapValues(x => new java.lang.Double(Math.round((x.toFloat / getNumberOfCompetencesLinkedToCourse().toFloat) * 100))).asJava
    val result = new ProgressMap
    result.putAll(resultScala)
    return result
  }

  def getNumberOfCompetencesLinkedToCourse(): Int = {
    if (selectedCompetences.isEmpty() || selectedCompetences == null) {
      return linkedCompetences.length
    }
    return linkedCompetences.filter(x => selectedCompetences.contains(x.getDefinition)).length
  }

  def getUserNumberOfLinksMap(): Map[String, Int] = {
    if (selectedCompetences.isEmpty() || selectedCompetences == null) {
      val result = userOfCourseContext.
        map(x => (x.getName -> x.getAssociatedLinks.asScala.filter(link => link.getAllCourseContexts.contains(courseDao)).length))
      return result.toMap
    }

    val result = userOfCourseContext.
      map(x => (x.getName -> x.getAssociatedLinks.asScala.
        filter(y => selectedCompetences.containsAll(y.getAllLinkedCompetences.asScala.map(competence => competence.getDefinition).asJava)).
        filter(link => link.getAllCourseContexts.contains(courseDao)).length)).toMap
    return result
  }
}