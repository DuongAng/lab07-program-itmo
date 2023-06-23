package utility;

import data.Color;
import data.Coordinates;
import data.Location;
import data.Person;
import exceptions.DatabaseHandlingException;
import interaction.PersonRaw;
import interaction.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;

public class DatabaseCollectionManager {
    //PERSON_TABLE
    private final String SELECT_ALL_PERSON = "SELECT*FROM " + DatabaseHandler.PERSON_TABLE;
    private final String SELECT_PERSON_BY_ID = SELECT_ALL_PERSON + " WHERE " + DatabaseHandler.PERSON_TABLE_ID_COLUMN +
            " = ? ";
    private final String SELECT_PERSON_BY_ID_AND_USER_ID = SELECT_PERSON_BY_ID + " AND " +
            DatabaseHandler.PERSON_TABLE_USER_ID_COLUMN + " = ? ";
    private final String INSERT_PERSON = "INSERT INTO " +
            DatabaseHandler.PERSON_TABLE + " (" +
            DatabaseHandler.PERSON_TABLE_NAME_COLUMN + ", " +
            DatabaseHandler.PERSON_TABLE_CREATION_DATE_COLUMN + ", " +
            DatabaseHandler.PERSON_TABLE_HEIGHT_COLUMN + ", " +
            DatabaseHandler.PERSON_TABLE_BIRTHDAY_COLUMN + ", " +
            DatabaseHandler.PERSON_TABLE_WEIGHT_COLUMN + ", " +
            DatabaseHandler.PERSON_TABLE_HAIR_COLOR_COLUMN + ", " +
            DatabaseHandler.PERSON_TABLE_LOCATION_COLUMN + ", " +
            DatabaseHandler.PERSON_TABLE_USER_ID_COLUMN + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private final String DELETE_PERSON_BY_ID = "DELETE FROM " + DatabaseHandler.PERSON_TABLE +
            " WHERE " + DatabaseHandler.PERSON_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_PERSON_NAME_BY_ID = "UPDATE " + DatabaseHandler.PERSON_TABLE + " SET " +
            DatabaseHandler.PERSON_TABLE_NAME_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.PERSON_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_PERSON_HEIGHT_BY_ID = "UPDATE " + DatabaseHandler.PERSON_TABLE + " SET " +
            DatabaseHandler.PERSON_TABLE_HEIGHT_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.PERSON_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_PERSON_BIRTHDAY_BY_ID = "UPDATE " + DatabaseHandler.PERSON_TABLE + " SET " +
            DatabaseHandler.PERSON_TABLE_BIRTHDAY_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.PERSON_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_PERSON_HAIR_COLOR_BY_ID = "UPDATE " + DatabaseHandler.PERSON_TABLE + " SET " +
            DatabaseHandler.PERSON_TABLE_HAIR_COLOR_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.PERSON_TABLE_ID_COLUMN + " = ?";
    private final String UPDATE_PERSON_WEIGHT_BY_ID = "UPDATE " + DatabaseHandler.PERSON_TABLE + " SET " +
            DatabaseHandler.PERSON_TABLE_WEIGHT_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.PERSON_TABLE_ID_COLUMN + " = ?";
    //COORDINATES_TABLE
    private final String SELECT_ALL_COORDINATES = "SELECT * FROM " + DatabaseHandler.COORDINATES_TABLE;
    private final String SELECT_COORDINATES_BY_PERSON_ID = SELECT_ALL_COORDINATES +
            " WHERE " + DatabaseHandler.COORDINATES_TABLE_PERSON_ID_COLUMN + " = ?";
    private final String INSERT_COORDINATES = "INSERT INTO " +
            DatabaseHandler.COORDINATES_TABLE + " (" +
            DatabaseHandler.COORDINATES_TABLE_PERSON_ID_COLUMN + ", " +
            DatabaseHandler.COORDINATES_TABLE_X_COLUMN + ", " +
            DatabaseHandler.COORDINATES_TABLE_Y_COLUMN + ") VALUES (?, ?, ?)";
    private final String UPDATE_COORDINATES_BY_PERSON_ID = "UPDATE " + DatabaseHandler.COORDINATES_TABLE + " SET " +
            DatabaseHandler.COORDINATES_TABLE_X_COLUMN + " = ?, " +
            DatabaseHandler.COORDINATES_TABLE_Y_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.COORDINATES_TABLE_PERSON_ID_COLUMN + " = ?";
    //LOCATION_TABLE
    private final String SELECT_ALL_LOCATION = "SELECT*FROM " + DatabaseHandler.LOCATION_TABLE;
    private final String SELECT_LOCATION_BY_PERSON_ID = SELECT_ALL_LOCATION +
            " WHERE " + DatabaseHandler.LOCATION_TABLE_ID_COLUMN + " = ?";
    private final String INSERT_LOCATION = "INSERT INTO " +
            DatabaseHandler.LOCATION_TABLE + " (" +
            DatabaseHandler.LOCATION_TABLE_X_COLUMN + ", " +
            DatabaseHandler.LOCATION_TABLE_Y_COLUMN + ", " +
            DatabaseHandler.LOCATION_TABLE_Z_COLUMN + ") VALUES (?, ?, ?)";
    private final String UPDATE_LOCATION_BY_PERSON_ID = "UPDATE " + DatabaseHandler.LOCATION_TABLE + " SET " +
            DatabaseHandler.LOCATION_TABLE_X_COLUMN + " = ?, " +
            DatabaseHandler.LOCATION_TABLE_Y_COLUMN + " = ?, " +
            DatabaseHandler.LOCATION_TABLE_Z_COLUMN + " = ?" + " WHERE " +
            DatabaseHandler.LOCATION_TABLE_ID_COLUMN + " = ?";
    private final String DELETE_LOCATION_BY_ID = "DELETE FROM " + DatabaseHandler.LOCATION_TABLE +
            " WHERE " + DatabaseHandler.LOCATION_TABLE_ID_COLUMN + " = ?";

