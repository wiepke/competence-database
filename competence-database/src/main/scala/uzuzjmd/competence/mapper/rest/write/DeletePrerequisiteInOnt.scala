package uzuzjmd.competence.mapper.rest.write

import java.util.LinkedList
import uzuzjmd.competence.persistence.abstractlayer.WriteTransactional
import uzuzjmd.competence.persistence.dao.Competence
import uzuzjmd.competence.shared.competence.PrerequisiteData
import scala.collection.JavaConversions.asScalaBuffer

/**
 * @author jbe
 */

object DeletePrerequisiteInOnt extends WriteTransactional[PrerequisiteData] {
  def convert(changes: PrerequisiteData) {
    execute(convertDeletePrerequisiteInOnt _, changes)
  }

  def convertDeletePrerequisiteInOnt(changes: PrerequisiteData) {
    val competence = new Competence(changes.getPostCompetence);
    if (changes.getPrerequisiteCompetences == null || changes.getPrerequisiteCompetences.isEmpty()) {
      changes.setPrerequisiteCompetences(new LinkedList[String]());
      competence.getRequiredCompetencesAsArray.foreach { x => changes.addElementSelectedCompetences(x) }
    }
    changes.getPrerequisiteCompetences.foreach { x => competence.addNotRequiredCompetence(new Competence(x)) }
  }
}