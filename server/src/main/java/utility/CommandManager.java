package utility;

import commands.Command;
import exceptions.HistoryIsEmptyException;
import interaction.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Operates the commands.
 */
public class CommandManager {
    private final int COMMAND_HISTORY_SIZE = 12;

    private String[] commandHistory = new String[COMMAND_HISTORY_SIZE];
    private List<Command> commands = new ArrayList<>();
    private Command helpCommand;
    private Command infoCommand;
    private Command showCommand;
    private Command insertCommand;
    private Command updateCommand;
    private Command removeKeyCommand;
    private Command clearCommand;
    private Command executeScriptCommand;
    private Command exitCommand;
    private Command historyCommand;
    private Command replaceIfGreaterCommand;
    private Command removeLowerKeyCommand;
    private Command filterStartsWithNameCommand;
    private Command printUniqueLocationCommand;
    private Command printFieldDescendingWeightCommand;
    private Command serverExitCommand;
    private Command loginCommand;
    private Command registerCommand;

    private ReadWriteLock historyLocker = new ReentrantReadWriteLock();
    private ReadWriteLock collectionLocker = new ReentrantReadWriteLock();

    public CommandManager(Command helpCommand, Command infoCommand, Command showCommand, Command insertCommand,
                          Command updateCommand, Command removeKeyCommand, Command clearCommand, Command executeScriptCommand,
                          Command exitCommand, Command historyCommand, Command replaceIfGreaterCommand,
                          Command removeLowerKeyCommand, Command filterStartsWithNameCommand, Command printUniqueLocationCommand,
                          Command printFieldDescendingWeightCommand, Command serverExitCommand, Command loginCommand,
                          Command registerCommand) {
        this.helpCommand = helpCommand;
        this.infoCommand = infoCommand;
        this.showCommand = showCommand;
        this.insertCommand = insertCommand;
        this.updateCommand = updateCommand;
        this.removeKeyCommand = removeKeyCommand;
        this.clearCommand = clearCommand;
        this.executeScriptCommand = executeScriptCommand;
        this.exitCommand = exitCommand;
        this.historyCommand = historyCommand;
        this.replaceIfGreaterCommand = replaceIfGreaterCommand;
        this.removeLowerKeyCommand = removeLowerKeyCommand;
        this.filterStartsWithNameCommand = filterStartsWithNameCommand;
        this.printUniqueLocationCommand = printUniqueLocationCommand;
        this.printFieldDescendingWeightCommand = printFieldDescendingWeightCommand;
        this.serverExitCommand = serverExitCommand;
        this.loginCommand = loginCommand;
        this.registerCommand = registerCommand;

        commands.add(helpCommand);
        commands.add(infoCommand);
        commands.add(showCommand);
        commands.add(insertCommand);
        commands.add(updateCommand);
        commands.add(removeKeyCommand);
        commands.add(clearCommand);
        commands.add(executeScriptCommand);
        commands.add(exitCommand);
        commands.add(historyCommand);
        commands.add(replaceIfGreaterCommand);
        commands.add(removeLowerKeyCommand);
        commands.add(filterStartsWithNameCommand);
        commands.add(printUniqueLocationCommand);
        commands.add(printFieldDescendingWeightCommand);
        commands.add(serverExitCommand);
    }

    /**
     * Adds command to command history.
     *
     * @param commandToStore Command to add.
     * @param user           User object.
     */
    public void addToHistory(String commandToStore, User user) {
        historyLocker.writeLock().lock();
        try {
            for (Command command : commands) {
                if (command.getName().equals(commandToStore)) {
                    for (int i = COMMAND_HISTORY_SIZE - 1; i > 0; i--) {
                        commandHistory[i] = commandHistory[i - 1];
                    }
                    commandHistory[0] = commandToStore + " (" + user.getUsername() + ')';
                }
            }
        } finally {
            historyLocker.writeLock().unlock();
        }
    }

    /**
     * Prints info about the all commands.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @param user           User object.
     * @return Command exit status.
     */
    public boolean help(String stringArgument, Object objectArgument, User user) {
        if (helpCommand.execute(stringArgument, objectArgument, user)) {
            for (Command command : commands) {
                ResponseOutputer.appendtable(command.getName() + " " + command.getUsage(), command.getDescription());
            }
            return true;
        } else return false;
    }

    /**
     * Executes needed command.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @param user           User object.
     * @return Command exit status.
     */
    public boolean info(String stringArgument, Object objectArgument, User user) {
        collectionLocker.readLock().lock();
        try {
            return infoCommand.execute(stringArgument, objectArgument, user);
        } finally {
            collectionLocker.readLock().unlock();
        }
    }

    /**
     * Executes needed command.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @param user           User object.
     * @return Command exit status.
     */
    public boolean show(String stringArgument, Object objectArgument, User user) {
        collectionLocker.readLock().lock();
        try {
            return showCommand.execute(stringArgument, objectArgument, user);
        } finally {
            collectionLocker.readLock().unlock();
        }
    }

