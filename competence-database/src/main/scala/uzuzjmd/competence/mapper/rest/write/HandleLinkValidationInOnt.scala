package uzuzjmd.competence.mapper.rest.write

import uzuzjmd.competence.persistence.abstractlayer.WriteTransactional
import uzuzjmd.competence.persistence.dao.AbstractEvidenceLink
import uzuzjmd.competence.shared.activity.LinkValidationData

/**
* @author jbe
**/

object HandleLinkValidationInOnt extends WriteTransactional[LinkValidationData]{
  def convert(change:LinkValidationData) {
    execute(convertHandleLinkValidationInOnt _, change)
  }
  
  def convertHandleLinkValidationInOnt(changes: LinkValidationData) {
    val abstractEvidenceLink = new AbstractEvidenceLink(changes.getLinkId)
    abstractEvidenceLink.setValidated(changes.getIsValid)
    abstractEvidenceLink.persist()
  }
}