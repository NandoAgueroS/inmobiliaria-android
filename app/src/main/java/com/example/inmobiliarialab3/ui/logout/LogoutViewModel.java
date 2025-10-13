package com.example.inmobiliarialab3.ui.logout;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class LogoutViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> mTokenEliminado = new MutableLiveData<>();

    public LogoutViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Boolean> getMTokenEliminado(){
        return mTokenEliminado;
    }
    public void eliminarToken(){
        SharedPreferences sp = getApplication().getSharedPreferences("preferencias.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sp.edit();
        editor.remove("token");
        editor.commit();
        mTokenEliminado.setValue(true);
    }
}
