package exceptions;

import models.Node;

import java.util.List;

public class DependencyResolutionError extends Exception {
    private String message;
    private List<Node> unresolved;

    public DependencyResolutionError(String message) {
        this.message = message;
    }

    public DependencyResolutionError(List<Node> unresolved, String message) {
        this.unresolved = unresolved;
        this.message = message;
    }

    public List<Node> getUnresolved() {
        return unresolved;
    }

    public void setUnresolved(List<Node> unresolved) {
        this.unresolved = unresolved;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Exception occurred while resolving dependencies : " + message;
    }
}
