package uzuzjmd.competence.mapper.rest.write

import uzuzjmd.competence.exceptions.UserNotExistsException
import uzuzjmd.competence.persistence.abstractlayer.WriteTransactional
import uzuzjmd.competence.persistence.dao.{AbstractEvidenceLink, Comment, User}
import uzuzjmd.competence.shared.activity.CommentData

/**
 * @author dehne
 */
object Comment2Ont extends RoleConverter with WriteTransactional[CommentData] {

  def convert(data: CommentData) {
    execute(createComment _, data)
  }

  def createComment(data: CommentData) {
    val creator2 = new User(data.getUser);
    if (!creator2.exists()) {
      throw new UserNotExistsException(creator2);
    }
    val abstractEvidenceLink = new AbstractEvidenceLink(data.getLinkId)
    val comment = new Comment(data.getText, creator2, System.currentTimeMillis());
    //if (data.getLinkId != null && abstractEvidenceLink.exists()) {
      comment.persist();
      abstractEvidenceLink.linkComment(comment);
    //}

  }
}