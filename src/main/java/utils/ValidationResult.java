package utils;

public class ValidationResult<T> {
    private final boolean isValid;
    private final String errorMessage;
    private final T object;

    public ValidationResult(boolean isValid, T object, String errorMessage) {
        this.isValid = isValid;
        this.object = object;
        this.errorMessage = errorMessage;
    }

    public boolean isValid() { return isValid; }
    public T getObject() { return object; }
    public String getErrorMessage() { return errorMessage; }
}
