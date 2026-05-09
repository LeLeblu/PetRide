package com.moonlight.petride.screens.pets.edit_pet;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.moonlight.petride.R;

/**
 * Pantalla de edición con validaciones alineadas al contrato de la tabla pets.
 */
public class EditPetActivity extends AppCompatActivity {

    private EditText etNombreMascota, etRaza, etEdad, etCareTips;
    private TextView tvTituloMascotaForm;
    private Button btnGuardarCambios;
    private TextView tvVolver;
    private long petId;
    private long ownerId;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_pet);

        vincularVistas();
        configurarTextosModoEdicion();
        recibirDatosDesdeIntent();
        configurarBotones();
    }

    private void vincularVistas() {
        etNombreMascota = findViewById(R.id.etNombreMascota);
        etRaza = findViewById(R.id.etRaza);
        etEdad = findViewById(R.id.etEdad);
        etCareTips = findViewById(R.id.etCare_tips);
        tvTituloMascotaForm = findViewById(R.id.tvTituloMascotaForm);
        btnGuardarCambios = findViewById(R.id.btnAgregarMascota);
        tvVolver = findViewById(R.id.tvVolverMisMascotas);
    }

    private void configurarBotones() {
        btnGuardarCambios.setOnClickListener(v -> {
            if (!validarCamposMascota()) {
                return;
            }
            finish();
        });

        tvVolver.setOnClickListener(v -> finish());
    }

    private void configurarTextosModoEdicion() {
        tvTituloMascotaForm.setText("Editar Mascota");
        btnGuardarCambios.setText("Guardar Cambios");
    }

    private void recibirDatosDesdeIntent() {
        // TODO: FASE SQLITE - Cambiar esta recepción de extras por una consulta a SQLite usando el petId.
        petId = getIntent().getLongExtra("pet_id", -1);
        ownerId = getIntent().getLongExtra("pet_owner_id", -1);
        String nombre = getIntent().getStringExtra("pet_name");
        String raza = getIntent().getStringExtra("pet_breed");
        int edad = getIntent().getIntExtra("pet_age", 0);
        String careTips = getIntent().getStringExtra("pet_care_tips");
        imagePath = getIntent().getStringExtra("pet_image_path");

        if (nombre != null) {
            etNombreMascota.setText(nombre);
        }
        if (raza != null) {
            etRaza.setText(raza);
        }
        etEdad.setText(String.valueOf(edad));
        if (careTips != null) {
            etCareTips.setText(careTips);
        }
    }

    private boolean validarCamposMascota() {
        String nombre = etNombreMascota.getText().toString().trim();
        String raza = etRaza.getText().toString().trim();
        String edadTexto = etEdad.getText().toString().trim();

        // Contrato de pets (main): name y breed son TEXT NOT NULL.
        if (nombre.isEmpty()) {
            etNombreMascota.setError("El nombre es obligatorio");
            etNombreMascota.requestFocus();
            return false;
        }

        if (raza.isEmpty()) {
            etRaza.setError("La raza es obligatoria");
            etRaza.requestFocus();
            return false;
        }

        // Contrato de pets (main): age es INTEGER NOT NULL.
        if (edadTexto.isEmpty()) {
            etEdad.setError("La edad es obligatoria");
            etEdad.requestFocus();
            return false;
        }

        try {
            int edad = Integer.parseInt(edadTexto);
            if (edad < 0) {
                etEdad.setError("La edad no puede ser negativa");
                etEdad.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            etEdad.setError("La edad debe ser numérica");
            etEdad.requestFocus();
            return false;
        }

        // care_tips es opcional en la base de datos, por eso no se obliga.
        return true;
    }
}
