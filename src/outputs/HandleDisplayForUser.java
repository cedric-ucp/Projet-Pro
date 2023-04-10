package outputs;

import utils.Const;
import utils.Utils;

import java.util.logging.Level;

public class HandleDisplayForUser {
    public static void printWelcomeMessage(){
        Utils.getLogger().log(Level.INFO , "App launched");
        String GREEN = "\u001B[32m";
        String message = "WELCOME TO AUDIT SESSIONS";
        String line = "************************************************************";
        String messageLine = "****************************************" + message + "********************";
        System.out.println(GREEN + messageLine);
        System.out.println("Choose action");
        System.out.println("[1] Run Audit");
        System.out.println("[2] Run Scan");
        System.out.println(line);
    }
    public static void printMessage(String message){
        String GREEN = "\u001B[32m";
        System.out.println(GREEN + message);
    }
    public static void printAvailableScan(){
        System.out.println("[1] Scan Port");
        System.out.println("[2] Aggressive Scan");
        System.out.println("[3] OS Info Scan");
        System.out.println("[4] Service Detection Scan");
        System.out.println("[5] Firewalling Scan");
    }
    public static void printProcessingScanMessage(int scan , String target){
        switch(scan){
            case 1 -> printMessage("Processing " + Const.SCAN_PORT + " on target " + target + "...");
            case 2 -> printMessage("Processing " + Const.AGGRESSIVE_SCAN + " on target " + target + "...");
            case 3 -> printMessage("Processing " + Const.OS_INFO_SCAN + " on target " + target + "...");
            case 4 -> printMessage("Processing " + Const.SERVICE_DETECTION_SCAN + " on target " + target + "...");
            case 5 -> printMessage("Processing " + Const.FIREWALLING_SCAN + " on target " + target + "...");
        }
    }
    public static void printErrorMessage(String message){
        String RED = "\u001B[31m";
        System.out.println(RED + message);
    }

}
