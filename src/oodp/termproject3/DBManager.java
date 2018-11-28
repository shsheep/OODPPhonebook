package oodp.termproject3;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * TP#3에서 데이터베이스 액세스 부분에 해당하는 요소들을 구현하기 위한 클래스입니다.
 * 구체적으로, 이 클래스에는 다양한 조건에 따라 쿼리를 수행하고 그 결과를 가리키는 <code>Cursor</code> 인스턴스를 반환하는 메서드들이 포함됩니다.<br>
 * <br>
 * <b>주의:</b><br>
 * 아래의 코드들은 일종의 예시일 뿐입니다. 여러분은 각자 팀의 계획에 맞도록 아래의 내용을 적절히 수정하거나 새로운 메서드들을 추가해야 합니다.
 * 
 * @author Racin
 *
 */
public class DBManager
{
	private DBHelper helper;
	private SQLiteDatabase db;
	
	public DBManager(Activity activityToUseThisFactory)
	{
		helper = new DBHelper(activityToUseThisFactory);
		db = helper.getWritableDatabase();
	}
	
	public Cursor GetListOfPeople()
	{
		//SQL 의미: people table의 모든 것들을 가져오되 이름 순으로 정렬하기
		Cursor cursor = db.rawQuery(	"SELECT * " +
										"FROM people " +
										"ORDER BY name", null);
		return cursor;
	}
	
	public Cursor GetListOfPeopleLike(String nameToSearch)
	{
		//SQL 의미: people table에서 이름에 nameToSearch가 들어가는 모든 것들을 가져오되 이름 순으로 정렬하기
		Cursor cursor;
		
		if ( nameToSearch.isEmpty() == true )
			cursor = GetListOfPeople();
		else
			cursor = db.rawQuery(	"SELECT * " +
									"FROM people " + 
									"WHERE name LIKE ? " +
									"ORDER BY name", new String[] { "%" + nameToSearch + "%" });
		return cursor;
	}
	
	public void InsertPerson(String newName, String newNumber)
	{
		//SQL 의미: people table에 name이 newName이고 number가 newNumber인 새 데이터 추가
		//(여기서 VALUES 내용은 DBHelper.java의 CREATE TABLE 부분에서 적어 놓은 순서에 따라 알맞게 투입해야 하며 _id 부분은 null로 두면 됨)
		db.execSQL(String.format(	"INSERT INTO people " +
									"VALUES ( null, '%s', '%s' );", newName, newNumber));
	}
	
	public void UpdatePeople(String oldName, String oldNumber, String newName, String newNumber)
	{
		//SQL 의미: people table에서 name이 oldName이고 number가 oldNumber인 모든 요소들의 name을 newName으로, number를 newNumber로 변경
		db.execSQL(String.format(	"UPDATE people " +
									"SET name = '%s', number = '%s' " + 
									"WHERE name = '%s' AND number = '%s';", newName, newNumber, oldName, oldNumber));
	}
	
	public void UpdatePeople(long idToUpdate, String newName, String newNumber)
	{
		//SQL 의미: people table에서 _id가 idToUpdate인 요소의 name을 newName으로, number를 newNumber로 변경
		db.execSQL(String.format(	"UPDATE people " +
									"SET name = '%s', number = '%s' " + 
									"WHERE _id = %d;", newName, newNumber, idToUpdate));
	}
	
	public void DeletePeople(String nameToDelete, String numberToDelete)
	{
		//SQL 의미: people table에서 name이 nameToDelete고 number가 numberToDelete인 모든 요소들을 제거
		db.execSQL(String.format(	"DELETE FROM people " + 
									"WHERE name = '%s' AND number = '%s';", nameToDelete, numberToDelete));
	}
	
