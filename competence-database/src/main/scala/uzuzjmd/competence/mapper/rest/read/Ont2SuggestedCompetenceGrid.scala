package uzuzjmd.competence.mapper.rest.read

import java.util
import javax.ws.rs.WebApplicationException

import com.google.common.collect.Lists
import uzuzjmd.competence.logging.Logging
import uzuzjmd.competence.persistence.abstractlayer.ReadTransactional
import uzuzjmd.competence.persistence.dao._
import uzuzjmd.competence.shared.assessment.{TypeOfSelfAssessment, Assessment, ReflectiveAssessment, ReflectiveAssessmentsListHolder}
import uzuzjmd.competence.shared.learningtemplate.{LearningTemplateData, SuggestedCompetenceColumn, SuggestedCompetenceGrid, SuggestedCompetenceRow}

import scala.collection.JavaConverters._
import scala.collection.mutable.Buffer

/**
  * Created by dehne on 11.12.2015.
  */
object Ont2SuggestedCompetenceGrid extends ReadTransactional[LearningTemplateData, SuggestedCompetenceGrid] with Logging{

  protected type ComPairList = Buffer[(Competence, Competence)]

  //private val logger = LogManager.getLogger(Ont2SuggestedCompetenceGrid.getClass().getName());

  def convert(changes: LearningTemplateData): SuggestedCompetenceGrid = {
    return execute(convertHelper _, changes)
  }

  private def convertHelper(changes: LearningTemplateData): SuggestedCompetenceGrid = {
    val context = new CourseContext(changes.getGroupId);
    val user = new User(changes.getUserName, Role.teacher.toString , null, null, context);
    val learningTemplate = new LearningProjectTemplate(changes.getSelectedTemplate);
    if (learningTemplate == null || !learningTemplate.exists()) {
      throw new WebApplicationException("learningtemplate null or does not exist");
    }
    val result = Ont2SuggestedCompetenceGrid.convertToTwoDimensionalGrid(learningTemplate, user, TypeOfSelfAssessment
      .EPOSTYPE);
    return result
  }

  def convertToTwoDimensionalGrid(learningProjectTemplate: LearningProjectTemplate, user: User, typeOfSelfAssessment: TypeOfSelfAssessment):
  SuggestedCompetenceGrid = {
    val result = new SuggestedCompetenceGrid
    val scalaGrid = convertToTwoDimensionalGrid1(learningProjectTemplate)
    val scalaGridDeNormalized: List[(Catchword, List[Competence])] = scalaGrid.map(x => x._2.map(oneList => ((x._1, oneList)))).toList.flatten
    val unsortedRows = scalaGridDeNormalized.map(x => mapScalaGridToSuggestedCompetenceRow(x._1, x._2, user, typeOfSelfAssessment))
    val rows = unsortedRows.sortBy(_.getSuggestedCompetenceRowHeader()).asJava
    result.setSuggestedCompetenceRows(rows)
    return result
  }

  private def mapScalaGridToSuggestedCompetenceRow(catchword: Catchword, competences: List[Competence], user: User,
                                                   typeOfSelfAssessment: TypeOfSelfAssessment): SuggestedCompetenceRow = {
    val result = new SuggestedCompetenceRow
    result.setSuggestedCompetenceRowHeader(catchword.getDefinition)
    result.setSuggestedCompetenceColumns(competences.map(convertCompetenceToColumn(_)(user)(typeOfSelfAssessment)).asJava)
    return result
  }

  private def convertCompetenceToColumn(competence: Competence)(user: User) (typeOfSelfAssessment:
                                                                             TypeOfSelfAssessment): SuggestedCompetenceColumn = {
    val result = new SuggestedCompetenceColumn
    result.setTestOutput(competence.getDefinition)
    result.setProgressInPercent(calculateAssessmentIndex(competence, user,typeOfSelfAssessment))

    // if there are no subclasses the competence itself should be used for assessment
    if (competence.listSubClasses(classOf[Competence]).isEmpty) {
      val holder = new ReflectiveAssessmentsListHolder
      val assessment = new Assessment
      holder.setAssessment(assessment)
      holder.setSuggestedMetaCompetence(competence.getDefinition)
      val reflectiveAssessment = competenceToReflectiveAssessment(competence)(user)(typeOfSelfAssessment)
      holder.setReflectiveAssessmentList((reflectiveAssessment :: Nil).asJava)
      result.setReflectiveAssessmentListHolder(holder)
    } else {
      result.setReflectiveAssessmentListHolder(competenceToReflexiveAssessmentsListHolder(competence, user, typeOfSelfAssessment))
    }
    return result
  }

