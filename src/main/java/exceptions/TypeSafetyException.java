package exceptions;

public class TypeSafetyException extends Exception{
    public TypeSafetyException(String message) {
        super(message);
    }

    public TypeSafetyException(String message, Throwable cause) {
        super(message, cause);
    }
}
