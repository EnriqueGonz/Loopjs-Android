package com.example.Actividad4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
Button btnIniciar;
EditText txtUsuario;
EditText txtPassword;
String usuario,password,token;
AsyncHttpClient client;
String ruta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnIniciar= findViewById(R.id.btnIniciarSesion);
        txtUsuario = findViewById(R.id.txtUsuario);
        txtPassword  = findViewById(R.id.txtPassword);
        client = new AsyncHttpClient();
        ruta = "https://10ec5349.ngrok.io/";
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarSesion();
            }
        });

    }
    public void iniciarSesion(){
        usuario = txtUsuario.getText().toString();
        password = txtPassword.getText().toString();
        RequestParams params = new RequestParams();
        params.put("username",usuario);
        params.put("password",password);
        client.post(ruta+"api/v1/login/",params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject json = new JSONObject(new String(responseBody));


                    token = json.getString("token");
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    intent.putExtra("usuario",usuario);
                    intent.putExtra("token",token);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("", "Error usuario o contraseña incorrecta ");
                Toast.makeText(getApplicationContext(),"Error usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();


            }
        });

    }
}
