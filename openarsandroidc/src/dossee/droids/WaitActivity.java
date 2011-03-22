package dossee.droids;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import dossee.droids.entities.QuestionJSON;
import dossee.droids.entities.VoteJSON;
import dossee.droids.entities.VoteResponseJSON;
import dossee.droids.rest.RestClient;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;


public class WaitActivity extends Activity {
	private long pollID;
	private long remainingTime;
	
    @Override
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get Extras from previous Activity
        pollID = this.getIntent().getExtras().getLong("pollID");
        remainingTime = this.getIntent().getExtras().getLong("remainingTime");
 
        //show wait screen
    	setContentView(R.layout.wait);
        
        //set poll id
		((TextView)findViewById(R.id.tv_pollID)).setText("Poll #" + pollID);
		
        //CountDownTimer
        final TextView tv_duration = (TextView)findViewById(R.id.tv_duration);

        CountDownTimer cdt = new CountDownTimer((remainingTime)* 1000, 1000) {

        	public void onTick(long millisUntilFinished) {
        		tv_duration.setText(Long.toString(millisUntilFinished / 1000));
        		remainingTime = millisUntilFinished / 1000;
        	}

        	public void onFinish() {
        		//on countdown finish start StatisticsActivity
        		Intent intent = new Intent(WaitActivity.this, StatisticsActivity.class);					
        		intent.putExtra("pollID", Long.toString(pollID));
        		startActivity(intent);
        		WaitActivity.this.finish();
        	}
        }.start();
		
        
    }
}