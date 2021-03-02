package com.example.geometryfly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.geometryfly.modelo.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText etNombre;
    Button bPlay,bLaderboard;

    DatabaseReference database;

    boolean existe=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etNombre = findViewById(R.id.etNombre);
        bPlay = findViewById(R.id.bEmpezar);
        bLaderboard = findViewById(R.id.bLaderboard);

        setup();
    }

    private void setup() {
        bPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etNombre.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Debes introducir un nombre para jugar", Toast.LENGTH_LONG).show();
                }else{
                    database = FirebaseDatabase.getInstance().getReference().child("usuarios");
                    database.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!existe) {
                                for (DataSnapshot child : snapshot.getChildren()) {
                                    if (child.child("nombre").getValue().toString().equals(etNombre.getText().toString())) {
                                        existe = true;
                                    }
                                }
                                if (!existe) {
                                    Usuario newUsuario = new Usuario(etNombre.getText().toString());
                                    database = FirebaseDatabase.getInstance().getReference().child("usuarios");
                                    existe = true;
                                    database.push().setValue(newUsuario);
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.putExtra("nombre", newUsuario.getNombre());
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Ese nombre ya está registrado", Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        bLaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ActivityUsuarios.class);
                startActivity(intent);
            }
        });
    }
}