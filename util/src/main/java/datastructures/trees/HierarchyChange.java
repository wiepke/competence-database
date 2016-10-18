package datastructures.trees;

import java.io.Serializable;

public class HierarchyChange implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String oldClass;
	private String newClass;
	private String nodeSelected;

	public HierarchyChange() {
	}

	public HierarchyChange(String oldClass, String newClass,
						   String nodeSelected) {
		super();
		this.oldClass = oldClass;
		this.newClass = newClass;
		this.nodeSelected = nodeSelected;
	}

	public String getOldClass() {
		return oldClass;
	}

	public void setOldClass(String oldClass) {
		this.oldClass = oldClass;
	}

	public String getNewClass() {
		return newClass;
	}

	public void setNewClass(String newClass) {
		this.newClass = newClass;
	}

	public String getNodeSelected() {
		return nodeSelected;
	}

	public void setNodeSelected(String nodeSelected) {
		this.nodeSelected = nodeSelected;
	}
}
