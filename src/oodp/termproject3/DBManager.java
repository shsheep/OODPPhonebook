package oodp.termproject3;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * TP#3���� �����ͺ��̽� �׼��� �κп� �ش��ϴ� ��ҵ��� �����ϱ� ���� Ŭ�����Դϴ�.
 * ��ü������, �� Ŭ�������� �پ��� ���ǿ� ���� ������ �����ϰ� �� ����� ����Ű�� <code>Cursor</code> �ν��Ͻ��� ��ȯ�ϴ� �޼������ ���Ե˴ϴ�.<br>
 * <br>
 * <b>����:</b><br>
 * �Ʒ��� �ڵ���� ������ ������ ���Դϴ�. �������� ���� ���� ��ȹ�� �µ��� �Ʒ��� ������ ������ �����ϰų� ���ο� �޼������ �߰��ؾ� �մϴ�.
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
		//SQL �ǹ�: people table�� ��� �͵��� �������� �̸� ������ �����ϱ�
		Cursor cursor = db.rawQuery(	"SELECT * " +
										"FROM people " +
										"ORDER BY name", null);
		return cursor;
	}
	
	public Cursor GetListOfPeopleLike(String nameToSearch)
	{
		//SQL �ǹ�: people table���� �̸��� nameToSearch�� ���� ��� �͵��� �������� �̸� ������ �����ϱ�
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
		//SQL �ǹ�: people table�� name�� newName�̰� number�� newNumber�� �� ������ �߰�
		//(���⼭ VALUES ������ DBHelper.java�� CREATE TABLE �κп��� ���� ���� ������ ���� �˸°� �����ؾ� �ϸ� _id �κ��� null�� �θ� ��)
		db.execSQL(String.format(	"INSERT INTO people " +
									"VALUES ( null, '%s', '%s' );", newName, newNumber));
	}
	
	public void UpdatePeople(String oldName, String oldNumber, String newName, String newNumber)
	{
		//SQL �ǹ�: people table���� name�� oldName�̰� number�� oldNumber�� ��� ��ҵ��� name�� newName����, number�� newNumber�� ����
		db.execSQL(String.format(	"UPDATE people " +
									"SET name = '%s', number = '%s' " + 
									"WHERE name = '%s' AND number = '%s';", newName, newNumber, oldName, oldNumber));
	}
	
	public void UpdatePeople(long idToUpdate, String newName, String newNumber)
	{
		//SQL �ǹ�: people table���� _id�� idToUpdate�� ����� name�� newName����, number�� newNumber�� ����
		db.execSQL(String.format(	"UPDATE people " +
									"SET name = '%s', number = '%s' " + 
									"WHERE _id = %d;", newName, newNumber, idToUpdate));
	}
	
	public void DeletePeople(String nameToDelete, String numberToDelete)
	{
		//SQL �ǹ�: people table���� name�� nameToDelete�� number�� numberToDelete�� ��� ��ҵ��� ����
		db.execSQL(String.format(	"DELETE FROM people " + 
									"WHERE name = '%s' AND number = '%s';", nameToDelete, numberToDelete));
	}
	
	public void DeletePeople(long idToDelete)
	{
		//SQL �ǹ�: people table���� _id�� idToDelete�� ��Ҹ� ����
		db.execSQL(String.format(	"DELETE FROM people " + 
									"WHERE _id = %d;", idToDelete));
	}
	
	
	
	
	public Cursor GetListOfSendedSMS(String myNumber)
	{
		//SQL �ǹ�: 1)�� ����� 2)�� ����� UNION(����)���� ��������
		//���� �̺��� �� ������ ����� �ְ�����... �׳� �̴�� ������ �սô�.
		Cursor cursor = db.rawQuery(
				//1) sms table�� �� ��ҵ� �� �ش� ����� r_number�� people table�� � ����� number�� ������ �ش� ����� s_number�� myNumber�� ���� ��� ��ҿ� ����,
				//   �� ����� _id��, people table���� �˻��� ���� �� name(�̸� nameOrNumber�� ���), �׸��� message�� ��������
				"SELECT sms._id AS _id, people.name AS nameOrNumber, message " +
				"FROM sms INNER JOIN people ON people.number = sms.r_number " +
				"WHERE s_number = ?" +
				"UNION " +
				//2) sms table�� �� ��ҵ� �� �ش� ����� s_number�� myNumber�� ���� �ش� ����� _id�� 3) ��Ͽ� ���Ե��� ���� ��� ��ҿ� ����,
				//   �� ����� _id��, r_number(�̸� receiver�� ���), �׸��� message�� ��������
				"SELECT _id, r_number AS nameOrNumber, message " +
				"FROM sms " +
				"WHERE s_number = ? AND _id NOT IN " +
					//3) sms table�� �� ��ҵ� �� �ش� ����� r_number�� people table�� � ����� number�� ������ �ش� ����� s_number�� myNumber�� ���� ��� ��ҿ� ����,
					//   �� ����� _id�� ��������
					"(SELECT sms._id AS _id " +
					"FROM sms INNER JOIN people ON people.number = sms.r_number " +
					"WHERE s_number = ?)", new String[] { myNumber, myNumber, myNumber });
		return cursor;
	}
	
	public Cursor GetListOfReceivedSMS(String myNumber)
	{
		//SQL �ǹ�: 1)�� ����� 2)�� ����� UNION(����)���� ��������
		//���� �̺��� �� ������ ����� �ְ�����... �׳� �̴�� ������ �սô�.
		Cursor cursor = db.rawQuery(
				//1) sms table�� �� ��ҵ� �� �ش� ����� s_number�� people table�� � ����� number�� ������ �ش� ����� r_number�� myNumber�� ���� ��� ��ҿ� ����,
				//   �� ����� _id��, people table���� �˻��� ���� �� name(�̸� nameOrNumber�� ���), �׸��� message�� ��������
				"SELECT sms._id AS _id, people.name AS nameOrNumber, message " +
				"FROM sms INNER JOIN people ON people.number = sms.s_number " +
				"WHERE r_number = ?" +
				"UNION " +
				//2) sms table�� �� ��ҵ� �� �ش� ����� r_number�� myNumber�� ���� �ش� ����� _id�� 3) ��Ͽ� ���Ե��� ���� ��� ��ҿ� ����,
				//   �� ����� _id��, s_number(�̸� nameOrNumber�� ���), �׸��� message�� ��������
				"SELECT _id, s_number AS nameOrNumber, message " +
				"FROM sms " +
				"WHERE r_number = ? AND _id NOT IN " +
					//3) sms table�� �� ��ҵ� �� �ش� ����� s_number�� people table�� � ����� number�� ������ �ش� ����� r_number�� myNumber�� ���� ��� ��ҿ� ����,
					//   �� ����� _id�� ��������
					"(SELECT sms._id AS _id " +
					"FROM sms INNER JOIN people ON people.number = sms.s_number " +
					"WHERE r_number = ?)", new String[] { myNumber, myNumber, myNumber });
		return cursor;
	}
	
	public void InsertSMS(String s_number, String r_number, String message)
	{
		//SQL �ǹ�: sms table�� s_number, r_number, message�� ���� �� ������ �߰�
		db.execSQL(String.format(	"INSERT INTO sms " + 
									"VALUES (null, '%s', '%s', '%s');", s_number, r_number, message));
	}
	
	public void DeleteSMS(long idToDelete)
	{
		//SQL �ǹ�: sms table���� _id�� idToDelete�� ��Ҹ� ����
		db.execSQL(String.format(	"DELETE FROM sms " + 
									"WHERE _id = %d;", idToDelete));
	}
	

	public Cursor GetListOfSendedCallHistory(String myNumber)
	{
		//SQL �ǹ�: 1)�� ����� 2)�� ����� UNION(����)���� ��������
		//���� �̺��� �� ������ ����� �ְ�����... �׳� �̴�� ������ �սô�.
		Cursor cursor = db.rawQuery(
				//1) callhistory table�� �� ��ҵ� �� �ش� ����� r_number�� people table�� � ����� number�� ������ �ش� ����� s_number�� myNumber�� ���� ��� ��ҿ� ����,
				//   �� ����� _id��, people table���� �˻��� ���� �� name(�̸� nameOrNumber�� ���), �׸��� duration�� ��������
				"SELECT callhistory._id AS _id, people.name AS nameOrNumber, duration " +
				"FROM callhistory INNER JOIN people ON people.number = callhistory.r_number " +
				"WHERE s_number = ?" +
				"UNION " +
				//2) callhistory table�� �� ��ҵ� �� �ش� ����� s_number�� myNumber�� ���� �ش� ����� _id�� 3) ��Ͽ� ���Ե��� ���� ��� ��ҿ� ����,
				//   �� ����� _id��, r_number(�̸� receiver�� ���), �׸��� duration�� ��������
				"SELECT _id, r_number AS nameOrNumber, duration " +
				"FROM callhistory " +
				"WHERE s_number = ? AND _id NOT IN " +
					//3) callhistory table�� �� ��ҵ� �� �ش� ����� r_number�� people table�� � ����� number�� ������ �ش� ����� s_number�� myNumber�� ���� ��� ��ҿ� ����,
					//   �� ����� _id�� ��������
					"(SELECT callhistory._id AS _id " +
					"FROM callhistory INNER JOIN people ON people.number = callhistory.r_number " +
					"WHERE s_number = ?)", new String[] { myNumber, myNumber, myNumber });
		return cursor;
	}
	
	public Cursor GetListOfReceivedCallHistory(String myNumber)
	{
		//SQL �ǹ�: 1)�� ����� 2)�� ����� UNION(����)���� ��������
		//���� �̺��� �� ������ ����� �ְ�����... �׳� �̴�� ������ �սô�.
		Cursor cursor = db.rawQuery(
				//1) sms table�� �� ��ҵ� �� �ش� ����� s_number�� people table�� � ����� number�� ������ �ش� ����� r_number�� myNumber�� ���� ��� ��ҿ� ����,
				//   �� ����� _id��, people table���� �˻��� ���� �� name(�̸� nameOrNumber�� ���), �׸��� message�� ��������
				"SELECT callhistory._id AS _id, people.name AS nameOrNumber, duration " +
				"FROM callhistory INNER JOIN people ON people.number = callhistory.s_number " +
				"WHERE r_number = ?" +
				"UNION " +
				//2) callhistory table�� �� ��ҵ� �� �ش� ����� r_number�� myNumber�� ���� �ش� ����� _id�� 3) ��Ͽ� ���Ե��� ���� ��� ��ҿ� ����,
				//   �� ����� _id��, s_number(�̸� nameOrNumber�� ���), �׸��� duration�� ��������
				"SELECT _id, s_number AS nameOrNumber, duration " +
				"FROM callhistory " +
				"WHERE r_number = ? AND _id NOT IN " +
					//3) callhistory table�� �� ��ҵ� �� �ش� ����� s_number�� people table�� � ����� number�� ������ �ش� ����� r_number�� myNumber�� ���� ��� ��ҿ� ����,
					//   �� ����� _id�� ��������
					"(SELECT callhistory._id AS _id " +
					"FROM callhistory INNER JOIN people ON people.number = callhistory.s_number " +
					"WHERE r_number = ?)", new String[] { myNumber, myNumber, myNumber });
		return cursor;
	}
	
	public void InsertCallHistory(String s_number, String r_number, String duration)
	{
		//SQL �ǹ�: sms table�� s_number, r_number, message�� ���� �� ������ �߰�
		db.execSQL(String.format(	"INSERT INTO callhistory " + 
									"VALUES (null, '%s', '%s', '%s');", s_number, r_number, duration));
	}
	
	public void DeleteCallHistory(long idToDelete)
	{
		//SQL �ǹ�: sms table���� _id�� idToDelete�� ��Ҹ� ����
		db.execSQL(String.format(	"DELETE FROM callhistory " + 
									"WHERE _id = %d;", idToDelete));
	}
	
	
	public void Reset()
	{
		//DBHelper class�� ���ǵ� '��� ���̺� ���� �� �����' �޼��� ����
		helper.onUpgrade(db, db.getVersion(), db.getVersion());
	}
}
