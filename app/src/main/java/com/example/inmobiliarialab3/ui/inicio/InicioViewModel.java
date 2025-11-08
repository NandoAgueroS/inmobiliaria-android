package com.example.inmobiliarialab3.ui.inicio;

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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InicioViewModel extends AndroidViewModel {

    private MutableLiveData<Propietario> mPropietario = new MutableLiveData<>();
    private MutableLiveData<MapaActual> mMapa = new MutableLiveData<>();
    private MutableLiveData<Boolean> mSesionInvalida = new MutableLiveData<>();

    public InicioViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Propietario> getMPropietario(){
        return mPropietario;
    }
    public LiveData<MapaActual> getMMapa(){
        return mMapa;
    }

    public LiveData<Boolean> getmSesionInvalida() {
        return mSesionInvalida;
    }

    public void obtenerPerfil(){
        ApiClient.InmobiliariaService inmobiliariaService = ApiClient.getInmobiliariaService();

        Call<Propietario> tokenCall = inmobiliariaService.getPerfil(ApiClient.leerToken(getApplication()));
        tokenCall.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful()){
                    Propietario propietario = response.body();
                    mPropietario.postValue(propietario);
                }else if (response.code() == 401){
                    sesionInvalida();
                }
                else{
                    Toast.makeText(getApplication(), "Error al recuperar el perfil", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                Toast.makeText(getApplication(), "Error al recuperar el perfil", Toast.LENGTH_LONG).show();
            }
        });
        mMapa.postValue(new MapaActual());
    }

    public void sesionInvalida(){
        mSesionInvalida.postValue(true);
        ApiClient.eliminarToken(getApplication());
    }
        public class MapaActual implements OnMapReadyCallback {

            LatLng inmobiliaria= new LatLng(-33.15021468171232, -66.30479988251177);

            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {//cuando el mapa esté cargado y se esté renderizando
                MarkerOptions macadorInmobiliaria = new MarkerOptions();
                macadorInmobiliaria.position(inmobiliaria);
                macadorInmobiliaria.title("Inmobiliaria");

                googleMap.addMarker(macadorInmobiliaria);
                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                CameraPosition cameraPosition = new CameraPosition
                        .Builder()
                        .target(inmobiliaria)
                        .zoom(20)
                        .bearing(45)
                        .tilt(15)
                        .build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);

                googleMap.animateCamera(cameraUpdate);
            }
        }

    }