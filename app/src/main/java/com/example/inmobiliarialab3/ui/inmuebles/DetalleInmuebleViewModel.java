package com.example.inmobiliarialab3.ui.inmuebles;

import android.app.Application;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliarialab3.model.Inmueble;
import com.example.inmobiliarialab3.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleInmuebleViewModel extends AndroidViewModel {
    private MutableLiveData<Inmueble> mInmueble = new MutableLiveData<>();
    private MutableLiveData<Boolean> mSesionInvalida = new MutableLiveData<>();

    public DetalleInmuebleViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Inmueble> getmInmueble() {
        return mInmueble;
    }

    public LiveData<Boolean> getmSesionInvalida() {
        return mSesionInvalida;
    }

    public void sesionInvalida(){
        mSesionInvalida.postValue(true);
        ApiClient.eliminarToken(getApplication());
    }

    public void mostrarInmueble(Bundle bundle){
        Inmueble inmueble = bundle.getSerializable("inmueble", Inmueble.class);
       if (inmueble != null){
           mInmueble.setValue(inmueble);
       }
    }
    public void actualizarInmueble(boolean estado){
        Inmueble inmuebleActualizado = new Inmueble();

        inmuebleActualizado.setDisponible(estado);
        inmuebleActualizado.setIdInmueble(mInmueble.getValue().getIdInmueble());

        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmobiliariaService inmobiliariaService = ApiClient.getInmobiliariaService();

        Call<Inmueble> inmuebleCall = inmobiliariaService.actualizarInmueble(token, inmuebleActualizado);

        inmuebleCall.enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplication(), "Inmueble actualizado", Toast.LENGTH_LONG).show();
                }else if(response.code() == 401) {
                    sesionInvalida();
                }else{
                    Toast.makeText(getApplication(), "Error al actualizar", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
                Toast.makeText(getApplication(), "Error en el servidor", Toast.LENGTH_LONG).show();
            }
        });
    }
}