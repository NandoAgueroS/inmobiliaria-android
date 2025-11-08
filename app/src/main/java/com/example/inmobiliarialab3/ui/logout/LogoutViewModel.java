package com.example.inmobiliarialab3.ui.logout;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliarialab3.request.ApiClient;

public class LogoutViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> mTokenEliminado = new MutableLiveData<>();
    private MutableLiveData<Boolean> mSesionInvalida = new MutableLiveData<>();

    public LogoutViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Boolean> getMTokenEliminado(){
        return mTokenEliminado;
    }

    public LiveData<Boolean> getmSesionInvalida() {
        return mSesionInvalida;
    }

    public void sesionInvalida(){
        mSesionInvalida.postValue(true);
        ApiClient.eliminarToken(getApplication());
    }

    public void eliminarToken(){
        ApiClient.eliminarToken(getApplication());
        mTokenEliminado.setValue(true);
    }

}
