package run;

import commands.*;
import exceptions.NotInDeclaredLimitsException;
import exceptions.WrongAmountOfElementsException;
import utility.*;

/**
 * Main server class. Creates all server instances.
 *
 * @author Duong Dinh Anh (Зыонг Динь Ань).
 */
public class App {
    private static final int MAX_CLIENTS = 1000;
//    private static String databaseUsername = "s343390";
    private static String databaseUsername = "postgres";
    private static int port = 4444;
    private static String databaseHost = null;
//    private static String databasePassword = null;
//    private static String databaseAddress = null;
//    private static String databaseAddress = "jdbc:postgresql://localhost:5432/studs";
    private static String databaseAddress = "jdbc:postgresql://localhost:5432/testlab7";
//    private static String databasePassword = "ypXFZuDY114nFBfX";
    private static String databasePassword = "1234";

    public static void main(String[] args) {
//        if (!initialize(args)) return;
        DatabaseHandler databaseHandler = new DatabaseHandler(databaseAddress, databaseUsername, databasePassword);
        DatabaseUserManager databaseUserManager = new DatabaseUserManager(databaseHandler);
        DatabaseCollectionManager databaseCollectionManager = new DatabaseCollectionManager(databaseHandler, databaseUserManager);
        CollectionManager collectionManager = new CollectionManager(databaseCollectionManager);
        CommandManager commandManager = new CommandManager(
                new HelpCommand(),
                new InfoCommand(collectionManager),
                new ShowCommand(collectionManager),
                new InsertCommand(collectionManager, databaseCollectionManager),
                new UpdateCommand(collectionManager, databaseCollectionManager),
                new RemoveKeyCommand(collectionManager, databaseCollectionManager),
                new ClearCommand(collectionManager, databaseCollectionManager),
                new ExitCommand(),
                new ExecuteScriptCommand(),
                new RemoveLowerKeyCommand(collectionManager, databaseCollectionManager),
                new ReplaceIfGreaterCommand(collectionManager, databaseCollectionManager),
                new HistoryCommand(),
                new FilterStartsWithNameCommand(collectionManager),
                new PrintUniqueLocationCommand(collectionManager),
                new PrintFieldDescendingWeightCommand(collectionManager),
                new ServerExitCommand(),
                new LoginCommand(databaseUserManager),
                new RegisterCommand(databaseUserManager)
        );
        Server server = new Server(port, MAX_CLIENTS, commandManager);
        server.run();
        databaseHandler.closeConnection();
    }

    /**
     * Controls initialization.
     */
    private static boolean initialize(String[] args) {
        try {
            if (args.length != 1) throw new WrongAmountOfElementsException();
            port = Integer.parseInt(args[0]);
            if (port < 0) throw new NotInDeclaredLimitsException();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            String jarName = new java.io.File(App.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName();
            Outputer.println("Usage: 'java -jar " + jarName + " <port> <db_host> <db_password>'");
        } catch (NumberFormatException exception) {
            Outputer.printerror("The port must be represented by a number!");
        } catch (NotInDeclaredLimitsException exception) {
            Outputer.printerror("Port can not be negative!");
        }
        return false;
    }
}
