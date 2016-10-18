package uzuzjmd.competence.mapper.rest.read

import java.util.{SortedSet, TreeSet}

import uzuzjmd.competence.persistence.abstractlayer.ReadTransactional
import uzuzjmd.competence.persistence.dao.{AbstractEvidenceLink, Comment, User}
import uzuzjmd.competence.persistence.ontology.Edge
import uzuzjmd.competence.shared.activity.CommentData
import uzuzjmd.competence.shared.competence.{CompetenceLinksMap, CompetenceLinksView, CompetenceLinksViewComparator}
import uzuzjmd.competence.shared.converter.CommentAdaptor
import uzuzjmd.java.collections.MapsMagic

import scala.collection.JavaConverters._
import scala.collection.mutable


/**
  * Created by dehne on 11.12.2015.
  */
object Ont2CompetenceLinkMap extends ReadTransactional[String, CompetenceLinksMap] {

  private def toSortedSet[A](input: List[CompetenceLinksView]): SortedSet[CompetenceLinksView] = {
    val sorted = java.util.Collections.synchronizedSortedSet(new TreeSet(new CompetenceLinksViewComparator));
    sorted.addAll(input.asJava)
    return sorted
  }

  implicit def toMap(input: Map[String, List[CompetenceLinksView]]): Map[String, java.util.SortedSet[CompetenceLinksView]] = {
    val result: Map[String, java.util.SortedSet[CompetenceLinksView]] = Map.empty
    return result ++ input.mapValues(toSortedSet)
  }

  def convert(user: String): CompetenceLinksMap = {
    return execute(getCompetenceLinkMap, user)
  }

  private def getCompetenceLinkMap(user: String): CompetenceLinksMap = {
    val userDap = new User(user)
    if (!userDap.exists) {
      val result = new CompetenceLinksMap
      return result
    }
    val links = userDap.getAssociatedLinks.asScala
    val maps = links.map(link => (link -> link.getAllLinkedCompetences.asScala.map(x => x.getDefinition()).toList)).toMap
    val competencesLinked = MapsMagic.invertAssociation(maps)
    val resultScalaTmp1: scala.collection.immutable.Map[String, List[CompetenceLinksView]] = competencesLinked.map(x => (x._1, x._2.map(mapAbstractEvidenceLinkToCompetenceLinksView).flatten))
    val resultScalaTmp2 = resultScalaTmp1.filterKeys(p => p != null)
    val resultAsArray = resultScalaTmp2.mapValues { x => x.toArray }
    val result = new CompetenceLinksMap(resultAsArray.asJava);
    return result
  }

  private def mapAbstractEvidenceLinkToCompetenceLinksView(input: AbstractEvidenceLink): mutable.Buffer[CompetenceLinksView] = {
    val linkedEvidence = input.getAllActivities.asScala
    val comments: mutable.Buffer[Comment] = input.getComments.asScala.map(_.getFullDao.asInstanceOf[Comment]);
    var linkedComments : java.util.List[CommentData] =  null;
    if (comments != null) {
      linkedComments = comments.map(mapCommentToCommentData).asJava
    }
    val competenceLinksView = linkedEvidence.map(x => new CompetenceLinksView(input.getId, x.getPrintableName, x.getUrl, linkedComments, input.getValidated))
    return competenceLinksView
  }

  private def mapCommentToCommentData(input: Comment): CommentData = {
    val converter = new CommentAdaptor;
    val result = converter.unmarshal(input)
    return result
  }

}
