package com.example.inmobiliarialab3.ui.bienvenida;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliarialab3.model.Propietario;
import com.example.inmobiliarialab3.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PantallaCargaActivityViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> mYaLogueado = new MutableLiveData<>();
    private MutableLiveData<Boolean> mSesionInvalida= new MutableLiveData<>();
    private MutableLiveData<Boolean> mSinToken= new MutableLiveData<>();
    public PantallaCargaActivityViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<Boolean> getmYaLogueado(){
        return mYaLogueado;
    }


    public LiveData<Boolean> getmSesionInvalida() {
        return mSesionInvalida;
    }
    public LiveData<Boolean> getmSinToken() {
        return mSinToken;
    }

    public void sesionInvalida(){
        mSesionInvalida.postValue(true);
        ApiClient.eliminarToken(getApplication());
    }
    public void verificarLogueado(){
        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmobiliariaService inmobiliariaService = ApiClient.getInmobiliariaService();
        if(token != null && !token.isBlank()){
            Call<Propietario> call = inmobiliariaService.getPerfil(token);
            call.enqueue(new Callback<Propietario>() {
                @Override
                public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                    if (response.isSuccessful()){
                        mYaLogueado.postValue(true);
                    }else if(response.code() == 401) {
                        sesionInvalida();
                        Toast.makeText(getApplication(), "Sesión expirada", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplication(), "Error al iniciar sesión", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Propietario> call, Throwable t) {
                    Log.d("API_ERROR", t.getMessage());
                    Toast.makeText(getApplication(), "Error al iniciar sesión", Toast.LENGTH_LONG).show();
                }
            });
        }else{
            mSinToken.setValue(true);
        }
}}
