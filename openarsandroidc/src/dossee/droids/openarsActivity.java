package dossee.droids;

import android.app.*;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;


public class openarsActivity extends Activity {
    /** Called when the activity is first created. */
	
	private String lv_items[] = {"First question", "Second question"};
	private Toast vote;
    private Context ctx;
    private ListView lView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.main);
       // setContentView(R.layout.answers_ch);
        setContentView(R.layout.answers_rb);
        /**
         * Question list
         * TODO: fetch the questions from JSON here 
         * instead of lv_answers
         */
        lView = (ListView)findViewById(R.id.lv_questions);
        
        /**
         * make a multiple choice answer layout
         */
        
        /*
        lView.setAdapter(new ArrayAdapter<String>(this,
        			android.R.layout.simple_list_item_multiple_choice,lv_items));
        lView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        
        Button btn_mult = (Button)findViewById(R.id.btn_vote_multiple);
        btn_mult.setOnClickListener(VoteBtnListener);
   
        */
        
        /**
         * make a single choice answer layout
         */
        
        lView.setAdapter(new ArrayAdapter<String>(this,
    			android.R.layout.simple_list_item_single_choice,lv_items));
        lView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        
        Button btn_single = (Button)findViewById(R.id.btn_vote_single);
        btn_single.setOnClickListener(VoteBtnListener);
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
				
				//Log.i("SELECTED ITEM", lView.getSelectedItem().toString());
				
				if (lView.getSelectedItem()!=null) {
					
					CharSequence thank_text = "Thank you for voting!";
					
					vote = Toast.makeText(ctx, thank_text, duration);
					vote.show();					
				} else {
					CharSequence ask_text = "Please choose your answer!";
					vote = Toast.makeText(ctx, ask_text, duration);
					vote.show();
				}
					
			}
	   
   };
 
   
  
}




