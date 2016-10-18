package datastructures.graph;

public class GraphTriple {

	private int node1id;
	private int node2id;

	public int getNode1id() {
		return node1id;
	}

	public void setNode1id(int node1id) {
		this.node1id = node1id;
	}

	public int getNode2id() {
		return node2id;
	}

	public void setNode2id(int node2id) {
		this.node2id = node2id;
	}

	public GraphTriple() {
	}

	public GraphTriple(String fromNode, String toNode, String label,
			Boolean directed) {
		this.fromNode = fromNode;
		this.node1id = fromNode.hashCode();
		this.node2id = toNode.hashCode();
		this.toNode = toNode;
		this.label = label;
		this.directed = directed;
	}

	public String fromNode;
	public String toNode;
	public String label;
	public Boolean directed;

	@Override
	public boolean equals(Object obj) {
		return (((GraphTriple) obj).fromNode.equals(fromNode))
				&& ((GraphTriple) obj).toNode.equals(toNode)
				// && ((GraphTriple)obj).label.equals(label);
				&& ((GraphTriple) obj).directed == directed;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return fromNode.hashCode() + toNode.hashCode() + directed.hashCode();
	}

	@Override
	public String toString() {
		return "fromNode: " + fromNode + ", toNode: " + toNode + ", Label: "
				+ label + ", directed:" + directed;
	}
}
