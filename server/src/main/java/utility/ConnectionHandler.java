package utility;

import interaction.Request;
import interaction.Response;
import interaction.ResponseCode;
import run.Server;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * Handles user connection.
 */
public class ConnectionHandler implements Runnable {

    private Server server;

    private Socket clientSocket;

    private CommandManager commandManager;

    private ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);

    public ConnectionHandler(Server server, Socket clientSocket, CommandManager commandManager) {
        this.server = server;
        this.clientSocket = clientSocket;
        this.commandManager = commandManager;
    }

    /**
     * Main handling cycle.
     */

    @Override
    public void run() {
        Request userRequest = null;
        Response responseToUser = null;
        boolean stopFlag = false;
        try (ObjectInputStream clientReader = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.getOutputStream())) {
            do {
                userRequest = (Request) clientReader.readObject();
                responseToUser = forkJoinPool.invoke(new HandleRequestTask(userRequest, commandManager));
                Response finalResponseToUser = responseToUser;
                if (!fixedThreadPool.submit(() -> {
                    try {
                        clientWriter.writeObject(finalResponseToUser);
                        clientWriter.flush();
                        return true;
                    } catch (IOException exception) {
                        Outputer.printerror("An error occurred while sending data to the client!");
                    }
                    return false;
                }).get()) break;
            } while (responseToUser.getResponseCode() != ResponseCode.SERVER_EXIT &&
                    responseToUser.getResponseCode() != ResponseCode.CLIENT_EXIT);
            if (responseToUser.getResponseCode() == ResponseCode.SERVER_EXIT)
                stopFlag = true;
        } catch (ClassNotFoundException exception) {
            Outputer.printerror("An error occurred while reading received data!");
        } catch (CancellationException | ExecutionException | InterruptedException exception) {
            Outputer.println("A multithreading error occurred while processing the request!");
        } catch (IOException exception) {
            Outputer.printerror("Unexpected loss of connection with the client!");
        } finally {
            try {
                fixedThreadPool.shutdown();
                clientSocket.close();
                Outputer.println("Client disconnected from server.");
            } catch (IOException exception) {
                Outputer.printerror("An error occurred while trying to terminate the connection with the client!");
            }
            if (stopFlag) server.stop();
        }
    }
}