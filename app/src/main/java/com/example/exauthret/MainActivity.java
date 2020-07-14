package com.example.exauthret;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.exauthret.Model.Login;
import com.example.exauthret.Model.User;
import com.example.exauthret.service.UserClient;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
* Baseado no exemplo de https://www.youtube.com/watch?v=by-pChg9_A4
*
* */

public class MainActivity extends AppCompatActivity {
private static String token;
Button btSecret, btLogin;
final Login login = new Login("leandro","admin123");

Retrofit.Builder builder = new Retrofit.Builder()
        .baseUrl("http://192.168.1.107:8080/") ///aqui e o servidor
        .addConverterFactory(GsonConverterFactory.create());

    Retrofit retrofit = builder.build();

    UserClient userClient = retrofit.create(UserClient.class);
    //instancia do objeto retrofit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        btLogin=(Button)findViewById(R.id.btnLogin);
        btSecret=(Button)findViewById(R.id.btnSecret);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        btSecret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSecret();
            }
        });

    }

    private void login(){

        Call<User> call = userClient.login(login);
        //chama a interface com a chamada de login que fara
        //o post para receber o token
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){

                    Toast.makeText(getApplicationContext(), "funcionou: "+ response.body().getToken(), Toast.LENGTH_LONG).show();
                    token=response.body().getToken();
                    //pega a resposta do body e armazena na variavel token
                }
                else {
                    Toast.makeText(getApplicationContext(), "erro ao logar", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Falha" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getSecret(){
        Call<ResponseBody> call = userClient.getSecret(" Bearer " + token);
        //em usercliente chame o getsecret que fara um get passando em seu
        //cabecalho o bearer token

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        Toast.makeText(getApplicationContext(), "GetSecret: "+ response.body().string(), Toast.LENGTH_LONG).show();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "No get secret"+response.message(), Toast.LENGTH_LONG).show();
                    Log.d("erro", response.message());
                    Log.d("erro", String.valueOf(response.code()));
                    Log.d("erro", response.errorBody().toString());
                    Log.d("erro", response.headers().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_LONG).show();

            }
        });

    }

}
