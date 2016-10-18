package uzuzjmd.competence.persistence.performance

/**
 * @author dehne
 */
abstract class AbstractTimer[INPUTTYPE, OUTPUTTYPE] {
  def convert(changes: INPUTTYPE): OUTPUTTYPE
}