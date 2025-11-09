package com.example.inmobiliarialab3.ui.inmuebles;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliarialab3.R;
import com.example.inmobiliarialab3.databinding.FragmentCargarInmuebleBinding;
import com.example.inmobiliarialab3.ui.login.LoginActivity;

public class CargarInmuebleFragment extends Fragment {

    private CargarInmuebleViewModel mViewModel;
    private FragmentCargarInmuebleBinding binding;
    private ActivityResultLauncher<Intent> arl;
    private Intent intent;

    public static CargarInmuebleFragment newInstance() {
        return new CargarInmuebleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCargarInmuebleBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(CargarInmuebleViewModel.class);

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

        abrirGaleria();
        binding.btnCargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arl.launch(intent);
            }
        });
        mViewModel.getmUri().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                binding.ivCargarImagen.setImageURI(uri);
            }
        });
        mViewModel.getmMensaje().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.tvCargarMensaje.setText(s);
            }
        });
        mViewModel.getmGuardado().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.nav_inmuebles);
            }
        });
        binding.btCargarInmueble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.cargarInmueble(
                        binding.etCargarDireccion.getText().toString(),
                        binding.etCargarValor.getText().toString(),
                        binding.etCargarTipo.getText().toString(),
                        binding.etCargarUso.getText().toString(),
                        binding.etCargarAmbientes.getText().toString(),
                        binding.etCargarSuperficie.getText().toString(),
                        binding.cbCargarDisponible.isChecked(),
                        binding.etCargarLatitud.getText().toString(),
                        binding.etCargarLongitud.getText().toString()
                );
            }
        });
        return binding.getRoot();
    }

    private void abrirGaleria() {
        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        arl = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                //Log.d("AgregarInmuebleFragment", "Result: " + result);
                mViewModel.recibirFoto(result);

            }
        });
    }
}