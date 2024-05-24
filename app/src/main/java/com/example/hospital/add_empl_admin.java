package com.example.hospital;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link add_empl_admin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class add_empl_admin extends Fragment {

    private EditText nameEditText;
    private EditText passwordEditText;
    private EditText rootEditText;
    private Button saveButton;
    DBhelper dbHelper;
    SQLiteDatabase database;

    private OnPatientAddedListener listener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPatientAddedListener) {
            listener = (OnPatientAddedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnPatientAddedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void addEmployee() {
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
            dbHelper.insertEmployee(name, password, root);
            if (listener != null) {
                listener.onPatientAdded();
            }
            // Показываем Toast с сообщением об обновлении данных
            Toast.makeText(getActivity(), "Пользователь добавлен", Toast.LENGTH_SHORT).show();

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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Обновляем данные в БД
                addEmployee();

            }
        });

        return view;
    }
}