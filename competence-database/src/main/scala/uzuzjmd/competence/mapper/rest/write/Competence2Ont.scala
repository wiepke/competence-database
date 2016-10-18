package uzuzjmd.competence.mapper.rest.write

import java.util.LinkedList

import uzuzjmd.competence.exceptions.{CatchwordNotGivenException, OperatorNotGivenException}
import uzuzjmd.competence.persistence.abstractlayer.WriteTransactional
import uzuzjmd.competence.persistence.dao.{Catchword, Competence, LearningProjectTemplate, Operator}
import uzuzjmd.competence.persistence.ontology.Edge
import uzuzjmd.competence.persistence.validation.CompetenceGraphValidator
import uzuzjmd.competence.shared.competence.CompetenceData

import scala.collection.JavaConverters.asScalaBufferConverter

/**
 * @author dehne
 */
object Competence2Ont extends WriteTransactional[CompetenceData] {

  def convert(data: CompetenceData): String = {
    if (data.getCatchwords == null ||data.getCatchwords.isEmpty) {
        throw new CatchwordNotGivenException
    }

    if (data.getOperator == null) {
      throw new OperatorNotGivenException
    }
    addCompetence(data)
  }

  def addCompetence(data: CompetenceData): String = {
    val addedCompetence = new Competence(data.getForCompetence);
    val superCompetencesTyped = new LinkedList[Competence]();
    for (competence <- data.getSuperCompetences.asScala) {
      val superCompetence = new Competence(competence);
      superCompetence.persist();
      superCompetencesTyped.add(superCompetence);


    }
    val subCompetencesTyped = new LinkedList[Competence]();
    for (competence <- data.getSubCompetences.asScala) {
      val subCompetence = new Competence(competence);
      subCompetence.persist()
      subCompetencesTyped.add(subCompetence);

    }
    val competenceGraphValidator = new CompetenceGraphValidator(addedCompetence, superCompetencesTyped, subCompetencesTyped);
    if (competenceGraphValidator.isValid()) {
      addedCompetence.persist()
      for (catchwordItem <- data.getCatchwords.asScala) {
        val catchword = new Catchword(catchwordItem);
        catchword.persist()
        catchword.createEdgeWith(Edge.CatchwordOf, addedCompetence);

      }
      if (data.getLearningProjectName != null) {
        val learningProjectTemplate = new LearningProjectTemplate(data.getLearningProjectName);
        addedCompetence.addLearningTemplate(learningProjectTemplate);
      }
      if (data.getOperator != null) {
        val operatorDAO = new Operator(data.getOperator)
        operatorDAO.persist()
        addedCompetence.createEdgeWith(operatorDAO, Edge.OperatorOf)
      }
      for (subCompetence <- subCompetencesTyped.asScala) {
        subCompetence.addSuperCompetence(addedCompetence);
      }
      for (superCompetence <- superCompetencesTyped.asScala) {
        addedCompetence.addSuperCompetence(superCompetence);
      }
    }
    val resultMessage = competenceGraphValidator.getExplanationPath();
    return resultMessage;
  }

}