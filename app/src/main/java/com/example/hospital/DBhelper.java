package com.example.hospital;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DBhelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "hospital_employees";
    public static final String TABLE_INTERNS = "employees";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_ROOT = "root";
    private static final String ALGORITHM = "AES";
    private static final byte[] keyValue =
            new byte[] { 'T', 'h', 'i', 's', 'I', 's', 'A', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y' };

    private Context context;

    public DBhelper(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
        this.context = context;
    }

    public void insertEmployee(String name, String password, String root) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        try {
            values.put(KEY_PASSWORD, encrypt(password));
        } catch (Exception e) {
            e.printStackTrace();
        }
        values.put(KEY_ROOT, root);
        db.insert(TABLE_INTERNS, null, values);
    }

    public String getPasswordByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_INTERNS, new String[]{KEY_PASSWORD}, KEY_NAME + "=?", new String[]{name}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        String encryptedPassword = cursor.getString(0);
        cursor.close();
        String password = "";
        try {
            password = decrypt(encryptedPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return password;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_INTERNS + " (" + KEY_ID
                + " integer primary key, " + KEY_NAME + " text, " +KEY_PASSWORD + " text, " + KEY_ROOT+ " text" + ")");

        // Вставка записи по умолчанию, если она ещё не существует
        try {
            db.execSQL("INSERT OR IGNORE INTO " + TABLE_INTERNS + " (" + KEY_NAME + ", " + KEY_PASSWORD + ", " + KEY_ROOT + ") VALUES ('Intern', '" + encrypt("1") + "', 'Intern')");
            db.execSQL("INSERT OR IGNORE INTO " + TABLE_INTERNS + " (" + KEY_NAME + ", " + KEY_PASSWORD + ", " + KEY_ROOT + ") VALUES ('Doctor', '" + encrypt("vas1") + "', 'Doctor')");
            db.execSQL("INSERT OR IGNORE INTO " + TABLE_INTERNS + " (" + KEY_NAME + ", " + KEY_PASSWORD + ", " + KEY_ROOT + ") VALUES ('Admin', '" + encrypt("Vas2") + "', 'Admin')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String encrypt(String data) throws Exception {
        SecretKeySpec key = new SecretKeySpec(keyValue, ALGORITHM);
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encVal);
    }

    public String decrypt(String encryptedData) throws Exception {
        SecretKeySpec key = new SecretKeySpec(keyValue, ALGORITHM);
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = Base64.getDecoder().decode(encryptedData);
        byte[] decValue = c.doFinal(decodedValue);
        return new String(decValue, StandardCharsets.UTF_8);
    }
    public void updateEmplId(int oldId, int newId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, newId);
        db.update(TABLE_INTERNS, values, KEY_ID + "=?", new String[]{String.valueOf(oldId)});
    }

}
