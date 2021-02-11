package com.careyoutbaby.swetarabi.babycare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SQLiteBabyCare.db";
    private static final int DATABASE_VERSION = 1;


    public static final String BOUNDARY_TABLE_NAME = "boundary";
    public static final String BOUNDARY_COLUMN_ID = "_id";
    public static final String BOUNDARY_COLUMN_NAME = "name";
    public static final String BOUNDARY_COLUMN_ADDRESS = "address";
    public static final String BOUNDARY_COLUMN_LATLNG = "Latlng";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + BOUNDARY_TABLE_NAME +
                        "(" + BOUNDARY_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        BOUNDARY_COLUMN_NAME + " TEXT, " +
                        BOUNDARY_COLUMN_ADDRESS + " TEXT, " +
                        BOUNDARY_COLUMN_LATLNG + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + BOUNDARY_COLUMN_ADDRESS);
        onCreate(db);
    }

    public boolean insertNewLocation(String name, String address, String latlng) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(BOUNDARY_COLUMN_NAME, name);
        contentValues.put(BOUNDARY_COLUMN_ADDRESS, address);
        contentValues.put(BOUNDARY_COLUMN_LATLNG, latlng);

        db.insert(BOUNDARY_TABLE_NAME, null, contentValues);
        return true;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, BOUNDARY_TABLE_NAME);
        return numRows;
    }

    public boolean updateNewLocation(Integer id, String name, String address, String latlng) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOUNDARY_COLUMN_NAME, name);
        contentValues.put(BOUNDARY_COLUMN_ADDRESS, address);
        contentValues.put(BOUNDARY_COLUMN_LATLNG,latlng);
        db.update(BOUNDARY_TABLE_NAME, contentValues, BOUNDARY_COLUMN_ID + " = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public void allDelete() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ BOUNDARY_TABLE_NAME);
    }

    public Cursor getLocation(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("SELECT * FROM " + BOUNDARY_TABLE_NAME + " WHERE " +
                BOUNDARY_COLUMN_ID + "=?", new String[]{Integer.toString(id)});
        return res;
    }

    public Cursor getAllLocation() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM " + BOUNDARY_TABLE_NAME, null );
        return res;
    }

   /* public String getLocationString(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String res;
        String query = "SELECT BOUNDARY_COLUMN_LATLNG FROM " + BOUNDARY_TABLE_NAME + " WHERE " + BOUNDARY_COLUMN_ID + "=?", new String[] { Integer.toString(id) };
        res = String.valueOf(db.rawQuery(query),0);
        return res;
    }*/


    public ArrayList<String> getAllLocationLatLng() {

        ArrayList<String> arrayList=new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT * FROM  "+BOUNDARY_TABLE_NAME, null );
        res.moveToFirst();

        if (res != null)
        {
            while(res.isAfterLast() == false){
                arrayList.add(res.getString(res.getColumnIndex(BOUNDARY_COLUMN_LATLNG)));
                //Log.d("emailssinlisttt",arrayList.toString());
                res.moveToNext();
            }}
        return arrayList;

    }
}
