package uzuzjmd.competence.datasource.epos.mapper

import uzuzjmd.competence.datasource.epos.filter.LevelFilter
import uzuzjmd.competence.persistence.dao.{Competence, LearningProjectTemplate}
import uzuzjmd.competence.shared.epos.{DESCRIPTORSETType, DESCRIPTORType}

import scala.collection.JavaConverters._
import scala.collection.mutable

/**
  * Generates learning paths implied by levels in the epos xml format
  */
object EposXMLToSuggestedLearningPath {

  def convertLevelsToOWLRelations(descriptorSetType: java.util.List[DESCRIPTORSETType]) {
    descriptorSetType.asScala.foreach(x => convertLevelsToOWLRelations2(x.getDESCRIPTOR().asScala.toList, new LearningProjectTemplate(x.getNAME).persist().asInstanceOf[LearningProjectTemplate]))
    //descriptorSetType.asScala.foreach(x => createTemplateAssociation(x))
  }

  def convertLevelsAndLearningGoalToTemplate(descriptorSetType: java.util.List[DESCRIPTORSETType]) {
    descriptorSetType.asScala.foreach(x => createTemplateAssociation(x))
  }

  def createTemplateAssociation(x: DESCRIPTORSETType) {
    val templateName = x.getNAME()
    val competences: mutable.Buffer[Competence] = x.getDESCRIPTOR().asScala.map(EposXML2FilteredCSVCompetence.descriptorSetType2Id).map(x => new Competence(x, false).persist()).map(x => x.asInstanceOf[Competence])
    val learningProjectTemplate = new LearningProjectTemplate(templateName, competences.toList.asJava)
    learningProjectTemplate.persistMore()
  }

  def convertLevelsToOWLRelations2(descriptorSetType: List[DESCRIPTORType], learningProject: LearningProjectTemplate) {
    val result = descriptorSetType.map(x => (x.getCOMPETENCE, x.getLEVEL(), x.getNAME)).distinct.combinations(2)
    result.foreach(x => convertLevelToOWLRelations3((x.head._1, x.head._2,x.head._3), (x.tail.head._1, x.tail.head._2,x.tail.head._3), learningProject))
  }

  def convertLevelToOWLRelations3(domain: (String, String, String), range: (String, String, String), learningProject: LearningProjectTemplate) {
    if (LevelFilter.filterSuggestedLevels(domain._2, range._2) && domain._1.equals(range._1)) {
      val domainId = domain._3
      val rangeId = range._3
      val domainCompetence = new Competence(domainId, false)
      domainCompetence.persist()
      domainCompetence.addLearningTemplate(learningProject)
      val rangeCompetence = new Competence(rangeId, false)
      rangeCompetence.persist()
      rangeCompetence.addSuggestedCompetenceRequirement(domainCompetence)
      rangeCompetence.addLearningTemplate(learningProject)
    }
  }
}