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
        String userInput = input.nextLine().trim();
        logger.log(Level.INFO, "User input : " + userInput);
        switch (userInput) {
            case "1" -> {
                String target = Utils.checkingTarget(input, logger);
                HandleUserInputs.setAuditActions(Utils.getIpAddressFromDomain(target));
            }
            case "2" -> {
                HandleDisplayForUser.printAvailableScan();
                String chosenScan = input.nextLine().trim();
                logger.log(Level.INFO , "User chosen scan : " + chosenScan);
                while (!Utils.checkChosenScan(chosenScan)){
                    HandleDisplayForUser.printErrorMessage("Choose available Scan");
                    logger.log(Level.SEVERE, "Wrong input");
                    chosenScan = input.nextLine();
                }
                String target = Utils.checkingTarget(input, logger);
                setSingleScan(target , Integer.parseInt(chosenScan));
            }
            default ->{
                HandleDisplayForUser.printErrorMessage("Wrong input, choose available options");
                handleUserAction(input);
            }
        }
    }

    private static void setAuditActions(String target){
        try{
            Map <String , String> parameters = new HashMap<>();
            parameters.put("target" , target);
            HandleDisplayForUser.printMessage("Audit scan on target : " + target);
            HandleDisplayForUser.printMessage("This will take several minutes, please wait until the scan end\n");
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
    public static void handleReport(){

        boolean correctInput = false;
        while (!correctInput) {
            HandleDisplayForUser.printMessage("Do you want to build a report ? [yes/no]");
            Scanner input = new Scanner(System.in);
            String userInput = input.nextLine().trim();
            Utils.getLogger().log(Level.INFO , "User input : " + userInput);

            if(!userInput.isEmpty() && !userInput.isBlank()) {
                switch (userInput.toLowerCase()){
                    case "yes" -> {
                        Utils.getLogger().log(Level.INFO , "Building PDF report");
                        BridgeBetweenClasses.printPDFDocument(BridgeBetweenClasses.result + BridgeBetweenClasses.cveResults);
                        correctInput = true;
                    }
                    case "no" -> {
                        Utils.getLogger().log(Level.INFO, "No print in PDF document");
                        correctInput = true;
                    }
                    default -> {
                        Utils.getLogger().log(Level.WARNING, "Wrong input : " + userInput + "!");
                        HandleDisplayForUser.printErrorMessage("Wrong input ! Enter yes or no");
                    }
                }
            }
        }
    }
}
