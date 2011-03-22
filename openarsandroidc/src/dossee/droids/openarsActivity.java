package dossee.droids;

import java.util.UUID;

import dossee.droids.R;
import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;


public class openarsActivity extends Activity {
	String macAddr;
    UUID uuid;
   
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
    
    private OnClickListener GoBtnListener = 
	   	new OnClickListener(){

			public void onClick(View v) {
				Log.i("VoteBtnListener - openarsActivity","onClick");
				EditText et_pollID = (EditText)findViewById(R.id.et_pollID);
				String pollID = et_pollID.getText().toString();
				
				if(pollID.length() == 0) {
					Toast.makeText(getApplicationContext(), "Please, enter the poll ID", 2000).show();
				} else {
					Log.i("VoteBtnListener - openarsActivity","startActivity");
					
					
					//put Extra variables into intent
					Intent intent = new Intent(openarsActivity.this, QuestionActivity.class);
					intent.putExtra("pollID", Long.parseLong(pollID));
					
					//start QuestionActivity
					startActivity(intent);
					
					//set TextView to default value
					et_pollID.setText("");
					
					//openarsActivity.this.finish();
				}
			}
   };
   
   
   private OnClickListener SettingsListener = 
	   	new OnClickListener(){

			public void onClick(View v) {
				//create new intent
				Intent intent = new Intent(openarsActivity.this, SettingsActivity.class);
				
				//start QuestionActivity
				startActivity(intent);
			}
  };
    
}