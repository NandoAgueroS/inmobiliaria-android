package com.example.inmobiliarialab3.ui.perfil;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliarialab3.R;
import com.example.inmobiliarialab3.databinding.FragmentCambiarContraseniaBinding;
import com.example.inmobiliarialab3.ui.login.LoginActivity;

public class CambiarContraseniaFragment extends Fragment {

    private CambiarContraseniaViewModel mViewModel;
    private FragmentCambiarContraseniaBinding binding;

    public static CambiarContraseniaFragment newInstance() {
        return new CambiarContraseniaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(CambiarContraseniaViewModel.class);
        binding = FragmentCambiarContraseniaBinding.inflate(inflater, container, false);

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

        mViewModel.getmMensaje().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.tvContraseniaMensaje.setText(s);
            }
        });
        mViewModel.getmGuardado().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.nav_perfil);
            }
        });
        binding.btCambiarContrasenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.cambiarContrasenia(
                        binding.etContraseniaActual.getText().toString(),
                        binding.etContraseniaNueva.getText().toString(),
                        binding.etContraseniaConfirmada.getText().toString()
                );
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}