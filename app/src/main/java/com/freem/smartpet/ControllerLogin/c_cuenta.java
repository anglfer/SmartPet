package com.freem.smartpet.ControllerLogin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.freem.smartpet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class c_cuenta extends AppCompatActivity {

    private static final String TAG = "Antut";
    private FirebaseAuth mAuth;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Button btnRegresar;
    private Button btnAcceder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_cuenta);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnRegresar = findViewById(R.id.btnRegresar);
        btnAcceder = findViewById(R.id.btnAcceder);
        mAuth = FirebaseAuth.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null) {
                    startActivity(new Intent(c_cuenta.this, prueba.class)); // cuando vuelvan los datso correctos me llevaraa esa pantalla
                } else{
                    //  Toast.makeText(c_cuenta.this, "Datos incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        };



        btnRegresar.setOnClickListener(v -> {
            Intent intent = new Intent(c_cuenta.this, MainActivity.class);
            startActivity(intent);
        });

        btnAcceder.setOnClickListener(view -> LogearUsuario());

    }

    //cada vez q inicie la aplicacion verifica si esta  o no logeado el usuario, si esta logeado
    // nos llleva directamente a la otra pantalla

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void LogearUsuario() {

        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(c_cuenta.this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d(TAG, "createUserWithEmail:onComplete" + task.isSuccessful());
                        if (task.isSuccessful()) {
                            Toast.makeText(c_cuenta.this, "Iniciando sesión.....",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(c_cuenta.this, "Datos incorrectos vuelva ingresa" ,
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


}