    private DatabaseHandler databaseHandler;
    private DatabaseUserManager databaseUserManager;

    public DatabaseCollectionManager(DatabaseHandler databaseHandler, DatabaseUserManager databaseUserManager) {
        this.databaseHandler = databaseHandler;
        this.databaseUserManager = databaseUserManager;
    }

    /**
     * Create person
     *
     * @param resultSet Result set parametres of person.
     * @return New person.
     * @throws SQLException When there's exception inside.
     */
    private Person createPerson(ResultSet resultSet) throws SQLException{
        int id = resultSet.getInt(DatabaseHandler.PERSON_TABLE_ID_COLUMN);
        String name = resultSet.getString(DatabaseHandler.PERSON_TABLE_NAME_COLUMN);
        Coordinates coordinates = getCoordinatesByPersonId(id);
        LocalDateTime creationDate = resultSet.getTimestamp(DatabaseHandler.PERSON_TABLE_CREATION_DATE_COLUMN).toLocalDateTime();
        float height = resultSet.getFloat(DatabaseHandler.PERSON_TABLE_HEIGHT_COLUMN);
        ZonedDateTime birthday = resultSet.getTimestamp(DatabaseHandler.PERSON_TABLE_BIRTHDAY_COLUMN).toInstant().atZone(ZoneId.systemDefault());
        int weight = resultSet.getInt(DatabaseHandler.PERSON_TABLE_WEIGHT_COLUMN);
        Color hairColor = Color.valueOf(resultSet.getString(DatabaseHandler.PERSON_TABLE_HAIR_COLOR_COLUMN));
        Location location = getLocationByPersonId(id);
        User owner = databaseUserManager.getUserById(resultSet.getLong(DatabaseHandler.PERSON_TABLE_USER_ID_COLUMN));
        return new Person(
                id,
                name,
                coordinates,
                creationDate,
                height,
                birthday,
                weight,
                hairColor,
                location,
                owner
        );
    }

