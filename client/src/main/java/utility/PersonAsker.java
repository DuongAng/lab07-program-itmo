package utility;

import data.Color;
import data.Coordinates;
import data.Location;
import data.Person;
import exceptions.IncorrectInputInScriptException;
import exceptions.MustBeNotEmptyException;
import exceptions.NotInDeclaredLimitsException;
import run.App;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static data.Person.*;

public class PersonAsker {
    private Scanner userScanner;
    private boolean fileMode;


    public PersonAsker(Scanner userScanner){
        this.userScanner = userScanner;
        fileMode = false;
    }

    /**
     * Set a scanner to scan user input.
     * @param userScanner Scanner to set.
     */
    public void setUserScanner(Scanner userScanner){
        this.userScanner = userScanner;
    }

    /**
     * @return Scanner, which uses for user input.
     */
    public Scanner getUserScanner(){
        return userScanner;
    }

    /**
     * Sets person asker mode to 'File Mode'.
     */
    public void setFileMode() {
        this.fileMode = true;
    }

    /**
     * Set person asker mode to 'User Mode'.
     */
    public void setUserMode(){
        this.fileMode = false;
    }

    /**
     * Ask a user the person's name.
     * @return that name.
     * @throws  IncorrectInputInScriptException script is running and something goes wrong
     */
    public String askName() throws IncorrectInputInScriptException {
        String name;
        while (true){
            try{
                Outputer.println("Enter your name:");
                Outputer.print(App.PS2);
                name = userScanner.nextLine().trim();
                if(fileMode) Outputer.println(name);
                if(name.equals("")) throw new MustBeNotEmptyException();
                break;
            }catch (NoSuchElementException exception){
                Outputer.printerror("Name not recognized!");
                if(fileMode) throw new IncorrectInputInScriptException();
            }catch (MustBeNotEmptyException exception){
                Outputer.printerror("Name cannot be empty!");
                if(fileMode) throw new IncorrectInputInScriptException();
            }catch(IllegalStateException exception){
                Outputer.printerror("Unexpected error!");
                System.exit(0);
            }
        }
        return name;
    }

    /**
     * Asks a user the person's X coordinate.
     * @return Person's X coordinate.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public Long askX() throws IncorrectInputInScriptException{
        String strX;
        Long x;
        while(true){
            try{
                Outputer.println("Enter coordinate X < " + (Person.MAX_X + 1) + ":");
                Outputer.print(App.PS2);
                strX = userScanner.nextLine().trim();
                if(fileMode) Outputer.println(strX);
                x = Long.parseLong(strX);
                if(x > Person.MAX_X) throw new NotInDeclaredLimitsException();
                break;
            }catch(NoSuchElementException exception){
                Outputer.printerror("X coordinate not recognized!");
                if(fileMode) throw new IncorrectInputInScriptException();
            }catch(NotInDeclaredLimitsException exception){
                Outputer.printerror("X coordinate cannot exceed " + Person.MAX_X +"!");
                if(fileMode) throw new IncorrectInputInScriptException();
            }catch(NumberFormatException exception){
                Outputer.printerror("X coordinate must be represented by a number!");
                if(fileMode) throw new IncorrectInputInScriptException();
            }catch (NullPointerException | IllegalStateException exception){
                Outputer.printerror("Unexpected error!");
                System.exit(0);
            }
        }
        return x;
    }

    /**
     * Asks a user the person's Y coordinate.
     * @return Person's Y coordinate.
     * @throws  IncorrectInputInScriptException script is running and something goes wrong.
     */
    public Float askY() throws IncorrectInputInScriptException{
        String strY;
        Float y;
        while(true){
            try{
                Outputer.println("Enter coordinate Y < " + (MAX_Y + 1) + ":");
                Outputer.print(App.PS2);
                strY = userScanner.nextLine().trim();
                if(fileMode) Outputer.println(strY);
                y = Float.parseFloat(strY);
                if(y > MAX_Y) throw new NotInDeclaredLimitsException();
                break;
            }catch(NoSuchElementException exception){
                Outputer.printerror("Y coordinate not recognized!");
                if(fileMode) throw new IncorrectInputInScriptException();
            }catch(NotInDeclaredLimitsException exception){
                Outputer.printerror("Y coordinate cannot exceed " + MAX_Y +"!");
                if(fileMode) throw new IncorrectInputInScriptException();
            }catch(NumberFormatException exception){
                Outputer.printerror("Y coordinate must be represented by a number!");
                if(fileMode) throw new IncorrectInputInScriptException();
            }catch (NullPointerException | IllegalStateException exception){
                Outputer.printerror("Unexpected error!");
                System.exit(0);
            }
        }
        return y;
    }

