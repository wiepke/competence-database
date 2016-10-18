package uzuzjmd.competence.evidence.service.rest.mapper

import config.MagicStrings
import datastructures.trees.ActivityEntry
import uzuzjmd.competence.evidence.service.moodle.{Module, MoodleContentResponseList}
import uzuzjmd.competence.shared.activity.ActivityTyp
import uzuzjmd.competence.shared.moodle.{MoodleEvidence, UserTree}

import scala.collection.JavaConverters._
import scala.collection.mutable.Buffer

/**
 * Diese Klasse mappt die Evidenzen aus der Moodle-Datenbank und von dem Moodle-RestService so, dass
 * Die Evidenzen als GWT-Tree angezeigt werden können
  *
  *
  * Merges information from the moodle webservice and the self-written moodle plugin to generate an overview over the
  * activities in moodle
 */
case class Evidence2Tree(moodleResponses: MoodleContentResponseList, moodleEvidences: Array[MoodleEvidence]) {
  val filteredMoodleResponses = moodleResponses.asScala.filterNot(x => x.getModules().isEmpty()) // nur die mit Moduldaten
  val modules = filteredMoodleResponses.map(x => x.getModules().asScala).flatten.view // nur die mit Module	
  // url->modicon map erstellen
  val modiconMap = modules.map(x => x.getModplural() -> x.getModicon()).toMap
  // url->printableName erstellen
  val printableNameMap = modules.map(x => x.getUrl() -> x.getName()).toMap
  // modname -> modnamePluralMap erstellen
  val pluralMap = modules.map(x => x.getModname() -> x.getModplural()).toMap.filterKeys(key => (!key.equals("competence")))

  def getUserTrees(): java.util.List[UserTree] = {
    val groupedByUser = moodleEvidences.filterNot(x => x.getActivityTyp().equals("courseId")).groupBy(evidence => evidence.getUsername())
    val groupedByModAndMappedToPlural = groupedByUser.map(x => (x._1, x._2.groupBy(x => x.getActivityTyp()).map(y => (getValueFromMap(y._1)(pluralMap), y._2.map(z => z.getUrl()).distinct))))
    val testresult = groupedByModAndMappedToPlural.map(x => activityTypEntryMapToActivityTyp(x._2))
    val result = groupedByModAndMappedToPlural.map(x => new UserTree(x._1, "Benutzer", "http://icons.iconarchive.com/icons/artua/dragon-soft/16/User-icon.png", activityTypEntryMapToActivityTyp(x._2)))
    return result.toList.filterNot(userEntry => userEntry.getName().equals("notfound")).asJava
  }

  def getValueFromMap(key: String)(map: Map[String, String]): String = {
    return map.get(key) match {
      case Some(i) => return i;
      case None    => return "notfound";
    };
  }

  def activityTypEntryMapToActivityTyp(activityTypMap: Map[String, Array[String]]): java.util.List[ActivityTyp] = {
    val testresult = activityTypMap.map(y => y._2)
    val result = activityTypMap.map(y => new ActivityTyp(y._1, "Aktivitätstyp", getValueFromMap(y._1)(modiconMap), createActivityEntries(y._2)))
    return result.toList.filterNot(x => x.getName().equals(x.getIcon())).filterNot(userEntry => userEntry.getName().equals("notfound")).asJava
  }

  def createActivityEntries(urls: Array[String]): java.util.List[ActivityEntry] = {
    return urls.map { x => x.replace("\\", "") }.map { x => MagicStrings.MOODLEURL + x }.map(url => new ActivityEntry(getValueFromMap(url)(printableNameMap), "Activität", MagicStrings.ICONPATHMOODLE + "/appbar.monitor.to.svg", url)).toList.filterNot(userEntry => userEntry.getName().equals("notfound")).asJava
  }

  def printMap(map: Map[String, String]) {
    map.foreach(x => println("Key: " + x._1 + "  value:" + x._2))
  }

  def getUserTreesOld(): java.util.List[UserTree] = {
    val filteredMoodleResponses = moodleResponses.asScala.filterNot(x => x.getModules().isEmpty()) // nur die mit Moduldaten
    val modules = filteredMoodleResponses.map(x => x.getModules().asScala).flatten.view // nur die mit Module
    val filteredModules = modules.filterNot(x => x.getUrl().equals(null)).filterNot(x => x.getModname().equals("competence")); // nur die mit URL
    val groupedEvidences = moodleEvidences.view.map(x => (x.getUrl(), x)) // key map nach url erstellt für evidenzen
    val joinedPairs = filteredModules.map(filteredModules => (filteredModules, groupedEvidences.filter(x => filteredModules.getUrl().equals(x._1)))).
      map(x => (x._1, x._2.toMap.values.toBuffer)) // nach url gejoined und die map wieder platt gemacht
    val groupedPairsByUser = joinedPairs.map(pair => (pair._1, pair._2.groupBy(evidence => evidence.getUsername()))).toBuffer // nach dem namen gruppieren
    //val groupedPairsByActivity = joinedPairs.map(pair => (pair._1, pair._2.groupBy(evidence => evidence.getActivityTyp()))).toBuffer // nach der aktivität gruppieren
    val result = groupedPairsByUser.map(pair => pair._2.map(map => new UserTree(map._1, "Benutzer", "http://icons.iconarchive.com/icons/artua/dragon-soft/16/User-icon.png", createActivityTypes(map._2, pair._1, map._1).asJava))).flatten //oberste hierarchieebene erstellen
    //    return result.distinct.asJava;
    val groupedByUserTree = result.toList.groupBy(userTree => userTree.getName()).map(x => x._2.reduce((a: UserTree, b: UserTree) => createUserTree(a, b)))
    return groupedByUserTree.toList.asJava;
  }

  def createUserTree(a: UserTree, b: UserTree): UserTree = {
    val c = new UserTree(a.getName(), a.getQtip(), a.getIcon(),
      (a.getActivityTypez().asScala ++ b.getActivityTypez().asScala).groupBy(x => x.getName()).
        map(x => x._2.reduce((a: ActivityTyp, b: ActivityTyp) => createActivityTyp(a, b))).toList.asJava)
    return c;
  }

  def createActivityTyp(a: ActivityTyp, b: ActivityTyp): ActivityTyp = {
    val c = new ActivityTyp(a.getName(), a.getQtip(), a.getIcon(),
      (a.getActivities().asScala ++ b.getActivities().asScala).asJava)
    return c
  }

  def createActivityTypes(evidences: Buffer[MoodleEvidence], module: Module, user: String): Seq[ActivityTyp] = {
    val groupedByActivityTyp = evidences.groupBy(evidence => evidence.getActivityTyp())
    val result = groupedByActivityTyp.map(x => new ActivityTyp(module.getModname().toUpperCase(), "Aktivitätstyp", module.getModicon(), createActivities(x._2, module.getName()).asJava))
    return result.toSeq;
  }

  def createActivities(evidences: Buffer[MoodleEvidence], modReadableName: String): List[ActivityEntry] = {
    val result = evidences.map(x => new ActivityEntry(modReadableName, "Activität", MagicStrings.ICONPATHMOODLE + "/appbar.monitor.to.svg", x.getUrl()))
    return result.toList
  }

}