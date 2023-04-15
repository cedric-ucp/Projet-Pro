package bridge;

import outputs.HandleDisplayForUser;
import utils.Const;
import connection.Connection;
import utils.Utils;

import java.util.Map;
import java.util.logging.Level;

public class BridgeBetweenClasses {
    public static void runAuditAction(Map<String , String> data){
        Connection connection = new Connection();
        for(Map.Entry entry : data.entrySet()){
            connection.getData().put((String) entry.getKey() , (String) entry.getValue());
        }
        if(data.containsKey("target") && data.containsKey("scan_type")) {
            connection.launchRequest(Const.AUDIT_ACTION);
        }
        else {
            HandleDisplayForUser.printErrorMessage("Target or scan_type not set");
            Utils.getLogger().log(Level.SEVERE, "Target or scan_type not set");
        }
    }
    public static void runScanAction(String scanName , Map<String , String> data){
        Connection connection = new Connection();
        for(Map.Entry entry : data.entrySet()){
            connection.getData().put((String) entry.getKey() , (String) entry.getValue());
        }
        if(data.containsKey("target") && data.containsKey("scan_type")) {
            connection.launchRequest(scanName);
        }
        else {
            HandleDisplayForUser.printErrorMessage("Target or scan_type not set");
            Utils.getLogger().log(Level.SEVERE, "Target or scan_type not set");
        }

    }
}
