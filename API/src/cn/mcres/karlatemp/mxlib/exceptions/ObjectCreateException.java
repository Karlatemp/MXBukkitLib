package cn.mcres.karlatemp.mxlib.exceptions;

public class ObjectCreateException extends Exception {
    public ObjectCreateException() {
    }

    public ObjectCreateException(String message) {
        super(message);
    }

    public ObjectCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectCreateException(Throwable cause) {
        super(cause);
    }
}
