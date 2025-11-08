package com.example.inmobiliarialab3.ui.login;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliarialab3.MainActivity;
import com.example.inmobiliarialab3.model.Propietario;
import com.example.inmobiliarialab3.request.ApiClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends AndroidViewModel {

    private MutableLiveData<String> mLogin = new MutableLiveData<>();
    private MutableLiveData<String> mMensaje= new MutableLiveData<>();
    private MutableLiveData<Boolean> mSesionInvalida = new MutableLiveData<>();

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getmLogin() {
        return mLogin;
    }

    public LiveData<String> getmMensaje() {
        return mMensaje;
    }



    public LiveData<Boolean> getmSesionInvalida() {
        return mSesionInvalida;
    }

    public void login(String usuario, String contrasenia){

        if (validarCampos(usuario, contrasenia)){
            ApiClient.InmobiliariaService inmobiliariaService = ApiClient.getInmobiliariaService();

            Call<String> tokenCall = inmobiliariaService.login(usuario, contrasenia);
            tokenCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()){
                        String token = response.body();
                        ApiClient.guardarToken(getApplication(), token);
                        mLogin.setValue("");
                    }else{
                        mMensaje.postValue("El usuario y/o la contraseña son incorrectos");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    mMensaje.postValue("Logueo incorrecto");
                    Log.d("API Failure", t.getMessage());
                }
            });
        }

    }
    public boolean validarCampos(String usuario, String clave){
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(usuario);

        if (usuario.isBlank() && clave.isBlank()){
            mMensaje.setValue("Debe ingresar un usuario y una clave");
            return false;
        }else if (!matcher.matches() && clave.isBlank()){
            mMensaje.setValue("Debe ingresar un usuario válido y una clave");
            return false;
        }else if (usuario.isBlank()){
            mMensaje.setValue("Debe ingresar un usuario");
            return false;
        }else if (clave.isBlank()){
            mMensaje.setValue("Debe ingresar una clave");
            return false;
        }else if (!matcher.matches()){
            mMensaje.setValue("Debe ingresar un usuario válido");
            return false;
        }
        return true;
    }

}
