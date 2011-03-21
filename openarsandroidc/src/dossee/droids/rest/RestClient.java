package dossee.droids.rest;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import javax.security.auth.login.LoginException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
//import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

public class RestClient{

    private static RestClient instance;
	private static String server_address = "http://devel1.openars.dk";
    private static int server_port = 80;
    private int response_code;
    private String message;
    private String response;
    private final String tag = "RestClient";

    /**
     * Constructor
     * @param address is address of server
     * @param port the server is running on
     */
    private RestClient(String address, int port) {
        server_address = address;
        server_port = port;
    }
    
    
    /**
     * getInstance of RestClient Singleton
     * @return
     */
    public static RestClient getInstance() {
    	if(instance == null) 
    		instance = new RestClient(server_address, server_port);
    	return instance;
    }

    /**
     * This method provide hard assembled process to connect to server
     * @param service
     */
    private void executeRequest(HttpRequestBase method, String service, JSONObject json) throws LoginException {
        DefaultHttpClient client = new DefaultHttpClient();
        StringBuilder sb = new StringBuilder();
        try {
        	String url = server_address + ":" + Integer.toString(server_port) + "/" + service;
        	//String url = server_address + "/" + service;
        	URI u = new URI(url);
            method.setURI(u);
            Log.i(this.tag, url);
            
            if ((method instanceof HttpPost) && (json != null)) {
                ByteArrayEntity bae = new ByteArrayEntity(json.toString().getBytes());
                bae.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                ((HttpPost) method).setEntity(bae);
            }

            HttpResponse hr = client.execute(method);

            this.response_code = hr.getStatusLine().getStatusCode();
            this.message = hr.getStatusLine().getReasonPhrase();
            Log.i(this.tag, Integer.toString(this.response_code) + " - " + this.message);
            
            //if(Integer.toString(this.response_code).equals("401"))
            if(this.message.equals("Unauthorized")) {
            	throw new LoginException("Unauthorized");
            }
			
            HttpEntity entity = hr.getEntity();
            if (entity != null) {
                InputStream is = entity.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                response = sb.toString();
                is.close();
            }
        } catch (URISyntaxException ex) {
            Log.e("ERROR 3", ex.getMessage(),ex);
        } catch (IOException ex) {
            Log.e("ERROR 2", ex.getMessage(),ex);
        } finally {
            client.getConnectionManager().shutdown();
        }
    }

    /**
     * Encapsulate http response from server to JSONObject
     * @param service
     * @return JSONObject
     */
    @SuppressWarnings("finally")
	private JSONObject getService(String service) {
        JSONObject jso = new JSONObject();
        HttpRequestBase hrb = new HttpGet();

        try {
        	this.executeRequest(hrb, service, null);
            jso = new JSONObject(this.response);
        } catch (LoginException ex) {
            Log.e("UNAUTHORIZED", ex.getMessage());
        } catch (JSONException ex) {
            Log.e("ERROR 1", ex.getMessage(),ex);
        } finally {
            return jso;
        }
    }

    /**
     * Send json to the server in the request
     * @param service
     * @param json
     * @return
     */
    @SuppressWarnings("finally")
	private JSONObject postService(String service, JSONObject json) {
        JSONObject jso = new JSONObject();
        HttpRequestBase hrb = new HttpPost();
        try {
        	this.executeRequest(hrb, service, json);
            json = new JSONObject(this.response);
        } catch (LoginException ex) {
            Log.e("UNAUTHORIZED", ex.getMessage());
        } catch (JSONException ex) {
            Log.e("ERROR 1", ex.getMessage(),ex);
        } finally {
            return jso;
        }
    }
    
    /**
     * Returns the response of the HTTP request
     * @return
     */
    public String getResponse() {
        return this.response;
    }
    
    /**
     * Handle method
     * @return get question encapsulated in JSONObject
     */
    public JSONObject getPoll(String pollID) {
        return this.getService(StaticQuery.get_poll + "/" + pollID);
    }

    
    /**
     * Private class for identifying base URL for services
     */
    private class StaticQuery {
        public final static String get_poll = "";
    }
	
}