	public void DeletePeople(long idToDelete)
	{
		//SQL 의미: people table에서 _id가 idToDelete인 요소를 제거
		db.execSQL(String.format(	"DELETE FROM people " + 
									"WHERE _id = %d;", idToDelete));
	}
	
	
	
	
	public Cursor GetListOfSendedSMS(String myNumber)
	{
		//SQL 의미: 1)의 결과와 2)의 결과를 UNION(병합)시켜 가져오기
		//물론 이보다 더 간단한 방법이 있겠지만... 그냥 이대로 쓰도록 합시다.
		Cursor cursor = db.rawQuery(
				//1) sms table의 각 요소들 중 해당 요소의 r_number와 people table의 어떤 요소의 number가 같으며 해당 요소의 s_number가 myNumber와 같은 모든 요소에 대해,
				//   각 요소의 _id와, people table에서 검색해 가져 온 name(이를 nameOrNumber라 명명), 그리고 message를 가져오기
				"SELECT sms._id AS _id, people.name AS nameOrNumber, message " +
				"FROM sms INNER JOIN people ON people.number = sms.r_number " +
				"WHERE s_number = ?" +
				"UNION " +
				//2) sms table의 각 요소들 중 해당 요소의 s_number가 myNumber와 같고 해당 요소의 _id가 3) 목록에 포함되지 않은 모든 요소에 대해,
				//   각 요소의 _id와, r_number(이를 receiver라 명명), 그리고 message를 가져오기
				"SELECT _id, r_number AS nameOrNumber, message " +
				"FROM sms " +
				"WHERE s_number = ? AND _id NOT IN " +
					//3) sms table의 각 요소들 중 해당 요소의 r_number와 people table의 어떤 요소의 number가 같으며 해당 요소의 s_number가 myNumber와 같은 모든 요소에 대해,
					//   각 요소의 _id를 가져오기
					"(SELECT sms._id AS _id " +
					"FROM sms INNER JOIN people ON people.number = sms.r_number " +
					"WHERE s_number = ?)", new String[] { myNumber, myNumber, myNumber });
		return cursor;
	}
	
	public Cursor GetListOfReceivedSMS(String myNumber)
	{
		//SQL 의미: 1)의 결과와 2)의 결과를 UNION(병합)시켜 가져오기
		//물론 이보다 더 간단한 방법이 있겠지만... 그냥 이대로 쓰도록 합시다.
		Cursor cursor = db.rawQuery(
				//1) sms table의 각 요소들 중 해당 요소의 s_number와 people table의 어떤 요소의 number가 같으며 해당 요소의 r_number가 myNumber와 같은 모든 요소에 대해,
				//   각 요소의 _id와, people table에서 검색해 가져 온 name(이를 nameOrNumber라 명명), 그리고 message를 가져오기
				"SELECT sms._id AS _id, people.name AS nameOrNumber, message " +
				"FROM sms INNER JOIN people ON people.number = sms.s_number " +
				"WHERE r_number = ?" +
				"UNION " +
				//2) sms table의 각 요소들 중 해당 요소의 r_number가 myNumber와 같고 해당 요소의 _id가 3) 목록에 포함되지 않은 모든 요소에 대해,
				//   각 요소의 _id와, s_number(이를 nameOrNumber라 명명), 그리고 message를 가져오기
				"SELECT _id, s_number AS nameOrNumber, message " +
				"FROM sms " +
				"WHERE r_number = ? AND _id NOT IN " +
					//3) sms table의 각 요소들 중 해당 요소의 s_number와 people table의 어떤 요소의 number가 같으며 해당 요소의 r_number가 myNumber와 같은 모든 요소에 대해,
					//   각 요소의 _id를 가져오기
					"(SELECT sms._id AS _id " +
					"FROM sms INNER JOIN people ON people.number = sms.s_number " +
					"WHERE r_number = ?)", new String[] { myNumber, myNumber, myNumber });
		return cursor;
	}
	
	public void InsertSMS(String s_number, String r_number, String message)
	{
		//SQL 의미: sms table에 s_number, r_number, message를 가진 새 데이터 추가
		db.execSQL(String.format(	"INSERT INTO sms " + 
									"VALUES (null, '%s', '%s', '%s');", s_number, r_number, message));
	}
	
	public void DeleteSMS(long idToDelete)
	{
		//SQL 의미: sms table에서 _id가 idToDelete인 요소를 제거
		db.execSQL(String.format(	"DELETE FROM sms " + 
									"WHERE _id = %d;", idToDelete));
	}
	

