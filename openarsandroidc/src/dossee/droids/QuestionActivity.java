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
       // setContentView(R.layout.main);
        setContentView(R.layout.question);
        
        lView = (ListView)findViewById(R.id.lv_options);
       
        //get question JSON from server
        try {
        JSONObject questionJSON = RestClient.getInstance().getQuestion();
        //Log.i("openarsActivity.java", "question JSON.toString()" + questionJSON.toString());
        
        	String pollID = questionJSON.getString("pollId");
			String questionID = questionJSON.getString("questionId");
			String question = questionJSON.getString("question");
			JSONArray answersArray = questionJSON.getJSONArray("answers");

			//Gson gson = new Gson();
			//String test = "answers":["Orangeeee","Humaaaaan","Meeee"]";
			//Question questionClass = gson.fromJson(questionJSON.toString(), Question.class);
			//Log.i("Question answers", questionClass.getAnswers().toString());
			
			String multipleAllowed = questionJSON.getString("multipleAllowed");
			
			//checkboxes / radio buttons (mulltiple Allowed?)
			if(multipleAllowed.equals("true")) {
				lView.setAdapter(new ArrayAdapter<String>(this,
		    			android.R.layout.simple_list_item_multiple_choice,lv_items));
				lView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			} else {
				lView.setAdapter(new ArrayAdapter<String>(this,
		    			android.R.layout.simple_list_item_single_choice,lv_items));
				lView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			}

			//set data
			((TextView)findViewById(R.id.tv_pollID)).setText("Poll #" + pollID);
			((TextView)findViewById(R.id.tv_question)).setText(question);
	
			for (int i = 0; i < answersArray.length(); i++) {
				lv_items.add(answersArray.get(i).toString());
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
        Button btn = (Button)findViewById(R.id.btn_vote);
        btn.setOnClickListener(VoteBtnListener);
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