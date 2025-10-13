package com.example.inmobiliarialab3.request;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.inmobiliarialab3.model.Propietario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

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

        @GET("api/Propietarios")
        Call<Propietario> getPerfil(@Header("Authorization") String token);
    }
}
