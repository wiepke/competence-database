package uzuzjmd.competence.evidence.service.moodle;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * DTOs für den Moodle REST-Service
 *
 * DTOs for the moodle for the moodle rest service
 *
 * @author julian
 * 
 */
@XmlRootElement
public class MoodleContentResponse {

	private int id; // Section ID
	// PLAIN or 4 = MARKDOWN)
	private List<Module> modules; // list of module
	private String name; // Section name
	private String summary; // Section description
	private int summaryformat; // summary format (1 = HTML, 0 = MOODLE, 2 =

	private int visible; // is the section visible

	public int getId() {
		return id;
	}

	public List<Module> getModules() {
		return modules;
	}

	public String getName() {
		return name;
	}

	public String getSummary() {
		return summary;
	}

	public int getSummaryformat() {
		return summaryformat;
	}

	public int getVisible() {
		return visible;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setModules(List<Module> modules) {
		this.modules = modules;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public void setSummaryformat(int summaryformat) {
		this.summaryformat = summaryformat;
	}

	public void setVisible(int visible) {
		this.visible = visible;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getClass().getName() + ": ("
				+ this.name + " , " + this.id + " , "
				+ this.summary + " )";
	}
}
