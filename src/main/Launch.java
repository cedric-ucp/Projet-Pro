package main;

import inputs.HandleUserInputs;
import outputs.HandleDisplayForUser;
import utils.Utils;

import java.util.Scanner;

public class Launch implements Runnable {
    public Launch(){
        Utils.configLog();
        run();
    }


    @Override
    public void run() {
        while(Boolean.TRUE) {
            Scanner userInput = new Scanner(System.in);
            HandleDisplayForUser.printWelcomeMessage();
            HandleUserInputs.handleUserAction(userInput);
        }
    }

}
