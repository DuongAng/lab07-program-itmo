package commands;

import data.Person;
import exceptions.*;
import interaction.User;
import utility.CollectionManager;
import utility.DatabaseCollectionManager;
import utility.ResponseOutputer;

/**
 * Command ' remove_key'. Removes the element by its ID.
 */
public class RemoveKeyCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public RemoveKeyCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("remove_key","<ID>", "removes element from collection by ID.");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user){
        try {
            if (stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            int id = Integer.parseInt(stringArgument);
            if (id <=0) throw new NumberFormatException();
            Person personToRemove = collectionManager.getByKey(id);
            if (personToRemove == null) throw new PersonNotFoundException();
            if (!databaseCollectionManager.checkPersonUserId(personToRemove.getId(), user)) throw new ManualDatabaseEditException();
            databaseCollectionManager.deletePersonById(id);
            collectionManager.removeFromCollection(personToRemove);
            ResponseOutputer.appendln("Person successfully removed!");
            return true;
        } catch (WrongAmountOfElementsException exception){
            ResponseOutputer.appendln("Usage: '" + getName() + " " + getUsage() + "'");
        } catch (CollectionIsEmptyException exception){
            ResponseOutputer.appenderror("The collection is empty!");
        } catch (NumberFormatException exception){
            ResponseOutputer.appenderror("ID must be a number!");
        } catch (PersonNotFoundException exception){
            ResponseOutputer.appenderror("Person with that ID not found!");
        } catch (ManualDatabaseEditException exception){
            ResponseOutputer.appenderror("A direct database change has occurred!");
            ResponseOutputer.appendln("Restart the client to avoid possible errors.");
        } catch (DatabaseHandlingException exception){
            ResponseOutputer.appenderror("An error occurred while accessing the database!");
        }
        return  false;
    }
}
