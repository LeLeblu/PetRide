package com.moonlight.petride.screens.pets.manage_pet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.moonlight.petride.R;
import com.moonlight.petride.data.model.Pet;
import com.moonlight.petride.screens.pets.edit_pet.EditPetActivity;

public class ManagePetsActivity extends AppCompatActivity {

    private TextView tvNombreMascota, tvRaza, tvEdad, tvCuidados, tvVolver;
    private Button btnEditar, btnEliminar;
    private Pet pet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_pets);
        
        vincularVistas();
        recibirDatos();
        configurarBotones();
    }

    private void vincularVistas() {
        tvNombreMascota = findViewById(R.id.tvNombreMascota);
        tvRaza = findViewById(R.id.tvRaza);
        tvEdad = findViewById(R.id.tvEdad);
        tvCuidados = findViewById(R.id.tvCuidados);
        tvVolver = findViewById(R.id.tvVolver);
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);
    }

    private void recibirDatos() {
        if (getIntent() != null && getIntent().hasExtra("PET_DATA")) {
            pet = (Pet) getIntent().getSerializableExtra("PET_DATA");
            if (pet != null) {
                mostrarDatos();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void mostrarDatos() {
        tvNombreMascota.setText(pet.getName());
        tvRaza.setText("Raza: " + pet.getBreed());
        tvEdad.setText("Edad: " + pet.getAge() + " años");
        tvCuidados.setText("Cuidados especiales: " + pet.getCareTips());
    }

    private void configurarBotones() {
        tvVolver.setOnClickListener(v -> finish());
        
        btnEditar.setOnClickListener(v -> {
            if (pet == null) {
                return;
            }

            Intent intent = new Intent(ManagePetsActivity.this, EditPetActivity.class);
            // TODO: FASE SQLITE - En el futuro, solo pasaremos el petId y consultaremos la BD en el EditPetActivity.
            intent.putExtra("pet_id", pet.getId());
            intent.putExtra("pet_owner_id", pet.getOwnerId());
            intent.putExtra("pet_name", pet.getName());
            intent.putExtra("pet_breed", pet.getBreed());
            intent.putExtra("pet_age", pet.getAge());
            intent.putExtra("pet_care_tips", pet.getCareTips());
            intent.putExtra("pet_image_path", pet.getImagePath());
            startActivity(intent);
        });

        btnEliminar.setOnClickListener(v -> {
            finish();
        });
    }
}