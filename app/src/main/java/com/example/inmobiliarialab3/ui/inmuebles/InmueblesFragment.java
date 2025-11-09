package com.example.inmobiliarialab3.ui.inmuebles;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inmobiliarialab3.R;
import com.example.inmobiliarialab3.databinding.FragmentInmueblesBinding;
import com.example.inmobiliarialab3.model.Inmueble;
import com.example.inmobiliarialab3.ui.login.LoginActivity;

import java.util.List;

public class InmueblesFragment extends Fragment {

    private FragmentInmueblesBinding binding;
    private InmueblesViewModel inmueblesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        inmueblesViewModel = new ViewModelProvider(this).get(InmueblesViewModel.class);
        binding = FragmentInmueblesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        inmueblesViewModel.getmSesionInvalida().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("desde_sesion_expirada", true);
                startActivity(intent);
            }
        });

        inmueblesViewModel.getmInmuebles().observe(getViewLifecycleOwner(), new Observer<List<Inmueble>>() {
            @Override
            public void onChanged(List<Inmueble> inmuebles) {
                InmuebleAdapter adapter = new InmuebleAdapter(inmuebles, getContext());
                GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
                RecyclerView recyclerView = binding.rvInmuebles;
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(layoutManager);
            }
        });
        inmueblesViewModel.cargarInmuebles();
        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.cargar_inmueble_fragment);
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