package com.example.inmobiliarialab3.ui.contratos;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliarialab3.R;
import com.example.inmobiliarialab3.databinding.FragmentContratosBinding;
import com.example.inmobiliarialab3.databinding.FragmentDetalleContratoBinding;
import com.example.inmobiliarialab3.model.Contrato;

public class DetalleContratoFragment extends Fragment {

    private DetalleContratoViewModel mViewModel;
private FragmentDetalleContratoBinding binding;
    public static DetalleContratoFragment newInstance() {
        return new DetalleContratoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(DetalleContratoViewModel.class);
        binding = FragmentDetalleContratoBinding.inflate(inflater, container, false);

        mViewModel.getmContrato().observe(getViewLifecycleOwner(), new Observer<Contrato>() {
            @Override
            public void onChanged(Contrato contrato) {
                binding.tvContratoFechaInicio.setText(contrato.getFechaInicio());
                binding.tvContratoFechaFin.setText(contrato.getFechaFinalizacion());
                binding.tvContratoMonto.setText(contrato.getMontoAlquiler() + "");
                binding.tvContratoInquilino.setText(contrato.getInquilino().getNombre() + " " + contrato.getInquilino().getApellido());
                binding.tvContratoInmueble.setText(contrato.getInmueble().getDireccion());
                binding.tvContratoCodigo.setText(contrato.getIdContrato() + "");
            }
        });
        mViewModel.mostrarContrato(getArguments());

        binding.btContratoPagos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idContrato = Integer.parseInt(binding.tvContratoCodigo.getText().toString());
                Bundle bundle = new Bundle();
                bundle.putInt("idContrato", idContrato);
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.pagos_fragment, bundle);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

}