package uzuzjmd.competence.mapper.rest.read

import java.util.{HashMap, NoSuchElementException}

import datastructures.graph.{Graph, GraphTriple}
import uzuzjmd.competence.logging.Logging
import uzuzjmd.competence.persistence.dao.{Competence, LearningProjectTemplate}
import uzuzjmd.competence.shared.learningtemplate.LearningTemplateResultSet
import uzuzjmd.java.collections.MapsMagic

import scala.collection.JavaConverters._
import scala.collection.mutable

/**
  * @author dehne
  *         TODO rewrite
  */
object Ont2SuggestedCompetenceGraph extends Logging {

  implicit def listToString(input: List[String]): String = {
    return input.reduce((a, b) => "[" + a + " , " + b + "]")
  }

  def getLearningTemplateResultSet(learningProjectTemplate: LearningProjectTemplate): LearningTemplateResultSet = {
    val graph = getGraph(learningProjectTemplate)
    val hashMap = getHashMap(learningProjectTemplate, graph)
    val result = new LearningTemplateResultSet(graph, hashMap, learningProjectTemplate.getDefinition);
    return result
  }

  def getGraph(learningProjectTemplate: LearningProjectTemplate): Graph = {
    val result = new Graph
    val competences: mutable.Buffer[Competence] = learningProjectTemplate.getAssociatedCompetences().asScala
    // convert relations in 2-tuples
    val result1: mutable.Buffer[(Competence, Competence)] = competences.map { x => (x.getSuggestedCompetenceRequirements(), x) }.map(y => y._1.asScala.map { z=>(z, y._2)}).flatten
    // convert to triples
    result1.foreach(x => result.addTriple(x._1.getDefinition(), x._2.getDefinition(), learningProjectTemplate.getDefinition, true))
    return result
  }

  /** UNIT TEST expects triple : "using tags" , "using JSP tags") as one of the result keys; mit programming und catchwords **/
  def getHashMap(learningProjectTemplate: LearningProjectTemplate, graph: Graph): HashMap[GraphTriple, Array[String]] = {

    val result1 = Ont2SuggestedCompetenceGrid.convertToTwoDimensionalGrid1(learningProjectTemplate).mapValues { x => x.flatten }

    logger.trace("BEFORE INVERT")
    result1.foreach(x => logger.trace(" key: " + x._1 + ", values:" + x._2))
    val resultInverted = MapsMagic.invertAssociation(result1).map { case (key, value) => (key.getDefinition(), value) }.mapValues(x => x.map(y => y.getDefinition()))
    // TODO fix TEST 5 in UNIT Test of ANH

    val resultInvertedCleaned = resultInverted.keySet.toList.distinct
    val resultInvertedCleaned2 = resultInvertedCleaned.map(x => (x, resultInverted.get(x).toList.flatten.distinct)).toMap

    logger.trace("AFTER INVERT and CLEAN")
    resultInvertedCleaned2.foreach(x => logger.trace(" key: " + x._1 + ", values:" + x._2))

    logger.trace("before matching same catchword")
    logger.trace(graph.triples)

    // test if both parts of triple have same catchword
    val result2 = graph.triples.asScala.map(mapTripleToCommonCatchwords(_)(resultInvertedCleaned2)).filterNot(p => p._2.isEmpty).toMap

    logger.debug("AFTER matching same catchword")
    result2.foreach(x => logger.debug(x._1.toString() + x._2) + "\n")

    val result = new HashMap[GraphTriple, Array[String]]
    result.putAll(result2.mapValues { x => x.toArray }.asJava)
    return result
  }

  def mapTripleToCommonCatchwords(triple: GraphTriple)(checkMap: Map[String, List[String]]): (GraphTriple, Set[String]) = {
    try {
      val fromNodeCatchwords = checkMap.get(triple.fromNode).get.toSet
      val toNodeCatchwords = checkMap.get(triple.toNode).get.toSet

      val result = (triple, fromNodeCatchwords.intersect(toNodeCatchwords))
      val result2 = (result._1, result._2)
      return result2
    } catch {
      case ioe: NoSuchElementException =>
        print("nono") // more specific cases first !
        logger.error("triple: "+triple +"\n checkMap: "+ checkMap.toList.toString())
      case e: Exception => print("hjoho")
    }
    return null

  }

}
