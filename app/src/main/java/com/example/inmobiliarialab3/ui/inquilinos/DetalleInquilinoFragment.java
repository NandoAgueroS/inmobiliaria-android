package com.example.inmobiliarialab3.ui.inquilinos;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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