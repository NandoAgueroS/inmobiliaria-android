package com.example.inmobiliarialab3.ui.inmuebles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inmobiliarialab3.R;
import com.example.inmobiliarialab3.model.Inmueble;

import java.util.List;

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapter.InmuebleViewHolder> {

    private List<Inmueble> lista;
    private Context context;

    public InmuebleAdapter(List<Inmueble> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public InmuebleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new InmuebleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InmuebleViewHolder holder, int position) {

        String urlBase = "";
        Inmueble inmueble = lista.get(position);
        holder.tvDireccion.setText(inmueble.getDireccion());
        holder.tvPrecio.setText(String.valueOf(inmueble.getValor()));
        Glide.with(context)
                .load(urlBase + inmueble.getImagen())
                .placeholder(R.drawable.ic_login)
                .error(R.drawable.ic_login)
                .into(holder.ivImagen);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class InmuebleViewHolder extends RecyclerView.ViewHolder{
        TextView tvDireccion, tvPrecio;
        ImageView ivImagen;

        public InmuebleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvPrecio= itemView.findViewById(R.id.tvPrecio);
            ivImagen= itemView.findViewById(R.id.ivImagen);
        }
    }
}
