package uzuzjmd.competence.mapper.rest.write

import uzuzjmd.competence.persistence.abstractlayer.WriteTransactional
import uzuzjmd.competence.persistence.dao.AbstractEvidenceLink

/**
 * @author dehne
 */
object AbstractEvidenceLink2Ont extends WriteTransactional[String] {

  def convert(changes: String) {
    execute(convertAbstractEvidenceLink _, changes)
  }

  def convertAbstractEvidenceLink(changes: String) {
    val abstractEvidenceLink = new AbstractEvidenceLink(changes);
    abstractEvidenceLink.delete;
  }
}