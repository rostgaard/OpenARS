package dossee.droids;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

/**
 * @author Erik Telepovsky
 * 
 * ErrorActivity shows corresponding error if there is something wrong
 */
public class ErrorActivity extends Activity {
	private long pollID;
	private String error = null;
	
    @Override
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get pollID and error message from previous Activity
        pollID = this.getIntent().getExtras().getLong("pollID");
        error = this.getIntent().getExtras().getString("error");

        //show error screen
    	setContentView(R.layout.poll_error);

    	//set poll id
		((TextView)findViewById(R.id.tv_pollID)).setText("Poll #" + pollID);
			
		// set corresponding message
		
		//INACTIVE POLL SCREEN
		if(error.equals("inactive")) {
			Log.i("poll status","Poll is inactive!");
			((TextView)findViewById(R.id.tv_error)).setText(R.string.inactive);
		
		//ALREADY VOTED
		} else if(error.equals("already voted")) {
			Log.i("poll status","You have already voted in this round!");
			((TextView)findViewById(R.id.tv_error)).setText(R.string.already_voted);
		
		//NOT EXISTING POLL
		} else {
			Log.i("poll status","Poll does NOT exist!");
			((TextView)findViewById(R.id.tv_error)).setText(R.string.no_poll);
		}

		//refresh button
		Button btn_refresh = (Button)findViewById(R.id.btn_refresh);
		btn_refresh.setOnClickListener(RefreshBtnListener);
		return;
    }
    
    /**
	 * OnClickListener for Refresh (try again) button.
	 * Application will try to do last action (join the poll) again
	 */
    private OnClickListener RefreshBtnListener = 
	   	new OnClickListener(){

			public void onClick(View v) {
				//show toast (trying again)
				Toast.makeText(getApplicationContext(), R.string.refreshing, 2000).show();
				
				//start the QuestionActivity again
				Intent intent = new Intent(ErrorActivity.this, QuestionActivity.class);
				intent.putExtra("pollID", pollID);
				//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				ErrorActivity.this.finish();
		}
   };
}