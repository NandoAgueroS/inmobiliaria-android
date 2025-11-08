package com.example.inmobiliarialab3.ui.perfil;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.inmobiliarialab3.R;
import com.example.inmobiliarialab3.databinding.FragmentPerfilBinding;
import com.example.inmobiliarialab3.model.Propietario;
import com.example.inmobiliarialab3.ui.login.LoginActivity;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private PerfilViewModel perfilViewModel;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
                perfilViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);

        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        perfilViewModel.getmSesionInvalida().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        perfilViewModel.getMPropietario().observe(getViewLifecycleOwner(), new Observer<Propietario>() {
            @Override
            public void onChanged(Propietario propietario) {
                binding.etDni.setText(propietario.getDni());
                binding.etApellido.setText(propietario.getApellido());
                binding.etEmail.setText(propietario.getEmail());
                binding.etNombre.setText(propietario.getNombre());
                binding.etTelefono.setText(propietario.getTelefono());
            }
        });
        perfilViewModel.getBmEstado().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.etTelefono.setEnabled(aBoolean);
                binding.etEmail.setEnabled(aBoolean);
                binding.etNombre.setEnabled(aBoolean);
                binding.etApellido.setEnabled(aBoolean);
                binding.etDni.setEnabled(aBoolean);
            }
        });
        perfilViewModel.getBmTexto().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.btEditar.setText(s);
            }
        });
        perfilViewModel.mostrarPerfil();
        perfilViewModel.getmMensaje().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.tvPerfilError.setText(s);
            }
        });

        binding.btEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = binding.etNombre.getText().toString();
                String apellido = binding.etApellido.getText().toString();
                String dni = binding.etDni.getText().toString();
                String email = binding.etEmail.getText().toString();
                String telefono = binding.etTelefono.getText().toString();
                String btTexto = binding.btEditar.getText().toString();
                perfilViewModel.guardar(btTexto, nombre, apellido, dni, email, telefono);
            }
        });

        binding.btPerfilCambiarContrasenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.cambiar_contrasenia_fragment);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}