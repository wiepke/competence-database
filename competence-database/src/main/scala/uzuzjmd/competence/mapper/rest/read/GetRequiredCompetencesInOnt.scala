package uzuzjmd.competence.mapper.rest.read

import uzuzjmd.competence.persistence.abstractlayer.ReadTransactional
import uzuzjmd.competence.persistence.dao.Competence

/**
 * @author jbe
 */

object GetRequiredCompetencesInOnt extends ReadTransactional[String, Array[String]] {
  def convert(changes: String): Array[String] = {
    return execute(convertGetRequiredCompetencesInOnt _, changes)
  }

  def convertGetRequiredCompetencesInOnt( changes: String): Array[String] = {
    val competence = new Competence(changes);
    return competence.getRequiredCompetencesAsArray();
  }
}