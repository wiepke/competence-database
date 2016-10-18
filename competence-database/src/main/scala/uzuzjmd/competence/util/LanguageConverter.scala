package uzuzjmd.competence.util


import java.util

import com.google.common.collect.Lists

import scala.collection.JavaConverters._
import scala.collection.mutable


/**
  * Created by dehne on 16.03.2016.
  */
trait LanguageConverter {

  def stuff(): Unit = {
    new util.LinkedList().asScala.asJava

  }

  implicit def language[T](input: Seq[T]) : java.util.List[T] = {
    return input.asJava
  }

  implicit def language4[T](input: java.util.List[T]) : mutable.Buffer[T] = {
    return input.asScala
  }

  implicit def language3[T](input: Seq[T]) : java.util.ArrayList[T] = {
    return Lists.newArrayList(input.asJava)
  }

  implicit def language2[T](input: java.util.ArrayList[T]) : mutable.Buffer[T] = {
    return input.asScala
  }
}
