package commands;

import data.Person;
import exceptions.CollectionIsEmptyException;
import exceptions.DatabaseHandlingException;
import exceptions.PersonNotFoundException;
import exceptions.WrongAmountOfElementsException;
import interaction.User;
import utility.CollectionManager;
import utility.DatabaseCollectionManager;
import utility.ResponseOutputer;

/**
 * Command 'remove_lower_key'. Removes the elements if it lower.
 */
public class RemoveLowerKeyCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public RemoveLowerKeyCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("remove_lower_key", "{element}", "remove from the collection all elements whose key is less than the given one");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            int id = Integer.parseInt(stringArgument);
            if (id <=0) throw new NumberFormatException();
            int count = 0;
            for (Person person : collectionManager.getCollection().values()) {
                if (person.getId() < id) {
                    collectionManager.removeFromCollection(person);
                    databaseCollectionManager.deletePersonById(person.getId());
                    count++;
                }
            }
            if (count == 0) {
                ResponseOutputer.appendln("No persons with ID less than " + id + " found in the collection.");
            } else {
                ResponseOutputer.appendln("Successfully removed " + count + " persons with ID less than " + id + ".");
            }
            return true;
        } catch (WrongAmountOfElementsException exception){
            ResponseOutputer.appendln("Usage: '" + getName() + " " + getUsage() + "'");
        } catch (CollectionIsEmptyException exception){
            ResponseOutputer.appenderror("The collection is empty!");
        } catch (NumberFormatException exception){
            ResponseOutputer.appenderror("ID must be a number!");
        } catch (ClassCastException exception){
            ResponseOutputer.appenderror("The object passed by the client is invalid!");
        } catch (DatabaseHandlingException e) {
            ResponseOutputer.appenderror("An error occurred while accessing the database!");
        }
        return  false;
    }
}
