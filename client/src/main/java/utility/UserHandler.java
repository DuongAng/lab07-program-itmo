package utility;

import data.Color;
import data.Coordinates;
import data.Location;
import exceptions.CommandUsageException;
import exceptions.IncorrectInputInScriptException;
import exceptions.ScriptRecursionException;
import interaction.PersonRaw;
import interaction.Request;
import interaction.ResponseCode;
import interaction.User;
import run.App;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.ZonedDateTime;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;

/**
 * Receives user requests.
 */
public class UserHandler {
    private final int maxRewriteAttempts = 1;

    private Scanner userScanner;
    private Stack<File> scriptStack = new Stack<>();
    private Stack<Scanner> scannerStack = new Stack<>();

    public UserHandler(Scanner userScanner) {
        this.userScanner = userScanner;
    }

    /**
     * Receives user input.
     *
     * @param serverResponseCode Last server's response code.
     * @param user User object.
     * @return New request to server.
     */
    public Request handle(ResponseCode serverResponseCode, User user) {
        String userInput;
        String[] userCommand;
        ProcessingCode processingCode;
        int rewriteAttempts = 0;
        try {
            do {
                try {
                    if (fileMode() && (serverResponseCode == ResponseCode.ERROR ||
                            serverResponseCode == ResponseCode.SERVER_EXIT))
                        throw new IncorrectInputInScriptException();
                    while (fileMode() && !userScanner.hasNextLine()) {
                        userScanner.close();
                        userScanner = scannerStack.pop();
                        scriptStack.pop();
                    }
                    if (fileMode()) {
                        userInput = userScanner.nextLine();
                        if (!userInput.isEmpty()) {
                            Outputer.print(App.PS1);
                            Outputer.println(userInput);
                        }
                    } else {
                        Outputer.print(App.PS1);
                        userInput = userScanner.nextLine();
                    }
                    userCommand = (userInput.trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                } catch (NoSuchElementException | IllegalStateException exception) {
                    Outputer.println();
                    Outputer.printerror("An error occurred while entering the command!");
                    userCommand = new String[]{"", ""};
                    rewriteAttempts++;
                    if (rewriteAttempts >= maxRewriteAttempts) {
                        Outputer.printerror("Exceeded number of input attempts!");
                        System.exit(0);
                    }
                }
                processingCode = processCommand(userCommand[0], userCommand[1]);
            } while (processingCode == ProcessingCode.ERROR && !fileMode() || userCommand[0].isEmpty());
            try {
                if (fileMode() && (serverResponseCode == ResponseCode.ERROR || processingCode == ProcessingCode.ERROR))
                    throw new IncorrectInputInScriptException();
                switch (processingCode) {
                    case OBJECT:
                        PersonRaw personInsertRaw = generatePersonInsert();
                        return new Request(userCommand[0], userCommand[1], personInsertRaw, user);
                    case UPDATE_OBJECT:
                        PersonRaw personUpdateRaw = generatePersonUpdate();
                        return new Request(userCommand[0], userCommand[1], personUpdateRaw, user);
                    case SCRIPT:
                        File scriptFile = new File(userCommand[1]);
                        if (!scriptFile.exists()) throw new FileNotFoundException();
                        if (!scriptStack.isEmpty() && scriptStack.search(scriptFile) != -1)
                            throw new ScriptRecursionException();
                        scannerStack.push(userScanner);
                        scriptStack.push(scriptFile);
                        userScanner = new Scanner(scriptFile);
                        Outputer.println("Executing a script '" + scriptFile.getName() + "'...");
                        break;
                }
            } catch (FileNotFoundException exception) {
                Outputer.printerror("Script file not found!");
            } catch (ScriptRecursionException exception) {
                Outputer.printerror("Scripts cannot be called recursively!");
                throw new IncorrectInputInScriptException();
            }
        } catch (IncorrectInputInScriptException exception) {
            Outputer.printerror(exception.getMessage());
            Outputer.printerror("Script execution aborted!");
            while (!scannerStack.isEmpty()) {
                userScanner.close();
                userScanner = scannerStack.pop();
            }
            scriptStack.clear();
            return new Request(user);
        }
        return new Request(userCommand[0], userCommand[1], null, user);
    }

    /**
     * Processes the entered command.
     *
     * @return Status of code.
     */
    private ProcessingCode processCommand(String command, String commandArgument) {
        try {
            switch (command) {
                case "":
                    return ProcessingCode.ERROR;
                case "help":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "info":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "show":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "insert":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException("{element}");
                    return ProcessingCode.OBJECT;
                case "update":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<ID> {element}");
                    return ProcessingCode.UPDATE_OBJECT;
                case "remove_key":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<ID>");
                    break;
                case "clear":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "execute_script":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<file_name>");
                    return ProcessingCode.SCRIPT;
                case "exit":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "replace_if_greater":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException("{element}");
                    return ProcessingCode.OBJECT;
                case "remove_lower_key":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException("{element}");
                    return ProcessingCode.OBJECT;
                case "history":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "print_unique_location":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "print_field_descending_weight":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "filter_starts_with_name":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<name>");
                    break;
                case "server_exit":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                default:
                    Outputer.println("Command '" + command + "' not found. Type 'help' for help.");
                    return ProcessingCode.ERROR;
            }
        } catch (CommandUsageException exception) {
            if (exception.getMessage() != null) command += " " + exception.getMessage();
            Outputer.println("Usage: '" + command + "'");
            return ProcessingCode.ERROR;
        }
        return ProcessingCode.OK;
    }

    /**
     * Generates Person to insert.
     *
     * @return Person to insert.
     * @throws IncorrectInputInScriptException When something went wrong in script.
     */
    private PersonRaw generatePersonInsert() throws IncorrectInputInScriptException {
        PersonAsker personAsker = new PersonAsker(userScanner);
        if (fileMode()) personAsker.setFileMode();
        return new PersonRaw(
                personAsker.askName(),
                personAsker.askCoordinates(),
                personAsker.askHeight(),
                personAsker.askBirthday(),
                personAsker.askWeight(),
                personAsker.askColor(),
                personAsker.askLocation()
        );
    }

    /**
     * Generates Person to update.
     *
     * @return Person to update.
     * @throws IncorrectInputInScriptException When something went wrong in script.
     */
    private PersonRaw generatePersonUpdate() throws IncorrectInputInScriptException {
        PersonAsker personAsker = new PersonAsker(userScanner);
        if (fileMode()) personAsker.setFileMode();
        String name = personAsker.askQuestion("Want to change the person's name?") ?
                personAsker.askName() : null;
        Coordinates coordinates = personAsker.askQuestion("Want to change the coordinates of a person?") ?
                personAsker.askCoordinates() : null;
        float height = personAsker.askQuestion("Want to change the person's height?") ?
                personAsker.askHeight() : -1;
        ZonedDateTime birthday = personAsker.askQuestion("Want to change the person's birthday?") ?
                personAsker.askBirthday() : null;
        int weight = personAsker.askQuestion("Want to change the person's weight?") ?
                personAsker.askWeight() : null;
        Color color = personAsker.askQuestion("Want to change the person's hair color?") ?
                personAsker.askColor() : null;
        Location location = personAsker.askQuestion("Want to change the person's location?") ?
                personAsker.askLocation() : null;
        return new PersonRaw(
                name,
                coordinates,
                height,
                birthday,
                weight,
                color,
                location
        );
    }

    /**
     * Checks if UserHandler is in file mode now.
     *
     * @return Is UserHandler in file mode now boolean.
     */
    private boolean fileMode() {
        return !scannerStack.isEmpty();
    }
}