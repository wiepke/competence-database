package uzuzjmd.competence.mapper.rest.read

import datastructures.graph.GraphNode
import uzuzjmd.competence.persistence.abstractlayer.ReadTransactional
import uzuzjmd.competence.persistence.dao.LearningProjectTemplate
import uzuzjmd.competence.shared.learningtemplate.LearningTemplateResultSet

/**
 * @author dehne
 */
object Ont2LearningTemplateResultSet extends ReadTransactional[String, LearningTemplateResultSet] {
  def convert(changes: String): LearningTemplateResultSet = {
    return execute(convertHelper _, changes)
  }

  private def convertHelper(changes: String): LearningTemplateResultSet = {
    val learningProjectTemplate = new LearningProjectTemplate(changes);
    val associatedCompetences = learningProjectTemplate.getAssociatedCompetences();

    if (associatedCompetences.isEmpty()) {
      val result = new LearningTemplateResultSet
      result.setNameOfTheLearningTemplate(changes);
      return result
    } else if (associatedCompetences.size() == 1) {
      val result = new LearningTemplateResultSet(new GraphNode(associatedCompetences.get(0).getDefinition()));
      result.setNameOfTheLearningTemplate(changes);
      return result
    } else {
      return Ont2SuggestedCompetenceGraph.getLearningTemplateResultSet(learningProjectTemplate);
    }
  }
}