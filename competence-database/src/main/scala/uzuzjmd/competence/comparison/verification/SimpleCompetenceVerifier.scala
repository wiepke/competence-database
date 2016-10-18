package comparison.verification

import edu.stanford.nlp.trees.GrammaticalRelation
import edu.stanford.nlp.trees.GrammaticalRelation.Language
import uzuzjmd.competence.comparison.analysis.{WordToStem, SentenceToNoun, SentenceToOperator}
import uzuzjmd.competence.comparison.synonyms.OpenThesaurusSynonymCreator
import uzuzjmd.competence.comparison.verification.{CompetenceVerifier, ValidSubjects, ValidOperators}

/**
  * Created by dehne on 14.03.2016.
  */
class SimpleCompetenceVerifier extends CompetenceVerifier{
  private var language: Language = null
  private var sentence: String = null

  def this(sentence: String, language: Language) {
    this()
    this.sentence = sentence
    this.language = language
  }

  @Override
  def isCompetence(sentence: String, language: GrammaticalRelation.Language): java.lang.Boolean = {
    this.sentence = sentence
    this.language = language
    return isCompetence2
  }


  def isCompetence2: Boolean = {
    return satisfiesCorrectSubjectRestriction && satisfiesCorrectVerbRestriction
  }

  /**
    * checks for valid operators
    */
  def satisfiesCorrectVerbRestriction: Boolean = {
    val g = SentenceToOperator.convertSentenceToFilteredElement _

    val set: Array[String] = ValidOperators.values().map(x => x.toString);
    return checkIfMatchesSet(g, set)
  }

  /**
    * checks if the subject is correct
    *
    * @return
    */
  def satisfiesCorrectSubjectRestriction: Boolean = {
    val f = SentenceToNoun.convertSentenceToFilteredElement _
    val set = ValidSubjects.values().map(x => x.toString)
    return checkIfMatchesSet(f, set)
  }

  def checkIfMatchesSet(f: (String) => List[String], set: Array[String]): Boolean = {
    val words: List[String] = f(sentence);
    if (words.isEmpty) {
      return false;
    }
    val stemmedWords = words.map(x => WordToStem.stemWord(x))
    val stemmedValidWords = set.map(x => WordToStem.stemWord(x))

    val synonyms: List[String] = words.map(x => OpenThesaurusSynonymCreator.getSynonyms(x)).flatten
    val stemmedSynonyms = synonyms.map(x => WordToStem.stemWord(x))

    val casea = !stemmedValidWords.intersect(stemmedWords).isEmpty
    val caseb = !stemmedValidWords.intersect(stemmedSynonyms).isEmpty
    val casec = !set.toList.intersect(words).isEmpty
    return  casea || caseb || casec

  }
}