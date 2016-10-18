package uzuzjmd.competence.mapper.rest.write

import uzuzjmd.competence.persistence.abstractlayer.WriteTransactional
import uzuzjmd.competence.persistence.dao._
import uzuzjmd.competence.shared.activity.EvidenceData
import scala.collection.JavaConverters.asScalaBufferConverter

/**
  * @author dehne
  */
object Evidence2Ont extends WriteTransactional[EvidenceData] with RoleConverter {

  def writeLinkToDatabase(data: EvidenceData) {
    execute(linkCompetencesToJson _, data)
  }

  def linkCompetencesToJson(data: EvidenceData) {
    if (data.getCreator == null) {
      data.setCreator(data.getEvidence.getUserId)
    }

    //val creatorRole = convertRole(data.getRole);
    val evidence = data.getEvidence
    for (competence <- data.getCompetences.asScala) {
      val courseContext = new CourseContext(data.getCourseId);
      val creatorUser = new User(data.getCreator);
      val linkedUserUser = new User(data.getEvidence.getUserId);
      val evidenceActivity = new EvidenceActivity(evidence.getUrl, evidence.getShortname);
      evidenceActivity.persist()
      val competenceDao = new Competence(competence);
      competenceDao.persist
      val abstractEvidenceLink = new AbstractEvidenceLink(creatorUser, linkedUserUser, courseContext, evidenceActivity, System.currentTimeMillis(), false, competenceDao,
        null);
      abstractEvidenceLink.persistMore();
      if (competenceDao.listSubClasses().isEmpty) {
        linkedUserUser.addCompetencePerformed(competenceDao)
      }
    }

  }

}