  private def competenceToReflexiveAssessmentsListHolder(competence: Competence, user: User, typeOfSelfAssessment: TypeOfSelfAssessment):
  ReflectiveAssessmentsListHolder = {
    val holder = new ReflectiveAssessmentsListHolder
    val assessment = new Assessment
    holder.setAssessment(assessment)
    holder.setSuggestedMetaCompetence(competence.getDefinition)
    holder.setReflectiveAssessmentList(Lists.newArrayList(competence.listSubClasses(classOf[Competence])).asScala.map
    (competenceToReflectiveAssessment(_)(user)(typeOfSelfAssessment)).filter(_.getCompetenceDescription() != null).asJava)
    return holder
  }

  private def competenceToReflectiveAssessment(competence: Competence)(user: User) (typeOfSelfAssessment:
                                                                                    TypeOfSelfAssessment): ReflectiveAssessment = {
    val result = new ReflectiveAssessment
    val assessmentInDB = competence.getAssessment(user, typeOfSelfAssessment)
    val index = assessmentInDB.getAssessmentIndex
    val assessment = new Assessment
    result.setAssessment(assessment.getItems().get(index))
    result.setCompetenceDescription(competence.getDefinition)
    result.setIsLearningGoal(assessmentInDB.getLearningGoal)
    return result
  }

  private def calculateAssessmentIndex(competence: Competence, user: User, typeOfSelfAssessment:TypeOfSelfAssessment)
  : java.lang.Integer
  = {

    val listSubclasses = competence.listSubClasses(classOf[Competence])
    if (listSubclasses.isEmpty) {
      val number = Math.round(competence.getAssessment(user,typeOfSelfAssessment).getAssessmentIndex * 33.33333)
      return Integer.parseInt(number + "")
    }
    val sum: Int = listSubclasses.asScala.map(x => x.getAssessment(user,typeOfSelfAssessment)).map(x => x.getAssessmentIndex).map(x => x.toInt).sum
    val average = (sum) / listSubclasses.size
    val result = Math.round(average * 33.33333)
    return Integer.parseInt(result + "")
  }

  def convertToTwoDimensionalGrid1(learningProjectTemplate: LearningProjectTemplate): Map[Catchword, List[List[Competence]]] = {

    val includedCompetences: util.List[Competence] = learningProjectTemplate.getAssociatedCompetences

    // identify most used catchwords
    val allCatchwords1 = includedCompetences.asScala.map(x => x.getCatchwords.asScala)
    val allCatchwords2 = allCatchwords1.flatten.groupBy(identity)

    if (allCatchwords2.isEmpty) {
      //throw new WebApplicationException(new Exception("No catchwords for competences found"));
      logger.warn("No catchwords found for learningprojecttemplate" + includedCompetences)
      return Map.empty[Catchword,List[List[Competence]]]
    }
    val allCatchwords = allCatchwords2.mapValues(_.size).toList
    val sortedCatchwords = allCatchwords.sortBy(_._2).toMap.map(x => x._1)

    // group by catchwords
    val groupedCompetences = sortedCatchwords.map(catchword => (catchword, includedCompetences)).map(x => (x._1, x._2.asScala.filter(_.getCatchwords.contains(x._1)))).toMap

    // convert to datagrid structure for visualization
    val grid = groupedCompetences.mapValues(x => x.toList).mapValues(sortListOfSuggestedCompetences)

    return grid
  }

