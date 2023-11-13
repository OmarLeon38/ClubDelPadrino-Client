package com.upao.cliente.clubdelpadrino_client.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.upao.cliente.clubdelpadrino_client.R;
import com.upao.cliente.clubdelpadrino_client.entity.service.Usuario;
import com.upao.cliente.clubdelpadrino_client.utils.DateSerializer;
import com.upao.cliente.clubdelpadrino_client.utils.TimeSerializer;
import com.upao.cliente.clubdelpadrino_client.viewmodel.UsuarioViewModel;

import java.sql.Date;
import java.sql.Time;

public class MainActivity extends AppCompatActivity {

    private EditText edtMail, edtPassword;

    private Button btnIniciarSesion;

    private UsuarioViewModel viewModel;

    private TextView txtInputUsuario, txtInputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initViewModel();
        this.init();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
    }

    private void init(){
        edtMail = findViewById(R.id.edtMail);
        edtPassword = findViewById(R.id.edtPassword);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnIniciarSesion.setOnClickListener(view -> {
            viewModel.login(edtMail.getText().toString(), edtPassword   .getText().toString()).observe(this, usuarioGenericResponse -> {
                if (usuarioGenericResponse.getRpta() == 1){
                    //Toast.makeText(this, usuarioGenericResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    toastOk(usuarioGenericResponse.getMessage());
                    Usuario u = usuarioGenericResponse.getBody();
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = preferences.edit();
                    final Gson g = new GsonBuilder()
                            .registerTypeAdapter(Date.class, new DateSerializer())
                            .registerTypeAdapter(Time.class, new TimeSerializer())
                            .create();
                    editor.putString("UsuarioJson", g.toJson(u, new TypeToken<Usuario>(){
                    }.getType()));
                    editor.apply();
                    edtMail.setText("");
                    edtPassword.setText("");
                    startActivity(new Intent(this, InicioActivity.class));
                }else{
                    toastNoOk(usuarioGenericResponse.getMessage());
                }
            });
        });
    }

    public void toastOk(String msg){
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_ok, (ViewGroup) findViewById(R.id.ll_custom_toast_ok));
        TextView txtMensaje = view.findViewById(R.id.txtMensajeToastOk);
        txtMensaje.setText(msg);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    public void toastNoOk(String msg){
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_no_ok, (ViewGroup) findViewById(R.id.ll_custom_toast_no_ok));
        TextView txtMensaje = view.findViewById(R.id.txtMensajeToastNoOk);
        txtMensaje.setText(msg);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }
}