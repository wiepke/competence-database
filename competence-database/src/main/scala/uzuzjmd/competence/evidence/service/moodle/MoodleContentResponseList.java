package uzuzjmd.competence.evidence.service.moodle;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * DTOs für den Moodle REST-Service
 *
 * DTOs forthe moodle rest service
 * 
 * @author julian
 * 
 */
@XmlSeeAlso(uzuzjmd.competence.evidence.service.moodle.MoodleContentResponse.class)
@XmlRootElement
public class MoodleContentResponseList extends ArrayList<MoodleContentResponse> {

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object arg0) {
		return super.equals(arg0);
	}

}
