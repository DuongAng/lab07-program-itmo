package data;

import interaction.User;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class Person implements  Comparable<Person>{
    public static int MAX_X = 835;
    public static int MAX_Y = 196;
    public static float MIN_height = 0;
    public static int MIN_weight = 0;

    private int id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть
    // уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates;//Поле не может быть null
    private java.time.LocalDateTime creationDate;//Поле не может быть null, Значение этого поля должно
    // генерироваться автоматически
    private float height;//Поле не может быть null, Значение поля должно быть больше 0
    private java.time.ZonedDateTime birthday;//Поле может быть null
    private int weight;//Поле не может быть null, Значение поля должно быть больше 0
    private Color hairColor;//Поле не может быть null
    private Location location;//Поле может быть null
    private User owner;

    public Person(int id, String name, Coordinates coordinates, LocalDateTime creationDate, float height,
                  ZonedDateTime birthday, int weight, Color hairColor, Location location, User owner) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.height = height;
        this.birthday = birthday;
        this.weight = weight;
        this.hairColor = hairColor;
        this.location = location;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public float getHeight() {
        return height;
    }

    public ZonedDateTime getBirthday() {
        return birthday;
    }

    public int getWeight() {
        return weight;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public Location getLocation() {
        return location;
    }

    public User getOwner(){
        return owner;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setBirthday(ZonedDateTime birthday) {
        this.birthday = birthday;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public int compareTo(Person personObj){
        if ( id < personObj.getId()){
            return -1;
        }else if (id > personObj.getId()){
            return 1;
        }else{
            return 0;
        }
    }

    @Override
    public String toString(){
        String info = "";
        info += "Create: " +id;
        info += " (day) " + creationDate.toLocalDate() + " " + creationDate.toLocalTime() + ")";
        info += "\n Name: " + name;
        info += "\n Coordinates: " + coordinates;
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
