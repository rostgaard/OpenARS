package dossee.droids;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class ErrorActivity extends Activity {
	private long pollID;
	private String error = null;
	
    @Override
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get Extras from previous Activity
        pollID = this.getIntent().getExtras().getLong("pollID");
        error = this.getIntent().getExtras().getString("error");

        //show error screen
    	setContentView(R.layout.poll_error);

    	//set poll id
		((TextView)findViewById(R.id.tv_pollID)).setText("Poll #" + pollID);
			
		// set message
		if(error.equals("inactive")) {
			//INACTIVE POLL SCREEN
			Log.i("poll status","Poll is inactive!");
			((TextView)findViewById(R.id.tv_error)).setText(R.string.inactive);
		} else {
			//NOT EXISTING POLL
			Log.i("poll status","Poll does NOT exist!");
			((TextView)findViewById(R.id.tv_error)).setText(R.string.no_poll);
		}

		//refresh button
		Button btn_refresh = (Button)findViewById(R.id.btn_refresh);
		btn_refresh.setOnClickListener(RefreshBtnListener);
		return;
    }
    
    private OnClickListener RefreshBtnListener = 
	   	new OnClickListener(){

			public void onClick(View v) {
				Log.i("Refresh (ErrorActivity)","onClick");
				Intent intent = new Intent(ErrorActivity.this, QuestionActivity.class);
				intent.putExtra("pollID", pollID);
				Toast.makeText(getApplicationContext(), R.string.refreshing, 2000).show();
				startActivity(intent);
				ErrorActivity.this.finish();
		}
   };
}