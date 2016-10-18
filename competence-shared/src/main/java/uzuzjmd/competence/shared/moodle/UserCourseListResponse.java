package uzuzjmd.competence.shared.moodle;

import uzuzjmd.competence.shared.moodle.UserCourseListItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

@XmlRootElement
public class UserCourseListResponse extends ArrayList<UserCourseListItem>
		implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserCourseListResponse() {

	}

	public void setSize(int size) {

	}

}
