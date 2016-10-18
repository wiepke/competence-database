package uzuzjmd.competence.shared.competence;

import datastructures.trees.AbstractXMLTree;
import datastructures.lists.SortedList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.List;


/**
 * This class allows for competences to be sorted  by their definitions in a competence tree
 */
@XmlRootElement(name = "competenceRoot")
@XmlSeeAlso(AbstractXMLTree.class)
public class CompetenceXMLTree extends
		AbstractXMLTree<CompetenceXMLTree> {

	private Boolean isCompulsory = false;

	public CompetenceXMLTree() {

	}

	public CompetenceXMLTree(String name, String qtip,
			String icon, List<CompetenceXMLTree> children) {
		super(name, qtip, icon, children);
		SortedList<CompetenceXMLTree> sortedCompetences = new SortedList<CompetenceXMLTree>(
				new CompetenceXMLTreeComparator());
		sortedCompetences.addAll(children);
		setChildren(sortedCompetences);
	}

	@XmlElement(name = "competence")
	@Override
	public SortedList<CompetenceXMLTree> getChildren() {
		return super.getChildren();
	}

	public Boolean getIsCompulsory() {
		return isCompulsory;
	}

	public void setIsCompulsory(Boolean isCompulsory) {
		this.isCompulsory = isCompulsory;
	}

	@Override
	public String toString() {
		return "CompetenceXMLTree [isCompulsory="
				+ isCompulsory + ", getChildren()="
				+ getChildren() + ", getIsCompulsory()="
				+ getIsCompulsory() + ", getName()="
				+ getName() + ", getQtip()=" + getQtip()
				+ ", getIcon()=" + getIcon()
				+ ", hashCode()=" + hashCode()
				+ ", getClass()=" + getClass()
				+ ", toString()=" + super.toString() + "]";
	}

}
