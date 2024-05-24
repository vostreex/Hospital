package com.example.hospital;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Admin_panel extends AppCompatActivity implements OnPatientAddedListener{

    ArrayList<Integer> selected_empl= new ArrayList<>();

    Button addBtn,deleteBtn;

    DBhelper dBhelper;
    SQLiteDatabase database;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        dBhelper = new DBhelper(this);
        database = dBhelper.getWritableDatabase();
        listView = findViewById(R.id.list_empl);
        deleteBtn = findViewById(R.id.delete_empl);
        addBtn = findViewById(R.id.add_empl);

        loadEmplList();

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создание строителя диалоговых окон
                AlertDialog.Builder builder = new AlertDialog.Builder(Admin_panel.this);
                // Установка заголовка и сообщения диалогового окна
                builder.setTitle("Подтверждение");
                builder.setMessage("Вы уверены, что хотите удалить выбранных Пользователей?");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteSelectedEmpl();
                    }
                });
                builder.setNegativeButton("Отмена", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Обработка отмены действия
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_empl_admin fragment = new add_empl_admin();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        // Обработка нажатия на элемент в ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранного сотрудника
                int selectedId = (int) parent.getItemIdAtPosition(position)+1;
                selected_empl.add(selectedId);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранного пациента
                int selectedId = (int) parent.getItemIdAtPosition(position)+1;

                Cursor cursor = database.query(DBhelper.TABLE_INTERNS, null, DBhelper.KEY_ID + "=?", new String[]{String.valueOf(selectedId)}, null, null, null);
                if (cursor.moveToFirst()) {
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DBhelper.KEY_NAME));
                    @SuppressLint("Range") String pass = cursor.getString(cursor.getColumnIndex(DBhelper.KEY_PASSWORD));
                    @SuppressLint("Range") String root = cursor.getString(cursor.getColumnIndex(DBhelper.KEY_ROOT));

                    // Открываем фрагмент с информацией о пациенте
                    Empl_inf_admin fragment = new Empl_inf_admin();
                    Bundle bundle = new Bundle();
                    bundle.putString("name", name);
                    bundle.putString("password", pass);
                    bundle.putString("root", root);
                    bundle.putInt("emplId", selectedId);
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment)
                            .addToBackStack(null)
                            .commit();

                }
                cursor.close();
                return true;
            }
        });
    }

    private void deleteSelectedEmpl() {
        for (Integer emplId : selected_empl) {
            database.delete(DBhelper.TABLE_INTERNS, DBhelper.KEY_ID + "=?", new String[]{String.valueOf(emplId)});
            // Обновляем id элементов, следующих за удаленным
            for (int i = emplId + 1; ; i++) {
                Cursor cursor = database.query(DBhelper.TABLE_INTERNS, null, DBhelper.KEY_ID + "=?", new String[]{String.valueOf(i)}, null, null, null);
                if (cursor.getCount() == 0) {
                    break;
                }
                cursor.moveToFirst();
                int newId = i - 1;
                dBhelper.updateEmplId(i, newId);
                cursor.close();
            }
        }
        selected_empl.clear();
        loadEmplList();
        listView.setSelection(0);
    }

    private void loadEmplList() {
        Cursor cursor = database.query(DBhelper.TABLE_INTERNS, null, null, null, null, null, null);
        List<String> emplList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(DBhelper.KEY_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DBhelper.KEY_NAME));
                @SuppressLint("Range") String root = cursor.getString(cursor.getColumnIndex(DBhelper.KEY_ROOT));
                String EmplData = " ID: " + id + " ФИО: " + name + ", Права доступа: " +  root;
                emplList.add(EmplData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, emplList);
        listView.setAdapter(adapter);
    }
    @Override
    public void onPatientAdded() {
        loadEmplList();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (database != null && database.isOpen()) {
            database.close();
        }
    }
}