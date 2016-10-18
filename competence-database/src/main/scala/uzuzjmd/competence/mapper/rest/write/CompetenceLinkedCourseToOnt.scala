package uzuzjmd.competence.mapper.rest.write

import uzuzjmd.competence.persistence.abstractlayer.WriteTransactional
import uzuzjmd.competence.shared.competence.CompetenceLinkedToCourseData

/**
 * @author dehne
 */
object CompetenceLinkedCourseToOnt extends WriteTransactional[CompetenceLinkedToCourseData]  {
  def convert(changes: CompetenceLinkedToCourseData) {
    throw new NotImplementedError()
  }
}