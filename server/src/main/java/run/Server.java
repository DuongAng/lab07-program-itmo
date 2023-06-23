package run;

import exceptions.ClosingSocketException;
import exceptions.ConnectionErrorException;
import exceptions.OpeningServerSocketException;
import utility.CommandManager;
import utility.ConnectionHandler;
import utility.Outputer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Server {
    private int port;
    private ServerSocket serverSocket;
    private CommandManager commandManager;
    private boolean isStopped;
    private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();


    private Semaphore semaphore;

    public Server(int port, int maxClients, CommandManager commandManager) {
        this.port = port;
        this.commandManager = commandManager;
        this.semaphore = new Semaphore(maxClients);
    }

    /**
     * Begins server operation.
     */
    public void run() {
        try {
            openServerSocket();
            while (!isStopped()) {
                try {
                    acquireConnection();
                    if (isStopped()) throw new ConnectionErrorException();
                    Socket clientSocket = connectToClient();
                    cachedThreadPool.submit(new ConnectionHandler(this, clientSocket, commandManager));
                } catch (ConnectionErrorException exception) {
                    if (!isStopped()) {
                        Outputer.printerror("An error occurred while connecting to the client!");
                    } else break;
                }
            }
            cachedThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            Outputer.println("Server shut down.");
        } catch (OpeningServerSocketException exception) {
            Outputer.printerror("Server cannot be started!");
        } catch (InterruptedException e) {
            Outputer.printerror("An error occurred while shutting down already connected clients!");
        }
    }

    /**
     * Acquire connection.
     */
    public void acquireConnection() {
        try {
            semaphore.acquire();
        } catch (InterruptedException exception) {
            Outputer.printerror("An error occurred while obtaining permission for a new connection!");
        }
    }

    /**
     * Release connection.
     */
    public void releaseConnection() {
        semaphore.release();
    }

    /**
     * Finishes server operation.
     */
    public synchronized void stop() {
        try {
            if (serverSocket == null) throw new ClosingSocketException();
            isStopped = true;
            cachedThreadPool.shutdown();
            serverSocket.close();
            Outputer.println("Completing work with already connected clients...");
        } catch (ClosingSocketException exception) {
            Outputer.printerror("Unable to shut down server not yet running!");
        } catch (IOException exception) {
            Outputer.printerror("An error occurred while shutting down the server!");
            Outputer.println("Completing work with already connected clients...");
        }
    }

    /**
     * Checked stops of server.
     *
     * @return Status of server stop.
     */
    private synchronized boolean isStopped() {
        return isStopped;
    }

    /**
     * Open server socket.
     */
    private void openServerSocket() throws OpeningServerSocketException {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IllegalArgumentException exception) {
            Outputer.printerror("Port '" + port + "' is out of range!");
            throw new OpeningServerSocketException();
        } catch (IOException exception) {
            Outputer.printerror("An error occurred while trying to use the port '" + port + "'!");
            throw new OpeningServerSocketException();
        }
    }

    /**
     * Connecting to client.
     */
    private Socket connectToClient() throws ConnectionErrorException {
        try {
            Outputer.println("Port listening '" + port + "'...");
            Socket clientSocket = serverSocket.accept();
            Outputer.println("Client connection established.");
            return clientSocket;
        } catch (IOException exception) {
            throw new ConnectionErrorException();
        }
    }

}