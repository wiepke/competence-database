package uzuzjmd.competence.evidence.service.moodle;

import java.util.List;

/**
 * DTOs für den Moodle REST-Service
 *
 * DTOs for the moodle rest service
 * 
 * @author julian
 * 
 */
public class Module {
	private int availablefrom; // module availability start date
	private int availableuntil; // Optional //module availability en date
	private List<Content> contents; // contents
	private String description; // activity description
	private int id; // activity id
	private int indent; // number of identation in the site
	private String modicon; // activity icon url
	private String modname; // activity module type
	private String modplural; // activity module plural name
	private String name; // activity module name
	private String instance;

	private String url; // activity url

	private int visible; // is the module visible

	public int getAvailablefrom() {
		return availablefrom;
	}

	public int getAvailableuntil() {
		return availableuntil;
	}

	public List<Content> getContents() {
		return contents;
	}

	public String getDescription() {
		return description;
	}

	public int getId() {
		return id;
	}

	public int getIndent() {
		return indent;
	}

	public String getModicon() {
		return modicon;
	}

	public String getModname() {
		return modname;
	}

	public String getModplural() {
		return modplural;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	public int getVisible() {
		return visible;
	}

	public void setAvailablefrom(int availablefrom) {
		this.availablefrom = availablefrom;
	}

	public void setAvailableuntil(int availableuntil) {
		this.availableuntil = availableuntil;
	}

	public void setContents(List<Content> contents) {
		this.contents = contents;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setIndent(int indent) {
		this.indent = indent;
	}

	public void setModicon(String modicon) {
		this.modicon = modicon;
	}

	public void setModname(String modname) {
		this.modname = modname;
	}

	public void setModplural(String modplural) {
		this.modplural = modplural;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setVisible(int visible) {
		this.visible = visible;
	}

	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

}
