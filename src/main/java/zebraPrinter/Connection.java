package zebraPrinter;

/**
 * Author: yanyang.wang
 * Date: 08/12/2014
 */
public interface Connection {
    void open() throws PrintException;

    void close() throws PrintException;

    void write(byte[] data) throws PrintException;

    boolean isConnected();

    byte[] read() throws PrintException;

    int bytesAvailable() throws PrintException;

    void waitForData(int millis) throws PrintException;

    byte[] sendAndWaitForResponse(byte[] data, String endValidationString) throws PrintException;
}
