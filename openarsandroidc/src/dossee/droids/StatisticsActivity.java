package dossee.droids;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import dossee.droids.entities.ResultsJSON;
import dossee.droids.rest.RestClient;

import android.app.*;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

/**
 * @author Erik Telepovsky
 *
 * StatisticsActivity shows the results 
 * of voting in last poll.
 */
public class StatisticsActivity extends Activity {
	private long pollID;
	private String question;
	private String[] answers;
	private int[] votes;
    private Gson gson;
    
    @Override
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //get pollID from previous Activity
        pollID = this.getIntent().getExtras().getLong("pollID");
        
        try {
        	//if voting round is finished
        	//retrieve poll results from the server
        	
        	String resultsString = RestClient.getInstance().getResults(pollID);
        	gson = new Gson();
        	ResultsJSON resultsJSON = gson.fromJson(resultsString, ResultsJSON.class);
        	
        	//get data from JSON
	        question = resultsJSON.getQuestion();
	        answers = resultsJSON.getAnswers();
	        votes = resultsJSON.getVotes();
			
	        //show statistics screen
			setContentView(R.layout.statistics);
			
			//set poll id & question
	    	((TextView)findViewById(R.id.tv_pollID)).setText("Poll #" + pollID);
	    	((TextView)findViewById(R.id.tv_question)).setText(question);
	    	 
	    	//generate answers
	    	LinearLayout ll = (LinearLayout)findViewById(R.id.ll_statistics_answers);
	    	TextView tv;
	    	
	    	int counter = 0;
	    	for(String answer : answers) {
	    		
	    		//create new TextView
	    		tv = new TextView(getApplicationContext(), null);
	    		
	    		int countAnswerVotes = votes[counter]; 
	    		
	    		//if there is only one vote, label will be "vote"
	    		//otherwise "votes"
	    		String voteString = "vote";
	    		if(countAnswerVotes != 1)
	    			voteString += "s";
	    			
	    		//set text of new created TextView
	    		tv.setText(answer + ": " + countAnswerVotes + " " + voteString);
			    
	    		//show it in the results list
	    		ll.addView(tv);
	    		
	    		//increment counter of question answers
			    counter++;
	    	}
	    	 
	    	//exit button
	        Button btn_exit = (Button)findViewById(R.id.btn_exit);
	        btn_exit.setOnClickListener(ExitBtnListener);
	         
		} catch(JsonSyntaxException e) {
			e.printStackTrace();
		}
    }

    /**
	 * OnClickListener for Exit button.
	 * Close StatisticsActivity.
	 */
	 private OnClickListener ExitBtnListener = 
		 new OnClickListener(){
	
		 public void onClick(View v) {
			 StatisticsActivity.this.finish();
		 }
	 };
}