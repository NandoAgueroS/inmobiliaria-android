package com.example.inmobiliarialab3.ui.contratos;

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
import com.example.inmobiliarialab3.databinding.FragmentContratosBinding;
import com.example.inmobiliarialab3.databinding.FragmentDetalleContratoBinding;
import com.example.inmobiliarialab3.model.Contrato;
import com.example.inmobiliarialab3.ui.login.LoginActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

        mViewModel.getmSesionInvalida().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        mViewModel.getmContrato().observe(getViewLifecycleOwner(), new Observer<Contrato>() {
            @Override
            public void onChanged(Contrato contrato) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter formatterLocal = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate fechaInicio = LocalDate.parse(contrato.getFechaInicio(), formatter);
                LocalDate fechaFin= LocalDate.parse(contrato.getFechaFinalizacion(), formatter);
                String fechaInicioString = fechaInicio.format(formatterLocal);
                String fechaFinString = fechaFin.format(formatterLocal);

                binding.tvContratoFechaInicio.setText(fechaInicioString);
                binding.tvContratoFechaFin.setText(fechaFinString);
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