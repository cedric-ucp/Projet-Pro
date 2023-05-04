package connection;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

public class CVE{
    private final String url;
    private final HashMap <String , String> responseData = new HashMap<>();
    private final ArrayList<String> vulnerabilities;
    public CVE(String url , String vulnerability){
        this.url = url;
        vulnerabilities = Utils.stringToArray(vulnerability , ",");
        if(vulnerabilities == null || vulnerabilities.size() == 0){
            Utils.getLogger().log(Level.SEVERE , "Error parsing vulnerabilities object or no vulnerabilities to scan");
            return;
        }
        for(String vul : vulnerabilities)
            sendRequest(vul);
    }
    private void sendRequest(String vulnerability){
        if(url != null && Utils.valueExists(vulnerability)) {
            try {
                Utils.getLogger().log(Level.INFO , "launch CVE request for cve : " + vulnerability);
                Request.Builder builder = new Request.Builder();
                String apiKey = "a44b03e3-f9c2-49c9-b142-e4cc5cb66d39";
                builder.url(buildRequestUrl(vulnerability))
                        .addHeader("apiKey" , apiKey)
                        .get();
                Request request = builder.build();
                Response response = new OkHttpClient().newCall(request).execute();
                Utils.responseLog(response , response.code() , "1");

                if(response.body() != null) {
                    String responseBody = response.body().string();
                    if (responseBody != null && !responseBody.isEmpty()){
                        Utils.getLogger().log(Level.INFO, "response : " + responseBody);
                        fillResponseData(new JSONObject(responseBody));
                    }
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
    private void fillResponseData(JSONObject responseBody){
        try {
            if (responseBody != null) {
                JSONArray vulnerabilities = (JSONArray) responseBody.get("vulnerabilities");
                if(vulnerabilities != null){
                    JSONObject vulnData = (JSONObject) vulnerabilities.get(vulnerabilities.length() - 1);
                    if(vulnData != null) {
                        Utils.getLogger().log(Level.INFO , "cve : " + vulnData);
                        JSONObject cve = (JSONObject) vulnData.get("cve");
                        if(cve != null) {
                            getCveData(cve);
                        }
                        else
                            Utils.getLogger().log(Level.SEVERE , "Error : cve jsonObject is null");
                    }
                    else
                        Utils.getLogger().log(Level.SEVERE , "Error : vulnerability's intel jsonObject is null");
                }
                else{
                    Utils.getLogger().log(Level.SEVERE , "vulnerabilities jsonObject is null");
                }
            }
            else {
                Utils.getLogger().log(Level.SEVERE, "Error : responseBody JsonObject is null");
            }
        }
        catch(Exception e){
            Utils.getLogger().log(Level.SEVERE , "Error while parsing vulnerabilities jsonObject");
            Utils.getLogger().log(Level.SEVERE , e.getMessage());
            e.printStackTrace();
        }
    }

    private void getCveData(JSONObject cve){
        try {
            String id = (String) cve.get("id");
            if (Utils.valueExists(id)) {
                responseData.put("cve_id", id);
            }
            else
                Utils.getLogger().log(Level.SEVERE, "Error : id jsonObject is null");

            String published = (String) cve.get("published");
            if (Utils.valueExists(published)) {
                responseData.put("published", published);
            }
            else
                Utils.getLogger().log(Level.SEVERE, "Error : published jsonObject is null");

            JSONArray descriptions = cve.getJSONArray("descriptions");
            if (descriptions != null && descriptions.length() > 0) {
                JSONObject englishDescription = (JSONObject) descriptions.get(0);
                String description = (String) englishDescription.get("value");
                if (Utils.valueExists(description)) {
                    responseData.put("description", description);
                }
                else
                    Utils.getLogger().log(Level.SEVERE, "Error : value jsonObject is null");
            }
            else
                Utils.getLogger().log(Level.SEVERE, "Error : descriptions jsonArray is null or Empty");
            getCveMetrics(cve);
        }
        catch(Exception e){
            Utils.getLogger().log(Level.SEVERE , "Error while getting cve " + e);
        }
    }

    private void getCveMetrics(JSONObject cve){
       JSONObject metrics = cve.getJSONObject("metrics");
       try {
           if (metrics != null) {
               JSONArray metricV31 = metrics.getJSONArray("cvssMetricV31");
               if (metricV31 != null) {
                   JSONObject cvssData = metricV31.getJSONObject(0).getJSONObject("cvssData");
                   if (cvssData != null) {
                       getCvssData(cvssData);
                   }
               }
           }
           else
               Utils.getLogger().log(Level.SEVERE , "Error : metrics jsonArray is null");
       }
       catch(Exception e){
           Utils.getLogger().log(Level.SEVERE , "Error while getting cveMetrics " + e);
       }
    }

    private void getCvssData(JSONObject cvssData){
        try {
            if (cvssData.has("baseSeverity"))
                responseData.put("baseSeverity", cvssData.getString("baseSeverity"));
            if (cvssData.has("confidentialityImpact"))
                responseData.put("confidentialityImpact", cvssData.getString("confidentialityImpact"));
            if (cvssData.has("attackComplexity"))
                responseData.put("attackComplexity", cvssData.getString("attackComplexity"));
            if (cvssData.has("scope"))
                responseData.put("scope", cvssData.getString("scope"));
            if (cvssData.has("attackVector"))
                responseData.put("attackVector", cvssData.getString("attackVector"));
            if (cvssData.has("availabilityImpact"))
                responseData.put("availabilityImpact", cvssData.getString("availabilityImpact"));
            if (cvssData.has("integrityImpact"))
                responseData.put("integrityImpact", cvssData.getString("integrityImpact"));
            if (cvssData.has("privilegesRequired"))
                responseData.put("privilegesRequired", cvssData.getString("privilegesRequired"));
            if (cvssData.has("version"))
                responseData.put("version", cvssData.getString("version"));
            if (cvssData.has("userInteraction"))
                responseData.put("userInteraction", cvssData.getString("userInteraction"));

            System.out.println("responseData " + responseData);
        }
        catch(Exception e){
            Utils.getLogger().log(Level.SEVERE, "Error while getting cvssData " + e);
        }
    }
}
















