package com.example.inmobiliarialab3.request;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.inmobiliarialab3.model.Contrato;
import com.example.inmobiliarialab3.model.Inmueble;
import com.example.inmobiliarialab3.model.Pago;
import com.example.inmobiliarialab3.model.Propietario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public class ApiClient {

    public static final String URL_BASE = "https://inmobiliariaulp-amb5hwfqaraweyga.canadacentral-01.azurewebsites.net/";

    public static InmobiliariaService getInmobiliariaService(){

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(InmobiliariaService.class);
    }

    public static void guardarToken(Context context, String token){
        SharedPreferences sp = context.getSharedPreferences("preferencias.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString("token",token);
        editor.apply();
    }

    public static String leerToken(Context context){
        SharedPreferences sp = context.getSharedPreferences("preferencias.xml", Context.MODE_PRIVATE);
        return sp.getString("token",null);
    }

    public interface InmobiliariaService{
        @FormUrlEncoded
        @POST("api/Propietarios/login")
        Call<String> login(@Field("Usuario") String usuario, @Field("Clave") String clave);

        @FormUrlEncoded
        @PUT("api/Propietarios/changePassowrd")
        Call<String> changePassword(@Field("currentPassword") String currentPassword, @Field("newPassword") String newPassword);

        @GET("api/Propietarios")
        Call<Propietario> getPerfil(@Header("Authorization") String token);

        @PUT("api/Propietarios/actualizar")
        Call<Propietario> actualizarPropietario(@Header("Authorization") String token, @Body Propietario propietario);

        @GET("api/inmuebles")
        Call<List<Inmueble>> getInmuebles(@Header("Authorization") String token);

        @GET("api/inmuebles/GetContratoVigente")
        Call<List<Inmueble>> getInmueblesContratoVigente(@Header("Authorization") String token);

        @PUT("api/Inmuebles/actualizar")
        Call<Inmueble> actualizarInmueble(@Header("Authorization") String token, @Body Inmueble inmueble);

        @Multipart
        @POST("api/Inmuebles/cargar")
        Call<Inmueble> cargarInmueble(@Header("Authorization") String token,
                                      @Part MultipartBody.Part imagen,
                                      @Part("inmueble") RequestBody inmuebleBody);

        @GET("api/contratos/inmueble/{id}")
        Call<Contrato> getContratoVigente(@Header("Authorization") String token, @Path("id") int id);

        @GET("api/pagos/contrato/{id}")
        Call<List<Pago>> getPagos(@Header("Authorization") String token, @Path("id") int id);
    }
}
