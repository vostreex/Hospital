package com.example.hospital;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper_Patients extends SQLiteOpenHelper {
    private static int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "hospital_patients";
    public static final String TABLE_Patients = "Patients";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_Gender = "gender";
    public static final String KEY_YEAR_OF_BIRTH = "year_of_birth";
    public static final String KEY_Diagnosis = "diagnosis";
    public static final String KEY_Treatment_plan = "treatment_plan";
    public static final String KEY_Comment = "comment";

    private Context context;

    public DBhelper_Patients(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
        this.context = context;
    }
    public void updatePatientId(int oldId, int newId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, newId);
        db.update(TABLE_Patients, values, KEY_ID + "=?", new String[]{String.valueOf(oldId)});
    }

    public void insertPatient(String name, String gender, String birth, String diagnosis, String plan, String comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_Gender, gender);
        values.put(KEY_YEAR_OF_BIRTH, birth);
        values.put(KEY_Diagnosis, diagnosis);
        values.put(KEY_Treatment_plan, plan);
        values.put(KEY_Comment, comment);
        db.insert(TABLE_Patients, null, values);
        db.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_Patients + " (" + KEY_ID
                + " integer primary key, " + KEY_NAME + " text, " +KEY_Gender + " text, " + KEY_YEAR_OF_BIRTH + " text, " +KEY_Diagnosis + " text, " + KEY_Treatment_plan+ " text, " + KEY_Comment + " text)");

        // Вставка записей о пациентах, если они ещё не существуют
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_Patients + " (" + KEY_NAME + ", " + KEY_Gender + ", " + KEY_YEAR_OF_BIRTH + ", " + KEY_Diagnosis + ", " + KEY_Treatment_plan + ", " + KEY_Comment + ") VALUES ('Пациент 1', 'М', '28.04.2004', 'Перелом правой руки', 'Rest and drink fluids', 'test com')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_Patients + " (" + KEY_NAME + ", " + KEY_Gender + ", " + KEY_YEAR_OF_BIRTH + ", " + KEY_Diagnosis + ", " + KEY_Treatment_plan + ", " + KEY_Comment + ") VALUES ('Пациент 2', 'Ж', '21.09.1997', 'Ушиб ноги', 'Bed rest and antiviral medication', 'апы')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_Patients + " (" + KEY_NAME + ", " + KEY_Gender + ", " + KEY_YEAR_OF_BIRTH + ", " + KEY_Diagnosis + ", " + KEY_Treatment_plan + ", " + KEY_Comment + ") VALUES ('Пациент 3', 'М', '28.12.2005', 'Температура 40', 'Cast and physical therapy', 'пы')");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // You can add code here to handle database upgrades
        // For example, you could drop the old table and create a new one
    }
}
