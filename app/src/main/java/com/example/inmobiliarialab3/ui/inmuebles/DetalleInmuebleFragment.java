package com.example.inmobiliarialab3.ui.inmuebles;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.inmobiliarialab3.R;
import com.example.inmobiliarialab3.databinding.FragmentDetalleInmuebleBinding;
import com.example.inmobiliarialab3.model.Inmueble;
import com.example.inmobiliarialab3.request.ApiClient;
import com.example.inmobiliarialab3.ui.login.LoginActivity;

public class DetalleInmuebleFragment extends Fragment {

    public static DetalleInmuebleFragment newInstance() {
        return new DetalleInmuebleFragment();
    }

    private DetalleInmuebleViewModel mViewModel;
    private FragmentDetalleInmuebleBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDetalleInmuebleBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(DetalleInmuebleViewModel.class);

        mViewModel.getmSesionInvalida().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        mViewModel.getmInmueble().observe(getViewLifecycleOwner(), new Observer<Inmueble>() {
            @Override
            public void onChanged(Inmueble inmueble) {
                binding.tvIdInmueble.setText(String.valueOf(inmueble.getIdInmueble()));
                binding.tvAmbientesI.setText(String.valueOf(inmueble.getAmbientes()));
                binding.tvDireccionI.setText(inmueble.getDireccion());
                binding.tvUsoI.setText(inmueble.getUso());
                binding.tvLatitudI.setText(String.valueOf(inmueble.getLatitud()));
                binding.tvLongitudI.setText(String.valueOf(inmueble.getLongitud()));
                binding.tvValorI.setText(String.valueOf(inmueble.getValor()));
                binding.checkDisponible.setChecked(inmueble.isDisponible());
                Glide.with(getContext())
                        .load(ApiClient.URL_BASE + inmueble.getImagen())
                        .placeholder(R.drawable.ic_login)
                        .error(R.drawable.ic_login)
                        .into(binding.imgInmueble);
            }
        });
        mViewModel.mostrarInmueble(getArguments());

        binding.checkDisponible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.actualizarInmueble(binding.checkDisponible.isChecked());
            }
        });
        return binding.getRoot();
    }

}