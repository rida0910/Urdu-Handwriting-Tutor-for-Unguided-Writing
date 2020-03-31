package com.example.datacollection;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import javax.xml.validation.Validator;

public class MyDbHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UrduHandwritingData.db";
    public static final String TABLE_STROKEDATA = "StrokeData";
    public static final String TABLE_USERDATA = "Users";
    public static final String COLUMN_USERID = "UserId";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_AGE = "Age";
    public static final String COLUMN_GENDER = "Gender";
    public static final String COLUMN_PHONENUMBER = "PhoneNumber";
    public static final String COLUMN_BASECITY = "BaseCity";
    public static final String COLUMN_WRITINGHAND = "WritingHand";
    public static final String COLUMN_DOMINANTHAND = "DominantHand";
    public static final String COLUMN_QUALIFICATION = "Qualification";
    public static final String COLUMN_CURRENTLOCATION = "CurrentLocation";
    public static final String COLUMN_STROKEID = "StrokeId";
    public static final String COLUMN_XCOORD = "XCoord";
    public static final String COLUMN_YCOORD = "YCoord";
    public static final  String COLUMN_TIME = "TimeElapsed";
    public static final String COLUMN_CANVASSIZE = "CanvasSize";
    public static final String COLUMN_STOREDCHARACTER = "StoredCHaracter";
    public static final String COLUMN_DEVICE = "DeviceUsed";
    public static final String COLUMN_INPUTTYPE = "InputType";

    public MyDbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String query = "CREATE TABLE " + TABLE_USERDATA + "(" +
                COLUMN_USERID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_AGE + " TEXT NOT NULL, " +
                COLUMN_GENDER + " TEXT NOT NULL, " +
                COLUMN_PHONENUMBER + " TEXT NOT NULL UNIQUE, " +
                COLUMN_BASECITY + " TEXT NOT NULL, " +
                COLUMN_WRITINGHAND + " TEXT NOT NULL, " +
                COLUMN_DOMINANTHAND + " TEXT NOT NULL, " +
                COLUMN_QUALIFICATION + " TEXT NOT NULL, " +
                COLUMN_CURRENTLOCATION + " TEXT NOT NULL " +
                " );";
        sqLiteDatabase.execSQL(query);

        query = "CREATE TABLE " + TABLE_STROKEDATA + "(" +
                COLUMN_STROKEID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_XCOORD + " TEXT NOT NULL, " +
                COLUMN_YCOORD + " TEXT NOT NULL, " +
                COLUMN_TIME + " TEXT NOT NULL, " +
                COLUMN_CANVASSIZE + " TEXT NOT NULL, " +
                COLUMN_USERID + " TEXT NOT NULL, " +
                COLUMN_STOREDCHARACTER + " TEXT NOT NULL, " +
                COLUMN_INPUTTYPE + " TEXT NOT NULL, " +
                COLUMN_DEVICE + " TEXT NOT NULL, " +
                 "FOREIGN KEY(" + COLUMN_USERID + ") REFERENCES " + TABLE_USERDATA + "("+COLUMN_USERID+ "));";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_STROKEDATA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERDATA);
        onCreate(sqLiteDatabase);
    }

    public long addUserData(String Name, String Age, String Gender, String BaseCity, String Phone,
                        String Whand, String Dhand, String Qualification, String CurrLoc) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        long rowid;
        values.put(COLUMN_NAME, Name);
        values.put(COLUMN_AGE, Age);
        values.put(COLUMN_GENDER, Gender);
        values.put(COLUMN_BASECITY, BaseCity);
        values.put(COLUMN_PHONENUMBER, Phone);
        values.put(COLUMN_WRITINGHAND, Whand);
        values.put(COLUMN_DOMINANTHAND, Dhand);
        values.put(COLUMN_QUALIFICATION, Qualification);
        values.put(COLUMN_CURRENTLOCATION, CurrLoc);
        rowid = db.insert(TABLE_USERDATA, null, values);
        db.close();
        return rowid;
    }

    public void addStrokeData(CanvasView cv, int UserId, int Cwidth, int Cheight, String StoredCharacter,
                              String InputType, String Device)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        StringBuilder stringBuilder = new StringBuilder();
        for (float f : cv.getListX()) {
            stringBuilder.append(f);
            stringBuilder.append(",");
        }
        values.put(COLUMN_XCOORD, stringBuilder.toString());

        StringBuilder stringBuilder1 = new StringBuilder();
        for (float f : cv.getListY()) {
            stringBuilder1.append(f);
            stringBuilder1.append(",");
        }
        values.put(COLUMN_YCOORD, stringBuilder1.toString());


        StringBuilder stringBuilder4 = new StringBuilder();
        for (Long f : cv.TimeList) {
            stringBuilder4.append(f);
            stringBuilder4.append(",");
        }
        values.put(COLUMN_TIME, stringBuilder4.toString());

        values.put(COLUMN_USERID, UserId);

        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("(" + Cwidth + "," + Cheight + ")");
        values.put(COLUMN_CANVASSIZE, stringBuilder2.toString());

        values.put(COLUMN_STOREDCHARACTER, StoredCharacter);
        values.put(COLUMN_INPUTTYPE, InputType);
        values.put(COLUMN_DEVICE, Device);
        db.insert(TABLE_STROKEDATA, null, values);
        db.close();
    }

    public void updateStrokeData(CanvasView cv, int StrokeId)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        StringBuilder stringBuilder = new StringBuilder();
        for (float f : cv.getListX()) {
            stringBuilder.append(f);
            stringBuilder.append(",");
        }
        values.put(COLUMN_XCOORD, stringBuilder.toString());

        StringBuilder stringBuilder1 = new StringBuilder();
        for (float f : cv.getListY()) {
            stringBuilder1.append(f);
            stringBuilder1.append(",");
        }
        values.put(COLUMN_YCOORD, stringBuilder1.toString());


        StringBuilder stringBuilder4 = new StringBuilder();
        for (Long f : cv.TimeList) {
            stringBuilder4.append(f);
            stringBuilder4.append(",");
        }
        values.put(COLUMN_TIME, stringBuilder4.toString());
        db.update(TABLE_STROKEDATA, values, "StrokeId="+StrokeId, null);
        db.close();
    }

    public String getStrokeid(int UserId, String StoredCharacter, String InputType)
    {
        SQLiteDatabase db = getWritableDatabase();
        long recc;
        String id=null;

        String query = "SELECT " + COLUMN_STROKEID + " FROM " + TABLE_STROKEDATA +
                " WHERE " + COLUMN_USERID + "=" + UserId + " and " + COLUMN_STOREDCHARACTER + "='" +
                StoredCharacter + "'" +  " and " + COLUMN_INPUTTYPE + "= '" + InputType + "'";

        Cursor mCursor = db.rawQuery(query, null);
        if (mCursor != null && mCursor.moveToFirst())
        {
            mCursor.moveToFirst();
            recc=mCursor.getLong(mCursor.getColumnIndex(COLUMN_STROKEID));
            id=String.valueOf(recc);
        }
        return id;
    }
}
