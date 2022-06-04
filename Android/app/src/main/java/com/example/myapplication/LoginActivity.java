package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;


import com.example.myapplication.models.User;
import com.example.myapplication.models.UserData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    TextView textView;
    EditText userName;
    EditText psw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textView = findViewById(R.id.textView5);
        userName = findViewById(R.id.editUser);
        psw = findViewById(R.id.editPassword);

    }

    public void buttonLOGIN(android.view.View v) {
        if (userName.getText().toString().isEmpty() || psw.getText().toString().isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please enter all the values", Toast.LENGTH_SHORT).show();

            return;
        } else
            postData(this.userName.getText().toString(), this.psw.getText().toString());
    }


    public void postData(String userName, String psw) {

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(RetrofitAPI.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        RetrofitAPI gerritAPI = retrofit.create(RetrofitAPI.class);
        Bundle bundle = new Bundle();
        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        Call<User> call = gerritAPI.login(new UserData(null, userName, null, psw));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                int variable = response.code();
                Log.i("LOGIN CODE", ":" + variable);
                if (response.isSuccessful()) {
                    User user = response.body();
                    String userNom = user.getUsername();
                    Log.i("Name", ":" + userNom);
                    String userPsw = user.getPass();
                    Log.i("Psw", ":" + userPsw);
                    String userMail = user.getMail();
                    Log.i("Mail", ":"+userMail);
                    Log.i("LOGIN", "OK" + user);
                   bundle.putString("user", response.body().getUsername());
                   bundle.putString("mail", response.body().getMail());
                    intent.putExtras(bundle);
                    startActivity(intent);
                  //  SharedPreferences sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE);
                  //  SharedPreferences.Editor editor = sharedPref.edit();
                  //  editor.putString("User", userNom);
                 //   editor.putString("psw", userPsw);
                    //editor.putString("mail", userMail);
                 //   editor.commit();
                    Log.i("Log", "OK" + user);

                    Toast.makeText(LoginActivity.this, "Usuario y password correctas", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Usuario y/o password incorrectas", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("LOGIN", "ERROR", t);
                Toast.makeText(LoginActivity.this, "Usuario y/o password incorrectas", Toast.LENGTH_LONG).show();
            }
        });


    }

    public void buttonRegister(android.view.View v) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    public void buttonFor(android.view.View v) {
        Intent intent = new Intent(this, ActivityForget.class);
        startActivity(intent);
    }
}