package com.example.hospital;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

public class ADD_PATIENT extends Fragment {

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

    private TextInputEditText fio, gender, date, diagnos, plan, recomendation;
    private Button addPatientButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a_d_d__p_a_t_i_e_n_t, container, false);

        fio = view.findViewById(R.id.FIO);
        gender = view.findViewById(R.id.GENDER);
        date = view.findViewById(R.id.Date);
        diagnos = view.findViewById(R.id.diagnos);
        plan = view.findViewById(R.id.Plan);
        recomendation = view.findViewById(R.id.Recomendation);
        addPatientButton = view.findViewById(R.id.AddPatient);

        addPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fioText = fio.getText().toString();
                String genderText = gender.getText().toString();
                String dateText = date.getText().toString();
                String diagnosText = diagnos.getText().toString();
                String planText = plan.getText().toString();
                String recomendationText = recomendation.getText().toString();

                if (fioText.isEmpty() || genderText.isEmpty() || dateText.isEmpty() || diagnosText.isEmpty()) {
                    Toast.makeText(getActivity(), "Пожалуйста, заполните все обязательные поля (ФИО, Пол, Дата рождения, Диагноз)", Toast.LENGTH_SHORT).show();
                    return;
                }

                DBhelper_Patients dbHelper = new DBhelper_Patients(getActivity());
                dbHelper.insertPatient(fioText, genderText, dateText, diagnosText, planText, recomendationText);
                if (listener != null) {
                    listener.onPatientAdded();
                }
                // Показываем Toast с сообщением об обновлении данных
                Toast.makeText(getActivity(), "Пациент добавлен", Toast.LENGTH_SHORT).show();

                // Закрываем фрагмент
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

}
