package connection;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

public class CVE{
    private String url = "";
    private final String apiKey = "a44b03e3-f9c2-49c9-b142-e4cc5cb66d39";
    private HashMap<String , String> responseData = new HashMap<>();
    private ArrayList<String> vulnerabilities;
    public CVE(String url , String vulnerability){
        this.url = url;
        vulnerabilities = Utils.stringToArray(vulnerability , ",");
        if(vulnerabilities == null || vulnerabilities.size() == 0){
            Utils.getLogger().log(Level.SEVERE , "Error parsing vulnerabilities object or no vulnerabilities to scan");
            return;
        }
        Utils.getLogger().log(Level.INFO , "Url : " + this.url);
        Utils.getLogger().log(Level.INFO , "vulnerabilities : " + vulnerability);
        for(String vul : vulnerabilities)
            sendRequest(vul);
    }
    private void sendRequest(String vulnerability){
        if(url != null && Utils.valueExists(vulnerability)) {
            try {
                Utils.getLogger().log(Level.INFO , "launch CVE request for cve : " + vulnerability);
                Request.Builder builder = new Request.Builder();
                builder.url(buildRequestUrl(vulnerability))
                        .addHeader("apiKey" , apiKey)
                        .get();
                Request request = builder.build();
                Response response = new OkHttpClient().newCall(request).execute();
                Utils.responseLog(response , response.code() , "1");
                String responseBody = response.body().string();
                if(responseBody != null && !responseBody.isEmpty()){
                    Utils.getLogger().log(Level.INFO, "response : " + responseBody);
                    System.out.println("response : " + responseBody);
                    handleResponseData(JSONObject.stringToValue(responseBody));
                }
                else
                    Utils.getLogger().log(Level.SEVERE , "No response from request");
            }
            catch(Exception e) {
                Utils.getLogger().log(Level.SEVERE, e.getMessage());
            }
        }
    }
    private String buildRequestUrl(String vulnerability){
        String modifiedUrl =  url + vulnerability;
        Utils.getLogger().log(Level.INFO , "modified url " + modifiedUrl);
        return modifiedUrl;
    }
    private void handleResponseData(Object responseBody){
        JSONObject vulnerabilities;
        try {
            if (responseBody != null) {
                vulnerabilities = (JSONObject) (JSONObject..get("vulnerabilities");
                if(vulnerabilities != null){
                    System.out.println("vulnerabilities : " + vulnerabilities);
                    JSONObject cve = (JSONObject) vulnerabilities.get("cve");
                    if(cve != null)
                        System.out.println("cve : " + cve);
                    else
                        Utils.getLogger().log(Level.SEVERE , "Error : cve jsonObject is null");
                }
                else{
                    Utils.getLogger().log(Level.SEVERE , "vulnerabilities jsonObject is null");
                }
            } else {
                Utils.getLogger().log(Level.SEVERE, "Error : responseBody JsonObject is null");
            }
        }
        catch(Exception e){
            Utils.getLogger().log(Level.SEVERE , "Error while parsing vulnerabilities jsonObject");
            Utils.getLogger().log(Level.SEVERE , e.getMessage());
            e.printStackTrace();
        }
    }
}
