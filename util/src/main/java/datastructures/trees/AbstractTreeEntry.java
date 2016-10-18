package datastructures.trees;

import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlAttribute;


public class AbstractTreeEntry {

	private String name;
	@ApiModelProperty(value = "a description", required = false)
	private String qtip;
	@ApiModelProperty(value = "the path to an icon for this tree entry", required = false)
	private String icon;

	public AbstractTreeEntry() {

	}

	public AbstractTreeEntry(String name, String qtip,
			String icon) {
		this.name = name;
		this.qtip = qtip;
		this.icon = icon;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	@XmlAttribute(name = "treetipp")
	public String getQtip() {
		return qtip;
	}

	public void setQtip(String qtip) {
		this.qtip = qtip;
	}

	@XmlAttribute
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Override
	public boolean equals(Object arg0) {
		return ((AbstractTreeEntry) arg0).getName().equals(
				this.getName());
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}

	@Override
	public String toString() {
		return "AbstractTreeEntry [name=" + name
				+ ", qtip=" + qtip + ", icon=" + icon
				+ ", getName()=" + getName()
				+ ", getQtip()=" + getQtip()
				+ ", getIcon()=" + getIcon()
				+ ", hashCode()=" + hashCode()
				+ ", getClass()=" + getClass()
				+ ", toString()=" + super.toString() + "]";
	}
}
