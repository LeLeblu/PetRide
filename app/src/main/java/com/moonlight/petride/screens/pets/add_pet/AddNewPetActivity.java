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
import com.moonlight.petride.data.PetDAO;
import com.moonlight.petride.data.SessionDAO;

/**
 * Actividad para agregar una nueva mascota.
 * Implementa validaciones y manejo de imagen mediante ActivityResultLauncher.
 * Ahora utiliza SessionDAO para obtener el ID del usuario logueado.
 */
public class AddNewPetActivity extends AppCompatActivity {

    private static final String TAG = "AddNewPetActivity";

    private ImageView imgMascota;
    private EditText etNombreMascota, etRaza, etEdad, etCareTips;
    private Button btnAgregarMascota;

    private PetDAO petDAO;
    private SessionDAO sessionDAO;
    private long userId;
    private String imagePath = ""; // Ruta o URI de la imagen seleccionada

    // --- REGISTRO DE LAUNCHERS ---

    // 1. Launcher para Galería (Photo Picker)
    private final ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    imgMascota.setImageURI(uri);
                    imagePath = uri.toString();
                }
            });

    // 2. Launcher para Cámara (Retorna miniatura)
    private final ActivityResultLauncher<Void> takePicture =
            registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), bitmap -> {
                if (bitmap != null) {
                    imgMascota.setImageBitmap(bitmap);
                    // Simulamos un path para el ejemplo universitario
                    imagePath = "camera_capture_" + System.currentTimeMillis();
                }
            });

    // 3. Launcher para solicitud de permisos
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

        // Ajuste de padding para EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // REQUERIMIENTO: Obtener el ID del usuario desde la sesión en SQLite
        sessionDAO = new SessionDAO(this);
        userId = sessionDAO.getLoggedUserId();
        
        // Verificación de seguridad didáctica
        if (userId == -1) {
            Toast.makeText(this, "Error: No hay una sesión activa. Por favor, inicia sesión.", Toast.LENGTH_LONG).show();
            finish(); // Cerramos la actividad si no hay usuario identificado
            return;
        }

        petDAO = new PetDAO(this);

        vincularVistas();
        configurarSeleccionImagen();

        btnAgregarMascota.setOnClickListener(v -> {
            validarYGuardarMascota();
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

    private void validarYGuardarMascota() {
        String nombre = etNombreMascota.getText().toString().trim();
        String raza = etRaza.getText().toString().trim();
        String edadStr = etEdad.getText().toString().trim();
        String cuidados = etCareTips.getText().toString().trim();

        if (nombre.isEmpty()) {
            etNombreMascota.setError("El nombre es obligatorio");
            return;
        }

        if (raza.isEmpty()) {
            etRaza.setError("La raza es obligatoria");
            return;
        }

        if (edadStr.isEmpty()) {
            etEdad.setError("La edad es obligatoria");
            return;
        }

        if (imagePath.isEmpty()) {
            Toast.makeText(this, "Por favor, selecciona una foto", Toast.LENGTH_SHORT).show();
            return;
        }

        int edad;
        try {
            edad = Integer.parseInt(edadStr);
        } catch (NumberFormatException e) {
            etEdad.setError("Edad inválida");
            return;
        }

        // Ahora el userId viene directamente del SessionDAO (SQLite), garantizando que la FK sea correcta.
        Log.d(TAG, "Intentando insertar mascota para USER_ID: " + userId);
        
        long id = petDAO.insertPet(userId, nombre, raza, edad, cuidados, imagePath);

        if (id != -1) {
            Toast.makeText(this, "¡" + nombre + " se ha registrado con éxito!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Log.e(TAG, "Error al insertar en SQLite. FK Violation o error de BD.");
            Toast.makeText(this, "Error: No se pudo guardar la mascota.", Toast.LENGTH_LONG).show();
        }
    }
}
