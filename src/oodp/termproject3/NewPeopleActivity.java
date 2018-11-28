package oodp.termproject3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class NewPeopleActivity extends Activity
{
	long id;
	String mode;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_people);
		
		Intent intent = getIntent();
		id = intent.getLongExtra("id", -1);
		mode = intent.getStringExtra("mode");

		EditText et_name = (EditText)findViewById(R.id.et_name);
		EditText et_number = (EditText)findViewById(R.id.et_number);

		et_name.setText(intent.getStringExtra("name"));
		et_number.setText(intent.getStringExtra("number"));
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_people, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if ( id == R.id.action_settings )
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void bt_people_accept_Click(View view)
	{
		EditText et_name = (EditText)findViewById(R.id.et_name);
		EditText et_number = (EditText)findViewById(R.id.et_number);
		
		Intent intent = new Intent();
		intent.putExtra("id", id);
		intent.putExtra("mode", mode);
		intent.putExtra("name", et_name.getText().toString());
		intent.putExtra("number", et_number.getText().toString());
		
		setResult(RESULT_OK, intent);
		finish();
	}
}
