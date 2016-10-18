package uzuzjmd.competence.service.rest

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.tartarus.snowball.SnowballStemmer
import uzuzjmd.competence.comparison.analysis.{SentenceToNoun, SentenceToOperator}
import uzuzjmd.competence.comparison.simple.mapper.SimpleCompetenceComparatorMapper
import uzuzjmd.competence.comparison.synonyms.OpenThesaurusSynonymCreator

@RunWith(classOf[JUnitRunner])
class SimpleVerbTest extends FunSuite with ShouldMatchers {

  def getTestList: List[String] = {
    "Ich kann den Einfluss meiner Biografie, meiner Werteund Normvorstellungen auf mein berufliches Handeln erkennen und reflektieren. " :: "Ich bin in der Lage, Entscheidungen zu treffen, deren mögliche Konsequenzen zu antizipieren und Verantwortung für mein Handeln zu übernehmen. " :: "Ich kann meine Arbeit sinnvoll planen, verfüge über effektive Arbeitstechniken, kann in meiner Arbeitsorganisation Prioritäten setzen und selbständig arbeiten." :: "Ich kann verschiedene berufliche Handlungen (z.B. informieren, beraten, begleiten, befähigen, verhandeln, konfrontieren) situationsgerecht ausführen und reflektieren." :: "Ich kann Probleme aus der Perspektive der AdressatInnen erkennen und verstehen, mich aber auch abgrenzen. " :: "Ich kann berufliche  Beziehungen zu AdressatInnen unabhängig von deren Herkunft, gesellschaftlicher Stellung sowie mir fremder Werte- und Normvorstellungen aufbauen und gestalten. " :: "Ich kann verschiedene Gesprächstechniken situationsadäquat anwenden. " :: "Ich kann (Beratungs-) Gespräche selbständig vorbereiten, durchführen und auswerten. " :: "Ich kann für die mir übertragenen Aufgaben selbständig eine Problem- und Ressourcenanalyse durchführen, kann Prioritäten setzen, Ziele formulieren und vereinbaren, Lösungsstrategien entwickeln und meine Tätigkeit reflektieren. " :: "Ich kenne Bedürfnisse und Ansprüche der AdressatInnen." :: "Ich verfüge über relevantes Fachwissen in Bezug auf die Themen/Problemlagen, mit denen die Praxisstelle hauptsächlich konfrontiert ist." :: Nil
  }

  test("given a sentence, the verb should be returned") {
    val input = "Die Lehramtsstudenten der alten Schule sind in der Lage komplexe Texte zu verstehen und gehen gerne baden ."
    SentenceToOperator.convertSentenceToFilteredElement(input) should not be ('empty)

    val input2 = "Es dient der Verstärkung und dem Ausbau der Betreuung und Beratung von Studierenden der Filmregie im Hauptstudium.";
    SentenceToOperator.convertSentenceToFilteredElement(input2) should not be ('empty)
    //print(SentenceToVerb.convertSentenceToVerbs(input))
  }

  test("given a sentence, the subject should be returned") {
    val input = "Die Lehramtsstudenten der alten Schule sind in der Lage komplexe Texte zu verstehen und gehen gerne baden ."
    SentenceToNoun.convertSentenceToFilteredElement(input) should not be ('empty)



  }

  test("given a word, the stemmed version should be returned") {

    val result = stemWord("verstehen")
    val result2 = stemWord("verstehst")

    (result.equals(result2)) should not be false
  }

  def stemWord(input: String): String = {

    val stemClass = Class.forName("org.tartarus.snowball.ext." + "german" + "Stemmer");
    val stemmer = stemClass.newInstance().asInstanceOf[SnowballStemmer];
    stemmer.setCurrent(input);
    stemmer.stem();
    val result = stemmer.getCurrent();
    return result
  }

  test("given a word, all synonyms should be returned") {
    val result = OpenThesaurusSynonymCreator.getSynonyms("verstehen");
    result.contains("begreifen") should not be false
  }

  test("given a word, similar words should be returned") {
    val result = OpenThesaurusSynonymCreator.getSimilarWords("Atomenerg");
    result.contains("Atomenergie") should not be false
  }

  test("given a sentence with similar verbs, similar = true should be returned ith strat 1") {

    val input1 = "Die Lehramtsstudenten lernen, programmieren zu können"
    val input2 = "Die Lehramtsstudenten begreifen durch die Lehre, dass ein Informatiker entwickeln kann"

    val comparator = new SimpleCompetenceComparatorMapper

    val result = comparator.isSimilarVerbsStrategy1(input1, input2)
    result should not be false

  }

  test("given a sentence with similar verbs, similar = true should be returned with strat 2") {

    val input1 = "Die Lehramtsstudenten versteht"
    val input2 = "Ein Lehramtsstudenten verstehen"

    val comparator = new SimpleCompetenceComparatorMapper

    val result = comparator.isSimilarVerbsStrategy2(input1, input2)
    result should not be false

  }

  test("given a sentence with similar verbs, similar = true should be returned with strat 2- case 2") {

    val input1 = "Ein Lehramtsstudent begreift"
    val input2 = "Ein Lehramtsstudenten versteht"

    val comparator = new SimpleCompetenceComparatorMapper

    val result = comparator.isSimilarVerbsStrategy2(input1, input2)
    result should not be false

    val result2 = comparator.isSimilarVerbsStrategy1(input1, input2)
    result2 should not be true

  }

  test("given a sentence with similar nouns, similar = true should be returned with strat 1") {

    val input1 = "Ein Lehramtsstudent begreift"
    val input2 = "Ein Lehramtsstudenten versteht"

    val comparator = new SimpleCompetenceComparatorMapper

    val result = comparator.isSimilarCatchwordStrategy2(input1, input2)
    result should not be false

    val result2 = comparator.isSimilarCatchwordStrategy1(input1, input2)
    result2 should not be true
  }

  test("given a sentence with similar nouns or verbs, similar = true should be returned with strat 1") {

    val input1 = "Ein Lehramtsstudent begreift"
    val input2 = "Ein Lehramtsstudenten versteht"

    val comparator = new SimpleCompetenceComparatorMapper

    val result = comparator.isSimilarStrings(input1, input2)
    result should not be false

  }

  //39:55
  //  test("given a set of competencies from a domain, the relatedness should be of high percentage") {
  //    val list = getTestList.combinations(2).toList
  //    val comparator = new SimpleCompetenceComparatorMapper
  //    //println("similar count: " + list.map { x => comparator.isSimilarStrings2(x.head, x.last) }.count { x => x == true })
  //    //println("total number of pairs is: " + list.length)
  //
  //    list.map(x => (comparator.isSimilarStrings2(x.head, x.last), x.head, x.last)).foreach(x=> print("##########\n"+ x._1 + "\n " + x._2 + " \n"+ x._3+ "\n##########\n"))
  //  }
}