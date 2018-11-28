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

public class CallHistoryActivity extends Activity  {

    DBManager dbManager;
    SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_call_history);

	dbManager = new DBManager(this);

	Cursor cursor = dbManager.GetListOfReceivedCallHistory("0");
	adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor,
		new String[] { "nameOrNumber", "duration" }, new int[] { android.R.id.text1, android.R.id.text2 }, 0);

	ListView lv_callhistory = (ListView) findViewById(oodp.termproject3.R.id.lv_callhistory);
	lv_callhistory.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.call_history, menu);
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

    public void bt_callhistory_new_Click(View view) {
	Intent intent = new Intent(this, NewCallHistoryActivity.class);
	intent.putExtra("mode", "new");
	intent.putExtra("s_call_number", "�߽���");
	intent.putExtra("r_call_number", "������");
	intent.putExtra("duration", "��ȭ�ð�");
	startActivityForResult(intent, 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	if (resultCode == RESULT_OK) {
	    /*
	     * �ش� Activity�� ���� �� ������ �� �޼����� argument�� data�� ��� �ֽ��ϴ�.
	     */
	    String mode = data.getStringExtra("mode");
	    String newsNumber = data.getStringExtra("s_call_number");
	    String newrNumber = data.getStringExtra("r_call_number");
	    String newDuration = data.getStringExtra("duration");

	    if (mode.equals("new") == true) {
		dbManager.InsertCallHistory(newsNumber, newrNumber, newDuration);
	    }

	    adapter.changeCursor(dbManager.GetListOfReceivedCallHistory("0"));
	    dbManager.GetListOfReceivedCallHistory("0");

	}
    }

    public void bt_sms_mode_Click(View view) {
	// �� �� ����
    }

}
