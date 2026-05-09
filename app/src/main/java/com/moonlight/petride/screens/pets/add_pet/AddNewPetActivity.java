package com.moonlight.petride.screens.pets.add_pet;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

public class AddNewPetActivity extends AppCompatActivity {

    private ImageView imgMascota;
    private EditText etNombreMascota, etRaza, etEdad, etCareTips;
    private Button btnAgregarMascota;
    private TextView tvVolverMisMascotas;
    private String imagePath = "";

    // Abre el selector de imágenes del sistema (galería/archivos).
    private final ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    imgMascota.setImageURI(uri);
                    imagePath = uri.toString();
                }
            });

    // Abre la cámara y recibe una miniatura para mostrarla en pantalla.
    private final ActivityResultLauncher<Void> takePicture =
            registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), bitmap -> {
                if (bitmap != null) {
                    imgMascota.setImageBitmap(bitmap);
                    imagePath = "camera_capture_" + System.currentTimeMillis();
                }
            });

    // Solicita permiso de cámara en tiempo de ejecución.
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    takePicture.launch(null);
                } else {
                    Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
                }
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
        configurarBotones();

    }

    private void configurarBotones() {
        // Por ahora simula fin de la actividad.
        btnAgregarMascota.setOnClickListener(v -> finish());
        // Vuelve a la pantalla anterior, igual que en otras vistas.
        tvVolverMisMascotas.setOnClickListener(v -> finish());
    }

    private void vincularVistas() {
        imgMascota = findViewById(R.id.imgMascota);
        etNombreMascota = findViewById(R.id.etNombreMascota);
        etRaza = findViewById(R.id.etRaza);
        etEdad = findViewById(R.id.etEdad);
        etCareTips = findViewById(R.id.etCare_tips);
        btnAgregarMascota = findViewById(R.id.btnAgregarMascota);
        tvVolverMisMascotas = findViewById(R.id.tvVolverMisMascotas);
    }

    private void configurarSeleccionImagen() {
        imgMascota.setOnClickListener(v -> {
            String[] opciones = {"Tomar foto", "Elegir de la galería"};
            new AlertDialog.Builder(this)
                    .setTitle("Foto de tu mascota")
                    .setItems(opciones, (dialog, which) -> {
                        if (which == 0) {
                            gestionarCamara();
                        } else {
                            gestionarGaleria();
                        }
                    })
                    .show();
        });
    }

    // Usa PhotoPicker para elegir una imagen sin pedir permisos extra.
    private void gestionarGaleria() {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    // Si hay permiso, abre cámara; si no, lo solicita.
    private void gestionarCamara() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            takePicture.launch(null);
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }
}