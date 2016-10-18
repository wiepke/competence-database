package uzuzjmd.competence.mapper.rest.write

import uzuzjmd.competence.persistence.abstractlayer.WriteTransactional
import uzuzjmd.competence.persistence.dao.{LearningProjectTemplate, Role, User}
import uzuzjmd.competence.persistence.ontology.Edge
import uzuzjmd.competence.shared.learningtemplate.LearningTemplateData

/**
 * @author dehne
  *    Delete association user has with learningtemplate
  *    He has not chosen to learn this template after this.
 */
object DeleteTemplateInOnt extends WriteTransactional[LearningTemplateData] {

  def convert(changes: LearningTemplateData) {
    execute(convertHelper _, changes)
  }

  def convertHelper(changes: LearningTemplateData): Unit = {
    //val context = new CourseContext(changes.getGroupId);
    val user = new User(changes.getUserName);
    val learningTemplate = new LearningProjectTemplate(changes.getSelectedTemplate)
    learningTemplate.deleteEdgeWith(user, Edge.UserOfLearningProjectTemplate)
  }
}