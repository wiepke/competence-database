package uzuzjmd.competence.mapper.rest.read

import uzuzjmd.competence.persistence.abstractlayer.ReadTransactional
import uzuzjmd.competence.persistence.dao.CourseContext
import uzuzjmd.competence.shared.course.CourseData
import uzuzjmd.competence.util.LanguageConverter

import scala.collection.JavaConverters.{asScalaBufferConverter, _}

/**
 * @author dehne
 */
object Ont2SelectedCompetencesForCourse extends ReadTransactional[String, Array[String]] with LanguageConverter {
  def convert(changes: String): Array[String] = {
    execute(convertHelper _, changes)
  }

  def convertHelper(changes: String): Array[String] = {
      val course = new CourseContext(changes);
      return course.getLinkedCompetences.asScala.map(x=>x.getDefinition).toArray;
  }

  def convertDao(changes: String) : CourseData =  {
    val course = new CourseContext(changes);
    val competences: List[String] = course.getLinkedCompetences.asScala.map(x=>x.getDefinition).toList;
    course.getFullDao.printableName
    val courseData = new CourseData(course.getId, course.printableName, competences.asJava);
    return courseData;
  }

}