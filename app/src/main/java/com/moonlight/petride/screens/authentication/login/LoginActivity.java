package com.moonlight.petride.screens.authentication.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.moonlight.petride.MainActivity;
import com.moonlight.petride.R;
import com.moonlight.petride.data.UserDAO;
import com.moonlight.petride.screens.authentication.signup.SignupActivity;

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

        // 1. Vincular los componentes Java con los del XML (IDs definidos en activity_login.xml)
        etEmail = findViewById(R.id.etEmailLogin);
        etPassword = findViewById(R.id.etPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignup = findViewById(R.id.tvSignup);

        // Inicializar el DAO
        userDAO = new UserDAO(this);

        // 2. Configurar el evento clic del botón de Login
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
        // Obtener texto de los campos
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // 3. Validación básica de campos vacíos
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa tu correo y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        // 4. Verificar credenciales usando el UserDAO
        boolean isSuccess = userDAO.checkUserCredentials(email, password);

        if (isSuccess) {
            // Si son correctas, informamos al usuario y vamos a la pantalla principal
            Toast.makeText(this, "¡Bienvenido a PetRide!", Toast.LENGTH_SHORT).show();
            
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Cerramos el Login para que no regrese al presionar "atrás"
        } else {
            // Si fallan, mostramos un mensaje de error
            Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_LONG).show();
        }
    }
}
