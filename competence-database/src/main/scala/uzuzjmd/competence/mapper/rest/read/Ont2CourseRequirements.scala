package uzuzjmd.competence.mapper.rest.read

import uzuzjmd.competence.persistence.abstractlayer.ReadTransactional
import uzuzjmd.competence.persistence.dao.CourseContext

/**
 * @author dehne
 */
object Ont2CourseRequirements extends ReadTransactional[String, String] {

  def convert(changes: String): String = {
    return execute(convertHelper _, changes)
  }

  def convertHelper(changes: String): String = {
    val courseContext = new CourseContext(changes)
    return courseContext.getRequirement;
  }
}
