package commands;

import data.Color;
import data.Coordinates;
import data.Location;
import data.Person;
import exceptions.*;
import interaction.PersonRaw;
import interaction.User;
import utility.CollectionManager;
import utility.DatabaseCollectionManager;
import utility.ResponseOutputer;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * Command 'update'. Updates the information about selected person.
 */
public class UpdateCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public UpdateCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("update", "<ID> {element}", "update collection element value by ID.");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user){
        try {
            if(stringArgument.isEmpty() || objectArgument == null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();

            int id = Integer.parseInt(stringArgument);
            if (id <= 0)throw new NumberFormatException();
            Person oldPerson = collectionManager.getByKey(id);
            if (oldPerson == null) throw new PersonNotFoundException();
            if (!oldPerson.getOwner().equals(user)) throw new PermissionDeniedException();
            if (!databaseCollectionManager.checkPersonUserId(oldPerson.getId(), user)) throw new ManualDatabaseEditException();

            PersonRaw personRaw = (PersonRaw) objectArgument;
            databaseCollectionManager.updatePersonById(id, personRaw);

            String name = personRaw.getName() == null ? oldPerson.getName() : personRaw.getName();
            Coordinates coordinates = personRaw.getCoordinates() == null ? oldPerson.getCoordinates() : personRaw.getCoordinates();
            LocalDateTime creationDate = oldPerson.getCreationDate();
            float height = personRaw.getHeight() == -1 ? oldPerson.getHeight() : personRaw.getHeight();
            ZonedDateTime birthday = personRaw.getBirthday() == null ? oldPerson.getBirthday() : personRaw.getBirthday();
            int weight = personRaw.getWeight() == -1 ? oldPerson.getWeight() : personRaw.getWeight();
            Color hairColor = personRaw.getHairColor() == null ? oldPerson.getHairColor() : personRaw.getHairColor();
            Location location = personRaw.getLocation() == null ? oldPerson.getLocation() : personRaw.getLocation();

            collectionManager.removeFromCollection(oldPerson);
            collectionManager.addToCollection(new Person(
                    id,
                    name,
                    coordinates,
                    creationDate,
                    height,
                    birthday,
                    weight,
                    hairColor,
                    location,
                    user
            ));
            ResponseOutputer.appendln("Person successfully changed!");
            return true;
        } catch (WrongAmountOfElementsException exception){
            ResponseOutputer.appendln("Usage: '" + getName() + " " + getUsage() + "'");
        } catch (CollectionIsEmptyException exception){
            ResponseOutputer.appenderror("Collection is empty!");
        } catch (NumberFormatException exception){
            ResponseOutputer.appenderror("ID must be represented by a number!");
        } catch (PersonNotFoundException exception){
            ResponseOutputer.appenderror("There is no person with this ID in the collection!");
        } catch (ClassCastException exception){
            ResponseOutputer.appenderror("The object passed by the client is invalid!");
        } catch (PermissionDeniedException exception){
            ResponseOutputer.appenderror("Insufficient rights to execute this command!");
            ResponseOutputer.appendln("Objects owned by other users are read-only.");
        } catch (ManualDatabaseEditException exception){
            ResponseOutputer.appenderror("A direct database change has occurred!");
            ResponseOutputer.appendln("Restart the client to avoid possible errors.");
        } catch (DatabaseHandlingException exception){
            ResponseOutputer.appenderror("An error occurred while accessing the database!");
        }
        return false;
    }
}
