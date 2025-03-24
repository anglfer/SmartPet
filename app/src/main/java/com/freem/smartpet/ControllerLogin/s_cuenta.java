package com.freem.smartpet.ControllerLogin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.freem.smartpet.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class s_cuenta extends AppCompatActivity {

    private Button btnRegresar, btnCrearCuenta;
    private EditText editTextEmail, editTextPassword, editTextNombre, editTextTelefono;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_cuenta);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnRegresar = findViewById(R.id.btnRegresar);
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextTelefono = findViewById(R.id.editTextTelefono);

        btnRegresar.setOnClickListener(v -> {
            Intent intent = new Intent(s_cuenta.this, MainActivity.class);
            startActivity(intent);
        });

        btnCrearCuenta.setOnClickListener(v -> registrarUsuario());
    }

    private void registrarUsuario() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String nombre = editTextNombre.getText().toString().trim();
        String telefono = editTextTelefono.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || nombre.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(s_cuenta.this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }


        Toast.makeText(s_cuenta.this, "Creando cuenta...", Toast.LENGTH_SHORT).show();


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        FirebaseUser user = mAuth.getCurrentUser();
                        guardarDatosUsuario(user.getUid(), nombre, email, telefono);
                    } else {

                        Toast.makeText(s_cuenta.this, "Error al registrar: Cuenta de gmail erroneo o contraseñana mayor a 6 digitos ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void guardarDatosUsuario(String userId, String nombre, String email, String telefono) {
        // Crear un mapa con los datos del usuario
        Map<String, Object> userData = new HashMap<>();
        userData.put("nombre", nombre);
        userData.put("email", email);
        userData.put("telefono", telefono);
        userData.put("fechaRegistro", System.currentTimeMillis());

        // Guardar en la base de datos
        mDatabase.child("usuarios").child(userId).setValue(userData)
                .addOnSuccessListener(aVoid -> {
                    Log.d("FIREBASE", "Datos guardados exitosamente para UID: " + userId);
                    Toast.makeText(s_cuenta.this, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show();

                    // Redirigir al usuario a la pantalla "prueba"
                    Intent intent = new Intent(s_cuenta.this, prueba.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Contraseña creada exitosamente", Toast.LENGTH_SHORT).show();
                });
    }
}