package au.com.dius.pact.model;

/**
 * Exception class to indicate an invalid pact specification
 */
public class InvalidPactException extends RuntimeException {
    public InvalidPactException() {
    }

    public InvalidPactException(String message) {
    }

    public InvalidPactException(String message, Throwable cause) {
    }

    public InvalidPactException(Throwable cause) {
    }

    protected InvalidPactException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    }
}
