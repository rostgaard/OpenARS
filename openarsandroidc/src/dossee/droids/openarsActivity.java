package dossee.droids;

import java.util.UUID;

import dossee.droids.R;
import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

/**
 * @author Erik Telepovsky
 *
 * application startup activity
 * user interface for joining the poll
 */
public class openarsActivity extends Activity {
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //set contentView to main screen
        setContentView(R.layout.main);
        
        //set settings listener
        TextView tv_settings = (TextView)findViewById(R.id.tv_settings);
        ImageView iv_gear = (ImageView)findViewById(R.id.iv_gear);
        tv_settings.setOnClickListener(SettingsListener);
        iv_gear.setOnClickListener(SettingsListener);
        
        //set go listener
        Button btn_go = (Button)findViewById(R.id.btn_go);
        btn_go.setOnClickListener(GoBtnListener);
    }
    
    /**
	 * OnClickListener for Go button (join the poll).
	 * Application will send request to the server 
	 * if pollID was entered
	 */
    private OnClickListener GoBtnListener = 
	   	new OnClickListener(){

			public void onClick(View v) {
				//get pollID from input
				EditText et_pollID = (EditText)findViewById(R.id.et_pollID);
				String pollID = et_pollID.getText().toString();
				
				//if pollID is empty, show Toast with message
				if(pollID.length() == 0) {
					Toast.makeText(getApplicationContext(), R.string.enter_poll_id, 2000).show();
				//if it is entered properly
				} else {
					//put Extra variables into intent
					Intent intent = new Intent(openarsActivity.this, QuestionActivity.class);
					intent.putExtra("pollID", Long.parseLong(pollID));
					
					//start QuestionActivity
					startActivity(intent);
					
					//set TextView to default value
					et_pollID.setText("");
				}
			}
   };
   
   /**
	* OnClickListener for Settings.
	* If settings label or gear icon is pressed
	* the SettingsActivity will start
	*/
   private OnClickListener SettingsListener = 
	   	new OnClickListener(){

			public void onClick(View v) {
				//create new intent
				Intent intent = new Intent(openarsActivity.this, SettingsActivity.class);
				
				//start SettingsActivity
				startActivity(intent);
			}
  };
    
}