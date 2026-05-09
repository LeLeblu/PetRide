package com.moonlight.petride;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.moonlight.petride.data.SessionDAO;
import com.moonlight.petride.screens.authentication.login.LoginActivity;
import com.moonlight.petride.screens.home.HomeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionDAO sessionDAO = new SessionDAO(this);
        long loggedUserId = sessionDAO.getLoggedUserId();

        // Punto de entrada unico de la app: redirige segun sesion activa.
        Intent intent;
        if (loggedUserId != -1) {
            intent = new Intent(MainActivity.this, HomeActivity.class);
        } else {
            intent = new Intent(MainActivity.this, LoginActivity.class);
        }

        startActivity(intent);
        finish();
    }
}