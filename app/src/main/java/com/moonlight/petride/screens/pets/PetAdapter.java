package com.moonlight.petride.screens.pets;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.moonlight.petride.R;
import com.moonlight.petride.data.model.Pet;
import java.util.List;

/**
 * PetAdapter: El "puente" entre los datos (la lista de mascotas) y la interfaz de usuario (RecyclerView).
 * 
 * Didáctica:
 * 1. RecyclerView solicita una nueva vista (onCreateViewHolder).
 * 2. El ViewHolder mantiene las referencias a los IDs de los componentes para no buscarlos repetidamente (findViewById).
 * 3. El Adapter vincula los datos de una mascota específica a ese ViewHolder (onBindViewHolder).
 */
public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {

    private List<Pet> petList;

    public PetAdapter(List<Pet> petList) {
        this.petList = petList;
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflamos el diseño de la tarjeta (item_pet.xml) para cada fila
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pet, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        // Obtenemos la mascota en la posición actual
        Pet pet = petList.get(position);
        
        // Asignamos los textos
        holder.tvName.setText(pet.getName());
        holder.tvBreed.setText(pet.getBreed());
        holder.tvAge.setText("Edad: " + pet.getAge() + " años");

        // Gestión de la imagen con manejo de errores
        if (pet.getImagePath() != null && !pet.getImagePath().isEmpty()) {
            try {
                // Intentamos cargar la URI de la imagen
                holder.ivPhoto.setImageURI(Uri.parse(pet.getImagePath()));
            } catch (Exception e) {
                // Si la imagen fue borrada o la URI es inválida, usamos una por defecto
                holder.ivPhoto.setImageResource(R.drawable.ic_dog);
            }
        } else {
            // Imagen por defecto si no hay ruta guardada
            holder.ivPhoto.setImageResource(R.drawable.ic_dog);
        }
    }

    @Override
    public int getItemCount() {
        return petList != null ? petList.size() : 0;
    }

    /**
     * ViewHolder: Clase interna que "sostiene" las vistas de un solo elemento de la lista.
     * Esto optimiza el rendimiento al evitar llamadas constantes a findViewById.
     */
    public static class PetViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        TextView tvName, tvBreed, tvAge;

        public PetViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.ivPetPhoto);
            tvName = itemView.findViewById(R.id.tvPetName);
            tvBreed = itemView.findViewById(R.id.tvPetBreed);
            tvAge = itemView.findViewById(R.id.tvPetAge);
        }
    }
    
    // Método para actualizar la lista de forma dinámica
    public void setPets(List<Pet> pets) {
        this.petList = pets;
        notifyDataSetChanged();
    }
}
