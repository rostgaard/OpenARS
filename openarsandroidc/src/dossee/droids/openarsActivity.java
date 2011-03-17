package dossee.droids;

import android.app.*;
import android.os.Bundle;
import android.widget.*;


public class openarsActivity extends Activity {
    /** Called when the activity is first created. */
	
	private String lv_items[] = {"First question", "Second question"};
	
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
        ListView lView = (ListView)findViewById(R.id.lv_questions);

        /**
         * make a multiple choice answer layout
         */
        /*
        
        lView.setAdapter(new ArrayAdapter<String>(this,
        			android.R.layout.simple_list_item_multiple_choice,lv_items));
        lView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        */
        
        /**
         * make a single choice answer layout
         */
        
        lView.setAdapter(new ArrayAdapter<String>(this,
    			android.R.layout.simple_list_item_single_choice,lv_items));
        lView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }
    
         
   
  
}




