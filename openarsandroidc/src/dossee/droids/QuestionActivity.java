package dossee.droids;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dossee.droids.rest.RestClient;

import android.app.*;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;


public class QuestionActivity extends Activity {
    /** Called when the activity is first created. */
	
	private List<String> lv_items = new ArrayList<String>();
	private Toast vote;
    private Context ctx;
    private ListView lView;
    private Resources r;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        //get question JSON from server
        try {
	        JSONObject questionJSON = RestClient.getInstance().getPoll("2345");
	        //Log.i("openarsActivity.java", "question JSON.toString()" + questionJSON.toString());
	        
        	final String pollID = questionJSON.getString("pollID");
			String questionID = questionJSON.getString("questionID");
			final String question = questionJSON.getString("question");
			final JSONArray answersArray = questionJSON.getJSONArray("answers");
			final String multipleAllowed = questionJSON.getString("multipleAllowed");
			Integer duration = Integer.parseInt(questionJSON.getString("duration"));
			
			//Gson gson = new Gson();
			//String test = "answers":["Orangeeee","Humaaaaan","Meeee"]";
			//Question questionClass = gson.fromJson(questionJSON.toString(), Question.class);
			//Log.i("Question answers", questionClass.getAnswers().toString());
			
			if(duration == 0) {
				Toast.makeText(getApplicationContext(), "Poll is INACTIVE", 2000).show();
				return;
			}
			
			setContentView(R.layout.inactive_poll);

			// set poll ID
			((TextView)findViewById(R.id.tv_pollID)).setText("Poll #" + pollID);
			
			final TextView tv_duration = (TextView)findViewById(R.id.tv_seconds);
			tv_duration.setText(Integer.toString(duration));
			
			CountDownTimer cdt = new CountDownTimer((duration + 1)* 1000, 1000) {

			     public void onTick(long millisUntilFinished) {
			    	 tv_duration.setText(Long.toString(millisUntilFinished / 1000));
			     }

			     public void onFinish() {
			    	 setContentView(R.layout.question);
			    	 lView = (ListView)findViewById(R.id.lv_options);
						
						//checkboxes / radio buttons (mulltiple Allowed?)
						if(multipleAllowed.equals("true")) {
							lView.setAdapter(new ArrayAdapter<String>(QuestionActivity.this,
					    			android.R.layout.simple_list_item_multiple_choice,lv_items));
							lView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
						} else {
							lView.setAdapter(new ArrayAdapter<String>(QuestionActivity.this,
					    			android.R.layout.simple_list_item_single_choice,lv_items));
							lView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
						}

						//set poll ID & question
						((TextView)findViewById(R.id.tv_question)).setText(question);
						((TextView)findViewById(R.id.tv_pollID)).setText("Poll #" + pollID);
						
						try {
							for (int i = 0; i < answersArray.length(); i++) {
									lv_items.add(answersArray.get(i).toString());
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						Button btn = (Button)findViewById(R.id.btn_vote);
				        btn.setOnClickListener(VoteBtnListener);
			     }
			  }.start();

		} catch (JSONException e) {
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
				r = getResources();
				if (counter != 0) {
					CharSequence thank_text = r.getString(R.string.thanks);
					
					vote = Toast.makeText(ctx, thank_text, duration);
					vote.show();					
				} else {
					CharSequence choose_text = r.getString(R.string.choose);
					vote = Toast.makeText(ctx, choose_text, duration);
					vote.show();
				}
					
			}
	   
   };
}