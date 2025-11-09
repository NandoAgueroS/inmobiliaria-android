package com.example.inmobiliarialab3.ui.inquilinos;

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

import com.example.inmobiliarialab3.R;
import com.example.inmobiliarialab3.databinding.FragmentDetalleInquilinoBinding;
import com.example.inmobiliarialab3.model.Inquilino;
import com.example.inmobiliarialab3.ui.login.LoginActivity;

public class DetalleInquilinoFragment extends Fragment {

    private DetalleInquilinoViewModel mViewModel;

    private FragmentDetalleInquilinoBinding binding;
    public static DetalleInquilinoFragment newInstance() {
        return new DetalleInquilinoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
         binding =  FragmentDetalleInquilinoBinding.inflate(inflater, container, false);
         mViewModel = new ViewModelProvider(this).get(DetalleInquilinoViewModel.class);

        mViewModel.getmSesionInvalida().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("desde_sesion_expirada", true);
                startActivity(intent);
            }
        });

        mViewModel.getmInquilino().observe(getViewLifecycleOwner(), new Observer<Inquilino>() {
             @Override
             public void onChanged(Inquilino inquilino) {
                 binding.tvInquilinoNombre.setText(inquilino.getNombre());
                 binding.tvInquilinoApellido.setText(inquilino.getApellido());
                 binding.tvInquilinoDni.setText(inquilino.getDni());
                 binding.tvInquilinoEmail.setText(inquilino.getEmail());
                 binding.tvInquilinoTelefono.setText(inquilino.getTelefono());
             }
         });

         mViewModel.mostrarInquilino(getArguments());
         return binding.getRoot();
    }

}