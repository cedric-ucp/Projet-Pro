package main;

import outputs.HandleDisplayForUser;
import utils.Utils;

public class Main{
    public static void main(String  [] args) {
        Utils.configLog();
        HandleDisplayForUser.printWelcomeMessage();
        new Launch();
    }
}
