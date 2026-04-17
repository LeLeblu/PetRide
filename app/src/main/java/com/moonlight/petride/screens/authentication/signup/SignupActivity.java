package com.moonlight.petride.screens.authentication.signup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.moonlight.petride.R;
import com.moonlight.petride.data.UserDAO;
import com.moonlight.petride.screens.authentication.login.LoginActivity;

public class SignupActivity extends AppCompatActivity {

    // Componentes de la interfaz (Vistas)
    private EditText etNombre, etTelefono, etCorreo, etPassword, etConfirmPassword;
    private Button btnRegistrar;
    private TextView tvLogin;
    
    // Objeto DAO para interactuar con la base de datos
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // 1. Vincular componentes Java con el diseño XML mediante sus IDs
        vincularVistas();

        // Inicializar el DAO
        userDAO = new UserDAO(this);

        // 2. Configurar el evento clic del botón de registro
        btnRegistrar.setOnClickListener(v -> {
            registrarUsuario();
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

    private void registrarUsuario() {
        // Obtener los valores de los campos
        String nombre = etNombre.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // 3. Validaciones básicas de campos vacíos
        if (nombre.isEmpty() || telefono.isEmpty() || correo.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validación de coincidencia de contraseñas
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // 4. Guardar en la base de datos (Usamos valores ficticios para dirección y ciudad si no están en el XML actual)
        long id = userDAO.insertUser(nombre, telefono, "N/A", "N/A", correo, password);

        if (id != -1) {
            // Éxito
            Toast.makeText(this, "¡Registro exitoso! Por favor, inicia sesión", Toast.LENGTH_LONG).show();
            // Redirigir al Login
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Cerrar esta actividad para que no puedan volver con el botón "atrás"
        } else {
            // Error en la inserción
            Toast.makeText(this, "Error al registrar el usuario. Intenta de nuevo.", Toast.LENGTH_SHORT).show();
        }
    }
}
