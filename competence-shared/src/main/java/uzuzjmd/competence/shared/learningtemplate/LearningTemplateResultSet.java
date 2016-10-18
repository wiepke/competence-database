package uzuzjmd.competence.shared.learningtemplate;

import datastructures.graph.Graph;
import datastructures.graph.GraphNode;
import datastructures.graph.GraphTriple;

import javax.faces.bean.ManagedProperty;
import javax.ws.rs.QueryParam;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;

@XmlRootElement
public class LearningTemplateResultSet {

	private GraphNode root; // root is set if graph consists of one node

	private Graph resultGraph;

	private HashMap<GraphTriple, String[]> catchwordMap;
	@QueryParam("nameOfTheLearningTemplate")
	@ManagedProperty(value = "nameOfTheLearningTemplate")
	private String nameOfTheLearningTemplate;

	public LearningTemplateResultSet() {
		this.catchwordMap = new HashMap<GraphTriple, String[]>();
		this.resultGraph = new Graph();
	}

	public LearningTemplateResultSet(GraphNode root) {
		this.root = root;
	}

	public void addTriple(GraphTriple triple,
			String[] catchwords) {		
		resultGraph.addSuggestedCompetenceTriple(triple);
		catchwordMap.put(triple, catchwords);
	}

	public LearningTemplateResultSet(Graph resultGraph,
			HashMap<GraphTriple, String[]> catchwordMap,
			String nameOfTheLearningTemplate) {
		super();
		this.resultGraph = resultGraph;
		this.catchwordMap = catchwordMap;
		this.nameOfTheLearningTemplate = nameOfTheLearningTemplate;
	}

	public Graph getResultGraph() {
		return resultGraph;
	}

	public void setResultGraph(Graph resultGraph) {
		this.resultGraph = resultGraph;
	}

	public HashMap<GraphTriple, String[]> getCatchwordMap() {
		return catchwordMap;
	}

	public void setCatchwordMap(
			HashMap<GraphTriple, String[]> catchwordMap) {
		this.catchwordMap = catchwordMap;
	}

	public String getNameOfTheLearningTemplate() {
		return nameOfTheLearningTemplate;
	}

	public void setNameOfTheLearningTemplate(
			String nameOfTheLearningTemplate) {
		this.nameOfTheLearningTemplate = nameOfTheLearningTemplate;
	}

	public GraphNode getRoot() {
		return root;
	}

	public void setRoot(GraphNode root) {
		this.root = root;
	}

}
