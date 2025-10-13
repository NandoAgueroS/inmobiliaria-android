package com.example.inmobiliarialab3.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliarialab3.R;
import com.example.inmobiliarialab3.databinding.FragmentInicioBinding;
import com.example.inmobiliarialab3.model.Propietario;

public class InicioFragment extends Fragment {

    private FragmentInicioBinding binding;

    private InicioViewModel inicioViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        inicioViewModel = new ViewModelProvider(this).get(InicioViewModel.class);

        binding = FragmentInicioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        inicioViewModel.getMPropietario().observe(getViewLifecycleOwner(), new Observer<Propietario>() {
            @Override
            public void onChanged(Propietario propietario) {
                TextView nombre = getActivity().findViewById(R.id.tvHeaderNombre);
                TextView email= getActivity().findViewById(R.id.tvHeaderEmail);

                StringBuilder nombreCompleto = new StringBuilder();
                nombreCompleto.append(propietario.getNombre()).append(" ").append(propietario.getApellido());

                nombre.setText(nombreCompleto.toString());
                email.setText(propietario.getEmail());
            }
        });

        inicioViewModel.obtenerPerfil();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}