package com.moonlight.petride.screens.rides.history_rides;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moonlight.petride.R;
import com.moonlight.petride.data.RideDAO;
import com.moonlight.petride.data.SessionDAO;

import java.util.ArrayList;
import java.util.List;

public class RidesHistoryActivity extends AppCompatActivity {

    private RecyclerView rvRidesHistory;
    private TextView tvVolverHistorial;
    private RideHistoryAdapter adapter;
    private SessionDAO sessionDAO;
    private RideDAO rideDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rides_history);
        
        vincularVistas();
        sessionDAO = new SessionDAO(this);
        rideDAO = new RideDAO(this);
        configurarRecyclerView();
        configurarBotones();
        cargarDatosDesdeSQLite();
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

    private void cargarDatosDesdeSQLite() {
        long userId = sessionDAO.getLoggedUserId();
        List<RideHistory> rides = new ArrayList<>();

        if (userId != -1) {
            List<RideDAO.RideWithPet> ridesDb = rideDAO.getRidesByUserId(userId);
            for (RideDAO.RideWithPet rideDb : ridesDb) {
                String[] fechaHora = separarFechaHora(rideDb.getDateTime());
                rides.add(new RideHistory(
                        rideDb.getPetName(),
                        fechaHora[0],
                        fechaHora[1],
                        rideDb.getPickupLocation(),
                        rideDb.getRideState(),
                        "3000000000"
                ));
            }
        }

        adapter = new RideHistoryAdapter(rides);
        rvRidesHistory.setAdapter(adapter);
    }

    private String[] separarFechaHora(String fechaHoraCompleta) {
        if (fechaHoraCompleta == null || fechaHoraCompleta.trim().isEmpty()) {
            return new String[]{"", ""};
        }

        String texto = fechaHoraCompleta.trim();
        int espacio = texto.indexOf(" ");
        if (espacio == -1) {
            return new String[]{texto, ""};
        }

        String fecha = texto.substring(0, espacio).trim();
        String hora = texto.substring(espacio + 1).trim();
        return new String[]{fecha, hora};
    }
}