    /**
     * @return List of Person.
     * @thorw DatabaseHandLingException When there's exception inside.
     */
    public HashMap<Integer, Person> getCollection() throws DatabaseHandlingException{
        HashMap<Integer, Person> personList = new HashMap<>();
        PreparedStatement preparedSelectAllStatement = null;
        try {
            preparedSelectAllStatement = databaseHandler.getPreparedStatement(SELECT_ALL_PERSON, false);
            ResultSet resultSet = preparedSelectAllStatement.executeQuery();
            while (resultSet.next()){
                Person person = createPerson(resultSet);
                personList.put(person.getId(), person);
            }
        } catch (SQLException exception){
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectAllStatement);
        }
        return personList;
    }

    /**
     * @param personId Id of person.
     * @return Location.
     * @throws SQLException When there's exception inside.
     */
    private Location getLocationByPersonId(int personId) throws SQLException{
        Location location;
        PreparedStatement preparedSelectPersonByIdStatement = null;
        try {
            preparedSelectPersonByIdStatement = databaseHandler.getPreparedStatement(SELECT_LOCATION_BY_PERSON_ID, false);
            preparedSelectPersonByIdStatement.setInt(1, personId);
            ResultSet resultSet = preparedSelectPersonByIdStatement.executeQuery();
            if (resultSet.next()){
                location = new Location(
                        resultSet.getInt(DatabaseHandler.LOCATION_TABLE_X_COLUMN),
                        resultSet.getLong(DatabaseHandler.LOCATION_TABLE_Y_COLUMN),
                        resultSet.getInt(DatabaseHandler.LOCATION_TABLE_Z_COLUMN)
                );
            } else throw new SQLException();
        } catch (SQLException exception){
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectPersonByIdStatement);
        }
        return location;
    }

    /**
     * @param personId Id of person.
     * @return coordinates.
     * @throws SQLException When there's exception inside.
     */
    private Coordinates getCoordinatesByPersonId(int personId) throws SQLException{
        Coordinates coordinates;
        PreparedStatement preparedSelectCoordinatesByPersonIdStatement = null;
        try {
            preparedSelectCoordinatesByPersonIdStatement = databaseHandler.getPreparedStatement(SELECT_COORDINATES_BY_PERSON_ID, false);
            preparedSelectCoordinatesByPersonIdStatement.setInt(1, personId);
            ResultSet resultSet = preparedSelectCoordinatesByPersonIdStatement.executeQuery();
            if (resultSet.next()){
                coordinates = new Coordinates(
                        resultSet.getLong(DatabaseHandler.COORDINATES_TABLE_X_COLUMN),
                        resultSet.getFloat(DatabaseHandler.COORDINATES_TABLE_Y_COLUMN)
                );
            } else throw new SQLException();
        } catch (SQLException exception){
            throw new SQLException(exception);
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectCoordinatesByPersonIdStatement);
        }
        return coordinates;
    }

    /**
     * @param personRaw Person raw.
     * @param user User.
     * @return Person.
     * @throws DatabaseHandlingException When there's exception inside.
     */
    public Person insertPerson(PersonRaw personRaw, User user) throws DatabaseHandlingException {
        Person person;
        PreparedStatement preparedInsertPersonStatement = null;
        PreparedStatement preparedInsertCoordinatesStatement = null;
        PreparedStatement preparedInsertLocationStatement = null;
        try {
            databaseHandler.setCommitMode();
            databaseHandler.setSavepoint();

            LocalDateTime creationTime = LocalDateTime.now();

            preparedInsertPersonStatement = databaseHandler.getPreparedStatement(INSERT_PERSON, true);
            preparedInsertCoordinatesStatement = databaseHandler.getPreparedStatement(INSERT_COORDINATES, true);
            preparedInsertLocationStatement = databaseHandler.getPreparedStatement(INSERT_LOCATION, true);

            preparedInsertLocationStatement.setInt(1, personRaw.getLocation().getX());
            preparedInsertLocationStatement.setLong(2, personRaw.getLocation().getY());
            preparedInsertLocationStatement.setInt(3, personRaw.getLocation().getZ());
            if (preparedInsertLocationStatement.executeUpdate() == 0) throw new SQLException();
            ResultSet generatedLocationKeys = preparedInsertLocationStatement.getGeneratedKeys();
            long locationId;
            if (generatedLocationKeys.next()) {
                locationId = generatedLocationKeys.getLong(1);
            } else throw new SQLException();

            preparedInsertPersonStatement.setString(1, personRaw.getName());
            preparedInsertPersonStatement.setTimestamp(2, Timestamp.valueOf(creationTime));
            preparedInsertPersonStatement.setFloat(3, personRaw.getHeight());
            preparedInsertPersonStatement.setTimestamp(4, Timestamp.from(personRaw.getBirthday().toInstant()));
            preparedInsertPersonStatement.setInt(5, personRaw.getWeight());
            preparedInsertPersonStatement.setString(6, personRaw.getHairColor().toString());
            preparedInsertPersonStatement.setLong(7, locationId);
            preparedInsertPersonStatement.setLong(8, databaseUserManager.getUserIdByUsername(user));
            if (preparedInsertPersonStatement.executeUpdate() == 0) throw new SQLException();
            ResultSet generatedPersonKeys = preparedInsertPersonStatement.getGeneratedKeys();
            int personId;
            if (generatedPersonKeys.next()) {
                personId = generatedPersonKeys.getInt(1);
            } else throw new SQLException();

            preparedInsertCoordinatesStatement.setInt(1, personId);
            preparedInsertCoordinatesStatement.setLong(2, personRaw.getCoordinates().getX());
            preparedInsertCoordinatesStatement.setFloat(3, personRaw.getCoordinates().getY());
            if (preparedInsertCoordinatesStatement.executeUpdate() == 0) throw new SQLException();
            person = new Person(
                    personId,
                    personRaw.getName(),
                    personRaw.getCoordinates(),
                    creationTime,
                    personRaw.getHeight(),
                    personRaw.getBirthday(),
                    personRaw.getWeight(),
                    personRaw.getHairColor(),
                    personRaw.getLocation(),
                    user
            );

            databaseHandler.commit();
            return person;
        } catch (SQLException exception) {
            databaseHandler.rollback();
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedInsertPersonStatement);
            databaseHandler.closePreparedStatement(preparedInsertCoordinatesStatement);
            databaseHandler.closePreparedStatement(preparedInsertLocationStatement);
            databaseHandler.setNormalMode();
        }
    }

    /**
     * @param personRaw Person raw.
     * @param personId  Id of Person.
     * @throws DatabaseHandlingException When there's exception inside.
     */
    public void updatePersonById(int personId, PersonRaw personRaw) throws DatabaseHandlingException {
        PreparedStatement preparedUpdatepersonNameByIdStatement = null;
        PreparedStatement preparedUpdatepersonHeightByIdStatement = null;
        PreparedStatement preparedUpdatepersonBirthdayByIdStatement = null;
        PreparedStatement preparedUpdatepersonWeightByIdStatement = null;
        PreparedStatement preparedUpdatepersonHairColorByIdStatement = null;
        PreparedStatement preparedUpdateCoordinatesBypersonIdStatement = null;
        PreparedStatement preparedUpdateLocationByIdStatement = null;
        try {
            databaseHandler.setCommitMode();
            databaseHandler.setSavepoint();

            preparedUpdatepersonNameByIdStatement = databaseHandler.getPreparedStatement(UPDATE_PERSON_NAME_BY_ID, false);
            preparedUpdatepersonHeightByIdStatement = databaseHandler.getPreparedStatement(UPDATE_PERSON_HEIGHT_BY_ID, false);
            preparedUpdatepersonBirthdayByIdStatement = databaseHandler.getPreparedStatement(UPDATE_PERSON_BIRTHDAY_BY_ID, false);
            preparedUpdatepersonWeightByIdStatement = databaseHandler.getPreparedStatement(UPDATE_PERSON_WEIGHT_BY_ID, false);
            preparedUpdatepersonHairColorByIdStatement = databaseHandler.getPreparedStatement(UPDATE_PERSON_HAIR_COLOR_BY_ID, false);
            preparedUpdateCoordinatesBypersonIdStatement = databaseHandler.getPreparedStatement(UPDATE_COORDINATES_BY_PERSON_ID, false);
            preparedUpdateLocationByIdStatement = databaseHandler.getPreparedStatement(UPDATE_LOCATION_BY_PERSON_ID, false);

            if (personRaw.getName() != null) {
                preparedUpdatepersonNameByIdStatement.setString(1, personRaw.getName());
                preparedUpdatepersonNameByIdStatement.setInt(2, personId);
                if (preparedUpdatepersonNameByIdStatement.executeUpdate() == 0) throw new SQLException();
            }
            if (personRaw.getCoordinates() != null) {
                preparedUpdateCoordinatesBypersonIdStatement.setLong(1, personRaw.getCoordinates().getX());
                preparedUpdateCoordinatesBypersonIdStatement.setFloat(2, personRaw.getCoordinates().getY());
                preparedUpdateCoordinatesBypersonIdStatement.setInt(3, personId);
                if (preparedUpdateCoordinatesBypersonIdStatement.executeUpdate() == 0) throw new SQLException();

            }
            if (personRaw.getHeight() != -1) {
                preparedUpdatepersonHeightByIdStatement.setFloat(1, personRaw.getHeight());
                preparedUpdatepersonHeightByIdStatement.setInt(2, personId);
                if (preparedUpdatepersonHeightByIdStatement.executeUpdate() == 0) throw new SQLException();
            }
            if (personRaw.getBirthday() != null) {
                preparedUpdatepersonBirthdayByIdStatement.setTimestamp(1, Timestamp.from(personRaw.getBirthday().toInstant()));
                preparedUpdatepersonBirthdayByIdStatement.setInt(2, personId);
                if (preparedUpdatepersonBirthdayByIdStatement.executeUpdate() == 0) throw new SQLException();
            }
            if (personRaw.getWeight() != -1) {
                preparedUpdatepersonWeightByIdStatement.setInt(1, personRaw.getWeight());
                preparedUpdatepersonWeightByIdStatement.setInt(2, personId);
                if (preparedUpdatepersonWeightByIdStatement.executeUpdate() == 0) throw new SQLException();
            }
            if (personRaw.getHairColor() != null) {
                preparedUpdatepersonHairColorByIdStatement.setString(1, personRaw.getHairColor().toString());
                preparedUpdatepersonHairColorByIdStatement.setInt(2, personId);
                if (preparedUpdatepersonHairColorByIdStatement.executeUpdate() == 0) throw new SQLException();
            }
            if (personRaw.getLocation() != null) {
                preparedUpdateLocationByIdStatement.setInt(1, personRaw.getLocation().getX());
                preparedUpdateLocationByIdStatement.setLong(2, personRaw.getLocation().getY());
                preparedUpdateLocationByIdStatement.setInt(3, personRaw.getLocation().getZ());
                preparedUpdateLocationByIdStatement.setInt(4, personId);
                if (preparedUpdateLocationByIdStatement.executeUpdate() == 0) throw new SQLException();
            }

            databaseHandler.commit();
        } catch (SQLException exception) {
            databaseHandler.rollback();
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedUpdatepersonNameByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdatepersonHeightByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdatepersonBirthdayByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdatepersonWeightByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdatepersonHairColorByIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateCoordinatesBypersonIdStatement);
            databaseHandler.closePreparedStatement(preparedUpdateLocationByIdStatement);
            databaseHandler.setNormalMode();
        }
    }

    /**
     * Delete person by id.
     *
     * @param personId Id of person.
     * @throws DatabaseHandlingException When there's exception inside.
     */
    public void deletePersonById(int personId) throws DatabaseHandlingException {
        PreparedStatement preparedDeleteLocationByIdStatement = null;
        try {
            preparedDeleteLocationByIdStatement = databaseHandler.getPreparedStatement(DELETE_LOCATION_BY_ID, false);
            preparedDeleteLocationByIdStatement.setInt(1, personId);
            if (preparedDeleteLocationByIdStatement.executeUpdate() == 0) Outputer.println(3);
        } catch (SQLException exception) {
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedDeleteLocationByIdStatement);
        }
    }

    /**
     * Checks person user id.
     *
     * @param personId Id of person.
     * @param user Owner of person.
     * @throws DatabaseHandlingException When there's exception inside.
     * @return Is everything ok.
     */
    public boolean checkPersonUserId(int personId, User user) throws DatabaseHandlingException {
        PreparedStatement preparedSelectPersonByIdAndUserIdStatement = null;
        try {
            preparedSelectPersonByIdAndUserIdStatement = databaseHandler.getPreparedStatement(SELECT_PERSON_BY_ID_AND_USER_ID, false);
            preparedSelectPersonByIdAndUserIdStatement.setLong(1, personId);
            preparedSelectPersonByIdAndUserIdStatement.setLong(2, databaseUserManager.getUserIdByUsername(user));
            ResultSet resultSet = preparedSelectPersonByIdAndUserIdStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException exception) {
            throw new DatabaseHandlingException();
        } finally {
            databaseHandler.closePreparedStatement(preparedSelectPersonByIdAndUserIdStatement);
        }
    }

    /**
     * Clear the collection.
     *
     * @throws DatabaseHandlingException When there's exception inside.
     */
    public void clearCollection() throws DatabaseHandlingException {
        HashMap<Integer, Person> personList = getCollection();
        for (Person person : personList.values()) {
            deletePersonById(person.getId());
        }
    }
}
