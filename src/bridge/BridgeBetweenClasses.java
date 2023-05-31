package bridge;

import connection.ShodanApi;
import document.PDFDocument;
import inputs.HandleUserInputs;
import outputs.HandleDisplayForUser;
import utils.Const;
import connection.Connection;
import utils.Utils;

import java.util.Map;
import java.util.logging.Level;

public class BridgeBetweenClasses {
    private static ShodanApi shodanApi;
    public static String result = "";
    public static String cveResults = "";
    public static void runAuditAction(String target) {
        shodanApi = new ShodanApi(target);
        Const.display.thread.interrupt();
        if (Const.display.thread.isAlive()){
            if(!shodanApi.getBannerData().isEmpty() &&  !shodanApi.getHostData().isEmpty()) {
                printShodanResults();
                try {
                    if (shodanApi.getCve() != null)
                        printCveResults(shodanApi.getCve().getCveResults());
                    else
                        Utils.getLogger().log(Level.WARNING, "No CVE results to print because cveResults map is null");
                    if (!result.equals(""))
                        HandleUserInputs.handleReport();
                } catch (Exception e) {
                    Utils.getLogger().log(Level.SEVERE, "Error while trying to print audit results : " + e.getMessage());
                }
            }
            else{
                    Utils.getLogger().log(Level.SEVERE , "Nothing to print cause target's banner data and host data are null");
            }
        }

    }
    public static void runScanAction(String scanName , Map<String , String> data){
        Connection Connection = new Connection();
        for(Map.Entry entry : data.entrySet()){
            Connection.getData().put((String) entry.getKey() , (String) entry.getValue());
        }
        if(data.containsKey("target") && data.containsKey("scan_type")) {
            String result = Connection.launchRequest(scanName);
            try {
                Const.display.thread.interrupt();
                if (result != null && !result.equals("")) {
                    if (Const.display.thread.isInterrupted()) {
                        HandleDisplayForUser.printMessage(result);
                    }
                }
                else{
                    Utils.getLogger().log(Level.WARNING , "Unique scan results cannot be printed because scan results is empty or null");
                }
            }
            catch(Exception e){
                Utils.getLogger().log(Level.SEVERE , "Error while printing unique scan results : " + e.getMessage());
            }
        }
        else {
            HandleDisplayForUser.printErrorMessage("Target or scan_type not set");
            Utils.getLogger().log(Level.SEVERE, "Target or scan_type not set");
        }
    }

