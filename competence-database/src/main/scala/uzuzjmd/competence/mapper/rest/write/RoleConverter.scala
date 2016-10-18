package uzuzjmd.competence.mapper.rest.write

import uzuzjmd.competence.persistence.dao.Role

/**
 * @author dehne
 */
trait RoleConverter {
  def convertRole(role: String): Role = {
    return Role.valueOf(role)
  }
}
