package uzuzjmd.competence.persistence.abstractlayer

/**
 * @author dehne
 */
trait ReadTransactional[A, T] {
  type TRANSACTIONAL = A => T
  type TRANSACTIONAL2 = () => Unit

  def execute(f: TRANSACTIONAL, g: A): T = {
    try {
      return f(g)
    } catch { case e: Exception => e.printStackTrace() }
    return f(g)
  }

  def executeNoParam(f: TRANSACTIONAL2): Unit = {
    try {
      return f
    } catch { case e: Exception => e.printStackTrace() }
    return f
  }



}