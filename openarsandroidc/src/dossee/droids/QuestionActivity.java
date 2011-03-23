package dossee.droids;

import java.util.ArrayList;
import java.util.Arrays;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

/** 
 * @author Erik Telepovsky
 *
 * QuestionActivity shows the actual question of activated poll
 * with all possible answers. There is also displayed a remaining
 * time (duration) of the poll.
 */
public class QuestionActivity extends ListActivity {
	private long pollID;
	private long questionID;
	private String question;
	private Integer duration;
	private boolean multipleAllowed;
	private String[] answers;
    private Context ctx;
    private Gson gson = new Gson();
    private long remainingTime;
    private boolean statisticsDisplayed = false;
    private MergeAdapter adapter=null;
    private ListView merged_lv;
    private LayoutInflater vi;
    
    @Override
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        String questionString = null;
        
        //get LayoutInflater
        vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        //get pollID from previous Activity
        pollID = this.getIntent().getExtras().getLong("pollID");
        
        try {
        	//retrieve question from the server
        	questionString = RestClient.getInstance().getPoll(Long.toString(pollID));
        	
        	//and make an instance of QuestionJSON class
        	QuestionJSON questionJSON = gson.fromJson(questionString, QuestionJSON.class);
	        	
	        //get data from JSON
	        question = questionJSON.getQuestion();
	        questionID = questionJSON.getQuestionID();
	        answers = questionJSON.getAnswers();
			multipleAllowed = questionJSON.isMultipleAllowed();
			duration = questionJSON.getDuration();

			//build data and show QUESTION SCREEN
        	setContentView(R.layout.question_main);
        	merged_lv = (ListView)findViewById(android.R.id.list);
        	adapter = new MergeAdapter();
        	adapter.addView(buildHeader());
    		adapter.addAdapter(buildList());
    		adapter.addView(buildButton(), true);
    		setListAdapter(adapter);
        	
	        //CountDownTimer of remaining time (question duration)
    		final TextView tv_duration = (TextView) (adapter.getView(0, null, null).findViewById(R.id.tv_duration));
			
    		new CountDownTimer((duration + 1)* 1000, 1000) {
    			
    			 //refresh the remaining time every second
			     public void onTick(long millisUntilFinished) {
			    	 remainingTime = millisUntilFinished / 1000;
			    	 tv_duration.setText(Long.toString(remainingTime));
			     }

			   //StatisticsActivity starts on countdown finish
			     public void onFinish() {
			    
			    	 //show the statistics if it is not displayed already
			    	 if(!statisticsDisplayed) {
			    		 statisticsDisplayed =  true;
				    	 Intent intent = new Intent(QuestionActivity.this, StatisticsActivity.class);					
				    	 intent.putExtra("pollID", pollID);
				    	 startActivity(intent);
				    	 QuestionActivity.this.finish();
			    	 }
			     }
			}.start();
			
        } catch(Exception e) { //JsonSyntaxException
    		e.printStackTrace();
    		
    		Intent intent = new Intent(QuestionActivity.this, ErrorActivity.class);
			intent.putExtra("pollID", pollID);
			intent.putExtra("error", questionString);
			startActivity(intent);
			QuestionActivity.this.finish();
			return;
			
		}/* catch (Exception e) {
			e.printStackTrace();
		}*/
    }
    
    /**
     * Build the answer list of question.
     * If multiple answers are allowed, build list with checkboxes
     * otherwise with radio buttons.
     * @return ArrayAdapter with possible options
     */
    private ArrayAdapter<String> buildList() {
		
    	int resource = 0;
    	
    	if(multipleAllowed) {
    		resource = android.R.layout.simple_list_item_multiple_choice;
    		merged_lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    	} else {
    		resource = android.R.layout.simple_list_item_single_choice;
    		merged_lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    	}
    	
		ArrayAdapter<String> aa = new ArrayAdapter<String>(
				this, 
				resource,
				new ArrayList<String>(Arrays.asList(answers)));
		
		merged_lv.setAdapter(aa);
		
		return(aa);
	}

	
    /**
     * Builds the Vote Button with listener
     * @return View
     */
	private View buildButton() {
		Button result=new Button(this);
		result.setText(R.string.vote);
		result.setOnClickListener(VoteBtnListener);
		
		return(result);
	}
	
	/**
	 * Builds the header of layout.
	 * (pollID, question and duration are displayed)
	 * @return
	 */
	private View buildHeader() {
		//get header layout
		View v = vi.inflate(R.layout.question_header, null);		
		
		//set poll ID & question
		((TextView)v.findViewById(R.id.tv_pollID)).setText("Poll #" + pollID);
		((TextView)v.findViewById(R.id.tv_question)).setText(question);
		
		return(v);
	}
    
	/**
	 * OnClickListener for Vote button.
	 * Application will send vote to the server 
	 * if at least one answer is selected.
	 */
	private OnClickListener VoteBtnListener = 
	   	new OnClickListener(){

			public void onClick(View v) {
				
				String voteResponseJSON = null;
				
				try {
					ctx = getApplicationContext();
					int duration = 2000;
					List<String> answersList = new ArrayList<String>();
					answersList.clear();
					
					//get selected answers			
					for(int i = 1; i < answers.length + 1; i++) {
						
						//if option is marked / selected
						if(merged_lv.getCheckedItemPositions().get(i)) {
							
							//get answer on actual position of cycle
							String selectedAnswer = (String) merged_lv.getItemAtPosition(i);
							//insert it into vote answers
							answersList.add(selectedAnswer);
						}
					}
		
					//if there is at least one selected answer, VOTE!
					if (answersList.size() != 0) {
						
						//convert arrayList to array of strings
						String[] answers = (String[]) answersList.toArray(new String[0]);
						
						//get unique responderID (macAddr / uuid)
						String responderID = UniqueID_Generator.getInstance(getApplication()).getUniqueID();
						Log.i("OA responderID", responderID);
						
						//create new VoteJSON object and convert it to string using gson
						VoteJSON vote = new VoteJSON(pollID, questionID, (String[]) answers, responderID);
						Gson gson = new Gson();
						String voteJSON = gson.toJson(vote);
						Log.i("voteJSON",voteJSON);
						
						//send vote to server
						voteResponseJSON = RestClient.getInstance().sendVote(pollID, voteJSON);
						gson = new Gson();
						VoteResponseJSON voteResponse = gson.fromJson(voteResponseJSON, VoteResponseJSON.class); 
				
						//if voting was successful
						if(voteResponse.isVoteSuccessful()) {
							
							//show thanks toast
							Toast.makeText(ctx, R.string.thanks, duration).show();
							
							//show the statistics if it is not displayed already
							if(!statisticsDisplayed) {
					    		statisticsDisplayed =  true;
						    	Intent intent = new Intent(QuestionActivity.this, WaitActivity.class);					
						    	intent.putExtra("pollID", pollID);
						    	intent.putExtra("remainingTime", remainingTime);
						    	startActivity(intent);
						    	QuestionActivity.this.finish();
							}
						} else {
							//if error occured (voteResponse is not successful)
							//show error
							Toast.makeText(ctx, R.string.error, duration).show();
						}
						
					//there is no selected answer !
					} else {
						//show Toast
						Toast.makeText(ctx, R.string.choose, duration).show();
					}
				} catch(JsonSyntaxException e) {
					//e.printStackTrace();
					
					//ALREADY VOTED IN THIS ROUND
					if(voteResponseJSON.equals("already voted")) {
						//start ErrorActivity
						Intent intent = new Intent(QuestionActivity.this, ErrorActivity.class);
						intent.putExtra("pollID", pollID);
						intent.putExtra("error", voteResponseJSON);
						startActivity(intent);
						QuestionActivity.this.finish();
						return;
					}
				}
			}
   };
}