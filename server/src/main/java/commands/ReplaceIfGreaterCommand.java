package commands;

import data.Color;
import data.Coordinates;
import data.Location;
import data.Person;
import exceptions.CollectionIsEmptyException;
import exceptions.PersonNotFoundException;
import exceptions.WrongAmountOfElementsException;
import interaction.PersonRaw;
import interaction.User;
import utility.CollectionManager;
import utility.DatabaseCollectionManager;
import utility.ResponseOutputer;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * Command 'replace_if_greater'. Replace the element if it greater.
 */
public class ReplaceIfGreaterCommand extends AbstractCommand{
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public ReplaceIfGreaterCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("replace_if_greater", "{element}", "replace value by key if new value is greater than old.");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    /**
     * Executes the Command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (stringArgument.isEmpty() || objectArgument == null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0 )throw new CollectionIsEmptyException();

            int id = Integer.parseInt(stringArgument);
            if(id <= 0) throw new NumberFormatException();
            Person oldPerson = collectionManager.getByKey(id);
            if (oldPerson == null) throw new PersonNotFoundException();

            PersonRaw personRaw = (PersonRaw) objectArgument;
            String name = personRaw.getName() == null ? oldPerson.getName() : personRaw.getName();
            Coordinates coordinates = personRaw.getCoordinates() == null ? oldPerson.getCoordinates() : personRaw.getCoordinates();
            LocalDateTime creationDate = oldPerson.getCreationDate();
            float height = personRaw.getHeight() == -1 ? oldPerson.getHeight() : personRaw.getHeight();
            ZonedDateTime birthday = personRaw.getBirthday() == null ? oldPerson.getBirthday() : personRaw.getBirthday();
            int weight = personRaw.getWeight() == -1 ? oldPerson.getWeight() : personRaw.getWeight();
            Color hairColor = personRaw.getHairColor() == null ? oldPerson.getHairColor() : personRaw.getHairColor();
            Location location = personRaw.getLocation() == null ? oldPerson.getLocation() : personRaw.getLocation();

            boolean updated = false;
            if (name.compareTo(oldPerson.getName()) > 0) {
                oldPerson.setName(name);
                updated = true;
            }
            if (height > oldPerson.getHeight()) {
                oldPerson.setHeight(height);
                updated = true;
            }
            if (birthday.compareTo(oldPerson.getBirthday()) > 0) {
                oldPerson.setBirthday(birthday);
                updated = true;
            }
            if (weight > oldPerson.getWeight()) {
                oldPerson.setWeight(weight);
                updated = true;
            }
            if (hairColor.compareTo(oldPerson.getHairColor()) > 0) {
                oldPerson.setHairColor(hairColor);
                updated = true;
            }
            if (updated) {
                ResponseOutputer.appendln("Person successfully replaced!");
            } else {
                ResponseOutputer.appendln("No fields were updated for person with ID " + id);
            }
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
        }
        return false;
    }
}
