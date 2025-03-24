package com.freem.smartpet.ControllerLogin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.freem.smartpet.R;
import com.google.firebase.auth.FirebaseAuth;

public class prueba extends AppCompatActivity {
    Button btnRegresarp;
    Button btnCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prueba);


        btnRegresarp = findViewById(R.id.btnRegresarp);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);


        btnCerrarSesion.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(prueba.this, MainActivity.class));
        });

        btnRegresarp.setOnClickListener(v -> {
            Intent intent = new Intent(prueba.this, MainActivity.class);
            startActivity(intent);
        });
    }



}