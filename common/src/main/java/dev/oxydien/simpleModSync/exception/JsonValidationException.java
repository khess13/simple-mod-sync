package dev.oxydien.simpleModSync.exception;

public class JsonValidationException extends RuntimeException {

    public JsonValidationException(String fieldName, String expectedType) {
        super(String.format("Required field '%s' of type %s is missing from JSON",
                fieldName, expectedType));
    }
}
