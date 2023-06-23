package commands;

import data.Location;
import exceptions.CollectionIsEmptyException;
import exceptions.WrongAmountOfElementsException;
import interaction.User;
import utility.CollectionManager;
import utility.ResponseOutputer;

import java.util.List;

/**
 * Command 'print_unique_location'. Display the unique values of the position field of all elements in the collection.
 */
public class PrintUniqueLocationCommand extends AbstractCommand{
    private CollectionManager collectionManager;

    public PrintUniqueLocationCommand(CollectionManager collectionManager) {
        super("print_unique_location", "", "display the unique values of the position field of all elements in the collection.");
        this.collectionManager = collectionManager;
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user){
        try{
            if(!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            List<Location> uniqueLocations = collectionManager.uniqueLocation();

            if(!uniqueLocations.isEmpty()){
                ResponseOutputer.appendln("Unique locations:");
                for(Location location : uniqueLocations){
                    ResponseOutputer.appendln(location.getX() + " " + location.getY() + " " + location.getZ());
                }
                return true;
            } else {
                ResponseOutputer.appendln("There are no unique locations in the collection!");
            }
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
