package uzuzjmd.competence.mapper.rest.read

import uzuzjmd.competence.persistence.dao.Competence
import uzuzjmd.competence.persistence.ontology.Edge

/**
  * Filter class for converting model to grid layout based on the suggested competence relationship
  */
object Ont2SuggestedCompetencyGridFilter {
  def filterIsSuggestedCompetency(pair: (Competence, Competence)): Boolean = {
    return pair._1.hasEdge(Edge.SuggestedCompetencePrerequisiteOf, pair._2)
  }

  def sortCompetencePairs(a: (Competence, Competence), b: (Competence, Competence)): Boolean = {
    return filterIsSuggestedCompetency((a._2, b._1)) || filterIsSuggestedCompetency((b._2, a._1))
  }
}