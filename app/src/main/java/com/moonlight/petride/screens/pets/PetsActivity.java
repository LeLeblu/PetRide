package com.moonlight.petride.screens.pets;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.moonlight.petride.R;
import com.moonlight.petride.screens.pets.add_pet.AddNewPetActivity;

public class PetsActivity extends AppCompatActivity {

    private LinearLayout containerMascotas;
    private Button btnAgregarNueva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pets);
        
        vincularVistas();
        configurarNavegacion();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void vincularVistas() {
        containerMascotas = findViewById(R.id.containerMascotas);
        btnAgregarNueva = findViewById(R.id.btnAgregarNueva);
    }

    private void configurarNavegacion() {
        btnAgregarNueva.setOnClickListener(v -> {
            Intent intent = new Intent(PetsActivity.this, AddNewPetActivity.class);
            startActivity(intent);
        });
    }
}