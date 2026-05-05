package com.moonlight.petride.screens.rides;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.moonlight.petride.R;
import com.moonlight.petride.data.model.Pet;
import com.moonlight.petride.screens.rides.history_rides.RidesHistoryActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Pantalla para solicitar un paseo.
 * Datos simulados para mantener el flujo de la interfaz.
 */
public class RidesActivity extends AppCompatActivity {

    private Spinner spMascotas;
    private EditText etFecha, etHora, etDireccionPaseo;
    private Button btnConfirmarPaseo, btnMisPaseos;

    // Lista simulada de mascotas para el Spinner
    private List<Pet> listaMascotas;

    private int anio, mes, dia, hora, minuto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rides);
        
        vincularVistas();
        
        // Datos simulados para datos de prueba
        poblarSpinnerMascotasSimulado();
        
        configurarSeleccionadores();
        configurarBotonConfirmar();
        configurarBotonMisPaseos();
    }

    private void vincularVistas() {
        spMascotas = findViewById(R.id.spMascotas);
        etFecha = findViewById(R.id.etFecha);
        etHora = findViewById(R.id.etHora);
        etDireccionPaseo = findViewById(R.id.etDireccionPaseo);
        btnConfirmarPaseo = findViewById(R.id.btnConfirmarPaseo);
        btnMisPaseos = findViewById(R.id.btnMisPaseos);
    }

    /**
     * Lista estatica de mascotas simuladas.
     */
    private void poblarSpinnerMascotasSimulado() {
        listaMascotas = new ArrayList<>();
        
        // Agregamos mascotas de ejemplo para que la UI sea funcional
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
        
        // Aseguramos que el botón esté habilitado en el modo diseño
        btnConfirmarPaseo.setEnabled(true);
    }

    private void configurarSeleccionadores() {
        etFecha.setOnClickListener(v -> mostrarDatePicker());
        etHora.setOnClickListener(v -> mostrarTimePicker());
    }

    private void mostrarDatePicker() {
        final Calendar c = Calendar.getInstance();
        anio = c.get(Calendar.YEAR);
        mes = c.get(Calendar.MONTH);
        dia = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
            String fechaSeleccionada = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, (monthOfYear + 1), year);
            etFecha.setText(fechaSeleccionada);
        }, anio, mes, dia);
        datePickerDialog.show();
    }

    private void mostrarTimePicker() {
        final Calendar c = Calendar.getInstance();
        hora = c.get(Calendar.HOUR_OF_DAY);
        minuto = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minuteOfHour) -> {
            String horaSeleccionada = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minuteOfHour);
            etHora.setText(horaSeleccionada);
        }, hora, minuto, true);
        timePickerDialog.show();
    }

    private void configurarBotonConfirmar() {
        btnConfirmarPaseo.setOnClickListener(v -> {
            String fecha = etFecha.getText().toString();
            String horaText = etHora.getText().toString();
            String direccion = etDireccionPaseo.getText().toString();
            
            // Obtenemos el nombre seleccionado del Spinner
            String nombreMascota = spMascotas.getSelectedItem() != null ? 
                    spMascotas.getSelectedItem().toString() : "Mascota";

            if (fecha.isEmpty() || horaText.isEmpty() || direccion.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // NAVEGACIÓN: Mantenemos el flujo hacia el historial pasando los datos por Intent
            Intent intent = new Intent(RidesActivity.this, RidesHistoryActivity.class);
            intent.putExtra("FECHA_PASEO", fecha);
            intent.putExtra("HORA_PASEO", horaText);
            intent.putExtra("DIRECCION_PASEO", direccion);
            intent.putExtra("NOMBRE_MASCOTA", nombreMascota);
            
            startActivity(intent);
        });
    }

    private void configurarBotonMisPaseos() {
        btnMisPaseos.setOnClickListener(v -> {
            Intent intent = new Intent(RidesActivity.this, RidesHistoryActivity.class);
            startActivity(intent);
        });
    }
}
