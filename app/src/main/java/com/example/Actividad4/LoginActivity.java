package com.example.Actividad4;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {
TableLayout tabla;
TextView prueba;
 String  token;
String ruta;
AsyncHttpClient client;
ArrayList ids,nombres,edads,direcciones,sexos,apellidosp,apellidosm,carreras;

TableRow fila;
TableRow fila2;
TextView columna,txtId,txtNombre,txtcarrera,txtDireccion,txtSexo,txtapellidoP,txtapellidoM,txtEdad,txtCarrera;
Button btnEditar,btnEliminar;
EditText txtNombreActualizar,txtDireccionActualizar,txtCarreraActualizar;
String[]columnas = {"id","nombre","carrera","Editar","Eliminar"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tabla = findViewById(R.id.lista);

        client = new AsyncHttpClient();
        ids = new ArrayList();
        nombres = new ArrayList();
        edads = new ArrayList();
        direcciones = new ArrayList();
        sexos = new ArrayList();
        apellidosp = new ArrayList();
        apellidosm = new ArrayList();
        carreras = new ArrayList();
        fila = new TableRow(LoginActivity.this);
        fila2 = new TableRow(LoginActivity.this);
        token = getIntent().getStringExtra("token");
        ruta = "https://10ec5349.ngrok.io/";


        for (int i = 0; i< columnas.length;i++){
            columna = new TextView(LoginActivity.this);
            columna.setGravity(Gravity.CENTER_VERTICAL);
            columna.setPadding(15,15,15,15);
            columna.setText(columnas[i]);
            fila.addView(columna);

        }
        tabla.addView(fila);
        recuperarDatos(token);

        Button buttonR = findViewById(R.id.botonr);
        buttonR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar();
            }
        });
    }

    public void recuperarDatos(String token){
        client.addHeader("Authorization","Token"+" "+ token);
        client.get(ruta+"api/v1/alumno/alumno_lista/", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONArray array = new JSONArray(new String(responseBody));
                    //JSONObject json = new JSONObject(new String(responseBody));
                    System.out.println(array);

                    JSONObject json;


                   for (int i = 0; i< array.length();i++) {
                       json = array.getJSONObject(i);
                       ids.add(json.getInt("id"));
                       nombres.add(json.getString("nombre"));
                       edads.add(json.getString("edad"));
                       direcciones.add(json.getString("direccion"));
                       sexos.add(json.getString("sexo"));
                       apellidosp.add(json.getString("apellidoPaterno"));
                       apellidosm.add(json.getString("apellidoMaterno"));
                       carreras.add(json.getString("carrera"));
                   }
                  for(int i = 0;i<array.length();i++) {
                      final int posicion = i;
                      TableRow tableRow = new TableRow(LoginActivity.this);
                      tabla.addView(tableRow);
                       for (int j = 0; j < columnas.length; j++) {
                           txtId= new EditText(LoginActivity.this);
                           txtNombre= new EditText(LoginActivity.this);
                           txtcarrera= new EditText(LoginActivity.this);
                           txtId.setGravity(Gravity.CENTER_VERTICAL);
                           txtId.setPadding(15, 15, 15, 15);
                           txtNombre.setGravity(Gravity.CENTER_VERTICAL);
                           txtNombre.setPadding(15, 15, 15, 15);
                           txtcarrera.setGravity(Gravity.CENTER_VERTICAL);
                           txtcarrera.setPadding(15, 15, 15, 15);
                           switch (j) {
                               case 0:
                                   txtId.setText(ids.get(i).toString());
                                   txtId.setEnabled(false);
                                   System.out.println(ids.get(i));
                                   tableRow.addView(txtId);
                                   break;
                               case 1:
                                   txtNombre.setText(nombres.get(i).toString());
                                   txtNombre.setEnabled(false);
                                   System.out.println(nombres.get(i));
                                   tableRow.addView(txtNombre);
                                   break;
                               case 2:
                                   txtcarrera.setText(carreras.get(i).toString());
                                   txtcarrera.setEnabled(false);
                                   System.out.println(carreras.get(i));
                                   tableRow.addView(txtcarrera);

                                   break;
                               case 3:
                                   btnEditar = new Button(LoginActivity.this);

                                   btnEditar.setBackgroundColor(Color.parseColor("#F0A732"));
                                   btnEditar.setText("Editar");
                                   btnEditar.setGravity(Gravity.CENTER_VERTICAL);
                                   btnEditar.setPadding(15, 15, 15, 15);
                                   btnEditar.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           actualizar(posicion);
                                       }
                                   });
                                   tableRow.addView(btnEditar);
                                   break;
                               case 4:
                                   btnEliminar= new Button(LoginActivity.this);
                                   btnEliminar.setBackgroundColor(Color.parseColor("#F26522"));
                                   btnEliminar.setText("Eliminar");
                                   btnEliminar.setGravity(Gravity.CENTER_VERTICAL);
                                   btnEliminar.setPadding(15, 15, 15, 15);
                                   btnEliminar.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           eliminar(posicion);
                                       }
                                   });
                                   tableRow.addView(btnEliminar);
                                   break;

                           }


                       }

                   }


                    System.out.println(ids.size());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println(statusCode);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });

    }
    public void eliminar(int posicion){
        int id = (int) ids.get(posicion);
        Toast.makeText(getApplicationContext(),"ID " + id, Toast.LENGTH_SHORT).show();
        client.addHeader("Authorization","Token"+" "+ token);
        client.delete(ruta+"api/v1/alumno/alumno_detail/"+id+"/", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(getApplicationContext(),"Se ha eliminado con exito",Toast.LENGTH_SHORT).show();
                ids.clear();
                nombres.clear();
                edads.clear();
                direcciones.clear();
                sexos.clear();
                apellidosp.clear();
                apellidosm.clear();
                carreras.clear();
                tabla.removeAllViews();
                recuperarDatos(token);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("", "Error no se pudo eliminar");
                System.out.println("Error no se pudo eliminar: "+ statusCode);

            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }

    public void actualizar(int posicion){
        final int posicion2 = posicion;
        final int id = (int) ids.get(posicion);
        AlertDialog.Builder miBuilder = new AlertDialog.Builder(LoginActivity.this);
        final View modal = getLayoutInflater().inflate(R.layout.formulario, null);
        txtNombreActualizar = modal.findViewById(R.id.txtnombre1);
        txtDireccionActualizar = modal.findViewById(R.id.txtDireccion1);
        txtCarreraActualizar = modal.findViewById(R.id.editTextCarrera);

        txtNombreActualizar.setText(nombres.get(posicion).toString());
        txtDireccionActualizar.setText(direcciones.get(posicion).toString());
        txtCarreraActualizar.setText(carreras.get(posicion).toString());
        miBuilder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                RequestParams params = new RequestParams();

                params.put("nombre",txtNombreActualizar.getText().toString());
                params.put("edad",edads.get(posicion2).toString());
                params.put("direccion",txtDireccionActualizar.getText().toString());
                params.put("sexo",sexos.get(posicion2).toString());
                params.put("apellidoPaterno",apellidosp.get(posicion2).toString());
                params.put("apellidoMaterno",apellidosm.get(posicion2).toString());
                params.put("carrera",txtCarreraActualizar.getText().toString());

                client.addHeader("Authorization","Token"+" "+ token);
                client.put(ruta+"api/v1/alumno/alumno_detail/"+id+"/",params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        // called before request is started
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        ids.clear();
                        nombres.clear();
                        edads.clear();
                        direcciones.clear();
                        sexos.clear();
                        apellidosp.clear();
                        apellidosm.clear();
                        carreras.clear();
                        tabla.removeAllViews();
                        recuperarDatos(token);
                        Toast.makeText(getApplicationContext(),"Se ha actualizado con exito ", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d("", "Error no se pudo actualizar");
                        System.out.println("Error no se pudo actualizar: "+ statusCode);

                    }

                    @Override
                    public void onRetry(int retryNo) {
                        // called when request is retried
                    }
                });
            }
        });
        miBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        miBuilder.setView(modal);
        AlertDialog dialog2 = miBuilder.create();
        dialog2.show();

    }



    public void registrar(){
        AlertDialog.Builder miBuilder = new AlertDialog.Builder(LoginActivity.this);
        final View modal = getLayoutInflater().inflate(R.layout.formularioregistro, null);
        txtNombre = modal.findViewById(R.id.txtnombre1);
        txtEdad = modal.findViewById(R.id.txtEdad);
        txtapellidoP = modal.findViewById(R.id.txtApellidoMaterno);
        txtapellidoM = modal.findViewById(R.id.txtApellidoMaterno);
        txtSexo = modal.findViewById(R.id.txtSexo1);
        txtDireccion = modal.findViewById(R.id.txtDireccion1);
        txtCarrera = modal.findViewById(R.id.txtCarrera1);
        miBuilder.setPositiveButton("Registrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RequestParams params = new RequestParams();
                        params.put("nombre",txtNombre.getText().toString());
                        params.put("edad",txtEdad.getText().toString());
                        params.put("direccion",txtDireccion.getText().toString());
                        params.put("sexo",txtSexo.getText().toString());
                        params.put("apellidoPaterno",txtapellidoP.getText().toString());
                        params.put("apellidoMaterno",txtapellidoM.getText().toString());
                        params.put("carrera",txtCarrera.getText().toString());

                        client.addHeader("Authorization","Token"+" "+ token);
                        client.post(ruta+"api/v1/alumno/alumno_lista/",params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                Log.d("", "Correcto");
                                ids.clear();
                                nombres.clear();
                                edads.clear();
                                direcciones.clear();
                                sexos.clear();
                                apellidosp.clear();
                                apellidosm.clear();
                                carreras.clear();
                                tabla.removeAllViews();
                                recuperarDatos(token);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            }
                        });
                    }
                   });
        miBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        miBuilder.setView(modal);
        AlertDialog dialog2 = miBuilder.create();
        dialog2.show();

    }


}
