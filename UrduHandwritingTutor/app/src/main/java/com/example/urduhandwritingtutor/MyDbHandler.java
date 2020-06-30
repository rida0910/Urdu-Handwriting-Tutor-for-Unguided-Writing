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
    public static final String COLUMN_EVALUATIONID = "EvaluationId";
    public static final String COLUMN_CHARACTER = "Character";
    public static final String COLUMN_PERCENTAGECORRECTNESS = "Percentage_Correctness";
    public static final String COLUMN_FEEDBACK = "Feedback";

    public static final String TABLE_QUIZRESULTS = "QuizResults";
    public static final String TABLE_QUIZ = "Quizzes";
    public static final String COLUMN_QUIZID = "QuizID";
    public static final String COLUMN_QUIZNUMBER = "QuizNumber";



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

        query = "CREATE TABLE " + TABLE_QUIZ + "(" +
                COLUMN_QUIZID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_QUIZNUMBER + " INTEGER NOT NULL);";
        sqLiteDatabase.execSQL(query);

        query = "CREATE TABLE " + TABLE_QUIZRESULTS + "(" +
                COLUMN_QUIZID + " INTEGER REFERENCES " + TABLE_QUIZ + "(" + COLUMN_QUIZID + ")," +
                COLUMN_EVALUATIONID + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + COLUMN_EVALUATIONID + ") REFERENCES " + TABLE_EVALUATIONS + "("+COLUMN_EVALUATIONID+ "));";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERDATA);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_EVALUATIONS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZRESULTS);
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



    public List<evaluation_class> getEvaluations() {
        SQLiteDatabase db = getReadableDatabase();
        String TestChars = "";
        String Feedback = "";
        int Scores = 0;
        int id;
        List<evaluation_class> evaluations = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM  Evaluations JOIN Users ON Evaluations.UserId = Users.UserId " +
                "WHERE State = 'logged_in' and EvaluationID not in (SELECT EvaluationID FROM QuizResults)",null);
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(COLUMN_EVALUATIONID));
                TestChars = cursor.getString(cursor.getColumnIndex(COLUMN_CHARACTER));
                Feedback = cursor.getString(cursor.getColumnIndex(COLUMN_FEEDBACK));
                Scores = cursor.getInt(cursor.getColumnIndex(COLUMN_PERCENTAGECORRECTNESS));
                evaluation_class evaluation = new evaluation_class(id, TestChars, Scores, Feedback);
                evaluations.add(evaluation);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return evaluations;
    }

    public long addQuiz() {
        SQLiteDatabase db = getReadableDatabase();
        int MaxQuizNumber;
        Cursor cursor = db.rawQuery("SELECT MAX(QuizNumber) FROM  "+ TABLE_QUIZ +" JOIN "+ TABLE_QUIZRESULTS +" ON " +
                "Quizzes.QuizID = QuizResults.QuizID JOIN "+ TABLE_EVALUATIONS +" ON QuizResults.EvaluationID = Evaluations.EvaluationID " +
                "JOIN USERS ON Users.UserId = Evaluations.UserId WHERE State = 'logged_in'",null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                MaxQuizNumber = cursor.getInt(cursor.getColumnIndex("MAX(QuizNumber)"));
            } while (cursor.moveToNext());

        }
        else {
            MaxQuizNumber = 0;
        }
        cursor.close();

        db.close();
        db = getWritableDatabase();
        ContentValues values = new ContentValues();
        long rowid;
        values.put(COLUMN_QUIZNUMBER, MaxQuizNumber + 1);
        rowid = db.insert(TABLE_QUIZ, null, values);
        db.close();
        return rowid;
    }

    public void addQuizResults(long quizID, long evaId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVALUATIONID, evaId);
        values.put(COLUMN_QUIZID, quizID);
        db.insert(TABLE_QUIZRESULTS, null, values);
        db.close();
    }


    public List<Quiz_class> getQuizzes()
    {
        SQLiteDatabase db = getReadableDatabase();
        List<Quiz_class> quizzes = new ArrayList<>();
        List<Integer> QuizIds = new ArrayList<>();
        List<Integer> QuizNumbers = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT Quizzes.QuizId, QuizNumber FROM " + TABLE_QUIZ + " JOIN QuizResults ON Quizzes.QuizId = QuizResults.QuizId " +
                "JOIN Evaluations ON Evaluations.EvaluationId = QuizResults.EvaluationId JOIN Users ON Users.UserId = Evaluations.UserId " +
                "WHERE State = 'logged_in'  GROUP BY Quizzes.QuizID, QuizNumber", null);

        if (cursor.moveToFirst()) {
            do {
                QuizIds.add(cursor.getInt(cursor.getColumnIndex(COLUMN_QUIZID)));
                QuizNumbers.add(cursor.getInt(cursor.getColumnIndex(COLUMN_QUIZNUMBER)));
            } while (cursor.moveToNext());
        }
        cursor.close();

        for (int i = 0; i < QuizIds.size(); i++) {

            cursor = db.rawQuery("SELECT * FROM  Evaluations JOIN QuizResults ON Evaluations.EvaluationId = QuizResults.EvaluationId " +
                    "WHERE QuizId = " + QuizIds.get(i), null);
            List<evaluation_class> evaluations = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    String TestChars = cursor.getString(cursor.getColumnIndex(COLUMN_CHARACTER));
                    String Feedback = cursor.getString(cursor.getColumnIndex(COLUMN_FEEDBACK));
                    int Scores = cursor.getInt(cursor.getColumnIndex(COLUMN_PERCENTAGECORRECTNESS));
                    int id = cursor.getInt(cursor.getColumnIndex(COLUMN_EVALUATIONID));
                    evaluation_class evaluation = new evaluation_class(id, TestChars, Scores, Feedback);
                    evaluations.add(evaluation);

                } while (cursor.moveToNext());
            }
            Quiz_class quiz = new Quiz_class(QuizIds.get(i), QuizNumbers.get(i), evaluations);
            quizzes.add(quiz);
        }
        cursor.close();
        return quizzes;

    }

    public int getQuizNumber(int id)
    {
        int quizNumber = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM  Quizzes  " +
                "WHERE QuizId = " + id, null);
        if (cursor.moveToFirst()) {
            do {
                quizNumber = cursor.getInt(cursor.getColumnIndex(COLUMN_QUIZNUMBER));
            } while (cursor.moveToNext());
        }
        return quizNumber;
    }

    public void deleteEvaluation(long id)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_EVALUATIONS, COLUMN_EVALUATIONID + "=" + id, null);
    }
}
