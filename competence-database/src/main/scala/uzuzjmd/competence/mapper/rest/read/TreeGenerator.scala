/*
package uzuzjmd.competence.mapper.rest.read

import java.util

import datastructures.trees.{Node, TreePair}
import uzuzjmd.competence.logging.Logging

import scala.collection.JavaConverters._

/**
  * Created by dehne on 22.01.2016.
  */
object TreeGenerator extends Logging{
  def getTree(nodesArray3: util.List[TreePair], rootLabel : String) : Node = {
    val rootNode = new Node(rootLabel)
    val result =  getTree(nodesArray3.asScala.toList,scala.collection.mutable.HashSet.empty[Node], rootNode)
    return result
  }

  def getTree(leftNodes: List[TreePair], addedNodes: scala.collection.mutable.HashSet[Node], root: Node ): Node = {
     if(leftNodes.size > 0 ) {
       val elem = leftNodes.head
       val parent = new Node(elem.parent)
       val child = new Node(elem.child)
       if (parent.equals(root)) {
          root.children.add(child)
          addedNodes.add(child)
          return getTree(leftNodes.tail, addedNodes, root)
       } else {
          if (addedNodes.contains(parent)) {
            val parentInRootTree = findNodeInTree(root, parent)
            parentInRootTree.children.add(child)
            addedNodes.add(child)
            return getTree(leftNodes.tail, addedNodes, root)
          } else {
            addedNodes.add(child)
            return getTree(leftNodes ::: elem :: Nil , addedNodes, root)
            //logger.debug("unrooted elem found: "+ parent.id)
          }
       }
     }
    return root
  }

  def findNodeInTree(root: Node, toFind: Node): Node = {
    if (root.equals(toFind)) {
      return root;
    } else {
      if (root.children.isEmpty) {
        return null;
      } else {
        val childrenMapped = root.children.asScala.map(x => findNodeInTree(x, toFind)).filterNot(_ == null);
        if (childrenMapped.isEmpty) {
          return null;
        } else {
          return childrenMapped.head
        }
      }
    }

    return null
  }

}
*/
