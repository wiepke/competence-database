package uzuzjmd.competence.comparison.analysis

import comparison.analysis.ParserFactory
import edu.stanford.nlp.ling.Sentence
import edu.stanford.nlp.parser.lexparser.LexicalizedParser
import edu.stanford.nlp.trees.Tree

import scala.collection.JavaConverters._

/**
  * @author dehne
  * SubObjects of this trait are filter to get a specific word class out of a sentence.
  */
trait SentenceAnalyser {


  def hitList = List("VVIZU", "VVINF", "VVFIN", "VVPP", "ADJD") // as an example all verbs

  /**
    *
    */
  def convertSentenceToFilteredElement(input: String): List[String] = {

    if (input == null || input.size < 20 || !input.contains(" ")) {
      return List.empty
    }

    //val tlp = new PennTreebankLanguagePack();
    // Uncomment the following line to obtain original Stanford Dependencies
    // tlp.setGenerateOriginalDependencies(true);
    //    val gsf = tlp.grammaticalStructureFactory();
    val sent = input.split(" ").toList.filterNot(_.length < 1).filterNot(_.length > 20)
    if (sent.size < 2) {
      return List.empty
    }

    val parse = ParserFactory.lp.apply(Sentence.toWordList(sent.asJava));
    //val parse =  ParserFactory.getLP().apply(Sentence.toWordList(sent.asJava));
    //    val gs = gsf.newGrammaticalStructure(parse);
    //    val tdl = gs.typedDependenciesCCprocessed();
    //    println(tdl.toString())
    val result = treeWalker(parse)
    return result
  }

  def convertSentenceToFilteredElementStemmed(input: String): List[String] = {
    return convertSentenceToFilteredElement(input).map { x => WordToStem.stemWord(x) }
  }


  /**
    * recursively walks the dependency tree in order to find all the words given the specified filter
    */
  def treeWalker(input: Tree): List[String] = {
    val startList = List(): List[String]
    return treeWalker_Helper(input, startList)
  }


  def treeWalker_Helper(input: Tree, startList: List[String]): List[String] = {
    if (hitList.forall(x => !input.nodeString().contains(x))) {
      return startList ::: input.children().map(x => treeWalker_Helper(x, startList)).flatten.toList
    } else {
      if (input.size() == 0) {
        return List.empty
      }
      val newList = input.getChild(0).toString() :: startList
      return startList ::: input.children().map(x => treeWalker_Helper(x, newList)).flatten.toList
    }

  }
}