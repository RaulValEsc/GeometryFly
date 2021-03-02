package com.example.geometryfly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.geometryfly.adapter.UsuariosAdapter;
import com.example.geometryfly.modelo.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivityUsuarios extends AppCompatActivity {

    RecyclerView recyclerView;

    ArrayList<Usuario> listaUsuario;

    DatabaseReference baseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        recyclerView = findViewById(R.id.recyclerViewUsuarios);

        listaUsuario = new ArrayList<>();

        baseDate = FirebaseDatabase.getInstance().getReference().child("usuarios");

        baseDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaUsuario.clear();
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                recyclerView.setAdapter(new UsuariosAdapter(listaUsuario, getApplicationContext()));
                for (DataSnapshot child: snapshot.getChildren()) {
                    Usuario u = child.getValue(Usuario.class);
                    listaUsuario.add(u);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
                    ordenarArray();
                    recyclerView.setAdapter(new UsuariosAdapter(listaUsuario, getApplicationContext()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void ordenarArray() {
        int i, j, aux;
        String auxN;
        for (i = 0; i < listaUsuario.size() - 1; i++) {
            for (j = 0; j < listaUsuario.size() - i - 1; j++) {
                if (listaUsuario.get(j + 1).getPuntos() > listaUsuario.get(j).getPuntos()) {
                    aux = listaUsuario.get(j + 1).getPuntos();
                    auxN = listaUsuario.get(j + 1).getNombre();

                    listaUsuario.get(j + 1).setPuntos(listaUsuario.get(j).getPuntos());
                    listaUsuario.get(j + 1).setNombre(listaUsuario.get(j).getNombre());

                    listaUsuario.get(j).setPuntos(aux);
                    listaUsuario.get(j).setNombre(auxN);
                }
            }
        }
    }
}