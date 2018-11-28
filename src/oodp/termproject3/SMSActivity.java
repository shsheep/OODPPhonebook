package oodp.termproject3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ToggleButton;
import android.widget.TwoLineListItem;

public class SMSActivity extends Activity implements OnItemClickListener {
    DBManager dbManager;
    SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_sms);

	dbManager = new DBManager(this);

	Cursor cursor = dbManager.GetListOfReceivedSMS("0");
	adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor,
		new String[] {"nameOrNumber", "message" },
		new int[] { android.R.id.text1, android.R.id.text2 }, 0);

	ListView lv_sms = (ListView) findViewById(R.id.lv_sms);
	lv_sms.setAdapter(adapter);
	lv_sms.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.sm, menu);
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

    public void bt_sms_new_Click(View view) {
	Intent intent = new Intent(this, NewSMSActivity.class);
	intent.putExtra("mode", "new");
	intent.putExtra("s_number", "보내는 이");
	intent.putExtra("r_number", "받는 이");
	intent.putExtra("message", "내용");
	startActivityForResult(intent, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	if (resultCode == RESULT_OK) {
	    /*
	     * 해당 Activity가 보내 준 정보는 이 메서드의 argument인 data에 들어 있습니다.
	     */
	    String mode = data.getStringExtra("mode");
	    String newsNumber = data.getStringExtra("s_number");
	    String newrNumber = data.getStringExtra("r_number");
	    String newContent = data.getStringExtra("message");

	    if (mode.equals("new") == true) {
		dbManager.InsertSMS(newsNumber, newrNumber, newContent);
	    }

	    adapter.changeCursor(dbManager.GetListOfReceivedSMS("0"));
	    dbManager.GetListOfReceivedSMS("0");

	}
    }

    public void bt_sms_mode_Click(View view) {
	ToggleButton bt_sms_mode = (ToggleButton) findViewById(R.id.bt_sms_mode);
	if (bt_sms_mode.isChecked() == false) {
	    adapter.changeCursor(dbManager.GetListOfReceivedSMS("0"));
	    dbManager.GetListOfReceivedSMS("0");
	} else if (bt_sms_mode.isChecked() == true) {
	    adapter.changeCursor(dbManager.GetListOfSendedSMS("0"));
	    dbManager.GetListOfSendedSMS("0");
	}
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	ToggleButton bt_sms_mode = (ToggleButton) findViewById(R.id.toggleButton1);
	if (bt_sms_mode.isChecked() == false) {
	    TwoLineListItem item = (TwoLineListItem) view;
	    String r_number = item.getText1().getText().toString();

	    Intent intent = new Intent(this, NewSMSActivity.class);
	    intent.putExtra("mode", "reply");
	    intent.putExtra("s_number", "0");
	    intent.putExtra("r_number", r_number);
	    intent.putExtra("message", "내용" );
	    startActivityForResult(intent, 0);
	} else if (bt_sms_mode.isChecked() == true) {
	    dbManager.DeleteSMS(id);
	    
	    ToggleButton bt_sms_mod = (ToggleButton) findViewById(R.id.bt_sms_mode);
		if (bt_sms_mod.isChecked() == false) {
		    adapter.changeCursor(dbManager.GetListOfReceivedSMS("0"));
		    dbManager.GetListOfReceivedSMS("0");
		} else if (bt_sms_mod.isChecked() == true) {
		    adapter.changeCursor(dbManager.GetListOfSendedSMS("0"));
		    dbManager.GetListOfSendedSMS("0");
		}
	}
    }

    public void bt_sms_mode_Click2(View view) {
//	ToggleButton bt_sms_mode = (ToggleButton) findViewById(R.id.bt_sms_mode);
//	if (bt_sms_mode.isChecked() == false) {
//	    TwoLineListItem item = (TwoLineListItem) view;
//	    String r_number = item.getText1().getText().toString();
//
//	    Intent intent = new Intent(this, NewSMSActivity.class);
//	    intent.putExtra("mode", "reply");
//	    intent.putExtra("s_number", "0");
//	    intent.putExtra("r_number", r_number);
//	    intent.putExtra("message", "내용" );
//	    startActivityForResult(intent, 0);
//	}
    }
}
