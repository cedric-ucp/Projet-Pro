package connection;

import outputs.HandleDisplayForUser;
import utils.Const;
import utils.Utils;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import okhttp3.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Connection {
    private final String apiKey = "dkjhj9iacgm63abk3bbpdzrgap7ie3b2zgikl9bxfsekmmjg";
    private final Utils utils = new Utils();
    private final Logger LOG;
    private Response response = null;
    OkHttpClient client = new OkHttpClient();
    RequestBody body = null;
    private final Map<String , String> data = new HashMap<>();
    private boolean scanFinished = false;
    private String scan_id = "";

    public Connection(){
        LOG = Utils.getLogger();
    }

    private void sendRequest (String state){
        try {
            buildRequestBody(data);
            Request request;
            Request.Builder builder = new Request.Builder();
            builder.url(handleUrlRequest(state))
                    .post(body)
                    .addHeader("content-type" , "multipart/form-data")
                    .addHeader("NMAP-API-KEY" , apiKey);
            request = builder.build();
            if(response != null)
                response = null;
            response = client.newCall(request).execute();
            handleResponse(response.code() , state);
        }
        catch (Exception e){
            LOG.log(Level.SEVERE , Const.REQUEST_FAILED_MESSAGE);
            e.printStackTrace();
            LOG.log(Level.SEVERE , e.getMessage());
        }
    }

    private void runScanInfo(JsonObject responseBody){
        scan_id = responseBody.get("scan_id").asString();
        LOG.log(Level.INFO , "scan_id : " + scan_id);
        utils.buildParamRunScan(data , scan_id);
        sendRequest(Const.SCAN_INFO);
    }

    private void handleScanResult(JsonObject responseBody){
        try {
            if ((responseBody.get("status_code").asInt()) == Const.STATUS_OK) {
                String result = responseBody.get(Const.RESULT).asString();
                String target = responseBody.get(Const.TARGET).asString();
                String command = responseBody.get(Const.SCAN_TYPE).asString();
                String nmapCommand = responseBody.get(Const.SINGLE_SCAN).asString();
                LOG.log(Level.INFO, "target : " + target);
                LOG.log(Level.INFO, "command : " + command);
                LOG.log(Level.INFO, "nmap_command : " + nmapCommand);
                LOG.log(Level.INFO, "result : " + result);
                HandleDisplayForUser.printMessage(result);
            } else {
                utils.buildParamRunScan(data, scan_id);
                sendRequest(Const.SCAN_RESULTS);
            }
        }
        catch(Exception e){
            e.printStackTrace();
            LOG.log(Level.INFO , e.getMessage());
        }
    }
    private void handleResponse (int status , String state){
        try {
            utils.responseLog(response , status , state);
            if(response.body() != null && (status == Const.STATUS_OK || status == Const.STATUS_CREATED || status == Const.STATUS_ACCEPTED)) {
                data.clear();
                String responseBody = response.body().string();
                LOG.log(Level.INFO, "Response Body : " + responseBody);
                if (Objects.equals(state, Const.START_SCAN))
                    runScanInfo(Json.parse(responseBody).asObject());
                else if (Objects.equals(state, Const.SCAN_INFO)) {
                    utils.buildParamRunScan(data , scan_id);
                    sendRequest(Const.SCAN_STATUS);
                }
                else if(Objects.equals(state, Const.SCAN_STATUS)){
                    utils.buildParamRunScan(data , scan_id);
                    sendRequest(Const.SCAN_RESULTS);
                }
                else
                    handleScanResult(Json.parse(responseBody).asObject());
            }
            else {
                LOG.log(Level.INFO, "No response body");
                HandleDisplayForUser.printErrorMessage("Error scanning");
            }
        }
        catch(Exception e){
            e.printStackTrace();
            LOG.log(Level.SEVERE , e.getMessage());
        }
    }
    public void launchRequest(String action){
        if(action.equals(Const.AUDIT_ACTION)){
            handleAuditScanState();
            sendRequest(Const.START_SCAN);
        }
        else{
            data.put("target" , "www.certifiedhacker.com");
            data.put("scan_type" , "single");
            ScanRequest.osInfoScan(data);
            sendRequest(Const.START_SCAN);
        }
    }
    private void buildRequestBody(Map <String , String> data){
        LOG.log(Level.INFO , "data : " + data);
        if(body != null)
            body = null;
        FormBody.Builder builder = new FormBody.Builder();
        for(Map.Entry entry : data.entrySet()){
           builder.add((String) entry.getKey() , (String) entry.getValue());
        }
        body = builder.build();
    }
    private String handleUrlRequest(String state){
        if(Objects.equals(state, Const.START_SCAN))
            return Const.START_SCAN_URL;
        else if (Objects.equals(state, Const.SCAN_INFO))
            return Const.SCAN_INFO_URL;
        else if (Objects.equals(state, Const.SCAN_STATUS))
            return Const.SCAN_STATUS_URL;
        else
            return Const.SCAN_RESULTS_URL;
    }

    private void handleAuditScanState(){
        if(!Const.SCAN_PORT_DONE && !Const.AGGRESSIVE_SCAN_DONE && !Const.MALWARE_DETECTION_SCAN_DONE && !Const.SERVICE_DETECTION_SCAN_DONE
            && !Const.OS_INFO_SCAN_DONE && !Const.FIREWALLING_SCAN_DONE){
            ScanRequest.runAuditRequest(data, Const.SCAN_PORT);
            Const.SCAN_PORT_DONE = true;
            LOG.log(Level.INFO , "Starting " + Const.SCAN_PORT);
        }
        else if(Const.SCAN_PORT_DONE && !Const.AGGRESSIVE_SCAN_DONE && !Const.MALWARE_DETECTION_SCAN_DONE && !Const.SERVICE_DETECTION_SCAN_DONE
                && !Const.OS_INFO_SCAN_DONE && !Const.FIREWALLING_SCAN_DONE){
            ScanRequest.runAuditRequest(data, Const.AGGRESSIVE_SCAN);
            Const.AGGRESSIVE_SCAN_DONE = true;
            LOG.log(Level.INFO , "Starting " + Const.AGGRESSIVE_SCAN);
        }
        else if(Const.SCAN_PORT_DONE && Const.AGGRESSIVE_SCAN_DONE && !Const.MALWARE_DETECTION_SCAN_DONE && !Const.SERVICE_DETECTION_SCAN_DONE
                && !Const.OS_INFO_SCAN_DONE && !Const.FIREWALLING_SCAN_DONE){
            ScanRequest.runAuditRequest(data, Const.OS_INFO_SCAN);
            Const.OS_INFO_SCAN_DONE = true;
            LOG.log(Level.INFO , "Starting " + Const.OS_INFO_SCAN);
        }
        else if(Const.SCAN_PORT_DONE && Const.AGGRESSIVE_SCAN_DONE && !Const.MALWARE_DETECTION_SCAN_DONE && !Const.SERVICE_DETECTION_SCAN_DONE
                && Const.OS_INFO_SCAN_DONE && !Const.FIREWALLING_SCAN_DONE){
            ScanRequest.runAuditRequest(data, Const.SERVICE_DETECTION_SCAN);
            Const.SERVICE_DETECTION_SCAN_DONE = true;
            LOG.log(Level.INFO , "Starting " + Const.SERVICE_DETECTION_SCAN);
        }
        else if(Const.SCAN_PORT_DONE && Const.AGGRESSIVE_SCAN_DONE && !Const.MALWARE_DETECTION_SCAN_DONE && Const.SERVICE_DETECTION_SCAN_DONE
                && Const.OS_INFO_SCAN_DONE && !Const.FIREWALLING_SCAN_DONE){
            ScanRequest.runAuditRequest(data, Const.MALWARE_DETECTION_SCAN);
            Const.MALWARE_DETECTION_SCAN_DONE = true;
            LOG.log(Level.INFO , "Starting " + Const.MALWARE_DETECTION_SCAN);
        }
        else if(Const.SCAN_PORT_DONE && Const.AGGRESSIVE_SCAN_DONE && Const.MALWARE_DETECTION_SCAN_DONE && Const.SERVICE_DETECTION_SCAN_DONE
                && Const.OS_INFO_SCAN_DONE && !Const.FIREWALLING_SCAN_DONE){
            ScanRequest.runAuditRequest(data , Const.FIREWALLING_SCAN);
            Const.FIREWALLING_SCAN_DONE = true;
            LOG.log(Level.INFO , "Starting " + Const.FIREWALLING_SCAN);
        }
    }
    public Map<String , String> getData(){
        return data;
    }
}
