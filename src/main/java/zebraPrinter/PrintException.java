package zebraPrinter;

/**
 * Author: yanyang.wang
 * Date: 08/12/2014
 */
public class PrintException extends Exception {
    public PrintException() {
        super();
    }

    public PrintException(String message) {
        super(message);
    }

    public PrintException(String message, Throwable cause) {
        super(message, cause);
    }

    public PrintException(Throwable cause) {
        super(cause);
    }
}
