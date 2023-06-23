package commands;

import exceptions.WrongAmountOfElementsException;
import interaction.User;
import utility.ResponseOutputer;

/**
 * Command 'help'. Display help on available commands.
 */
public class HelpCommand extends AbstractCommand {

    public HelpCommand() {
        super("help", "", "display help on available commands");
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user){
        try{
            if(!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            return true;
        }catch (WrongAmountOfElementsException exception){
            ResponseOutputer.appendln("Usage: '" + getName() + " " + getUsage() + "'");
        }
        return false;
    }
}