  /**
   * in case of bad run time performance topological sorting might be a better choice
   */
  private def sortListOfSuggestedCompetences(rawList: List[Competence]): List[List[Competence]] = {

    logger.trace("list of competences to be sorted: ")
    logger.trace(rawList.map(x => x.getDefinition).reduce((a, b) => a + " , " + b))

    if (rawList.isEmpty) {
      return List.empty
    }
    if (rawList.size == 1) {
      return rawList :: Nil
    }

    // map to triples and filter the ones that have a suggested prerequisiste relationship
    val hList0TMP = Ont2SuggestedCompetencyGridMapper.convertListToSuggestedCompetenceTriples(rawList.toBuffer)
    val hList0 = hList0TMP.filter(Ont2SuggestedCompetencyGridFilter.filterIsSuggestedCompetency)
    // init algorithm

    if (hList0.isEmpty) {
      return rawList.map(x => x :: Nil)
    }
    if (hList0.size == 1) {
      return rawList.map(x => x :: Nil)
    }

    val hList1 = Buffer(hList0.head)
    val hList0WithoutPivot = hList0.tail

    logger.trace("init algorithm with:")
    logger.trace("hList0:" + compareListToString(hList0))
    logger.trace("hListWithoutPivot:" + compareListToString(hList0WithoutPivot))
    logger.trace("hList1:" + compareListToString(hList1))

    // start recursive algorithm
    return sortListOfSuggestedCompetences1(hList0WithoutPivot, hList1)
  }

  private def sortListOfSuggestedCompetences1(hList0WithoutPivot: ComPairList, hList1: ComPairList): List[List[Competence]] = {

    // need to copy the result of this run in order to calculate longest path
    val hListThisRun: ComPairList = Buffer.empty
    hListThisRun.appendAll(hList1)

    return sortListOfSuggestedCompetences2(hList0WithoutPivot, hList1, hListThisRun)
  }

  private def sortListOfSuggestedCompetences2(hList0WithoutPivot: ComPairList, hList1: ComPairList, hList1LastRun: ComPairList): List[List[Competence]] = {
    val hList2: ComPairList = Buffer.empty
    val sortedhList0WithoutPivot = hList0WithoutPivot.sortWith(Ont2SuggestedCompetencyGridFilter.sortCompetencePairs)
    sortedhList0WithoutPivot.foreach(addHlist0ElementToCorrectList(_)(hList1, hList2))

    logger.trace("after adding elements to correct lists:")
    logger.trace("hList0:" + compareListToString(sortedhList0WithoutPivot))
    logger.trace("hList1:" + compareListToString(hList1))
    logger.trace("hList2:" + compareListToString(hList2))

    val reverseConvertedList = Ont2SuggestedCompetencyGridMapper.convertListToSuggestedCompetenceTriplesInverse(hList1).toList
    // stop if all elements have been added to a path
    if (hList2.isEmpty) {
      return List(reverseConvertedList)
    }
    // restarting if path length has increased
    val hListThisRun: ComPairList = Buffer.empty
    hListThisRun.appendAll(hList1)
    if (hList1.size > hList1LastRun.size) {
      return sortListOfSuggestedCompetences2(hList2, hList1, hListThisRun)
    }
    // start new list if a fork has been detected
    return reverseConvertedList :: sortListOfSuggestedCompetences2(hList2.tail, hList2.take(1), hList2.take(1))
  }

  /**
   * returns TreePair Of (Hlist1, and HList2)
   */
  private def addHlist0ElementToCorrectList(hList0Element: (Competence, Competence))(hList1: ComPairList, hList2: ComPairList) = {

    if (hList0Element._2.equals(hList1.head._1)) {
      hList1.prepend(hList0Element)
    } else if (hList0Element._1.equals(hList1.last._2)) {
      hList1.append(hList0Element)
    } else {
      hList2.append(hList0Element)
    }
  }

  def compareListToString(input: ComPairList): String = {
    if (input.isEmpty) {
      return "[]";
    }
    return input.map(x => (x._1.getDefinition, x._2.getDefinition)).map(x => "(" + x._1 + "," + x._2 + ")").reduce((a, b) => a + " , " + b)
  }

  private def catchwordsStoString(input: Buffer[Catchword]): String = {
    if (input.isEmpty) {
      return ""
    }
    return input.map(x => x.getDefinition).reduce((a, b) => a + ", " + b)
  }

}
