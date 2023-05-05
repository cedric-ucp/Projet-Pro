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
    public static void runAuditAction(String target){
        new ShodanApi().auditHost(target);
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
}
