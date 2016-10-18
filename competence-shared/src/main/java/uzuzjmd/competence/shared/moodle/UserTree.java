package uzuzjmd.competence.shared.moodle;

import datastructures.trees.AbstractTreeEntry;
import uzuzjmd.competence.shared.activity.ActivityTyp;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import java.beans.Transient;
import java.util.List;

/**
 * XMl-mappbare DTOs für den eigenen REST-Service (Evidenzen)
 * 
 * @author julian
 * 
 */
@XmlRootElement(name = "activity")
@XmlSeeAlso(AbstractTreeEntry.class)
public class UserTree extends AbstractTreeEntry {

	private List<ActivityTyp> activityTypes;

	public UserTree() {
	}

	public UserTree(List<ActivityTyp> activityTypes) {
		super();
		this.setActivityTypes(activityTypes);
	}

	public UserTree(String name, String qtip, String icon,
			List<ActivityTyp> activityTypes) {
		super(name, qtip, icon);
		this.setActivityTypes(activityTypes);
	}

	@XmlElement(name = "activity")
	public List<ActivityTyp> getActivityTypes() {
		return activityTypes;
	}

	@Transient
	@XmlTransient
	public List<ActivityTyp> getActivityTypez() {
		return activityTypes;
	}

	public void setActivityTypes(List<ActivityTyp> activityTypes) {
		this.activityTypes = activityTypes;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public boolean equals(Object arg0) {
		// TODO Auto-generated method stub
		return super.equals(arg0);
	}

}
