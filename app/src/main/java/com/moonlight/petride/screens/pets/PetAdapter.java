package com.moonlight.petride.screens.pets;

import android.content.Intent;
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
import com.moonlight.petride.screens.pets.manage_pet.ManagePetsActivity;
import java.util.List;

/**
 * PetAdapter: Gestiona la lista de mascotas y sus eventos.
 */
public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {

    private List<Pet> petList;

    public PetAdapter(List<Pet> petList) {
        this.petList = petList;
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pet, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        Pet pet = petList.get(position);
        
        holder.tvName.setText(pet.getName());
        holder.tvBreed.setText(pet.getBreed());
        holder.tvAge.setText("Edad: " + pet.getAge() + " años");

        // Imagen Circular
        if (pet.getImagePath() != null && !pet.getImagePath().isEmpty()) {
            try {
                holder.ivPhoto.setImageURI(Uri.parse(pet.getImagePath()));
            } catch (Exception e) {
                holder.ivPhoto.setImageResource(R.drawable.ic_dog);
            }
        } else {
            holder.ivPhoto.setImageResource(R.drawable.ic_dog);
        }

        // EVENTO CLICK: Al tocar el ítem, navegamos a la pantalla de gestión
        holder.itemView.setOnClickListener(v -> {
            // Creamos el Intent hacia ManagePetsActivity
            Intent intent = new Intent(v.getContext(), ManagePetsActivity.class);
            
            // Pasamos el objeto pet completo. 
            // IMPORTANTE: La clase Pet debe implementar Serializable.
            intent.putExtra("PET_DATA", pet);
            
            // Iniciamos la actividad
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return petList != null ? petList.size() : 0;
    }

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
}
