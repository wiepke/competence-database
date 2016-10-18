package uzuzjmd.competence.logging

import org.apache.log4j.LogManager
import uzuzjmd.competence.persistence.abstractlayer.ClassNameAdder

/**
 * @author dehne
  *         adds logger to a given scala class without having to specify the class name
 */
trait Logging {
  implicit protected def anyref2classnameadder(ref: AnyRef) = new ClassNameAdder(ref: AnyRef)
  protected val logger = LogManager.getLogger(this.className)
}