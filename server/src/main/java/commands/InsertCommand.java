package commands;

import data.Person;
import exceptions.DatabaseHandlingException;
import exceptions.WrongAmountOfElementsException;
import interaction.PersonRaw;
import interaction.User;
import utility.CollectionManager;
import utility.DatabaseCollectionManager;
import utility.Outputer;
import utility.ResponseOutputer;

import java.time.LocalDateTime;

/**
 * Command "insert null'. Add a new element to collection.
 */
public class InsertCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public InsertCommand( CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("insert null", "{element}", "add a new element with given key.");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user){
        try{
            if(!stringArgument.isEmpty() || objectArgument == null) throw new WrongAmountOfElementsException();
            PersonRaw personRaw = (PersonRaw) objectArgument;
            collectionManager.addToCollection(databaseCollectionManager.insertPerson(personRaw, user));
            ResponseOutputer.appendln("Person successfully insert!");
            return true;
        } catch (WrongAmountOfElementsException exception){
            ResponseOutputer.appendln("Usage: '" + getName() + " " + getUsage() + "'");
        } catch (ClassCastException exception){
            ResponseOutputer.appenderror("The object passed by the client is invalid!");
        } catch (DatabaseHandlingException exception){
            ResponseOutputer.appenderror(exception.getMessage());
            ResponseOutputer.appenderror("An error occurred while accessing the database");
        }
        return false;
    }
}
