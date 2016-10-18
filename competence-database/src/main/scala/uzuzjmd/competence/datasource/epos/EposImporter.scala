package uzuzjmd.competence.datasource.epos

import uzuzjmd.competence.datasource.epos.mapper.{EposXML2FilteredCSVCompetence, EposXMLToSuggestedLearningPath}
import uzuzjmd.competence.persistence.abstractlayer.WriteTransactional
import uzuzjmd.competence.shared.epos.DESCRIPTORSETType

/**
 * @author dehne
 */

class EposImporter extends WriteTransactional[java.util.List[DESCRIPTORSETType]] {
  def importEposCompetences(eposList: java.util.List[DESCRIPTORSETType]) {
    EposXMLToSuggestedLearningPath.convertLevelsToOWLRelations(eposList)
    EposXMLToSuggestedLearningPath.convertLevelsAndLearningGoalToTemplate(eposList)
  }
}