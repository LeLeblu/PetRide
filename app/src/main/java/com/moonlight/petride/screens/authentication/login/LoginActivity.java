package com.moonlight.petride.screens.authentication.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.moonlight.petride.R;
import com.moonlight.petride.data.SessionDAO;
import com.moonlight.petride.data.UserDAO;
import com.moonlight.petride.screens.authentication.signup.SignupActivity;
import com.moonlight.petride.screens.home.HomeActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvSignup;
    private UserDAO userDAO;
    private SessionDAO sessionDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionDAO = new SessionDAO(this);
        long loggedUserId = sessionDAO.getLoggedUserId();
        if (loggedUserId != -1) {
            irAHome();
            return;
        }

        setContentView(R.layout.activity_login);

        vincularVistas();
        userDAO = new UserDAO(this);

        btnLogin.setOnClickListener(v -> {
            if (!validarCamposLogin()) {
                return;
            }
            iniciarSesion();
        });

        tvSignup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    private void iniciarSesion() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        long userId = userDAO.checkUserCredentials(email, password);
        if (userId != -1) {
            sessionDAO.login(userId);
            Toast.makeText(this, "Inicio de sesion exitoso", Toast.LENGTH_SHORT).show();
            irAHome();
        } else {
            Toast.makeText(this, "Correo o contrasena incorrectos", Toast.LENGTH_LONG).show();
        }
    }

    private void irAHome() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void vincularVistas() {
        etEmail = findViewById(R.id.etEmailLogin);
        etPassword = findViewById(R.id.etPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignup = findViewById(R.id.tvSignup);
    }

    private boolean validarCamposLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("El correo es obligatorio");
            etEmail.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            etPassword.setError("La contraseña es obligatoria");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }
}