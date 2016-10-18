package uzuzjmd.competence.mapper.rest.read

import java.util

import datastructures.trees.{TreeGenerator, AbstractXMLTree, Node, TreePair}
import uzuzjmd.competence.logging.Logging
import uzuzjmd.competence.persistence.dao.{Catchword, Competence, Operator}
import uzuzjmd.competence.persistence.neo4j.DBFactory
import uzuzjmd.competence.persistence.validation.TextValidator
import uzuzjmd.competence.shared._
import uzuzjmd.competence.shared.competence.{CompetenceFilterData, CompetenceXMLTree}

import scala.collection.JavaConverters._

object Ont2CompetenceTree extends Logging{

  val neo4jqueryManager = DBFactory.getDB;
  val iconRootPath = "nvm"
  val iconPathCompetence = iconRootPath + "/icons/competence.png"
  val iconPathOperator = iconRootPath + "/icons/filter.png"
  val iconPathCatchword = iconRootPath + "/icons/filter.png"


  /**
    * returns all the operators in the database as a tree
 *
    * @param filterData
    * @return
    */
  def getOperatorXMLTree(filterData: CompetenceFilterData): java.util.List[OperatorXMLTree] = {
    val competenceLabel = classOf[Operator].getSimpleName;
    val f = convertNodeXMLTree (classOf[OperatorXMLTree]) _
    // TODO implement filter
    return convertTree (competenceLabel, filterData, f(x=>true)(iconPathOperator))
  }

  /**
    * returns all the catchwords in the database as a tree
 *
    * @param filterData
    * @return
    */
  def getCatchwordXMLTree(filterData: CompetenceFilterData): java.util.List[CatchwordXMLTree] = {
    val competenceLabel = classOf[Catchword].getSimpleName;
    val f = convertNodeXMLTree (classOf[CatchwordXMLTree]) _
    // TODO implement filter
    return convertTree(competenceLabel, filterData, f(x=>true)(iconPathCatchword))
  }

  def getCompetenceTree(filterData: CompetenceFilterData): java.util.List[CompetenceXMLTree] = {
    val competenceLabel = classOf[Competence].getSimpleName;
    val f = convertNodeXMLTree (classOf[CompetenceXMLTree]) _
    return convertTree(competenceLabel, filterData, f(competenceNodeFilter (filterData)(_))(iconPathCompetence))
  }



  /**
    * filters the result from the database and converts it into pairs of the subclass triples
 *
    * @param competenceLabel
    * @param f
    * @tparam T
    * @return
    */
  def convertTree[T <: AbstractXMLTree[T]](competenceLabel: String, filterData: CompetenceFilterData, f: (Node) => T) : java.util.List[T] = {
    val nodesArray = neo4jqueryManager.getSubClassTriples(competenceLabel, filterData)
    val nodesArray2 = nodesArray.asScala.filterNot(_.isEmpty)
    val nodesArray3 = nodesArray2.map(x => new TreePair(x.get(1), x.get(0)))
      .asJava
    if (nodesArray3.isEmpty) {
      return new util.LinkedList[T]()
    }
    val rootNode = TreeGenerator.getTree(nodesArray3);
    //logger.debug(rootNode.toStrinz);
    return (f(rootNode) :: Nil).asJava
  }

  /**
    * converts a Node to a xml tree
 *
    * @param iconPath
    * @param node
    * @tparam T
    * @return
    */
  def convertNodeXMLTree[T <: AbstractXMLTree[T]] (clazz: Class[T])(nodeFilter : (String) => Boolean )(iconPath: String)(node : Node)  : T = {
    val result =  clazz.getConstructor(classOf[String], classOf[String], classOf[String], classOf[java.util.List[T]]).newInstance(node.id, "", iconPath, new util.LinkedList[T]());
    if (node.children != null && !node.children.isEmpty && nodeFilter(node.id)) {
      val children: java.util.List[T] = node.children.asScala.map(x => convertNodeXMLTree (clazz)(nodeFilter)(iconPath)(x)).map(x=>x.asInstanceOf[T]).asJava
      result.asInstanceOf[T].setChildren(children)
    } else {
      result.asInstanceOf[T].setChildren(new util.ArrayList[T]());
    }
    return result
  }

  def competenceNodeFilter(competenceFilterData : CompetenceFilterData)(input : String) :  java.lang.Boolean = {
     val textCorrect = TextValidator.isValidText(input,competenceFilterData.getTextFilter)
     return textCorrect
  }




}

