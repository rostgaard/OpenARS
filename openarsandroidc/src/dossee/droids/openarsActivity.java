package dossee.droids;

import dossee.droids.R;
import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;


public class openarsActivity extends Activity {
   
    Button btn_go;
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.i("openarsActivity","onCreate");
        btn_go = (Button)findViewById(R.id.btn_go);
        btn_go.setOnClickListener(VoteBtnListener);
    }
    
    private OnClickListener VoteBtnListener = 
	   	new OnClickListener(){

			public void onClick(View v) {
				Log.i("VoteBtnListener - openarsActivity","onClick");
				EditText et_pollID = (EditText)findViewById(R.id.et_pollID);
				String pollID = et_pollID.getText().toString();
				
				if(pollID.length() == 0) {
					Toast.makeText(getApplicationContext(), "Please, enter the poll ID", 2000).show();
				} else {
					Log.i("VoteBtnListener - openarsActivity","startActivity");
					Intent intent = new Intent(openarsActivity.this, QuestionActivity.class);
					startActivity(intent);
					openarsActivity.this.finish();
				}
			}
   };
    
}