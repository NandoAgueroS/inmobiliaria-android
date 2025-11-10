package com.example.inmobiliarialab3.ui.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliarialab3.MainActivity;
import com.example.inmobiliarialab3.R;
import com.example.inmobiliarialab3.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private LoginActivityViewModel mv;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mv = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(LoginActivityViewModel.class);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        EdgeToEdge.enable(this);

        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pedirPermisos();

        mv.getmLogin().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mv.guardarEmail(binding.etUsuario.getText().toString());
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        mv.getmMensaje().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.spinKit.setVisibility(View.GONE);
                binding.tvError.setText(s);
            }
        });

        binding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvError.setText("");
                binding.spinKit.setVisibility(View.VISIBLE);
                String usuario = binding.etUsuario.getText().toString();
                String contrasenia = binding.etContrasenia.getText().toString();
                mv.login(usuario, contrasenia);
            }
        });
        mv.getmEmailGuardado().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.etUsuario.setText(s);
            }
        });
        mv.getmSesionInvalida().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.tvError.setText("Su sesión expiró, ingrese nuevamente");
            }
        });
        mv.verificarSesionExpirada(getIntent());
        mv.recuperarEmail();
        mv.activarLecturas();
    }

    public void pedirPermisos(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},1000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mv.desactivarLecturas();
    }
}