package cn.mcres.karlatemp.mxlib.exceptions;

public class ScanException extends Exception{
    public ScanException() {
    }

    public ScanException(String message) {
        super(message);
    }

    public ScanException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScanException(Throwable cause) {
        super(cause);
    }
}
