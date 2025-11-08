package com.example.inmobiliarialab3.ui.perfil;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliarialab3.request.ApiClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CambiarContraseniaViewModel extends AndroidViewModel {

    private MutableLiveData<String> mMensaje = new MutableLiveData<>();
    private MutableLiveData<Boolean> mGuardado= new MutableLiveData<>();
    private MutableLiveData<Boolean> mSesionInvalida = new MutableLiveData<>();

    public CambiarContraseniaViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getmMensaje() {
        return mMensaje;
    }

    public LiveData<Boolean> getmGuardado() {
        return mGuardado;
    }

    public LiveData<Boolean> getmSesionInvalida() {
        return mSesionInvalida;
    }

    public void sesionInvalida(){
        mSesionInvalida.postValue(true);
        ApiClient.eliminarToken(getApplication());
    }

    public void cambiarContrasenia(String actual, String nueva, String confirmada){
        if (actual.isBlank() || nueva.isBlank() || confirmada.isBlank()){
            mMensaje.setValue("Debe completar todos los campos");
            return;
        }

        if (!nueva.equals(confirmada)){
            mMensaje.setValue("Las contraseñas no coinciden");
            return;
        }

        String token = ApiClient.leerToken(getApplication());
        ApiClient.InmobiliariaService inmobiliariaService = ApiClient.getInmobiliariaService();

        Call<Void> call = inmobiliariaService.changePassword(token, actual, nueva);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                try {
                if (response.isSuccessful()){
                    Toast.makeText(getApplication(), "Contraseña actualizada", Toast.LENGTH_LONG).show();
                    mGuardado.postValue(true);
                }else if(response.code() == 400 && response.errorBody().string().equals("La contraseña actual es incorrecta.")) {
                    mMensaje.postValue("La contraseña actual es incorrecta");
                }else if (response.code() == 401){
                    sesionInvalida();
                }else{
                    mMensaje.postValue("Error al actualizar la contraseña");
                }
                } catch (IOException e) {
                    e.printStackTrace();
                    mMensaje.postValue("Ocurrió un error inesperado");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mMensaje.postValue("Error en el servidor");
                Log.d("API_ERROR", t.getMessage());
            }
        });

    }
}