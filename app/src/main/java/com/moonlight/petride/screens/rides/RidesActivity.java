package com.moonlight.petride.screens.rides;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.moonlight.petride.R;
import com.moonlight.petride.data.model.Pet;
import com.moonlight.petride.screens.rides.history_rides.RidesHistoryActivity;

import java.util.ArrayList;
import java.util.List;

public class RidesActivity extends AppCompatActivity {

    private Spinner spMascotas;
    private EditText etFecha, etHora, etDireccionPaseo;
    private Button btnConfirmarPaseo, btnMisPaseos;
    private TextView tvVolverHome;
    private List<Pet> listaMascotas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rides);
        
        vincularVistas();
        poblarSpinnerMascotas();
        configurarNavegacion();
    }

    private void vincularVistas() {
        spMascotas = findViewById(R.id.spMascotas);
        etFecha = findViewById(R.id.etFecha);
        etHora = findViewById(R.id.etHora);
        etDireccionPaseo = findViewById(R.id.etDireccionPaseo);
        btnConfirmarPaseo = findViewById(R.id.btnConfirmarPaseo);
        btnMisPaseos = findViewById(R.id.btnMisPaseos);
        tvVolverHome = findViewById(R.id.tvVolverHome);
    }

    private void poblarSpinnerMascotas() {
        listaMascotas = new ArrayList<>();
        listaMascotas.add(new Pet(1, 1, "Firulais", "Golden Retriever", 3, "", ""));
        listaMascotas.add(new Pet(2, 1, "Rex", "Pastor Alemán", 5, "", ""));

        List<String> nombresMascotas = new ArrayList<>();
        for (Pet p : listaMascotas) {
            nombresMascotas.add(p.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
                android.R.layout.simple_spinner_item, nombresMascotas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMascotas.setAdapter(adapter);
    }

    private void configurarNavegacion() {
        btnConfirmarPaseo.setOnClickListener(v -> {
            Intent intent = new Intent(RidesActivity.this, RidesHistoryActivity.class);
            startActivity(intent);
        });

        btnMisPaseos.setOnClickListener(v -> {
            Intent intent = new Intent(RidesActivity.this, RidesHistoryActivity.class);
            startActivity(intent);
        });

        tvVolverHome.setOnClickListener(v -> {
            finish();
        });
    }
}