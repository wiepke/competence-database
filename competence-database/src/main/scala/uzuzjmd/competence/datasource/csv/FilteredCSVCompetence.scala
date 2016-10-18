package uzuzjmd.competence.datasource.csv

import scala.collection.immutable.List
import scala.collection.mutable.Buffer

/**
  * Eine Zwischenklasse um die bereinigten Daten der Exceltabelle zu halten
  */
case class FilteredCSVCompetence(val competence: String, val catchwordsFiltered: List[String],
                                 val operator: String, val metaoperator: String, val evidencen: String,
                                 val metacatchwords: List[String], val learner: String, val supercompetence: String, val learningtemplate : String = null) {

}