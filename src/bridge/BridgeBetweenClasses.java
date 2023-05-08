package bridge;

import connection.ShodanApi;
import document.PDFDocument;
import outputs.HandleDisplayForUser;
import utils.Const;
import connection.Connection;
import utils.Utils;

import java.util.Map;
import java.util.logging.Level;

public class BridgeBetweenClasses {
    private static ShodanApi shodanApi;
    public static void runAuditAction(String target){
        shodanApi = new ShodanApi(target);
        printShodanResults();
    }
    public static void runScanAction(String scanName , Map<String , String> data){
        Connection Connection = new Connection();
        for(Map.Entry entry : data.entrySet()){
            Connection.getData().put((String) entry.getKey() , (String) entry.getValue());
        }
        if(data.containsKey("target") && data.containsKey("scan_type")) {
            Connection.launchRequest(scanName);
        }
        else {
            HandleDisplayForUser.printErrorMessage("Target or scan_type not set");
            Utils.getLogger().log(Level.SEVERE, "Target or scan_type not set");
        }
    }

    public static void printPDFDocument(String result){
        PDFDocument document = new PDFDocument();
        document.addParagraph(result);
    }
    public static void printBannerData(Map <String , String> bannerData){
        //Print bannerData first
        try {
            if (Utils.valueExists(bannerData.get("title"))) {
                HandleDisplayForUser.printMessage("TITLE : " + bannerData.get("title"));
            } else {
                HandleDisplayForUser.printMessage("TITLE : UNKNOWN");
            }
            if (Utils.valueExists(bannerData.get("ip"))) {
                HandleDisplayForUser.printMessage("IP : " + bannerData.get("ip"));
            } else {
                HandleDisplayForUser.printMessage("IP : UNKNOWN");
            }
            if (Utils.valueExists(bannerData.get("version"))) {
                HandleDisplayForUser.printMessage("VERSION : " + bannerData.get("version"));
            } else {
                HandleDisplayForUser.printMessage("VERSION : UNKNOWN");
            }
            if (Utils.valueExists(bannerData.get("deviceType"))) {
                HandleDisplayForUser.printMessage("DEVICE TYPE : " + bannerData.get("deviceType"));
            } else {
                HandleDisplayForUser.printMessage("DEVICE TYPE : UNKNOWN");
            }
            if (Utils.valueExists(bannerData.get("os"))) {
                HandleDisplayForUser.printMessage("OS : " + bannerData.get("os"));
            } else {
                HandleDisplayForUser.printMessage("OS : UNKNOWN");
            }
            if (Utils.valueExists(bannerData.get("domains"))) {
                HandleDisplayForUser.printMessage("DOMAINS : " + bannerData.get("domains"));
            } else {
                HandleDisplayForUser.printMessage("DOMAINS : UNKNOWN");
            }
            if (Utils.valueExists(bannerData.get("hostnames"))) {
                HandleDisplayForUser.printMessage("HOSTNAMES : " + bannerData.get("hostnames"));
            } else {
                HandleDisplayForUser.printMessage("HOSTNAMES : UNKNOWN");
            }
            if (Utils.valueExists(bannerData.get("options"))) {
                HandleDisplayForUser.printMessage("OPTIONS : " + bannerData.get("options"));
            } else {
                HandleDisplayForUser.printMessage("OPTIONS : UNKNOWN");
            }
            if (Utils.valueExists(bannerData.get("metadata"))) {
                HandleDisplayForUser.printMessage("METADATA : " + bannerData.get("metadata"));
            } else {
                HandleDisplayForUser.printMessage("METADATA : UNKNOWN");
            }
            if (Utils.valueExists(bannerData.get("port"))) {
                HandleDisplayForUser.printMessage("PORT : " + bannerData.get("port"));
            } else {
                HandleDisplayForUser.printMessage("PORT : UNKNOWN");
            }
            if (Utils.valueExists(bannerData.get("data"))) {
                HandleDisplayForUser.printMessage("DATA : " + bannerData.get("data"));
            } else {
                HandleDisplayForUser.printMessage("DATA : UNKNOWN");
            }
            if (Utils.valueExists(bannerData.get("location"))) {
                HandleDisplayForUser.printMessage("LOCATION : " + bannerData.get("location"));
            } else {
                HandleDisplayForUser.printMessage("LOCATION : UNKNOWN");
            }
            if (Utils.valueExists(bannerData.get("sslInfo"))) {
                HandleDisplayForUser.printMessage("SSL INFO : " + bannerData.get("sslInfo"));
            } else {
                HandleDisplayForUser.printMessage("SSL INFO : UNKNOWN");
            }
        }
        catch(Exception e){
            Utils.getLogger().log(Level.WARNING , "Error while printing banner data : " + e.getMessage());
        }
    }
    public static void printHostData(Map <String , String> hostData){
        try{
            if(hostData.containsKey("organization")){
                HandleDisplayForUser.printMessage("ORGANIZATION : " + hostData.get("organization"));
            }
            else{
                HandleDisplayForUser.printMessage("ORGANIZATION : UNKNOWN");
            }
            if(hostData.containsKey("vulnerability")){
                HandleDisplayForUser.printMessage("Vulnerabilities : " + hostData.get("vulnerability"));
            }
            else{
                HandleDisplayForUser.printMessage("Vulnerabilities : UNKNOWN");
            }
        }
        catch(Exception e){
            Utils.getLogger().log(Level.WARNING , "Error while printing host data : " + e.getMessage());
        }
    }
    public static void printShodanResults(){
       printBannerData(shodanApi.getBannerData());
       HandleDisplayForUser.printMessage("==============================================================================");
       printHostData(shodanApi.getHostData());
    }
}
