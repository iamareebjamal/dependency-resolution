package parser;

import exceptions.DependencyResolutionError;
import models.Node;

import java.util.*;

public class DependencySolver {
    private List<Node> nodes;
    private List<Node> orderedNodes = new ArrayList<>();
    private Map<Node, List<Node>> dependencyMap = new HashMap<>();

    public DependencySolver(List<Node> nodes) {
        this.nodes = new ArrayList<>(nodes);

        for (Node node : nodes) {
            dependencyMap.put(node, new ArrayList<>(node.getDependencies()));
        }

    }

    public List<Node> getResolvedNodes() throws DependencyResolutionError {

        while (!nodes.isEmpty()) {
            solveDependency();
        }
        return orderedNodes;
    }

    private void solveDependency() throws DependencyResolutionError {
        Node leaf = getLeafNode();
        if (leaf == null)
            throw new DependencyResolutionError(orderedNodes, "Cyclic Dependency can't be resolved");

        for (Node dependent : leaf.getDependents()) {
            dependencyMap.get(dependent).remove(leaf);
        }
        orderedNodes.add(leaf);
        nodes.remove(leaf);
    }

    public Node getLeafNode() {
        for (Node node : nodes) {
            if (dependencyMap.get(node).size() == 0 && !orderedNodes.contains(node))
                return node;
        }
        return null;
    }

}
