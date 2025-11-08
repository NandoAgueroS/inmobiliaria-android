package com.example.inmobiliarialab3.ui.contratos;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliarialab3.model.Inmueble;
import com.example.inmobiliarialab3.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContratosViewModel extends AndroidViewModel {
    private MutableLiveData<List<Inmueble>> mInmuebles = new MutableLiveData<>();
    private MutableLiveData<Boolean> mSesionInvalida = new MutableLiveData<>();

    public ContratosViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Inmueble>> getmInmuebles(){
        return mInmuebles;
    }

    public LiveData<Boolean> getmSesionInvalida() {
        return mSesionInvalida;
    }

    public void sesionInvalida(){
        mSesionInvalida.postValue(true);
        ApiClient.eliminarToken(getApplication());
    }

    public void cargarInmuebles(){
        String bearerToken = ApiClient.leerToken(getApplication());
        ApiClient.InmobiliariaService inmobiliariaService = ApiClient.getInmobiliariaService();
        Call<List<Inmueble>> inmueblesCall = inmobiliariaService.getInmueblesContratoVigente(bearerToken);

        inmueblesCall.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (response.isSuccessful()) {
                    mInmuebles.postValue(response.body());
                }else if (response.code() == 401){
                   sesionInvalida();
                }else{
                    Toast.makeText(getApplication(), "Error al cargar los inmuebles", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                Toast.makeText(getApplication(), "Error en el servidor", Toast.LENGTH_LONG).show();
                Log.d("error", t.getMessage());
            }
        });

    }
}