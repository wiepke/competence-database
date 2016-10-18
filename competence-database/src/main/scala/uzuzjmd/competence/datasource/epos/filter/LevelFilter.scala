package uzuzjmd.competence.datasource.epos.filter



object LevelFilter {

  def filterSuggestedLevels(descriptor1: String, descriptor2: String): Boolean = {
    return (descriptor1.equals("A1") && descriptor2.equals("A2")) ||
      (descriptor1.equals("A2") && descriptor2.equals("B1")) ||
      (descriptor1.equals("B1") && descriptor2.equals("B2")) ||
      (descriptor1.equals("B2") && descriptor2.equals("C1")) ||
      (descriptor1.equals("C1") && descriptor2.equals("C2"))

  }
}