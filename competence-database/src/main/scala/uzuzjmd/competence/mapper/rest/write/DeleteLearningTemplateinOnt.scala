package uzuzjmd.competence.mapper.rest.write

import uzuzjmd.competence.persistence.abstractlayer.WriteTransactional
import uzuzjmd.competence.persistence.dao.LearningProjectTemplate

/**
 * @author dehne
 */
object DeleteLearningTemplateinOnt extends WriteTransactional[String] {

  def convert(changes: String) {
    execute(convertHelper _, changes)
  }

  def convertHelper(changes: String) {
    val learningProjectTemplate = new LearningProjectTemplate(changes)
    learningProjectTemplate.delete
  }
}