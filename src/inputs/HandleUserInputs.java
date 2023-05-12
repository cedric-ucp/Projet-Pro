package inputs;

import utils.Const;
import utils.Utils;
import bridge.BridgeBetweenClasses;
import outputs.HandleDisplayForUser;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class HandleUserInputs {
    public static void handleUserAction(Scanner input){
        Logger logger = Utils.getLogger();
        String userInput = input.nextLine();
        logger.log(Level.INFO, "User input : " + userInput);
        switch (userInput) {
            case "1" -> {
                String target = checkingTarget(input, logger);
                HandleUserInputs.setAuditActions(Utils.getIpAddressFromDomain(target));
            }
            case "2" -> {
                HandleDisplayForUser.printAvailableScan();
                String chosenScan = input.nextLine();
                logger.log(Level.INFO , "User chosen scan : " + chosenScan);
                while (!HandleUserInputs.checkChosenScan(chosenScan)){
                    HandleDisplayForUser.printErrorMessage("Choose available Scan");
                    logger.log(Level.SEVERE, "Wrong input");
                    chosenScan = input.nextLine();
                }
                String target = checkingTarget(input, logger);
                setSingleScan(target , Integer.parseInt(chosenScan));
            }
            default ->{
                HandleDisplayForUser.printErrorMessage("Wrong input, choose available options");
                handleUserAction(input);
            }
        }
    }
    private static String checkingTarget(Scanner input, Logger logger) {
        HandleDisplayForUser.printMessage("Enter target ip or domain");
        String target = input.nextLine();
        logger.log(Level.INFO , "User entered target : " + target);
        while (!HandleUserInputs.checkTargetUser(target)) {
            HandleDisplayForUser.printErrorMessage("Bad target address provided !");
            logger.log(Level.SEVERE, "Bad target format");
            HandleDisplayForUser.printMessage("Enter valid target ip or domain");
            target = input.nextLine();
        }
        return target;
    }
    private static boolean checkChosenScan(String chosenScan){
        if(Utils.containsNoLetters(chosenScan)) {
            int scan = Integer.parseInt(chosenScan);
            return scan == 1 || scan == 2 || scan == 3 || scan == 4 || scan == 5;
        }
        else{
            return false;
        }
    }
    private static boolean checkTargetUser(String target){
        Utils.getLogger().log(Level.INFO , "Target : " + target);
        try {
            if (target != null && !target.isBlank() && !target.isEmpty()) {
                return checkTargetIpAddress(target) || checkTargetDomain(target);
            }
        }
        catch(Exception e){
            e.printStackTrace();
            Utils.getLogger().log(Level.SEVERE , e.getMessage());
        }
        return false;
    }
    private static void setAuditActions(String target){
        try{
            Map <String , String> parameters = new HashMap<>();
            parameters.put("target" , target);
            HandleDisplayForUser.printMessage("Audit scan on target : " + target);
            HandleDisplayForUser.printMessage("This will take several minutes, please wait until the scan end");
            Const.display = new HandleDisplayForUser();
            Const.display.thread.start();
            BridgeBetweenClasses.runAuditAction(target);
        }
        catch(Exception e){
            e.printStackTrace();
            Utils.getLogger().log(Level.SEVERE , e.getMessage());
        }
    }
    private static void setSingleScan(String target , int chosenScan){
        try {
            String scanName = Utils.optionsToScanNames(chosenScan);
            Map<String, String> parameters = new HashMap<>();
            parameters.put("target", target);
            parameters.put("scan_type", "single");
            HandleDisplayForUser.printProcessingScanMessage(chosenScan , target);
            HandleDisplayForUser.printMessage("This will take several minutes, please wait until the scan end");
            Const.display = new HandleDisplayForUser();
            Const.display.thread.start();
            BridgeBetweenClasses.runScanAction(scanName , parameters);
        }
        catch(Exception e){
            e.printStackTrace();
            Utils.getLogger().log(Level.SEVERE , e.getMessage());
        }
    }
    private static boolean checkTargetDomain(String domain){
        try {
            new URL(domain).toURI();
        }
        catch(Exception e){
            Utils.getLogger().log(Level.SEVERE , e.getMessage());
            return false;
        }
        return true;
    }
    private static boolean checkTargetIpAddress(String ip){
        if(ip.length() <= 16 && ip.length() >= 7 && Utils.containsNoLetters(ip)){
            while(ip.length() > 3){
                int delimiterIndex = ip.indexOf(".");
                if(delimiterIndex >= 0){
                    String ipNumber = ip.substring(0, delimiterIndex);
                    if (ipNumber.length() <= 3 && Integer.parseInt(ipNumber) <= 254)
                        ip = ip.substring(delimiterIndex + 1);
                    else {
                        Utils.getLogger().log(Level.SEVERE , "Ip address not valid : ipNumber not valid");
                        return false;
                    }
                }
                else {
                    Utils.getLogger().log(Level.SEVERE , "Ip address not valid : delimiterIndex = " + delimiterIndex + " out of bounds ");
                    return false;
                }
            }
        }
        else {
            Utils.getLogger().log(Level.SEVERE , "Ip address not valid : ip length = " + ip.length() + " not valid");
            return false;
        }
        Utils.getLogger().log(Level.INFO , "Ip address valid");
        return true;
    }


}
