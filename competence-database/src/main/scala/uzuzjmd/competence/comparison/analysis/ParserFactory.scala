package comparison.analysis

import config.MagicStrings
import edu.stanford.nlp.parser.lexparser.LexicalizedParser

/**
  * Created by dehne on 17.03.2016.
  */
object ParserFactory {
  val lp: LexicalizedParser = LexicalizedParser.loadModel(MagicStrings.GERMANMODELLOCATION, "-maxLength", "80");

  def instance() : LexicalizedParser ={
    LexicalizedParser.loadModel(MagicStrings.GERMANMODELLOCATION, "-maxLength", "80");
  }

  def getLP() : LexicalizedParser = {
    return LexicalizedParser.loadModel(MagicStrings.GERMANMODELLOCATION, "-maxLength", "80");
  }
}
