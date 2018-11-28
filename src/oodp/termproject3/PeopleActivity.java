package oodp.termproject3;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.ToggleButton;
import android.widget.TwoLineListItem;

public class PeopleActivity extends Activity implements OnItemClickListener {
    DBManager dbManager;
    SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_people);

	dbManager = new DBManager(this);

	Cursor cursor = dbManager.GetListOfPeople();
	adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor,
		new String[] { "name", "number" }, new int[] { android.R.id.text1, android.R.id.text2 }, 0);

	ListView lv_people = (ListView) findViewById(R.id.lv_people);
	lv_people.setAdapter(adapter);
	lv_people.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.people, menu);
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

    public void bt_people_new_Click(View view) {
	Intent intent = new Intent(this, NewPeopleActivity.class);
	intent.putExtra("mode", "new");
	intent.putExtra("name", "새 이름");
	intent.putExtra("number", "새 번호");
	startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	if (resultCode == RESULT_OK) {
	    /*
	     * 해당 Activity가 보내 준 정보는 이 메서드의 argument인 data에 들어 있습니다.
	     */
	    String mode = data.getStringExtra("mode");
	    String newName = data.getStringExtra("name");
	    String newNumber = data.getStringExtra("number");

	    if (mode.equals("new") == true) {
		dbManager.InsertPerson(newName, newNumber);
	    } else {
		long id = data.getLongExtra("id", -1);
		dbManager.UpdatePeople(id, newName, newNumber);
	    }

	    adapter.changeCursor(dbManager.GetListOfPeople());
	}
    }

    public void bt_people_mode_Click(View view) {
	// 할 일 없엉
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	ToggleButton bt_people_mode = (ToggleButton) findViewById(R.id.bt_people_mode);
	if (bt_people_mode.isChecked() == false)
	    dbManager.DeletePeople(id);
	else {
	    TwoLineListItem item = (TwoLineListItem) view;
	    String name = item.getText1().getText().toString();
	    String number = item.getText2().getText().toString();

	    Intent intent = new Intent(this, NewPeopleActivity.class);
	    intent.putExtra("id", id);
	    intent.putExtra("mode", "modify");
	    intent.putExtra("name", name);
	    intent.putExtra("number", number);
	    startActivityForResult(intent, 0);
	}

	adapter.changeCursor(dbManager.GetListOfPeople());
    }
}
