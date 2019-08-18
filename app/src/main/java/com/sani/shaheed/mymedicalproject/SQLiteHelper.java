package com.sani.shaheed.mymedicalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "MEDICATION_DB";
    private static final String TB_NAME = "Medicine_TB";
    private static final int DB_VERSION = 1;
    private static final String userTable = "User_TB";
    private SQLiteDatabase db;
    private ContentValues contentValues;

    private static final String create_TB = "CREATE TABLE IF NOT EXISTS " + TB_NAME + " " +
            "(_ID INTEGER primary key autoincrement, medName TEXT not null, " +
            "medDescription TEXT not null, medInterval INTEGER not null," +
            " dosage INTEGER not null, entryDate TEXT not null," +
            " alarmOnOff INTEGER not null, start_date TEXT not null, finish_date TEXT not null)";

    private static final String create_TB_if  = "CREATE TABLE IF NOT EXISTS " + userTable + "" +
            "(_userId INTEGER primary key autoincrement, username TEXT not null, " +
            " password TEXT not null)";

    private static final String drop_TB = "DROP TABLE IF EXISTS " + TB_NAME;

    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_TB);
        db.execSQL(create_TB_if);
    }

    public long insert(String medName, String medDes, int medInterval, String entryDate, int dosage, int alarmOnOff,
                       String start_date, String finish_date){
        db = this.getWritableDatabase();
        db = this.getReadableDatabase();
        contentValues = new ContentValues();
        contentValues.put("medName", medName);
        contentValues.put("medDescription", medDes);
        contentValues.put("medInterval", medInterval);
        contentValues.put("entryDate", entryDate);
        contentValues.put("dosage", dosage);
        contentValues.put("alarmOnOff", alarmOnOff);
        contentValues.put("start_date", start_date);
        contentValues.put("finish_date", finish_date);
        return db.insert(TB_NAME, null, contentValues);
    }

    public boolean deleteEntryById(int id){
        db = this.getWritableDatabase();
        return db.delete(TB_NAME, "_ID = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public Cursor getEntryByName(String text){
        db = this.getReadableDatabase();
        return db.query(TB_NAME, new String[]{"_ID", "medName", "medDescription", "entryDate", "dosage", "alarmOnOff", "start_date", "finish_date"},
                "medName = ?", new String[]{text},
                null, null, "entryDate ASC");
    }

    public Cursor getAllEntries(){
        db = this.getReadableDatabase();
        return db.query(TB_NAME, new String[]{"_ID", "medName", "medDescription", "medInterval", "entryDate", "dosage", "alarmOnOff", "start_date", "finish_date"},
                null, null, null, null, null);
    }

    public Cursor getEntryById(int id){
        db = this.getReadableDatabase();
        return db.query(TB_NAME, new String[]{"_ID", "medName", "medDescription", "medInterval", "entryDate", "dosage", "alarmOnOff", "start_date", "finish_date"},
                "_ID = ?", new String[]{Integer.toString(id)},
                null, null, null);
    }

    public long updateEntry(int id, String medName, String medDes, int medInterval, String entryDate, int dosage,
                            int alarmOnOff, String start_date, String finish_date){
        db = this.getWritableDatabase();
        contentValues = new ContentValues();
        contentValues.put("medName", medName);
        contentValues.put("medDescription", medDes);
        contentValues.put("medInterval", medInterval);
        contentValues.put("entryDate", entryDate);
        contentValues.put("dosage", dosage);
        contentValues.put("alarmOnOff", alarmOnOff);
        contentValues.put("start_date", start_date);
        contentValues.put("finish_date", finish_date);
        return db.update(TB_NAME, contentValues, "_ID = " + id, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(drop_TB);
        onCreate(db);
    }
}
