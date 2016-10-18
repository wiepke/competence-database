package datastructures.trees;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dehne on 14.01.2016.
 */
public class Node {
    public String id;
    public List<Node> children;
    public Node parent;

    static Logger logger = LogManager
            .getLogger(Node.class);

    public Node(String id) {
        this.id = id;
        children = new LinkedList<>();
    }

    public String toStrinz() {
        return stringify(this,"", "");
    }

    private String stringify(Node node, String result, String spacer) {
        logger.debug("ID is:" + id + "children length is: " + children.size());
        spacer += "   ";
        for (Node child : children) {
            result += spacer + child.id + "\n";
            result += stringify(child, result, spacer);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        return this.id.equals(((Node) o).id);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
