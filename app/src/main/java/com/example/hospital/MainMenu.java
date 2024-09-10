package com.example.hospital;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainMenu extends AppCompatActivity implements OnPatientAddedListener{

    ArrayList<Integer> selectedPatients = new ArrayList<>();
    Button deleteButton,addButton;
    public static final String EXTRA_ACCESS_RIGHTS = "access_rights";

    DBhelper_Patients dBhelper;
    SQLiteDatabase database;
    ListView listView;
    PatientAdapter patientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String accessRights = getIntent().getStringExtra(EXTRA_ACCESS_RIGHTS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        dBhelper = new DBhelper_Patients(this);
        database = dBhelper.getWritableDatabase();
        listView = findViewById(R.id.listView);
        deleteButton = findViewById(R.id.deleteBTN);
        addButton = findViewById(R.id.addBTN);

        /*if (accessRights.equals("Intern")){
            deleteButton.setEnabled(false);
            addButton.setEnabled(false);
            deleteButton.setVisibility(View.GONE);
            addButton.setVisibility(View.GONE);
        }
*/
        // Чтение данных из таблицы "Patients"
        loadPatientsList();

        // Обработка нажатия на элемент в ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранного пациента
                int selectedId = (int) view.getTag();
                selectedPatients.add(selectedId);
            }
        });

        EditText searchEditText = findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchText = s.toString().trim();
                if (searchText.isEmpty()) {
                    loadPatientsList();
                } else {
                    searchPatients(""/*searchText*/);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранного пациента
                int selectedId = (int) view.getTag();

                Cursor cursor = database.query(DBhelper_Patients.TABLE_Patients, null, DBhelper_Patients.KEY_ID + "=?", new String[]{String.valueOf(selectedId)}, null, null, null);
                if (cursor.moveToFirst()) {
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DBhelper_Patients.KEY_NAME));
                    @SuppressLint("Range") String gender = cursor.getString(cursor.getColumnIndex(DBhelper_Patients.KEY_Gender));
                    @SuppressLint("Range") String birth = cursor.getString(cursor.getColumnIndex(DBhelper_Patients.KEY_YEAR_OF_BIRTH));
                    @SuppressLint("Range") String diagnos = cursor.getString(cursor.getColumnIndex(DBhelper_Patients.KEY_Diagnosis));
                    @SuppressLint("Range") String plan = cursor.getString(cursor.getColumnIndex(DBhelper_Patients.KEY_Treatment_plan));
                    @SuppressLint("Range") String comment = cursor.getString(cursor.getColumnIndex(DBhelper_Patients.KEY_Comment));

                    // Открываем фрагмент с информацией о пациенте
                    Patient_info_fragment fragment = new Patient_info_fragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("name", name);
                    bundle.putString("gender", gender);
                    bundle.putString("birth", birth);
                    bundle.putString("diagnos", diagnos);
                    bundle.putString("plan", plan);
                    bundle.putString("comment", comment);
                    bundle.putInt("patientId", selectedId);
                    bundle.putString("ACCESS",accessRights);
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

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создание строителя диалоговых окон
                AlertDialog.Builder builder = new AlertDialog.Builder(MainMenu.this);
                // Установка заголовка и сообщения диалогового окна
                builder.setTitle("Подтверждение");
                builder.setMessage("Вы уверены, что хотите удалить выбранных пациентов?");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteSelectedPatients();
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

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ADD_PATIENT fragment = new ADD_PATIENT();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    private void searchPatients(String searchText) {
        Cursor cursor = database.query(DBhelper_Patients.TABLE_Patients, null, DBhelper_Patients.KEY_NAME + " LIKE ?", new String[]{"%" + searchText + "%"}, null, null, null);
        List<Patient> patientsList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(DBhelper_Patients.KEY_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DBhelper_Patients.KEY_NAME));
                @SuppressLint("Range") String gender = cursor.getString(cursor.getColumnIndex(DBhelper_Patients.KEY_Gender));
                @SuppressLint("Range") String birth = cursor.getString(cursor.getColumnIndex(DBhelper_Patients.KEY_YEAR_OF_BIRTH));
                Patient patient = new Patient(id, name, gender, birth);
                patientsList.add(patient);
            } while (cursor.moveToNext());
        }
        cursor.close();
        patientAdapter = new PatientAdapter(this, patientsList);
        listView.setAdapter(patientAdapter);
    }

    private void deleteSelectedPatients() {
        for (Integer patientId : selectedPatients) {
            database.delete(DBhelper_Patients.TABLE_Patients, DBhelper_Patients.KEY_ID + "=?", new String[]{String.valueOf(patientId)});
            // Обновляем id элементов, следующих за удаленным
            for (int i = patientId + 1; ; i++) {
                Cursor cursor = database.query(DBhelper_Patients.TABLE_Patients, null, DBhelper_Patients.KEY_ID + "=?", new String[]{String.valueOf(i)}, null, null, null);
                if (cursor.getCount() == 0) {
                    break;
                }
                cursor.moveToFirst();
                int newId = i - 1;
                dBhelper.updatePatientId(i, newId);
                cursor.close();
            }
        }
        selectedPatients.clear();
        loadPatientsList();
        listView.setSelection(0);
    }

    private void loadPatientsList() {
        Cursor cursor = database.query(DBhelper_Patients.TABLE_Patients, null, null, null, null, null, null);
        List<Patient> patientsList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(DBhelper_Patients.KEY_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DBhelper_Patients.KEY_NAME));
                @SuppressLint("Range") String gender = cursor.getString(cursor.getColumnIndex(DBhelper_Patients.KEY_Gender));
                @SuppressLint("Range") String birth = cursor.getString(cursor.getColumnIndex(DBhelper_Patients.KEY_YEAR_OF_BIRTH));
                Patient patient = new Patient(id, name, gender, birth);
                patientsList.add(patient);
            } while (cursor.moveToNext());
        }
        cursor.close();
        patientAdapter = new PatientAdapter(this, patientsList);
        listView.setAdapter(patientAdapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    class Patient {
        int id;
        String name;
        String gender;
        String birth;

        Patient(int id, String name, String gender, String birth) {
            this.id = id;
            this.name = name;
            this.gender = gender;
            this.birth = birth;
        }
    }

    class PatientAdapter extends ArrayAdapter<Patient> {
        PatientAdapter(MainMenu context, List<Patient> patients) {
            super(context, 0, patients);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = getLayoutInflater().inflate(android.R.layout.simple_list_item_multiple_choice, parent, false);
            }
            Patient patient = getItem(position);
            ((TextView) view.findViewById(android.R.id.text1)).setText(patient.name + ", " + patient.gender + ", " + patient.birth);
            view.setTag(patient.id);
            return view;
        }
    }
    @Override
    public void onPatientAdded() {
        loadPatientsList();
    }
}
