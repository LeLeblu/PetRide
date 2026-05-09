package com.moonlight.petride.screens.pets.manage_pet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.moonlight.petride.R;
import com.moonlight.petride.data.PetDAO;
import com.moonlight.petride.data.SessionDAO;
import com.moonlight.petride.data.model.Pet;
import com.moonlight.petride.screens.authentication.login.LoginActivity;
import com.moonlight.petride.screens.pets.edit_pet.EditPetActivity;

import java.io.File;

public class ManagePetsActivity extends AppCompatActivity {

    private TextView tvNombreMascota, tvRaza, tvEdad, tvCuidados, tvVolver;
    private ImageView ivPetPhotoDetail;
    private Button btnEditar, btnEliminar;
    private Pet pet;
    private PetDAO petDAO;
    private SessionDAO sessionDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_pets);

        vincularVistas();
        petDAO = new PetDAO(this);
        sessionDAO = new SessionDAO(this);
        recibirDatos();
        configurarBotones();
    }

    private void vincularVistas() {
        tvNombreMascota = findViewById(R.id.tvNombreMascota);
        tvRaza = findViewById(R.id.tvRaza);
        tvEdad = findViewById(R.id.tvEdad);
        tvCuidados = findViewById(R.id.tvCuidados);
        ivPetPhotoDetail = findViewById(R.id.ivPetPhotoDetail);
        tvVolver = findViewById(R.id.tvVolver);
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);
    }

    private void recibirDatos() {
        long userId = sessionDAO.getLoggedUserId();
        if (userId == -1) {
            Toast.makeText(this, "Sesion expirada. Inicia sesion de nuevo.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        long petId = getIntent().getLongExtra("pet_id", -1);
        if (petId == -1) {
            Toast.makeText(this, "Mascota no encontrada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        pet = petDAO.getPetById(petId);
        if (pet == null || pet.getOwnerId() != userId) {
            Toast.makeText(this, "No tienes permiso para ver esta mascota", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mostrarDatos();
    }

    @SuppressLint("SetTextI18n")
    private void mostrarDatos() {
        tvNombreMascota.setText(pet.getName());
        tvRaza.setText("Raza: " + pet.getBreed());
        tvEdad.setText("Edad: " + pet.getAge() + " años");
        tvCuidados.setText("Cuidados especiales: " + pet.getCareTips());
        cargarImagenMascota(pet.getImagePath());
    }

    private void configurarBotones() {
        tvVolver.setOnClickListener(v -> finish());
        
        btnEditar.setOnClickListener(v -> {
            if (pet == null) {
                return;
            }

            Intent intent = new Intent(ManagePetsActivity.this, EditPetActivity.class);
            intent.putExtra("pet_id", pet.getId());
            startActivity(intent);
        });

        btnEliminar.setOnClickListener(v -> {
            if (pet == null) {
                return;
            }
            int rows = petDAO.deletePet(pet.getId());
            if (rows > 0) {
                Toast.makeText(this, "Mascota eliminada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No se pudo eliminar la mascota", Toast.LENGTH_SHORT).show();
            }
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pet != null) {
            Pet petActualizada = petDAO.getPetById(pet.getId());
            if (petActualizada != null) {
                pet = petActualizada;
                mostrarDatos();
            }
        }
    }

    private void cargarImagenMascota(String imagePath) {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            ivPetPhotoDetail.setImageResource(R.drawable.ic_dog);
            return;
        }

        try {
            if (imagePath.startsWith("content://") || imagePath.startsWith("file://")) {
                ivPetPhotoDetail.setImageURI(Uri.parse(imagePath));
            } else {
                File file = new File(imagePath);
                if (file.exists()) {
                    ivPetPhotoDetail.setImageURI(Uri.fromFile(file));
                } else {
                    ivPetPhotoDetail.setImageResource(R.drawable.ic_dog);
                }
            }
        } catch (Exception e) {
            ivPetPhotoDetail.setImageResource(R.drawable.ic_dog);
        }
    }
}