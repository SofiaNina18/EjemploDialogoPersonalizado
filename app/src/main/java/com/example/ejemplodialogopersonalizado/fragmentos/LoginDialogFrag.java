package com.example.ejemplodialogopersonalizado.fragmentos;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.ejemplodialogopersonalizado.CentralActivity;
import com.example.ejemplodialogopersonalizado.R;

public class LoginDialogFrag extends DialogFragment {

    //ATRIBUTOS D LA CLASE

    private EditText etNombre;

    private EditText etPassword;
    private Button btnAceptar;
    private Button btnCancelar;


    public LoginDialogFrag(){
        super();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstance) {

        Dialog dialog = super.onCreateDialog(savedInstance);
        dialog.setTitle("login");
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialogo_personalizado, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //INSTANCIAMOS LOS EDITTEXT Y LOS BOTONES
        etNombre=view.findViewById(R.id.etUser);
        etPassword=view.findViewById(R.id.etPassword);
        btnAceptar=view.findViewById(R.id.btnAceptar);
        btnCancelar=view.findViewById(R.id.btnCancelar);
        //PROGRAMAMOS LOS BOTONES
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = etNombre.getText().toString();
                String password = etPassword.getText().toString();

                //COMPROBAR SIN BASE DE DATOS
                if (password.equals("Almi123") && nombre.equals("Almi")) {
                    Toast.makeText(getContext(), "Bienvenidos a Almi", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), CentralActivity.class);
                    startActivity(intent);
                } else {
                    dismiss();
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
