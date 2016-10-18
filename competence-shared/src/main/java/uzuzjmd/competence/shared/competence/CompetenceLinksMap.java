package uzuzjmd.competence.shared.competence;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@XmlRootElement
public class CompetenceLinksMap {

	/*
	 * maps linked competence to the view
	 */
	private Map<String, CompetenceLinksView[]> mapUserCompetenceLinks = new TreeMap<String, CompetenceLinksView[]>();

	public CompetenceLinksMap() {
		this.mapUserCompetenceLinks = new HashMap<>();

	}

	public CompetenceLinksMap(
			Map<String, CompetenceLinksView[]> mapUserCompetenceLinks) {
		super();
		this.mapUserCompetenceLinks = mapUserCompetenceLinks;
	}

	public Map<String, CompetenceLinksView[]> getMapUserCompetenceLinks() {
		return mapUserCompetenceLinks;
	}

	public void setMapUserCompetenceLinks(
			Map<String, CompetenceLinksView[]> mapUserCompetenceLinks) {
		this.mapUserCompetenceLinks = mapUserCompetenceLinks;
	}

	// public CompetenceLinksMap(
	// TreeMap<String, SortedSet<CompetenceLinksView>> mapUserCompetenceLinks) {
	// super();
	// for (String element : mapUserCompetenceLinks.keySet()) {
	// List<CompetenceLinksView> output = new LinkedList<CompetenceLinksView>();
	// output.addAll(mapUserCompetenceLinks.get(element));
	// this.mapUserCompetenceLinks.put(element, output);
	// }
	//
	// }
}
