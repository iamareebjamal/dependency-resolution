package models;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private String name;
    private int id;
    private List<String> dependenciesName = new ArrayList<>();

    // Graph
    private List<Node> dependencies = new ArrayList<>();
    private List<Node> dependents = new ArrayList<>();

    public Node(int id) {
        this.id = id;
    }

    public List<String> getDependenciesName() {
        return dependenciesName;
    }

    public List<Node> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<String> dependencies) {
        this.dependenciesName = dependencies;
    }

    public List<Node> getDependents() {
        return dependents;
    }

    public void addDependency(Node dependency) {
        dependencies.add(dependency);
    }

    public void addDependent(Node dependent) {
        dependents.add(dependent);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        String message = "Node : { " +
                "\n\tID : " + id +
                "\n\tName : " + name +
                "\n\tDependencies : " + dependenciesName +
                "\n}";

        // Can only occur after generating graph from list of nodes
        if (dependents.size() > 0) {
            message = message.substring(0, message.lastIndexOf("}")) +
                    "\tDependents : [";
            for (Node node : dependents)
                message += node.getName() + ", ";

            message = message.substring(0, message.lastIndexOf(",")) + "]" +
                    "\n}";
        }
        return message;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node) {
            return id == ((Node) obj).getId() && name.equals(((Node) obj).getName());
        }

        return false;
    }
}
