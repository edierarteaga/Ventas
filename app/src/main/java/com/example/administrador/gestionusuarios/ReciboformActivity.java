package com.example.administrador.gestionusuarios;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ReciboformActivity extends AppCompatActivity implements View.OnClickListener, BlankFragment.OnFragmentInteractionListener{
    private TextView placarecibo_recibos, tv_recibo, tv_msg, tv_placa;
    private String user_id,apiKey,vendedor, nom_conductor, placa, cedulan;
    private int dia,mes,anio;
    private Spinner spinner;
    private EditText fecharecibo_recibos,horarecibo_recibos;
    private static DatePickerDialog.OnDateSetListener selectorFec;
    private static TimePickerDialog.OnTimeSetListener selectorTim;
    private TimePicker timePicker1;
    private String format = "";

    private ProductoAdapter adapterproducto;
    private List<Producto> listaProductos;
    private RecyclerView rv_productos;
    private GridLayoutManager glm_cli2;

    private ImageButton icon_agregarproducto;
    private Button btnformrecibos,btnformrecibos2,btneditarrecibos,btneliminarrecibos;
    private Recibo recibo;
    private SharedPreferences sp;
    SessionManager session;
    private static final int MY_PERMISSION_REQUEST_WHRITE_EXTERNAL=1;
    public static List<Producto> productolist;
    public static int posicioproductos=-1;
    String enlinea;
    String atoguardar;
    GlobalClass globalClass;
    ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"sistemaonline",null,10);
    @Override protected void onResume() {
        super.onResume();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        hideKeyboard(ReciboformActivity.this);
                    }
                },
                1000);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reciboform);
        checkPermission();
        icon_agregarproducto=(ImageButton) findViewById(R.id.ib_agregarproducto);
        globalClass=(GlobalClass)getApplicationContext();


        // Toast.makeText(getBaseContext(), globalClass.getEnlinea(), Toast.LENGTH_SHORT).show();
       /* int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        */

        String pathDB = getDatabasePath("sistemaonline").toString();
        /*copiaBD(pathDB,Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + getPackageName() + "/" + "xxxxxx");*/

        /*exportDatabse("sistemaonline"); //si funciona */
        //((EditText)findViewById(R.id.descuentorecibo_recibos)).setHint("Dia");
        rv_productos = (RecyclerView) findViewById(R.id.rv_productos);

        glm_cli2 = new GridLayoutManager(this,1);
        rv_productos.setLayoutManager(glm_cli2);
        btnformrecibos=(Button) findViewById(R.id.btn_crearformRecibo);
        btnformrecibos2=(Button) findViewById(R.id.btn_crearformRecibo2);
        btneditarrecibos=(Button) findViewById(R.id.btn_editarrecibos);
        btneliminarrecibos=(Button) findViewById(R.id.btn_eliminarrecibos);

        //spinner = (Spinner) findViewById(R.id.serviciorecibo_recibos);
        //horarecibo_recibos=(EditText) findViewById(R.id.horarecibo_recibos);


        Calendar calendar = Calendar.getInstance();
        anio = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        /*timePicker1 = (TimePicker) findViewById(R.id.horatmp);
        int hour = timePicker1.getCurrentHour();
        int min = timePicker1.getCurrentMinute();
        showTime(hour, min);*/
        fecharecibo_recibos = (EditText) findViewById(R.id.fechageneradorecibo_recibos);
        session = new SessionManager(getApplicationContext());
        sp = session.getVariables2();
        user_id=sp.getString("name","23");
        String[] items = sp.getString("comboes","fdf,jj").split(",");

        if(sp.getString("cobrardias","false").equals("true")) {
            //((TextInputLayout) findViewById(R.id.tarifahoraadicionalrecibo_recibosl)).setHint("Dia adicional");
        }
        /*horarecibo_recibos = (EditText) findViewById(R.id.horarecibo_recibos);*/
        mostrarFecha();
        selectorFec = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                anio = year;
                mes = month;
                dia = dayOfMonth;
                mostrarFecha();
            }
        };

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items){

            public View getView(int position, View convertView,ViewGroup parent) {

                View v = super.getView(position, convertView, parent);

                ((TextView) v).setTextSize(16);
                ((TextView) v).setGravity(Gravity.CENTER);

                return v;

            }

            public View getDropDownView(int position, View convertView,ViewGroup parent) {

                View v = super.getDropDownView(position, convertView,parent);


                ((TextView) v).setGravity(Gravity.CENTER);

                return v;

            }

        };


        //registrar$MODEL_NAME_PLURAL_HUMANql();
