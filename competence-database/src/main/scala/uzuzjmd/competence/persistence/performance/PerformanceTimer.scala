package uzuzjmd.competence.persistence.performance

import uzuzjmd.competence.logging.Logging

/**
 * @author dehne
 */
trait PerformanceTimer[INPUT, OUTPUT] extends Logging {

  def convertTimed(changes: INPUT): OUTPUT = {
    var result = null: Any
    logger.debug("starting timer for " + this.className)
    val startTime = System.currentTimeMillis()
    result = convert(changes)
    val timed = System.currentTimeMillis() - startTime
    logger.debug("It took " + timed + " Millisconds for " + this.className + " to convert")
    return result.asInstanceOf[OUTPUT]
  }

  def convert(changes: INPUT): OUTPUT

}