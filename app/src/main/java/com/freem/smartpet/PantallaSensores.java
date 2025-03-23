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
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_sensores);

        txtTemperatura = findViewById(R.id.txtTemperatura);
        txtHumedad = findViewById(R.id.txtHumedad);

        // Inicializa Firebase Auth y Database
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("sensor/registros");

        // Email y password para autenticación
        String email = "christoferriveravalderrama@gmail.com";
        String password = "123456";

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("AuthSuccess", "Usuario autenticado");

                        // Evento de lectura de la base de datos
                        valueEventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // Asegúrate de que haya registros disponibles
                                if (snapshot.exists()) {
                                    // Iterar sobre los registros y obtener el último
                                    for (DataSnapshot registro : snapshot.getChildren()) {
                                        Double estadoTemperatura = registro.child("temperatura").getValue(Double.class);
                                        Double estadoHumedad = registro.child("humedad").getValue(Double.class);

                                        if (estadoTemperatura != null && estadoHumedad != null) {
                                            // Actuliza la UI con los últimos valores
                                            txtTemperatura.setText(estadoTemperatura.toString());
                                            txtHumedad.setText(estadoHumedad.toString());
                                        }
                                    }
                                } else {
                                    txtTemperatura.setText("No disponible");
                                    txtHumedad.setText("No disponible");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("FirebaseError", "Error al leer datos: " + error.getMessage());
                            }
                        };

                        myRef.addValueEventListener(valueEventListener);
                    } else {
                        Log.e("AuthError", "Error al autenticar usuario: " + task.getException());
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Desuscribirse del ValueEventListener
        if (mAuth.getCurrentUser() != null && valueEventListener != null) {
            myRef.removeEventListener(valueEventListener);
        }
    }
}