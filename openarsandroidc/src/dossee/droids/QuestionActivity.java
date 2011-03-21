package dossee.droids;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dossee.droids.rest.RestClient;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.method.QwertyKeyListener;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;


public class QuestionActivity extends Activity {
    /** Called when the activity is first created. */
	
	private List<String> answers = new ArrayList<String>();
	private Toast vote;
    private Context ctx;
    private ListView lView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String pollID = this.getIntent().getExtras().getString("pollID");
        //final String pollID = "404389";
        
        //get question JSON from server
        try {
		        JSONObject questionJSON = RestClient.getInstance().getPoll(pollID);
		        Log.i("openarsActivity.java", "question JSON.toString()" + questionJSON.toString());
		        
				String questionID = questionJSON.getString("questionID");
				final String question = questionJSON.getString("question");
				final JSONArray answersArray = questionJSON.getJSONArray("answers");
				final String multipleAllowed = questionJSON.getString("multipleAllowed");
				Integer duration = Integer.parseInt(questionJSON.getString("duration"));

//				String questionID = "1";
//				final String question = "how are you?";
//				final JSONArray answersArray = new JSONArray();
//				answersArray.put("Good");
//				answersArray.put("As usually");
//				answersArray.put("Bad");
//				final String multipleAllowed = "false";
//				Integer duration = 5;
        	
        	//Gson gson = new Gson();
			//String test = "answers":["Orangeeee","Humaaaaan","Meeee"]";
			//Question questionClass = gson.fromJson(questionJSON.toString(), Question.class);
			//Log.i("Question answers", questionClass.getAnswers().toString());
        	
        	if(duration == 0) {
        		//INACTIVE POLL SCREEN
        		setContentView(R.layout.inactive_poll);
        		// set poll ID
    			((TextView)findViewById(R.id.tv_pollID)).setText("Poll #" + pollID);
				return;
			}
        	
        	//QUESTION SCREEN
        	setContentView(R.layout.question);
        	
        	//set poll ID & question
			((TextView)findViewById(R.id.tv_pollID)).setText("Poll #" + pollID);
			((TextView)findViewById(R.id.tv_question)).setText(question);
			
			
			lView = (ListView)findViewById(R.id.lv_options);
			
			//checkboxes / radio buttons (mulltiple Allowed?)
			if(multipleAllowed.equals("true")) {
				lView.setAdapter(new ArrayAdapter<String>(QuestionActivity.this,
		    			android.R.layout.simple_list_item_multiple_choice,answers));
				lView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			} else {
				lView.setAdapter(new ArrayAdapter<String>(QuestionActivity.this,
		    			android.R.layout.simple_list_item_single_choice,answers));
				lView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			}
			
			//generate answers
			try {
				for (int i = 0; i < answersArray.length(); i++) {
					answers.add(answersArray.get(i).toString());
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Button btn = (Button)findViewById(R.id.btn_vote);
	        btn.setOnClickListener(VoteBtnListener);
        	
	        
	        //CountDownTimer
			final TextView tv_duration = (TextView)findViewById(R.id.tv_duration);
			
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
				
				int counter = 0;
				for(int i = 0; i < lView.getChildCount(); i++) {
					if(lView.getCheckedItemPositions().get(i))
						counter++;
				}
				
				if (counter != 0) {
					vote = Toast.makeText(ctx, R.string.thanks, duration);
					vote.show();					
				} else {
					vote = Toast.makeText(ctx, R.string.choose, duration);
					vote.show();
				}
					
			}
   };
   
   private OnClickListener ExitBtnListener = 
	   	new OnClickListener(){

			public void onClick(View v) {
				QuestionActivity.this.finish();
			}
  };
}