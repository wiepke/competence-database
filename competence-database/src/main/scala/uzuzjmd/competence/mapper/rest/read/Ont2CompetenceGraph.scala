package uzuzjmd.competence.mapper.rest.read

import java.util

import datastructures.graph.{Graph, GraphFilterData}
import uzuzjmd.competence.logging.Logging
import uzuzjmd.competence.persistence.abstractlayer.ReadTransactional
import uzuzjmd.competence.persistence.dao.{Competence, CourseContext}
import uzuzjmd.competence.persistence.ontology.Edge

import scala.collection.JavaConverters._
import scala.collection.{SeqView, mutable}


object Ont2CompetenceGraph extends ReadTransactional[GraphFilterData, Graph] with Logging{

  def convert(changes:GraphFilterData): Graph = {
    return execute(getCompetenceGraphInternal _, changes)
  }

  def selectionFilter(x: Competence, changes:GraphFilterData): Boolean = {
    val competenceDefinition = x.getDefinition
    return changes.getCompetencesSelected.contains(competenceDefinition) || changes.getCompetencesSelected.isEmpty()
  }
  
  def getCompetenceGraphInternal(changes: GraphFilterData) : Graph = {
    val courseContext = new CourseContext(changes.getCourse)
    val competences = courseContext.getLinkedCompetences.asScala
    val competences2: mutable.Buffer[Competence] = competences.filter(x => x.isLinkedAsRequired || x.isLinkedAsSuggestedRequired)
    val competences3: mutable.Buffer[Competence] = competences2.filter(selectionFilter(_,changes))
    val competences4 = competences3.filter(selectionFilter(_,changes)).view
    logger.debug("Filtered Competences are: " + competences.toList)


    val relation1 = Edge.SuggestedCompetencePrerequisiteOf;
    val f1 : (Competence => util.HashSet[Competence] ) = _.getSuggestedCompetenceRequirements

    val relation2 = Edge.PrerequisiteOf
    val f2 : (Competence => util.List[Competence] ) = _.getRequiredCompetences

    val result = new Graph()
    convertCompetencesToTriples(competences4, f1, relation1, result)
    convertCompetencesToTriples(competences4, f2, relation2, result)

    logger.debug("triples are:" + result.triples)
    return result
  }


  def convertCompetencesToTriples(competences: SeqView[Competence, mutable.Buffer[Competence]], f1: (Competence) => util.Collection[Competence], relation: Edge, result:Graph): Unit = {
    competences.map(x => (x, f1.apply(x))).foreach(y => y._2.asScala.foreach(z => result.addTriple(z.getDefinition, y._1.getDefinition, relation.name(), true)))
  }
}