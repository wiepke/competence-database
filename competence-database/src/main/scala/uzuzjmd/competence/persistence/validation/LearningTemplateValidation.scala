package uzuzjmd.scompetence.owl.validation

import java.util.HashMap
import uzuzjmd.competence.logging.Logging
import uzuzjmd.competence.shared.learningtemplate.LearningTemplateResultSet
import scala.collection.JavaConverters._
import scala.collection.mutable.Stack

/**
 * @author dehne
 * implements Tarjans strongly connected components algorithm in order to identify cycles in graph
 * https://en.wikipedia.org/wiki/Tarjan%27s_strongly_connected_components_algorithm
 */
class LearningTemplateValidation(learningTemplate: LearningTemplateResultSet) extends Logging{

  val graph = learningTemplate.getResultGraph
  val vertices = graph.nodes.asScala.map { x => x.getLabel }
  //  val edgesMap = graph.triples.asScala.map { x => (x.toString(), x) }
  //  val edges = edgesMap.map(x => x._1)
  val edges = graph.triples.asScala.map { x => (x.fromNode, x.toNode) }

  var index = 0
  var S = new Stack[String]

  // contains the index for vertice [string]
  var indexMap = new HashMap[String, Integer]

  var lowLinkMap = new HashMap[String, Integer]
  var onStackMap = new HashMap[String, Boolean]

  vertices.foreach { x => onStackMap.put(x, false) }
  //  vertices.foreach { x => lowLinkMap.put(x, -1) }
  //  vertices.foreach { x => indexMap.put(x, 999) }

  var sccsResult = List.empty[List[String]]

  def isValid: Boolean = {

    logger.trace("stuff")

    logger.trace("edges are: " + edges)
    logger.trace("vertices are: " + vertices)

    // check self-reference
    if (!edges.forall(x => !x._1.equals(x._2))) {
      logger.trace("stuff1")
      return false
    }

    // to tarjan
    for (v <- vertices) {
      if (!indexMap.containsKey(v)) {
        strongConnect(v)
      }
    }
    val result = sccsResult.isEmpty
    logger.trace(sccsResult.toList)
    logger.trace("stuff2")
    return result
  }

  //  def getSCCs(): List[List[String]] = {
  //    return sccsResult
  //  }

  def strongConnect(v: String) {
    indexMap.put(v, index)
    lowLinkMap.put(v, index)
    index = index + 1
    S.push(v)
    onStackMap.put(v, true)

    for (x <- edges) {

      val a = v
      val w = x._2

      if (edges.contains(a, w)) {
        logger.trace(" edge is: " + v + w)
        //      val v = a._1
        //      val w = a._2

        if (!indexMap.containsKey(w)) {
          logger.trace("is not defined" + w)
          strongConnect(w)
          lowLinkMap.put(a, Math.min(lowLinkMap.get(a), lowLinkMap.get(w)))
        } else if (onStackMap.get(w)) {
          logger.trace("is defined and on stack" + w)
          lowLinkMap.put(a, Math.min(lowLinkMap.get(a), indexMap.get(w)))
        }
      }
    }

    // if v is a root node, pop the stakc and generate an SCC
    if (lowLinkMap.get(v).equals(indexMap.get(v))) {
      // start a new strongly connected component
      var scc = List.empty: List[String]
      var break = true
      while (break) {
        val w = S.pop()
        onStackMap.put(w, false)
        scc = w :: scc
        if (w.equals(v)) {
          break = false;
        }
      }

      //      logger.trace("scc is " + scc)
      if (scc.toList.size > 1) {
        sccsResult = scc :: sccsResult
      }
    }
  }
}
