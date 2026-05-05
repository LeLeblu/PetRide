package com.moonlight.petride.screens.authentication.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.moonlight.petride.R;
import com.moonlight.petride.screens.authentication.signup.SignupActivity;
import com.moonlight.petride.screens.home.HomeActivity;

/**
 * LoginActivity: Refactorizada para eliminar lógica de base de datos.
 * Ahora permite el acceso directo para facilitar pruebas de UI.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Vincular componentes
        etEmail = findViewById(R.id.etEmailLogin);
        etPassword = findViewById(R.id.etPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignup = findViewById(R.id.tvSignup);

        // Configurar el evento clic del botón de Login
        btnLogin.setOnClickListener(v -> {
            iniciarSesionSimulada();
        });

        // Configurar el texto para ir a la pantalla de Registro
        tvSignup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Simula el inicio de sesión sin consultar base de datos.
     */
    private void iniciarSesionSimulada() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validación visual básica
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa tu correo y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        // LÓGICA DE DISEÑO: Aceptamos cualquier credencial para navegar al Home
        Toast.makeText(this, "¡Bienvenido a PetRide! (Modo Diseño)", Toast.LENGTH_SHORT).show();
        irAHome();
    }

    private void irAHome() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
