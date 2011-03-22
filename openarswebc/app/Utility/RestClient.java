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
	// private static String server_address = "http://devel2.openars.dk";
	// private static String server_address = "http://78.47.162.117";
	// private static String server_address = "http://192.168.0.2";
	//private static String server_address = "http://172.29.40.161";
	private static String server_address = "http://json.openars.dk";
	private static int server_port = 80;
	//private static int server_port = 9000;
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
			String stringJSON) {
		DefaultHttpClient client = new DefaultHttpClient();
		StringBuilder sb = new StringBuilder();
		try {
			String url = server_address + ":" + Integer.toString(server_port)
					+ "/" + service;
			URI u = new URI(url);
			method.setURI(u);

			if ((method instanceof HttpPost) && (stringJSON.length() != 0)) {
				ByteArrayEntity bae = new ByteArrayEntity(stringJSON.getBytes());
				bae.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				((HttpPost) method).setEntity(bae);
			}

			HttpResponse hr = client.execute(method);
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
		} catch (IOException ex) {
		} finally {
			client.getConnectionManager().shutdown();
		}
	}

	/**
	 * Encapsulate http response from server to String
	 * 
	 * @param service
	 * @return JSONObject
	 */
	@SuppressWarnings("finally")
	private String getService(String service) {
		HttpRequestBase hrb = new HttpGet();
		String ret = "ERROR";
		try {
			this.executeRequest(hrb, service, null);
			ret = this.response;
		} finally {
			return ret;
		}
	}

	/**
	 * Send json to the server in the request
	 * 
	 * @param service
	 * @param json
	 * @return
	 */
	@SuppressWarnings("finally")
	private String postService(String service, String stringJSON) {
		HttpRequestBase hrb = new HttpPost();
		String ret = "ERROR";
		try {
			this.executeRequest(hrb, service, stringJSON);
			ret = this.response;
		} finally {
			return ret;
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
	public String getQuestion(String id) {
		return this.getService(StaticQuery.get_question(id));
	}

	public boolean vote(Vote v) {
		try {
			Gson g = new Gson();
			JSONObject o = new JSONObject(this.postService(StaticQuery
					.vote(v.pollID), g.toJson(v)));
			return o.getBoolean("voteSuccessful");
		} catch (JSONException e) {
		}

		return false;
	}

	public String activate(String id, String adminkey, int duration) {
		try {
			JSONObject o = new JSONObject();
			o.put("duration", duration);
			return this.postService(StaticQuery.activate(id, adminkey), o
					.toString());
		} catch (JSONException e) {
		}
		return null;
	}

	/**
	 * Post a new question to the server
	 * 
	 * @param o
	 *            The object to base the JSON on.
	 * @return A JSON object with the adminkey and pollID.
	 * @throws JSONException
	 */
	public String createQuestion(Object o) throws JSONException {
		Gson gson = new Gson();
		return this.postService(StaticQuery.post_question, gson.toJson(o));
	}

	/**
	 * Handle method
	 * 
	 * @return get poll results from server
	 */
	public String getResults(String pollID, String adminkey) {
		return this.getService(StaticQuery.get_results(pollID, adminkey));
	}

	public boolean checkAdminkey(String id, String adminkey)
			throws JSONException {
		String res = this.getService(StaticQuery
				.get_checkadminkey(id, adminkey));
		return Boolean.parseBoolean(res);
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

		public static String get_results(String id, String adminkey) {
			if (adminkey != null)
				return "getResults/" + id + "/" + adminkey;
			else
				return "getResults/" + id;
		}

		public static String get_checkadminkey(String id, String adminkey) {
			return "checkAdminKey/" + id + "/" + adminkey;
		}

		public static String activate(String id, String adminkey) {
			return "activation/" + id + "/" + adminkey;
		}
	}
}