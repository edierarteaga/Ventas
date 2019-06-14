package com.example.administrador.gestionusuarios;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ImageButton;
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

public  class ProductosActivity extends AppCompatActivity implements View.OnClickListener, BlankFragment.OnFragmentInteractionListener{
    private TextView tv_vendedor, tv_producto, tv_msg, tv_placa;
    private String user_id,apiKey,vendedor,producto, nom_conductor, placa, cedulan;
    private EditText et_cedula;
    private RecyclerView rv_productos;
    private GridLayoutManager glm_cli;
    private ProductoAdapter adapter;
    private List<Producto> productolist;
    private List<Configproducto> configproductolist;
    private List<Producto> listaProductos;
    private SharedPreferences sp;
    private Boolean verificado;
    private Button  btn_buscarced;
    private FloatingActionButton btn_crearProducto;
    private ImageButton clearBtn;
    private TableRow tr_buscar;
    private String enlinea;
    private String cadena;
    private String sep;

    ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"sistemaonline",null,10);
    TimerTask task;
    SessionManager session;
    public static GlobalClass globalClass;
    public static Integer valorTotalPedido=0;
    public static List<Producto> productopedidolist;
    private static Button  btn_totalValorPedido;
    SQLiteDatabase db;
    ContentValues values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        globalClass=(GlobalClass)getApplicationContext();




        productopedidolist = new ArrayList<>();
        tv_vendedor = (TextView) findViewById(R.id.tv_vendedor);
        tv_producto = (TextView) findViewById(R.id.tv_producto);
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        tv_placa = (TextView) findViewById(R.id.tv_placa);
        rv_productos = (RecyclerView) findViewById(R.id.rv_productos);
        et_cedula = (EditText) findViewById(R.id.et_cedproducto);
        tr_buscar = (TableRow) findViewById(R.id.tr_buscar);
        session = new SessionManager(getApplicationContext());
        sp = session.getVariables2();
        user_id=sp.getString("name","23");

        //task = (TimerTask) Comunicador.getObjeto();

        btn_crearProducto = (FloatingActionButton) findViewById(R.id.btn_crearProducto);
        btn_buscarced= (Button) findViewById(R.id.btn_buscarced);
        btn_totalValorPedido= (Button) findViewById(R.id.btnTotalValorPedido);

        if(globalClass.getEnlinea()==null||globalClass.getEnlinea()=="false"){
            btn_totalValorPedido.setVisibility(View.VISIBLE);
        }else{
            btn_totalValorPedido.setVisibility(View.GONE);
        }
        actualizarVaorPedido(this);
        glm_cli = new GridLayoutManager(this,1);
        rv_productos.setLayoutManager(glm_cli);

        if (true){
            if (getIntent().getExtras() != null){
                cedulan = getIntent().getExtras().getString("cedula_new");
                if (cedulan != null){
                    et_cedula.setText(cedulan);
                }
            }

            tv_producto.setText("Busqueda: ");
            tr_buscar.setVisibility(View.VISIBLE);
            if(/*globalClass.getEnlinea()==null||globalClass.getEnlinea()=="false"*/true) {
                loadProductosSqlite("0");
            }else {

                //loadCLientes("0",user_id);
            }
            btn_buscarced.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(et_cedula.length() > 0){
                        if(/*globalClass.getEnlinea()==null||globalClass.getEnlinea()=="false"*/true) {
                            loadProductosSqlite(et_cedula.getText().toString().trim());
                        }else {
                            //loadCLientes(et_cedula.getText().toString().trim(),user_id);

                        }
                        tv_producto.setText("se digito!: "+ et_cedula.getText().toString().trim());
                        producto = et_cedula.getText().toString().trim();
                       /* SharedPreferences.Editor editor = sp.edit();
                        editor.putString("producto", producto);
                        editor.commit();*/
                    }else{
                        tv_producto.setText("se digito!: ");
                        if(/*globalClass.getEnlinea()==null||globalClass.getEnlinea()=="false"*/true) {
                            loadProductosSqlite("");
                        }else {

                            //loadCLientes("0",user_id);
                        }
                        //Toast.makeText(ProductosActivity.this,"buscar"+globalClass.getEnlinea(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else if (true){
            tv_producto.setText("Busqueda: " );


        }
        btn_crearProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent productonew = new Intent(ProductosActivity.this, ProductoformActivity.class);
                productonew.putExtra("editarproducto","1");
                startActivity(productonew);

            }
        });

        btn_totalValorPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recibonew = new Intent(ProductosActivity.this, ReciboformActivity.class);
                recibonew.putExtra("autoguardar","1");
                startActivity(recibonew);

            }
        });

    }
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
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
        //getMenuInflater().inflate(R.menu.menu_crear_producto, menu);
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
                    //loadCLientes("0",user_id);
                    loadProductosSqlite("0");
                    btn_totalValorPedido.setVisibility(View.GONE);
                }else{
                    // Toast.makeText(getBaseContext(), "unChecked", Toast.LENGTH_SHORT).show();
                    globalClass.setEnlinea("false");
                    loadProductosSqlite("0");
                    btn_totalValorPedido.setVisibility(View.VISIBLE);

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
            stopService(new Intent(ProductosActivity.this, PedidosService.class));
            //Toast.makeText(CapturarDocumentoActivity.this,"Servicio Detenido",Toast.LENGTH_SHORT).show();
            task.cancel();

           // sp.edit().clear().commit();
            Intent login = new Intent(ProductosActivity.this, LoginActivity.class);
            startActivity(login);
            Toast.makeText(this, "Sesion cerrada", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }*/

        if (id == R.id.action_cerrar) {
            session.logoutUser();
            Intent login = new Intent(ProductosActivity.this, LoginActivity.class);
            startActivity(login);
            Toast.makeText(this, "Sesion cerrada", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }
        if (id == R.id.misventas) {

            Intent misventas = new Intent(ProductosActivity.this, RecibosActivity.class);
            startActivity(misventas);

            //finish();
            return true;
        }
        if (id == R.id.eliminarrecibos) {

            ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"sistemaonline",null,10);
            SQLiteDatabase db=conn.getWritableDatabase();
            //db.execSQL("delete from  " + Utilidades.tabla_recibos+ " ");

            db.execSQL("delete from  " + Utilidades.tabla_recpros+ " ");
            db.close();
            Toast.makeText(getApplicationContext(),"Registros Eliminado",Toast.LENGTH_LONG).show();




            return true;
        }
        if (id == R.id.nuevaventa) {

            ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"sistemaonline",null,10);
            SQLiteDatabase db=conn.getWritableDatabase();
            String insert="update "+ Utilidades.tabla_productos+"  set Pa2producto='0' ";
            db.execSQL(insert);
            db.close();
            loadProductosSqlite(et_cedula.getText().toString().trim());
            actualizarVaorPedido(this);
           // Toast.makeText(getApplicationContext(),"Registros Eliminado",Toast.LENGTH_LONG).show();
            return true;
        }
        if(true){
            //registrarconfigProductoSqlite();
        }
        /*if (id == R.id.action_actualizarproducto_internet) {

            sincronizarproducto("0");

            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    public static boolean actualizarVaorPedido3(){
        for(int x=0;x<productopedidolist.size();x++) {
            if(productopedidolist.get(x).getExistenciasproducto()!="0") {
                //valorTotalPedido++;
            }
        }
        btn_totalValorPedido.setText("$"+valorTotalPedido);
        return true;
    }
    public static boolean actualizarVaorPedido(Context context){
        valorTotalPedido=0;
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(context,"sistemaonline",null,10);
        SQLiteDatabase db=conn.getReadableDatabase();
        Producto producto=null;
        productopedidolist.clear();;
        Cursor cursor=db.rawQuery("select * from "+ Utilidades.tabla_productos+" where deleted_at is null and pa2producto<>0   order by pa2producto desc,id desc ",null);
        while(cursor.moveToNext()){
            producto=new Producto(
                    cursor.getString(cursor.getColumnIndex("id"))
                    ,cursor.getString(cursor.getColumnIndex("nombreproducto"))
                    ,cursor.getString(cursor.getColumnIndex("descripcionproducto"))
                    ,cursor.getString(cursor.getColumnIndex("marcaproducto"))
                    ,cursor.getString(cursor.getColumnIndex("categoriaproducto"))
                    ,cursor.getString(cursor.getColumnIndex("referenciaproducto"))
                    ,cursor.getString(cursor.getColumnIndex("preciocompraproducto"))
                    ,cursor.getString(cursor.getColumnIndex("precioventaproducto"))
                    ,cursor.getString(cursor.getColumnIndex("existenciasproducto"))
                    ,cursor.getString(cursor.getColumnIndex("minimoexistenciasproducto"))
                    ,cursor.getString(cursor.getColumnIndex("maximoexistenciasproducto"))
                    ,cursor.getString(cursor.getColumnIndex("proveedorproducto"))
                    ,cursor.getString(cursor.getColumnIndex("pa1producto"))
                    ,cursor.getString(cursor.getColumnIndex("pa2producto"))
                    ,cursor.getString(cursor.getColumnIndex("user_id"))
                    ,cursor.getString(cursor.getColumnIndex("groupuser_id"))
                    ,cursor.getString(cursor.getColumnIndex("todos"))
                    ,cursor.getString(cursor.getColumnIndex("excepgroupuser_id"))
                    ,cursor.getString(cursor.getColumnIndex("sistemacategoria_id"))
                    ,cursor.getString(cursor.getColumnIndex("sistema_id"))

            );
            productopedidolist.add(producto);

        }
        for(int x=0;x<productopedidolist.size();x++) {
            if(Integer.parseInt((productopedidolist.get(x).getPa2Producto().toString().equals("")?"0":productopedidolist.get(x).getPa2Producto()))!=0) {
                valorTotalPedido=valorTotalPedido+Integer.parseInt((productopedidolist.get(x).getPa2Producto().toString().equals("")?"0":productopedidolist.get(x).getPa2Producto()))*Integer.parseInt((productopedidolist.get(x).getPrecioventaproducto().toString().equals("")?"0":productopedidolist.get(x).getPrecioventaproducto()));
            }
        }
        btn_totalValorPedido.setText("$"+valorTotalPedido);
        return true;
    }

    private void loadProductosSqlite(final String productobusqueda){
        String wherecedula="";
        if((!TextUtils.isDigitsOnly(productobusqueda)) /*|| Integer.parseInt(productobusqueda)!=0*/){
            wherecedula=" and ( ";
            wherecedula=wherecedula+"  nombreproducto like '%"+productobusqueda+"%' ";
            wherecedula=wherecedula+" or nombreproducto like '%"+productobusqueda+"%' ";
            wherecedula=wherecedula+" or nombreproducto like '%"+productobusqueda+"%' ";
            wherecedula=wherecedula+" ) ";
        }
        //Toast.makeText(getApplicationContext(),wherecedula,Toast.LENGTH_SHORT).show();
        //wherecedula="";
        SQLiteDatabase db=conn.getReadableDatabase();
        Producto producto=null;
        listaProductos= new ArrayList<Producto>();
        Cursor cursor=db.rawQuery("select * from "+ Utilidades.tabla_productos+" where deleted_at is null "+wherecedula+"  order by pa2producto desc, id desc ",null);
        while(cursor.moveToNext()){
            producto=new Producto(
                    cursor.getString(cursor.getColumnIndex("id"))
                    ,cursor.getString(cursor.getColumnIndex("nombreproducto"))
                    ,cursor.getString(cursor.getColumnIndex("descripcionproducto"))
                    ,cursor.getString(cursor.getColumnIndex("marcaproducto"))
                    ,cursor.getString(cursor.getColumnIndex("categoriaproducto"))
                    ,cursor.getString(cursor.getColumnIndex("referenciaproducto"))
                    ,cursor.getString(cursor.getColumnIndex("preciocompraproducto"))
                    ,cursor.getString(cursor.getColumnIndex("precioventaproducto"))
                    ,cursor.getString(cursor.getColumnIndex("existenciasproducto"))
                    ,cursor.getString(cursor.getColumnIndex("minimoexistenciasproducto"))
                    ,cursor.getString(cursor.getColumnIndex("maximoexistenciasproducto"))
                    ,cursor.getString(cursor.getColumnIndex("proveedorproducto"))
                    ,cursor.getString(cursor.getColumnIndex("pa1producto"))
                    ,cursor.getString(cursor.getColumnIndex("pa2producto"))
                    ,cursor.getString(cursor.getColumnIndex("user_id"))
                    ,cursor.getString(cursor.getColumnIndex("groupuser_id"))
                    ,cursor.getString(cursor.getColumnIndex("todos"))
                    ,cursor.getString(cursor.getColumnIndex("excepgroupuser_id"))
                    ,cursor.getString(cursor.getColumnIndex("sistemacategoria_id"))
                    ,cursor.getString(cursor.getColumnIndex("sistema_id"))

            );
            listaProductos.add(producto);

        }

        adapter = new ProductoAdapter(ProductosActivity.this,listaProductos,'n');
        rv_productos.setAdapter(adapter);
        tv_msg.setText("Resultados encontrados: " + adapter.getItemCount());
        adapter.notifyDataSetChanged();
        actualizarVaorPedido(this);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        hideKeyboard(ProductosActivity.this);
                    }
                },
                1);
        //btn_crearProducto.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onBackPressed() {

    }

    private void loadCLientes(final String producto,final String user_id) {
        productolist = new ArrayList<>();
        rv_productos.setAdapter(null);
        final ProgressDialog pDialog2 = new ProgressDialog(ProductosActivity.this);
        pDialog2.setMessage("Cargando registros...");
        pDialog2.setCancelable(false);
        pDialog2.show();

        AsyncTask<String, String, String> productostask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                //String urlPedidos = "http://190.255.49.85/facturacion/CilAndroid/v1/index.php/buscar/producto/" + producto;
                String urlPedidos = "http://sistemaonline.co/task_manager/v1/index.php/getproductos/" + producto + "/" + user_id;
                //String urlPedidos = "http://192.168.10.2/CilAndroid/v1/index.php/buscar/producto/" + producto;

                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, urlPedidos, new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response.toString());
                            if (jsonObject.getString("error") == "true"){
                                tv_msg.setText("Error: " + jsonObject.getString("message"));
                            }else{
                                JSONArray productos = response.getJSONArray("productos");
                                if (productos.length() > 0){
                                    for (int i = 0; i < productos.length(); i++){
                                        JSONObject c = productos.getJSONObject(i);

                                        Producto producto = new Producto(
                                                c.getString("id")
                                                ,c.getString("nombreproducto")
                                                ,c.getString("descripcionproducto")
                                                ,c.getString("marcaproducto")
                                                ,c.getString("categoriaproducto")
                                                ,c.getString("referenciaproducto")
                                                ,c.getString("preciocompraproducto")
                                                ,c.getString("precioventaproducto")
                                                ,c.getString("existenciasproducto")
                                                ,c.getString("minimoexistenciasproducto")
                                                ,c.getString("maximoexistenciasproducto")
                                                ,c.getString("proveedorproducto")
                                                ,c.getString("pa1producto")
                                                ,c.getString("pa2producto")
                                                ,c.getString("user_id")
                                                ,c.getString("groupuser_id")
                                                ,c.getString("todos")
                                                ,c.getString("excepgroupuser_id")
                                                ,c.getString("sistemacategoria_id")
                                                ,c.getString("sistema_id")


                                        );

                                        productolist.add(producto);
                                    }
                                    adapter = new ProductoAdapter(ProductosActivity.this,productolist,'n');
                                    rv_productos.setAdapter(adapter);
                                    tv_msg.setText("Resultados encontrados: " + adapter.getItemCount());
                                    adapter.notifyDataSetChanged();
                                    pDialog2.hide();
                                    new android.os.Handler().postDelayed(
                                            new Runnable() {
                                                public void run() {

                                                    hideKeyboard(ProductosActivity.this);
                                                }
                                            },
                                            100);
                                    //btn_crearProducto.setVisibility(View.INVISIBLE);
                                }else {
                                    tv_msg.setText("El dato no existe en el sistema");
                                    tv_msg.setTextColor(Color.parseColor("#d50000"));
                                    //btn_crearProducto = (Button) findViewById(R.id.btn_crearProducto);
                                    btn_crearProducto.setVisibility(View.VISIBLE);

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
                MySingleton.getInstance(ProductosActivity.this).addRequestQueue(jsonRequest);
                return "";
            }
        };

        productostask.execute();
    }


    @Override
    public void onClick(View view) {

    }

    @Override protected void onRestart() {
        super.onRestart();
        //Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
        loadProductosSqlite(et_cedula.getText().toString().trim());
        actualizarVaorPedido(this);
    }


}


/*$FIELD_BODY$*/