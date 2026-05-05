package com.moonlight.petride.screens.rides.history_rides;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.moonlight.petride.R;
import java.util.ArrayList;
import java.util.List;

/**
 * RidesHistoryActivity: Pantalla que muestra el historial de paseos.
 */
public class RidesHistoryActivity extends AppCompatActivity {

    private RecyclerView rvRidesHistory;
    private TextView tvVolverHistorial;
    private RideHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rides_history);
        
        vincularVistas();
        configurarRecyclerView();
        configurarBotones();
        cargarDatosSimulados();
    }

    private void vincularVistas() {
        rvRidesHistory = findViewById(R.id.rvRidesHistory);
        tvVolverHistorial = findViewById(R.id.tvVolverHistorial);
    }

    private void configurarRecyclerView() {
        rvRidesHistory.setLayoutManager(new LinearLayoutManager(this));
    }

    private void configurarBotones() {
        tvVolverHistorial.setOnClickListener(v -> finish());
    }

    private void cargarDatosSimulados() {
        List<RideHistory> rides = new ArrayList<>();
        rides.add(new RideHistory("Firulais", "2023-10-25", "10:00 AM", "Parque Central", "Completado"));
        rides.add(new RideHistory("Rex", "2023-10-26", "02:00 PM", "Plaza de la Paz", "Pendiente"));
        
        adapter = new RideHistoryAdapter(rides);
        rvRidesHistory.setAdapter(adapter);
    }
}
