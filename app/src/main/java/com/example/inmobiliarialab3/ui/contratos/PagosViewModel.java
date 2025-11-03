package com.example.inmobiliarialab3.ui.contratos;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliarialab3.model.Pago;
import com.example.inmobiliarialab3.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagosViewModel extends AndroidViewModel {

    private MutableLiveData<List<Pago>> mPagos = new MutableLiveData<>();

    public PagosViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Pago>> getmPagos() {
        return mPagos;
    }

    public void cargarPagos(Bundle bundle){
        int idContrato = bundle.getInt("idContrato");

        if (idContrato == 0) return;

        String token = "Bearer " + ApiClient.leerToken(getApplication());
        ApiClient.InmobiliariaService inmobiliariaService = ApiClient.getInmobiliariaService();

        Call<List<Pago>> pagoCall = inmobiliariaService.getPagos(token, idContrato);

        pagoCall.enqueue(new Callback<List<Pago>>() {
            @Override
            public void onResponse(Call<List<Pago>> call, Response<List<Pago>> response) {
                if (response.isSuccessful()){
                    mPagos.postValue(response.body());
                }else{
                    Toast.makeText(getApplication(), "Error al cargar los pagos", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Pago>> call, Throwable t) {
                Toast.makeText(getApplication(), "Error en el servidor", Toast.LENGTH_LONG).show();
                Log.d("error", t.getMessage());            }
        });
    }
}