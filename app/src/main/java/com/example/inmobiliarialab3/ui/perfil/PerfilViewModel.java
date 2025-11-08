package com.example.inmobiliarialab3.ui.perfil;

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

public class PerfilViewModel extends AndroidViewModel {

    private MutableLiveData<Propietario> mPropietario = new MutableLiveData<>();
    private MutableLiveData<Boolean> bmEstado= new MutableLiveData<>();
    private MutableLiveData<String> bmTexto= new MutableLiveData<>();
    private MutableLiveData<String> mMensaje= new MutableLiveData<>();
    private MutableLiveData<Boolean> mSesionInvalida = new MutableLiveData<>();

    public PerfilViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Propietario> getMPropietario(){
        return mPropietario;
    }

    public LiveData<Boolean> getBmEstado() {
        return bmEstado;
    }

    public LiveData<String> getBmTexto() {
        return bmTexto;
    }

    public LiveData<String> getmMensaje() {
        return mMensaje;
    }

    public LiveData<Boolean> getmSesionInvalida() {
        return mSesionInvalida;
    }

    public void sesionInvalida(){
        mSesionInvalida.postValue(true);
        ApiClient.eliminarToken(getApplication());
    }

    public void mostrarPerfil(){
        ApiClient.InmobiliariaService inmobiliariaService = ApiClient.getInmobiliariaService();
        Call<Propietario> getPerfilCall = inmobiliariaService.getPerfil(ApiClient.leerToken(getApplication()));
        getPerfilCall.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful()){
                    mPropietario.setValue(response.body());
            }else if (response.code() == 401){
                sesionInvalida();
            }else{
                Toast.makeText(getApplication(), "Error al obtener el perfil", Toast.LENGTH_LONG).show();
            }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                Log.d("API_ERROR", t.getMessage());
                Toast.makeText(getApplication(), "Error en el servidor", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void guardar(String botonText, String nombre, String apellido, String dni, String email, String telefono) {
        if (botonText.equals("Editar")){
            bmEstado.setValue(true);
            bmTexto.setValue("Guardar");
        }else{

            Propietario propietarioActualizado = validarPropietario(nombre, apellido, dni, email, telefono);

            if (propietarioActualizado != null){
                String token = ApiClient.leerToken(getApplication());

                Call<Propietario> propietarioCall = ApiClient.getInmobiliariaService().actualizarPropietario(token, propietarioActualizado);
                propietarioCall.enqueue(new Callback<Propietario>() {
                    @Override
                    public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplication(), "Actualizado correctamente", Toast.LENGTH_LONG).show();
                            bmTexto.setValue("Editar");
                            bmEstado.setValue(false);
                        }else if (response.code() == 401){
                            sesionInvalida();
                        }else{
                            Toast.makeText(getApplication(), "Error al actualizar", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Propietario> call, Throwable t) {
                        Log.d("API_ERROR", t.getMessage());
                        Toast.makeText(getApplication(), "Error en el servidor", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    private Propietario validarPropietario(String nombre, String apellido, String dni, String email, String telefono){
        if (nombre.isBlank() || apellido.isBlank() || dni.isBlank() || email.isBlank() || telefono.isBlank()){
           mMensaje.setValue("No pueden haber campos vacios");
           return null;
        }

        try{
            Long.parseLong(dni);
        } catch (NumberFormatException e) {
            mMensaje.setValue("El DNI debe ser un número");
            return null;
        }

        try{
            Long.parseLong(telefono);
        } catch (NumberFormatException e) {
            mMensaje.setValue("El teléfono debe ser un número");
            return null;
        }

        Propietario propietario = new Propietario();
        propietario.setIdPropietario(mPropietario.getValue().getIdPropietario());
        propietario.setApellido(apellido);
        propietario.setNombre(nombre);
        propietario.setDni(dni);
        propietario.setEmail(email);
        propietario.setTelefono(telefono);
        propietario.setClave(null);

        return propietario;
    }
}