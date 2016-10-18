package datastructures.trees;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement(name = "HierarchyChangeSet")
public class HierarchyChangeSet implements Serializable {

	List<HierarchyChange> elements;

	public HierarchyChangeSet() {
		elements = new LinkedList<HierarchyChange>();
	}

	public List<HierarchyChange> getElements() {
		return elements;
	}

	public void setElements(List<HierarchyChange> elements) {
		this.elements = elements;
	}

	public List<String> convertToListString() {
		LinkedList<String> result = new LinkedList<String>();
		for (HierarchyChange changeelement : getElements()) {
			result.add(changeelement.getOldClass() + ":"
					+ changeelement.getNodeSelected() + ":"
					+ changeelement.getNewClass());
		}

		return result;
	}

	public HierarchyChangeSet convertListToModel(List<String> encodedSet) {
		HierarchyChangeSet result = new HierarchyChangeSet();
		for (String string : encodedSet) {
			String[] splitted = string.split(":");
			result.getElements()
					.add(new HierarchyChange(splitted[0], splitted[2],
							splitted[1]));
		}
		return result;
	}
}
