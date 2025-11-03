package com.example.inmobiliarialab3.ui.contratos;

import android.app.Application;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliarialab3.model.Contrato;
import com.example.inmobiliarialab3.model.Inquilino;
import com.example.inmobiliarialab3.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleContratoViewModel extends AndroidViewModel {
    private MutableLiveData<Contrato> mContrato = new MutableLiveData<>();

    public DetalleContratoViewModel(@NonNull Application application) {
        super(application);
    }
    // TODO: Implement the ViewModel

    public LiveData<Contrato> getmContrato(){
        return mContrato;
    }

    public void mostrarContrato(Bundle bundle){
        int idInmueble = bundle.getInt("idInmueble");
        if (idInmueble == 0){
            return;
        }

        String token = "Bearer " + ApiClient.leerToken(getApplication());
        ApiClient.InmobiliariaService inmobiliariaService = ApiClient.getInmobiliariaService();

        Call<Contrato> contratoCall = inmobiliariaService.getContratoVigente(token, idInmueble);

        contratoCall.enqueue(new Callback<Contrato>() {
            @Override
            public void onResponse(Call<Contrato> call, Response<Contrato> response) {
                if (response.isSuccessful() && response.body() != null){
                    mContrato.postValue(response.body());
                }else{
                    Toast.makeText(getApplication(), "Error al recuperar el contrato", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Contrato> call, Throwable t) {
                Toast.makeText(getApplication(), "Error en el servidor", Toast.LENGTH_LONG).show();
            }
        });
    }
}