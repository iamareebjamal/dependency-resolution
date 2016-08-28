package exceptions;

public class ParseException extends Exception {
    String message;

    public ParseException() {
    }

    public ParseException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Exception occured while parsing : " + message;
    }
}
