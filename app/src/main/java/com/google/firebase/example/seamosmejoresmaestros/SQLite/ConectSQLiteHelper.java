package com.google.firebase.example.seamosmejoresmaestros.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConectSQLiteHelper extends SQLiteOpenHelper {
    public ConectSQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE mesExp(Semana integer primary key autoincrement, Fecha text, Lector text, Encargado1 text, Ayudante1 text, Encargado2 text, Ayudante2 text, Encargado3 text, Ayudante3 text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS mesExp");
        onCreate(db);
    }
}
