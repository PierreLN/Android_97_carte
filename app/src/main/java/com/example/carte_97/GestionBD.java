package com.example.carte_97;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Vector;

public class  GestionBD extends SQLiteOpenHelper {

    private static GestionBD instance;
    private SQLiteDatabase database;

    public static GestionBD getInstance(Context context) {
        if (instance == null) {
            instance = new GestionBD(context.getApplicationContext());
        }
        return instance;
    }

    public GestionBD(@Nullable Context context) {
        super(context, "db", null, 1);
        ouvrirBD();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE score (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " point INTEGER); ");

        ajouterScore(new Score( 50), db);
        ajouterScore(new Score( 2120), db);
        ajouterScore(new Score( 12345), db);
    }

    public void ajouterScore(Score score, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put("point", score.getPointage());

        db.insert("score",null, cv);
    }

    public Vector<String> retournerScore() {
        Vector<String> reponse = new Vector<>();

        Cursor cursor = database.rawQuery("SELECT '#' || _id || ' partie - ' || point || ' Points' FROM score ORDER BY point DESC LIMIT 10", null, null);
        while (cursor.moveToNext()) {
            reponse.add(cursor.getString(0));
        }
        return reponse;
    }

    public int retourneMeilleur() {
        Cursor c = database.rawQuery("SELECT point FROM score ORDER BY point DESC LIMIT 1", null, null);
        c.moveToNext();
        return c.getInt(0);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS score");
        onCreate(db);
    }

    public void ouvrirBD() {
        database = this.getWritableDatabase();
    }

    public void fermerBD() {
        database.close();
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

}

