package com.moonlight.petride.screens.pets.add_pet;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.moonlight.petride.R;

/**
 * AddNewPetActivity: Refactorizada para eliminar lógica de base de datos.
 * Mantiene la funcionalidad de selección de imagen y validación de UI.
 */
public class AddNewPetActivity extends AppCompatActivity {

    private static final String TAG = "AddNewPetActivity";

    private ImageView imgMascota;
    private EditText etNombreMascota, etRaza, etEdad, etCareTips;
    private Button btnAgregarMascota;

    private String imagePath = ""; // Ruta simulada

    // --- REGISTRO DE LAUNCHERS (Se mantienen para diseño) ---

    private final ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    imgMascota.setImageURI(uri);
                    imagePath = uri.toString();
                }
            });

    private final ActivityResultLauncher<Void> takePicture =
            registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), bitmap -> {
                if (bitmap != null) {
                    imgMascota.setImageBitmap(bitmap);
                    imagePath = "camera_capture_simulated";
                }
            });

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) takePicture.launch(null);
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new_pet);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        vincularVistas();
        configurarSeleccionImagen();

        btnAgregarMascota.setOnClickListener(v -> {
            validarYSimularGuardado();
        });
    }

    private void vincularVistas() {
        imgMascota = findViewById(R.id.imgMascota);
        etNombreMascota = findViewById(R.id.etNombreMascota);
        etRaza = findViewById(R.id.etRaza);
        etEdad = findViewById(R.id.etEdad);
        etCareTips = findViewById(R.id.etCare_tips);
        btnAgregarMascota = findViewById(R.id.btnAgregarMascota);
    }

    private void configurarSeleccionImagen() {
        imgMascota.setOnClickListener(v -> {
            String[] opciones = {"Tomar Foto", "Elegir de la Galería"};
            new AlertDialog.Builder(this)
                    .setTitle("Foto de tu mascota")
                    .setItems(opciones, (dialog, which) -> {
                        if (which == 0) gestionarCamara();
                        else gestionarGaleria();
                    })
                    .show();
        });
    }

    private void gestionarGaleria() {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    private void gestionarCamara() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            takePicture.launch(null);
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void validarYSimularGuardado() {
        String nombre = etNombreMascota.getText().toString().trim();

        if (nombre.isEmpty()) {
            etNombreMascota.setError("El nombre es obligatorio");
            return;
        }

        // ÉXITO SIMULADO (Modo Diseño)
        Toast.makeText(this, "¡" + nombre + " se ha registrado (Simulado)!", Toast.LENGTH_LONG).show();
        finish();
    }
}
