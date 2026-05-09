package com.moonlight.petride.screens.pets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moonlight.petride.R;
import com.moonlight.petride.data.PetDAO;
import com.moonlight.petride.data.SessionDAO;
import com.moonlight.petride.data.model.Pet;
import com.moonlight.petride.screens.authentication.login.LoginActivity;
import com.moonlight.petride.screens.pets.add_pet.AddNewPetActivity;

import java.util.List;

public class PetsActivity extends AppCompatActivity {

    private RecyclerView rvPets;
    private TextView tvEmptyMessage, tvVolverHome;
    private Button btnAgregarNueva;
    private PetDAO petDAO;
    private SessionDAO sessionDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets);

        rvPets = findViewById(R.id.rvPets);
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);
        btnAgregarNueva = findViewById(R.id.btnAgregarNueva);
        tvVolverHome = findViewById(R.id.tvVolverHome);
        petDAO = new PetDAO(this);
        sessionDAO = new SessionDAO(this);

        rvPets.setLayoutManager(new LinearLayoutManager(this));

        btnAgregarNueva.setOnClickListener(v -> {
            Intent intent = new Intent(PetsActivity.this, AddNewPetActivity.class);
            startActivity(intent);
        });

        tvVolverHome.setOnClickListener(v -> {
            finish();
        });

        cargarMascotasDesdeSQLite();
    }

    private void cargarMascotasDesdeSQLite() {
        long userId = sessionDAO.getLoggedUserId();
        if (userId == -1) {
            Toast.makeText(this, "Sesion expirada. Inicia sesion de nuevo.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        List<Pet> pets = petDAO.getPetsByUserId(userId);

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
        cargarMascotasDesdeSQLite();
    }
}