    public static void printPDFDocument(String result){
        try {
            Utils.getLogger().log(Level.INFO , result);
            PDFDocument document = new PDFDocument();
            document.addParagraph(result);
        }
        catch(Exception e){
            Utils.getLogger().log(Level.SEVERE , "Error while building pdf document : " + e.getMessage());
        }
    }
    public static void printBannerData(Map <String , String> bannerData){
        //Print bannerData first
        try {
            if (Utils.valueExists(bannerData.get("title"))) {
                result += "TITLE : " + bannerData.get("title") + "\n";
            }
            else {
                result += "TITLE : UNKNOWN" + "\n";
            }
            if (Utils.valueExists(bannerData.get("ip"))) {
                result += "IP : " + bannerData.get("ip") + "\n";
            }
            else {
                result += "IP : UNKNOWN" + "\n";
            }
            if (Utils.valueExists(bannerData.get("version"))) {
                result += "VERSION : " + bannerData.get("version") + "\n";
            }
            else {
                result += "VERSION : UNKNOWN" + "\n";
            }
            if (Utils.valueExists(bannerData.get("deviceType"))) {
                result += "DEVICE TYPE : " + bannerData.get("deviceType") + "\n";
            }
            else {
                result += "DEVICE TYPE : UNKNOWN" + "\n";
            }
            if (Utils.valueExists(bannerData.get("os"))) {
                result += "OS : " + bannerData.get("os") + "\n";
            }
            else {
                result += "OS : UNKNOWN" + "\n";
            }
            if (Utils.valueExists(bannerData.get("domains"))) {
                result += "DOMAINS : " + bannerData.get("domains") + "\n";
            }
            else {
                result += "DOMAINS : UNKNOWN" + "\n";
            }
            if (Utils.valueExists(bannerData.get("hostnames"))) {
                result += "HOSTNAMES : " + bannerData.get("hostnames") + "\n";
            }
            else {
                result += "HOSTNAMES : UNKNOWN" + "\n";
            }
            if (Utils.valueExists(bannerData.get("options"))) {
                result += "OPTIONS : " + Utils.removeIndexFromString(bannerData.get("options") , "Options") + "\n";
            }
            else {
                result += "OPTIONS : UNKNOWN" + "\n";
            }
            if (Utils.valueExists(bannerData.get("metadata"))) {
                result += "METADATA : " + Utils.removeIndexFromString(bannerData.get("metadata") , "Metadata") + "\n";
            }
            else {
                result += "METADATA : UNKNOWN" + "\n";
            }
            if (Utils.valueExists(bannerData.get("port"))) {
                result += "PORT : " + bannerData.get("port") + "\n";
            }
            else {
                result += "PORT : UNKNOWN" + "\n";
            }
            if (Utils.valueExists(bannerData.get("data"))) {
                result += "DATA : " + bannerData.get("data") + "\n";
            }
            else {
                result += "DATA : UNKNOWN" + "\n";
            }
            if (Utils.valueExists(bannerData.get("location"))) {
                result += "LOCATION : " + Utils.removeIndexFromString(bannerData.get("location") , "Location") + "\n";
            }
            else {
                result += "LOCATION : UNKNOWN" + "\n";
            }
            if (Utils.valueExists(bannerData.get("sslInfo"))) {
                result += "SSL INFO : " + bannerData.get("sslInfo") + "\n";
            }
            else {
                result += "SSL INFO : UNKNOWN" + "\n";
            }
        }
        catch(Exception e){
            Utils.getLogger().log(Level.WARNING , "Error while printing banner data : " + e.getMessage());
        }
    }
    public static void printHostData(Map <String , String> hostData){
        try{
            if(hostData.containsKey("organization")){
                result += "ORGANIZATION : " + hostData.get("organization") + "\n";
            }
            else{
                result += "ORGANIZATION : UNKNOWN" + "\n";
            }
            if(hostData.containsKey("vulnerability")){
                result += "Vulnerabilities : " + hostData.get("vulnerability") + "\n";
            }
            else{
                result += "Vulnerabilities : UNKNOWN" + "\n";
            }
        }
        catch(Exception e){
            Utils.getLogger().log(Level.WARNING , "Error while printing host data : " + e.getMessage());
        }
    }
    public static void printShodanResults(){
       printBannerData(shodanApi.getBannerData());
       result += "\n==============================================================================\n";
       printHostData(shodanApi.getHostData());
       HandleDisplayForUser.printMessage(result);
    }
    public static void printCveResults(Map<String, Map<String, String>> cveResults){
        try{
        if(cveResults != null) {
            for (Map.Entry entry : cveResults.entrySet()) {
                String key = (String) entry.getKey();
                Map<String, String> value = (Map<String, String>) entry.getValue();
                if (value != null) {
                    BridgeBetweenClasses.cveResults += "\n=========================== " + key.toUpperCase() + " ===========================\n";
                    for (String mapKey : value.keySet()){
                        BridgeBetweenClasses.cveResults += mapKey.toUpperCase() + " : " + value.get(mapKey) + "\n";
                    }
                }
            }
            HandleDisplayForUser.printMessage(BridgeBetweenClasses.cveResults);
        }
        else
            Utils.getLogger().log(Level.WARNING , "cveResults map is null");
        }
        catch(Exception e){
            Utils.getLogger().log(Level.WARNING , "Error while printing cveResults : " + e.getMessage());
        }
    }
}
