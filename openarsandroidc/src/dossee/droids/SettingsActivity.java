package dossee.droids;

import dossee.droids.rest.RestClient;
import android.app.*;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

/**
 * @author Erik Telepovsky
 * 
 * SettingsActivity allows user 
 * to change server url and port of communication
 */
public class SettingsActivity extends Activity {
	
	private EditText et_serverURL;
	private EditText et_serverPort;
	
    @Override
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //set contentView to settings screen
        setContentView(R.layout.settings);
    
        //get EditTexts
        et_serverURL = (EditText)findViewById(R.id.et_serverURL);
        et_serverPort = (EditText)findViewById(R.id.et_serverPort);
        
        //get values of server URL and port 
        //which are stored in RestClient instance
        String serverUrl = RestClient.getInstance().getServerURL();
        Integer serverPort = RestClient.getInstance().getServerPort();
        
        //show values of server URL and port
        //in the edit texts
        et_serverURL.setHint(serverUrl);
        et_serverPort.setHint(Integer.toString(serverPort));
        
        //set save button listener
        Button btn_save = (Button)findViewById(R.id.btn_save);
        btn_save.setOnClickListener(SaveBtnListener);
    }
    
    /**
	 * OnClickListener for Save button.
	 * New settings will be saved in the RestClient class
	 */
    private OnClickListener SaveBtnListener = 
	   	new OnClickListener(){

			public void onClick(View v) {
				
				//trim entered data
				String serverURL = et_serverURL.getText().toString().trim();
				String serverPort = et_serverPort.getText().toString().trim();
				
				//check if server URL is not empty
				//if not, fix it
				if(serverURL.length() != 0) {
					
					//check if server starts with "http://"
					if(!serverURL.startsWith("http://"))
						serverURL = "http://" + serverURL;
					
					//save new server URL
					RestClient.getInstance().setServerURL(serverURL);
				}
				
				//check if server port is not empty
				if(serverPort.length() != 0)
					//if not, save new value
					RestClient.getInstance().setServerPort(Integer.parseInt(serverPort));
				
				//show the message, that settings have been saved
				Toast.makeText(getApplicationContext(), R.string.settings_saved, 2000).show();
				
				//finish this activity
				SettingsActivity.this.finish();
			}
  };
}