package uzuzjmd.competence.mapper.rest.read

import java.util

import datastructures.lists.StringList
import neo4j.Neo4JQueryManagerImpl
import uzuzjmd.competence.persistence.abstractlayer.ReadTransactional
import uzuzjmd.competence.persistence.neo4j.DBFactory
import uzuzjmd.competence.persistence.ontology.Label

/**
 * @author dehne
 */
object Ont2LearningTemplates extends ReadTransactional[Any, StringList] {
  def convert(): StringList = {
    return getLearningTemplates()
  }

  private def getLearningTemplates(): StringList = {
    val manager  = DBFactory.getDB;
    val tmp: util.List[String] = manager.getAllInstanceDefinitions(Label.LearningProjectTemplate)
    val learningTemplates = new StringList(tmp);
    return learningTemplates
  }
}
