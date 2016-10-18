package uzuzjmd.competence.persistence.validation

import uzuzjmd.competence.persistence.dao.Competence

import scala.collection.JavaConverters._

case class CompetenceGraphValidator(addedCompetence: Competence, superCompetences: java.util.List[Competence], subCompetences: java.util.List[Competence]) {
  var explanationText = "ok!"

  def isValid(): Boolean = {
    return superCompetencesShouldNotIntersectSubCompetences && checkSuperCompetenceAreNotSubCompetencesOfSubCompetences &&
      checkAddedIsNotSubCompetenceOfSubCompetences &&
      checkAddedIsNotSuperCompetenceOfSuperCompetences
  }

  def superCompetencesShouldNotIntersectSubCompetences(): Boolean = {
    val intersection = superCompetences.asScala.map(x => x.getDefinition).intersect((subCompetences.asScala.map(x => x.getDefinition)))
    if (!intersection.isEmpty) {
      explanationText = "Oberkompetenzen und Unterkompetenzen dürfen sich nicht überschneiden:        ";
      intersection.foreach(x => explanationText += "#" + x + "#" + "       ")
      return false;
    }
    return true;

  }

  def checkAddedIsNotSuperCompetenceOfSuperCompetences(): Boolean = {
    if (!addedCompetence.exists) {
      return true
    }

    return superCompetences.asScala.forall(checkAddedIsNotSuperCompetenceOfSuperCompetencesHelper(_)(addedCompetence))
  }

  def checkAddedIsNotSuperCompetenceOfSuperCompetencesHelper(superCompetences: Competence)(addedCompetence: Competence): Boolean = {
    if (addedCompetence.isSuperClassOf(superCompetences)) {
      val errorIntroText = "Die Kompetenz" + addedCompetence.getDefinition + "ist eine Oberkompetenz der ausgewählten Oberkompetenzen. Folgender Pfad stellt das Problem dar:        "
      val badPath = addedCompetence.getShortestPathToSubCompetence(superCompetences).asScala.map(_.getDefinition).reduce((a, b) => "#" + a + "#" + " ist Unterkompetenz von " + "#" + b + "#" + "       ")
      explanationText = errorIntroText + badPath
      return false
    }
    return true
  }

  def checkAddedIsNotSubCompetenceOfSubCompetences(): Boolean = {
    if (!addedCompetence.exists) {
      return true
    }

    return subCompetences.asScala.forall(checkAddedIsNotSubCompetenceOfSubCompetencesHelper(_)(addedCompetence))
  }

  def checkAddedIsNotSubCompetenceOfSubCompetencesHelper(subCompetence: Competence)(addedCompetence: Competence): Boolean = {

    if (addedCompetence.isSubClassOf(subCompetence)) {
      val errorIntroText = "Die Kompetenz #" + addedCompetence.getDefinition + "# ist eine Unterkompetenz der ausgewählten Unterkompetenzen. Folgender Pfad stellt das Problem dar:        "
      val badPath = subCompetence.getShortestPathToSubCompetence(addedCompetence).asScala.map(_.getDefinition).reduce((a, b) => "#" + a + "#" + " ist Unterkompetenz von " + "#" + b + "#" + "       ")
      explanationText = errorIntroText + badPath
      return false
    }
    return true
  }

  def checkSuperCompetenceAreNotSubCompetencesOfSubCompetences(): Boolean = {
    return superCompetences.asScala.forall(x => subCompetences.asScala.forall(y => checkSuperCompetenceAreNotSubCompetencesOfSubCompetencesHelper(x, y)))
  }

  def checkSuperCompetenceAreNotSubCompetencesOfSubCompetencesHelper(superCompetence: Competence, subCompetence: Competence): Boolean = {
    if (superCompetence.isSubClassOf(subCompetence)) {
      val errorIntroText = "Die Kompetenz: #" + superCompetence.getDefinition + "# ist eine Unterkompetenz der ausgewählten Unterkompetenzen. Folgender Pfad stellt das Problem dar:        "
      val badPath = subCompetence.getShortestPathToSubCompetence(superCompetence).asScala.map(_.getDefinition).reduce((a, b) => "#" + a + "#" + " ist Unterkompetenz von " + "#" + b + "#" + "       ")
      explanationText = errorIntroText + badPath
      return false
    }
    return true
  }

  def getExplanationPath(): String = {
    return explanationText
  }

}