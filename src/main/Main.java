package main;
import connection.ShodanApi;
import inputs.HandleUserInputs;
import outputs.HandleDisplayForUser;
import utils.Const;
import utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Main {
    public static void main(String  [] args){
       Utils.configLog();
        Scanner userInput = new Scanner(System.in);
        HandleDisplayForUser.printWelcomeMessage();
        HandleUserInputs.handleUserAction(userInput);
    }
}