	public Cursor GetListOfSendedCallHistory(String myNumber)
	{
		//SQL 의미: 1)의 결과와 2)의 결과를 UNION(병합)시켜 가져오기
		//물론 이보다 더 간단한 방법이 있겠지만... 그냥 이대로 쓰도록 합시다.
		Cursor cursor = db.rawQuery(
				//1) callhistory table의 각 요소들 중 해당 요소의 r_number와 people table의 어떤 요소의 number가 같으며 해당 요소의 s_number가 myNumber와 같은 모든 요소에 대해,
				//   각 요소의 _id와, people table에서 검색해 가져 온 name(이를 nameOrNumber라 명명), 그리고 duration를 가져오기
				"SELECT callhistory._id AS _id, people.name AS nameOrNumber, duration " +
				"FROM callhistory INNER JOIN people ON people.number = callhistory.r_number " +
				"WHERE s_number = ?" +
				"UNION " +
				//2) callhistory table의 각 요소들 중 해당 요소의 s_number가 myNumber와 같고 해당 요소의 _id가 3) 목록에 포함되지 않은 모든 요소에 대해,
				//   각 요소의 _id와, r_number(이를 receiver라 명명), 그리고 duration를 가져오기
				"SELECT _id, r_number AS nameOrNumber, duration " +
				"FROM callhistory " +
				"WHERE s_number = ? AND _id NOT IN " +
					//3) callhistory table의 각 요소들 중 해당 요소의 r_number와 people table의 어떤 요소의 number가 같으며 해당 요소의 s_number가 myNumber와 같은 모든 요소에 대해,
					//   각 요소의 _id를 가져오기
					"(SELECT callhistory._id AS _id " +
					"FROM callhistory INNER JOIN people ON people.number = callhistory.r_number " +
					"WHERE s_number = ?)", new String[] { myNumber, myNumber, myNumber });
		return cursor;
	}
	
	public Cursor GetListOfReceivedCallHistory(String myNumber)
	{
		//SQL 의미: 1)의 결과와 2)의 결과를 UNION(병합)시켜 가져오기
		//물론 이보다 더 간단한 방법이 있겠지만... 그냥 이대로 쓰도록 합시다.
		Cursor cursor = db.rawQuery(
				//1) sms table의 각 요소들 중 해당 요소의 s_number와 people table의 어떤 요소의 number가 같으며 해당 요소의 r_number가 myNumber와 같은 모든 요소에 대해,
				//   각 요소의 _id와, people table에서 검색해 가져 온 name(이를 nameOrNumber라 명명), 그리고 message를 가져오기
				"SELECT callhistory._id AS _id, people.name AS nameOrNumber, duration " +
				"FROM callhistory INNER JOIN people ON people.number = callhistory.s_number " +
				"WHERE r_number = ?" +
				"UNION " +
				//2) callhistory table의 각 요소들 중 해당 요소의 r_number가 myNumber와 같고 해당 요소의 _id가 3) 목록에 포함되지 않은 모든 요소에 대해,
				//   각 요소의 _id와, s_number(이를 nameOrNumber라 명명), 그리고 duration를 가져오기
				"SELECT _id, s_number AS nameOrNumber, duration " +
				"FROM callhistory " +
				"WHERE r_number = ? AND _id NOT IN " +
					//3) callhistory table의 각 요소들 중 해당 요소의 s_number와 people table의 어떤 요소의 number가 같으며 해당 요소의 r_number가 myNumber와 같은 모든 요소에 대해,
					//   각 요소의 _id를 가져오기
					"(SELECT callhistory._id AS _id " +
					"FROM callhistory INNER JOIN people ON people.number = callhistory.s_number " +
					"WHERE r_number = ?)", new String[] { myNumber, myNumber, myNumber });
		return cursor;
	}
	
	public void InsertCallHistory(String s_number, String r_number, String duration)
	{
		//SQL 의미: sms table에 s_number, r_number, message를 가진 새 데이터 추가
		db.execSQL(String.format(	"INSERT INTO callhistory " + 
									"VALUES (null, '%s', '%s', '%s');", s_number, r_number, duration));
	}
	
	public void DeleteCallHistory(long idToDelete)
	{
		//SQL 의미: sms table에서 _id가 idToDelete인 요소를 제거
		db.execSQL(String.format(	"DELETE FROM callhistory " + 
									"WHERE _id = %d;", idToDelete));
	}
	
	
	public void Reset()
	{
		//DBHelper class에 정의된 '모든 테이블 제거 후 재생성' 메서드 실행
		helper.onUpgrade(db, db.getVersion(), db.getVersion());
	}
}
