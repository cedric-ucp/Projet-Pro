package utils;
import okhttp3.Response;

import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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

    public void responseLog(Response response , int status , String state){
        LOG.log(Level.INFO , "State : " + state + " || status code : " + status + " || message : " + response.message());
    }
    public void buildParamRunScan(Map <String , String> data , String scan_id){
        data.put("scan_id" , scan_id);
    }
}
