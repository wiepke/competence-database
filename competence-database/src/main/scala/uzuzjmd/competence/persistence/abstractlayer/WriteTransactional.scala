package uzuzjmd.competence.persistence.abstractlayer

/**
  * @author dehne
  *
  *         ensures a write transaction
  */
trait WriteTransactional[A] {
  type TRANSACTIONAL = (A) => Unit
  type TRANSACTIONAL2 = () => Unit

  def execute(f: TRANSACTIONAL, g: A) {
    try {
      f(g)
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }

  def executeAll(f: Array[TRANSACTIONAL], g: A) {
    try {
      f.foreach(x=>x(g))
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }

  def executeNoParam(f: TRANSACTIONAL2) {
    try {
      f
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }

}
