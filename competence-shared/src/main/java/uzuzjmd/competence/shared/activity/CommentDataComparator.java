package uzuzjmd.competence.shared.activity;

import java.util.Comparator;

/**
 * This class allows commends to be sorted by time
 */
public class CommentDataComparator implements Comparator<CommentData> {

	@Override
	public int compare(CommentData arg0, CommentData arg1) {
		if (arg0.getCreated() == arg1.getCreated()) {
			return 0;
		} else {
			return (arg0.getCreated() > arg1.getCreated()) ? 1 : -1;
		}

	}

}
