package com.example.geometryfly.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geometryfly.R;
import com.example.geometryfly.modelo.Usuario;

import java.util.ArrayList;

public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.LeardBoardViewHolder>{

    Context context;
    ArrayList<Usuario> listaUsuarios;

    public UsuariosAdapter(ArrayList<Usuario> listaComercios, Context context) {
        this.listaUsuarios= listaComercios;
        this.context = context;
    }

    @NonNull
    @Override
    public LeardBoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_usuario, parent, false);
        return new LeardBoardViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LeardBoardViewHolder holder, int position) {
        Usuario usuario = listaUsuarios.get(position);
        holder.posicion.setText(Integer.toString(position+1));
        holder.nombre.setText(usuario.getNombre());
        holder.puntos.setText(Integer.toString(usuario.getPuntos()));

    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public static class LeardBoardViewHolder extends RecyclerView.ViewHolder{

        TextView nombre;
        TextView puntos;
        TextView posicion;

        public LeardBoardViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvUsuario);
            puntos = itemView.findViewById(R.id.tvPuntos);
            posicion = itemView.findViewById(R.id.tvNumero);
        }
    }
}

