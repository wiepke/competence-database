package uzuzjmd.competence.shared;

import datastructures.trees.AbstractXMLTree;
import datastructures.lists.SortedList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.List;

/**
 * This class specifies the abstract tree concepts for the concept of Catchwords in order to make
 * the API more human readable.
 */
@XmlRootElement(name = "catchwordRoot")
@XmlSeeAlso(AbstractXMLTree.class)
public class CatchwordXMLTree extends AbstractXMLTree<CatchwordXMLTree> {

	public CatchwordXMLTree() {

	}

	public CatchwordXMLTree(String name, String qtip, String icon,
			List<CatchwordXMLTree> children) {
		super(name, qtip, icon, children);

	}

	@XmlElement(name = "catchword")
	@Override
	public SortedList<CatchwordXMLTree> getChildren() {
		return super.getChildren();
	}

}
