package uzuzjmd.competence.mapper.rest.write

import java.util

import datastructures.graph.GraphTriple
import uzuzjmd.competence.exceptions.{ContextNotExistsException, UserNotExistsException}
import uzuzjmd.competence.persistence.abstractlayer.WriteTransactional
import uzuzjmd.competence.persistence.dao._
import uzuzjmd.competence.persistence.neo4j.DBFactory
import uzuzjmd.competence.persistence.ontology.Edge
import uzuzjmd.competence.persistence.performance.PerformanceTimer
import uzuzjmd.competence.shared.learningtemplate.{LearningTemplateData, LearningTemplateResultSet}
import uzuzjmd.scompetence.owl.validation.LearningTemplateValidation

import scala.collection.JavaConverters._

/**
  * @author dehne
  *
  *         This object saves a learning template in the database
  */
object LearningTemplateToOnt extends WriteTransactional[LearningTemplateData] with PerformanceTimer[LearningTemplateData, Unit] {

  def convert(changes: LearningTemplateData) {
    execute(convertHelper1 _, changes)
    execute(convertHelper _, changes)
  }

  private def convertHelper1(changes: LearningTemplateData) {
    val context = new CourseContext(changes.getGroupId);
    context.persist()
    val user = new User(changes.getUserName, Role.teacher.toString, null, null, context);
    user.persist()
    context.createEdgeWith(Edge.CourseContextOfUser, user)
  }

  private def convertHelper(changes: LearningTemplateData) {
    val context = new CourseContext(changes.getGroupId);
    val user = new User(changes.getUserName);
    if (!user.exists()) {
      throw new UserNotExistsException(user)
    }
    if (!context.exists()) {
      throw new ContextNotExistsException
    }
    val template = new LearningProjectTemplate(changes.getSelectedTemplate);
    template.persist()
    template.createEdgeWith(user, Edge.UserOfLearningProjectTemplate);
  }

  def toNode: (GraphTriple) => String = _.toNode

  def fromNode: (GraphTriple) => String = _.fromNode

  @throws[ContainsCircleException]
  def convertLearningTemplateResultSet(changes: LearningTemplateResultSet): Unit = {
    val validator = new LearningTemplateValidation(changes)
    if (changes.getResultGraph != null && changes.getResultGraph.triples != null && !changes.getResultGraph.triples.isEmpty) {

      if (!validator.isValid) {
        throw new ContainsCircleException
      }

      // case full set is given
      val triples: util.Set[GraphTriple] = changes.getResultGraph.triples

      val competences = triples.asScala.map(x => x.fromNode :: x.toNode :: Nil).flatten.toList.distinct.map(x => new Competence(x)).view
      competences.foreach(_.persist())
      val template = new LearningProjectTemplate(changes.getNameOfTheLearningTemplate, competences.asJava);
      template.persistMore()

      // create the relations maybe use batch update if it is too slow
      val manager = DBFactory.getDB;
      triples.asScala.view.foreach(x => manager.createRelationShip(x.fromNode, Edge
        .SuggestedCompetencePrerequisiteOf, x.toNode, classOf[Competence],classOf[Competence]))

      // create Catchword relations
      val map: util.HashMap[GraphTriple, Array[String]] = changes.getCatchwordMap
      createCatchwordRelations(map, toNode)
      createCatchwordRelations(map, fromNode)
    }

    // case only root was given
    if (changes.getRoot != null) {
      val root: String = changes.getRoot.getLabel;
      val template2 = new LearningProjectTemplate(changes.getNameOfTheLearningTemplate);
      template2.persist()
      val catchwords: Iterable[Catchword] = changes.getCatchwordMap.values().asScala.flatten.map(x=>new Catchword(x))
      val rootDao = new Competence(root, template2, catchwords.toList.asJava);
      rootDao.persist()
      template2.addCompetenceToProject(rootDao)
    } else {
      val template2 = new LearningProjectTemplate(changes.getNameOfTheLearningTemplate);
      template2.persist()
    }
  }


  def createCatchwordRelations(map: util.HashMap[GraphTriple, Array[String]], f: (GraphTriple => String)): Unit = {
    val competenceCatchwords = map.keySet().asScala.foreach(x => new Competence(f(x)).persist().createEdgeWithAll(getCatchwordsFromMap(map, x), Edge.CatchwordOf))
  }

  private def getCatchwordsFromMap(map: util.HashMap[GraphTriple, Array[String]], triple: GraphTriple): util.List[Dao] = {
    return map.get(triple).map(y => new Catchword(y).persist()).toList.asJava
  }
}