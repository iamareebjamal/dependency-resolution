package exceptions;

public class NodeCreationException extends Exception {
    private String message;

    public NodeCreationException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Exception occurred while creating node : " + message;
    }
}
