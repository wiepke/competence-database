package uzuzjmd.competence.comparison.simple.mapper

import uzuzjmd.competence.comparison.analysis.SentenceToOperator
import uzuzjmd.competence.comparison.simple.SimilarExplanations
import uzuzjmd.competence.comparison.synonyms.OpenThesaurusSynonymCreator
import uzuzjmd.competence.comparison.analysis.WordToStem
import uzuzjmd.competence.comparison.analysis.SentenceToNoun

/**
  * @author dehne
  */
class SimpleCompetenceComparatorMapper() {

  var strategy1Explanation = SimilarExplanations.NONE
  var strategy2Explanation = SimilarExplanations.NONE

  def isSimilarStrings(input1: String, input2: String): Boolean = {
    return isSimilarVerbsStrategy1(input1, input2) || isSimilarVerbsStrategy2(input1, input2) || isSimilarCatchwordStrategy1(input1, input2) || isSimilarCatchwordStrategy2(input1, input2)
  }

  def isSimilarStrings2(input1: String, input2: String): Boolean = {
    return (isSimilarVerbsStrategy1(input1, input2) || isSimilarVerbsStrategy2(input1, input2)) && (isSimilarCatchwordStrategy1(input1, input2) || isSimilarCatchwordStrategy2(input1, input2))
  }

  def computeSimilarityScore(input1: String, input2: String): Double = {

    var result = 0.0;

    var a = 1.0;
    var b = 1.0;

    if (isSimilarVerbsStrategy1(input1, input2)) {
      a = a * 1.5;
    }

    if (isSimilarVerbsStrategy2(input1, input2)) {
      a = a * 1.5
    }
    if (isSimilarCatchwordStrategy1(input1, input2)) {
      b = b * 1.3;
    }

    if (isSimilarCatchwordStrategy2(input1, input2)) {
      b = b * 1.3;
    }
    result = a * b;
    return result
  }

  def isSimilarCatchwordStrategy1(input1: String, input2: String): Boolean = {
    return isSimilarStrategyHelper(input1, input2, SentenceToNoun.convertSentenceToFilteredElement, isSimilarStrat1_Words)
  }


  def isSimilarCatchwordStrategy2(input1: String, input2: String): Boolean = {
    return isSimilarStrategyHelper(input1, input2, SentenceToNoun.convertSentenceToFilteredElement, isSimilarStrat2_Words)
  }

  /**
    * Simple Conmparison using stemming
    */
  def isSimilarVerbsStrategy2(input1: String, input2: String): Boolean = {
    return isSimilarStrategyHelper(input1, input2, SentenceToOperator.convertSentenceToFilteredElement, isSimilarStrat2_Words)
  }

  /**
    * Simple Comparison without stemming
    */
  def isSimilarVerbsStrategy1(input1: String, input2: String): Boolean = {
    return isSimilarStrategyHelper(input1, input2, SentenceToOperator.convertSentenceToFilteredElement, isSimilarStrat1_Words)
  }

  private def isSimilarStrategyHelper(input1: String, input2: String, f: (String => List[String]), g: (List[String], List[String]) => Boolean): Boolean = {
    val result = f(input1)
    val result2 = f(input2)
    return g(result, result2)
  }

  private def isSimilarStrat1_Words(result: List[String], result2: List[String]): Boolean = {
    // check if same
    if (!result.intersect(result2).isEmpty) {
      strategy1Explanation = SimilarExplanations.VERBSEQUAL
      return true
    }

    // check if sql like between operators or similar words
    val similarWords = result.map(OpenThesaurusSynonymCreator.getSimilarWords).flatten
    val similarWords2 = result2.map(OpenThesaurusSynonymCreator.getSimilarWords).flatten

    if (!similarWords.intersect(similarWords2).isEmpty) {
      strategy1Explanation = SimilarExplanations.VERBSSQLLIKE
      return true
    }

    // check if synonyms
    val synonyms = result.map(OpenThesaurusSynonymCreator.getSynonyms).flatten
    val synonyms2 = result2.map(OpenThesaurusSynonymCreator.getSynonyms).flatten

    if (!synonyms.intersect(synonyms2).isEmpty) {
      strategy1Explanation = SimilarExplanations.VERBSSYNONYMS
      return true
    }

    return false
  }

  private def isSimilarStrat2_Words(input1: List[String], input2: List[String]): Boolean = {
    val stemmedResult = input1.mapConserve(WordToStem.stemWord)
    val stemmedResult2 = input2.mapConserve(WordToStem.stemWord)

    //check if same
    if (!stemmedResult.intersect(stemmedResult2).isEmpty) {
      strategy2Explanation = SimilarExplanations.VERBSEQUAL
      return true
    }

    // check similarWords
    val similarStemmedResult = stemmedResult.map(OpenThesaurusSynonymCreator.getSimilarWords).flatten
    val similarStemmedResult2 = stemmedResult2.map(OpenThesaurusSynonymCreator.getSimilarWords).flatten

    if (!similarStemmedResult.intersect(similarStemmedResult2).isEmpty) {
      strategy2Explanation = SimilarExplanations.VERBSSQLLIKE
      return true
    }

    // check similarWordsInCase of stemming and similars
    val similarStemmedResultSyonomys = similarStemmedResult.map(OpenThesaurusSynonymCreator.getSynonyms).flatten
    val similarStemmedResultSyonomys2 = similarStemmedResult2.map(OpenThesaurusSynonymCreator.getSynonyms).flatten
    if (!similarStemmedResultSyonomys.intersect(similarStemmedResultSyonomys2).isEmpty) {
      strategy2Explanation = SimilarExplanations.VERBSSYNONYMS
      return true
    }

    return false
  }
}