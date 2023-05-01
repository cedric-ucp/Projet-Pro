package connection;

import okhttp3.*;
import utils.Const;
import utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class  ApiCommunication {
    private final Map<String , String> data = new HashMap<>();
    protected Response response = null;
    protected final Logger LOG;
    RequestBody body = null;
    protected int status = 0;
    protected String  state = "";
    Request.Builder builder = new Request.Builder();
    public ApiCommunication(Map <String , String> headerParameters){
        LOG = Utils.getLogger();
        for(Map.Entry entry : headerParameters.entrySet()){
            builder.addHeader((String) entry.getKey() , (String) entry.getValue());
        }
    }
    protected void sendRequest(){
        try {
            Request request = builder.build();
            if(response != null)
                response = null;
            response = new OkHttpClient().newCall(request).execute();
            status = response.code();
            state = response.message();
        }
        catch (Exception e){
            LOG.log(Level.SEVERE , Const.REQUEST_FAILED_MESSAGE);
            e.printStackTrace();
            LOG.log(Level.SEVERE , e.getMessage());
        }
    }

    protected void buildRequestBody(){
        LOG.log(Level.INFO , "data : " + data);
        if(body != null)
            body = null;
        FormBody.Builder builder = new FormBody.Builder();
        for(Map.Entry entry : data.entrySet()){
            builder.add((String) entry.getKey() , (String) entry.getValue());
        }
        body = builder.build();
    }
    public Map<String , String> getData(){
        return data;
    }
}
