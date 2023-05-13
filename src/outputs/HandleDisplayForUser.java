package outputs;

import utils.Const;
import utils.Utils;

import java.util.logging.Level;

public class HandleDisplayForUser implements Runnable {
    public  Thread thread = new Thread(this);
    public static void printWelcomeMessage(){
        Utils.getLogger().log(Level.INFO , "App launched");
        String GREEN = "\u001B[32m";
        String message = " _______  __   __  ______   ___   _______    _______  _______  _______  ___     \n" +
                "|   _   ||  | |  ||      | |   | |       |  |       ||       ||       ||   |    \n" +
                "|  |_|  ||  | |  ||  _    ||   | |_     _|  |_     _||   _   ||   _   ||   |    \n" +
                "|       ||  |_|  || | |   ||   |   |   |      |   |  |  | |  ||  | |  ||   |    \n" +
                "|       ||       || |_|   ||   |   |   |      |   |  |  |_|  ||  |_|  ||   |___ \n" +
                "|   _   ||       ||       ||   |   |   |      |   |  |       ||       ||       |\n" +
                "|__| |__||_______||______| |___|   |___|      |___|  |_______||_______||_______|";
        System.out.println(GREEN + message);
    }
    public static void printOptions(){
        String GREEN = "\u001B[32m";
        System.out.println("\n\n******************* CHOOSE ACTION *******************");
        String runAudit = "[1] RUN AUDIT";
        String runScan = "[2] RUN SCAN";
        for(int i = 0 ; i < "*******************".length() ; i++) {
            runAudit = " " + runAudit;
            runScan = " " + runScan;
        }
        printMessage(runAudit);
        printMessage(runScan);
        for(int i = 0 ; i < "******************* CHOOSE ACTION *******************".length() ; i++)
            System.out.print(GREEN + "*");
        System.out.println(GREEN + "\n");
    }
    public static void printMessage(String message){
        String GREEN = "\u001B[32m";
        System.out.println(GREEN + message);
    }
    public static void printAvailableScan(){
        printMessage("[1] Scan Port");
        printMessage("[2] Aggressive Scan");
        printMessage("[3] OS Info Scan");
        printMessage("[4] Service Detection Scan");
        printMessage("[5] Firewalling Scan");
    }
    public static void printProcessingScanMessage(int scan , String target){
        switch(scan){
            case 1 -> printMessage("Processing " + Const.SCAN_PORT + " on target " + target);
            case 2 -> printMessage("Processing " + Const.AGGRESSIVE_SCAN + " on target " + target);
            case 3 -> printMessage("Processing " + Const.OS_INFO_SCAN + " on target " + target);
            case 4 -> printMessage("Processing " + Const.SERVICE_DETECTION_SCAN + " on target " + target);
            case 5 -> printMessage("Processing " + Const.FIREWALLING_SCAN + " on target " + target);
        }
    }
    public static void printErrorMessage(String message){
        String RED = "\u001B[31m";
        System.out.println(RED + message);
    }
    public void run(){
        String space = "                    ";
        String bar = "=";
        String buildBar = "";
        try {
            while(!thread.isInterrupted()) {
                int progress = 5;
                for (int i = 0; i < 20; i++) {
                    buildBar += bar;
                    space = space.substring(1);
                    String loadingBar = "[" + buildBar + space + "]"  + progress + "%" + "\r";
                    System.out.print("\u001B[32m" + loadingBar);
                    progress += 5;
                    Thread.sleep(200);
                }
                buildBar = "";
                space = "                    ";
            }
        }
        catch(Exception e){
            Utils.getLogger().log(Level.SEVERE , "Error while building loading bar : " + e.getMessage());
        }
    }
}