//set the spinners adapter to the previously created one.
        //spinner.setAdapter(adapter);
        atoguardar="0";
        if(getIntent().getExtras() != null){
            recibo = getIntent().getExtras().getParcelable("recibo");
            atoguardar = getIntent().getExtras().getString("autoguardar");
        }

        if (recibo != null){

            // ((TextView)findViewById(R.id.id_recibos)).setText("" + recibo.getId());
            ((TextView)findViewById(R.id.usuarioidrecibo_recibos)).setText("" + recibo.getUsuarioidrecibo());
            ((TextView)findViewById(R.id.servicioidrecibo_recibos)).setText("" + recibo.getServicioidrecibo());
            ((TextView)findViewById(R.id.productoidrecibo_recibos)).setText("" + recibo.getProductoidrecibo());
            ((TextView)findViewById(R.id.aniorecibo_recibos)).setText("" + recibo.getAniorecibo());
            ((TextView)findViewById(R.id.mesrecibo_recibos)).setText("" + recibo.getMesrecibo());
            ((TextView)findViewById(R.id.valorrecibo_recibos)).setText("" + recibo.getValorrecibo());
            ((TextView)findViewById(R.id.valornetorecibo_recibos)).setText("" + recibo.getValornetorecibo());
            ((TextView)findViewById(R.id.pagoanticipadorecibo_recibos)).setText("" + recibo.getPagoanticipadorecibo());
            ((TextView)findViewById(R.id.estadorecibo_recibos)).setText("" + recibo.getEstadorecibo());
            ((TextView)findViewById(R.id.fechalimiterecibo_recibos)).setText("" + recibo.getFechalimiterecibo());
            ((TextView)findViewById(R.id.descuentorecibo_recibos)).setText("" + recibo.getDescuentorecibo());
            ((TextView)findViewById(R.id.formapagorecibo_recibos)).setText("" + recibo.getFormapagorecibo());
            ((TextView)findViewById(R.id.fechageneradorecibo_recibos)).setText("" + recibo.getFechageneradorecibo());
            ((TextView)findViewById(R.id.fechapagorecibo_recibos)).setText("" + recibo.getFechapagorecibo());
            ((TextView)findViewById(R.id.cantidadrecibo_recibos)).setText("" + recibo.getCantidadrecibo());
            ((TextView)findViewById(R.id.pa1recibo_recibos)).setText("" + recibo.getPa1Recibo());
            ((TextView)findViewById(R.id.pa2recibo_recibos)).setText("" + recibo.getPa2Recibo());
            ((TextView)findViewById(R.id.user_id_recibos)).setText("" + recibo.getUser_Id());
            ((TextView)findViewById(R.id.groupuser_id_recibos)).setText("" + recibo.getGroupuser_Id());
            ((TextView)findViewById(R.id.todos_recibos)).setText("" + recibo.getTodos());
            ((TextView)findViewById(R.id.excepgroupuser_id_recibos)).setText("" + recibo.getExcepgroupuser_Id());
            ((TextView)findViewById(R.id.sistemacategoria_id_recibos)).setText("" + recibo.getSistemacategoria_Id());
            ((TextView)findViewById(R.id.sistema_id_recibos)).setText("" + recibo.getSistema_Id());
            ((TextView)findViewById(R.id.horarecibo_recibos)).setText("" + recibo.getHorarecibo());

            ((TextView)findViewById(R.id.ivarecibo_recibos)).setText("" + recibo.getIvarecibo());



            //findViewById(R.id.btn_crearformRecibo).setVisibility(View.VISIBLE);
            findViewById(R.id.btn_crearformRecibo2).setVisibility(View.GONE);



        }else{
            //loadProductosSqliteRecPros("0");
            loadProductosSqliteUltimo("0");
        }
        if(atoguardar!=null&&atoguardar.equals("1")){
            funcenviar();
        }
        //mostrarHora();
       /* selectorTim = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker viewTimePicker) {
                anio = year;
                mes = month;
                dia = dayOfMonth;
                mostrarFecha();
            }
        };*/

        btneliminarrecibos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(globalClass.getEnlinea()==null||globalClass.getEnlinea()=="false") {
                    deleteRow(recibo.getId());
                }else{
                    //eliminarRecibos(recibo.getId());
                    deleteRow(recibo.getId());

                }


            }
        });
        btneditarrecibos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findViewById(R.id.usuarioidrecibo_recibos).setEnabled(true);
                findViewById(R.id.servicioidrecibo_recibos).setEnabled(true);
                findViewById(R.id.productoidrecibo_recibos).setEnabled(true);
                findViewById(R.id.aniorecibo_recibos).setEnabled(true);
                findViewById(R.id.mesrecibo_recibos).setEnabled(true);
                findViewById(R.id.valorrecibo_recibos).setEnabled(true);
                findViewById(R.id.valornetorecibo_recibos).setEnabled(true);
                findViewById(R.id.pagoanticipadorecibo_recibos).setEnabled(true);
                findViewById(R.id.estadorecibo_recibos).setEnabled(true);
                findViewById(R.id.fechalimiterecibo_recibos).setEnabled(true);
                findViewById(R.id.descuentorecibo_recibos).setEnabled(true);
                findViewById(R.id.formapagorecibo_recibos).setEnabled(true);
                findViewById(R.id.fechageneradorecibo_recibos).setEnabled(true);
                findViewById(R.id.fechapagorecibo_recibos).setEnabled(true);

                findViewById(R.id.horarecibo_recibos).setEnabled(true);
                findViewById(R.id.ivarecibo_recibos).setEnabled(true);

                findViewById(R.id.cantidadrecibo_recibos).setEnabled(true);
                findViewById(R.id.pa1recibo_recibos).setEnabled(true);
                findViewById(R.id.pa2recibo_recibos).setEnabled(true);
                findViewById(R.id.user_id_recibos).setEnabled(true);
                findViewById(R.id.groupuser_id_recibos).setEnabled(true);
                findViewById(R.id.todos_recibos).setEnabled(true);
                findViewById(R.id.excepgroupuser_id_recibos).setEnabled(true);
                findViewById(R.id.sistemacategoria_id_recibos).setEnabled(true);
                findViewById(R.id.sistema_id_recibos).setEnabled(true);


                /*$findViewByIdRid1*/

                findViewById(R.id.tr_botonesrecibos).setVisibility(View.GONE);

                //findViewById(R.id.tableRowobsersalidabotonactualizar).setVisibility(View.GONE);
               // findViewById(R.id.btn_crearformRecibo2).setVisibility(View.VISIBLE);



            }

        });
        btnformrecibos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcenviar();

            }
        });
        btnformrecibos2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcenviar();

            }
        });
        productolist = new ArrayList<>();
        icon_agregarproducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Paso 1: Obtener la instancia del administrador de fragmentos
                FragmentManager fragmentManager = getFragmentManager();

                //Paso 2: Crear una nueva transacción
                FragmentTransaction transaction = fragmentManager.beginTransaction();



                //Paso 3: Crear un nuevo fragmento y añadirlo
                BlankFragment fragment = new BlankFragment();

                posicioproductos=posicioproductos+1;

                transaction.add(R.id.productoidrecibo_recibosl, fragment,String.valueOf(posicioproductos));


                //Paso 4: Confirmar el cambio
                transaction.commit();

                Toast.makeText(getApplicationContext(),fragment.getTag()+"",Toast.LENGTH_SHORT).show();


            }
        });

        /*placarecibo_recibos= (TextView) findViewById(R.id.placarecibo_recibos); */
        if (getIntent().getExtras() != null){
            cedulan = getIntent().getExtras().getString("editarrecibo");
            if (cedulan != null){
                //placarecibo_recibos.setVisibility(View.GONE);
                /*$findViewByIdRid1*/
                findViewById(R.id.tr_botonesrecibos).setVisibility(View.GONE);
                //placarecibo_recibos.requestFocus();

            }else{
                /*findViewById(R.id.obserbacionsalidarecibo_recibos).requestFocus();
                findViewById(R.id.obserbacionsalidarecibo_recibos).clearFocus();*/
                /*findViewById(R.id.usuarioidrecibo_recibos).setEnabled(false);
((EditText)findViewById(R.id.usuarioidrecibo_recibos)).setTextColor(Color.BLACK);findViewById(R.id.servicioidrecibo_recibos).setEnabled(false);
((EditText)findViewById(R.id.servicioidrecibo_recibos)).setTextColor(Color.BLACK);findViewById(R.id.productoidrecibo_recibos).setEnabled(false);
((EditText)findViewById(R.id.productoidrecibo_recibos)).setTextColor(Color.BLACK);findViewById(R.id.aniorecibo_recibos).setEnabled(false);
((EditText)findViewById(R.id.aniorecibo_recibos)).setTextColor(Color.BLACK);findViewById(R.id.mesrecibo_recibos).setEnabled(false);
((EditText)findViewById(R.id.mesrecibo_recibos)).setTextColor(Color.BLACK);findViewById(R.id.valorrecibo_recibos).setEnabled(false);
((EditText)findViewById(R.id.valorrecibo_recibos)).setTextColor(Color.BLACK);findViewById(R.id.valornetorecibo_recibos).setEnabled(false);
((EditText)findViewById(R.id.valornetorecibo_recibos)).setTextColor(Color.BLACK);findViewById(R.id.pagoanticipadorecibo_recibos).setEnabled(false);
((EditText)findViewById(R.id.pagoanticipadorecibo_recibos)).setTextColor(Color.BLACK);findViewById(R.id.estadorecibo_recibos).setEnabled(false);
((EditText)findViewById(R.id.estadorecibo_recibos)).setTextColor(Color.BLACK);findViewById(R.id.fechalimiterecibo_recibos).setEnabled(false);
((EditText)findViewById(R.id.fechalimiterecibo_recibos)).setTextColor(Color.BLACK);findViewById(R.id.descuentorecibo_recibos).setEnabled(false);
((EditText)findViewById(R.id.descuentorecibo_recibos)).setTextColor(Color.BLACK);findViewById(R.id.formapagorecibo_recibos).setEnabled(false);
((EditText)findViewById(R.id.formapagorecibo_recibos)).setTextColor(Color.BLACK);findViewById(R.id.fechageneradorecibo_recibos).setEnabled(false);
((EditText)findViewById(R.id.fechageneradorecibo_recibos)).setTextColor(Color.BLACK);findViewById(R.id.fechapagorecibo_recibos).setEnabled(false);
((EditText)findViewById(R.id.fechapagorecibo_recibos)).setTextColor(Color.BLACK);findViewById(R.id.cantidadrecibo_recibos).setEnabled(false);
((EditText)findViewById(R.id.cantidadrecibo_recibos)).setTextColor(Color.BLACK);findViewById(R.id.pa1recibo_recibos).setEnabled(false);
((EditText)findViewById(R.id.pa1recibo_recibos)).setTextColor(Color.BLACK);findViewById(R.id.pa2recibo_recibos).setEnabled(false);
((EditText)findViewById(R.id.pa2recibo_recibos)).setTextColor(Color.BLACK);findViewById(R.id.user_id_recibos).setEnabled(false);
((EditText)findViewById(R.id.user_id_recibos)).setTextColor(Color.BLACK);findViewById(R.id.groupuser_id_recibos).setEnabled(false);
((EditText)findViewById(R.id.groupuser_id_recibos)).setTextColor(Color.BLACK);findViewById(R.id.todos_recibos).setEnabled(false);
((EditText)findViewById(R.id.todos_recibos)).setTextColor(Color.BLACK);findViewById(R.id.excepgroupuser_id_recibos).setEnabled(false);
((EditText)findViewById(R.id.excepgroupuser_id_recibos)).setTextColor(Color.BLACK);findViewById(R.id.sistemacategoria_id_recibos).setEnabled(false);
((EditText)findViewById(R.id.sistemacategoria_id_recibos)).setTextColor(Color.BLACK);findViewById(R.id.sistema_id_recibos).setEnabled(false);
((EditText)findViewById(R.id.sistema_id_recibos)).setTextColor(Color.BLACK);*/

                    if(atoguardar==null) {
                        if (globalClass.getEnlinea() == null || globalClass.getEnlinea() == "false") {
                            loadReciboSqlite(recibo.getId());
                            //loadRecproSqlite(recibo.getId());
                            //loadProductosSqliteRecPros(recibo.getId());
                            loadProductosSqliteRecibosproducto(recibo.getId());
                        } else {
                            //loadRegistroRecibo(recibo.getId());

                            loadReciboSqlite(recibo.getId());
                            //loadRecproSqlite(recibo.getId());
                            //loadProductosSqliteRecPros(recibo.getId());
                            loadProductosSqliteRecibosproducto(recibo.getId());
                        }
                    }



            }
        }

    }

    public void registrarReciboSqlite(final String id){
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"sistemaonline",null,10);
        //SQLiteDatabase db=conn.getWritableDatabase();
        SQLiteDatabase db=conn.getWritableDatabase();
        ContentValues values=new ContentValues();

        Cursor cursor=db.rawQuery("select * from "+ Utilidades.tabla_recibos+" order by id desc limit 1  ",null);
        int contconfig=0;

        int idinserta=0;
        if(cursor.getCount()!=0){

            while(cursor.moveToNext()){
                idinserta=Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            }
            idinserta=idinserta+1;
        }
        /*
        String[][] valoresservicioshorainicial = new String[dimensions][];
        String[][] valoresservicioshoraadicional = new String[dimensions][];
        if(cursor.getCount()==0){

            valoresservicioshorainicial[0] = new String[]{"CARRO", "2000"};
            valoresservicioshorainicial[1] = new String[]{"MOTO", "1200"};
            valoresservicioshorainicial[2] = new String[]{"BICICLETA", "500"};
            valoresservicioshorainicial[3] = new String[]{"servicio", "0"};


            valoresservicioshoraadicional[0] = new String[]{"CARRO", "300"};
            valoresservicioshoraadicional[1] = new String[]{"MOTO", "200"};
            valoresservicioshoraadicional[2] = new String[]{"BICICLETA", "100"};
            valoresservicioshoraadicional[3] = new String[]{"servicio", "0"};
        }

        //Toast.makeText(getApplicationContext(),""+contconfig,Toast.LENGTH_SHORT).show();

        //String val=valoresservicioshorainicial.length;
        int saliorec=0;
        for(int rec=0;rec<valoresservicioshorainicial.length;rec++){
            if(valoresservicioshorainicial[rec][0].toString().trim().equals(((Spinner) findViewById(R.id.serviciorecibo_recibos)).getSelectedItem().toString().trim())){
                saliorec=rec;
                //Toast.makeText(getApplicationContext(),"-"+valoresservicioshorainicial[rec][0]+"-"+((Spinner) findViewById(R.id.serviciorecibo_recibos)).getSelectedItem().toString()+"-",Toast.LENGTH_SHORT).show();
            }

        }*/
        if(id!="0") {
            values.put(Utilidades.CAMPO_ID,id);
        }else{
            values.put(Utilidades.CAMPO_ID,idinserta);
        }
        values.put(Utilidades.CAMPO_USUARIOIDRECIBO,((TextView) findViewById(R.id.usuarioidrecibo_recibos)).getText().toString());
        values.put(Utilidades.CAMPO_SERVICIOIDRECIBO,((TextView) findViewById(R.id.servicioidrecibo_recibos)).getText().toString());
        values.put(Utilidades.CAMPO_PRODUCTOIDRECIBO,((TextView) findViewById(R.id.productoidrecibo_recibos)).getText().toString());
        values.put(Utilidades.CAMPO_ANIORECIBO,((TextView) findViewById(R.id.aniorecibo_recibos)).getText().toString());
        values.put(Utilidades.CAMPO_MESRECIBO,((TextView) findViewById(R.id.mesrecibo_recibos)).getText().toString());
        values.put(Utilidades.CAMPO_VALORRECIBO,((TextView) findViewById(R.id.valorrecibo_recibos)).getText().toString());
        values.put(Utilidades.CAMPO_VALORNETORECIBO,((TextView) findViewById(R.id.valornetorecibo_recibos)).getText().toString());
        values.put(Utilidades.CAMPO_PAGOANTICIPADORECIBO,((TextView) findViewById(R.id.pagoanticipadorecibo_recibos)).getText().toString());
        values.put(Utilidades.CAMPO_ESTADORECIBO,((TextView) findViewById(R.id.estadorecibo_recibos)).getText().toString());
        values.put(Utilidades.CAMPO_FECHALIMITERECIBO,((TextView) findViewById(R.id.fechalimiterecibo_recibos)).getText().toString());
        values.put(Utilidades.CAMPO_DESCUENTORECIBO,((TextView) findViewById(R.id.descuentorecibo_recibos)).getText().toString());
        values.put(Utilidades.CAMPO_FORMAPAGORECIBO,((TextView) findViewById(R.id.formapagorecibo_recibos)).getText().toString());
        values.put(Utilidades.CAMPO_FECHAGENERADORECIBO,((TextView) findViewById(R.id.fechageneradorecibo_recibos)).getText().toString());
        values.put(Utilidades.CAMPO_FECHAPAGORECIBO,((TextView) findViewById(R.id.fechapagorecibo_recibos)).getText().toString());

        if(id!="0") {
            values.put(Utilidades.CAMPO_HORARECIBO,((TextView) findViewById(R.id.horarecibo_recibos)).getText().toString());
        }else{
            //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            values.put(Utilidades.CAMPO_HORARECIBO,simpleDateFormat.format(Calendar.getInstance().getTime()));
        }

        values.put(Utilidades.CAMPO_IVARECIBO,((TextView) findViewById(R.id.ivarecibo_recibos)).getText().toString());

        values.put(Utilidades.CAMPO_CANTIDADRECIBO,((TextView) findViewById(R.id.cantidadrecibo_recibos)).getText().toString());
        values.put(Utilidades.CAMPO_PA1RECIBO,((TextView) findViewById(R.id.pa1recibo_recibos)).getText().toString());
        values.put(Utilidades.CAMPO_PA2RECIBO,((TextView) findViewById(R.id.pa2recibo_recibos)).getText().toString());
        values.put(Utilidades.CAMPO_USER_ID,((TextView) findViewById(R.id.user_id_recibos)).getText().toString());
        values.put(Utilidades.CAMPO_GROUPUSER_ID,((TextView) findViewById(R.id.groupuser_id_recibos)).getText().toString());
        values.put(Utilidades.CAMPO_TODOS,((TextView) findViewById(R.id.todos_recibos)).getText().toString());
        values.put(Utilidades.CAMPO_EXCEPGROUPUSER_ID,((TextView) findViewById(R.id.excepgroupuser_id_recibos)).getText().toString());
        values.put(Utilidades.CAMPO_SISTEMACATEGORIA_ID,((TextView) findViewById(R.id.sistemacategoria_id_recibos)).getText().toString());
        values.put(Utilidades.CAMPO_SISTEMA_ID,((TextView) findViewById(R.id.sistema_id_recibos)).getText().toString());




        Long idResulttante =db.replace(Utilidades.tabla_recibos,Utilidades.CAMPO_ID,values);

        for(int x=0;x<ProductosActivity.productopedidolist.size();x++) {
            if(Integer.parseInt((ProductosActivity.productopedidolist.get(x).getPa2Producto().toString().equals("")?"0":ProductosActivity.productopedidolist.get(x).getPa2Producto()))!=0) {
         //       Toast.makeText(getApplicationContext(), "" + productolist.get(x).getExistenciasproducto() + " " + productolist.get(x).getNombreproducto(), Toast.LENGTH_SHORT).show();

        ContentValues values2=new ContentValues();
                if(id!="0") {
                    values2.put(Utilidades.CAMPO_RECIBOIDPRODUCTO, id);
                }else{
                    values2.put(Utilidades.CAMPO_RECIBOIDPRODUCTO, idinserta);
                }
                if(!ProductosActivity.productopedidolist.get(x).getExistenciasproducto().equals("")) {
                    String insert2 = "update " + Utilidades.tabla_productos + "  set existenciasproducto=existenciasproducto-" + ProductosActivity.productopedidolist.get(x).getPa2Producto() + " where id=" + ProductosActivity.productopedidolist.get(x).getId() + " ";
                    db.execSQL(insert2);
                }
       /* values2.put(Utilidades.CAMPO_PRODUCTOIDRECPRO,ProductosActivity.productopedidolist.get(x).getNombreproducto());
        values2.put(Utilidades.CAMPO_UNIDADESRECPRO,ProductosActivity.productopedidolist.get(x).getPa2Producto());
        values2.put(Utilidades.CAMPO_VALORUNIDADRECPRO,ProductosActivity.productopedidolist.get(x).getPrecioventaproducto());
        values2.put(Utilidades.CAMPO_VALORUNIDADESRECPRO,Integer.parseInt(ProductosActivity.productopedidolist.get(x).getPrecioventaproducto())*Integer.parseInt(ProductosActivity.productopedidolist.get(x).getPa2Producto()));
        */
                //values2.put(Utilidades.CAMPO_RECIBOIDPRODUCTO,ProductosActivity.productopedidolist.get(x).getReciboidproducto());
                values2.put(Utilidades.CAMPO_NOMBREPRODUCTO,ProductosActivity.productopedidolist.get(x).getNombreproducto());
                values2.put(Utilidades.CAMPO_DESCRIPCIONPRODUCTO,ProductosActivity.productopedidolist.get(x).getDescripcionproducto());
                values2.put(Utilidades.CAMPO_MARCAPRODUCTO,ProductosActivity.productopedidolist.get(x).getMarcaproducto());
                values2.put(Utilidades.CAMPO_CATEGORIAPRODUCTO,ProductosActivity.productopedidolist.get(x).getCategoriaproducto());
                values2.put(Utilidades.CAMPO_REFERENCIAPRODUCTO,ProductosActivity.productopedidolist.get(x).getReferenciaproducto());
                values2.put(Utilidades.CAMPO_PRECIOCOMPRAPRODUCTO,ProductosActivity.productopedidolist.get(x).getPreciocompraproducto());
                //values2.put(Utilidades.CAMPO_SUBTOTALPRODUCTO,ProductosActivity.productopedidolist.get(x).getSubtotalproducto());
                values2.put(Utilidades.CAMPO_PRECIOVENTAPRODUCTO,ProductosActivity.productopedidolist.get(x).getPrecioventaproducto());
                values2.put(Utilidades.CAMPO_EXISTENCIASPRODUCTO,ProductosActivity.productopedidolist.get(x).getExistenciasproducto());
                values2.put(Utilidades.CAMPO_MINIMOEXISTENCIASPRODUCTO,ProductosActivity.productopedidolist.get(x).getMinimoexistenciasproducto());
                values2.put(Utilidades.CAMPO_MAXIMOEXISTENCIASPRODUCTO,ProductosActivity.productopedidolist.get(x).getMaximoexistenciasproducto());
                values2.put(Utilidades.CAMPO_PROVEEDORPRODUCTO,ProductosActivity.productopedidolist.get(x).getProveedorproducto());
                //values2.put(Utilidades.CAMPO_PRECIOUNIDADESPRODUCTO,ProductosActivity.productopedidolist.get(x).getPreciounidadesproducto());
                //values2.put(Utilidades.CAMPO_DESCUENTOPRODUCTO,ProductosActivity.productopedidolist.get(x).getDescuentoproducto());
                values2.put(Utilidades.CAMPO_MENOSEXISTENCIAPRODUCTO,ProductosActivity.productopedidolist.get(x).getPa2Producto());
                //values2.put(Utilidades.CAMPO_URLIMAGENPRODUCTO,ProductosActivity.productopedidolist.get(x).getUrlimagenproducto());
                //values2.put(Utilidades.CAMPO_IVAPRODUCTO,ProductosActivity.productopedidolist.get(x).getIvaproducto());
                values2.put(Utilidades.CAMPO_PA1RECIBOSPRODUCTO,ProductosActivity.productopedidolist.get(x).getPa1Producto());
                values2.put(Utilidades.CAMPO_PA2RECIBOSPRODUCTO,ProductosActivity.productopedidolist.get(x).getId());


                Long idResulttante2 =db.replace(Utilidades.tabla_recibosproductos,Utilidades.CAMPO_ID,values2);
            }
        }
        //Toast.makeText(getApplicationContext(),"respuesta: "+idResulttante,Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),"Registro guardado correctamente",Toast.LENGTH_SHORT).show();
        //db.execSQL("drop table recibos");
        String insert="update "+ Utilidades.tabla_productos+"  set Pa2producto='0' ";
        db.execSQL(insert);



        Intent finalizar = new Intent(ReciboformActivity.this, RecibosActivity.class);
                                /*finalizar.putExtra("nom_cli", nom_cli);
                                finalizar.putExtra("ced_cli", ced_cli);
                                finalizar.putExtra("dir_cli", dir_cli);
                                finalizar.putExtra("num_fac", num_fac);
                                finalizar.putExtra("nif", ser_pro_ent);
                                finalizar.putExtra("pago", val_pro);
                                finalizar.putExtra("subsidio", subsidio);*/

        startActivity(finalizar);

        finish();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case MY_PERMISSION_REQUEST_WHRITE_EXTERNAL:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    // Toast.makeText(getApplicationContext(),"Se realizo",Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(getApplicationContext(),"No se realizo",Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    private void checkPermission() {
        if(ContextCompat.checkSelfPermission(ReciboformActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(ReciboformActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            }else{
                ActivityCompat.requestPermissions(ReciboformActivity.this,new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },MY_PERMISSION_REQUEST_WHRITE_EXTERNAL);
            }
        }
    }
    public void readfile(){

        File myfile=new File("/sdcard/mysdfile.txt");
        try{
            FileInputStream fileInputStream = new FileInputStream(myfile);
            BufferedReader myReader= new BufferedReader(new InputStreamReader(fileInputStream));
            String aData="";
            String aBuffer="";
            while((aData=myReader.readLine())!=null){
                aBuffer=aData;
            }
            myReader.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static boolean copiaBD(String from, String to) {
        boolean result = false;
        try{
            File dir = new File(to.substring(0, to.lastIndexOf('/')));
            dir.mkdirs();
            File tof = new File(dir, to.substring(to.lastIndexOf('/') + 1));
            int byteread;
            File oldfile = new File(from);
            if(oldfile.exists()){
                InputStream inStream = new FileInputStream(from);
                FileOutputStream fs = new FileOutputStream(tof);
                byte[] buffer = new byte[1024];
                while((byteread = inStream.read(buffer)) != -1){
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
            }
            result = true;
        }catch (Exception e){
            //Log.e("copyFile", "Error copiando archivo: " + e.getMessage());
        }
        return result;
    }
    public void exportDatabse(String databaseName) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            // Toast.makeText(getApplicationContext(),"ff1",Toast.LENGTH_SHORT).show();

            if (sd.canWrite()) {
                //Toast.makeText(getApplicationContext(),"ff2",Toast.LENGTH_SHORT).show();
                String currentDBPath = "//data//"+getPackageName()+"//databases//"+databaseName+"";
                String backupDBPath = "backupname.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    //Toast.makeText(getApplicationContext(),"se copio bd a tarjeta ",Toast.LENGTH_SHORT).show();
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {

        }
    }
    private void registrarReciboSqliteSQL(){
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"sistemaonline",null,1);
        SQLiteDatabase db=conn.getWritableDatabase();
        String insert="INSERT INTO "+Utilidades.tabla_recibos+" ( " +
                ""+Utilidades.CAMPO_ID+"" +
                ","+Utilidades.CAMPO_USUARIOIDRECIBO+"" +
                ","+Utilidades.CAMPO_SERVICIOIDRECIBO+"" +
                ","+Utilidades.CAMPO_PRODUCTOIDRECIBO+"" +
                ","+Utilidades.CAMPO_ANIORECIBO+"" +
                ","+Utilidades.CAMPO_MESRECIBO+"" +
                ","+Utilidades.CAMPO_VALORRECIBO+"" +
                ","+Utilidades.CAMPO_VALORNETORECIBO+"" +
                ","+Utilidades.CAMPO_PAGOANTICIPADORECIBO+"" +
                ","+Utilidades.CAMPO_ESTADORECIBO+"" +
                ","+Utilidades.CAMPO_FECHALIMITERECIBO+"" +
                ","+Utilidades.CAMPO_DESCUENTORECIBO+"" +
                ","+Utilidades.CAMPO_FORMAPAGORECIBO+"" +
                ","+Utilidades.CAMPO_FECHAGENERADORECIBO+"" +
                ","+Utilidades.CAMPO_HORARECIBO+"" +
                ","+Utilidades.CAMPO_CANTIDADRECIBO+"" +
                ","+Utilidades.CAMPO_FECHAPAGORECIBO+"" +

                ","+Utilidades.CAMPO_IVARECIBO+"" +
                ","+Utilidades.CAMPO_PA1RECIBO+"" +
                ","+Utilidades.CAMPO_PA2RECIBO+"" +
                ","+Utilidades.CAMPO_USER_ID+"" +
                ","+Utilidades.CAMPO_GROUPUSER_ID+"" +
                ","+Utilidades.CAMPO_TODOS+"" +
                ","+Utilidades.CAMPO_EXCEPGROUPUSER_ID+"" +
                ","+Utilidades.CAMPO_SISTEMACATEGORIA_ID+"" +
                ","+Utilidades.CAMPO_SISTEMA_ID+"" +


                ") values ("+1+"" +
                ",'"+((TextView) findViewById(R.id.usuarioidrecibo_recibos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.servicioidrecibo_recibos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.productoidrecibo_recibos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.aniorecibo_recibos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.mesrecibo_recibos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.valorrecibo_recibos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.valornetorecibo_recibos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.pagoanticipadorecibo_recibos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.estadorecibo_recibos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.fechalimiterecibo_recibos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.descuentorecibo_recibos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.formapagorecibo_recibos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.fechageneradorecibo_recibos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.horarecibo_recibos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.cantidadrecibo_recibos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.fechapagorecibo_recibos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.ivarecibo_recibos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.pa1recibo_recibos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.pa2recibo_recibos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.user_id_recibos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.groupuser_id_recibos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.todos_recibos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.excepgroupuser_id_recibos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.sistemacategoria_id_recibos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.sistema_id_recibos)).getText().toString()+"'"+



                ")";
        db.execSQL(insert);
        //db.close();
    }
    public  void funcenviar(){
        fecharecibo_recibos.getText().toString();
        //horarecibo_recibos.getText().toString();
        if(globalClass.getEnlinea()==null||globalClass.getEnlinea()=="false") {
            if (recibo != null) {
                registrarReciboSqlite((recibo == null ? "0" : recibo.getId()));
            } else {
                registrarReciboSqlite((recibo == null ? "0" : recibo.getId()));
            }

            exportDatabse("sistemaonline");
        }else {
            registrarFormRecibos(
                    (recibo == null ? "0" : recibo.getId())
                    ,((TextView)findViewById(R.id.usuarioidrecibo_recibos)).getText().toString()
                    ,((TextView)findViewById(R.id.servicioidrecibo_recibos)).getText().toString()
                    ,((TextView)findViewById(R.id.productoidrecibo_recibos)).getText().toString()
                    ,((TextView)findViewById(R.id.aniorecibo_recibos)).getText().toString()
                    ,((TextView)findViewById(R.id.mesrecibo_recibos)).getText().toString()
                    ,((TextView)findViewById(R.id.valorrecibo_recibos)).getText().toString()
                    ,((TextView)findViewById(R.id.valornetorecibo_recibos)).getText().toString()
                    ,((TextView)findViewById(R.id.pagoanticipadorecibo_recibos)).getText().toString()
                    ,((TextView)findViewById(R.id.estadorecibo_recibos)).getText().toString()
                    ,((TextView)findViewById(R.id.fechalimiterecibo_recibos)).getText().toString()
                    ,((TextView)findViewById(R.id.descuentorecibo_recibos)).getText().toString()
                    ,((TextView)findViewById(R.id.formapagorecibo_recibos)).getText().toString()
                    ,((TextView)findViewById(R.id.fechageneradorecibo_recibos)).getText().toString()
                    ,((TextView)findViewById(R.id.horarecibo_recibos)).getText().toString()
                    ,((TextView)findViewById(R.id.cantidadrecibo_recibos)).getText().toString()
                    ,((TextView)findViewById(R.id.fechapagorecibo_recibos)).getText().toString()
                    ,((TextView)findViewById(R.id.ivarecibo_recibos)).getText().toString()
                    ,((TextView)findViewById(R.id.pa1recibo_recibos)).getText().toString()
                    ,((TextView)findViewById(R.id.pa2recibo_recibos)).getText().toString()
                    ,user_id,((TextView)findViewById(R.id.groupuser_id_recibos)).getText().toString()
                    ,((TextView)findViewById(R.id.todos_recibos)).getText().toString()
                    ,((TextView)findViewById(R.id.excepgroupuser_id_recibos)).getText().toString()
                    ,((TextView)findViewById(R.id.sistemacategoria_id_recibos)).getText().toString()
                    ,((TextView)findViewById(R.id.sistema_id_recibos)).getText().toString()

            );
        }
    }
    public static void hideKeyboard(@NonNull Activity activity) {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    public void mostrarFecha(){

        fecharecibo_recibos.setText(anio+"-"+leftPad( (mes+1)+"", 2, "0")+"-"+leftPad(  padLeftSpaces(dia), 2, "0"));
    }
    public void mostrarHora(){
        horarecibo_recibos.setText(anio+"-"+(mes+1)+"-"+padLeftSpaces(dia));
    }
    public static String rightPad(String input, int length, String fill){
        String pad = input.trim() + String.format("%"+length+"s", "").replace(" ", fill);
        return pad.substring(0, length);
    }

    public  static  String leftPad(String input, int length, String fill){
        String pad = String.format("%"+length+"s", "").replace(" ", fill) + input.trim();
        return pad.substring(pad.length() - length, pad.length());
    }

    public static String padLeftSpaces(Integer inte) {
        return String.format("%02d", inte);
    }

    public void setTime(View view) {
        int hour = timePicker1.getCurrentHour();
        int min = timePicker1.getCurrentMinute();
        showTime(hour, min);
    }

    public void showTime(int hour, int min) {
       /* if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }*/
        format="";

        horarecibo_recibos.setText(new StringBuilder().append(leftPad(hour+"",2,"0")).append(":").append(leftPad(min+"",2,"0"))
                .append("").append(format));
    }
    private void loadReciboSqlite(final String id){

        //wherecedula="";
        SQLiteDatabase db=conn.getReadableDatabase();
        session = new SessionManager(getApplicationContext());
        sp = session.getVariables2();


        Cursor cursor=db.rawQuery("select * from recibos where id="+id+" ",null);
        while(cursor.moveToNext()){

            /*if(cursor.getString(cursor.getColumnIndex("tiemporecibo"))!=null) {
                ((TextView) findViewById(R.id.tiemporecibo_recibos)).setText(cursor.getString(cursor.getColumnIndex("tiemporecibo")).replace(".0", ""));
            }


            if(cursor.getString(cursor.getColumnIndex("subtotaladicionalrecibo"))!=null) {
                ((TextView) findViewById(R.id.subtotaladicionalrecibo_recibos)).setText(cursor.getString(cursor.getColumnIndex("subtotaladicionalrecibo")).replace("-0", "0"));
            }*/

            ((TextView)findViewById(R.id.usuarioidrecibo_recibos)).setText(cursor.getString(cursor.getColumnIndex("usuarioidrecibo")));
            ((TextView)findViewById(R.id.servicioidrecibo_recibos)).setText(cursor.getString(cursor.getColumnIndex("servicioidrecibo")));
            ((TextView)findViewById(R.id.productoidrecibo_recibos)).setText(cursor.getString(cursor.getColumnIndex("productoidrecibo")));
            ((TextView)findViewById(R.id.aniorecibo_recibos)).setText(cursor.getString(cursor.getColumnIndex("aniorecibo")));
            ((TextView)findViewById(R.id.mesrecibo_recibos)).setText(cursor.getString(cursor.getColumnIndex("mesrecibo")));
            ((TextView)findViewById(R.id.valorrecibo_recibos)).setText(cursor.getString(cursor.getColumnIndex("valorrecibo")));
            ((TextView)findViewById(R.id.valornetorecibo_recibos)).setText(cursor.getString(cursor.getColumnIndex("valornetorecibo")));
            ((TextView)findViewById(R.id.pagoanticipadorecibo_recibos)).setText(cursor.getString(cursor.getColumnIndex("pagoanticipadorecibo")));
            ((TextView)findViewById(R.id.estadorecibo_recibos)).setText(cursor.getString(cursor.getColumnIndex("estadorecibo")));
            ((TextView)findViewById(R.id.fechalimiterecibo_recibos)).setText(cursor.getString(cursor.getColumnIndex("fechalimiterecibo")));
            ((TextView)findViewById(R.id.descuentorecibo_recibos)).setText(cursor.getString(cursor.getColumnIndex("descuentorecibo")));
            ((TextView)findViewById(R.id.formapagorecibo_recibos)).setText(cursor.getString(cursor.getColumnIndex("formapagorecibo")));
            ((TextView)findViewById(R.id.fechageneradorecibo_recibos)).setText(cursor.getString(cursor.getColumnIndex("fechageneradorecibo")));
            ((TextView)findViewById(R.id.horarecibo_recibos)).setText(cursor.getString(cursor.getColumnIndex("horarecibo")));
            ((TextView)findViewById(R.id.cantidadrecibo_recibos)).setText(cursor.getString(cursor.getColumnIndex("cantidadrecibo")));
            ((TextView)findViewById(R.id.fechapagorecibo_recibos)).setText(cursor.getString(cursor.getColumnIndex("fechapagorecibo")));
            ((TextView)findViewById(R.id.ivarecibo_recibos)).setText(cursor.getString(cursor.getColumnIndex("ivarecibo")));
            ((TextView)findViewById(R.id.pa1recibo_recibos)).setText(cursor.getString(cursor.getColumnIndex("pa1recibo")));
            ((TextView)findViewById(R.id.pa2recibo_recibos)).setText(cursor.getString(cursor.getColumnIndex("pa2recibo")));
            ((TextView)findViewById(R.id.user_id_recibos)).setText(cursor.getString(cursor.getColumnIndex("user_id")));
            ((TextView)findViewById(R.id.groupuser_id_recibos)).setText(cursor.getString(cursor.getColumnIndex("groupuser_id")));
            ((TextView)findViewById(R.id.todos_recibos)).setText(cursor.getString(cursor.getColumnIndex("todos")));
            ((TextView)findViewById(R.id.excepgroupuser_id_recibos)).setText(cursor.getString(cursor.getColumnIndex("excepgroupuser_id")));
            ((TextView)findViewById(R.id.sistemacategoria_id_recibos)).setText(cursor.getString(cursor.getColumnIndex("sistemacategoria_id")));
            ((TextView)findViewById(R.id.sistema_id_recibos)).setText(cursor.getString(cursor.getColumnIndex("sistema_id")));






        }


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        hideKeyboard(ReciboformActivity.this);
                    }
                },
                100);



    }
    private void loadRegistroRecibo(final String cobrocuota) {

        final ProgressDialog pDialog2 = new ProgressDialog(ReciboformActivity.this);
        pDialog2.setMessage("Cargando Registro...");
        pDialog2.setCancelable(false);
        pDialog2.show();

        AsyncTask<String, String, String> clientestask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                //String urlPedidos = "http://190.255.49.85/facturacion/CilAndroid/v1/index.php/buscar/cliente/" + cliente;
                //String urlPedidos = "http://sistemaonline.co/task_manager/v1/index.php/listar/recibo/" + recibo.getId();
                String urlPedidos = "http://sistemaonline.co/factura/public/api/recibo/editar/" + recibo.getId();
                //String urlPedidos = "http://192.168.10.2/CilAndroid/v1/index.php/buscar/cliente/" + cliente;
                //http://sistemaonline.co/factura/public/api/auth/login
                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, urlPedidos, new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response.toString());
                            if (jsonObject.getString("error") == "true"){
                                // tv_msg2.setText("Error: " + jsonObject.getString("message"));
                            }else{
                                JSONArray cobrocuotas = response.getJSONArray("cobrocuotas");

                                if (cobrocuotas.length() > 0){


                                    for (int i = 0; i < cobrocuotas.length(); i++){
                                        JSONObject c = cobrocuotas.getJSONObject(i);

                                        ((TextView)findViewById(R.id.usuarioidrecibo_recibos)).setText(c.getString("usuarioidrecibo"));
                                        ((TextView)findViewById(R.id.servicioidrecibo_recibos)).setText(c.getString("servicioidrecibo"));
                                        ((TextView)findViewById(R.id.productoidrecibo_recibos)).setText(c.getString("productoidrecibo"));
                                        ((TextView)findViewById(R.id.aniorecibo_recibos)).setText(c.getString("aniorecibo"));
                                        ((TextView)findViewById(R.id.mesrecibo_recibos)).setText(c.getString("mesrecibo"));
                                        ((TextView)findViewById(R.id.valorrecibo_recibos)).setText(c.getString("valorrecibo"));
                                        ((TextView)findViewById(R.id.valornetorecibo_recibos)).setText(c.getString("valornetorecibo"));
                                        ((TextView)findViewById(R.id.pagoanticipadorecibo_recibos)).setText(c.getString("pagoanticipadorecibo"));
                                        ((TextView)findViewById(R.id.estadorecibo_recibos)).setText(c.getString("estadorecibo"));
                                        ((TextView)findViewById(R.id.fechalimiterecibo_recibos)).setText(c.getString("fechalimiterecibo"));
                                        ((TextView)findViewById(R.id.descuentorecibo_recibos)).setText(c.getString("descuentorecibo"));
                                        ((TextView)findViewById(R.id.formapagorecibo_recibos)).setText(c.getString("formapagorecibo"));
                                        ((TextView)findViewById(R.id.fechageneradorecibo_recibos)).setText(c.getString("fechageneradorecibo"));
                                        ((TextView)findViewById(R.id.horarecibo_recibos)).setText(c.getString("horarecibo"));
                                        ((TextView)findViewById(R.id.cantidadrecibo_recibos)).setText(c.getString("cantidadrecibo"));
                                        ((TextView)findViewById(R.id.fechapagorecibo_recibos)).setText(c.getString("fechapagorecibo"));
                                        ((TextView)findViewById(R.id.ivarecibo_recibos)).setText(c.getString("ivarecibo"));
                                        ((TextView)findViewById(R.id.pa1recibo_recibos)).setText(c.getString("pa1recibo"));
                                        ((TextView)findViewById(R.id.pa2recibo_recibos)).setText(c.getString("pa2recibo"));
                                        ((TextView)findViewById(R.id.user_id_recibos)).setText(c.getString("user_id"));
                                        ((TextView)findViewById(R.id.groupuser_id_recibos)).setText(c.getString("groupuser_id"));
                                        ((TextView)findViewById(R.id.todos_recibos)).setText(c.getString("todos"));
                                        ((TextView)findViewById(R.id.excepgroupuser_id_recibos)).setText(c.getString("excepgroupuser_id"));
                                        ((TextView)findViewById(R.id.sistemacategoria_id_recibos)).setText(c.getString("sistemacategoria_id"));
                                        ((TextView)findViewById(R.id.sistema_id_recibos)).setText(c.getString("sistema_id"));






                                    }

                                    pDialog2.hide();
                                    new android.os.Handler().postDelayed(
                                            new Runnable() {
                                                public void run() {

                                                    hideKeyboard(ReciboformActivity.this);
                                                }
                                            },
                                            100);

                                }else {


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
                MySingleton.getInstance(ReciboformActivity.this).addRequestQueue(jsonRequest);
                return "";
            }
        };

        clientestask.execute();
    }

    private void registrarFormRecibos(final String id ,final String usuarioidrecibo,final String servicioidrecibo,final String productoidrecibo,final String aniorecibo,final String mesrecibo,final String valorrecibo,final String valornetorecibo,final String pagoanticipadorecibo,final String estadorecibo,final String fechalimiterecibo,final String descuentorecibo,final String formapagorecibo,final String fechageneradorecibo,final String horarecibo,final String cantidadrecibo,final String fechapagorecibo,final String ivarecibo,final String pa1recibo,final String pa2recibo,final String user_id,final String groupuser_id,final String todos,final String excepgroupuser_id,final String sistemacategoria_id,final String sistema_id) {
        //final String ser_pro_entr = "108096144149";
        //tv_msg.setText(id_cli + " - " + id_dir + " - " + fec_ped + " - " + hra_ped + " - " + id_fac + " - " + tip_reg + " - " + tel_cli + " - " + id_pro + " - " + num_fac + " - " + val_pro + " - " + ser_pro_ent + " - " + subsidio + " - " + latitud + " - " + longitud + " - " + municipio);

        final ProgressDialog pDialog2 = new ProgressDialog(ReciboformActivity.this);
        pDialog2.setMessage("Registrando ...");
        pDialog2.setCancelable(false);
        pDialog2.show();

        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {


                String urlVenta = "http://sistemaonline.co/task_manager/v1/index.php/registrar/formrecibo";


                StringRequest jsonRequest = new StringRequest(Request.Method.POST, urlVenta, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response.toString());
                            if (jsonObject.getString("error") == "true"){
                                Toast.makeText(ReciboformActivity.this,"ERROR",Toast.LENGTH_LONG).show();

                                //tv_msg.setText("Error: " + jsonObject.getString("message"));
                            }else{
                                //tv_msg.setText(jsonObject.getString("message"));
                                Toast.makeText(ReciboformActivity.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                Intent finalizar = new Intent(ReciboformActivity.this, RecibosActivity.class);
                                /*finalizar.putExtra("nom_cli", nom_cli);
                                finalizar.putExtra("ced_cli", ced_cli);
                                finalizar.putExtra("dir_cli", dir_cli);
                                finalizar.putExtra("num_fac", num_fac);
                                finalizar.putExtra("nif", ser_pro_ent);
                                finalizar.putExtra("pago", val_pro);
                                finalizar.putExtra("subsidio", subsidio);*/

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
                        parameters.put("id", id) ;
                        parameters.put("usuarioidrecibo", usuarioidrecibo);
                        parameters.put("servicioidrecibo", servicioidrecibo);
                        parameters.put("productoidrecibo", productoidrecibo);
                        parameters.put("aniorecibo", aniorecibo);
                        parameters.put("mesrecibo", mesrecibo);
                        parameters.put("valorrecibo", valorrecibo);
                        parameters.put("valornetorecibo", valornetorecibo);
                        parameters.put("pagoanticipadorecibo", pagoanticipadorecibo);
                        parameters.put("estadorecibo", estadorecibo);
                        parameters.put("fechalimiterecibo", fechalimiterecibo);
                        parameters.put("descuentorecibo", descuentorecibo);
                        parameters.put("formapagorecibo", formapagorecibo);
                        parameters.put("fechageneradorecibo", fechageneradorecibo);
                        parameters.put("horarecibo", horarecibo);
                        parameters.put("cantidadrecibo", cantidadrecibo);
                        parameters.put("fechapagorecibo", fechapagorecibo);
                        parameters.put("ivarecibo", ivarecibo);
                        parameters.put("pa1recibo", pa1recibo);
                        parameters.put("pa2recibo", pa2recibo);
                        parameters.put("user_id", user_id);
                        parameters.put("groupuser_id", groupuser_id);
                        parameters.put("todos", todos);
                        parameters.put("excepgroupuser_id", excepgroupuser_id);
                        parameters.put("sistemacategoria_id", sistemacategoria_id);
                        parameters.put("sistema_id", sistema_id);






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
                MySingleton.getInstance(ReciboformActivity.this).addRequestQueue(jsonRequest);
                return "";
            }
        };

        task.execute();
    }
    public void deleteRow(String value)
    {
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"sistemaonline",null,10);
        SQLiteDatabase db=conn.getWritableDatabase();
        db.execSQL("UPDATE " + Utilidades.tabla_recibos+ " SET deleted_at =datetime('now','localtime') WHERE "+Utilidades.CAMPO_ID+"='"+value+"'");
        db.execSQL("UPDATE " + Utilidades.tabla_recibosproductos+ " SET deleted_at =datetime('now','localtime') WHERE "+Utilidades.CAMPO_RECIBOIDPRODUCTO+"='"+value+"'");




        Cursor cursor=db.rawQuery("select * from "+ Utilidades.tabla_recibosproductos+" where "+Utilidades.CAMPO_RECIBOIDPRODUCTO+"='"+value+"' ",null);

        while(cursor.moveToNext()){
            db.execSQL("UPDATE " + Utilidades.tabla_productos+ " SET existenciasproducto =existenciasproducto+"+cursor.getString(cursor.getColumnIndex("menosexistenciaproducto"))+" WHERE id="+cursor.getString(cursor.getColumnIndex("pa2recibosproducto")));


        }



        db.close();
        Toast.makeText(ReciboformActivity.this,"Registro Eliminado",Toast.LENGTH_LONG).show();
        Intent finalizar = new Intent(ReciboformActivity.this, RecibosActivity.class);


        startActivity(finalizar);
        finish();
    }
    private void eliminarRecibos(final String idrecibo) {

        final ProgressDialog pDialog2 = new ProgressDialog(ReciboformActivity.this);
        pDialog2.setMessage("Eliminando...");
        pDialog2.setCancelable(false);
        pDialog2.show();

        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {


                String urlVenta = "http://sistemaonline.co/factura/public/api/recibo/"+idrecibo;


                StringRequest jsonRequest = new StringRequest(Request.Method.DELETE, urlVenta, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response.toString());
                            if (jsonObject.getString("error") == "true"){
                                Toast.makeText(ReciboformActivity.this,"ERROR",Toast.LENGTH_LONG).show();

                                //tv_msg.setText("Error: " + jsonObject.getString("message"));
                            }else{
                                //tv_msg.setText(jsonObject.getString("message"));
                                Toast.makeText(ReciboformActivity.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                Intent finalizar = new Intent(ReciboformActivity.this, RecibosActivity.class);
                                /*finalizar.putExtra("nom_cli", nom_cli);
                                finalizar.putExtra("ced_cli", ced_cli);
                                finalizar.putExtra("dir_cli", dir_cli);
                                finalizar.putExtra("num_fac", num_fac);
                                finalizar.putExtra("nif", ser_pro_ent);
                                finalizar.putExtra("pago", val_pro);
                                finalizar.putExtra("subsidio", subsidio);*/

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
                        parameters.put("id", idrecibo) ;






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
                MySingleton.getInstance(ReciboformActivity.this).addRequestQueue(jsonRequest);
                return "";
            }
        };

        task.execute();
    }

    private void loadRecproSqlite(final String id){

        //wherecedula="";
        SQLiteDatabase db=conn.getReadableDatabase();
        session = new SessionManager(getApplicationContext());
        sp = session.getVariables2();


        //Paso 1: Obtener la instancia del administrador de fragmentos
        FragmentManager fragmentManager = getFragmentManager();

        //Paso 2: Crear una nueva transacción
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //Paso 3: Crear un nuevo fragmento y añadirlo



        Cursor cursor=db.rawQuery("select * from "+ Utilidades.tabla_recpros+"  where reciboidrecpro="+id+" ",null);
        final Producto producto= new Producto(String.valueOf(ReciboformActivity.posicioproductos),"nom","des","marca","cat","ref","prec","prec","1","min","max","pro","pa","pa","","","","","","");



        while(cursor.moveToNext()){
            posicioproductos=posicioproductos+1;
            BlankFragment fragment = new BlankFragment();
            Bundle args = new Bundle();
            args.putString("unidades", cursor.getString(cursor.getColumnIndex("unidadesrecpro")));
            args.putString("nombreprod", cursor.getString(cursor.getColumnIndex("productoidrecpro")));
            args.putString("valor", "1000");
            fragment.setArguments(args);
            ReciboformActivity.productolist.add(producto);
            transaction.add(R.id.productoidrecibo_recibosl, fragment,String.valueOf(posicioproductos));


            /*((TextView)findViewById(R.id.reciboidrecpro_recpros)).setText(cursor.getString(cursor.getColumnIndex("reciboidrecpro")));
            ((TextView)findViewById(R.id.productoidrecpro_recpros)).setText(cursor.getString(cursor.getColumnIndex("productoidrecpro")));
            ((TextView)findViewById(R.id.unidadesrecpro_recpros)).setText(cursor.getString(cursor.getColumnIndex("unidadesrecpro")));
            ((TextView)findViewById(R.id.valorunidadrecpro_recpros)).setText(cursor.getString(cursor.getColumnIndex("valorunidadrecpro")));
            ((TextView)findViewById(R.id.valorunidadesrecpro_recpros)).setText(cursor.getString(cursor.getColumnIndex("valorunidadesrecpro")));
            ((TextView)findViewById(R.id.descuentorecpro_recpros)).setText(cursor.getString(cursor.getColumnIndex("descuentorecpro")));
            ((TextView)findViewById(R.id.ivarecpro_recpros)).setText(cursor.getString(cursor.getColumnIndex("ivarecpro")));
            ((TextView)findViewById(R.id.subtotalrecpro_recpros)).setText(cursor.getString(cursor.getColumnIndex("subtotalrecpro")));
            ((TextView)findViewById(R.id.totalrecpro_recpros)).setText(cursor.getString(cursor.getColumnIndex("totalrecpro")));
            ((TextView)findViewById(R.id.pa1recpro_recpros)).setText(cursor.getString(cursor.getColumnIndex("pa1recpro")));
            ((TextView)findViewById(R.id.pa2recpro_recpros)).setText(cursor.getString(cursor.getColumnIndex("pa2recpro")));
            ((TextView)findViewById(R.id.user_id_recpros)).setText(cursor.getString(cursor.getColumnIndex("user_id")));
            ((TextView)findViewById(R.id.groupuser_id_recpros)).setText(cursor.getString(cursor.getColumnIndex("groupuser_id")));
            ((TextView)findViewById(R.id.todos_recpros)).setText(cursor.getString(cursor.getColumnIndex("todos")));
            ((TextView)findViewById(R.id.excepgroupuser_id_recpros)).setText(cursor.getString(cursor.getColumnIndex("excepgroupuser_id")));
            ((TextView)findViewById(R.id.sistemacategoria_id_recpros)).setText(cursor.getString(cursor.getColumnIndex("sistemacategoria_id")));
            ((TextView)findViewById(R.id.sistema_id_recpros)).setText(cursor.getString(cursor.getColumnIndex("sistema_id")));
            */






        }




        //Paso 4: Confirmar el cambio
        transaction.commit();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        hideKeyboard(ReciboformActivity.this);
                    }
                },
                100);



    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void loadProductosSqliteUltimo(final String productobusqueda){
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
        //SQLiteDatabase db=conn.getReadableDatabase();
        Producto producto=null;
        listaProductos= new ArrayList<Producto>();

        for(int x=0;x<ProductosActivity.productopedidolist.size();x++) {
            if(Integer.parseInt((ProductosActivity.productopedidolist.get(x).getPa2Producto().toString().equals("")?"0":ProductosActivity.productopedidolist.get(x).getPa2Producto()))!=0) {
                //valorTotalPedido++;

                listaProductos.add(ProductosActivity.productopedidolist.get(x));
            }
        }
        ((TextView)findViewById(R.id.valorrecibo_recibos)).setText(ProductosActivity.valorTotalPedido.toString());
        ((TextView)findViewById(R.id.valornetorecibo_recibos)).setText(ProductosActivity.valorTotalPedido.toString());
        /*Cursor cursor=db.rawQuery("select * from "+ Utilidades.tabla_productos+" where deleted_at is null "+wherecedula+"  order by id desc ",null);
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

        }*/

        adapterproducto = new ProductoAdapter(ReciboformActivity.this,listaProductos,'n');
        rv_productos.setAdapter(adapterproducto);
//        tv_msg.setText("Resultados encontrados: " + adapterproducto.getItemCount());
        adapterproducto.notifyDataSetChanged();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        hideKeyboard(ReciboformActivity.this);
                    }
                },
                1);
        //btn_crearProducto.setVisibility(View.INVISIBLE);
    }

    private void loadProductosSqliteRecPros(final String productobusqueda){
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
/*
        for(int x=0;x<ProductosActivity.productopedidolist.size();x++) {
            if(Integer.parseInt((ProductosActivity.productopedidolist.get(x).getPa2Producto().toString().equals("")?"0":ProductosActivity.productopedidolist.get(x).getPa2Producto()))!=0) {
                //valorTotalPedido++;
                Toast.makeText(getApplicationContext(),"Tiempo de conexión agotado",Toast.LENGTH_SHORT).show();
                listaProductos.add(ProductosActivity.productopedidolist.get(x));
            }
        }
        ((TextView)findViewById(R.id.valorrecibo_recibos)).setText(ProductosActivity.valorTotalPedido.toString());
        ((TextView)findViewById(R.id.valornetorecibo_recibos)).setText(ProductosActivity.valorTotalPedido.toString());
  */





        Cursor cursor=db.rawQuery("select * from "+ Utilidades.tabla_recpros+" where  reciboidrecpro='"+productobusqueda+"' and deleted_at is null "+wherecedula+"  order by id desc ",null);
        while(cursor.moveToNext()){
            producto=new Producto(
                    cursor.getString(cursor.getColumnIndex("reciboidrecpro"))
                    ,cursor.getString(cursor.getColumnIndex("productoidrecpro"))
                    ,cursor.getString(cursor.getColumnIndex("pa1recpro"))
                    ,cursor.getString(cursor.getColumnIndex("pa1recpro"))
                    ,cursor.getString(cursor.getColumnIndex("pa1recpro"))
                    ,cursor.getString(cursor.getColumnIndex("pa1recpro"))
                    ,cursor.getString(cursor.getColumnIndex("pa1recpro"))
                    ,cursor.getString(cursor.getColumnIndex("valorunidadrecpro"))
                    ,cursor.getString(cursor.getColumnIndex("pa1recpro"))
                    ,cursor.getString(cursor.getColumnIndex("pa1recpro"))
                    ,cursor.getString(cursor.getColumnIndex("pa1recpro"))
                    ,cursor.getString(cursor.getColumnIndex("pa1recpro"))
                    ,cursor.getString(cursor.getColumnIndex("pa1recpro"))
                    ,cursor.getString(cursor.getColumnIndex("unidadesrecpro"))
                    ,cursor.getString(cursor.getColumnIndex("user_id"))
                    ,cursor.getString(cursor.getColumnIndex("groupuser_id"))
                    ,cursor.getString(cursor.getColumnIndex("todos"))
                    ,cursor.getString(cursor.getColumnIndex("excepgroupuser_id"))
                    ,cursor.getString(cursor.getColumnIndex("sistemacategoria_id"))
                    ,cursor.getString(cursor.getColumnIndex("sistema_id"))

            );
            listaProductos.add(producto);

        }

        adapterproducto = new ProductoAdapter(ReciboformActivity.this,listaProductos,'n');
        rv_productos.setAdapter(adapterproducto);
//        tv_msg.setText("Resultados encontrados: " + adapterproducto.getItemCount());
        adapterproducto.notifyDataSetChanged();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        hideKeyboard(ReciboformActivity.this);
                    }
                },
                1);
        //btn_crearProducto.setVisibility(View.INVISIBLE);
    }

    private void loadProductosSqliteRecibosproducto(final String productobusqueda){
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
/*
        for(int x=0;x<ProductosActivity.productopedidolist.size();x++) {
            if(Integer.parseInt((ProductosActivity.productopedidolist.get(x).getPa2Producto().toString().equals("")?"0":ProductosActivity.productopedidolist.get(x).getPa2Producto()))!=0) {
                //valorTotalPedido++;
                Toast.makeText(getApplicationContext(),"Tiempo de conexión agotado",Toast.LENGTH_SHORT).show();
                listaProductos.add(ProductosActivity.productopedidolist.get(x));
            }
        }
        ((TextView)findViewById(R.id.valorrecibo_recibos)).setText(ProductosActivity.valorTotalPedido.toString());
        ((TextView)findViewById(R.id.valornetorecibo_recibos)).setText(ProductosActivity.valorTotalPedido.toString());
  */





        Cursor cursor=db.rawQuery("select * from "+ Utilidades.tabla_recibosproductos+" where  reciboidproducto='"+productobusqueda+"' and deleted_at is null "+wherecedula+"  order by id desc ",null);
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
                    ,cursor.getString(cursor.getColumnIndex("pa1recibosproducto"))
                    ,cursor.getString(cursor.getColumnIndex("menosexistenciaproducto"))
                    ,cursor.getString(cursor.getColumnIndex("user_id"))
                    ,cursor.getString(cursor.getColumnIndex("groupuser_id"))
                    ,cursor.getString(cursor.getColumnIndex("todos"))
                    ,cursor.getString(cursor.getColumnIndex("excepgroupuser_id"))
                    ,cursor.getString(cursor.getColumnIndex("sistemacategoria_id"))
                    ,cursor.getString(cursor.getColumnIndex("sistema_id"))

            );
            listaProductos.add(producto);

        }

        adapterproducto = new ProductoAdapter(ReciboformActivity.this,listaProductos,'n');
        rv_productos.setAdapter(adapterproducto);
//        tv_msg.setText("Resultados encontrados: " + adapterproducto.getItemCount());
        adapterproducto.notifyDataSetChanged();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        hideKeyboard(ReciboformActivity.this);
                    }
                },
                1);
        //btn_crearProducto.setVisibility(View.INVISIBLE);
    }
   /* @Override
    public void onBackPressed() {

    }*/

}
