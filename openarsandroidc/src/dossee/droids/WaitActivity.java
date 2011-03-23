package dossee.droids;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.*;

/**
 * @author Erik Telepovsky
 *
 * WaitActivity is displayed if user has voted in the poll
 * and the remaining time is greater than 0s.
 */
public class WaitActivity extends Activity {
	private long pollID;
	private long remainingTime;
	
    @Override
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get pollID and remaining time (duration) from previous Activity
        pollID = this.getIntent().getExtras().getLong("pollID");
        remainingTime = this.getIntent().getExtras().getLong("remainingTime");
 
        //show wait screen
    	setContentView(R.layout.wait);
        
        //set poll id
		((TextView)findViewById(R.id.tv_pollID)).setText("Poll #" + pollID);
		
        //CountDownTimer of remaining time (question duration)
        final TextView tv_duration = (TextView)findViewById(R.id.tv_duration);

        new CountDownTimer((remainingTime)* 1000, 1000) {

        	//refresh the remaining time every second
        	public void onTick(long millisUntilFinished) {
        		tv_duration.setText(Long.toString(millisUntilFinished / 1000));
        		remainingTime = millisUntilFinished / 1000;
        	}
        	
        	//StatisticsActivity starts on countdown finish 
        	public void onFinish() {
        		Intent intent = new Intent(WaitActivity.this, StatisticsActivity.class);					
        		intent.putExtra("pollID", pollID);
        		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        		startActivity(intent);
        		WaitActivity.this.finish();
        	}
        }.start();
		
    }
}