package com.moonlight.petride.screens.authentication.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.moonlight.petride.R;
import com.moonlight.petride.data.UserDAO;
import com.moonlight.petride.screens.authentication.signup.SignupActivity;
import com.moonlight.petride.screens.home.HomeActivity;

public class LoginActivity extends AppCompatActivity {

    // Variables para los componentes de la interfaz
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvSignup;

    // Objeto DAO para validar con la base de datos
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Vincular los componentes Java con los del XML
        etEmail = findViewById(R.id.etEmailLogin);
        etPassword = findViewById(R.id.etPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignup = findViewById(R.id.tvSignup);

        // Inicializar el DAO
        userDAO = new UserDAO(this);

        // Configurar el evento clic del botón de Login
        btnLogin.setOnClickListener(v -> {
            iniciarSesion();
        });

        // Configurar el texto para ir a la pantalla de Registro
        tvSignup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    private void iniciarSesion() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validación básica
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa tu correo y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar credenciales en la DB
        boolean isSuccess = userDAO.checkUserCredentials(email, password);

        if (isSuccess) {
            Toast.makeText(this, "¡Bienvenido a PetRide!", Toast.LENGTH_SHORT).show();
            
            // --- Lógica de Navegación y Backstack ---
            
            // Creamos un Intent para pasar del Login al Home (Pantalla principal)
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            
            // El método finish() destruye la actividad actual (LoginActivity).
            // Al hacer esto, la eliminamos de la "pila" de actividades (backstack).
            // Esto garantiza que si el usuario presiona el botón "Atrás", 
            // no regresará al Login, sino que saldrá de la aplicación.
            finish(); 
        } else {
            Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_LONG).show();
        }
    }
}
