package datastructures.trees;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dehne on 14.01.2016.
 * Algorithm taken from
 * http://stackoverflow.com/questions/30570146/convert-java-arraylist-of-parent-child-relation-into-tre
 *
 * Has a 2*n runtime
 */
public class TreeGenerator {
    public static Node getTree(List<TreePair> inputList) {
        HashMap<String, Node> tempMap = treeIfy(inputList);
        return rootify(tempMap);
    }

    private static HashMap treeIfy(List<TreePair> inputList) {
        HashMap <String, Node> temp = new HashMap<>(1000000);
        for (TreePair pair: inputList) {
            if (pair.child != null && pair.parent != null) {
                Node parent = getOrDefaultParent(temp, pair);
                Node child = getOrDefaultChild(temp, pair);
                parent.children.add(child);
                child.parent = parent;
                temp.put(parent.id, parent);
                temp.put(child.id, child);
            }
        }
        return temp;
    }


    private static Node rootify(HashMap<String, Node> treefiyedList) {
        for (Node n : treefiyedList.values()) {
            if (n.parent == null) {
                return n;
            }
        }
        return null;
    }

    private static Node getOrDefaultParent(Map<String, Node> temp, TreePair pair) {
        Node parent;
        if (temp.containsKey(pair.parent)) {
            parent = temp.get(pair.parent);
        } else {
            parent = new Node(pair.parent);
        }
        return parent;
    }

    private static Node getOrDefaultChild(Map<String, Node> temp, TreePair pair) {
        Node parent;
        if (temp.containsKey(pair.child)) {
            parent = temp.get(pair.child);
        } else {
            parent = new Node(pair.child);
        }
        return parent;
    }
}
