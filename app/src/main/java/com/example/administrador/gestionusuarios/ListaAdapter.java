package com.example.administrador.gestionusuarios;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ViewHolder> {
    private Context ctx;
    private List<String> mList;

    public ListaAdapter(Context ctx,List<String> list) {
        this.ctx = ctx;
        this.mList = list;
        if (list != null) mList.addAll(list);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;
        Context ctx;
        private List<String> mList = new ArrayList<>();
        public ViewHolder(View v, Context ctx,List<String> mList) {
            super(v);
            v.setOnClickListener(this);
            this.ctx = ctx;
            this.mList=mList;
            textView = (TextView) v.findViewById(R.id.zona);

        }

        @Override
        public void onClick(View view) {

        }
    }

    @Override
    public ListaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_view, parent, false);
        return new ListaAdapter.ViewHolder(view,ctx,mList);
    }

    @Override
    public void onBindViewHolder(ListaAdapter.ViewHolder holder, int position) {
        holder.textView.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
