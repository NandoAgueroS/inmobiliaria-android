package com.example.inmobiliarialab3.ui.contratos;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliarialab3.R;
import com.example.inmobiliarialab3.databinding.FragmentPagosBinding;
import com.example.inmobiliarialab3.model.Pago;
import com.example.inmobiliarialab3.ui.login.LoginActivity;

import java.util.List;

public class PagosFragment extends Fragment {

    private PagosViewModel mViewModel;
    private FragmentPagosBinding binding;

    public static PagosFragment newInstance() {
        return new PagosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(PagosViewModel.class);
        binding = FragmentPagosBinding.inflate(inflater, container, false);

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

        mViewModel.getmPagos().observe(getViewLifecycleOwner(), new Observer<List<Pago>>() {
            @Override
            public void onChanged(List<Pago> pagos) {
                PagosAdapter adapter = new PagosAdapter(pagos, getContext());
                GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
                binding.rvPagos.setLayoutManager(layoutManager);
                binding.rvPagos.setAdapter(adapter);
            }
        });
        mViewModel.cargarPagos(getArguments());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

}