package dossee.droids;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import dossee.droids.entities.QuestionJSON;
import dossee.droids.entities.VoteJSON;
import dossee.droids.rest.RestClient;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;


public class TestActivity extends Activity {
    /** Called when the activity is first created. */
	
	private long pollID;
	private long questionID;
	private String question;
	private Integer duration;
	private boolean multipleAllowed;
	private String[] answers;
    private Context ctx;
    private ListView lView;
    private String responderID;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //get Extras from previous Activity
        pollID = Long.parseLong(this.getIntent().getExtras().getString("pollID"));
        
        //generate unique ResponderID
        responderID = UniqueID_Generator.getInstance().getUniqueID();
        
        //get question JSON from server
        try {
        	String guestionString = RestClient.getInstance().getPoll(Long.toString(pollID));
        	Log.i("guestionString",guestionString);
        	Gson gson = new Gson();
        	
        	try {
        		QuestionJSON questionJSON = gson.fromJson(guestionString, QuestionJSON.class);
	        	Log.i("questionJSON",questionJSON.toString());
	        	
	        	//get data from JSON
	        	question = questionJSON.getQuestion();
	        	questionID = questionJSON.getQuestionID();
	        	answers = questionJSON.getAnswers();
				multipleAllowed = questionJSON.isMultipleAllowed();
				duration = questionJSON.getDuration();
				duration = 100;
				
        	} catch(JsonSyntaxException e) {
        		//display screen of not existing poll
        		//e.printStackTrace();
        		Log.i("GSON","Poll does NOT exist!");
        		setContentView(R.layout.poll_error);
        		
        		// set poll ID & message
    			((TextView)findViewById(R.id.tv_pollID)).setText("Poll #" + pollID);
    			((TextView)findViewById(R.id.tv_error)).setText(R.string.no_poll);
    			
    			//refresh button
		        Button btn_refresh = (Button)findViewById(R.id.btn_refresh);
		        btn_refresh.setOnClickListener(RefreshBtnListener);
    			return;
        	}
        	
        	if(duration == 0) {
        		//INACTIVE POLL SCREEN
        		setContentView(R.layout.poll_error);
        		
        		// set poll ID & message
    			((TextView)findViewById(R.id.tv_pollID)).setText("Poll #" + pollID);
    			((TextView)findViewById(R.id.tv_error)).setText(R.string.inactive);
    			
    			//refresh button
		        Button btn_refresh = (Button)findViewById(R.id.btn_refresh);
		        btn_refresh.setOnClickListener(RefreshBtnListener);
				return;
			}
        	
        	//QUESTION SCREEN
        	
        	setContentView(R.layout.test_main);
        	
        	
        	lView = (ListView)findViewById(R.id.lv_options);
        	  LayoutInflater layoutInflater = (LayoutInflater)this.getSystemService( Context.LAYOUT_INFLATER_SERVICE );  
        	  LinearLayout ll = (LinearLayout)layoutInflater.inflate( R.layout.test_header, null, false );
        	  LinearLayout rr = (LinearLayout)layoutInflater.inflate( R.layout.test_footer, null, false );
        	  lView.addHeaderView(ll);
        	  lView.addFooterView(rr);
        	  lView.setAdapter(new ArrayAdapter<String>(this,
        	    android.R.layout.simple_list_item_single_choice,
        	    android.R.id.text1, answers));
 
        	//set poll ID & question
			((TextView)findViewById(R.id.tv_pollID)).setText("Poll #" + pollID);
			((TextView)findViewById(R.id.tv_question)).setText(question);
			
			
			//mulltipleAllowed? -> checkboxes / radio buttons
			if(multipleAllowed) {
				lView.setAdapter(new ArrayAdapter<String>(TestActivity.this,
		    			android.R.layout.simple_list_item_multiple_choice,answers));
				lView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
				
			} else {
				lView.setAdapter(new ArrayAdapter<String>(TestActivity.this,
		    			android.R.layout.simple_list_item_single_choice,answers));
				lView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			}
			
			Button btn = (Button)findViewById(R.id.btn_vote);
	        btn.setOnClickListener(VoteBtnListener);
        	
	        //CountDownTimer
			final TextView tv_duration = (TextView)findViewById(R.id.tv_duration);
			//final TextView tv_duration = (TextView)ll.findViewById(R.id.tv_duration);
			
			CountDownTimer cdt = new CountDownTimer((duration + 1)* 1000, 1000) {

			     public void onTick(long millisUntilFinished) {
			    	 tv_duration.setText(Long.toString(millisUntilFinished / 1000));
			     }

			     public void onFinish() {
			    	 setContentView(R.layout.statistics);
			    	 ((TextView)findViewById(R.id.tv_pollID)).setText("Poll #" + pollID);
			    	 ((TextView)findViewById(R.id.tv_question)).setText(question);
			    	 
			    	 LinearLayout ll = (LinearLayout)findViewById(R.id.ll_statistics_answers);
			    	 TextView tv;
			    	 
					 //generate answers
			    	 for(String answer : answers) {
			    		 tv = new TextView(getApplicationContext(), null);
					     tv.setText(answer + ": 10 votes");
					     ll.addView(tv);
					 }
			    	 
			    	 //exit button
			         Button btn_exit = (Button)findViewById(R.id.btn_exit);
			         btn_exit.setOnClickListener(ExitBtnListener);
			     }
			  }.start();

		} catch (Exception e) {//JSON
			e.printStackTrace();
		}
		
        
    }
    
   private OnClickListener VoteBtnListener = 
	   	new OnClickListener(){

			public void onClick(View v) {
				/**
				 * If some option is chosen, make a thank-you toast and
				 * TODO change the layout to statistics
				 * 
				 * Else make an ask-for-response toast
				 */

				ctx = getApplicationContext();
				int duration = 2000;
				List<String> answersList = new ArrayList<String>();
				answersList.clear();
				
				//get selected answers
				
				Log.i("answers.length", Integer.toString(answers.length));
				
				ListView lView2 = (ListView)lView.findViewById(R.id.lv_options);
				Log.i("getChildCount of lView2", Integer.toString(lView2.getChildCount()));
								
				for(int i = 0; i < answers.length; i++) {
				//for(int i = 0; i < lView.getChildCount(); i++) {
					//Log.i("ITEM IN lView", lView.getItemAtPosition(i).toString());
					
					if(lView2.getCheckedItemPositions().get(i)) {
						Log.i("checked item", (String) lView2.getItemAtPosition(i));
						String selectedAnswer = (String) lView2.getItemAtPosition(i);
						answersList.add(selectedAnswer);
					}
				}
	
				//if there is at least one selected answer, VOTE!
				if (answersList.size() != 0) {
					
					//convert arrayList to array of strings
					String[] answers = (String[]) answersList.toArray(new String[0]);
					
					//create new VoteJSON object and convert it to string using gson
					VoteJSON vote = new VoteJSON(pollID, questionID, (String[]) answers, responderID);
					Gson gson = new Gson();
					String voteJSON = gson.toJson(vote);
					Log.i("voteJSON",voteJSON);
					
					//send vote to server
					String result = RestClient.getInstance().sendVote(pollID, voteJSON);
					Log.i("sendVote result",result);
					
					//show Toast
					Toast.makeText(ctx, R.string.thanks, duration).show();
					
				//there is no selected answer !
				} else {
					//show Toast
					Toast.makeText(ctx, R.string.choose, duration).show();
				}
					
			}
   };
   
   private OnClickListener ExitBtnListener = 
	   	new OnClickListener(){

			public void onClick(View v) {
				TestActivity.this.finish();
			}
   };
  
   private OnClickListener RefreshBtnListener = 
	   	new OnClickListener(){

			public void onClick(View v) {
				Log.i("RefreshBtnListener - openarsActivity","onClick");
				Intent intent = new Intent(TestActivity.this, TestActivity.class);
				intent.putExtra("pollID", Long.toString(pollID));
				Toast.makeText(getApplicationContext(), R.string.refreshing, 2000).show();
				startActivity(intent);
				TestActivity.this.finish();
		}
   };
}