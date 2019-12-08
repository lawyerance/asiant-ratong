package pers.lyks.asiant.server;

import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpException;
import org.apache.http.HttpServerConnection;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author lawyerance
 * @version 1.0 2019-12-08
 */
public class Worker extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(Worker.class);
    private final HttpService httpservice;
    private final HttpServerConnection conn;

    public Worker(HttpService httpservice, HttpServerConnection conn) {
        this.httpservice = httpservice;
        this.conn = conn;
    }

    @Override
    public void run() {
        logger.info("New request connection thread {}, open - {}", this.getId(), this.conn.isOpen());
        HttpContext context = new BasicHttpContext(null);
        try {
            while (!Thread.interrupted() && this.conn.isOpen()) {
                this.httpservice.handleRequest(this.conn, context);
            }
        } catch (ConnectionClosedException ex) {
            logger.error("Client closed connection", ex);
        } catch (IOException ex) {
            logger.error("I/O error", ex);
        } catch (HttpException ex) {
            logger.error("Unrecoverable HTTP protocol violation: ", ex);
        } finally {
            try {
                this.conn.shutdown();
            } catch (IOException ignore) {
                //Ignore
            }
        }
    }
}
