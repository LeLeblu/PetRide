package com.moonlight.petride.screens.pets.manage_pet;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.moonlight.petride.R;

public class ManagePetsActivity extends AppCompatActivity {

    private TextView tvNombreMascota, tvRaza, tvEdad, tvDireccion, tvCiudad, tvCuidados, tvVolver;
    private Button btnEditar, btnEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_pets);
        
        vincularVistas();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void vincularVistas() {
        tvNombreMascota = findViewById(R.id.tvNombreMascota);
        tvRaza = findViewById(R.id.tvRaza);
        tvEdad = findViewById(R.id.tvEdad);
        tvDireccion = findViewById(R.id.tvDireccion);
        tvCiudad = findViewById(R.id.tvCiudad);
        tvCuidados = findViewById(R.id.tvCuidados);
        tvVolver = findViewById(R.id.tvVolver);
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);
    }
}