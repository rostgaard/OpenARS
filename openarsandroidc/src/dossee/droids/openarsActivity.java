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
        setContentView(R.layout.answers_ch);
        ListView lView = (ListView)findViewById(R.id.lv_questions);
        lView.setAdapter(new ArrayAdapter<String>(this,
        			android.R.layout.simple_list_item_multiple_choice,lv_items));
        lView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }
    
   
  
}




