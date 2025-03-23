package com.freem.smartpet;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PantallaSensores extends AppCompatActivity {

    private TextView txtTemperatura;
    private TextView txtHumedad;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_sensores);

        txtTemperatura = findViewById(R.id.txtTemperatura);
        txtHumedad = findViewById(R.id.txtHumedad);

        // Inicializa Firebase Auth y Database
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("sensor");

        // Email y password para autenticación
        String email = "christoferriveravalderrama@gmail.com";
        String password = "123456";

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("AuthSuccess", "Usuario autenticado");

                        // Evento de lectura de la base de datos
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // Leer como Double en lugar de String
                                Double estadoTemperatura = snapshot.child("temperatura").getValue(Double.class);
                                Double estadoHumedad = snapshot.child("humedad").getValue(Double.class);

                                // Convertir a String para mostrar
                                txtTemperatura.setText(estadoTemperatura != null ? estadoTemperatura.toString() : "No disponible");
                                txtHumedad.setText(estadoHumedad != null ? estadoHumedad.toString() : "No disponible");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("FirebaseError", "Error al leer datos: " + error.getMessage());
                            }
                        });
                    } else {
                        Log.e("AuthError", "Error al autenticar usuario: " + task.getException());
                    }
                });
    }
}