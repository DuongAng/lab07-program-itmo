package utility;

import data.Location;
import data.Person;
import exceptions.DatabaseHandlingException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CollectionManager {
    private HashMap<Integer, Person> personCollection = new HashMap<>();
    private LocalDateTime lastInitTime;
    private DatabaseCollectionManager databaseCollectionManager;

    public CollectionManager(DatabaseCollectionManager databaseCollectionManager) {
        this.databaseCollectionManager = databaseCollectionManager;

        loadCollection();
    }

    /**
     * @return Persons collection.
     */
    public HashMap<Integer, Person> getCollection(){
        return personCollection;
    }

    /**
     * @return Last initialization time or null if there wasn't initialization.
     */
    public LocalDateTime getLastInitTime(){
        return lastInitTime;
    }

    /**
     * @return Name of the collection's type.
     */
    public String collectionType(){
        return personCollection.getClass().getName();
    }

    /**
     * @return Size of the collection.
     */
    public int collectionSize(){
        return personCollection.size();
    }

    /**
     * @return The first element of the collection or null if collection is empty.
     */
    public Person getFirst(){
        if(personCollection.isEmpty()) return null;
        Integer firstkey = Collections.min(personCollection.keySet());
        return personCollection.get(firstkey);
    }

    /**
     * @param id ID of the person.
     * @return A person by his ID or null if person isn't found.
     */
    public Person getByKey(int id){
        return personCollection.values()
                .stream()
                .filter(person -> person.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Adds a new person to collection.
     * @param person A person to add.
     */
    public void addToCollection(Person person){
        personCollection.put(person.getId(), person);
    }

    /**
     * Removes a new person to collection.
     * @param person A person to remove.
     */
    public void removeFromCollection(Person person){
        personCollection.remove(person.getId());
    }

    /**
     * Clears the collection.
     */
    public void clearCollection() {
        personCollection.clear();
    }

    /**
     * Generates next ID. IT'll be the bigger one +1.
     * @retrun Next ID.
     */
    public int generateNextId(){
        if (personCollection.isEmpty()) return 1;
        else {
            int maxId = Collections.max(personCollection.values(), Comparator.comparing(Person :: getId)).getId();
            return maxId + 1;
        }
    }

    /**
     * Loads the collection from file.
     */
    private void loadCollection(){
        try {
            personCollection = databaseCollectionManager.getCollection();
            lastInitTime = LocalDateTime.now();
            Outputer.println("Collection loaded!");
        } catch (DatabaseHandlingException exception){
            personCollection = new HashMap<>();
            Outputer.printerror("Collection can not loaded!");
        }
    }

    /**
     * @param nameToFilter Name to filter by.
     * @return Information about valid person or empty string, if there's no such person.
     */
    public String personFilterInfo(String nameToFilter){
        return personCollection.values().stream().filter(person -> person.getName().equals(nameToFilter))
                .map(person -> person + "\n\n")
                .collect(Collectors.joining())
                .trim();
    }

    /**
     * @return Return value unique location.
     */
    public List<Location> uniqueLocation() {
        Map<Location, Long> locationCounts = personCollection.values()
                .stream()
                .map(Person::getLocation)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return locationCounts.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * @return Return value descending weight.
     */
    public List<Float> getDescendingWeights() {
        return personCollection.values()
                .stream()
                .map(Person::getWeight)
                .map(Float::valueOf)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    /**
     * @return A person by his value or null if person isn't found.
     */
    public Person getByValue(Person personToFind){
        return personCollection.values()
                .stream()
                .filter(person -> person.equals(personToFind))
                .findFirst()
                .orElse(null);
    }

    /**
     * @param personFromCollection A person to remove with.
     * Remove person lower than the selected one.
     */
    public void removeLower(Person personFromCollection){
        personCollection.entrySet().removeIf(entry -> entry.getValue().compareTo(personFromCollection) < 0);
    }

    /**
     * Clears the collection.
     */
    public void clearCollecton(){
        personCollection.clear();
    }

    /**
     * @return Collection content or corresponding string if collection is empty.
     */
    public String showCollection(){
        if(personCollection.isEmpty()) return "Collection is empty!";
        return personCollection.values().stream().reduce("", (sum, m) -> sum += m + "\n\n"
                , (sum1, sum2) -> sum1 + sum2).trim();
    }

    @Override
    public String toString(){
        if (personCollection.isEmpty()) return "Collection is empty!";

        String info = "";
        for(Person person : personCollection.values()){
            info += person;
            if(person != personCollection.values().stream().reduce((a,b) -> b).orElse(null)) info += "\n\n";
        }
        return info;
    }
}
