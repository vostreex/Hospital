package com.example.hospital;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Empl_inf_admin extends Fragment {

    private EditText nameEditText;
    private EditText passwordEditText;
    private EditText rootEditText;
    private Button saveButton;
    DBhelper dbHelper;
    SQLiteDatabase database;

    private void updateEmployee() {
        String name = nameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String root = rootEditText.getText().toString();
        if (name.isEmpty() || password.isEmpty() || root.isEmpty() ) {
            Toast.makeText(getActivity(), "Пожалуйста, заполните все обязательные поля", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!root.equals("Intern") && !root.equals("Doctor") && !root.equals("Admin")){
            Toast.makeText(getActivity(), "Пожалуйста, введите корректные права доступа", Toast.LENGTH_SHORT).show();
            return;
        }
        else {

            // Обновляем данные в БД
            dbHelper = new DBhelper(getActivity());
            database = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DBhelper.KEY_NAME, name);
            try {
                values.put(DBhelper.KEY_PASSWORD, dbHelper.encrypt(password));
            } catch (Exception e) {
                e.printStackTrace();
            }
            values.put(DBhelper.KEY_ROOT, root);

            int selectedId = getArguments().getInt("emplId");
            database.update(DBhelper.TABLE_INTERNS, values, DBhelper.KEY_ID + "=?", new String[]{String.valueOf(selectedId)});
            // Показываем Toast с сообщением об обновлении данных
            Toast.makeText(getActivity(), "Данные обновлены", Toast.LENGTH_SHORT).show();

            // Закрываем фрагмент
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empl_inf_admin, container, false);

        nameEditText = view.findViewById(R.id.FIO);
        passwordEditText = view.findViewById(R.id.Password);
        rootEditText = view.findViewById(R.id.root);
        saveButton = view.findViewById(R.id.AddPatient);

        // Инициализируем dbHelper
        dbHelper = new DBhelper(getActivity());

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Обновляем данные в БД
                updateEmployee();

            }
        });

        // Заполняем EditText переданными данными
        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.getString("name");
            String password = bundle.getString("password");
            password.toString();
            try {
                passwordEditText.setText(dbHelper.decrypt(password));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            String root = bundle.getString("root");

            nameEditText.setText(name);
            rootEditText.setText(root);
        }

        return view;
    }


}