package main;

import inputs.HandleUserInputs;
import outputs.HandleDisplayForUser;

import java.util.Scanner;

public class Launch implements Runnable {
    public Launch(){
        new Thread(this).start();
    }
    @Override
    public void run() {
        while(Boolean.TRUE) {
            Scanner userInput = new Scanner(System.in);
            HandleDisplayForUser.printOptions();
            HandleUserInputs.handleUserAction(userInput);
        }
    }

}
