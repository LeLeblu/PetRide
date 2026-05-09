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

import java.io.File;
import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.ViewHolder> {

    private List<Pet> petList;

    public PetAdapter(List<Pet> petList) {
        this.petList = petList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pet pet = petList.get(position);
        holder.tvPetName.setText(pet.getName());
        holder.tvPetBreed.setText("Raza: " + pet.getBreed());
        holder.tvPetAge.setText("Edad: " + pet.getAge() + " años");
        cargarImagenMascota(holder.ivPetPhoto, pet.getImagePath());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ManagePetsActivity.class);
            intent.putExtra("pet_id", pet.getId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPetName, tvPetBreed, tvPetAge;
        ImageView ivPetPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPetName = itemView.findViewById(R.id.tvPetName);
            tvPetBreed = itemView.findViewById(R.id.tvPetBreed);
            tvPetAge = itemView.findViewById(R.id.tvPetAge);
            ivPetPhoto = itemView.findViewById(R.id.ivPetPhoto);
        }
    }

    private void cargarImagenMascota(ImageView imageView, String imagePath) {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            imageView.setImageResource(R.drawable.ic_dog);
            return;
        }

        try {
            if (imagePath.startsWith("content://") || imagePath.startsWith("file://")) {
                imageView.setImageURI(Uri.parse(imagePath));
            } else {
                File file = new File(imagePath);
                if (file.exists()) {
                    imageView.setImageURI(Uri.fromFile(file));
                } else {
                    imageView.setImageResource(R.drawable.ic_dog);
                }
            }
        } catch (Exception e) {
            imageView.setImageResource(R.drawable.ic_dog);
        }
    }
}