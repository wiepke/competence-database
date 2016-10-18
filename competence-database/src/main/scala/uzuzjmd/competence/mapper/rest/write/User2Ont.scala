package uzuzjmd.competence.mapper.rest.write

import uzuzjmd.competence.persistence.abstractlayer.WriteTransactional
import uzuzjmd.competence.persistence.dao.{User, CourseContext}
import uzuzjmd.competence.shared.user.UserData

/**
 * @author dehne
 */
object User2Ont extends RoleConverter with WriteTransactional[UserData] {

  def convert(data: UserData) {
    execute(createUser _, data)
  }

  def createUser(data: UserData) {
    if (data.getRole() == null) {
      data.setRole("student")
    }
    val creatorRole = convertRole(data.getRole);
    val courseContext = new CourseContext(data.getCourseContext);
    courseContext.persist();
    val creator = new User(data.getUserId, creatorRole.toString, null, null, courseContext);
    creator.persist();
  }
}