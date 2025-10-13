package com.example.inmobiliarialab3.ui.inicio;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliarialab3.model.Propietario;
import com.example.inmobiliarialab3.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InicioViewModel extends AndroidViewModel {

    private MutableLiveData<Propietario> mPropietario = new MutableLiveData<>();

    public InicioViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Propietario> getMPropietario(){
        return mPropietario;
    }

    public void obtenerPerfil(){
        ApiClient.InmobiliariaService inmobiliariaService = ApiClient.getInmobiliariaService();

        Call<Propietario> tokenCall = inmobiliariaService.getPerfil("Bearer " + ApiClient.leerToken(getApplication()));
        tokenCall.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful()){
                    Propietario propietario = response.body();
                    mPropietario.postValue(propietario);
                }else{
                    Toast.makeText(getApplication(), "Error al recuperar el perfil", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                Toast.makeText(getApplication(), "Error al recuperar el perfil", Toast.LENGTH_LONG).show();
            }
        });
    }
}