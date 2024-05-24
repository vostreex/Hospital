package com.example.hospital;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Patient_info_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Patient_info_fragment extends Fragment{

    private TextView patientInfoTextView,patientDiagnosisTextView,patientPlanTextView,patientRecomendTextView;
    private EditText commentEditText, diagnosEditText, planEditText;
    private Button saveCommentButton,saveDiagnosButton,savePlanButton;
    private DBhelper_Patients dBhelper;
    private SQLiteDatabase database;
    private int patientId;

    public static Patient_info_fragment newInstance(int patientId) {
        Patient_info_fragment fragment = new Patient_info_fragment();
        Bundle args = new Bundle();
        args.putInt("patientId", patientId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            patientId = getArguments().getInt("patientId");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_info_fragment, container, false);



        patientInfoTextView = view.findViewById(R.id.patientInfoTextView);
        commentEditText = view.findViewById(R.id.commentEditText);
        diagnosEditText = view.findViewById(R.id.commentDiagnos);
        planEditText = view.findViewById(R.id.commentPlan);
        saveCommentButton = view.findViewById(R.id.saveCommentButton);
        patientDiagnosisTextView = view.findViewById(R.id.patientDiagnosisTextView);
        patientPlanTextView = view.findViewById(R.id.patientPlanTextView);
        patientRecomendTextView = view.findViewById(R.id.patientRecomendTextView);
        savePlanButton = view.findViewById(R.id.savePlanButton);
        saveDiagnosButton = view.findViewById(R.id.saveDiagnosButton);
        dBhelper = new DBhelper_Patients(getActivity());
        database = dBhelper.getWritableDatabase();

        // Получаем данные о пациенте из аргументов
        Bundle arguments = getArguments();

        if (arguments != null) {
            String name = arguments.getString("name");
            String gender = arguments.getString("gender");
            String birth = arguments.getString("birth");
            String diagnos = arguments.getString("diagnos");
            String plan = arguments.getString("plan");
            String comment = arguments.getString("comment");
            patientId = arguments.getInt("patientId");


            patientInfoTextView.setText("ФИО: " + name + "\nПол: " + gender + "\nДата рождения: " + birth);
            patientDiagnosisTextView.setText("Диагноз: " + diagnos);
            patientPlanTextView.setText("План лечения: " + plan);
            patientRecomendTextView.setText("Рекомендации: " + comment);


            if (arguments.getString("ACCESS").equals("Intern")){
                saveCommentButton.setEnabled(false);
                savePlanButton.setEnabled(false);
                saveDiagnosButton.setEnabled(false);
                saveDiagnosButton.setVisibility(View.GONE);
                savePlanButton.setVisibility(View.GONE);
                saveCommentButton.setVisibility(View.GONE);
                commentEditText.setVisibility(View.GONE);
                diagnosEditText.setVisibility(View.GONE);
                planEditText.setVisibility(View.GONE);
            }
        }

        saveCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newComment = commentEditText.getText().toString();

                // Сохранение нового комментария в базу данных
                ContentValues values = new ContentValues();
                values.put(DBhelper_Patients.KEY_Comment, newComment);
                database.update(DBhelper_Patients.TABLE_Patients, values, DBhelper_Patients.KEY_ID + "=?", new String[]{String.valueOf(patientId)});

                Toast.makeText(getActivity(), "Комментарий сохранен", Toast.LENGTH_SHORT).show();

                // Чтение обновленного комментария из базы данных
                Cursor cursor = database.query(DBhelper_Patients.TABLE_Patients, new String[]{DBhelper_Patients.KEY_Comment}, DBhelper_Patients.KEY_ID + "=?", new String[]{String.valueOf(patientId)}, null, null, null);
                if (cursor.moveToFirst()) {
                    String updatedComment = cursor.getString(0);

                    // Обновление текста комментария в пользовательском интерфейсе
                    commentEditText.setText("");
                    patientRecomendTextView.setText("Рекомендации: " + updatedComment);

                }
                cursor.close();
            }
        });
        saveDiagnosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newDiagnos = diagnosEditText.getText().toString();

                // Сохранение нового диагноза в базу данных
                ContentValues values = new ContentValues();
                values.put(DBhelper_Patients.KEY_Diagnosis, newDiagnos);
                database.update(DBhelper_Patients.TABLE_Patients, values, DBhelper_Patients.KEY_ID + "=?", new String[]{String.valueOf(patientId)});

                Toast.makeText(getActivity(), "Диагноз сохранен", Toast.LENGTH_SHORT).show();

                // Чтение обновленного диагноза из базы данных
                Cursor cursor = database.query(DBhelper_Patients.TABLE_Patients, new String[]{DBhelper_Patients.KEY_Diagnosis}, DBhelper_Patients.KEY_ID + "=?", new String[]{String.valueOf(patientId)}, null, null, null);
                if (cursor.moveToFirst()) {
                    String updatedDiagnos = cursor.getString(0);

                    // Обновление текста диагноза в пользовательском интерфейсе
                    diagnosEditText.setText("");
                    patientDiagnosisTextView.setText("Диагноз: " + updatedDiagnos);
                }
                cursor.close();
            }
        });

        savePlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPlan = planEditText.getText().toString();

                // Сохранение нового плана лечения в базу данных
                ContentValues values = new ContentValues();
                values.put(DBhelper_Patients.KEY_Treatment_plan, newPlan);
                database.update(DBhelper_Patients.TABLE_Patients, values, DBhelper_Patients.KEY_ID + "=?", new String[]{String.valueOf(patientId)});

                Toast.makeText(getActivity(), "План лечения сохранен", Toast.LENGTH_SHORT).show();

                // Чтение обновленного плана лечения из базы данных
                Cursor cursor = database.query(DBhelper_Patients.TABLE_Patients, new String[]{DBhelper_Patients.KEY_Treatment_plan}, DBhelper_Patients.KEY_ID + "=?", new String[]{String.valueOf(patientId)}, null, null, null);
                if (cursor.moveToFirst()) {
                    String updatedPlan = cursor.getString(0);

                    // Обновление текста плана лечения в пользовательском интерфейсе
                    planEditText.setText("");
                    patientPlanTextView.setText("План лечения: " + updatedPlan);
                }
                cursor.close();
            }
        });

        return view;

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        database.close();
    }
}