package commands;

import data.Person;
import exceptions.*;
import interaction.User;
import utility.CollectionManager;
import utility.DatabaseCollectionManager;
import utility.ResponseOutputer;

/**
 * Command 'clear'. Clears the collection
 */
public class ClearCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public ClearCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager){
        super("clear", "", "clear the collection");
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
            if(!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            for (Person person : collectionManager.getCollection().values()){
                if (!person.getOwner().equals(user)) throw new PermissionDeniedException();
                if (!databaseCollectionManager.checkPersonUserId(person.getId(), user)) throw new ManualDatabaseEditException();
            }
            databaseCollectionManager.clearCollection();
            collectionManager.clearCollection();
            ResponseOutputer.appendln("Collection cleared!");
            return true;
        } catch (WrongAmountOfElementsException exception){
            ResponseOutputer.appendln("Usage: '" + getName() + " " + getUsage() + "'");
        }  catch (DatabaseHandlingException exception) {
            ResponseOutputer.appenderror("An error occurred while accessing the database!");
        } catch (PermissionDeniedException exception) {
            ResponseOutputer.appenderror("Insufficient rights to execute this command!");
            ResponseOutputer.appendln("Objects owned by other users are read-only.");
        } catch (ManualDatabaseEditException exception) {
            ResponseOutputer.appenderror("A direct database change has occurred!");
            ResponseOutputer.appendln("Restart the client to avoid possible errors.");
        }
        return false;
    }
}
