package utility;

import exceptions.MustBeNotEmptyException;
import exceptions.NotInDeclaredLimitsException;
import run.App;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Asks user a login and password.
 */
public class AuthAsker {
    private Scanner userScanner;

    public AuthAsker(Scanner userScanner) {
        this.userScanner = userScanner;
    }

    /**
     * Asks user a login.
     *
     * @return login.
     */
    public String askLogin(){
        String login;
        while (true) {
            try {
                Outputer.println("Enter login");
                Outputer.print(App.PS2);
                login = userScanner.nextLine().trim();
                if (login.equals("")) throw new MustBeNotEmptyException();
                break;
            } catch (NoSuchElementException exception){
                Outputer.printerror("This login does not exist!");
            } catch (MustBeNotEmptyException exception) {
                Outputer.printerror("Name cannot be empty!");
            } catch (IllegalStateException exception){
                Outputer.printerror("Unexpected error!");
                System.exit(0);
            }
        }
        return login;
    }

    /**
     * Asks user a password.
     */
    public String askPassword(){
        String password;
        while (true) {
            try{
                Outputer.println("Enter password");
                Outputer.print(App.PS2);
                password = userScanner.nextLine().trim();
                break;
            } catch (NoSuchElementException exception){
                Outputer.printerror("Wrong login or password!");
            }
        }
        return password;
    }

    /**
     * Asks a user a question.
     *
     * @param question A question.
     * @return Answer (true/false).
     */
    public boolean askQuestion(String question){
        String finalQuestion = question +" (Y/N):";
        String answer;
        while (true){
            try {
                Outputer.println(finalQuestion);
                Outputer.print(App.PS2);
                answer = userScanner.nextLine().trim();
                if (!answer.equals("Y") && !answer.equals("N")) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception){
                Outputer.printerror("Answer not recognized!");
            } catch (NotInDeclaredLimitsException exception){
                Outputer.printerror("Answer must be 'Y' or 'N' !");
            }
        }
        return answer.equals("Y");
    }
}
