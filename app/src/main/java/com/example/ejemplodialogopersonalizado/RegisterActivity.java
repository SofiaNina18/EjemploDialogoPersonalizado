package com.example.ejemplodialogopersonalizado;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;

import com.example.ejemplodialogopersonalizado.adaptadores.UsuariosAdapter;
import com.example.ejemplodialogopersonalizado.bbdd.AppDatabase;
import com.example.ejemplodialogopersonalizado.bbdd.AppExecutors;
import com.example.ejemplodialogopersonalizado.model.Usuario;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private ListView lvUsers;
    private Button btnRegistrarNuevo, btnUpdateUsuario;
    private EditText etNombre;
    private EditText etPassword;
    private EditText etRePassword;

    private AppDatabase mDb;
    private UsuariosAdapter usuariosAdapter;
    private int idUsuario = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //INSTANCIAMOS LA BD
        mDb = AppDatabase.getInstance(getApplicationContext());
        //CREAMOS UNA NOTIFICACION PARA COMPROBAR LA CREACION DE LA BASE DE DATOS
        Toast.makeText(this, "Base de datos preparada", Toast.LENGTH_SHORT).show();
        //INSTANCIAMOS TODAS LOS COMPONENTES DE MI VISTA (textView, EditText, button. etc...
        lvUsers=findViewById(R.id.lvUsers);
        lvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Usuario usuario = usuariosAdapter.getItem(position);
                idUsuario= usuario.getId();
                etNombre.setText(usuario.getUsuario());
                etPassword.setText(usuario.getPassword());
                etRePassword.setText(usuario.getPassword());
            }
        });
        lvUsers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int idEliminar = usuariosAdapter.getItem(position).getId();
                AlertDialog.Builder alerta = new AlertDialog.Builder(RegisterActivity.this);
                alerta.setTitle("Advertencia");
                alerta.setMessage("¿Estas seguro de que quieres eliminar este usuario");
                alerta.setPositiveButton("si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eliminar(idEliminar);
                    }
                });
                alerta.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "No se ha eliminado", Toast.LENGTH_SHORT).show();
                    }
                });
                alerta.show();
                return true;
            }
        });

        etNombre = findViewById(R.id.etRegistroUser);
        etPassword = findViewById(R.id.etRegistroPassword);
        etRePassword = findViewById(R.id.etRegistroRePassword);
        btnRegistrarNuevo= findViewById(R.id.btnNuevoUsuario);

        //CREAMOS EL ADAPTADOR Y SE LO ASIGNAMOS AL LISTVIEW
        this.usuariosAdapter = new UsuariosAdapter(getApplicationContext(), 1);
        lvUsers.setAdapter(this.usuariosAdapter);
        btnRegistrarNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = etNombre.getText().toString();
                String password = etPassword.getText().toString();
                guardarUsuario(user,password);
            }


        });
        btnUpdateUsuario = findViewById(R.id.btnUpdateUsuario);
        btnUpdateUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = etNombre.getText().toString();
                String password = etPassword.getText().toString();
                actualizar(idUsuario, user, password);
            }
        });

        etRePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String password = etPassword.getText().toString();
                String repassword = etRePassword.getText().toString();

                if (!password.equals(repassword)) {
                    btnRegistrarNuevo.setEnabled(false);
                    etRePassword.setBackgroundColor(Color.RED);
                }else {
                    btnRegistrarNuevo.setEnabled(true);
                    etRePassword.setBackgroundColor(Color.WHITE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
        consultarUsuarios();




    }

    private void actualizar(int idUsuario, String user, String password) {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                Usuario usu = mDb.usuariosDao().loadUsuarioById(idUsuario);
                if (usu != null) {
                    usu.setUsuario(user);
                    usu.setPassword(password);
                    mDb.usuariosDao().updateUsuario(usu);
                }
            }
        });
    }

    //CONSULTAR USUARIOS RELLENA EL ARRAY DE USUARIOS Y LOS CARGA EN EL ADAPTADOR
    private void consultarUsuarios() {
        mDb.usuariosDao().loadAllUsuarios().observe(this, new Observer<List<Usuario>>() {
            @Override
            public void onChanged(List<Usuario> usuarios) {
                usuariosAdapter.setmUsuarioList(usuarios);
            }
        });

    }

    //GUARDAR USUARIO GUARDA EL NOMBRE Y LAS PASSWORD EN LA BASE DE DATOS
    private void guardarUsuario(String user, String password)
    {

        final Usuario usuario = new Usuario(user,password);
        //guardamos el nuevo usuario
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                Log.d("ander", "llega");
                mDb.usuariosDao().insertUsuario(usuario);
            }
        });
    }
    private void eliminar(final int idEliminar)
    {
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                Usuario usu = mDb.usuariosDao().loadUsuarioById(idEliminar);
                if (usu != null) {
                    mDb.usuariosDao().delete(usu);
                }
            }
        });
    }

}








































