package commands;

import exceptions.WrongAmountOfElementsException;
import interaction.User;
import utility.CollectionManager;
import utility.ResponseOutputer;

import java.time.LocalDateTime;

/**
 * Command 'info'. Prints information about the collection.
 */
public class InfoCommand extends AbstractCommand{
    private CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager) {
        super("info","", "display information about the collection");
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
            LocalDateTime lasInitTime = collectionManager.getLastInitTime();
            String lastInitTimeString = (lasInitTime == null) ? "initialization has" +
                    "not yet taken place in this session." : lasInitTime.toLocalDate()
                    .toString() +" " + lasInitTime.toLocalTime().toString();

            ResponseOutputer.appendln("Collection details:");
            ResponseOutputer.appendln(" Type: " + collectionManager.collectionType());
            ResponseOutputer.appendln(" Amount of elements: " + collectionManager.collectionSize());
            ResponseOutputer.appendln(" Date of last initialization: " + lastInitTimeString);
            return true;
        }catch(WrongAmountOfElementsException exception){
            ResponseOutputer.appendln("Usage: '" + getName() + " " + getUsage() + "'");
        }
        return false;
    }
}
