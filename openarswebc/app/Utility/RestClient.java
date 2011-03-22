package Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import models.Vote;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

public class RestClient {

	private static RestClient instance;
	private static String server_address = "http://json.openars.dk";
	private static int server_port = 80;
	private int response_code;
	private String message;
	private String response;
	private final String tag = "RestClient";

	/**
	 * Constructor
	 * 
	 * @param address
	 *            is address of server
	 * @param port
	 *            the server is running on
	 */
	private RestClient(String address, int port) {
		server_address = address;
		server_port = port;
	}

	/**
	 * getInstance of RestClient Singleton
	 * 
	 * @return
	 */
	public static RestClient getInstance() {
		if (instance == null)
			instance = new RestClient(server_address, server_port);
		return instance;
	}

	/**
	 * This method provide hard assembled process to connect to server
	 * 
	 * @param service
	 */
	private void executeRequest(HttpRequestBase method, String service,
			JSONObject json) throws Exception {
		DefaultHttpClient client = new DefaultHttpClient();
		StringBuilder sb = new StringBuilder();
		try {
			String url = server_address + ":" + Integer.toString(server_port)
					+ "/" + service;
			URI u = new URI(url);
			method.setURI(u);

			if ((method instanceof HttpPost) && (json != null)) {
				ByteArrayEntity bae = new ByteArrayEntity(json.toString()
						.getBytes());
				bae.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				((HttpPost) method).setEntity(bae);
			}

			HttpResponse hr = client.execute(method);

			this.response_code = hr.getStatusLine().getStatusCode();
			this.message = hr.getStatusLine().getReasonPhrase();

			HttpEntity entity = hr.getEntity();
			if (entity != null) {
				InputStream is = entity.getContent();
				BufferedReader br = new BufferedReader(
						new InputStreamReader(is));
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				response = sb.toString();
				is.close();
			}
		} catch (URISyntaxException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			client.getConnectionManager().shutdown();
		}
	}

	/**
	 * Encapsulate http response from server to JSONObject
	 * 
	 * @param service
	 * @return JSONObject
	 */
	private JSONObject getService(String service) {
		JSONObject jso = new JSONObject();
		HttpRequestBase hrb = new HttpGet();

		try {
			this.executeRequest(hrb, service, null);
			jso = new JSONObject(this.response);
		} catch (JSONException ex) {
		} finally {
			return jso;
		}
	}

	/**
	 * Send json to the server in the request
	 * 
	 * @param service
	 * @param json
	 * @return
	 */
	private JSONObject postService(String service, JSONObject json) {
		JSONObject result = new JSONObject();
		HttpRequestBase hrb = new HttpPost();
		try {
			this.executeRequest(hrb, service, json);
			result = new JSONObject(this.response);
		} catch (JSONException ex) {
			ex.printStackTrace();
		} finally {
			return result;
		}
	}

	/**
	 * Returns the response of the HTTP request
	 * 
	 * @return
	 */
	public String getResponse() {
		return this.response;
	}

	/**
	 * Handle method
	 * 
	 * @return get question encapsulated in JSONObject
	 */
	public JSONObject getQuestion(String id) {
		return this.getService(StaticQuery.get_question(id));
	}

	public boolean vote(Vote v) {
		try {
			Gson g = new Gson();
			return this.postService(StaticQuery.vote(v.pollID),
					new JSONObject(g.toJson(v))).getBoolean("voteSuccessful");
		} catch (JSONException e) {
		}

		return false;
	}

	public void activate(String id, String adminkey, int duration) {
		try {
			JSONObject o = new JSONObject();
			o.put("duration", duration);
			this.postService(StaticQuery.activate(id, adminkey), o);
		} catch (JSONException e) {
		}
	}

	/**
	 * Post a new question to the server
	 * 
	 * @param o
	 *            The object to base the JSON on.
	 * @return A JSON object with the adminkey and pollID.
	 * @throws JSONException
	 */
	public JSONObject createQuestion(Object o) throws JSONException {
		Gson gson = new Gson();
		JSONObject json = new JSONObject(gson.toJson(o));
		return this.postService(StaticQuery.post_question, json);
	}

	public JSONObject getResults(String id) {
		return this.getService(StaticQuery.get_results(id));
	}

	/**
	 * Private class for identifying base URL for services
	 */
	private static class StaticQuery {
		public final static String post_question = "newPoll";

		public static String vote(int id) {
			return "vote/" + id;
		}

		public static String get_question(String id) {
			return id;
		}

		public static String get_results(String id) {
			return "getResults/" + id;
		}

		public static String activate(String id, String adminkey) {
			return "activation/" + id + "/" + adminkey;
		}
	}
}
