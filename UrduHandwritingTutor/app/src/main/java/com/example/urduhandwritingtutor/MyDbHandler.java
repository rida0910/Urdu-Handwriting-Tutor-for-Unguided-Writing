package com.example.urduhandwritingtutor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MyDbHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UrduHandwritingData.db";
    public static final String TABLE_USERDATA = "Users";
    public static final String COLUMN_USERID = "UserId";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_AGE = "Age";
    public static final String COLUMN_GENDER = "Gender";
    public static final String COLUMN_EMAIL = "EmailAddress";
    public static final String COLUMN_PASSWORD = "Password";
    public static final String COLUMN_WRITINGHAND = "WritingHand";
    public static final String COLUMN_STATE = "State";

    public static final String TABLE_EVALUATIONS = "Evaluations";
    public static final String COLUMN_EVALUATIONID = "Id";
    public static final String COLUMN_CHARACTER = "Character";
    public static final String COLUMN_PERCENTAGECORRECTNESS = "Percentage_Correctness";
    public static final String COLUMN_FEEDBACK = "Feedback";



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
                COLUMN_EMAIL + " TEXT NOT NULL UNIQUE, " +
                COLUMN_PASSWORD + " TEXT NOT NULL, " +
                COLUMN_WRITINGHAND + " TEXT NOT NULL, " +
                COLUMN_STATE + " TEXT NOT NULL " +
                " );";
        sqLiteDatabase.execSQL(query);

        query = "CREATE TABLE " + TABLE_EVALUATIONS + "(" +
                COLUMN_EVALUATIONID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CHARACTER + " TEXT NOT NULL, " +
                COLUMN_PERCENTAGECORRECTNESS + " INTEGER NOT NULL, " +
                COLUMN_FEEDBACK + " TEXT NOT NULL, " +
                COLUMN_USERID + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + COLUMN_USERID + ") REFERENCES " + TABLE_USERDATA + "("+COLUMN_USERID+ "));";

        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERDATA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_EVALUATIONS);
        onCreate(sqLiteDatabase);
    }

    public long addUserData(String Name, String Age, String Gender, String EmailAdress, String Password, String Whand) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        long rowid;
        values.put(COLUMN_NAME, Name);
        values.put(COLUMN_AGE, Age);
        values.put(COLUMN_GENDER, Gender);
        values.put(COLUMN_EMAIL, EmailAdress);
        values.put(COLUMN_PASSWORD, Password);
        values.put(COLUMN_WRITINGHAND, Whand);
        values.put(COLUMN_STATE, "logged_in");
        rowid = db.insert(TABLE_USERDATA, null, values);
        db.close();
        return rowid;
    }

    public String LogInUser(String Email, String Password)
    {
        SQLiteDatabase db = getWritableDatabase();
        long recc;
        String id=null;

        String query = "SELECT UserId FROM Users WHERE EmailAddress = '"+Email+"' and Password = '"+Password+"'";

        Cursor mCursor = db.rawQuery(query, null);
        if (mCursor != null && mCursor.moveToFirst())
        {
            mCursor.moveToFirst();
            recc=mCursor.getLong(mCursor.getColumnIndex(COLUMN_USERID));
            id=String.valueOf(recc);
        }
        if(id != null) {
            query = "UPDATE Users SET State='logged_in' WHERE UserId = '" + Integer.parseInt(id) + "'";
            db.execSQL(query);
        }
        mCursor.close();
        return id;
    }

    public String getLoggedInUser()
    {
        SQLiteDatabase db = getWritableDatabase();
        long recc;
        String id=null;

        String query = "SELECT UserId FROM Users WHERE State='logged_in'";

        Cursor mCursor = db.rawQuery(query, null);
        if (mCursor != null && mCursor.moveToFirst())
        {
            mCursor.moveToFirst();
            recc=mCursor.getLong(mCursor.getColumnIndex(COLUMN_USERID));
            id=String.valueOf(recc);
        }
        mCursor.close();
        return id;
    }

    public String getUserName(String id)
    {
        SQLiteDatabase db = getReadableDatabase();
        long recc;
        String name=null;

        String query = "SELECT Name FROM Users WHERE UserId = "+getLoggedInUser()+"";

        Cursor mCursor = db.rawQuery(query, null);
        if (mCursor != null && mCursor.moveToFirst())
        {
            mCursor.moveToFirst();
            name = mCursor.getString(mCursor.getColumnIndex(COLUMN_NAME));
        }
        mCursor.close();
        return name;
    }

    public String getUserEmail(String id)
    {
        SQLiteDatabase db = getReadableDatabase();
        long recc;
        String email=null;

        String query = "SELECT EmailAddress FROM Users WHERE UserId = "+Integer.parseInt(id)+"";

        Cursor mCursor = db.rawQuery(query, null);
        if (mCursor != null && mCursor.moveToFirst())
        {
            mCursor.moveToFirst();
            email = mCursor.getString(mCursor.getColumnIndex(COLUMN_EMAIL));
        }
        mCursor.close();
        return email;
    }

    public void LogOutUser(String id)
    {
        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE Users SET State='logged_out' WHERE UserId = '"+Integer.parseInt(id)+"'";
        db.execSQL(query);
    }

    public long addevaluation(String Char, int percentage_correctness, String Feedback) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        long rowid;
        values.put(COLUMN_CHARACTER, Char);
        values.put(COLUMN_PERCENTAGECORRECTNESS, percentage_correctness);
        values.put(COLUMN_USERID, getLoggedInUser());
        values.put(COLUMN_FEEDBACK, Feedback);
        rowid = db.insert(TABLE_EVALUATIONS, null, values);
        db.close();
        return rowid;
    }



    public evaluation_class getEvaluations() {
        SQLiteDatabase db = getReadableDatabase();
        List<String> TestChars = new ArrayList<>();
        List<String> Feedback = new ArrayList<>();
        List<Integer> Scores = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM  Evaluations JOIN Users ON Evaluations.UserId = Users.UserId WHERE State = 'logged_in'",null);
        if (cursor.moveToFirst()) {
            do {
                TestChars.add(cursor.getString(cursor.getColumnIndex(COLUMN_CHARACTER)));
                Feedback.add(cursor.getString(cursor.getColumnIndex(COLUMN_FEEDBACK)));
                Scores.add(cursor.getInt(cursor.getColumnIndex(COLUMN_PERCENTAGECORRECTNESS)));
            } while (cursor.moveToNext());
        }

        //code to set adapter to populate list
        cursor.close();
        evaluation_class evaluations = new evaluation_class(TestChars, Scores, Feedback);
        return evaluations;
    }
}
