package commands;

import exceptions.CollectionIsEmptyException;
import exceptions.WrongAmountOfElementsException;
import interaction.User;
import utility.CollectionManager;
import utility.ResponseOutputer;

/**
 * Command 'filter_starts_with_name'. Display elements whose name field value starts with the given substring.
 */
public class FilterStartsWithNameCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    public FilterStartsWithNameCommand(CollectionManager collectionManager) {
        super("filter_starts_with_name", "", "display elements whose name field value starts with the given substring.");
        this.collectionManager = collectionManager;
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user){
        try {
            if (!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            String nameToFilter = stringArgument.trim();
            String filterInfo = collectionManager.personFilterInfo(nameToFilter);
            if (!filterInfo.isEmpty()) {
                ResponseOutputer.appendln(filterInfo);
                return true;
            } else ResponseOutputer.appendln("There is no person with the same name in the collection!");
        } catch (WrongAmountOfElementsException exception){
            ResponseOutputer.appendln("Usage: '" + getName() + " " + getUsage() + "'");
        } catch (CollectionIsEmptyException exception){
            ResponseOutputer.appenderror("The collection is empty!");
        } catch (ClassCastException exception){
            ResponseOutputer.appenderror("The object passed by the client is invalid!");
        }
            return false;
    }
}