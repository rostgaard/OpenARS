package controllers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import play.mvc.Controller;
import Utility.RestClient;

import com.google.gson.Gson;

public class Application extends Controller {

    public static void index() {
        render();
    }
    
    public static void managepoll() {
        render();
    }
}