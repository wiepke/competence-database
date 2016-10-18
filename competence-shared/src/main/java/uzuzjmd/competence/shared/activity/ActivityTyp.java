package uzuzjmd.competence.shared.activity;

import datastructures.trees.AbstractTreeEntry;
import datastructures.trees.ActivityEntry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.List;

/**
 * XMl-mappbare DTOs für den eigenen REST-Service (Evidenzen)
 * 
 * @author julian
 * 
 */
@XmlSeeAlso(AbstractTreeEntry.class)
public class ActivityTyp extends AbstractTreeEntry {

	private List<ActivityEntry> activities;

	public ActivityTyp() {

	}

	public ActivityTyp(String name, String qtip, String icon) {
		super(name, qtip, icon);
		// TODO Auto-generated constructor stub
	}

	public ActivityTyp(String name, String qtip, String icon,
			List<ActivityEntry> activities) {
		super(name, qtip, icon);
		this.activities = activities;
	}

	@XmlElement(name = "activityEntry")
	public List<ActivityEntry> getActivities() {
		return activities;
	}

	public void setActivities(List<ActivityEntry> activities) {
		this.activities = activities;
	}
}
