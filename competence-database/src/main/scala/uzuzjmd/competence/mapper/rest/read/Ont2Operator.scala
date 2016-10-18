package uzuzjmd.competence.mapper.rest.read

import uzuzjmd.competence.persistence.abstractlayer.ReadTransactional
import uzuzjmd.competence.persistence.dao.Competence

/**
 * @author dehne
 */
object Ont2Operator extends ReadTransactional[String, String] {

  def convert(forCompetence: String): String = {
    return execute(getOperator, forCompetence)
  }

  def getOperator(forCompetence: String): String = {
    val competence = new Competence(forCompetence);
    val operators = competence.getOperators()
    var result: String = "";
    if (!operators.isEmpty) {
      result = operators.iterator().next().getDefinition();
    }
    return result;
  }
}
