package com.moonlight.petride.screens.pets.manage_pet;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.moonlight.petride.R;
import com.moonlight.petride.data.model.Pet;

/**
 * ManagePetsActivity: Pantalla para gestionar los detalles de una mascota seleccionada.
 * Recibe los datos a través de un Intent.
 */
public class ManagePetsActivity extends AppCompatActivity {

    private TextView tvNombreMascota, tvRaza, tvEdad, tvDireccion, tvCiudad, tvCuidados, tvVolver;
    private Button btnEditar, btnEliminar;
    private Pet pet; // Objeto que recibirá los datos

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
        tvDireccion = findViewById(R.id.tvDireccion); // Asegúrate de que existan en el modelo o base de datos si quieres llenarlos
        tvCiudad = findViewById(R.id.tvCiudad);
        tvCuidados = findViewById(R.id.tvCuidados);
        tvVolver = findViewById(R.id.tvVolver);
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);
    }

    /**
     * Extrae el objeto Pet enviado desde el Adapter.
     */
    private void recibirDatos() {
        if (getIntent() != null && getIntent().hasExtra("PET_DATA")) {
            // Obtenemos el objeto serializable y lo casteamos a Pet
            pet = (Pet) getIntent().getSerializableExtra("PET_DATA");
            
            if (pet != null) {
                mostrarDatos();
            }
        } else {
            Toast.makeText(this, "Error: No se recibieron datos de la mascota", Toast.LENGTH_SHORT).show();
            finish(); // Cerramos la actividad si no hay datos
        }
    }

    /**
     * Llena los componentes de la UI con la información del objeto Pet.
     */
    private void mostrarDatos() {
        tvNombreMascota.setText(pet.getName());
        tvRaza.setText("Raza: " + pet.getBreed());
        tvEdad.setText("Edad: " + pet.getAge() + " años");
        tvCuidados.setText("Cuidados especiales: " + pet.getCareTips());
        
        // Nota: Si el modelo Pet no tiene dirección o ciudad, 
        // podrías dejarlos con valores por defecto o quitarlos del XML si no se usan.
        tvDireccion.setText("Dirección: No especificada");
        tvCiudad.setText("Ciudad: No especificada");
    }

    private void configurarBotones() {
        tvVolver.setOnClickListener(v -> finish());
        
        btnEditar.setOnClickListener(v -> {
            // Lógica para editar (próximamente)
            Toast.makeText(this, "Editar: " + pet.getName(), Toast.LENGTH_SHORT).show();
        });

        btnEliminar.setOnClickListener(v -> {
            // Lógica para eliminar (próximamente)
            Toast.makeText(this, "Eliminar: " + pet.getName(), Toast.LENGTH_SHORT).show();
        });
    }
}
