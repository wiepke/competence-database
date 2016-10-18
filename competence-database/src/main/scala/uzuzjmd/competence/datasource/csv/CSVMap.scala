package uzuzjmd.competence.datasource.csv

import net.htmlparser.jericho.{Renderer, Segment, Source}

/**
 * Diese Klasse bereinigt die Daten in der Exceltabelle
 */
object CSVMap {
  def competenceBeansToFilteredCSVCompetences(competenceBeans: CompetenceBean): FilteredCSVCompetence = {
    return new FilteredCSVCompetence(
      competenceBeans.getCompetence(),
      competenceBeans.getCatchword().split(",").toList,
      competenceBeans.getOperator(),
      competenceBeans.getMetaoperator(),
      competenceBeans.getEvidenzen(),
      competenceBeans.getCompetenceArea().split("##").toList,
      "Die Lehramtsanwärter",
      competenceBeans.getSuperCompetence())
  }

  /**
   * Leerzeichen entfernen
   */
  def cleanCatchwords(catchword: String): String = {
    return catchword.replaceAll(" ", "");
  }

  /**
   * Es soll nur einen Operator geben und keine Leerzeichen
   */
  def cleanOperator(catchword: String): String = {
    return catchword.split(",").head.replaceAll(" ", "");
  }

  /**
   * Diese Methode eleminiert HTML Tags
   */
  def cleanHTML(htmlText: String): String = {
    val htmlSource = new Source(htmlText);
    val htmlSeg = new Segment(htmlSource, 0, htmlText.length());
    val htmlRend = new Renderer(htmlSeg);
    return htmlRend.toString();
  }

}

//map(comptenzgefiltered => comptenzgefiltered.catchwordsFiltered.filter(catchword => !catchword.trim().equals("")).