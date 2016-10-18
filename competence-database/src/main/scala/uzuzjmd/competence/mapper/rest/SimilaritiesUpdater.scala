package uzuzjmd.competence.mapper.rest

import java.util

import comparison.verification.SimpleCompetenceVerifier
import uzuzjmd.competence.comparison.simple.mapper.SimpleCompetenceComparatorMapper
import uzuzjmd.competence.logging.Logging
import uzuzjmd.competence.main.CompetenceCrawler
import uzuzjmd.competence.persistence.dao.{DaoAbstractImpl, Competence}
import uzuzjmd.competence.persistence.neo4j.CompetenceNeo4jQueryManagerImpl
import uzuzjmd.competence.persistence.ontology.Edge
import uzuzjmd.competence.util.LanguageConverter
import scala.collection.JavaConverters._

/**
  * Created by dehne on 29.04.2016.
  */
object SimilaritiesUpdater extends LanguageConverter with Logging {

  /**
    * verifies the competences
    * compares them best on simple matrix
    * writes the similarity score in the db
    * @param tuple
    * @return
    */
  def updateSimilarity(tuple: (Competence, Competence)) = {
    if (!tuple._1.getDefinition.equals(tuple._2.getDefinition)) {
      logger.info("updating similarities 2")
      try {
        val computer = new SimpleCompetenceComparatorMapper
        val verifier = new SimpleCompetenceVerifier
        var result: Double = 0.0
        val sentenceCorrect = CompetenceCrawler.verifySentence(verifier, tuple._1.getDefinition)
        val sentenceCorrect2 = CompetenceCrawler.verifySentence(verifier, tuple._2.getDefinition)
        if (sentenceCorrect && sentenceCorrect2 ) {
          result = computer.computeSimilarityScore(tuple._1.getDefinition, tuple._2.getDefinition)
          logger.info("found similarity: updating score result: " + result)
        }
        logger.info("not found similarity: updating")
        val queryManagerImpl = new CompetenceNeo4jQueryManagerImpl
        queryManagerImpl.createRelationShipWithWeight(tuple._1.getDefinition, Edge.SimilarTo, tuple._2.getDefinition,
          result, classOf[Competence],classOf[Competence])
        queryManagerImpl.createRelationShipWithWeight(tuple._2.getDefinition, Edge.SimilarTo, tuple._1.getDefinition,
          result, classOf[Competence],classOf[Competence])
      } catch {
        case e: Exception => List.empty
      }
    }
  }

  /**
    * updates the similarity compared to all other competences in the database
    */
  def update: Unit = {
    val competences: util.Set[Competence] = DaoAbstractImpl.getAllInstances(classOf[Competence]);
    val pairs = competences.asScala.toList.combinations(2).map(x => (x.head, x.tail.head)).foreach(updateSimilarity(_))
  }

  /**
    * helper function
    * @param input
    */
  def updateSimilarCompetencies(input: Competence): Unit = {
    val competences = DaoAbstractImpl.getAllInstances(classOf[Competence]);
    competences.asScala.foreach(x => updateSimilarity(x, input))
  }
}