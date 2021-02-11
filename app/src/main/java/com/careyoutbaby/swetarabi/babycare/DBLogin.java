package com.careyoutbaby.swetarabi.babycare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBLogin extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SQLiteBabyCareLogin.db";
    private static final int DATABASE_VERSION = 1;

    public static final String LOGIN_TABLE_NAME = "loginTable";
    public static final String USER_ID = "_id";
    public static final String USER_GOOGLE_ID = "userid";
    public static final String USER_NAME ="username";
    public static final String USER_EMAIL ="useremail";

    public DBLogin(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + LOGIN_TABLE_NAME +
                        "(" + USER_ID + " INTEGER PRIMARY KEY, "+
                        USER_GOOGLE_ID +" TEXT, "+
                        USER_NAME + " TEXT, " +
                        USER_EMAIL + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + LOGIN_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertNewUser(String GoogleId, String name, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(USER_GOOGLE_ID, GoogleId);
        contentValues.put(USER_NAME, name);
        contentValues.put(USER_EMAIL, email);

        db.insert(LOGIN_TABLE_NAME, null, contentValues);
        return true;
    }

    public int numberRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, LOGIN_TABLE_NAME);
        return numRows;
    }

    public boolean updateNewUser(Integer id, String GoogleId, String name, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_GOOGLE_ID, GoogleId);
        contentValues.put(USER_NAME, name);
        contentValues.put(USER_EMAIL,email);
        db.update(LOGIN_TABLE_NAME, contentValues, USER_ID + " = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public void allDeleteUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ LOGIN_TABLE_NAME);
    }

    public Cursor getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM " + LOGIN_TABLE_NAME + " WHERE " +
                USER_ID + "=?", new String[]{Integer.toString(id)});
        return res;
    }

    public Cursor getAllUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + LOGIN_TABLE_NAME, null );
        return res;
    }

   /* public String getLocationString(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String res;
        String query = "SELECT BOUNDARY_COLUMN_LATLNG FROM " + BOUNDARY_TABLE_NAME + " WHERE " + BOUNDARY_COLUMN_ID + "=?", new String[] { Integer.toString(id) };
        res = String.valueOf(db.rawQuery(query),0);
        return res;
    }*/


    public ArrayList<String> getAlluserName() {

        ArrayList<String> arrayList=new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM  "+LOGIN_TABLE_NAME, null );
        res.moveToFirst();

        if (res != null)
        {
            while(!res.isAfterLast()){
                arrayList.add(res.getString(res.getColumnIndex(USER_NAME)));
                //Log.d("emailssinlisttt",arrayList.toString());
                res.moveToNext();
            }}
        return arrayList;

    }
}
