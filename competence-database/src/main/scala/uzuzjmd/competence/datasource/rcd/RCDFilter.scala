package uzuzjmd.competence.datasource.rcd

import uzuzjmd.competence.persistence.ontology.Edge

object RCDFilter {
  /**
   * // Titel, Statementname, Statementtext bzw.
   *  _1 KompetenzIndividual _2 (Title), ObjectProperty, _3 Individual related to that ObjectProperty i.e. OperatorIndividual to OperatorOf
   */
  type CompetenceTriple = (String, String, String)
  /**
   *   suboperatortriples: x._1 competence, x._2 operator,  x._3 suboperator
   */
  type OperatorTriple = (String, String, String)
  
  /**
   *  suboperatortriples: x._1 competence,x._2 metacatchwords,  x._3 catchwords
   */
  type Catchwordtriple = (String, String, String)
  
  type CompetenceFilter = CompetenceTriple => Boolean

  def isObjectPropertyTriple(triple: CompetenceTriple): Boolean = {
    Edge.values.map(x => x.name()).contains(triple._2)
  }

  def isSubClassTriple(triple: CompetenceTriple): Boolean = {
    Edge.subClassOf.equals(Edge.valueOf(triple._2))
  }

  def isSubOperatorTriple(triple: CompetenceTriple): Boolean = {
    Edge.subClassOf.equals(Edge.valueOf(triple._2))
  }

  def isMetaCatchwordOfTriple(triple: CompetenceTriple): Boolean = {
    Edge.subClassOf.equals(Edge.valueOf(triple._2)) || Edge.subClassOf.equals(Edge.valueOf(triple._2))
  }



  def isTripleWithBlanc(triple: CompetenceTriple): Boolean = {
    triple match {
      //      case (null, egal, egal2) => return false
      //      case (egal, null, egal2) => return false
      //      case (egal, egal2, null) => return false
      case ("", egal, egal2) => return true
      case (egal, "", egal2) => return true
      case (egal, egal2, "") => return true
      case (egal, egal2, egal3) => return false
    }
  }

}