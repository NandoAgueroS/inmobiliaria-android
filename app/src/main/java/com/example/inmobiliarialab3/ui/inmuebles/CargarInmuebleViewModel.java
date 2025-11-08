package com.example.inmobiliarialab3.ui.inmuebles;

import static android.app.Activity.RESULT_OK;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.inmobiliarialab3.model.Inmueble;
import com.example.inmobiliarialab3.request.ApiClient;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CargarInmuebleViewModel extends AndroidViewModel {
    private MutableLiveData<Uri> mUri = new MutableLiveData<>();
    private MutableLiveData<Boolean> mGuardado = new MutableLiveData<>();
    private MutableLiveData<String> mMensaje= new MutableLiveData<>();
    private MutableLiveData<Boolean> mSesionInvalida = new MutableLiveData<>();

    public CargarInmuebleViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Uri> getmUri(){return mUri;}
    public LiveData<Boolean> getmGuardado(){return mGuardado;}
    public LiveData<String> getmMensaje(){return mMensaje;}

    public LiveData<Boolean> getmSesionInvalida() {
        return mSesionInvalida;
    }

    public void sesionInvalida(){
        mSesionInvalida.postValue(true);
        ApiClient.eliminarToken(getApplication());
    }

    public void recibirFoto(ActivityResult result){
        if (result.getResultCode() == RESULT_OK){
            Intent data = result.getData();
            Uri uri = data.getData();
            mUri.setValue(uri);
        }
    }
    public void cargarInmueble(String direccion, String valor, String tipo, String uso, String ambientes, String superficie, boolean disponible, String latitud, String longitud){
        int superficiePars, ambientesPars;
        double precio, latitudPars, longitudPars;

        try{

            if(direccion.isBlank() || tipo.isBlank() || uso.isBlank() || ambientes.isBlank() || superficie.isBlank() || valor.isBlank() || latitud.isBlank() || longitud.isBlank()){
                mMensaje.postValue("Debe completar todos los campos");
                return;
            }

            precio = Double.parseDouble(valor);
            superficiePars = Integer.parseInt(superficie);
            ambientesPars = Integer.parseInt(ambientes);
            latitudPars = Double.parseDouble(latitud);
            longitudPars = Double.parseDouble(longitud);

            if(mUri.getValue() == null) {
                mMensaje.postValue("Debe seleccionar una foto");
                return;
            }

            Inmueble inmueble = new Inmueble();
            inmueble.setDireccion(direccion);
            inmueble.setValor(precio);
            inmueble.setTipo(tipo);
            inmueble.setUso(uso);
            inmueble.setAmbientes(ambientesPars);
            inmueble.setSuperficie(superficiePars);
            inmueble.setDisponible(disponible);
            inmueble.setLatitud(latitudPars);
            inmueble.setLongitud(longitudPars);

            //Convertir en base a la uri
            byte[] imagen = transformarImagen();
            String inmuebleJson = new Gson().toJson(inmueble);
            RequestBody inmuebleBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inmuebleJson);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imagen);

            MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagen", "imagen.jpg", requestFile);

            ApiClient.InmobiliariaService inmobiliariaService= ApiClient.getInmobiliariaService();
            String token = ApiClient.leerToken(getApplication());
            Call<Inmueble> call = inmobiliariaService.cargarInmueble(token, imagenPart, inmuebleBody);
            call.enqueue(new Callback<Inmueble>() {
                @Override
                public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                    if(response.isSuccessful()) {
                        Toast.makeText(getApplication(), "Inmueble cargado exitosamente", Toast.LENGTH_SHORT).show();
                        mGuardado.postValue(true);
                    }else if (response.code() ==401){
                            sesionInvalida();
                    }else{
                        mMensaje.postValue("Error al cargar inmueble");
                    }
                }

                @Override
                public void onFailure(Call<Inmueble> call, Throwable t) {
                    mMensaje.postValue("Error al cargar inmueble");
                }
            });

        }catch(NumberFormatException nfe){
            mMensaje.postValue("Debe ingresar números en los campos de valor, superficie, ambientes y coordenadas");
        }

    }

    private byte[] transformarImagen() {
        try {
            Uri uri = mUri.getValue();
            InputStream inputStream = getApplication().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (
                FileNotFoundException er) {
            mMensaje.postValue("Debe seleccionar una foto");
            return new byte[]{};
        }
    }
}