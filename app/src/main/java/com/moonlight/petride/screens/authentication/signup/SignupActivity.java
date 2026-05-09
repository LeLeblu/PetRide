package com.moonlight.petride.screens.authentication.signup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.moonlight.petride.R;
import com.moonlight.petride.screens.authentication.login.LoginActivity;

public class SignupActivity extends AppCompatActivity {

    private EditText etNombre, etTelefono, etDireccion, etCiudad, etCorreo, etPassword, etConfirmPassword;
    private Button btnRegistrar;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        vincularVistas();

        btnRegistrar.setOnClickListener(v -> {
            if (!validarCamposRegistro()) {
                return;
            }
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void vincularVistas() {
        etNombre = findViewById(R.id.etNombre);
        etTelefono = findViewById(R.id.etTelefono);
        etDireccion = findViewById(R.id.etDireccion);
        etCiudad = findViewById(R.id.etCiudad);
        etCorreo = findViewById(R.id.etCorreo);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        tvLogin = findViewById(R.id.tvLogin);
    }

    private boolean validarCamposRegistro() {
        String nombre = etNombre.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String direccion = etDireccion.getText().toString().trim();
        String ciudad = etCiudad.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (nombre.isEmpty()) {
            etNombre.setError("El nombre completo es obligatorio");
            etNombre.requestFocus();
            return false;
        }

        if (telefono.isEmpty()) {
            etTelefono.setError("El teléfono es obligatorio");
            etTelefono.requestFocus();
            return false;
        }

        if (direccion.isEmpty()) {
            etDireccion.setError("La dirección es obligatoria");
            etDireccion.requestFocus();
            return false;
        }

        if (ciudad.isEmpty()) {
            etCiudad.setError("La ciudad es obligatoria");
            etCiudad.requestFocus();
            return false;
        }

        if (correo.isEmpty()) {
            etCorreo.setError("El correo es obligatorio");
            etCorreo.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            etPassword.setError("La contraseña es obligatoria");
            etPassword.requestFocus();
            return false;
        }

        if (confirmPassword.isEmpty()) {
            etConfirmPassword.setError("Confirma tu contraseña");
            etConfirmPassword.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Las contraseñas no coinciden");
            etConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }
}