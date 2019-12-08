package pers.lyks.asiant.server;

import org.apache.http.HttpConnectionFactory;
import org.apache.http.HttpServerConnection;
import org.apache.http.impl.DefaultBHttpServerConnection;
import org.apache.http.impl.DefaultBHttpServerConnectionFactory;
import org.apache.http.protocol.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author lawyerance
 * @version 1.0 2019-12-08
 */
public class RequestListenerDaemon extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(RequestListenerDaemon.class);
    private final HttpConnectionFactory<DefaultBHttpServerConnection> connFactory;
    private final ServerSocket serversocket;
    private final HttpService httpService;


    public RequestListenerDaemon(final int port, final HttpService httpService) throws IOException {
        this(DefaultBHttpServerConnectionFactory.INSTANCE, port, httpService);
    }

    public RequestListenerDaemon(HttpConnectionFactory<DefaultBHttpServerConnection> connFactory, final int port, final HttpService httpService) throws IOException {
        this.connFactory = connFactory;
        this.serversocket = new ServerSocket(port);
        this.httpService = httpService;
    }

    @Override
    public void run() {
        logger.info("Listening on port {}", this.serversocket.getLocalPort());
        while (!Thread.interrupted()) {
            try {
                // Set up HTTP connection
                Socket socket = this.serversocket.accept();
                logger.info("Incoming connection from {}", socket.getInetAddress());
                HttpServerConnection conn = this.connFactory.createConnection(socket);

                logger.info("connection open: {}", conn.isOpen());
                // Start worker thread
                Worker t = new Worker(this.httpService, conn);
                t.setDaemon(true);
                t.start();
            } catch (InterruptedIOException ex) {
                break;
            } catch (IOException e) {
                logger.error("I/O error initialising connection thread", e.getCause());
                break;
            }
        }
    }
}
