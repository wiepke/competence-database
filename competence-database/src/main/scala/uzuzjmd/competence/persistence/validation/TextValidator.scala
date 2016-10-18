package uzuzjmd.competence.persistence.validation


import datastructures.trees.AbstractXMLTree

import scala.collection.JavaConverters._


/**
  * validates text
  */
object TextValidator {

  def purifyText[A <: AbstractXMLTree[A]](input: A, textFilter: String): List[A] = {
    if (!hasValidText(input, textFilter)) {
      return List.empty[A]
    } else {
      return purifyTextHelper(input, textFilter) :: Nil
    }
  }

  def purifyTextHelper[A <: AbstractXMLTree[A]](input: A, textFilter: String): A = {
    if (input.getChildren().isEmpty()) {
      return input
    } else {
      val filteredChildren = input.getChildren().asScala.filter(hasValidText(_, textFilter))
      input.getChildren().clear()
      val purifiedChildren = filteredChildren.map(purifyTextHelper(_, textFilter))
      input.setChildren(filteredChildren.map(purifyTextHelper(_, textFilter)).asJava)
    }
    return input
  }

  def isValid[A <: AbstractXMLTree[A]](input: A, textFilter: String): Boolean = {
    if (input.getChildren().isEmpty()) {
      if (isValidText(input.getName(), textFilter)) {
        return true
      } else {
        return false
      }
    } else {
      return input.getChildren().asScala.toList.forall(x => hasValidText(x, textFilter))
    }

  }

  def hasValidText[A <: AbstractXMLTree[A]](input: A, textFilter: String): Boolean = {
    if (input.getChildren().isEmpty()) {
      if (isValidText(input.getName(), textFilter)) {
        return true
      } else {
        return false
      }
    } else {
      return !input.getChildren().asScala.toList.filter(x => hasValidText(x, textFilter)).isEmpty
    }

  }

  def isValidText(input: String, textFilter: String): Boolean = {
    if (textFilter == null) {
      return true
    } else if (textFilter.trim().equals("")) {
      return true
    } else {
      return input.contains(textFilter.trim())
    }
  }
}