package me.nkkumawat.lostify.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import me.nkkumawat.lostify.MainActivity;

/**
 * Created by sonu on 28/6/18.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final String CONTENT_AUTHORITY = "me.nkkumawat.lostfy";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_SMS = "SMSLOST";
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SMS).build();

    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "SMSLOST";
    public static final String SMS_RESPONSE = "SMSRESPONSE";
    public static final String SMS_QUERY = "SMSQUERY";
    String notFound = "notFound";
    // Shops Table Columns names
    public static final String KEY_ID = "_id";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_NUMBER = "number";
    public static final String KEY_METHOD = "method";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE_RESPONSE = "CREATE TABLE " + SMS_RESPONSE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_MESSAGE + " TEXT,"+ KEY_NUMBER + " TEXT," + KEY_METHOD + " TEXT" +")";
        String CREATE_CONTACTS_TABLE_QUERY = "CREATE TABLE " + SMS_QUERY + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_MESSAGE + " TEXT,"+ KEY_NUMBER + " TEXT," + KEY_METHOD + " TEXT" +")";
        db.execSQL(CREATE_CONTACTS_TABLE_RESPONSE);
        db.execSQL(CREATE_CONTACTS_TABLE_QUERY);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SMS_RESPONSE);
        db.execSQL("DROP TABLE IF EXISTS " + SMS_QUERY);
        onCreate(db);
    }
    public void insert(String message  , String number , String method , String tablename) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_MESSAGE, message);
        values.put(KEY_NUMBER, number);
        values.put(KEY_METHOD, method);
        db.insert(tablename, null, values);
        db.close();
//        MainActivity.messageAdapter.changeCursor(getWholeData("SMSQUERY"));
    }
    public Cursor getWholeData(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = new String[]{KEY_ID, KEY_MESSAGE, KEY_NUMBER, KEY_METHOD};
        Cursor cursor = db.query(tableName,columns, null, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return  cursor;
    }
//    public Cursor getMobileWiseData(String FROM) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        String[] columns = new String[]{KEY_ID, KEY_MESSAGE, KEY_USER_FROM , KEY_USER_TO};
//        Cursor cursor = db.query(CHAT_SMS,columns, KEY_USER_FROM + "=? or " + KEY_USER_TO + "=?", new String[]{FROM ,FROM }, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//        return  cursor;
//    }
    //    public String getDataPass(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(WAY_2, new String[]{KEY_ID, KEY_MOB, KEY_PASS}, KEY_ID + "=?",  new String[]{String.valueOf(id)}, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//        else
//            return notFound;
//        return  cursor.getString(2);
//    }
    public int getDatabaseSize(String tableName) {
        String countQuery = "SELECT  * FROM  " + tableName ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }

}
