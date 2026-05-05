package com.moonlight.petride.screens.rides.history_rides;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.moonlight.petride.R;

import java.util.List;

// Adaptador para mostrar la lista de paseos y conectarlo con la interfaz

public class RideHistoryAdapter extends RecyclerView.Adapter<RideHistoryAdapter.ViewHolder> {

    // aqui se guarda la lista de paseos
    private final List<RideHistory> rides;

    public RideHistoryAdapter(List<RideHistory> rides) {
        this.rides = rides;
    }

    // Crea una nueva vista con oncreateviewholder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ride_history, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    // toma el objeto de la lista y lo coloca en la vista
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RideHistory ride = rides.get(position);
        
        holder.tvMascota.setText("🐶 " + ride.getPetName());
        holder.tvFecha.setText("📅 " + ride.getDate());
        holder.tvHora.setText("⏰ " + ride.getTime());
        holder.tvDireccion.setText("📍 " + ride.getLocation());
        holder.tvEstado.setText("Estado: " + ride.getStatus());

        holder.btnContactarse.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:123456789"));
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return rides.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMascota, tvFecha, tvHora, tvDireccion, tvEstado;
        Button btnContactarse;

        ViewHolder(View v) {
            super(v);
            tvMascota = v.findViewById(R.id.tvMascotaHistorial);
            tvFecha = v.findViewById(R.id.tvFechaHistorial);
            tvHora = v.findViewById(R.id.tvHoraHistorial);
            tvDireccion = v.findViewById(R.id.tvDireccionHistorial);
            tvEstado = v.findViewById(R.id.tvEstadoHistorial);
            btnContactarse = v.findViewById(R.id.btnContactarse);
        }
    }
}
