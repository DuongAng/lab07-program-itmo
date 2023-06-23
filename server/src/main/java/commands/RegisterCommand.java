package commands;

import exceptions.DatabaseHandlingException;
import exceptions.UserAlreadyExists;
import exceptions.WrongAmountOfElementsException;
import interaction.User;
import utility.DatabaseUserManager;
import utility.ResponseOutputer;

/**
 * Command 'register'. Allows the user to register.
 */
public class RegisterCommand extends AbstractCommand{
    private DatabaseUserManager databaseUserManager;

    public RegisterCommand(DatabaseUserManager databaseUserManager) {
        super("register", "", "internal command.");
        this.databaseUserManager = databaseUserManager;
    }

    /**
     * Execute the command.
     *
     * @return Command exit status.
     */
    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user){
        try {
            if (!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            if (databaseUserManager.insertUser(user)) ResponseOutputer.appendln("User " +
                    user.getUsername() + " registeted.");
            else throw new UserAlreadyExists();
            return true;
        } catch (WrongAmountOfElementsException exception){
            ResponseOutputer.appendln("Usage:.....ttt.that is an internal command.....");
        } catch (ClassCastException exception){
            ResponseOutputer.appenderror("The object passed by the client is invalid!");
        } catch (DatabaseHandlingException exception){
            ResponseOutputer.appenderror("An error occurred while accessing the database!");
        } catch (UserAlreadyExists exception){
            ResponseOutputer.appenderror("User: " + user.getUsername() + " already exist!");
        }
        return false;
    }
}
