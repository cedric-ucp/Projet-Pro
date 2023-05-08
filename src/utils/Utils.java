package utils;
import okhttp3.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Pattern;

public class Utils {
    private static final Logger LOG = Logger.getLogger("LOG");
    public static void configLog(){
        try {
            String path = "./logs/project.log";
            FileHandler fileHandler = new FileHandler(path , true);
            LOG.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            LOG.setUseParentHandlers(false);
        }
        catch (Exception e){
            System.out.println("ERROR || " + getClassLogger() + "." + getMethodLogger() + e.getMessage());
        }
    }

    private static String getMethodLogger(){
        StackTraceElement [] stackTraceElements = Thread.currentThread().getStackTrace();
        return stackTraceElements [2].getMethodName();
    }
    private static String getClassLogger(){
        StackTraceElement [] stackTraceElements = Thread.currentThread().getStackTrace();
        return stackTraceElements [2].getClassName();
    }
    public static Logger getLogger(){
        return LOG;
    }

    public String logStatusCode(int status){
        if(status == Const.STATUS_OK || status == Const.STATUS_CREATED)
            return Const.REQUEST_SUCCESS_MESSAGE;
        else if(status == Const.STATUS_FAILED)
            return Const.REQUEST_ERROR_MESSAGE;
        else if(status == Const.STATUS_TIMEOUT)
            return Const.REQUEST_TIMEOUT;
        else if(status == Const.STATUS_UNAUTHORIZED)
            return Const.REQUEST_BAD_INFORMATION;
        else if(status == Const.STATUS_BAD_INFORMATION)
            return Const.REQUEST_BAD_INFORMATION;
        else
            return Const.REQUEST_UNKNOWN_STATUS;
    }

    public static void responseLog(Response response, int status, String state){
        LOG.log(Level.INFO , "State : " + state + " || status code : " + status + " || message : " + response.message());
    }
    public static String optionsToScanNames(int option){
        switch (option) {
            case 1 -> {
                return Const.SCAN_PORT;
            }
            case 2 -> {
                return Const.AGGRESSIVE_SCAN;
            }
            case 3 -> {
                return Const.OS_INFO_SCAN;
            }
            case 4 -> {
                return Const.SERVICE_DETECTION_SCAN;
            }
            case 5 -> {
                return Const.FIREWALLING_SCAN;
            }
            default ->{
                return null;
            }
        }
    }
    public static void buildParamRunScan(Map <String , String> data , String scan_id){
        data.put("scan_id" , scan_id);
    }
    public static Map <String , String> buildNmapHeaderParams(){
        Map <String , String> headerParams = new HashMap<>();
        headerParams.put(Const.NMAP_CONTENT_TYPE_KEY , Const.NMAP_CONTENT_TYPE_VALUE);
        headerParams.put(Const.NMAP_API_KEY_KEY , Const.NMAP_API_KEY_VALUE);
        return headerParams;
    }
    public static boolean valueExists(String string){
        return string != null && !string.isEmpty() && !string.isBlank() && !string.equals("unknown");
    }
    public static ArrayList<String> stringToArray(String string , String delimiter){
        string = string.substring(1 , string.length() - 1);
        ArrayList <String> vulnerabilities = new ArrayList<>();
        for (String s : string.split(delimiter))
            vulnerabilities.add(s);

        if(vulnerabilities.size() > 0)
            return vulnerabilities;
        else
            return null;
    }
    public static boolean checkTargetIpAddress(String ip){
        if(ip.length() <= 16 && ip.length() >= 7 && !containsNoLetters(ip)){
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
    private static boolean containsNoLetters(String string){
        return Pattern.matches("[a-zA-Z]+" , string);
    }
    private static String removeProtocol(String target){
        try {
            if (target.startsWith("https")) {
                target = target.substring("https://".length());
                Utils.getLogger().log(Level.INFO, "target without https protocol : " + target);
            } else if (target.startsWith("http")) {
                target = target.substring("http://".length());
                Utils.getLogger().log(Level.INFO, "target without http protocol : " + target);
            } else {
                Utils.getLogger().log(Level.SEVERE, "url doesn't contain any web protocol");
            }
        }
        catch(Exception e){
            Utils.getLogger().log(Level.SEVERE , "Error while removing protocol in target url : " + e.getMessage());
        }
        return target;
    }
    public static String getIpAddressFromDomain(String target) {
        target = removeProtocol(target);
        String command = "nslookup " + target;
        String ipAddress = "";
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            int i = 1;
            while ((line = reader.readLine()) != null && ipAddress.equals("")) {
                ipAddress = parseAddressLine(line , i);
                i++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ipAddress;
    }
    private static String parseAddressLine(String line , int state){
        String ipAddress = "";

        if(state == 5) {
            if(line.startsWith("Addresses"))
                 ipAddress = line.substring("Addresses:".length()).trim();
            else if(line.startsWith("Address"))
                 ipAddress = line.substring("Address:".length()).trim();
            Utils.getLogger().log(Level.INFO , "ip parsed : " + ipAddress);
            if(checkTargetIpAddress(ipAddress)){
                Utils.getLogger().log(Level.INFO , "state : " + state +" || ipAddress : " + ipAddress);
                return ipAddress;
            }
            else {
                Utils.getLogger().log(Level.SEVERE , "Address is not parsed : " + ipAddress);
                return "";
            }
        }
        else if (state == 6){
            ipAddress = line.trim();
            if(checkTargetIpAddress(ipAddress)) {
                Utils.getLogger().log(Level.INFO , "state : " + state + " || ipAddress : " + ipAddress);
                return ipAddress;
            }
            else{
                Utils.getLogger().log(Level.SEVERE , "Address is not parsed : " + ipAddress);
                return "";
            }
        }
        return "";
    }
}
