package uzuzjmd.competence.datasource.csv


object CSVFilter {

  def catchwordString(catchwordString: String): Boolean = {
    return !catchwordString.equals("") && !catchwordString.trim().equals("") && !catchwordString.equals("Schlagwort")
  }

  def operatorString(catchwordString: String): Boolean = {
    return !catchwordString.equals("Operator")
  }
}