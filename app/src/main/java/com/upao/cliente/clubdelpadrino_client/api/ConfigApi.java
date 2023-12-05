package com.upao.cliente.clubdelpadrino_client.api;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.upao.cliente.clubdelpadrino_client.utils.DateSerializer;
import com.upao.cliente.clubdelpadrino_client.utils.TimeSerializer;

import java.sql.Date;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfigApi {
    public static final String baseUrlE = "http://10.0.2.2:9090";
    //public static final String baseUrlE = "https://tedious-increase-production.up.railway.app";
    public static final String ipOmarLeon = "http://192.168.1.11:9090";
    private static Retrofit retrofit;
    private static String token = "";
    private static UsuarioApi usuarioApi;
    private static ClienteApi clienteApi;
    private static FotoApi fotoApi;
    private static CategoriaApi categoriaApi;
    private static ProductoApi productoApi;
    private static PedidoApi pedidoApi;

    static {
        initClient();
    }

    private static void initClient() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .registerTypeAdapter(Time.class, new TimeSerializer())
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(ipOmarLeon)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getClient())
                .build();
    }

    public static OkHttpClient getClient() {
        HttpLoggingInterceptor loggin = new HttpLoggingInterceptor();
        loggin.level(HttpLoggingInterceptor.Level.BODY);

        StethoInterceptor stetho = new StethoInterceptor();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(loggin)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addNetworkInterceptor(stetho);
        return builder.build();
    }

    public static void setToken(String value){
        token = value;
        initClient();
    }

    public static UsuarioApi getUsuarioApi(){
        if(usuarioApi == null){
            usuarioApi = retrofit.create(UsuarioApi.class);
        }
        return usuarioApi;
    }

    public static ClienteApi getClienteApi(){
        if (clienteApi == null){
            clienteApi = retrofit.create(ClienteApi.class);
        }
        return clienteApi;
    }

    public static FotoApi getFotoApi(){
        if (fotoApi == null){
            fotoApi = retrofit.create(FotoApi.class);
        }
        return fotoApi;
    }

    public static CategoriaApi getCategoriaApi(){
        if (categoriaApi == null){
            categoriaApi = retrofit.create(CategoriaApi.class);
        }
        return categoriaApi;
    }

    public static ProductoApi getProductoApi(){
        if (productoApi == null){
            productoApi = retrofit.create(ProductoApi.class);
        }
        return productoApi;
    }

    public static PedidoApi getPedidoApi(){
        if (pedidoApi == null){
            pedidoApi = retrofit.create(PedidoApi.class);
        }
        return pedidoApi;
    }
}