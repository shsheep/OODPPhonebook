package oodp.termproject3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity
{
	DBManager dbManager;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		dbManager = new DBManager(this);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	
	public void bt_people_Click(View view)
	{
		Intent intent = new Intent(this, PeopleActivity.class);
		startActivity(intent);
	}	
	
	public void bt_sms_Click(View view)
	{
		Intent intent = new Intent(this, SMSActivity.class);
		startActivity(intent);
	}
	
	public void bt_callhistory_Click(View view)
	{
		Intent intent = new Intent(this, CallHistoryActivity.class);
		startActivity(intent);
	}
	
	public void bt_reset_Click(View view)
	{
		dbManager.Reset();
	}
}
