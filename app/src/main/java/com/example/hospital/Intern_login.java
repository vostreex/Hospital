package com.example.hospital;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Intern_login extends AppCompatActivity {
    Button login;
    EditText UName, password;
    DBhelper dBhelper;
    SQLiteDatabase database;
    public String access_rights = "";

    public String getAccess_rights(){
        return access_rights;
    }
    int attemptsLeft = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intern_login);

        login = (Button) findViewById(R.id.button_login);
        UName = findViewById(R.id.email_intent_title);
        password = findViewById(R.id.password_title_intent);
        dBhelper = new DBhelper(this);
        database = dBhelper.getWritableDatabase();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAvaid = false;
                if (UName.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Поля не должны быть пустыми", Toast.LENGTH_SHORT).show();
                } else {
                    Cursor cursor = database.query(DBhelper.TABLE_INTERNS, null, null, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        do {
                            int nameIndex = cursor.getColumnIndex(DBhelper.KEY_NAME);
                            int passwordIndex = cursor.getColumnIndex(DBhelper.KEY_PASSWORD);
                            int rootIndex = cursor.getColumnIndex(DBhelper.KEY_ROOT);
                            String name = cursor.getString(nameIndex);
                            String pass = cursor.getString(passwordIndex);
                            String root = cursor.getString(rootIndex);
                            try {
                                if (UName.getText().toString().equals(name) && dBhelper.decrypt(pass).equals(password.getText().toString()) && root.equals("Intern")) {
                                    isAvaid = true;
                                    Intent intent = new Intent(Intern_login.this, MainMenu.class);
                                    intent.putExtra(MainMenu.EXTRA_ACCESS_RIGHTS, "Intern");
                                    startActivity(intent);
                                    cursor.close();
                                    access_rights = "Intern";
                                    break;
                                }
                                if (UName.getText().toString().equals(name) && dBhelper.decrypt(pass).equals(password.getText().toString()) && root.equals("Doctor")) {
                                    isAvaid = true;
                                    Intent intent = new Intent(Intern_login.this, MainMenu.class);
                                    intent.putExtra(MainMenu.EXTRA_ACCESS_RIGHTS, "Doctor");
                                    startActivity(intent);
                                    cursor.close();
                                    access_rights = "Doctor";
                                    break;
                                }
                                if (UName.getText().toString().equals(name) && dBhelper.decrypt(pass).equals(password.getText().toString()) && root.equals("Admin")) {
                                    isAvaid = true;
                                    Intent intent = new Intent(Intern_login.this, Admin_panel.class);
                                    startActivity(intent);
                                    cursor.close();
                                    access_rights = "Admin";
                                    break;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } while (cursor.moveToNext());
                        cursor.close();
                    }
                    if (!isAvaid) {
                        Toast.makeText(getApplicationContext(), "Неверное имя пользователя или пароль. Попробуйте еще раз.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (database != null && database.isOpen()) {
            database.close();
        }
    }
}