    /**
     * Asks a user the person's coordinates.
     * @return Person's coordinates.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public Coordinates askCoordinates() throws IncorrectInputInScriptException{
        Long x;
        Float y;
        x = askX();
        y = askY();
        return new Coordinates(x, y);
    }

    /**
     * Asks a user the person's height.
     * @return Person's height.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public float askHeight() throws IncorrectInputInScriptException{
        String strHeight;
        float height;
        while (true){
            try{
                Outputer.println("Enter height: ");
                Outputer.print(App.PS2);
                strHeight = userScanner.nextLine().trim();
                if(fileMode) Outputer.println(strHeight);
                height = Float.parseFloat(strHeight);
                if(height < MIN_height) throw new NotInDeclaredLimitsException();
                break;
            }catch(NoSuchElementException exception){
                Outputer.printerror("Height not recognized!");
                if(fileMode) throw new IncorrectInputInScriptException();
            }catch(NotInDeclaredLimitsException exception){
                Outputer.printerror("Height must be greater than 0");
                if(fileMode) throw new IncorrectInputInScriptException();
            }catch(NumberFormatException exception){
                Outputer.printerror("Height must be represented by a number!");
                if(fileMode) throw new IncorrectInputInScriptException();
            }catch(NullPointerException | IllegalStateException exception){
                Outputer.printerror("Unexpected error!");
                System.exit(0);
            }
        }
        return height;
    }

    /**
     * Asks a user the person's weight.
     * @return Person's weight.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public int askWeight() throws IncorrectInputInScriptException{
        String strWeight;
        int weight;
        while(true){
            try{
                Outputer.println("Enter weight: ");
                Outputer.print(App.PS2);
                strWeight = userScanner.nextLine().trim();
                if(fileMode) Outputer.println(strWeight);
                weight = Integer.parseInt(strWeight);
                if(weight < MIN_weight) throw new NotInDeclaredLimitsException();
                break;
            }catch(NoSuchElementException exception){
                Outputer.printerror("Weight not recognized!");
                if(fileMode) throw new IncorrectInputInScriptException();
            }catch(NotInDeclaredLimitsException exception){
                Outputer.printerror("Weight must be greater than 0");
                if(fileMode) throw new IncorrectInputInScriptException();
            }catch(NumberFormatException exception){
                Outputer.printerror("Weight must be represented by a number!");
                if(fileMode) throw new IncorrectInputInScriptException();
            }catch(NullPointerException | IllegalStateException exception){
                Outputer.printerror("Unexpected error!");
                System.exit(0);
            }
        }
        return weight;
    }

    /**
     * Asks a user the person's hair color.
     * @return person's hair color.
     * @throws IncorrectInputInScriptException If script running and something goes wrong.
     */
    public Color askColor() throws IncorrectInputInScriptException{
        String strColor;
        Color color;
        while(true){
            try{
                Outputer.println("Color list: " +Color.nameList());
                Outputer.println("Enter hair color: ");
                Outputer.print(App.PS2);
                strColor = userScanner.nextLine().trim();
                if(fileMode) Outputer.println(strColor);
                color = Color.valueOf(strColor.toUpperCase());
                break;
            }catch(NoSuchElementException exception){
                Outputer.printerror("Color not recognized!");
                if(fileMode) throw new IncorrectInputInScriptException();
            }catch (IllegalArgumentException exception){
                Outputer.printerror("Color is't in list!");
                if(fileMode) throw new IncorrectInputInScriptException();
            }catch (IllegalStateException exception){
                Outputer.printerror("Unexpected error!");
                System.exit(0);
            }
        }
        return color;
    }

