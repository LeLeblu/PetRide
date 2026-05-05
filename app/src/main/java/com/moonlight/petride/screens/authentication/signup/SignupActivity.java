package com.moonlight.petride.screens.authentication.signup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.moonlight.petride.R;
import com.moonlight.petride.screens.authentication.login.LoginActivity;

/**
 * SignupActivity: Refactorizada para eliminar persistencia.
 * Permite simular el registro de un usuario.
 */
public class SignupActivity extends AppCompatActivity {

    private EditText etNombre, etTelefono, etCorreo, etPassword, etConfirmPassword;
    private Button btnRegistrar;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        vincularVistas();

        // 2. Configurar el evento clic del botón de registro
        btnRegistrar.setOnClickListener(v -> {
            registrarUsuarioSimulado();
        });

        // Configurar el texto para ir al Login
        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void vincularVistas() {
        etNombre = findViewById(R.id.etNombre);
        etTelefono = findViewById(R.id.etTelefono);
        etCorreo = findViewById(R.id.etCorreo);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        tvLogin = findViewById(R.id.tvLogin);
    }

    private void registrarUsuarioSimulado() {
        String nombre = etNombre.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (nombre.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Éxito simulado
        Toast.makeText(this, "¡Registro exitoso! (Modo Diseño)", Toast.LENGTH_LONG).show();
        
        // Redirigir al Login
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
