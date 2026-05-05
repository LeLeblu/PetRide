package com.moonlight.petride.screens.pets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.moonlight.petride.R;
import com.moonlight.petride.data.model.Pet;
import com.moonlight.petride.screens.pets.add_pet.AddNewPetActivity;
import java.util.ArrayList;
import java.util.List;


 // Muestra una lista de mascotas simulada -->
public class PetsActivity extends AppCompatActivity {

    private RecyclerView rvPets;
    private TextView tvEmptyMessage;
    private Button btnAgregarNueva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets);

        // 1. Vincular vistas
        rvPets = findViewById(R.id.rvPets);
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);
        btnAgregarNueva = findViewById(R.id.btnAgregarNueva);

        // 2. Configurar el RecyclerView
        rvPets.setLayoutManager(new LinearLayoutManager(this));

        // 3. Configurar navegación
        btnAgregarNueva.setOnClickListener(v -> {
            Intent intent = new Intent(PetsActivity.this, AddNewPetActivity.class);
            startActivity(intent);
        });

        // 4. Cargar datos simulados
        cargarMascotasSimuladas();
    }

    private void cargarMascotasSimuladas() {
        // LÓGICA DE DISEÑO: Creamos una lista ficticia para visualizar el RecyclerView
        List<Pet> pets = new ArrayList<>();
        pets.add(new Pet(1, 1, "Firulais", "Golden Retriever", 3, "Mucho amor", ""));
        pets.add(new Pet(2, 1, "Rex", "Pastor Alemán", 5, "Ejercicio diario", ""));

        if (pets.isEmpty()) {
            tvEmptyMessage.setVisibility(View.VISIBLE);
            rvPets.setVisibility(View.GONE);
        } else {
            tvEmptyMessage.setVisibility(View.GONE);
            rvPets.setVisibility(View.VISIBLE);

            PetAdapter petAdapter = new PetAdapter(pets);
            rvPets.setAdapter(petAdapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // En modo diseño, mantenemos la carga simulada
        cargarMascotasSimuladas();
    }
}
