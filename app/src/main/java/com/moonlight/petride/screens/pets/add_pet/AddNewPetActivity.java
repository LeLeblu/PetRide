package com.moonlight.petride.screens.pets.add_pet;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.moonlight.petride.R;

public class AddNewPetActivity extends AppCompatActivity {

    private ImageView imgMascota;
    private Button btnGaleria, btnCamara, btnAgregarMascota;
    private EditText etNombreMascota, etRaza, etEdad, etDireccion, etCiudad, etCareTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new_pet);
        
        vincularVistas();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void vincularVistas() {
        imgMascota = findViewById(R.id.imgMascota);
        btnGaleria = findViewById(R.id.btnGaleria);
        btnCamara = findViewById(R.id.btnCamara);
        btnAgregarMascota = findViewById(R.id.btnAgregarMascota);
        etNombreMascota = findViewById(R.id.etNombreMascota);
        etRaza = findViewById(R.id.etRaza);
        etEdad = findViewById(R.id.etEdad);
        etDireccion = findViewById(R.id.etDireccion);
        etCiudad = findViewById(R.id.etCiudad);
        etCareTips = findViewById(R.id.etCare_tips);
    }
}