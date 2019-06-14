package com.example.administrador.gestionusuarios;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EDIER on 01/09/2017.
 */

class UsuarionuevoAdapter extends RecyclerView.Adapter<UsuarionuevoAdapter.ViewHolder>{
    private Context ctx;
    private List<Usuarionuevo> usuarionuevos;
    private char tipo_clic;

    public UsuarionuevoAdapter(Context ctx, List<Usuarionuevo> usuarionuevos, char tipo_clic){
        this.ctx = ctx;
        this.usuarionuevos = usuarionuevos;
        this.tipo_clic = tipo_clic;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cv;
        TextView usuarionuevo;
        TextView direccion;
        TextView telefono;
        ImageView personPhoto;
        Context ctx;
        List<Usuarionuevo> usuarionuevos = new ArrayList<Usuarionuevo>();

        public ViewHolder(View itemView, Context ctx, List<Usuarionuevo> usuarionuevos) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.usuarionuevos = usuarionuevos;
            this.ctx = ctx;
            cv = (CardView)itemView.findViewById(R.id.cv);
            usuarionuevo = (TextView)itemView.findViewById(R.id.zona);
            direccion = (TextView)itemView.findViewById(R.id.capacidad);
            telefono = (TextView)itemView.findViewById(R.id.telefono);
            personPhoto = (ImageView)itemView.findViewById(R.id.per_photo);
        }

        @Override
        public void onClick(View v) {
            int i = getAdapterPosition();
            Usuarionuevo usuarionuevo = this.usuarionuevos.get(i);
            if (tipo_clic=='n') {
                Intent usuarionuevoi = new Intent(this.ctx, UsuarionuevoformActivity.class);
                usuarionuevoi.putExtra("usuarionuevo", usuarionuevo);
                this.ctx.startActivity(usuarionuevoi);
                /*Intent usuarionuevoi = new Intent(this.ctx, ConsultarCilindroActivity.class);
                usuarionuevoi.putExtra("usuarionuevo", usuarionuevo);
                this.ctx.startActivity(usuarionuevoi);*/
            }else if(tipo_clic=='f'){
                /*Intent usuarionuevoi = new Intent(this.ctx, FugaCapturaActivity.class);
                usuarionuevoi.putExtra("usuarionuevo", usuarionuevo);
                this.ctx.startActivity(usuarionuevoi);*/
            }
            //ctx.finish();
        }
    }

    @Override
    public int getItemCount() {
        return usuarionuevos.size();
    }

    @Override
    public UsuarionuevoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.usuarionuevo_card, parent, false);
        return new UsuarionuevoAdapter.ViewHolder(v,ctx,usuarionuevos);
    }

    @Override
    public void onBindViewHolder(UsuarionuevoAdapter.ViewHolder holder, int position) {
        holder.usuarionuevo.setText(usuarionuevos.get(position).getNombresusuarionuevo());
        holder.direccion.setText(usuarionuevos.get(position).getDireccionusuarionuevo() + " - " + usuarionuevos.get(position).getBarriousuarionuevo());
        holder.telefono.setText(usuarionuevos.get(position).getTelefono1Usuarionuevo());
    }
}

