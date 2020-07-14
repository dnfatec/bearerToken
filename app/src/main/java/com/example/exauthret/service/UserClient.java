package com.example.exauthret.service;

import com.example.exauthret.Model.Login;
import com.example.exauthret.Model.User;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserClient {
    @POST("auth/signin/") ///jumento aqui e a url nao o verbo
    Call<User> login(@Body Login login); //passando os dados do objeto login

    @GET("api/book/v1/2") //jumento aqui sera o get pos autenticado
    Call<ResponseBody> getSecret(@Header("Authorization") String authToken);
    //a respota do get passando no cabecalho que ele precisa de uma autorizacao
    //que no sera via token bearer
}
