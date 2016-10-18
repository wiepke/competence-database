package uzuzjmd.competence.datasource.rcd

import uzuzjmd.competence.persistence.ontology.{Edge, Label}

object RCDMaps {
  type TripleToString = RCDFilter.CompetenceTriple => String
  type classToObjectProperty = Label => Edge

  def convertTriplesToOperators(input: RCDFilter.CompetenceTriple): RCDFilter.CompetenceTriple = {
    val values = Label.values.map(x => x.name())
    val value = input._2
    val contains = values.contains(value)
    if (contains) {
      return (input._1, classToObjectProperty(Label.valueOf(input._2)).name(), input._3)
    } else {
      return input
    }
  }

  def classToObjectProperty(compOntClass: Label): Edge = {
    compOntClass match {
      case Label.Catchword => return Edge.CatchwordOf
      case Label.Competence => return Edge.subClassOf
      case Label.Operator => return Edge.OperatorOf
      case Label.SubOperator => return Edge.subClassOf
      case Label.CourseContext => return Edge.CourseContextOfCompetence
      case default => return null
    }
  }

  def objectPropertyToClass(objectProperty: Edge): Label = {
    val classToObjectPropertyMap = Label.values.map(x => x -> classToObjectProperty(x))
    val objectToPropertyPairs = classToObjectPropertyMap.map(_.swap)
    val objectToPropertyMap = objectToPropertyPairs.toMap
    return objectToPropertyMap.get(objectProperty).head
  }

}