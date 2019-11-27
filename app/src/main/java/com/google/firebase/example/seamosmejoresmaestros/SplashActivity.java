package com.google.firebase.example.seamosmejoresmaestros;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private AutoCompleteTextView emailView;
    private EditText passwordView;
    private ProgressBar progressBarInicio;
    private TextView bienvenidaView;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();

        bienvenidaView = (TextView) findViewById(R.id.textViewBienvenida);
        emailView = (AutoCompleteTextView) findViewById(R.id.email);
        passwordView = (EditText) findViewById(R.id.password);
        progressBarInicio = findViewById(R.id.progressBarInicioSes);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean temaOscuro = sharedPreferences.getBoolean("activarOscuro", false);
        if (temaOscuro) {

            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }

        Button btnIniciar = (Button) findViewById(R.id.sign_in_button);
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarInciarSesion();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(this, SplashActivity.class);
        startActivity(myIntent);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        user = mAuth.getCurrentUser();
        if (user != null) {
            presentacion();
        }

    }

    public void validarInciarSesion() {
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        boolean emailValido;
        boolean passwordValido;

       if (!email.isEmpty()) {
            if (email.contains("@")) {
                emailValido = true;
            } else {
                emailView.setError("Formato incorrecto para correo");
                emailValido = false;
            }
       } else {
           emailView.setError("El campo no puede estar vacío");
           emailValido = false;
       }

       if (password.isEmpty() || (password.length() < 6)) {
           passwordValido = false;
           passwordView.setError("Mínimo 6 caracteres");
       } else {
           passwordValido = true;

       }

       if (passwordValido && emailValido) {
           progressBarInicio.setVisibility(View.VISIBLE);
           mAuth.signInWithEmailAndPassword(email, password)
                   .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()) {
                               Log.d("msg", "signInWithEmail:success");
                               user = mAuth.getCurrentUser();
                               progressBarInicio.setVisibility(View.GONE);
                               presentacion();
                           } else {
                               // If sign in fails, display a message to the user.
                               progressBarInicio.setVisibility(View.GONE);
                               Log.w("msg", "signInWithEmail:failure", task.getException());
                               Toast.makeText(SplashActivity.this, "Error al iniciar sesión\nPor favor, verifique los datos del Usuario y su conexión a internet",
                                       Toast.LENGTH_LONG).show();

                           }

                       }
                   });
       }
    }


    private void presentacion() {
        String nombrePerfil = sharedPreferences.getString("nombrePerfil", "Nombre de Perfil");

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.email_login_form);
        linearLayout.setVisibility(View.GONE);

        String userMail = user.getEmail();

        if (!nombrePerfil.equals("Nombre de Perfil")) {
            if (nombrePerfil.isEmpty()) {
                bienvenidaView.setText("Bienvenido " + userMail);
            } else {
                bienvenidaView.setText("Bienvenido " + nombrePerfil);
            }
        } else {
            bienvenidaView.setText("Bienvenido " + userMail);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3500);
    }
}
