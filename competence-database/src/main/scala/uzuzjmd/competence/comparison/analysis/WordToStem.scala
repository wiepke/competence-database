package uzuzjmd.competence.comparison.analysis

import org.tartarus.snowball.SnowballStemmer;

/**
 * @author dehne
 */
object WordToStem {
  def stemWord(input: String): String = {

    val stemClass = Class.forName("org.tartarus.snowball.ext." + "german" + "Stemmer");
    val stemmer = stemClass.newInstance().asInstanceOf[SnowballStemmer];
    stemmer.setCurrent(input);
    stemmer.stem();
    val result = stemmer.getCurrent();
    return result.substring(0, result.length()-1)
  }
}