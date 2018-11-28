package oodp.termproject3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Android app에서 SQLite 데이터베이스를 좀 더 쉽게 사용하기 위해 구현하는 보조 클래스입니다.
 * 이 클래스에서 눈여겨 볼 곳은 onCreate()와 onUpgrade()입니다.
 * 
 * @author Racin
 *
 */
public class DBHelper extends SQLiteOpenHelper
{
	public DBHelper(Context context)
	{
		/*
		 * SQLite는 하나의 파일을 사용하여 정보를 저장합니다.
		 * 파일 이름을 바꾸고 싶은 경우 아래의 문자열을 수정하세요.
		 * 물론, 여러분이 저 파일을 직접 건드릴 일은 거의 없을 것입니다. 
		 */
		super(context, "DB.db", null, 1);
	}

	/**
	 * 맨 처음 app을 실행시키고 맨 처음 DB를 만들 때 자동으로 호출되는 메서드입니다.
	 * 여기서는 주로 '새 테이블 만들기' 작업을 수행하며
	 * 이 작업은 SQL의 CREATE TABLE을 통해 이루어집니다.
	 */
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		/*
		 * 아래 내용이 무엇을 의미하는지는 이해하기 어렵지만
		 * 여러분이 테이블의 요소를 입맛에 맞게 다루는 것(새 필드 추가 등)은 쉬운 편입니다.
		 * 
		 * '해당 전화번호로 저장된 사람의 이름 가져오기'와 같은 기능은
		 * 일단 TP#3 막바지 부분으로 미루어 둡시다.
		 * 
		 * 그리고, _id의 존재는 일단 주소록 프로그램에서는 그리 신경쓰지 않아도 됩니다.
		 */
		db.execSQL(
				"CREATE TABLE people ( " +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"name TEXT, " +
				"number TEXT );");

		db.execSQL(
				"CREATE TABLE sms ( " +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"s_number TEXT, " +
				"r_number TEXT, " +
				"message TEXT );");

		db.execSQL(
				"CREATE TABLE callhistory ( " +
				"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"s_number TEXT, " +
				"r_number TEXT, " +
				"duration TEXT );");
	}

	/**
	 * 테스트 등의 이유로 DB의 내용을 바꾸기 위해 직접 호출할 메서드입니다.
	 * 만약 onCreate()에서 새 테이블을 만들었거나 테이블의 이름을 바꾼 경우
	 * 이 메서드의 내용도 그에 맞게 변경해 주세요.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// 여기서는 '업그레이드'라 칭하고 그냥 '다 지우고 다시 만들기'를 수행하고 있습니다.
		db.execSQL("DROP TABLE IF EXISTS people");
		db.execSQL("DROP TABLE IF EXISTS sms");
		db.execSQL("DROP TABLE IF EXISTS callhistory");
		onCreate(db);
	}

}
