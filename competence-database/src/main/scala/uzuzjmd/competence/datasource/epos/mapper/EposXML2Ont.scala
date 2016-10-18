package uzuzjmd.competence.datasource.epos.mapper

import java.util

import uzuzjmd.competence.datasource.csv.FilteredCSVCompetence
import uzuzjmd.competence.persistence.abstractlayer.WriteTransactional
import uzuzjmd.competence.persistence.dao._
import uzuzjmd.competence.persistence.ontology.Edge
import uzuzjmd.competence.shared.epos.DESCRIPTORSETType
import collection.JavaConverters._

/**
 * @author dehne
  *         utility class to bundles different steps of persisting competences given in the epos format
 */
object EposXML2Ont extends WriteTransactional[java.util.List[DESCRIPTORSETType]] {

  def convert(changes: java.util.List[DESCRIPTORSETType]) {
    execute(convertHelper _, changes)
  }

  def convertHelper(changes: java.util.List[DESCRIPTORSETType]) {

    val filteredCSVCompetence: util.List[FilteredCSVCompetence] = EposXML2FilteredCSVCompetence.mapEposXML(changes)
    mapFilteredCSVCompetence2ont(filteredCSVCompetence)
    EposXMLToSuggestedLearningPath.convertLevelsToOWLRelations(changes)
    //EposXMLToSuggestedLearningPath.convertLevelsAndLearningGoalToTemplate(changes)
  }

  def mapFilteredCSVCompetence2ont(input : util.List[FilteredCSVCompetence]): Unit = {
     val scalaList = input.asScala
    scalaList.foreach(convertFilteredCSVCompetence _)

  }

  def convertFilteredCSVCompetence (input: FilteredCSVCompetence): Unit = {
      val competence = new Competence(input.competence)
      competence.persist()
      val catchwords: List[Dao] = input.catchwordsFiltered.map(x=>new Catchword(x).persist())
      catchwords.foreach(x=>competence.addCatchword(x.asInstanceOf[Catchword]))
      val operator = new Operator(input.operator)
      operator.persist()
      competence.createEdgeWith(operator, Edge.OperatorOf)
      val superCompetence =  new Competence(input.supercompetence)
      competence.addSuperCompetence(superCompetence)
      competence.addLearningTemplate(new LearningProjectTemplate(input.learningtemplate).persist().asInstanceOf[LearningProjectTemplate])
  }
}