package com.example.inmobiliarialab3.ui.inquilinos;

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

public class DetalleInquilinoViewModel extends AndroidViewModel {

    private MutableLiveData<Inquilino> mInquilino = new MutableLiveData<>();

    public DetalleInquilinoViewModel(@NonNull Application application) {
        super(application);
    }
    // TODO: Implement the ViewModel

    public LiveData<Inquilino> getmInquilino(){
        return mInquilino;
    }

    public void mostrarInquilino(Bundle bundle){
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
                if (response.isSuccessful() && response.body().getInquilino() != null){
                    mInquilino.postValue(response.body().getInquilino());
                }else{
                    Toast.makeText(getApplication(), "Error al recuperar el inquilino", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Contrato> call, Throwable t) {
                Toast.makeText(getApplication(), "Error en el servidor", Toast.LENGTH_LONG).show();
            }
        });
    }
}