package zebraPrinter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Author: yanyang.wang
 * Date: 08/12/2014
 */
public class TcpConnection implements Connection {
    private static final int DEFAULT_TIME_TO_WAIT_FOR_MORE_DATA = 500;
    private static final int DEFAULT_MAX_TIMEOUT_FOR_READ = 5000;
    private static int MAX_DATA_LENGTH_TO_WRITE = 1024;

    private Connector connector;
    private OutputStream outputStream;
    private InputStream inputStream;
    private Socket socket;
    private boolean isConnected = false;

    public TcpConnection(String address, int port) {
        this.connector = new Connector(address, port);
    }

    @Override
    public void open() throws PrintException {
        if (!isConnected) {
            try {
                socket = connector.open();
                outputStream = socket.getOutputStream();
                inputStream = socket.getInputStream();
                isConnected = true;
            } catch (IOException e) {
                isConnected = false;
                throw new PrintException("Can not connect printer: " + e.getMessage());
            }
        }
    }

    @Override
    public void close() throws PrintException {
        if (isConnected) {
            isConnected = false;
            try {
                outputStream.close();
                inputStream.close();
                socket.close();
            } catch (IOException e) {
                throw new PrintException("Can not close printer: " + e.getMessage());
            }
        }
    }

    @Override
    public void write(byte[] data) throws PrintException {
        if (outputStream != null && isConnected()) {
            try {
                int remainLength = data.length;
                int length;
                for (int i = 0; remainLength > 0; i += length, remainLength -= length) {
                    length = remainLength > MAX_DATA_LENGTH_TO_WRITE ? MAX_DATA_LENGTH_TO_WRITE : remainLength;
                    outputStream.write(data, i, length);
                    outputStream.flush();
                    sleep(10L);
                }
            } catch (IOException e) {
                throw new PrintException("Error writing to printer: " + e.getMessage());
            }
        } else {
            throw new PrintException("The connection is not open");
        }
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public byte[] read() throws PrintException {
        int lengthRemain = bytesAvailable();
        if (lengthRemain > 0) {
            byte[] data = new byte[lengthRemain];

            try {
                inputStream.read(data);
                return data;
            } catch (IOException e) {
                throw new PrintException(e.getMessage());
            }
        } else {
            return null;
        }
    }

    @Override
    public int bytesAvailable() throws PrintException {
        try {
            return inputStream.available();
        } catch (IOException e) {
            throw new PrintException(e.getMessage());
        }
    }

    @Override
    public void waitForData(int millis) throws PrintException {
        long endTime = System.currentTimeMillis() + (long) millis;

        while (bytesAvailable() == 0 && System.currentTimeMillis() < endTime) {
            sleep(50L);
        }
    }

    @Override
    public byte[] sendAndWaitForResponse(byte[] data, String endValidationString) throws PrintException {
        if (!isConnected()) {
            throw new PrintException("No Printer Connection");
        } else {
            write(data);
            waitForData(DEFAULT_MAX_TIMEOUT_FOR_READ);
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();

            while (bytesAvailable() > 0) {
                byte[] response = read();

                try {
                    outStream.write(response);
                } catch (IOException e) {
                    throw new PrintException(e.getMessage());
                }

                if (shouldWaitForData(outStream.toString(), endValidationString)) {
                    waitForData(DEFAULT_TIME_TO_WAIT_FOR_MORE_DATA);
                }
            }

            return outStream.toByteArray();
        }
    }

    private boolean shouldWaitForData(String message, String endString) {
        return endString == null ? true : !message.endsWith(endString);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            //todo: log;
        }
    }
}
