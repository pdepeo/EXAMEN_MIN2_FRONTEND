package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.myapplication.models.Avatar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MenuActivity extends AppCompatActivity {

    TextView username;
    TextView mail;
    ImageView avatar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        setContentView(R.layout.activity_menu);
        getAvatar();
        username = findViewById(R.id.username);
        mail = findViewById(R.id.mail);
        avatar = findViewById(R.id.imageView3);
        Bundle dataUser = getIntent().getExtras();
        String usernameValue = dataUser.getString("user");
        username.setText(usernameValue);
        String mailValue = dataUser.getString("mail");
        mail.setText(mailValue);
       // Glide.with(context).load("https://cdn.pixabay.com/photo/2017/07/11/15/51/kermit-2493979_1280.png").into(avatar);



    }

    public static Context context;

    public void buttonLOGOUT(android.view.View v) {
        SharedPreferences sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
        finish();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void getAvatar() {
        final String[] avatarFigure = new String[1];
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(RetrofitAPI.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        RetrofitAPI gerritAPI = retrofit.create(RetrofitAPI.class);
        Call<Avatar> callAvatar = gerritAPI.getAvatar();
        callAvatar.enqueue(new Callback<Avatar>() {
            @Override
            public void onResponse(Call<Avatar> call, Response<Avatar> response) {
                Avatar avatarResponse = response.body();
                String avatarValue = avatarResponse.getAvatar();
                Log.i("avatarValue", ":" + avatarValue);
                Glide.with(context).load(avatarValue).into(avatar);
            }

            @Override
            public void onFailure(Call<Avatar> call, Throwable t) {

            }
        });

    }
}