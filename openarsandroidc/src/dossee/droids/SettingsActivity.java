package dossee.droids;

import dossee.droids.rest.RestClient;
import android.app.*;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

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
        
        //setValues
        String serverUrl = RestClient.getInstance().getServerURL();
        Integer serverPort = RestClient.getInstance().getServerPort();
        
        et_serverURL.setHint(serverUrl);
        et_serverPort.setHint(Integer.toString(serverPort));
        
        //set saveListener
        Button btn_save = (Button)findViewById(R.id.btn_save);
        btn_save.setOnClickListener(SaveBtnListener);
    }
    
    private OnClickListener SaveBtnListener = 
	   	new OnClickListener(){

			public void onClick(View v) {
				String serverURL = et_serverURL.getText().toString().trim();
				String serverPort = et_serverPort.getText().toString().trim();
				
				if(serverURL.length() != 0) {
					if(!serverURL.startsWith("http://"))
						serverURL = "http://" + serverURL;
					
					RestClient.getInstance().setServerURL(serverURL);
				}
				
				if(serverPort.length() != 0)
					RestClient.getInstance().setServerPort(Integer.parseInt(serverPort));
				
				Toast.makeText(getApplicationContext(), R.string.settings_saved, 2000).show();
				
				SettingsActivity.this.finish();
			}
  };
}