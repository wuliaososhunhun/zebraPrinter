package zebraPrinter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Author: yanyang.wang
 * Date: 08/12/2014
 */
public class Connector {
    private int port;
    private String address;
    private final int MAX_TIMEOUT = 15000;

    public Connector(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public Socket open() throws IOException {
        Socket socket = new Socket();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(this.address, this.port);
        socket.connect(inetSocketAddress, MAX_TIMEOUT);
        return socket;
    }

    public int getPort() {
        return this.port;
    }

    public String getAddress() {
        return this.address;
    }
}