    /**
     * Asks a user the person's X location.
     * @return Person's X location.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public int askXLocation() throws IncorrectInputInScriptException{
        String strX;
        int x;
        while (true) {
            try {
                Outputer.println("Enter X location: ");
                Outputer.print(App.PS2);
                strX = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(strX);
                x = Integer.parseInt(strX);
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("X location not recognized!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                Outputer.printerror("X location must be a number!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printerror("Unexpected error!");
                System.exit(0);
            }
        }
        return x;
    }

    /**
     * Asks a user the person's Y location.
     * @return Person's Y location.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public long askYLocation() throws IncorrectInputInScriptException{
        String strY;
        long y;
        while (true) {
            try {
                Outputer.println("Enter Y location: ");
                Outputer.print(App.PS2);
                strY = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(strY);
                y = Long.parseLong(strY);
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Y location not recognized!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                Outputer.printerror("Y location must be a number!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printerror("Unexpected error!");
                System.exit(0);
            }
        }
        return y;
    }

    /**
     * Asks a user the person's Z location.
     * @return Person's Z location.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public Integer askZLocation() throws IncorrectInputInScriptException{
        String strZ;
        int z;
        while (true) {
            try {
                Outputer.println("Enter Z location: ");
                Outputer.print(App.PS2);
                strZ = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(strZ);
                z = Integer.parseInt(strZ);
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Z location not recognized!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                Outputer.printerror("Z location must be a number!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printerror("Unexpected error!");
                System.exit(0);
            }
        }
        return z;
    }

    /**
     * Asks a user the person's location.
     * @return Person's location.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public Location askLocation() throws IncorrectInputInScriptException{
        int x;
        long y;
        Integer z;
        x = askXLocation();
        y = askYLocation();
        z = askZLocation();
        return new Location(z, y, z);
    }

    /**
     * Asks a user person's birthday.
     * @return Person's birthday.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public ZonedDateTime askBirthday() throws IncorrectInputInScriptException{
        String strBirthday;
        ZonedDateTime birthday;
        while(true){
            try{
                Outputer.println("Enter person's birthday (dd/MM/yyyy): ");
                strBirthday = userScanner.nextLine().trim();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate date = LocalDate.parse(strBirthday, formatter);
                birthday = ZonedDateTime.of(date, LocalTime.of(0, 0), ZoneId.systemDefault());
                break;
            }catch (NoSuchElementException exception){
                Outputer.printerror("Birthday not recognized!");
                if(fileMode) throw new IncorrectInputInScriptException();
            }catch(DateTimeParseException exception){
                Outputer.printerror("Invalid format, please enter person's birthday in (dd/MM/yyyy) format!");
                if(fileMode) throw new IncorrectInputInScriptException();
            }catch (NullPointerException | IllegalStateException exception){
                Outputer.printerror("Unexpected error!");
                System.exit(0);
            }
        }
        return birthday;
    }

    /**
     * Asks a user a question.
     * @return Answer (true/false).
     * @param question A question.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public boolean askQuestion(String question) throws IncorrectInputInScriptException {
        String finalQuestion = question + " (+/-):";
        String answer;
        while (true) {
            try {
                Outputer.println(finalQuestion);
                Outputer.print(App.PS2);
                answer = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(answer);
                if (!answer.equals("+") && !answer.equals("-")) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Answer not recognized!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NotInDeclaredLimitsException exception) {
                Outputer.printerror("The answer must be in characters '+' or '-'!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Outputer.printerror("Unexpected error!");
                System.exit(0);
            }
        }
        return (answer.equals("+")) ? true : false;
    }
}
