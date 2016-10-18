package uzuzjmd.competence.persistence.abstractlayer

import neo4j.Neo4JQueryManagerImpl
import uzuzjmd.competence.persistence.dao.Dao
import uzuzjmd.competence.persistence.neo4j.DBFactory
import uzuzjmd.competence.persistence.ontology.Edge

/**
  * Created by dehne on 03.12.2015.
  *
  * Simplifies the creation of a simple edge.
  */
trait ObjectPropertyCED[Domain <:Dao, Range <:Dao] extends WriteTransactional[( String, String)] {

  val domainClass =  setDomain;
  val rangeClass  =setRange;
  val edgeType = setEdge;


  protected def setEdge() : Edge
  protected def setDomain() :  java.lang.Class[Domain]
  protected def setRange() : java.lang.Class[Range]

  def write(domain: String, range: String): Unit = {
    execute(writeHelper _, (domain, range))
  }

  def writeHelper(tuple: ( String, String)): Unit = {
    val manager = DBFactory.getDB;
    manager.createRelationShip(tuple._1, edgeType, tuple._2, domainClass, rangeClass)
  }

  def delete(domain: String, range: String): Unit = {
    execute(deleteHelper _, (domain, range))
  }

  def deleteHelper(tuple: ( String, String)): Unit = {
    val manager = DBFactory.getDB;
    manager.deleteRelationShip(tuple._1, tuple._2, edgeType)
  }

}
