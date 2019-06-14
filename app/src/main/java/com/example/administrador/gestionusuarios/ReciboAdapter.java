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

class ReciboAdapter extends RecyclerView.Adapter<ReciboAdapter.ViewHolder>{
    private Context ctx;
    private List<Recibo> recibos;
    private char tipo_clic;

    public ReciboAdapter(Context ctx, List<Recibo> recibos, char tipo_clic){
        this.ctx = ctx;
        this.recibos = recibos;
        this.tipo_clic = tipo_clic;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cv;
        TextView recibo;
        TextView direccion;
        TextView telefono;
        ImageView personPhoto;
        Context ctx;
        List<Recibo> recibos = new ArrayList<Recibo>();

        public ViewHolder(View itemView, Context ctx, List<Recibo> recibos) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.recibos = recibos;
            this.ctx = ctx;
            cv = (CardView)itemView.findViewById(R.id.cv);
            recibo = (TextView)itemView.findViewById(R.id.zona);
            direccion = (TextView)itemView.findViewById(R.id.capacidad);
            telefono = (TextView)itemView.findViewById(R.id.telefono);
            personPhoto = (ImageView)itemView.findViewById(R.id.per_photo);
        }

        @Override
        public void onClick(View v) {
            int i = getAdapterPosition();
            Recibo recibo = this.recibos.get(i);
            if (tipo_clic=='n') {
                Intent reciboi = new Intent(this.ctx, ReciboformActivity.class);
                reciboi.putExtra("recibo", recibo);
                this.ctx.startActivity(reciboi);

            }else if(tipo_clic=='f'){
                /*Intent reciboi = new Intent(this.ctx, FugaCapturaActivity.class);
                reciboi.putExtra("recibo", recibo);
                this.ctx.startActivity(reciboi);*/
            }
            //ctx.finish();
        }
    }

    @Override
    public int getItemCount() {
        return recibos.size();
    }

    @Override
    public ReciboAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recibo_card, parent, false);
        return new ReciboAdapter.ViewHolder(v,ctx,recibos);
    }

    @Override
    public void onBindViewHolder(ReciboAdapter.ViewHolder holder, int position) {
        holder.recibo.setText(recibos.get(position).getFechageneradorecibo()+ " - " + recibos.get(position).getHorarecibo());
        holder.direccion.setText(recibos.get(position).getValornetorecibo());

        //holder.telefono.setText(recibos.get(position).getTel_cli());
    }
}

