package uzuzjmd.competence.shared.assessment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "Assessment")
@SessionScoped
public class Assessment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Assessment() {
		init();
	}

	@ManagedProperty("#{items}")
	private List<String> items;

	public List<String> getItems() {
		return items;
	}

	public void setItems(List<String> items) {
		this.items = items;
	}

	public void init() {
		items = new ArrayList<String>();
		items.add("gar nicht"); // index 0
		items.add("schlecht"); // index 1
		items.add("mittel"); // index 2
		items.add("gut"); // index 3
	}
}
