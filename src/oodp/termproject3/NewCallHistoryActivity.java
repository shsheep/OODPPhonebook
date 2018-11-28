package oodp.termproject3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class NewCallHistoryActivity extends Activity {

    
    long id;
    String mode;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_new_call_history);
	
	Intent intent = getIntent();
	id = intent.getLongExtra("id", -1);
	mode = intent.getStringExtra("mode");
	
	EditText et_sender = (EditText) findViewById(R.id.et_sender);
	EditText et_receiver = (EditText) findViewById(R.id.et_receiver);
	EditText et_duration = (EditText) findViewById(R.id.et_duration);
	
	et_sender.setText(intent.getStringExtra("s_call_number"));
	et_receiver.setText(intent.getStringExtra("r_call_number"));
	et_duration.setText(intent.getStringExtra("duration"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.new_call_history, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	// Handle action bar item clicks here. The action bar will
	// automatically handle clicks on the Home/Up button, so long
	// as you specify a parent activity in AndroidManifest.xml.
	int id = item.getItemId();
	if (id == R.id.action_settings) {
	    return true;
	}
	return super.onOptionsItemSelected(item);
    }
    
    public void bt_callhistory_accept_Click(View view) {
	EditText et_sender = (EditText) findViewById(R.id.et_sender);
	EditText et_receiver = (EditText) findViewById(R.id.et_receiver);
	EditText et_duration = (EditText) findViewById(R.id.et_duration);

	Intent intent = new Intent();
	intent.putExtra("id", id);
	intent.putExtra("mode", mode);
	intent.putExtra("s_call_number", et_sender.getText().toString());
	intent.putExtra("r_call_number", et_receiver.getText().toString());
	intent.putExtra("duration", et_duration.getText().toString());

	setResult(RESULT_OK, intent);
	finish();
    }
}
