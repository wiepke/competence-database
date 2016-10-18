package uzuzjmd.java.collections

object MapsMagic {
  def invertAssociation[A, B](input: Map[A, List[B]]): Map[B, List[A]] = {

    return input.view.map(x => x.swap).
      map(x => x._1.map(y => (y -> x._2))).
      flatten.
      groupBy(x => x._1).
      map(x => (x._1, x._2.map(y => y._2).toList))
  }

  def invertMap[A, B](m: Map[A, B]): Map[B, List[A]] = {

    val k = ((m values) toList) distinct
    val v = k map { e => ((m keys) toList) filter { x => m(x) == e } }
    (k zip v) toMap
  }
}