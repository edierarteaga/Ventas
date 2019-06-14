package com.example.administrador.gestionusuarios;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.administrador.gestionusuarios.utilidades.GlobalClass;
import com.example.administrador.gestionusuarios.utilidades.Utilidades;
import com.google.gson.internal.bind.SqlDateTypeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

public class UsuarionuevosActivity extends AppCompatActivity {
    private TextView tv_vendedor, tv_usuarionuevo, tv_msg, tv_placa;
    private String user_id,apiKey,vendedor,usuarionuevo, nom_conductor, placa, cedulan;
    private EditText et_cedula;
    private RecyclerView rv_usuarionuevos;
    private GridLayoutManager glm_cli;
    private UsuarionuevoAdapter adapter;
    private List<Usuarionuevo> usuarionuevolist;
    private List<Configusuarionuevo> configusuarionuevolist;
    private List<Usuarionuevo> listaUsuarionuevos;
    private SharedPreferences sp;
    private Boolean verificado;
    private Button btn_crearUsuarionuevo, btn_buscarced;
    private TableRow tr_buscar;
    private String enlinea;
    private String cadena;
    private String sep;
    ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"sistemaonline",null,9);
    TimerTask task;
    SessionManager session;
    GlobalClass globalClass;
    SQLiteDatabase db;
    ContentValues values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarionuevos);

        globalClass=(GlobalClass)getApplicationContext();





        tv_vendedor = (TextView) findViewById(R.id.tv_vendedor);
        tv_usuarionuevo = (TextView) findViewById(R.id.tv_usuarionuevo);
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        tv_placa = (TextView) findViewById(R.id.tv_placa);
        rv_usuarionuevos = (RecyclerView) findViewById(R.id.rv_usuarionuevos);
        et_cedula = (EditText) findViewById(R.id.et_cedusuarionuevo);
        tr_buscar = (TableRow) findViewById(R.id.tr_buscar);
        session = new SessionManager(getApplicationContext());
        sp = session.getVariables2();
        user_id=sp.getString("name","23");

        //task = (TimerTask) Comunicador.getObjeto();

        btn_crearUsuarionuevo = (Button) findViewById(R.id.btn_crearUsuarionuevo);
        btn_buscarced= (Button) findViewById(R.id.btn_buscarced);



        glm_cli = new GridLayoutManager(this,1);
        rv_usuarionuevos.setLayoutManager(glm_cli);

        if (true){
            if (getIntent().getExtras() != null){
                cedulan = getIntent().getExtras().getString("cedula_new");
                if (cedulan != null){
                    et_cedula.setText(cedulan);
                }
            }

            tv_usuarionuevo.setText("Busqueda: ");
            tr_buscar.setVisibility(View.VISIBLE);
            if(globalClass.getEnlinea()==null||globalClass.getEnlinea()=="false") {
                loadUsuarionuevosSqlite("0");
            }else {

                loadCLientes("0",user_id);
            }
            btn_buscarced.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(et_cedula.length() > 0){
                        if(globalClass.getEnlinea()==null||globalClass.getEnlinea()=="false") {
                            loadUsuarionuevosSqlite(et_cedula.getText().toString().trim());
                        }else {
                            loadCLientes(et_cedula.getText().toString().trim(),user_id);

                        }
                        tv_usuarionuevo.setText("se digito!: "+ et_cedula.getText().toString().trim());
                        usuarionuevo = et_cedula.getText().toString().trim();
                       /* SharedPreferences.Editor editor = sp.edit();
                        editor.putString("usuarionuevo", usuarionuevo);
                        editor.commit();*/
                    }else{
                        tv_usuarionuevo.setText("se digito!: ");
                        if(globalClass.getEnlinea()==null||globalClass.getEnlinea()=="false") {
                            loadUsuarionuevosSqlite("");
                        }else {

                            loadCLientes("0",user_id);
                        }
                        //Toast.makeText(UsuarionuevosActivity.this,"buscar"+globalClass.getEnlinea(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else if (true){
            tv_usuarionuevo.setText("Busqueda: " );


        }
        btn_crearUsuarionuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent usuarionuevonew = new Intent(UsuarionuevosActivity.this, UsuarionuevoformActivity.class);
                usuarionuevonew.putExtra("editarusuarionuevo","1");
                startActivity(usuarionuevonew);
            }
        });
    }
    public static void hideKeyboard(@NonNull Activity activity) {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_crear_usuarionuevo, menu);
        getMenuInflater().inflate(R.menu.menu_solo_salir, menu);

        MenuItem itemSwitch=menu.findItem(R.id.myswitch);
        itemSwitch.setActionView(R.layout.switch_layout);
        final Switch sw = (Switch)menu.findItem(R.id.myswitch).getActionView().findViewById(R.id.switchForActionBar);
        if(globalClass.getEnlinea()==null||globalClass.getEnlinea()=="false"){
            sw.setChecked(false);
        }else{
            sw.setChecked(true);
        }


        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                GlobalClass globalClass=(GlobalClass)getApplicationContext();
                if(b){
                    // Toast.makeText(getBaseContext(), "Checked", Toast.LENGTH_SHORT).show();
                    globalClass.setEnlinea("true");
                    loadCLientes("0",user_id);
                }else{
                    // Toast.makeText(getBaseContext(), "unChecked", Toast.LENGTH_SHORT).show();
                    globalClass.setEnlinea("false");
                    loadUsuarionuevosSqlite("0");
                }
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_cerrar) {
            stopService(new Intent(UsuarionuevosActivity.this, PedidosService.class));
            //Toast.makeText(CapturarDocumentoActivity.this,"Servicio Detenido",Toast.LENGTH_SHORT).show();
            task.cancel();

           // sp.edit().clear().commit();
            Intent login = new Intent(UsuarionuevosActivity.this, LoginActivity.class);
            startActivity(login);
            Toast.makeText(this, "Sesion cerrada", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }*/

        if (id == R.id.action_cerrar) {
            session.logoutUser();
            Intent login = new Intent(UsuarionuevosActivity.this, LoginActivity.class);
            startActivity(login);
            Toast.makeText(this, "Sesion cerrada", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }
        if(true){
            registrarconfigUsuarionuevoSqlite();
        }
        if (id == R.id.action_actualizarusuarionuevo_internet) {
            /*session.logoutUser();
            Intent login = new Intent(UsuarionuevosActivity.this, LoginActivity.class);
            startActivity(login);*/
            sincronizarusuarionuevo("0");
            //Toast.makeText(this, "datos actualizados", Toast.LENGTH_SHORT).show();
            //finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void loadUsuarionuevosSqlite(final String usuarionuevobusqueda){
        String wherecedula="";
        if((!TextUtils.isDigitsOnly(usuarionuevobusqueda)) /*|| Integer.parseInt(usuarionuevobusqueda)!=0*/){
            wherecedula=" and ( ";
            wherecedula=wherecedula+"  placausuarionuevo like '%"+usuarionuevobusqueda+"%' ";
            wherecedula=wherecedula+" or carnetusuarionuevo like '%"+usuarionuevobusqueda+"%' ";
            wherecedula=wherecedula+" or llaverousuarionuevo like '%"+usuarionuevobusqueda+"%' ";
            wherecedula=wherecedula+" ) ";
        }
        //Toast.makeText(getApplicationContext(),wherecedula,Toast.LENGTH_SHORT).show();
        //wherecedula="";
        SQLiteDatabase db=conn.getReadableDatabase();
        Usuarionuevo usuarionuevo=null;
        listaUsuarionuevos= new ArrayList<Usuarionuevo>();
        Cursor cursor=db.rawQuery("select * from "+ Utilidades.tabla_usuarionuevos+" where deleted_at is null "+wherecedula+"  order by id desc ",null);
        while(cursor.moveToNext()){
            usuarionuevo=new Usuarionuevo(
                    cursor.getString(cursor.getColumnIndex("id"))
                    ,cursor.getString(cursor.getColumnIndex("codigousuarionuevo"))
                    ,cursor.getString(cursor.getColumnIndex("nombresusuarionuevo"))
                    ,cursor.getString(cursor.getColumnIndex("apellidosusuarionuevo"))
                    ,cursor.getString(cursor.getColumnIndex("cedulausuarionuevo"))
                    ,cursor.getString(cursor.getColumnIndex("telefono1usuarionuevo"))
                    ,cursor.getString(cursor.getColumnIndex("telefono2usuarionuevo"))
                    ,cursor.getString(cursor.getColumnIndex("fechaafiliacionusuarionuevo"))
                    ,cursor.getString(cursor.getColumnIndex("direccionusuarionuevo"))
                    ,cursor.getString(cursor.getColumnIndex("barriousuarionuevo"))
                    ,cursor.getString(cursor.getColumnIndex("valormensualusuarionuevo"))
                    ,cursor.getString(cursor.getColumnIndex("responsableusuarionuevo"))
                    ,cursor.getString(cursor.getColumnIndex("pa1usuarionuevo"))
                    ,cursor.getString(cursor.getColumnIndex("pa2usuarionuevo"))
                    ,cursor.getString(cursor.getColumnIndex("user_id"))
                    ,cursor.getString(cursor.getColumnIndex("groupuser_id"))
                    ,cursor.getString(cursor.getColumnIndex("todos"))
                    ,cursor.getString(cursor.getColumnIndex("excepgroupuser_id"))
                    ,cursor.getString(cursor.getColumnIndex("sistemacategoria_id"))
                    ,cursor.getString(cursor.getColumnIndex("sistema_id"))

            );
            listaUsuarionuevos.add(usuarionuevo);

        }

        adapter = new UsuarionuevoAdapter(UsuarionuevosActivity.this,listaUsuarionuevos,'n');
        rv_usuarionuevos.setAdapter(adapter);
        tv_msg.setText("Resultados encontrados: " + adapter.getItemCount());
        adapter.notifyDataSetChanged();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        hideKeyboard(UsuarionuevosActivity.this);
                    }
                },
                1000);
        //btn_crearUsuarionuevo.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onBackPressed() {

    }
    public void registrarconfigUsuarionuevoSqlite(){

        String[][] valoresservicioshorainicial = new String[4][];
        valoresservicioshorainicial[0] = new String[] {"CARRO", "2000"};
        valoresservicioshorainicial[1] = new String[] {"MOTO", "1200"};
        valoresservicioshorainicial[2] = new String[] {"BICICLETA", "500"};
        valoresservicioshorainicial[3] = new String[] {"servicio", "0"};

        String[][] valoresservicioshoraadicional = new String[4][];
        valoresservicioshoraadicional[0] = new String[] {"CARRO", "300"};
        valoresservicioshoraadicional[1] = new String[] {"MOTO", "200"};
        valoresservicioshoraadicional[2] = new String[] {"BICICLETA", "100"};
        valoresservicioshoraadicional[3] = new String[] {"servicio", "0"};
        int contad=1;
        //String val=valoresservicioshorainicial.length;

        configusuarionuevolist = new ArrayList<>();
        rv_usuarionuevos.setAdapter(null);
        final ProgressDialog pDialog2 = new ProgressDialog(UsuarionuevosActivity.this);
        pDialog2.setMessage("Cargando registros...");
        pDialog2.setCancelable(false);
        pDialog2.show();

        AsyncTask<String, String, String> usuarionuevostask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {

                String urlPedidos = "http://sistemaonline.co/task_manager/v1/index.php/getconfigusuarionuevos/" +  user_id;

                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, urlPedidos, new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject jsonObject= new JSONObject(response.toString());
                            ConexionSQLiteHelper conn=new ConexionSQLiteHelper(getApplicationContext(),"sistemaonline",null,9);
                            db=conn.getWritableDatabase();
                            values=new ContentValues();
                            if (jsonObject.getString("error") == "true"){
                                tv_msg.setText("Error: " + jsonObject.getString("message"));
                            }else{
                                JSONArray configusuarionuevos1 = response.getJSONArray("configusuarionuevos");
                                if (configusuarionuevos1.length() > 0){
                                    for (int i = 0; i < configusuarionuevos1.length(); i++){
                                        JSONObject c = configusuarionuevos1.getJSONObject(i);
                                        values.put(Utilidades.CAMPO_ID,c.getString("id"));
                                        values.put(Utilidades.CAMPO_CODIGOUSUARIONUEVO,c.getString("codigousuarionuevo"));
                                        values.put(Utilidades.CAMPO_NOMBRESUSUARIONUEVO,c.getString("nombresusuarionuevo"));
                                        values.put(Utilidades.CAMPO_APELLIDOSUSUARIONUEVO,c.getString("apellidosusuarionuevo"));
                                        values.put(Utilidades.CAMPO_CEDULAUSUARIONUEVO,c.getString("cedulausuarionuevo"));
                                        values.put(Utilidades.CAMPO_TELEFONO1USUARIONUEVO,c.getString("telefono1usuarionuevo"));
                                        values.put(Utilidades.CAMPO_TELEFONO2USUARIONUEVO,c.getString("telefono2usuarionuevo"));
                                        values.put(Utilidades.CAMPO_FECHAAFILIACIONUSUARIONUEVO,c.getString("fechaafiliacionusuarionuevo"));
                                        values.put(Utilidades.CAMPO_DIRECCIONUSUARIONUEVO,c.getString("direccionusuarionuevo"));
                                        values.put(Utilidades.CAMPO_BARRIOUSUARIONUEVO,c.getString("barriousuarionuevo"));
                                        values.put(Utilidades.CAMPO_VALORMENSUALUSUARIONUEVO,c.getString("valormensualusuarionuevo"));
                                        values.put(Utilidades.CAMPO_RESPONSABLEUSUARIONUEVO,c.getString("responsableusuarionuevo"));
                                        values.put(Utilidades.CAMPO_PA1USUARIONUEVO,c.getString("pa1usuarionuevo"));
                                        values.put(Utilidades.CAMPO_PA2USUARIONUEVO,c.getString("pa2usuarionuevo"));
                                        values.put(Utilidades.CAMPO_USER_ID,c.getString("user_id"));
                                        values.put(Utilidades.CAMPO_GROUPUSER_ID,c.getString("groupuser_id"));
                                        values.put(Utilidades.CAMPO_TODOS,c.getString("todos"));
                                        values.put(Utilidades.CAMPO_EXCEPGROUPUSER_ID,c.getString("excepgroupuser_id"));
                                        values.put(Utilidades.CAMPO_SISTEMACATEGORIA_ID,c.getString("sistemacategoria_id"));
                                        values.put(Utilidades.CAMPO_SISTEMA_ID,c.getString("sistema_id"));
                                        values.put(Utilidades.CAMPO_CREATED_AT,c.getString("created_at"));
                                        values.put(Utilidades.CAMPO_UPDATED_AT,c.getString("updated_at"));
                                        values.put(Utilidades.CAMPO_DELETED_AT,c.getString("deleted_at"));

                                        Long idResulttante =db.replace(Utilidades.tabla_configusuarionuevos,Utilidades.CAMPO_ID,values);







                                    }


                                    pDialog2.hide();

                                    //btn_crearUsuarionuevo.setVisibility(View.INVISIBLE);
                                }else {
                                    tv_msg.setText("El dato no existe en el sistema");
                                    tv_msg.setTextColor(Color.parseColor("#d50000"));
                                    //btn_crearUsuarionuevo = (Button) findViewById(R.id.btn_crearUsuarionuevo);
                                    btn_crearUsuarionuevo.setVisibility(View.VISIBLE);

                                }

                            }
                            pDialog2.hide();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(),"No hay acceso a internet, revisa tu conexión!",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getApplicationContext(),"Error de servidor",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(),"Acceso denegado, API KEY invalido",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(getApplicationContext(),"Parsing error! Please try again after some time!!",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NoConnectionError) {
                            Toast.makeText(getApplicationContext(),"NoConnectionError",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof TimeoutError) {
                            Toast.makeText(getApplicationContext(),"Tiempo de conexión agotado.",Toast.LENGTH_SHORT).show();
                        }
                        pDialog2.dismiss();
                    }
                }){
                    /**
                     * Passing some request headers
                     **/
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        //headers.put("Content-Type", "application/json");
                        headers.put("Authorization", apiKey);
                        return headers;
                    }
                };

                jsonRequest.setRetryPolicy(new DefaultRetryPolicy(20000,3,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                MySingleton.getInstance(UsuarionuevosActivity.this).addRequestQueue(jsonRequest);
                return "";
            }
        };
        usuarionuevostask.execute();




        Toast.makeText(getApplicationContext(),"Registros guardado correctamente",Toast.LENGTH_SHORT).show();
        //db.execSQL("drop table usuarionuevos");



        Intent finalizar = new Intent(UsuarionuevosActivity.this, UsuarionuevosActivity.class);
                                /*finalizar.putExtra("nom_cli", nom_cli);
                                finalizar.putExtra("ced_cli", ced_cli);
                                finalizar.putExtra("subsidio", subsidio);*/

        startActivity(finalizar);
        finish();
    }
    private void loadCLientes(final String usuarionuevo,final String user_id) {
        usuarionuevolist = new ArrayList<>();
        rv_usuarionuevos.setAdapter(null);
        final ProgressDialog pDialog2 = new ProgressDialog(UsuarionuevosActivity.this);
        pDialog2.setMessage("Cargando registros...");
        pDialog2.setCancelable(false);
        pDialog2.show();

        AsyncTask<String, String, String> usuarionuevostask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                //String urlPedidos = "http://190.255.49.85/facturacion/CilAndroid/v1/index.php/buscar/usuarionuevo/" + usuarionuevo;
                String urlPedidos = "http://sistemaonline.co/task_manager/v1/index.php/getusuarionuevos/" + usuarionuevo + "/" + user_id;
                //String urlPedidos = "http://192.168.10.2/CilAndroid/v1/index.php/buscar/usuarionuevo/" + usuarionuevo;

                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, urlPedidos, new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response.toString());
                            if (jsonObject.getString("error") == "true"){
                                tv_msg.setText("Error: " + jsonObject.getString("message"));
                            }else{
                                JSONArray usuarionuevos = response.getJSONArray("usuarionuevos");
                                if (usuarionuevos.length() > 0){
                                    for (int i = 0; i < usuarionuevos.length(); i++){
                                        JSONObject c = usuarionuevos.getJSONObject(i);

                                        Usuarionuevo usuarionuevo = new Usuarionuevo(
                                                c.getString("id")
                                                ,c.getString("codigousuarionuevo")
                                                ,c.getString("nombresusuarionuevo")
                                                ,c.getString("apellidosusuarionuevo")
                                                ,c.getString("cedulausuarionuevo")
                                                ,c.getString("telefono1usuarionuevo")
                                                ,c.getString("telefono2usuarionuevo")
                                                ,c.getString("fechaafiliacionusuarionuevo")
                                                ,c.getString("direccionusuarionuevo")
                                                ,c.getString("barriousuarionuevo")
                                                ,c.getString("valormensualusuarionuevo")
                                                ,c.getString("responsableusuarionuevo")
                                                ,c.getString("pa1usuarionuevo")
                                                ,c.getString("pa2usuarionuevo")
                                                ,c.getString("user_id")
                                                ,c.getString("groupuser_id")
                                                ,c.getString("todos")
                                                ,c.getString("excepgroupuser_id")
                                                ,c.getString("sistemacategoria_id")
                                                ,c.getString("sistema_id")


                                        );

                                        usuarionuevolist.add(usuarionuevo);
                                    }
                                    adapter = new UsuarionuevoAdapter(UsuarionuevosActivity.this,usuarionuevolist,'n');
                                    rv_usuarionuevos.setAdapter(adapter);
                                    tv_msg.setText("Resultados encontrados: " + adapter.getItemCount());
                                    adapter.notifyDataSetChanged();
                                    pDialog2.hide();
                                    new android.os.Handler().postDelayed(
                                            new Runnable() {
                                                public void run() {

                                                    hideKeyboard(UsuarionuevosActivity.this);
                                                }
                                            },
                                            100);
                                    //btn_crearUsuarionuevo.setVisibility(View.INVISIBLE);
                                }else {
                                    tv_msg.setText("El dato no existe en el sistema");
                                    tv_msg.setTextColor(Color.parseColor("#d50000"));
                                    //btn_crearUsuarionuevo = (Button) findViewById(R.id.btn_crearUsuarionuevo);
                                    btn_crearUsuarionuevo.setVisibility(View.VISIBLE);

                                }

                            }
                            pDialog2.hide();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(),"No hay acceso a internet, revisa tu conexión!",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getApplicationContext(),"Error de servidor",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(),"Acceso denegado, API KEY invalido",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(getApplicationContext(),"Parsing error! Please try again after some time!!",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NoConnectionError) {
                            Toast.makeText(getApplicationContext(),"NoConnectionError",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof TimeoutError) {
                            Toast.makeText(getApplicationContext(),"Tiempo de conexión agotado.",Toast.LENGTH_SHORT).show();
                        }
                        pDialog2.dismiss();
                    }
                }){
                    /**
                     * Passing some request headers
                     **/
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        //headers.put("Content-Type", "application/json");
                        headers.put("Authorization", apiKey);
                        return headers;
                    }
                };

                jsonRequest.setRetryPolicy(new DefaultRetryPolicy(20000,3,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                MySingleton.getInstance(UsuarionuevosActivity.this).addRequestQueue(jsonRequest);
                return "";
            }
        };

        usuarionuevostask.execute();
    }

    private void sincronizarusuarionuevo(final String estado) {

        String wherecedula="";
        if((!TextUtils.isDigitsOnly(estado)) /*|| Integer.parseInt(usuarionuevobusqueda)!=0*/){
            wherecedula=" and ( ";
            wherecedula=wherecedula+"  placausuarionuevo like '%"+estado+"%' ";
            wherecedula=wherecedula+" or carnetusuarionuevo like '%"+estado+"%' ";
            wherecedula=wherecedula+" or llaverousuarionuevo like '%"+estado+"%' ";
            wherecedula=wherecedula+" ) ";
        }
        //Toast.makeText(getApplicationContext(),wherecedula,Toast.LENGTH_SHORT).show();
        //wherecedula="";
        db=conn.getReadableDatabase();
        Usuarionuevo usuarionuevo=null;
        listaUsuarionuevos= new ArrayList<Usuarionuevo>();
        Cursor cursor=db.rawQuery("select * from "+ Utilidades.tabla_usuarionuevos+" where tiempousuarionuevo<>'' and deleted_at is null "+wherecedula+"  order by tiempousuarionuevo,id desc ",null);
        sep=" ";
        cadena="";

        while(cursor.moveToNext()){
            cadena=cadena+sep+"(";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("codigousuarionuevo"))+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("nombresusuarionuevo"))+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("apellidosusuarionuevo"))+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("cedulausuarionuevo"))+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("telefono1usuarionuevo"))+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("telefono2usuarionuevo"))+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("fechaafiliacionusuarionuevo"))+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("direccionusuarionuevo"))+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("barriousuarionuevo"))+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("valormensualusuarionuevo"))+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("responsableusuarionuevo"))+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("pa1usuarionuevo"))+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("pa2usuarionuevo"))+"',";

            cadena=cadena+"'"+user_id+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("groupuser_id"))+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("todos"))+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("excepgroupuser_id"))+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("sistemacategoria_id"))+"',";
            cadena=cadena+(cursor.getString(cursor.getColumnIndex("sistema_id"))==null?"NULL,":"'"+cursor.getString(cursor.getColumnIndex("sistema_id"))+"',");
            cadena=cadena+(cursor.getString(cursor.getColumnIndex("created_at"))==null?"NULL,":"'"+cursor.getString(cursor.getColumnIndex("created_at"))+"',");
            cadena=cadena+(cursor.getString(cursor.getColumnIndex("updated_at"))==null?"NULL,":"'"+cursor.getString(cursor.getColumnIndex("updated_at"))+"',");
            cadena=cadena+(cursor.getString(cursor.getColumnIndex("deleted_at"))==null?"NULL":"'"+cursor.getString(cursor.getColumnIndex("deleted_at"))+"'");

            cadena=cadena+")";
            sep=",";

        }
        if(cadena!="") {
            cadena = "INSERT INTO usuarionuevos (placausuarionuevo, serviciousuarionuevo, carnetusuarionuevo, fechausuarionuevo, horausuarionuevo, observacioningresousuarionuevo, llaverousuarionuevo, obserbacionsalidausuarionuevo, descuentousuarionuevo, tiempousuarionuevo, tarifahorainicialusuarionuevo, tarifahoraadicionalusuarionuevo, subtotaladicionalusuarionuevo, subtotalusuarionuevo, ivausuarionuevo, totalusuarionuevo, pa1usuarionuevo, pa2usuarionuevo, user_id, groupuser_id, todos, excepgroupuser_id, sistemacategoria_id, sistema_id, created_at, updated_at, deleted_at) VALUES " + cadena;
        }






        final ProgressDialog pDialog2 = new ProgressDialog(UsuarionuevosActivity.this);
        pDialog2.setMessage("Actualizando...");
        pDialog2.setCancelable(false);
        pDialog2.show();

        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {


                String urlVenta = "http://sistemaonline.co/task_manager/v1/index.php/actualizarusuarionuevosinternet";


                StringRequest jsonRequest = new StringRequest(Request.Method.POST, urlVenta, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response.toString());
                            if (jsonObject.getString("error") == "true"){
                                Toast.makeText(UsuarionuevosActivity.this,"ERROR "+jsonObject.getString("message"),Toast.LENGTH_LONG).show();

                                //tv_msg.setText("Error: " + jsonObject.getString("message"));
                            }else{
                                //tv_msg.setText(jsonObject.getString("message"));
                                Toast.makeText(UsuarionuevosActivity.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                Intent finalizar = new Intent(UsuarionuevosActivity.this, UsuarionuevosActivity.class);

                                //db.execSQL("UPDATE " + Utilidades.tabla_usuarionuevos+ " SET tiempousuarionuevo =null WHERE  tiempousuarionuevo=''");

                                db.execSQL("delete from  " + Utilidades.tabla_usuarionuevos+ " WHERE tiempousuarionuevo<>'' and deleted_at is null ");

                                startActivity(finalizar);
                                finish();


                            }
                            //tv_msg.setText("Regreso por la respuesta");
                            pDialog2.hide();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(),"No hay acceso a internet, revisa tu conexión!",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getApplicationContext(),"Error de servidor",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(),"Acceso denegado, API KEY invalido",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(getApplicationContext(),"Parsing error! Please try again after some time!!",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NoConnectionError) {
                            Toast.makeText(getApplicationContext(),"NoConnectionError",Toast.LENGTH_SHORT).show();
                        } else if (error instanceof TimeoutError) {
                            Toast.makeText(getApplicationContext(),"Tiempo de conexión agotado",Toast.LENGTH_SHORT).show();
                        }
                        pDialog2.dismiss();
                    }
                }){
                    /**
                     * Passing some request headers
                     **/
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        //headers.put("Content-Type", "application/json");
                        /* headers.put("Authorization", apiKey);*/
                        //headers.put("Content-Type", "application/x-www-form-urlencoded");
                        return headers;
                    }

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
                        parameters.put("id", user_id) ;
                        parameters.put("cadena", cadena) ;






                        return checkParams(parameters);
                    };

                    private Map<String, String> checkParams(Map<String, String> map){
                        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
                            if(pairs.getValue()==null){
                                map.put(pairs.getKey(), "");
                            }
                        }
                        return map;
                    }
                };

                jsonRequest.setRetryPolicy(new DefaultRetryPolicy(50000*60,1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                MySingleton.getInstance(UsuarionuevosActivity.this).addRequestQueue(jsonRequest);
                return "";
            }
        };


        task.execute();
    }
}


/*$FIELD_BODY$*/