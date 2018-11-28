package oodp.termproject3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class NewSMSActivity extends Activity {

    long id;
    String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_new_sms);

	Intent intent = getIntent();
	id = intent.getLongExtra("id", -1);
	mode = intent.getStringExtra("mode");

	EditText et_s_number = (EditText) findViewById(R.id.et_sender);
	EditText et_r_number = (EditText) findViewById(R.id.et_r_number5555);
	EditText et_message = (EditText) findViewById(R.id.et_message);

	et_s_number.setText(intent.getStringExtra("s_number"));
	et_r_number.setText(intent.getStringExtra("r_number"));
	et_message.setText(intent.getStringExtra("message"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.new_sm, menu);
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

    public void bt_sms_accept_Click(View view) {
	EditText et_s_number = (EditText) findViewById(R.id.et_sender);
	EditText et_r_number = (EditText) findViewById(R.id.et_r_number5555);
	EditText et_message = (EditText) findViewById(R.id.et_message);

	Intent intent = new Intent();
	intent.putExtra("id", id);
	intent.putExtra("mode", mode);
	intent.putExtra("s_number", et_s_number.getText().toString());
	intent.putExtra("r_number", et_r_number.getText().toString());
	intent.putExtra("message", et_message.getText().toString());

	setResult(RESULT_OK, intent);
	finish();
    }
}
