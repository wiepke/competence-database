package uzuzjmd.competence.mapper.rest.read

import uzuzjmd.competence.logging.Logging
import uzuzjmd.competence.persistence.dao.Competence
import uzuzjmd.competence.persistence.ontology.Edge

import scala.collection.mutable.Buffer

/**
  * Mapper class for converting model to grid layout based on the suggested competence relationship
  */
object Ont2SuggestedCompetencyGridMapper extends Logging {

  type ComPairList = Buffer[(Competence, Competence)]

  def convertListToSuggestedCompetenceTriples(input: Buffer[Competence]): ComPairList = {
    val pairs = input.combinations(2)
    val pairsTMP = pairs.map(x => (x.head, x.last))
    val filteredResult = pairsTMP.map(reorderPairs).filter(Ont2SuggestedCompetencyGridFilter.filterIsSuggestedCompetency).toBuffer
    logger.trace("combinations after filtering:")
    logger.trace(Ont2SuggestedCompetenceGrid.compareListToString(filteredResult))
    return filteredResult
  }

  def reorderPairs(pair: (Competence, Competence)): (Competence, Competence) = {
    if (pair._2.hasEdge(Edge.SuggestedCompetencePrerequisiteOf, pair._1)) {
      return (pair._2, pair._1)
    } else {
      return pair
    }
  }

  def convertListToSuggestedCompetenceTriplesInverse(input: ComPairList): Buffer[Competence] = {
    val result = input.map(x => x._1)
    result.append(input.last._2)
    return result
  }
}