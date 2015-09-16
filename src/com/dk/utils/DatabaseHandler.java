package com.dk.utils;
import java.util.Vector;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.dk.dao.ItemDataDAO;

public class DatabaseHandler extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "PharmEasyDB";

	// Contacts table name
	private static final String TABLE_DATA = "tblData";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		//3rd argument to be passed is CursorFactory instance
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE = "CREATE TABLE " + TABLE_DATA + "( Id INTEGER PRIMARY KEY, Code TEXT, Name TEXT, Label TEXT, Pack TEXT, Mfby TEXT "
				+ ", MRP TEXT, PackSize TEXT)";
		db.execSQL(CREATE_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);

		// Create tables again
		onCreate(db);
	}

	public void insertData(Vector<ItemDataDAO> vecData) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();

			Log.d("insertData", "Start Inserting Data.");
			if(vecData != null && vecData.size() > 0) {
				String sql = "INSERT OR REPLACE INTO "+ TABLE_DATA +" (Id, Code, Name, Label, Pack, Mfby, MRP, PackSize) values(?,?,?,?,?,?,?,?)";
				SQLiteStatement insert = db.compileStatement(sql);
				db.beginTransaction();

				for(int i = 0; i< vecData.size(); i++){
					ItemDataDAO objData = vecData.elementAt(i);
					insert.clearBindings();
					insert.bindLong(1, objData.getId());
					insert.bindString(2, "" +objData.getCode());
					insert.bindString(3, objData.getName());
					insert.bindString(4, objData.getLabel());
					insert.bindString(5, objData.getPack());
					insert.bindString(6, objData.getMfby());
					insert.bindString(7, objData.getMrp());
					insert.bindString(8, objData.getPackSize());
					insert.execute();
				}

				db.setTransactionSuccessful();
				db.endTransaction();

				Log.d("insertData", "End Inserting Data.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("Error in Insert", e.getMessage());
		}
	}

	public Vector<ItemDataDAO> getAllData()
	{
		Vector<ItemDataDAO> vecData = null;
		try {
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DATA, null);

			if(cursor.moveToFirst()) {
				vecData = new Vector<ItemDataDAO>();

				do {
					ItemDataDAO objData = new ItemDataDAO();
					objData.setId(cursor.getInt(0));
					objData.setCode(Integer.parseInt(cursor.getString(1)));
					objData.setName(cursor.getString(2));
					objData.setLabel(cursor.getString(3));
					objData.setPack(cursor.getString(4));
					objData.setMfby(cursor.getString(5));
					objData.setMrp(cursor.getString(6));
					objData.setPackSize(cursor.getString(7));
					vecData.addElement(objData);
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vecData;
	}
}