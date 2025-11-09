package com.example.inmobiliarialab3.ui.login;


import static java.lang.StrictMath.sqrt;

import android.app.Application;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliarialab3.MainActivity;
import com.example.inmobiliarialab3.model.Propietario;
import com.example.inmobiliarialab3.request.ApiClient;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends AndroidViewModel {

    private Float acceleration = 0f;
    private Float anteriorAcceleration = 0f;
    private Float actualAcceleration = 0f;
    private SensorManager manager;
    private List<Sensor> sensores;
    private ManejaSensores maneja;
    private final float SHAKE_THRESHOLD = 52f;
    private final float SHAKE_RESET_THRESHOLD = 0.001f;
    private boolean shakeInProgress = false;

    private MutableLiveData<String> mLogin = new MutableLiveData<>();
    private MutableLiveData<String> mMensaje= new MutableLiveData<>();
    private MutableLiveData<String> mEmailGuardado= new MutableLiveData<>();
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

    public LiveData<String> getmEmailGuardado() {
        return mEmailGuardado;
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

    public void activarLecturas(){
        manager = (SensorManager) getApplication().getSystemService(Context.SENSOR_SERVICE);
        sensores = manager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        maneja = new ManejaSensores();
        manager.registerListener(maneja, sensores.get(0), SensorManager.SENSOR_DELAY_GAME);

    }


    public void desactivarLecturas(){
        if (!sensores.isEmpty())
            manager.unregisterListener(maneja);
    }
    public void llamar(){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:123456789"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(intent);

    }
    public void guardarEmail(String email){
        SharedPreferences sp = getApplication().getSharedPreferences("preferencias.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString("email", email);
        editor.apply();
    }
    public void recuperarEmail(){
        SharedPreferences sp = getApplication().getSharedPreferences("preferencias.xml", Context.MODE_PRIVATE);
        String email = sp.getString("email",null);
        if (email != null && !email.isBlank()) {
            mEmailGuardado.setValue(email);
        }
    }
    public void verificarSesionExpirada(Intent intent){
       if (intent.getBooleanExtra("desde_sesion_expirada", false)){
          mSesionInvalida.setValue(true);
       }
    }
    private class ManejaSensores implements SensorEventListener {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) { //cuando cambia la presicion

        }

        @Override
        public void onSensorChanged(SensorEvent event) { //cuando el sensor envía una notificación
            float ejeX = event.values[0];
            float ejeY = event.values[1];
            float ejeZ = event.values[2];
            anteriorAcceleration = actualAcceleration;

            actualAcceleration = (float) sqrt(ejeX * ejeX + ejeY * ejeY + ejeZ * ejeZ);
            float delta = actualAcceleration - anteriorAcceleration;
            acceleration = acceleration * 0.9f + delta;

            if (!shakeInProgress && acceleration > SHAKE_THRESHOLD) {
                llamar();
                shakeInProgress = true;
            } else if (shakeInProgress && acceleration < SHAKE_RESET_THRESHOLD) {
                shakeInProgress = false;
            }
        }
    }

}
