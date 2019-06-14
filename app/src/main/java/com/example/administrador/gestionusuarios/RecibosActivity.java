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

public class RecibosActivity extends AppCompatActivity {
    private TextView tv_vendedor, tv_recibo, tv_msg, tv_placa;
    private String user_id,apiKey,vendedor,recibo, nom_conductor, placa, cedulan;
    private EditText et_cedula;
    private RecyclerView rv_recibos;
    private GridLayoutManager glm_cli;
    private ReciboAdapter adapter;
    private List<Recibo> recibolist;
    private List<Configrecibo> configrecibolist;
    private List<Recibo> listaRecibos;
    private SharedPreferences sp;
    private Boolean verificado;
    private Button btn_crearRecibo, btn_buscarced;
    private TableRow tr_buscar;
    private String enlinea;
    private String cadena;
    private String sep;
    ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"sistemaonline",null,10);
    TimerTask task;
    SessionManager session;
    GlobalClass globalClass;
    SQLiteDatabase db;
    ContentValues values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recibos);

        globalClass=(GlobalClass)getApplicationContext();





        tv_vendedor = (TextView) findViewById(R.id.tv_vendedor);
        tv_recibo = (TextView) findViewById(R.id.tv_recibo);
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        tv_placa = (TextView) findViewById(R.id.tv_placa);
        rv_recibos = (RecyclerView) findViewById(R.id.rv_recibos);
        et_cedula = (EditText) findViewById(R.id.et_cedrecibo);
        tr_buscar = (TableRow) findViewById(R.id.tr_buscar);
        session = new SessionManager(getApplicationContext());
        sp = session.getVariables2();
        user_id=sp.getString("name","23");

        //task = (TimerTask) Comunicador.getObjeto();

        btn_crearRecibo = (Button) findViewById(R.id.btn_crearRecibo);
        btn_buscarced= (Button) findViewById(R.id.btn_buscarced);



        glm_cli = new GridLayoutManager(this,1);
        rv_recibos.setLayoutManager(glm_cli);

        if (true){
            if (getIntent().getExtras() != null){
                cedulan = getIntent().getExtras().getString("cedula_new");
                if (cedulan != null){
                    et_cedula.setText(cedulan);
                }
            }

            tv_recibo.setText("Busqueda: ");
            tr_buscar.setVisibility(View.VISIBLE);
            if(globalClass.getEnlinea()==null||globalClass.getEnlinea()=="false") {
                loadRecibosSqlite("0");
            }else {
                loadRecibosSqlite("0");
                //loadCLientes("0",user_id);
            }
            btn_buscarced.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(et_cedula.length() > 0){
                        if(globalClass.getEnlinea()==null||globalClass.getEnlinea()=="false") {
                            loadRecibosSqlite(et_cedula.getText().toString().trim());
                        }else {
                            loadRecibosSqlite(et_cedula.getText().toString().trim());
                            //loadCLientes(et_cedula.getText().toString().trim(),user_id);

                        }
                        tv_recibo.setText("se digito!: "+ et_cedula.getText().toString().trim());
                        recibo = et_cedula.getText().toString().trim();
                       /* SharedPreferences.Editor editor = sp.edit();
                        editor.putString("recibo", recibo);
                        editor.commit();*/
                    }else{
                        tv_recibo.setText("se digito!: ");
                        if(globalClass.getEnlinea()==null||globalClass.getEnlinea()=="false") {
                            loadRecibosSqlite("");
                        }else {
                            loadRecibosSqlite("");
                            //loadCLientes("0",user_id);
                        }
                        //Toast.makeText(RecibosActivity.this,"buscar"+globalClass.getEnlinea(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else if (true){
            tv_recibo.setText("Busqueda: " );


        }
        btn_crearRecibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recibonew = new Intent(RecibosActivity.this, ReciboformActivity.class);
                recibonew.putExtra("editarrecibo","1");
                startActivity(recibonew);
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
        //getMenuInflater().inflate(R.menu.menu_crear_recibo, menu);
        /*getMenuInflater().inflate(R.menu.menu_solo_salir, menu);

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
                    //loadCLientes("0",user_id);
                    loadRecibosSqlite("0");
                }else{
                    // Toast.makeText(getBaseContext(), "unChecked", Toast.LENGTH_SHORT).show();
                    globalClass.setEnlinea("false");
                    loadRecibosSqlite("0");
                }
            }
        });*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_cerrar) {
            stopService(new Intent(RecibosActivity.this, PedidosService.class));
            //Toast.makeText(CapturarDocumentoActivity.this,"Servicio Detenido",Toast.LENGTH_SHORT).show();
            task.cancel();

           // sp.edit().clear().commit();
            Intent login = new Intent(RecibosActivity.this, LoginActivity.class);
            startActivity(login);
            Toast.makeText(this, "Sesion cerrada", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }*/

        if (id == R.id.action_cerrar) {
            session.logoutUser();
            Intent login = new Intent(RecibosActivity.this, LoginActivity.class);
            startActivity(login);
            Toast.makeText(this, "Sesion cerrada", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }
        if(true){
            //registrarconfigReciboSqlite();
        }
        /*if (id == R.id.action_actualizarrecibo_internet) {

            sincronizarrecibo("0");

            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
    private void loadRecibosSqlite(final String recibobusqueda){
        String wherecedula="";
        if((!TextUtils.isDigitsOnly(recibobusqueda)) /*|| Integer.parseInt(recibobusqueda)!=0*/){
            wherecedula=" and ( ";
            wherecedula=wherecedula+"  placarecibo like '%"+recibobusqueda+"%' ";
            wherecedula=wherecedula+" or carnetrecibo like '%"+recibobusqueda+"%' ";
            wherecedula=wherecedula+" or llaverorecibo like '%"+recibobusqueda+"%' ";
            wherecedula=wherecedula+" ) ";
        }
        //Toast.makeText(getApplicationContext(),wherecedula,Toast.LENGTH_SHORT).show();
        //wherecedula="";
        SQLiteDatabase db=conn.getReadableDatabase();
        Recibo recibo=null;
        listaRecibos= new ArrayList<Recibo>();
        Cursor cursor=db.rawQuery("select * from "+ Utilidades.tabla_recibos+" where deleted_at is null "+wherecedula+"  order by id desc ",null);
        while(cursor.moveToNext()){
            recibo=new Recibo(
                    cursor.getString(cursor.getColumnIndex("id"))
                    ,cursor.getString(cursor.getColumnIndex("usuarioidrecibo"))
                    ,cursor.getString(cursor.getColumnIndex("servicioidrecibo"))
                    ,cursor.getString(cursor.getColumnIndex("productoidrecibo"))
                    ,cursor.getString(cursor.getColumnIndex("aniorecibo"))
                    ,cursor.getString(cursor.getColumnIndex("mesrecibo"))
                    ,cursor.getString(cursor.getColumnIndex("valorrecibo"))
                    ,cursor.getString(cursor.getColumnIndex("valornetorecibo"))
                    ,cursor.getString(cursor.getColumnIndex("pagoanticipadorecibo"))
                    ,cursor.getString(cursor.getColumnIndex("estadorecibo"))
                    ,cursor.getString(cursor.getColumnIndex("fechalimiterecibo"))
                    ,cursor.getString(cursor.getColumnIndex("descuentorecibo"))
                    ,cursor.getString(cursor.getColumnIndex("formapagorecibo"))
                    ,cursor.getString(cursor.getColumnIndex("fechageneradorecibo"))
                    ,cursor.getString(cursor.getColumnIndex("horarecibo"))
                    ,cursor.getString(cursor.getColumnIndex("cantidadrecibo"))
                    ,cursor.getString(cursor.getColumnIndex("fechapagorecibo"))
                    ,cursor.getString(cursor.getColumnIndex("ivarecibo"))
                    ,cursor.getString(cursor.getColumnIndex("pa1recibo"))
                    ,cursor.getString(cursor.getColumnIndex("pa2recibo"))
                    ,cursor.getString(cursor.getColumnIndex("user_id"))
                    ,cursor.getString(cursor.getColumnIndex("groupuser_id"))
                    ,cursor.getString(cursor.getColumnIndex("todos"))
                    ,cursor.getString(cursor.getColumnIndex("excepgroupuser_id"))
                    ,cursor.getString(cursor.getColumnIndex("sistemacategoria_id"))
                    ,cursor.getString(cursor.getColumnIndex("sistema_id"))

            );
            listaRecibos.add(recibo);

        }

        adapter = new ReciboAdapter(RecibosActivity.this,listaRecibos,'n');
        rv_recibos.setAdapter(adapter);
        tv_msg.setText("Resultados encontrados: " + adapter.getItemCount());
        adapter.notifyDataSetChanged();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        hideKeyboard(RecibosActivity.this);
                    }
                },
                1000);
        //btn_crearRecibo.setVisibility(View.INVISIBLE);
    }
   /* @Override
    public void onBackPressed() {

    }*/
    /*public void registrarconfigReciboSqlite(){

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

        configrecibolist = new ArrayList<>();
        rv_recibos.setAdapter(null);
        final ProgressDialog pDialog2 = new ProgressDialog(RecibosActivity.this);
        pDialog2.setMessage("Cargando registros...");
        pDialog2.setCancelable(false);
        pDialog2.show();

        AsyncTask<String, String, String> recibostask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {

                String urlPedidos = "http://sistemaonline.co/task_manager/v1/index.php/getconfigrecibos/" +  user_id;

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
                                JSONArray configrecibos1 = response.getJSONArray("configrecibos");
                                if (configrecibos1.length() > 0){
                                    for (int i = 0; i < configrecibos1.length(); i++){
                                        JSONObject c = configrecibos1.getJSONObject(i);
                                        values.put(Utilidades.CAMPO_ID,c.getString("id"));
                                        values.put(Utilidades.CAMPO_USUARIOIDRECIBO,c.getString("usuarioidrecibo"));
                                        values.put(Utilidades.CAMPO_SERVICIOIDRECIBO,c.getString("servicioidrecibo"));
                                        values.put(Utilidades.CAMPO_PRODUCTOIDRECIBO,c.getString("productoidrecibo"));
                                        values.put(Utilidades.CAMPO_ANIORECIBO,c.getString("aniorecibo"));
                                        values.put(Utilidades.CAMPO_MESRECIBO,c.getString("mesrecibo"));
                                        values.put(Utilidades.CAMPO_VALORRECIBO,c.getString("valorrecibo"));
                                        values.put(Utilidades.CAMPO_VALORNETORECIBO,c.getString("valornetorecibo"));
                                        values.put(Utilidades.CAMPO_PAGOANTICIPADORECIBO,c.getString("pagoanticipadorecibo"));
                                        values.put(Utilidades.CAMPO_ESTADORECIBO,c.getString("estadorecibo"));
                                        values.put(Utilidades.CAMPO_FECHALIMITERECIBO,c.getString("fechalimiterecibo"));
                                        values.put(Utilidades.CAMPO_DESCUENTORECIBO,c.getString("descuentorecibo"));
                                        values.put(Utilidades.CAMPO_FORMAPAGORECIBO,c.getString("formapagorecibo"));
                                        values.put(Utilidades.CAMPO_FECHAGENERADORECIBO,c.getString("fechageneradorecibo"));
                                        values.put(Utilidades.CAMPO_FECHAPAGORECIBO,c.getString("fechapagorecibo"));
                                        values.put(Utilidades.CAMPO_CANTIDADRECIBO,c.getString("cantidadrecibo"));
                                        values.put(Utilidades.CAMPO_PA1RECIBO,c.getString("pa1recibo"));
                                        values.put(Utilidades.CAMPO_PA2RECIBO,c.getString("pa2recibo"));
                                        values.put(Utilidades.CAMPO_USER_ID,c.getString("user_id"));
                                        values.put(Utilidades.CAMPO_GROUPUSER_ID,c.getString("groupuser_id"));
                                        values.put(Utilidades.CAMPO_TODOS,c.getString("todos"));
                                        values.put(Utilidades.CAMPO_EXCEPGROUPUSER_ID,c.getString("excepgroupuser_id"));
                                        values.put(Utilidades.CAMPO_SISTEMACATEGORIA_ID,c.getString("sistemacategoria_id"));
                                        values.put(Utilidades.CAMPO_SISTEMA_ID,c.getString("sistema_id"));
                                        values.put(Utilidades.CAMPO_CREATED_AT,c.getString("created_at"));
                                        values.put(Utilidades.CAMPO_UPDATED_AT,c.getString("updated_at"));
                                        values.put(Utilidades.CAMPO_DELETED_AT,c.getString("deleted_at"));

                                        Long idResulttante =db.replace(Utilidades.tabla_configrecibos,Utilidades.CAMPO_ID,values);







                                    }


                                    pDialog2.hide();

                                    //btn_crearRecibo.setVisibility(View.INVISIBLE);
                                }else {
                                    tv_msg.setText("El dato no existe en el sistema");
                                    tv_msg.setTextColor(Color.parseColor("#d50000"));
                                    //btn_crearRecibo = (Button) findViewById(R.id.btn_crearRecibo);
                                    btn_crearRecibo.setVisibility(View.VISIBLE);

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

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        //headers.put("Content-Type", "application/json");
                        headers.put("Authorization", apiKey);
                        return headers;
                    }
                };

                jsonRequest.setRetryPolicy(new DefaultRetryPolicy(20000,3,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                MySingleton.getInstance(RecibosActivity.this).addRequestQueue(jsonRequest);
                return "";
            }
        };
        recibostask.execute();




        Toast.makeText(getApplicationContext(),"Registros guardado correctamente",Toast.LENGTH_SHORT).show();
        //db.execSQL("drop table recibos");



        Intent finalizar = new Intent(RecibosActivity.this, RecibosActivity.class);


        startActivity(finalizar);
        finish();
    }*/
    private void loadCLientes(final String recibo,final String user_id) {
        recibolist = new ArrayList<>();
        rv_recibos.setAdapter(null);
        final ProgressDialog pDialog2 = new ProgressDialog(RecibosActivity.this);
        pDialog2.setMessage("Cargando registros...");
        pDialog2.setCancelable(false);
        pDialog2.show();

        AsyncTask<String, String, String> recibostask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                //String urlPedidos = "http://190.255.49.85/facturacion/CilAndroid/v1/index.php/buscar/recibo/" + recibo;
                String urlPedidos = "http://sistemaonline.co/task_manager/v1/index.php/getrecibos/" + recibo + "/" + user_id;
                //String urlPedidos = "http://192.168.10.2/CilAndroid/v1/index.php/buscar/recibo/" + recibo;

                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, urlPedidos, new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response.toString());
                            if (jsonObject.getString("error") == "true"){
                                tv_msg.setText("Error: " + jsonObject.getString("message"));
                            }else{
                                JSONArray recibos = response.getJSONArray("recibos");
                                if (recibos.length() > 0){
                                    for (int i = 0; i < recibos.length(); i++){
                                        JSONObject c = recibos.getJSONObject(i);

                                        Recibo recibo = new Recibo(
                                                c.getString("id")
                                                ,c.getString("usuarioidrecibo")
                                                ,c.getString("servicioidrecibo")
                                                ,c.getString("productoidrecibo")
                                                ,c.getString("aniorecibo")
                                                ,c.getString("mesrecibo")
                                                ,c.getString("valorrecibo")
                                                ,c.getString("valornetorecibo")
                                                ,c.getString("pagoanticipadorecibo")
                                                ,c.getString("estadorecibo")
                                                ,c.getString("fechalimiterecibo")
                                                ,c.getString("descuentorecibo")
                                                ,c.getString("formapagorecibo")
                                                ,c.getString("fechageneradorecibo")
                                                ,c.getString("horarecibo")
                                                ,c.getString("cantidadrecibo")
                                                ,c.getString("fechapagorecibo")
                                                ,c.getString("ivarecibo")
                                                ,c.getString("pa1recibo")
                                                ,c.getString("pa2recibo")
                                                ,c.getString("user_id")
                                                ,c.getString("groupuser_id")
                                                ,c.getString("todos")
                                                ,c.getString("excepgroupuser_id")
                                                ,c.getString("sistemacategoria_id")
                                                ,c.getString("sistema_id")


                                        );

                                        recibolist.add(recibo);
                                    }
                                    adapter = new ReciboAdapter(RecibosActivity.this,recibolist,'n');
                                    rv_recibos.setAdapter(adapter);
                                    tv_msg.setText("Resultados encontrados: " + adapter.getItemCount());
                                    adapter.notifyDataSetChanged();
                                    pDialog2.hide();
                                    new android.os.Handler().postDelayed(
                                            new Runnable() {
                                                public void run() {

                                                    hideKeyboard(RecibosActivity.this);
                                                }
                                            },
                                            100);
                                    //btn_crearRecibo.setVisibility(View.INVISIBLE);
                                }else {
                                    tv_msg.setText("El dato no existe en el sistema");
                                    tv_msg.setTextColor(Color.parseColor("#d50000"));
                                    //btn_crearRecibo = (Button) findViewById(R.id.btn_crearRecibo);
                                    btn_crearRecibo.setVisibility(View.VISIBLE);

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
                MySingleton.getInstance(RecibosActivity.this).addRequestQueue(jsonRequest);
                return "";
            }
        };

        recibostask.execute();
    }

    /*private void sincronizarrecibo(final String estado) {

        String wherecedula="";
        if((!TextUtils.isDigitsOnly(estado)) ){
            wherecedula=" and ( ";
            wherecedula=wherecedula+"  placarecibo like '%"+estado+"%' ";
            wherecedula=wherecedula+" or carnetrecibo like '%"+estado+"%' ";
            wherecedula=wherecedula+" or llaverorecibo like '%"+estado+"%' ";
            wherecedula=wherecedula+" ) ";
        }

        db=conn.getReadableDatabase();
        Recibo recibo=null;
        listaRecibos= new ArrayList<Recibo>();
        Cursor cursor=db.rawQuery("select * from "+ Utilidades.tabla_recibos+" where tiemporecibo<>'' and deleted_at is null "+wherecedula+"  order by id desc ",null);
        sep=" ";
        cadena="";

        while(cursor.moveToNext()){
            cadena=cadena+sep+"(";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("usuarioidrecibo"))+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("servicioidrecibo"))+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("productoidrecibo"))+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("aniorecibo"))+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("mesrecibo"))+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("valorrecibo"))+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("valornetorecibo"))+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("pagoanticipadorecibo"))+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("estadorecibo"))+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("fechalimiterecibo"))+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("descuentorecibo"))+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("formapagorecibo"))+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("fechageneradorecibo"))+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("fechapagorecibo"))+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("cantidadrecibo"))+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("pa1recibo"))+"',";
            cadena=cadena+"'"+cursor.getString(cursor.getColumnIndex("pa2recibo"))+"',";

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
            cadena = "INSERT INTO recibos (placarecibo, serviciorecibo, carnetrecibo, fecharecibo, horarecibo, observacioningresorecibo, llaverorecibo, obserbacionsalidarecibo, descuentorecibo, tiemporecibo, tarifahorainicialrecibo, tarifahoraadicionalrecibo, subtotaladicionalrecibo, subtotalrecibo, ivarecibo, totalrecibo, pa1recibo, pa2recibo, user_id, groupuser_id, todos, excepgroupuser_id, sistemacategoria_id, sistema_id, created_at, updated_at, deleted_at) VALUES " + cadena;
        }






        final ProgressDialog pDialog2 = new ProgressDialog(RecibosActivity.this);
        pDialog2.setMessage("Actualizando...");
        pDialog2.setCancelable(false);
        pDialog2.show();

        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {


                String urlVenta = "http://sistemaonline.co/task_manager/v1/index.php/actualizarrecibosinternet";


                StringRequest jsonRequest = new StringRequest(Request.Method.POST, urlVenta, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response.toString());
                            if (jsonObject.getString("error") == "true"){
                                Toast.makeText(RecibosActivity.this,"ERROR "+jsonObject.getString("message"),Toast.LENGTH_LONG).show();

                                //tv_msg.setText("Error: " + jsonObject.getString("message"));
                            }else{
                                //tv_msg.setText(jsonObject.getString("message"));
                                Toast.makeText(RecibosActivity.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                Intent finalizar = new Intent(RecibosActivity.this, RecibosActivity.class);

                                //db.execSQL("UPDATE " + Utilidades.tabla_recibos+ " SET tiemporecibo =null WHERE  tiemporecibo=''");

                                db.execSQL("delete from  " + Utilidades.tabla_recibos+ " WHERE tiemporecibo<>'' and deleted_at is null ");

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

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();

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
                MySingleton.getInstance(RecibosActivity.this).addRequestQueue(jsonRequest);
                return "";
            }
        };


        task.execute();
    }*/
}


/*$FIELD_BODY$*/