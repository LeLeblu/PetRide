package com.moonlight.petride.screens.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.moonlight.petride.R;
import com.moonlight.petride.data.SessionDAO;
import com.moonlight.petride.screens.authentication.login.LoginActivity;
import com.moonlight.petride.screens.pets.PetsActivity;
import com.moonlight.petride.screens.rides.RidesActivity;

public class HomeActivity extends AppCompatActivity {

    private Button btnMascotas, btnPaseos, btnCerrarSesion;
    private SessionDAO sessionDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        
        vincularVistas();
        sessionDAO = new SessionDAO(this);
        configurarNavegacion();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void vincularVistas() {
        btnMascotas = findViewById(R.id.btnMascotas);
        btnPaseos = findViewById(R.id.btnPaseos);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
    }

    private void configurarNavegacion() {
        btnMascotas.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, PetsActivity.class);
            startActivity(intent);
        });

        btnPaseos.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, RidesActivity.class);
            startActivity(intent);
        });

        btnCerrarSesion.setOnClickListener(v -> cerrarSesion());
    }

    private void cerrarSesion() {
        sessionDAO.logout();
        Toast.makeText(this, "Sesion cerrada correctamente", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}