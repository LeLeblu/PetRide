package com.moonlight.petride.screens.rides;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.moonlight.petride.R;

public class RidesActivity extends AppCompatActivity {

    private Spinner spMascotas;
    private EditText etFecha, etHora, etDireccionPaseo;
    private Button btnConfirmarPaseo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rides);
        
        vincularVistas();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void vincularVistas() {
        spMascotas = findViewById(R.id.spMascotas);
        etFecha = findViewById(R.id.etFecha);
        etHora = findViewById(R.id.etHora);
        etDireccionPaseo = findViewById(R.id.etDireccionPaseo);
        btnConfirmarPaseo = findViewById(R.id.btnConfirmarPaseo);
    }
}