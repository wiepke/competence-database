package datastructures.trees;

import datastructures.lists.SortedList;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.List;

/**
 * This class is useful because it can be mapped by jersey (whereas mapping the
 * scala trees I am not so sure)
 *
 * It wraps an abstract tree that can be visualized in an html tree with links and icons.
 * 
 * @author julian
 * 
 */
@XmlSeeAlso(AbstractTreeEntry.class)
@XmlRootElement
public class AbstractXMLTree<T extends AbstractXMLTree<T>>
		extends AbstractTreeEntry {

	private SortedList<T> children;

	public AbstractXMLTree() {
		children = new SortedList<T>(
				new TreeEntryComparator());
	}

	// public AbstractXMLTree(String name, String qtip, String icon,
	// SortedList<T> children) {
	// super(name, qtip, icon);
	// this.setChildren(children);
	// }

	public AbstractXMLTree(String name, String qtip,
			String icon, List<T> children) {
		super(name, qtip, icon);
		this.setChildren(children);
	}

	public SortedList<T> getChildren() {
		return children;
	}

	/*public void setChildren(SortedList<T> children) {
		this.children = children;
	}*/

	public void setChildren(List<T> children) {
		this.children = new SortedList<T>(
				new TreeEntryComparator());
		this.children.addAll(children);
	}

	// @Override
	// public String toString() {
	// return "AbstractXMLTree [children=" + children
	// + ", getChildren()=" + getChildren()
	// + ", getName()=" + getName()
	// + ", getQtip()=" + getQtip()
	// + ", getIcon()=" + getIcon()
	// + ", hashCode()=" + hashCode()
	// + ", getClass()=" + getClass()
	// + ", toString()=" + super.toString() + "]";
	// }

}
