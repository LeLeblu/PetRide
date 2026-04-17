package com.moonlight.petride.screens.pets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.moonlight.petride.R;
import com.moonlight.petride.data.PetDAO;
import com.moonlight.petride.data.SessionDAO;
import com.moonlight.petride.data.model.Pet;
import com.moonlight.petride.screens.pets.add_pet.AddNewPetActivity;
import java.util.List;

/**
 * PetsActivity: Pantalla principal para visualizar la lista de mascotas del usuario logueado.
 * 
 * Conceptos clave:
 * - RecyclerView: Componente eficiente para mostrar grandes listas de datos.
 * - PetAdapter: Gestiona la creación y vinculación de las vistas de los elementos.
 * - PetDAO: Capa de datos para consultar la base de datos SQLite.
 */
public class PetsActivity extends AppCompatActivity {

    private RecyclerView rvPets;
    private TextView tvEmptyMessage;
    private Button btnAgregarNueva;
    private PetAdapter petAdapter;
    private PetDAO petDAO;
    private SessionDAO sessionDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets);

        // 1. Inicializar DAOs para acceso a datos
        petDAO = new PetDAO(this);
        sessionDAO = new SessionDAO(this);

        // 2. Vincular vistas del XML
        rvPets = findViewById(R.id.rvPets);
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);
        btnAgregarNueva = findViewById(R.id.btnAgregarNueva);

        // 3. Configurar el RecyclerView
        rvPets.setLayoutManager(new LinearLayoutManager(this));

        // 4. Configurar eventos
        btnAgregarNueva.setOnClickListener(v -> {
            Intent intent = new Intent(PetsActivity.this, AddNewPetActivity.class);
            startActivity(intent);
        });

        // 5. Cargar datos
        cargarMascotas();
    }

    private void cargarMascotas() {
        // Obtenemos el ID del usuario que tiene la sesión activa
        long userId = sessionDAO.getLoggedUserId();

        if (userId != -1) {
            // Consultamos la lista de mascotas filtrada por el ID del dueño
            List<Pet> pets = petDAO.getPetsByUserId(userId);

            // Gestionamos la visibilidad del mensaje de lista vacía
            if (pets.isEmpty()) {
                tvEmptyMessage.setVisibility(View.VISIBLE);
                rvPets.setVisibility(View.GONE);
            } else {
                tvEmptyMessage.setVisibility(View.GONE);
                rvPets.setVisibility(View.VISIBLE);

                // Inicializamos y asignamos el adaptador al RecyclerView
                petAdapter = new PetAdapter(pets);
                rvPets.setAdapter(petAdapter);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargamos la lista al volver a la actividad por si se añadió una mascota nueva
        cargarMascotas();
    }
}
