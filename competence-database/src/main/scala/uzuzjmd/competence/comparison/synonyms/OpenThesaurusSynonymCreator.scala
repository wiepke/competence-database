package uzuzjmd.competence.comparison.synonyms

import config.MagicStrings
import mysql.MysqlConnect
import scala.collection.JavaConverters._

/**
 * @author dehne
  *        creates synonyms based on the openthesaurs library
 */
object OpenThesaurusSynonymCreator {
  def getSynonyms(input: String): List[String] = {

     val connector = new MysqlConnect
    connector.connect(createConnectionString)

    var result = List(): List[String]

    val resultSet = connector.issueSelectStatement("SELECT synset_id, word FROM term a WHERE a.word = ? LIMIT 30", input)

    
    while (resultSet.next()) {
      val resultInt = resultSet.getInt("synset_id"): java.lang.Integer;

      val resultSet2 = connector.issueSelectStatement("SELECT word, b.id FROM term a JOIN synset b on a.synset_id = b.id where b.id = ? LIMIT 30", resultInt);

      while (resultSet2.next()) {
        val currentWord = resultSet2.getString("word")
        result = currentWord  :: result
      }
    }

    connector.close()
    return result
  }

  def getSysnonymsAsJava(input: String) : java.util.List[String] = {
    getSynonyms(input).asJava
  }

  def getSimilarWords(input: String): List[String] = {

    val connector = new MysqlConnect
    connector.connect(createConnectionString)
    val resultSet = connector.issueSelectStatement("SELECT id, word FROM term a WHERE a.word like ? LIMIT 50", "%" + input + "%")

    var result = List(): List[String]
    while (resultSet.next()) {
      result = resultSet.getString("word") :: result
    }

    connector.close()
    return result
  }

  def createConnectionString: String = {
    return "jdbc:mysql://" + MagicStrings.thesaurusDatabaseUrl +
      "/" + MagicStrings.thesaurusDatabaseName +
      "?user=" + MagicStrings.thesaurusLogin +
      "&password=" + MagicStrings.thesaurusPassword;
  }
}