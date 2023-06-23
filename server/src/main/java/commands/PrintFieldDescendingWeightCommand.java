package commands;

import data.Location;
import exceptions.CollectionIsEmptyException;
import exceptions.WrongAmountOfElementsException;
import interaction.User;
import utility.CollectionManager;
import utility.ResponseOutputer;

import java.util.List;

/**
 * Command 'print_field_descending_weight'. Display the values of the weight field of all elements in descending order.
 */
public class PrintFieldDescendingWeightCommand extends AbstractCommand{
    private CollectionManager collectionManager;

    public PrintFieldDescendingWeightCommand(CollectionManager collectionManager) {
        super("print_field_descending_weight", "", "display the values of the weight field of all elements in descending order.");
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
            if(collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            List<Float> weights = collectionManager.getDescendingWeights();
            if(weights.isEmpty()) ResponseOutputer.appendln("No weights found in the collection!");
            else ResponseOutputer.appendln("Descending weights: " + weights);
            return true;
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
