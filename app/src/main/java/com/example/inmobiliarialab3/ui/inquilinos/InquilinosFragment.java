package com.example.inmobiliarialab3.ui.inquilinos;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliarialab3.R;
import com.example.inmobiliarialab3.databinding.FragmentInmueblesBinding;
import com.example.inmobiliarialab3.databinding.FragmentInquilinosBinding;
import com.example.inmobiliarialab3.model.Inmueble;
import com.example.inmobiliarialab3.ui.inmuebles.InmuebleAdapter;
import com.example.inmobiliarialab3.ui.inmuebles.InmueblesViewModel;
import com.example.inmobiliarialab3.ui.login.LoginActivity;

import java.util.List;

public class InquilinosFragment extends Fragment {

    private InquilinosViewModel mViewModel;
    private FragmentInquilinosBinding binding;

    public static InquilinosFragment newInstance() {
        return new InquilinosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel= new ViewModelProvider(this).get(InquilinosViewModel.class);
        binding = FragmentInquilinosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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

        mViewModel.getmInmuebles().observe(getViewLifecycleOwner(), new Observer<List<Inmueble>>() {
            @Override
            public void onChanged(List<Inmueble> inmuebles) {
                InquilinoAdapter adapter = new InquilinoAdapter(inmuebles, getContext());
                GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
                RecyclerView recyclerView = binding.rvInquilinos;
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(layoutManager);            }
        });
        mViewModel.cargarInmuebles();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}