package interaction;

import data.Color;
import data.Coordinates;
import data.Location;
import data.Person;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Class for get the Person values.
 */
public class PersonRaw implements Serializable {
    private String name;
    private Coordinates coordinates;
    private float height;
    private java.time.ZonedDateTime birthday;
    private int weight;
    private Color hairColor;
    private Location location;

    public PersonRaw(String name, Coordinates coordinates, float height,
                     ZonedDateTime birthday, int weight, Color hairColor, Location location) {
        this.name = name;
        this.coordinates = coordinates;
        this.height = height;
        this.birthday = birthday;
        this.weight = weight;
        this.hairColor = hairColor;
        this.location = location;
    }

    /**
     * @return Name of the person.
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return Coordinates of the person.
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     *
     * @return Height of the person.
     */
    public float getHeight() {
        return height;
    }

    /**
     *
     * @return Birthday of the person.
     */
    public ZonedDateTime getBirthday() {
        return birthday;
    }

    /**
     *
     * @return Weight of the person.
     */
    public int getWeight() {
        return weight;
    }

    /**
     *
     * @return Hair color of the person.
     */
    public Color getHairColor() {
        return hairColor;
    }

    /**
     *
     * @return Location of the person.
     */
    public Location getLocation() {
        return location;
    }

    @Override
    public String toString(){
        String info = "";
        info += "Create: " ;
        info += "\n Name: " + name;
        info += "\n Coordinates: " + coordinates;
        info += "\n Birthday: " + birthday;
        info += "\n Height: " + height;
        info += "\n Weight: " + weight;
        info += "\n Hair color: " + hairColor;
        info += "\n Location: " + location;
        return info;
    }

    @Override
    public int hashCode(){
        return name.hashCode() + coordinates.hashCode() + (int) height + birthday.hashCode()
                + weight + hairColor.hashCode() + location.hashCode();
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if(obj instanceof Person){
            Person personObj = (Person) obj;
            return name.equals(personObj.getName()) && coordinates.equals(personObj.getCoordinates()) &&
                    (height == personObj.getHeight()) && (birthday.equals(personObj.getBirthday())) &&
                    (weight == personObj.getWeight()) && (hairColor == personObj.getHairColor()) &&
                    location.equals(equals(personObj.getLocation()));
        }
        return false;
    }
}
