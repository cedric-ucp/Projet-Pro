package main;
import connection.Connection;
import utils.Const;
import utils.Utils;
import inputs.HandleUserInputs;
import outputs.HandleDisplayForUser;
import java.util.Scanner;


public class Main {
    public static void main(String  [] args){
        Utils.configLog();
        Scanner userInput = new Scanner(System.in);
        HandleDisplayForUser.printWelcomeMessage();
        HandleUserInputs.handleUserAction(userInput);
    }
}
