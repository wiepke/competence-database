package uzuzjmd.competence.logging

import uzuzjmd.competence.persistence.abstractlayer.WriteTransactional

/**
  * A shortcut for a write transactional that provides logging
  * @tparam A
  */
trait LoggingWriteTransactional[A] extends WriteTransactional[A] with Logging {

  override def execute(f: TRANSACTIONAL, g: A) {
    logger.debug("Entering TDBWriteTransactional with function:" + "dummy" + " and Parameter:" + g.toString())
    super.execute(f, g)
    logger.debug("Leaving TDBWriteTransactional function:" + "dummy")
  }
}