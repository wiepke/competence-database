package uzuzjmd.competence.mapper.rest.read

import uzuzjmd.competence.persistence.abstractlayer.ReadTransactional
import uzuzjmd.competence.shared.course.CourseData
import uzuzjmd.competence.shared.progress.ProgressMap

/**
 * @author jbe
 */

object GetProgressMinOnt extends ReadTransactional[CourseData, ProgressMap] {
  def convert(changes: CourseData): ProgressMap = {
    return execute(convertGetProgressMInOnt _, changes)
  }

  def convertGetProgressMInOnt(changes: CourseData): ProgressMap = {
    val map = new Ont2ProgressMap(changes.getCourseId, changes.getCompetences);
    val result = map.getProgressMap();
    return result
  }
}