package com.moonlight.petride.screens.calendar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.moonlight.petride.R;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView tvFechaSeleccionada, tvVolverCalendario;
    private Button btnAgregarEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calendar);
        
        vincularVistas();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.calendarView), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void vincularVistas() {
        calendarView = findViewById(R.id.calendarView);
        tvFechaSeleccionada = findViewById(R.id.tvFechaSeleccionada);
        btnAgregarEvento = findViewById(R.id.btnAgregarEvento);
        tvVolverCalendario = findViewById(R.id.tvVolverCalendario);
    }
}