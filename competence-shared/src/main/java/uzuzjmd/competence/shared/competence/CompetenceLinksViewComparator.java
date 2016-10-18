package uzuzjmd.competence.shared.competence;

import java.util.Comparator;

/**
 * This class allows for competence links to be sorted
 */
public class CompetenceLinksViewComparator implements Comparator<CompetenceLinksView> {

	@Override
	public int compare(CompetenceLinksView o1, CompetenceLinksView o2) {
		return o1.compareTo(o2);
	}

}
