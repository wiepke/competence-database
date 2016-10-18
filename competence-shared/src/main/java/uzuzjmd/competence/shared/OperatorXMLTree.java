package uzuzjmd.competence.shared;

import datastructures.trees.AbstractXMLTree;
import datastructures.lists.SortedList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.List;

@XmlRootElement(name = "operatorRoot")
@XmlSeeAlso(AbstractXMLTree.class)
public class OperatorXMLTree extends AbstractXMLTree<OperatorXMLTree> {

	public OperatorXMLTree() {

	}

	public OperatorXMLTree(String name, String qtip, String icon,
			List<OperatorXMLTree> children) {
		super(name, qtip, icon, children);

	}

	@XmlElement(name = "operator")
	@Override
	public SortedList<OperatorXMLTree> getChildren() {
		return super.getChildren();
	}

}
