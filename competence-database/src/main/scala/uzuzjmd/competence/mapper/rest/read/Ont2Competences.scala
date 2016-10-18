package uzuzjmd.competence.mapper.rest.read

import java.util

import com.google.common.collect.Lists
import uzuzjmd.competence.persistence.dao.Competence
import uzuzjmd.competence.persistence.neo4j.CompetenceNeo4jQueryManagerImpl
import uzuzjmd.competence.shared.competence.CompetenceFilterData
import uzuzjmd.competence.util.LanguageConverter

/**
  * Created by dehne on 12.04.2016.
  */
object Ont2Competences extends LanguageConverter{
  val qManager = new CompetenceNeo4jQueryManagerImpl

  def convert(filter: CompetenceFilterData): java.util.HashSet[String] = {
    val nodesArray: util.List[util.ArrayList[String]] = qManager.getSubClassTriples(classOf[Competence].getSimpleName, filter)
    val result =  new util.HashSet[String]
    nodesArray.foreach(x=>result.addAll(x))
    result.remove("Kompetenz")
    result.remove("List()")
    return result
    //return qManager.getCompetences(filter)
    // TODO implement more efficient function
  }
}