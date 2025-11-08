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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inmobiliarialab3.R;
import com.example.inmobiliarialab3.model.Inmueble;
import com.example.inmobiliarialab3.model.Pago;
import com.example.inmobiliarialab3.request.ApiClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PagosAdapter extends RecyclerView.Adapter<PagosAdapter.PagoViewHolder> {

    private List<Pago> lista;
    private Context context;

    public PagosAdapter(List<Pago> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public PagoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pagos, parent, false);
        return new PagoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PagoViewHolder holder, int position) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatterLocal = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Pago pago= lista.get(position);
        LocalDate fechaLocalDate = LocalDate.parse(pago.getFechaPago(), formatter);
        holder.tvCodigo.setText(String.valueOf(pago.getIdPago()));
        holder.tvDetalle.setText(pago.getDetalle());
        holder.tvFecha.setText(fechaLocalDate.format(formatterLocal));
        holder.tvImporte.setText(String.valueOf(pago.getMonto()));
        holder.tvContrato.setText(String.valueOf(pago.getIdContrato()));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class PagoViewHolder extends RecyclerView.ViewHolder{
        TextView tvCodigo, tvImporte, tvDetalle, tvFecha, tvContrato;

        public PagoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCodigo = itemView.findViewById(R.id.tvPagoCodigo);
            tvImporte = itemView.findViewById(R.id.tvPagoImporte);
            tvDetalle = itemView.findViewById(R.id.tvPagoDetalle);
            tvFecha = itemView.findViewById(R.id.tvPagoFecha);
            tvContrato = itemView.findViewById(R.id.tvPagoContrato);
        }
    }
}
