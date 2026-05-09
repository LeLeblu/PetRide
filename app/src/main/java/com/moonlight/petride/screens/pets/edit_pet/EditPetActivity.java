package com.moonlight.petride.screens.pets.edit_pet;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.moonlight.petride.R;
import com.moonlight.petride.data.PetDAO;
import com.moonlight.petride.data.SessionDAO;
import com.moonlight.petride.data.model.Pet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Pantalla de edición con validaciones alineadas al contrato de la tabla pets.
 */
public class EditPetActivity extends AppCompatActivity {

    private ImageView imgMascota;
    private EditText etNombreMascota, etRaza, etEdad, etCareTips;
    private TextView tvTituloMascotaForm;
    private Button btnGuardarCambios;
    private TextView tvVolver;
    private long petId;
    private String imagePath;
    private PetDAO petDAO;
    private SessionDAO sessionDAO;

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
        setContentView(R.layout.activity_add_new_pet);

        vincularVistas();
        petDAO = new PetDAO(this);
        sessionDAO = new SessionDAO(this);
        configurarTextosModoEdicion();
        configurarSeleccionImagen();
        recibirDatosDesdeIntent();
        configurarBotones();
    }

    private void vincularVistas() {
        imgMascota = findViewById(R.id.imgMascota);
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
            guardarCambiosEnSQLite();
        });

        tvVolver.setOnClickListener(v -> finish());
    }

    private void configurarTextosModoEdicion() {
        tvTituloMascotaForm.setText("Editar Mascota");
        btnGuardarCambios.setText("Guardar Cambios");
    }

    private void configurarSeleccionImagen() {
        imgMascota.setOnClickListener(v -> {
            String[] opciones = {"Tomar foto", "Elegir de la galería"};
            new AlertDialog.Builder(this)
                    .setTitle("Actualizar foto de tu mascota")
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

    private void gestionarGaleria() {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    private void gestionarCamara() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            takePicture.launch(null);
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void recibirDatosDesdeIntent() {
        petId = getIntent().getLongExtra("pet_id", -1);
        if (petId == -1) {
            Toast.makeText(this, "Mascota no encontrada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        long userId = sessionDAO.getLoggedUserId();
        if (userId == -1) {
            Toast.makeText(this, "Sesion expirada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Pet pet = petDAO.getPetById(petId);
        if (pet == null || pet.getOwnerId() != userId) {
            Toast.makeText(this, "No puedes editar esta mascota", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etNombreMascota.setText(pet.getName());
        etRaza.setText(pet.getBreed());
        etEdad.setText(String.valueOf(pet.getAge()));
        etCareTips.setText(pet.getCareTips());
        imagePath = pet.getImagePath();
        cargarImagenActual(imagePath);
    }

    private void guardarCambiosEnSQLite() {
        String nombre = etNombreMascota.getText().toString().trim();
        String raza = etRaza.getText().toString().trim();
        int edad = Integer.parseInt(etEdad.getText().toString().trim());
        String careTips = etCareTips.getText().toString().trim();

        int rows = petDAO.updatePet(petId, nombre, raza, edad, careTips, imagePath);
        if (rows > 0) {
            Toast.makeText(this, "Mascota actualizada", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "No se pudo actualizar la mascota", Toast.LENGTH_SHORT).show();
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

    private void cargarImagenActual(String ruta) {
        if (ruta == null || ruta.trim().isEmpty()) {
            imgMascota.setImageResource(R.drawable.ic_dog);
            return;
        }

        try {
            File archivo = new File(ruta);
            if (archivo.exists()) {
                imgMascota.setImageURI(Uri.fromFile(archivo));
            } else if (ruta.startsWith("content://") || ruta.startsWith("file://")) {
                imgMascota.setImageURI(Uri.parse(ruta));
            } else {
                imgMascota.setImageResource(R.drawable.ic_dog);
            }
        } catch (Exception e) {
            imgMascota.setImageResource(R.drawable.ic_dog);
        }
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
