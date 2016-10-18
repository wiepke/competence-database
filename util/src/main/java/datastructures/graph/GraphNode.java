package datastructures.graph;

public class GraphNode {

	private int id;

	public GraphNode() {
	}

	public GraphNode(String label) {
		this.setLabel(label);
		this.id = label.hashCode();
	}

	private String label;

	@Override
	public boolean equals(Object obj) {
		return (((GraphNode) obj).getLabel().equals(getLabel()));
	}

	@Override
	public int hashCode() {
		return Integer.valueOf(getLabel().codePointAt(0));
	}

	@Override
	public String toString() {
		return getLabel();
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		// this.label = encode(label);
		this.label = label;
		this.id = label.hashCode();
	}

	// private String encode(String label2) {
	// return label2.replaceAll(" ", "-");
	// }

	public int getId() {
		return id;
	}
}
