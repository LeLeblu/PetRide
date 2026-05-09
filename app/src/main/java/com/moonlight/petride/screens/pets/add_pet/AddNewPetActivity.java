package com.moonlight.petride.screens.pets.add_pet;

import android.Manifest;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.moonlight.petride.data.PetDAO;
import com.moonlight.petride.data.SessionDAO;
import com.moonlight.petride.screens.authentication.login.LoginActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AddNewPetActivity extends AppCompatActivity {

    private ImageView imgMascota;
    private EditText etNombreMascota, etRaza, etEdad, etCareTips;
    private Button btnAgregarMascota;
    private TextView tvVolverMisMascotas;
    private String imagePath = "";
    private PetDAO petDAO;
    private SessionDAO sessionDAO;

    // Abre el selector de imágenes del sistema (galería/archivos).
    private final ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    String rutaGuardada = guardarUriEnAlmacenInterno(uri);
                    if (rutaGuardada != null) {
                        imagePath = rutaGuardada;
                        imgMascota.setImageURI(Uri.fromFile(new File(rutaGuardada)));
                    } else {
                        Toast.makeText(this, "No se pudo guardar la imagen seleccionada", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    // Abre la cámara y recibe una miniatura para mostrarla en pantalla.
    private final ActivityResultLauncher<Void> takePicture =
            registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), bitmap -> {
                if (bitmap != null) {
                    String rutaGuardada = guardarBitmapEnAlmacenInterno(bitmap);
                    if (rutaGuardada != null) {
                        imagePath = rutaGuardada;
                        imgMascota.setImageURI(Uri.fromFile(new File(rutaGuardada)));
                    } else {
                        Toast.makeText(this, "No se pudo guardar la foto tomada", Toast.LENGTH_SHORT).show();
                    }
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
        petDAO = new PetDAO(this);
        sessionDAO = new SessionDAO(this);
        configurarSeleccionImagen();
        configurarBotones();

    }

    private void configurarBotones() {
        btnAgregarMascota.setOnClickListener(v -> {
            if (!validarCamposMascota()) {
                return;
            }
            guardarMascotaEnSQLite();
        });
        tvVolverMisMascotas.setOnClickListener(v -> finish());
    }

    private void guardarMascotaEnSQLite() {
        long userId = sessionDAO.getLoggedUserId();
        if (userId == -1) {
            Toast.makeText(this, "Sesion expirada. Inicia sesion de nuevo.", Toast.LENGTH_SHORT).show();
            startActivity(new android.content.Intent(this, LoginActivity.class));
            finish();
            return;
        }

        String nombre = etNombreMascota.getText().toString().trim();
        String raza = etRaza.getText().toString().trim();
        int edad = Integer.parseInt(etEdad.getText().toString().trim());
        String careTips = etCareTips.getText().toString().trim();

        long id = petDAO.insertPet(userId, nombre, raza, edad, careTips, imagePath);
        if (id != -1) {
            Toast.makeText(this, "Mascota registrada", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "No se pudo guardar la mascota", Toast.LENGTH_SHORT).show();
        }
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

    private boolean validarCamposMascota() {
        String nombre = etNombreMascota.getText().toString().trim();
        String raza = etRaza.getText().toString().trim();
        String edadTexto = etEdad.getText().toString().trim();

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

        if (edadTexto.isEmpty()) {
            etEdad.setError("La edad es obligatoria");
            etEdad.requestFocus();
            return false;
        }

        int edad;
        try {
            edad = Integer.parseInt(edadTexto);
        } catch (NumberFormatException e) {
            etEdad.setError("La edad debe ser numérica");
            etEdad.requestFocus();
            return false;
        }

        if (edad < 0) {
            etEdad.setError("La edad no puede ser negativa");
            etEdad.requestFocus();
            return false;
        }

        return true;
    }

    private String guardarBitmapEnAlmacenInterno(Bitmap bitmap) {
        File archivo = new File(getFilesDir(), "pet_" + System.currentTimeMillis() + ".jpg");
        try (FileOutputStream outputStream = new FileOutputStream(archivo)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
            outputStream.flush();
            return archivo.getAbsolutePath();
        } catch (IOException e) {
            return null;
        }
    }

    private String guardarUriEnAlmacenInterno(Uri sourceUri) {
        File archivo = new File(getFilesDir(), "pet_" + System.currentTimeMillis() + ".jpg");
        try (InputStream inputStream = getContentResolver().openInputStream(sourceUri);
             FileOutputStream outputStream = new FileOutputStream(archivo)) {

            if (inputStream == null) {
                return null;
            }

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            return archivo.getAbsolutePath();
        } catch (IOException e) {
            return null;
        }
    }
}