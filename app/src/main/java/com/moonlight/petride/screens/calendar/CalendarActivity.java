package com.moonlight.petride.screens.calendar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.moonlight.petride.R;

/**
 * CalendarActivity: Pantalla que muestra el calendario y los datos del paseo recibido.
 */
public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView tvFechaSeleccionada, tvVolverCalendario;
    private Button btnAgregarEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        
        vincularVistas();
        recibirDatosPaseo();
        configurarEventos();
    }

    private void vincularVistas() {
        calendarView = findViewById(R.id.calendarView);
        tvFechaSeleccionada = findViewById(R.id.tvFechaSeleccionada);
        btnAgregarEvento = findViewById(R.id.btnAgregarEvento);
        tvVolverCalendario = findViewById(R.id.tvVolverCalendario);
    }

    /**
     * Recibe la fecha y hora enviadas desde RidesActivity.
     */
    private void recibirDatosPaseo() {
        if (getIntent() != null && getIntent().hasExtra("FECHA_PASEO")) {
            String fecha = getIntent().getStringExtra("FECHA_PASEO");
            String hora = getIntent().getStringExtra("HORA_PASEO");

            // Mostramos los datos en el TextView de la pantalla
            String mensaje = "Próximo paseo: " + fecha + " a las " + hora;
            tvFechaSeleccionada.setText(mensaje);
            
            Toast.makeText(this, "Paseo recibido con éxito", Toast.LENGTH_SHORT).show();
        }
    }

    private void configurarEventos() {
        tvVolverCalendario.setOnClickListener(v -> finish());
        
        // Listener del calendario para cuando el usuario toca un día
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String fechaManual = dayOfMonth + "/" + (month + 1) + "/" + year;
            tvFechaSeleccionada.setText("Día seleccionado: " + fechaManual);
        });
    }
}
