package com.example.inmobiliarialab3.ui.contratos;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inmobiliarialab3.R;
import com.example.inmobiliarialab3.model.Inmueble;
import com.example.inmobiliarialab3.request.ApiClient;

import java.util.List;

public class InmuebleContratoAdapter extends RecyclerView.Adapter<InmuebleContratoAdapter.InmuebleViewHolder> {

    private List<Inmueble> lista;
    private Context context;

    public InmuebleContratoAdapter(List<Inmueble> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public InmuebleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inmuebles_inquilinos, parent, false);
        return new InmuebleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InmuebleViewHolder holder, int position) {

        Inmueble inmueble = lista.get(position);
        holder.tvDireccion.setText(inmueble.getDireccion());
        Glide.with(context)
                .load(ApiClient.URL_BASE + inmueble.getImagen())
                .placeholder(R.drawable.ic_login)
                .error(R.drawable.ic_login)
                .into(holder.ivImagen);
        holder.btVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("idInmueble", inmueble.getIdInmueble());
                Navigation.findNavController((Activity) v.getContext(), R.id.nav_host_fragment_content_main).navigate(R.id.detalle_contrato_fragment, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class InmuebleViewHolder extends RecyclerView.ViewHolder{
        TextView tvDireccion;
        ImageView ivImagen;
        Button btVer;

        public InmuebleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDireccion = itemView.findViewById(R.id.tvInquilinosDireccion);
            ivImagen= itemView.findViewById(R.id.ivInquilinosImagen);
            btVer = itemView.findViewById(R.id.btVerInquilino);
        }
    }
}
