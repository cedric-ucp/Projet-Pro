package connection;

import utils.Const;

import java.util.Map;

public class ScanRequest {
    public static void osInfoScan(Map<String , String> data){
        data.put("command" , "osinfo");
        data.put("options" , "");
        data.put("schedule" , "now");
    }

    public static void osInfoScanV2(Map<String , String> data){
        data.put("command" , "normal");
        data.put("options" , "o");
        data.put("schedule" , "now");
    }

    public static void serviceDetectionScan(Map<String , String> data){
        data.put("command" , "normal");
        data.put("options" , "sv9");
        data.put("schedule" , "now");
    }

    public static void fastScan(Map<String , String> data){
        data.put("command" , "fast");
        data.put("options" , "f");
        data.put("schedule" , "now");
    }

    public static void fireWallSettingsScan(Map<String , String> data){
        data.put("command" , "firewall");
        data.put("options" , "");
        data.put("schedule" , "now");
    }

    public static void scanPorts(Map<String , String> data){
        data.put("command" , "top20port");
        data.put("options" , "");
        data.put("schedule" , "now");
    }

    public static void aggressiveScan(Map<String , String> data){
        data.put("command" , "normal");
        data.put("options" , "t4");
        data.put("schedule" , "now");
    }

    public static void malwareInfectionsScan(Map<String , String> data){
        data.put("command" , "malware");
        data.put("options" , "");
        data.put("schedule" , "now");
    }
    public static void runAuditRequest(Map <String , String> data , String nextScan){
        data.put("scan_type" , "single");
        switch (nextScan) {
            case Const.AGGRESSIVE_SCAN -> aggressiveScan(data);
            case Const.SCAN_PORT -> scanPorts(data);
            case Const.MALWARE_DETECTION_SCAN -> malwareInfectionsScan(data);
            case Const.SERVICE_DETECTION_SCAN -> serviceDetectionScan(data);
            case Const.OS_INFO_SCAN -> osInfoScan(data);
            case Const.FIREWALLING_SCAN -> fireWallSettingsScan(data);
        }
    }
}
