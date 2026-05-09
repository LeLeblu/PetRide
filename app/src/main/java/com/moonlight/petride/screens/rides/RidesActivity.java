package com.moonlight.petride.screens.rides;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.moonlight.petride.R;
import com.moonlight.petride.data.model.Pet;
import com.moonlight.petride.screens.rides.history_rides.RidesHistoryActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RidesActivity extends AppCompatActivity {

    private Spinner spMascotas;
    private EditText etFecha, etHora, etDireccionPaseo;
    private Button btnConfirmarPaseo, btnMisPaseos;
    private FrameLayout flMapaIndicador;
    private TextView tvVolverHome;
    private List<Pet> listaMascotas;
    private LocationManager locationManager;

    // Solicita permiso de ubicación cuando el usuario lo necesita.
    private final ActivityResultLauncher<String> requestLocationPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    obtenerUbicacionActual();
                } else {
                    Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rides);
        
        vincularVistas();
        configurarSelectoresFechaHora();
        poblarSpinnerMascotas();
        configurarNavegacion();
    }

    private void vincularVistas() {
        spMascotas = findViewById(R.id.spMascotas);
        etFecha = findViewById(R.id.etFecha);
        etHora = findViewById(R.id.etHora);
        etDireccionPaseo = findViewById(R.id.etDireccionPaseo);
        btnConfirmarPaseo = findViewById(R.id.btnConfirmarPaseo);
        btnMisPaseos = findViewById(R.id.btnMisPaseos);
        flMapaIndicador = findViewById(R.id.flMapaIndicador);
        tvVolverHome = findViewById(R.id.tvVolverHome);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    private void configurarSelectoresFechaHora() {
        // Abre el selector de fecha y carga el valor elegido en el campo.
        etFecha.setOnClickListener(v -> {
            Calendar calendario = Calendar.getInstance();
            int anioActual = calendario.get(Calendar.YEAR);
            int mesActual = calendario.get(Calendar.MONTH);
            int diaActual = calendario.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    RidesActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        String fechaFormateada = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
                        etFecha.setText(fechaFormateada);
                    },
                    anioActual,
                    mesActual,
                    diaActual
            );
            datePickerDialog.show();
        });

        // Abre el selector de hora y carga el valor elegido en el campo.
        etHora.setOnClickListener(v -> {
            Calendar calendario = Calendar.getInstance();
            int horaActual = calendario.get(Calendar.HOUR_OF_DAY);
            int minutoActual = calendario.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    RidesActivity.this,
                    (view, hourOfDay, minute) -> {
                        String minutoConCero = minute < 10 ? "0" + minute : String.valueOf(minute);
                        String horaFormateada = hourOfDay + ":" + minutoConCero;
                        etHora.setText(horaFormateada);
                    },
                    horaActual,
                    minutoActual,
                    true
            );
            timePickerDialog.show();
        });
    }

    private void poblarSpinnerMascotas() {
        listaMascotas = new ArrayList<>();
        listaMascotas.add(new Pet(1, 1, "Firulais", "Golden Retriever", 3, "", ""));
        listaMascotas.add(new Pet(2, 1, "Rex", "Pastor Alemán", 5, "", ""));

        List<String> nombresMascotas = new ArrayList<>();
        for (Pet p : listaMascotas) {
            nombresMascotas.add(p.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
                android.R.layout.simple_spinner_item, nombresMascotas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMascotas.setAdapter(adapter);
    }

    private void configurarNavegacion() {
        btnConfirmarPaseo.setOnClickListener(v -> {
            if (!validarCamposPaseo()) {
                return;
            }
            Intent intent = new Intent(RidesActivity.this, RidesHistoryActivity.class);
            startActivity(intent);
        });

        btnMisPaseos.setOnClickListener(v -> {
            Intent intent = new Intent(RidesActivity.this, RidesHistoryActivity.class);
            startActivity(intent);
        });

        // El mapa funciona como acceso rápido para cargar coordenadas actuales.
        flMapaIndicador.setOnClickListener(v -> obtenerUbicacionActual());

        tvVolverHome.setOnClickListener(v -> {
            finish();
        });
    }

    private void obtenerUbicacionActual() {
        // Verifica permiso nativo de ubicación.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            return;
        }

        String provider = null;
        // Prioriza GPS y usa red como respaldo.
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        }

        if (provider == null) {
            Toast.makeText(this, "Activa GPS o red para obtener ubicación", Toast.LENGTH_SHORT).show();
            return;
        }

        Location lastLocation = locationManager.getLastKnownLocation(provider);
        if (lastLocation != null) {
            colocarCoordenadasEnCampo(lastLocation);
            return;
        }

        // Si no hay última ubicación, pide una actualización simple.
        locationManager.requestSingleUpdate(provider, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                colocarCoordenadasEnCampo(location);
            }
        }, Looper.getMainLooper());
    }

    private void colocarCoordenadasEnCampo(Location location) {
        String coordenadas = location.getLatitude() + ", " + location.getLongitude();
        etDireccionPaseo.setText(coordenadas);
        Toast.makeText(this, "Ubicación agregada", Toast.LENGTH_SHORT).show();
    }

    private boolean validarCamposPaseo() {
        String fecha = etFecha.getText().toString().trim();
        String direccion = etDireccionPaseo.getText().toString().trim();

        if (listaMascotas == null || listaMascotas.isEmpty()) {
            Toast.makeText(this, "Debes tener al menos una mascota registrada", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (spMascotas.getSelectedItemPosition() < 0) {
            Toast.makeText(this, "Selecciona una mascota", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (fecha.isEmpty()) {
            etFecha.setError("La fecha es obligatoria");
            etFecha.requestFocus();
            return false;
        }

        if (direccion.isEmpty()) {
            etDireccionPaseo.setError("La dirección es obligatoria");
            etDireccionPaseo.requestFocus();
            return false;
        }

        String estadoInicial = "Pendiente";
        if (estadoInicial.trim().isEmpty()) {
            Toast.makeText(this, "No se pudo definir el estado del paseo", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}