package commands;

import exceptions.WrongAmountOfElementsException;
import interaction.User;
import utility.ResponseOutputer;

/**
 * Command 'server_exit'. Checks for wrong arguments then do nothing.
 */
public class ServerExitCommand extends AbstractCommand{

    public ServerExitCommand(){
        super("server_exit", "", "shut down the server");
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user){
        try {
            if (!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            ResponseOutputer.appendln("Server successfully shut down!");
            return true;
        } catch (WrongAmountOfElementsException exception){
            ResponseOutputer.appendln("Usage: '" + getName() + " " + getUsage() + "'");
        }
        return false;
    }
}
