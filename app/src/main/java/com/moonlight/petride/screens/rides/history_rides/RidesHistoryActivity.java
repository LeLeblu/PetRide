package com.moonlight.petride.screens.rides.history_rides;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.moonlight.petride.R;

/**
 * RidesHistoryActivity: Pantalla que muestra el historial de paseos.
 * Ahora permite contactar al paseador mediante el marcador telefónico.
 */
public class RidesHistoryActivity extends AppCompatActivity {

    private TextView tvMascotaHistorial;
    private TextView tvFechaHistorial;
    private TextView tvHoraHistorial;
    private TextView tvDireccionHistorial;
    private TextView tvEstadoHistorial;
    private TextView tvVolverHistorial;
    private LinearLayout containerUltimoPaseo;
    private Button btnContactarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rides_history);
        
        vincularVistas();
        recibirDatosPaseo();
        configurarBotones();
    }

    private void vincularVistas() {
        tvMascotaHistorial = findViewById(R.id.tvMascotaHistorial);
        tvFechaHistorial = findViewById(R.id.tvFechaHistorial);
        tvHoraHistorial = findViewById(R.id.tvHoraHistorial);
        tvDireccionHistorial = findViewById(R.id.tvDireccionHistorial);
        TextView tvUsuarioHistorial = findViewById(R.id.tvUsuarioHistorial);
        tvEstadoHistorial = findViewById(R.id.tvEstadoHistorial);
        tvVolverHistorial = findViewById(R.id.tvVolverHistorial);
        containerUltimoPaseo = findViewById(R.id.containerUltimoPaseo);
        Button btnVerDetalle = findViewById(R.id.btnVerDetalle);
        btnContactarse = findViewById(R.id.btnContactarse);
    }

    private void recibirDatosPaseo() {
        if (getIntent() != null && getIntent().hasExtra("FECHA_PASEO")) {
            String fecha = getIntent().getStringExtra("FECHA_PASEO");
            String hora = getIntent().getStringExtra("HORA_PASEO");
            String direccion = getIntent().getStringExtra("DIRECCION_PASEO");
            String nombreMascota = getIntent().getStringExtra("NOMBRE_MASCOTA");

            tvMascotaHistorial.setText("🐶 " + nombreMascota);
            tvFechaHistorial.setText("📅 " + fecha);
            tvHoraHistorial.setText("⏰ " + hora);
            tvDireccionHistorial.setText("📍 " + direccion);
            tvEstadoHistorial.setText("Estado: Pendiente");
            
            containerUltimoPaseo.setVisibility(View.VISIBLE);
        }
    }

    private void configurarBotones() {
        tvVolverHistorial.setOnClickListener(v -> finish());

        // INTENT IMPLÍCITO PARA ABRIR EL MARCADOR
        btnContactarse.setOnClickListener(v -> {
            // Número de teléfono del paseador
            String numeroTelefono = "123456789"; 
            
            // Intent para abrir el teléfono
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + numeroTelefono));
            
            startActivity(intent);
        });
    }
}