    /**
     * Executes needed command.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @param user           User object.
     * @return Command exit status.
     */
    public boolean insert(String stringArgument, Object objectArgument, User user) {
        collectionLocker.writeLock().lock();
        try {
            return insertCommand.execute(stringArgument, objectArgument, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /**
     * Executes needed command.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @param user           User object.
     * @return Command exit status.
     */
    public boolean update(String stringArgument, Object objectArgument, User user) {
        collectionLocker.writeLock().lock();
        try {
            return updateCommand.execute(stringArgument, objectArgument, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /**
     * Executes needed command.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @param user           User object.
     * @return Command exit status.
     */
    public boolean removeKey(String stringArgument, Object objectArgument, User user) {
        collectionLocker.writeLock().lock();
        try {
            return removeKeyCommand.execute(stringArgument, objectArgument, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /**
     * Executes needed command.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @param user           User object.
     * @return Command exit status.
     */
    public boolean clear(String stringArgument, Object objectArgument, User user) {
        collectionLocker.writeLock().lock();
        try {
            return clearCommand.execute(stringArgument, objectArgument, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /**
     * Executes needed command.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @param user           User object.
     * @return Command exit status.
     */
    public boolean exit(String stringArgument, Object objectArgument, User user) {
        return exitCommand.execute(stringArgument, objectArgument, user);
    }

    /**
     * Executes needed command.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @param user           User object.
     * @return Command exit status.
     */
    public boolean executeScript(String stringArgument, Object objectArgument, User user) {
        return executeScriptCommand.execute(stringArgument, objectArgument, user);
    }

    /**
     * Prints the history of used commands.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @param user           User object.
     * @return Command exit status.
     */
    public boolean history(String stringArgument, Object objectArgument, User user) {
        if (historyCommand.execute(stringArgument, objectArgument, user)) {
            historyLocker.readLock().lock();
            try {
                if (commandHistory.length == 0) throw new HistoryIsEmptyException();
                ResponseOutputer.appendln("Последние использованные команды:");
                for (String command : commandHistory) {
                    if (command != null) ResponseOutputer.appendln(" " + command);
                }
                return true;
            } catch (HistoryIsEmptyException exception) {
                ResponseOutputer.appendln("Ни одной команды еще не было использовано!");
            } finally {
                historyLocker.readLock().unlock();
            }
        }
        return false;
    }

    /**
     * Executes needed command.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @param user           User object.
     * @return Command exit status.
     */
    public boolean serverExit(String stringArgument, Object objectArgument, User user) {
        return serverExitCommand.execute(stringArgument, objectArgument, user);
    }

    /**
     * Executes needed command.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @param user           User object.
     * @return Command exit status.
     */
    public boolean login(String stringArgument, Object objectArgument, User user) {
        return loginCommand.execute(stringArgument, objectArgument, user);
    }

    /**
     * Executes needed command.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @param user           User object.
     * @return Command exit status.
     */
    public boolean register(String stringArgument, Object objectArgument, User user) {
        return registerCommand.execute(stringArgument, objectArgument, user);
    }

    /**
     * Executes needed command.
     *
     * @param stringArgument Its string argument.
     * @param objectArgument Its object argument.
     * @param user           User object.
     * @return Command exit status.
     */
    public boolean removeLowerKey(String stringArgument, Object objectArgument, User user) {
        collectionLocker.writeLock().lock();
        try {
            return removeLowerKeyCommand.execute(stringArgument, objectArgument, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /**
     * Executes needs command.
     * @param stringArgument Its argument.
     * @param objectArgument Its object argument.
     * @return Command exit status.
     */
    public boolean replaceIfGreater (String stringArgument, Object objectArgument, User user){
        collectionLocker.writeLock().lock();
        try {
            return replaceIfGreaterCommand.execute(stringArgument, objectArgument, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /**
     * Executes needs command.
     * @param stringArgument Its argument.
     * @param objectArgument Its object argument.
     * @return Command exit status.
     */
    public boolean filterStartsWithName(String stringArgument, Object objectArgument, User user){
        collectionLocker.writeLock().lock();
        try {
            return filterStartsWithNameCommand.execute(stringArgument, objectArgument, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /**
     * Executes needs command.
     * @param stringArgument Its argument.
     * @param objectArgument Its object argument.
     * @return Command exit status.
     */
    public boolean printUniqueLocation(String stringArgument, Object objectArgument, User user){
        collectionLocker.writeLock().lock();
        try {
            return printUniqueLocationCommand.execute(stringArgument, objectArgument, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }

    /**
     * Executes needs command.
     * @param stringArgument Its argument.
     * @param objectArgument Its object argument.
     * @return Command exit status.
     */
    public boolean printFieldDescendingWeight(String stringArgument, Object objectArgument, User user){
        collectionLocker.writeLock().lock();
        try {
            return printFieldDescendingWeightCommand.execute(stringArgument, objectArgument, user);
        } finally {
            collectionLocker.writeLock().unlock();
        }
    }
}
