package com.example.administrador.gestionusuarios;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UsuarionuevoformActivity extends AppCompatActivity {
    private TextView placausuarionuevo_usuarionuevos, tv_usuarionuevo, tv_msg, tv_placa;
    private String user_id,apiKey,vendedor, nom_conductor, placa, cedulan;
    private int dia,mes,anio;
    private Spinner spinner;
    private EditText fechausuarionuevo_usuarionuevos,horausuarionuevo_usuarionuevos;
    private static DatePickerDialog.OnDateSetListener selectorFec;
    private static TimePickerDialog.OnTimeSetListener selectorTim;
    private TimePicker timePicker1;
    private String format = "";
    private Button btnformusuarionuevos,btnformusuarionuevos2,btneditarusuarionuevos,btneliminarusuarionuevos;
    private Usuarionuevo usuarionuevo;
    private SharedPreferences sp;
    SessionManager session;
    private static final int MY_PERMISSION_REQUEST_WHRITE_EXTERNAL=1;
    String enlinea;
    GlobalClass globalClass;
    ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"sistemaonline",null,9);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_usuarionuevoform);
        checkPermission();
        globalClass=(GlobalClass)getApplicationContext();


        // Toast.makeText(getBaseContext(), globalClass.getEnlinea(), Toast.LENGTH_SHORT).show();
       /* int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        */

        String pathDB = getDatabasePath("sistemaonline").toString();
        /*copiaBD(pathDB,Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + getPackageName() + "/" + "xxxxxx");*/

        /*exportDatabse("sistemaonline"); //si funciona */
        //((EditText)findViewById(R.id.descuentousuarionuevo_usuarionuevos)).setHint("Dia");

        btnformusuarionuevos=(Button) findViewById(R.id.btn_crearformUsuarionuevo);
        btnformusuarionuevos2=(Button) findViewById(R.id.btn_crearformUsuarionuevo2);
        btneditarusuarionuevos=(Button) findViewById(R.id.btn_editarusuarionuevos);
        btneliminarusuarionuevos=(Button) findViewById(R.id.btn_eliminarusuarionuevos);
        /*placausuarionuevo_usuarionuevos= (TextView) findViewById(R.id.placausuarionuevo_usuarionuevos);
        spinner = (Spinner) findViewById(R.id.serviciousuarionuevo_usuarionuevos);
        horausuarionuevo_usuarionuevos=(EditText) findViewById(R.id.horausuarionuevo_usuarionuevos);*/
        //String[] items = new String[]{"CARRO", "MOTO", "BICICLETA"};

        Calendar calendar = Calendar.getInstance();
        anio = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        //timePicker1 = (TimePicker) findViewById(R.id.horatmp);
        //int hour = timePicker1.getCurrentHour();
        //int min = timePicker1.getCurrentMinute();
        //showTime(hour, min);
        fechausuarionuevo_usuarionuevos = (EditText) findViewById(R.id.fechaafiliacionusuarionuevo_usuarionuevos);
        session = new SessionManager(getApplicationContext());
        sp = session.getVariables2();
        user_id=sp.getString("name","23");
        String[] items = sp.getString("comboes","fdf,jj").split(",");

        if(sp.getString("cobrardias","false").equals("true")) {
            //((TextInputLayout) findViewById(R.id.tarifahoraadicionalusuarionuevo_usuarionuevosl)).setHint("Dia adicional");
        }
        /*horausuarionuevo_usuarionuevos = (EditText) findViewById(R.id.horausuarionuevo_usuarionuevos);*/
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items){

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
       // spinner.setAdapter(adapter);
        if(getIntent().getExtras() != null){
            usuarionuevo = getIntent().getExtras().getParcelable("usuarionuevo");
        }
        if (usuarionuevo != null){

            // ((TextView)findViewById(R.id.id_usuarionuevos)).setText("" + usuarionuevo.getId());
            ((TextView)findViewById(R.id.codigousuarionuevo_usuarionuevos)).setText("" + usuarionuevo.getCodigousuarionuevo());
            ((TextView)findViewById(R.id.nombresusuarionuevo_usuarionuevos)).setText("" + usuarionuevo.getNombresusuarionuevo());
            ((TextView)findViewById(R.id.apellidosusuarionuevo_usuarionuevos)).setText("" + usuarionuevo.getApellidosusuarionuevo());
            ((TextView)findViewById(R.id.cedulausuarionuevo_usuarionuevos)).setText("" + usuarionuevo.getCedulausuarionuevo());
            ((TextView)findViewById(R.id.telefono1usuarionuevo_usuarionuevos)).setText("" + usuarionuevo.getTelefono1Usuarionuevo());
            ((TextView)findViewById(R.id.telefono2usuarionuevo_usuarionuevos)).setText("" + usuarionuevo.getTelefono2Usuarionuevo());
            ((TextView)findViewById(R.id.fechaafiliacionusuarionuevo_usuarionuevos)).setText("" + usuarionuevo.getFechaafiliacionusuarionuevo());
            ((TextView)findViewById(R.id.direccionusuarionuevo_usuarionuevos)).setText("" + usuarionuevo.getDireccionusuarionuevo());
            ((TextView)findViewById(R.id.barriousuarionuevo_usuarionuevos)).setText("" + usuarionuevo.getBarriousuarionuevo());
            ((TextView)findViewById(R.id.valormensualusuarionuevo_usuarionuevos)).setText("" + usuarionuevo.getValormensualusuarionuevo());
            ((TextView)findViewById(R.id.responsableusuarionuevo_usuarionuevos)).setText("" + usuarionuevo.getResponsableusuarionuevo());
            ((TextView)findViewById(R.id.pa1usuarionuevo_usuarionuevos)).setText("" + usuarionuevo.getPa1Usuarionuevo());
            ((TextView)findViewById(R.id.pa2usuarionuevo_usuarionuevos)).setText("" + usuarionuevo.getPa2Usuarionuevo());
            ((TextView)findViewById(R.id.user_id_usuarionuevos)).setText("" + usuarionuevo.getUser_Id());
            ((TextView)findViewById(R.id.groupuser_id_usuarionuevos)).setText("" + usuarionuevo.getGroupuser_Id());
            ((TextView)findViewById(R.id.todos_usuarionuevos)).setText("" + usuarionuevo.getTodos());
            ((TextView)findViewById(R.id.excepgroupuser_id_usuarionuevos)).setText("" + usuarionuevo.getExcepgroupuser_Id());
            ((TextView)findViewById(R.id.sistemacategoria_id_usuarionuevos)).setText("" + usuarionuevo.getSistemacategoria_Id());
            ((TextView)findViewById(R.id.sistema_id_usuarionuevos)).setText("" + usuarionuevo.getSistema_Id());



            findViewById(R.id.btn_crearformUsuarionuevo).setVisibility(View.VISIBLE);
            findViewById(R.id.btn_crearformUsuarionuevo2).setVisibility(View.GONE);



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

        btneliminarusuarionuevos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(globalClass.getEnlinea()==null||globalClass.getEnlinea()=="false") {
                    deleteRow(usuarionuevo.getId());
                }else{
                    eliminarUsuarionuevos(usuarionuevo.getId());

                }

            }
        });
        btneditarusuarionuevos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findViewById(R.id.codigousuarionuevo_usuarionuevos).setEnabled(true);
                findViewById(R.id.nombresusuarionuevo_usuarionuevos).setEnabled(true);
                findViewById(R.id.apellidosusuarionuevo_usuarionuevos).setEnabled(true);
                findViewById(R.id.cedulausuarionuevo_usuarionuevos).setEnabled(true);
                findViewById(R.id.telefono1usuarionuevo_usuarionuevos).setEnabled(true);
                findViewById(R.id.telefono2usuarionuevo_usuarionuevos).setEnabled(true);
                findViewById(R.id.fechaafiliacionusuarionuevo_usuarionuevos).setEnabled(true);
                findViewById(R.id.direccionusuarionuevo_usuarionuevos).setEnabled(true);
                findViewById(R.id.barriousuarionuevo_usuarionuevos).setEnabled(true);
                findViewById(R.id.valormensualusuarionuevo_usuarionuevos).setEnabled(true);
                findViewById(R.id.responsableusuarionuevo_usuarionuevos).setEnabled(true);
                findViewById(R.id.pa1usuarionuevo_usuarionuevos).setEnabled(true);
                findViewById(R.id.pa2usuarionuevo_usuarionuevos).setEnabled(true);
                findViewById(R.id.user_id_usuarionuevos).setEnabled(true);
                findViewById(R.id.groupuser_id_usuarionuevos).setEnabled(true);
                findViewById(R.id.todos_usuarionuevos).setEnabled(true);
                findViewById(R.id.excepgroupuser_id_usuarionuevos).setEnabled(true);
                findViewById(R.id.sistemacategoria_id_usuarionuevos).setEnabled(true);
                findViewById(R.id.sistema_id_usuarionuevos).setEnabled(true);


                /*findViewById(R.id.codigousuarionuevo_usuarionuevos1).setVisibility(View.GONE);
findViewById(R.id.nombresusuarionuevo_usuarionuevos1).setVisibility(View.GONE);
findViewById(R.id.apellidosusuarionuevo_usuarionuevos1).setVisibility(View.GONE);
findViewById(R.id.cedulausuarionuevo_usuarionuevos1).setVisibility(View.GONE);
findViewById(R.id.telefono1usuarionuevo_usuarionuevos1).setVisibility(View.GONE);
findViewById(R.id.telefono2usuarionuevo_usuarionuevos1).setVisibility(View.GONE);
findViewById(R.id.fechaafiliacionusuarionuevo_usuarionuevos1).setVisibility(View.GONE);
findViewById(R.id.direccionusuarionuevo_usuarionuevos1).setVisibility(View.GONE);
findViewById(R.id.barriousuarionuevo_usuarionuevos1).setVisibility(View.GONE);
findViewById(R.id.valormensualusuarionuevo_usuarionuevos1).setVisibility(View.GONE);
findViewById(R.id.responsableusuarionuevo_usuarionuevos1).setVisibility(View.GONE);
findViewById(R.id.pa1usuarionuevo_usuarionuevos1).setVisibility(View.GONE);
findViewById(R.id.pa2usuarionuevo_usuarionuevos1).setVisibility(View.GONE);
findViewById(R.id.user_id_usuarionuevos1).setVisibility(View.GONE);
findViewById(R.id.groupuser_id_usuarionuevos1).setVisibility(View.GONE);
findViewById(R.id.todos_usuarionuevos1).setVisibility(View.GONE);
findViewById(R.id.excepgroupuser_id_usuarionuevos1).setVisibility(View.GONE);
findViewById(R.id.sistemacategoria_id_usuarionuevos1).setVisibility(View.GONE);
findViewById(R.id.sistema_id_usuarionuevos1).setVisibility(View.GONE);
*/

                findViewById(R.id.tr_botonesusuarionuevos).setVisibility(View.GONE);

                //findViewById(R.id.tableRowobsersalidabotonactualizar).setVisibility(View.GONE);
                findViewById(R.id.btn_crearformUsuarionuevo2).setVisibility(View.VISIBLE);



            }

        });
        btnformusuarionuevos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcenviar();

            }
        });
        btnformusuarionuevos2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcenviar();

            }
        });

        /*placausuarionuevo_usuarionuevos= (TextView) findViewById(R.id.placausuarionuevo_usuarionuevos); */
        if (getIntent().getExtras() != null){
            cedulan = getIntent().getExtras().getString("editarusuarionuevo");
            if (cedulan != null){
                //placausuarionuevo_usuarionuevos.setVisibility(View.GONE);
                /*findViewById(R.id.codigousuarionuevo_usuarionuevosl).setVisibility(View.GONE);
findViewById(R.id.nombresusuarionuevo_usuarionuevosl).setVisibility(View.GONE);
findViewById(R.id.apellidosusuarionuevo_usuarionuevosl).setVisibility(View.GONE);
findViewById(R.id.cedulausuarionuevo_usuarionuevosl).setVisibility(View.GONE);
findViewById(R.id.telefono1usuarionuevo_usuarionuevosl).setVisibility(View.GONE);
findViewById(R.id.telefono2usuarionuevo_usuarionuevosl).setVisibility(View.GONE);
findViewById(R.id.fechaafiliacionusuarionuevo_usuarionuevosl).setVisibility(View.GONE);
findViewById(R.id.direccionusuarionuevo_usuarionuevosl).setVisibility(View.GONE);
findViewById(R.id.barriousuarionuevo_usuarionuevosl).setVisibility(View.GONE);
findViewById(R.id.valormensualusuarionuevo_usuarionuevosl).setVisibility(View.GONE);
findViewById(R.id.responsableusuarionuevo_usuarionuevosl).setVisibility(View.GONE);
findViewById(R.id.pa1usuarionuevo_usuarionuevosl).setVisibility(View.GONE);
findViewById(R.id.pa2usuarionuevo_usuarionuevosl).setVisibility(View.GONE);
findViewById(R.id.user_id_usuarionuevosl).setVisibility(View.GONE);
findViewById(R.id.groupuser_id_usuarionuevosl).setVisibility(View.GONE);
findViewById(R.id.todos_usuarionuevosl).setVisibility(View.GONE);
findViewById(R.id.excepgroupuser_id_usuarionuevosl).setVisibility(View.GONE);
findViewById(R.id.sistemacategoria_id_usuarionuevosl).setVisibility(View.GONE);
findViewById(R.id.sistema_id_usuarionuevosl).setVisibility(View.GONE);
*/
                findViewById(R.id.tr_botonesusuarionuevos).setVisibility(View.GONE);
               // placausuarionuevo_usuarionuevos.requestFocus();

            }else{
                /*findViewById(R.id.obserbacionsalidausuarionuevo_usuarionuevos).requestFocus();
                findViewById(R.id.obserbacionsalidausuarionuevo_usuarionuevos).clearFocus();*/
                findViewById(R.id.codigousuarionuevo_usuarionuevos).setEnabled(false);
((EditText)findViewById(R.id.codigousuarionuevo_usuarionuevos)).setTextColor(Color.BLACK);findViewById(R.id.nombresusuarionuevo_usuarionuevos).setEnabled(false);
((EditText)findViewById(R.id.nombresusuarionuevo_usuarionuevos)).setTextColor(Color.BLACK);findViewById(R.id.apellidosusuarionuevo_usuarionuevos).setEnabled(false);
((EditText)findViewById(R.id.apellidosusuarionuevo_usuarionuevos)).setTextColor(Color.BLACK);findViewById(R.id.cedulausuarionuevo_usuarionuevos).setEnabled(false);
((EditText)findViewById(R.id.cedulausuarionuevo_usuarionuevos)).setTextColor(Color.BLACK);findViewById(R.id.telefono1usuarionuevo_usuarionuevos).setEnabled(false);
((EditText)findViewById(R.id.telefono1usuarionuevo_usuarionuevos)).setTextColor(Color.BLACK);findViewById(R.id.telefono2usuarionuevo_usuarionuevos).setEnabled(false);
((EditText)findViewById(R.id.telefono2usuarionuevo_usuarionuevos)).setTextColor(Color.BLACK);findViewById(R.id.fechaafiliacionusuarionuevo_usuarionuevos).setEnabled(false);
((EditText)findViewById(R.id.fechaafiliacionusuarionuevo_usuarionuevos)).setTextColor(Color.BLACK);findViewById(R.id.direccionusuarionuevo_usuarionuevos).setEnabled(false);
((EditText)findViewById(R.id.direccionusuarionuevo_usuarionuevos)).setTextColor(Color.BLACK);findViewById(R.id.barriousuarionuevo_usuarionuevos).setEnabled(false);
((EditText)findViewById(R.id.barriousuarionuevo_usuarionuevos)).setTextColor(Color.BLACK);findViewById(R.id.valormensualusuarionuevo_usuarionuevos).setEnabled(false);
((EditText)findViewById(R.id.valormensualusuarionuevo_usuarionuevos)).setTextColor(Color.BLACK);findViewById(R.id.responsableusuarionuevo_usuarionuevos).setEnabled(false);
((EditText)findViewById(R.id.responsableusuarionuevo_usuarionuevos)).setTextColor(Color.BLACK);findViewById(R.id.pa1usuarionuevo_usuarionuevos).setEnabled(false);
((EditText)findViewById(R.id.pa1usuarionuevo_usuarionuevos)).setTextColor(Color.BLACK);findViewById(R.id.pa2usuarionuevo_usuarionuevos).setEnabled(false);
((EditText)findViewById(R.id.pa2usuarionuevo_usuarionuevos)).setTextColor(Color.BLACK);findViewById(R.id.user_id_usuarionuevos).setEnabled(false);
((EditText)findViewById(R.id.user_id_usuarionuevos)).setTextColor(Color.BLACK);findViewById(R.id.groupuser_id_usuarionuevos).setEnabled(false);
((EditText)findViewById(R.id.groupuser_id_usuarionuevos)).setTextColor(Color.BLACK);findViewById(R.id.todos_usuarionuevos).setEnabled(false);
((EditText)findViewById(R.id.todos_usuarionuevos)).setTextColor(Color.BLACK);findViewById(R.id.excepgroupuser_id_usuarionuevos).setEnabled(false);
((EditText)findViewById(R.id.excepgroupuser_id_usuarionuevos)).setTextColor(Color.BLACK);findViewById(R.id.sistemacategoria_id_usuarionuevos).setEnabled(false);
((EditText)findViewById(R.id.sistemacategoria_id_usuarionuevos)).setTextColor(Color.BLACK);findViewById(R.id.sistema_id_usuarionuevos).setEnabled(false);
((EditText)findViewById(R.id.sistema_id_usuarionuevos)).setTextColor(Color.BLACK);


                if(globalClass.getEnlinea()==null||globalClass.getEnlinea()=="false") {
                    loadUsuarionuevoSqlite(usuarionuevo.getId());
                }else {
                    loadRegistroUsuarionuevo(usuarionuevo.getId());
                }



            }
        }else{

        }
    }

    public void registrarUsuarionuevoSqlite(final String id){
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"sistemaonline",null,9);
        //SQLiteDatabase db=conn.getWritableDatabase();
        SQLiteDatabase db=conn.getReadableDatabase();
        ContentValues values=new ContentValues();
/*
        Cursor cursor=db.rawQuery("select * from "+ Utilidades.tabla_configusuarionuevos+" WHERE estadoconfigusuarionuevo=1 AND deleted_at ='null'  ",null);
        int contconfig=0;
        int dimensions=4;
        if(cursor.getCount()!=0){
            dimensions=cursor.getCount();
        }
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
        while(cursor.moveToNext()) {
            //Toast.makeText(getApplicationContext(),""+contconfig,Toast.LENGTH_SHORT).show();

            if(cursor.getString(cursor.getColumnIndex("tarifadiainicialconfigusuarionuevo")).equals("null")){


                valoresservicioshorainicial[contconfig] = (cursor.getString(cursor.getColumnIndex("servicioconfigusuarionuevo"))+","+cursor.getString(cursor.getColumnIndex("tarifahorainicialconfigusuarionuevo"))).split(",");
                valoresservicioshoraadicional[contconfig] = (cursor.getString(cursor.getColumnIndex("servicioconfigusuarionuevo"))+","+cursor.getString(cursor.getColumnIndex("tarifahoraadicionalconfigusuarionuevo"))).split(",");
            }else{
                valoresservicioshorainicial[contconfig] = (cursor.getString(cursor.getColumnIndex("servicioconfigusuarionuevo"))+","+cursor.getString(cursor.getColumnIndex("tarifadiainicialconfigusuarionuevo"))).split(",");
                valoresservicioshoraadicional[contconfig] = (cursor.getString(cursor.getColumnIndex("servicioconfigusuarionuevo"))+","+cursor.getString(cursor.getColumnIndex("tarifadiaadicionalconfigusuarionuevo"))).split(",");
            }
            contconfig++;
        }
        //Toast.makeText(getApplicationContext(),""+contconfig,Toast.LENGTH_SHORT).show();

        //String val=valoresservicioshorainicial.length;
        int saliorec=0;
        for(int rec=0;rec<valoresservicioshorainicial.length;rec++){
            if(false){
                saliorec=rec;
                //Toast.makeText(getApplicationContext(),"-"+valoresservicioshorainicial[rec][0]+"-"+((Spinner) findViewById(R.id.serviciousuarionuevo_usuarionuevos)).getSelectedItem().toString()+"-",Toast.LENGTH_SHORT).show();
            }

        }*/
        if(id!="0") {
            values.put(Utilidades.CAMPO_ID,id);
        }
        values.put(Utilidades.CAMPO_CODIGOUSUARIONUEVO,((TextView) findViewById(R.id.codigousuarionuevo_usuarionuevos)).getText().toString());
        values.put(Utilidades.CAMPO_NOMBRESUSUARIONUEVO,((TextView) findViewById(R.id.nombresusuarionuevo_usuarionuevos)).getText().toString());
        values.put(Utilidades.CAMPO_APELLIDOSUSUARIONUEVO,((TextView) findViewById(R.id.apellidosusuarionuevo_usuarionuevos)).getText().toString());
        values.put(Utilidades.CAMPO_CEDULAUSUARIONUEVO,((TextView) findViewById(R.id.cedulausuarionuevo_usuarionuevos)).getText().toString());
        values.put(Utilidades.CAMPO_TELEFONO1USUARIONUEVO,((TextView) findViewById(R.id.telefono1usuarionuevo_usuarionuevos)).getText().toString());
        values.put(Utilidades.CAMPO_TELEFONO2USUARIONUEVO,((TextView) findViewById(R.id.telefono2usuarionuevo_usuarionuevos)).getText().toString());
        values.put(Utilidades.CAMPO_FECHAAFILIACIONUSUARIONUEVO,((TextView) findViewById(R.id.fechaafiliacionusuarionuevo_usuarionuevos)).getText().toString());
        values.put(Utilidades.CAMPO_DIRECCIONUSUARIONUEVO,((TextView) findViewById(R.id.direccionusuarionuevo_usuarionuevos)).getText().toString());
        values.put(Utilidades.CAMPO_BARRIOUSUARIONUEVO,((TextView) findViewById(R.id.barriousuarionuevo_usuarionuevos)).getText().toString());
        values.put(Utilidades.CAMPO_VALORMENSUALUSUARIONUEVO,((TextView) findViewById(R.id.valormensualusuarionuevo_usuarionuevos)).getText().toString());
        values.put(Utilidades.CAMPO_RESPONSABLEUSUARIONUEVO,((TextView) findViewById(R.id.responsableusuarionuevo_usuarionuevos)).getText().toString());
        values.put(Utilidades.CAMPO_PA1USUARIONUEVO,((TextView) findViewById(R.id.pa1usuarionuevo_usuarionuevos)).getText().toString());
        values.put(Utilidades.CAMPO_PA2USUARIONUEVO,((TextView) findViewById(R.id.pa2usuarionuevo_usuarionuevos)).getText().toString());
        values.put(Utilidades.CAMPO_USER_ID,((TextView) findViewById(R.id.user_id_usuarionuevos)).getText().toString());
        values.put(Utilidades.CAMPO_GROUPUSER_ID,((TextView) findViewById(R.id.groupuser_id_usuarionuevos)).getText().toString());
        values.put(Utilidades.CAMPO_TODOS,((TextView) findViewById(R.id.todos_usuarionuevos)).getText().toString());
        values.put(Utilidades.CAMPO_EXCEPGROUPUSER_ID,((TextView) findViewById(R.id.excepgroupuser_id_usuarionuevos)).getText().toString());
        values.put(Utilidades.CAMPO_SISTEMACATEGORIA_ID,((TextView) findViewById(R.id.sistemacategoria_id_usuarionuevos)).getText().toString());
        values.put(Utilidades.CAMPO_SISTEMA_ID,((TextView) findViewById(R.id.sistema_id_usuarionuevos)).getText().toString());




        Long idResulttante =db.replace(Utilidades.tabla_usuarionuevos,Utilidades.CAMPO_ID,values);
        //Toast.makeText(getApplicationContext(),"respuesta: "+idResulttante,Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),"Registro guardado correctamente",Toast.LENGTH_SHORT).show();
        //db.execSQL("drop table usuarionuevos");



        Intent finalizar = new Intent(UsuarionuevoformActivity.this, UsuarionuevosActivity.class);
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
        if(ContextCompat.checkSelfPermission(UsuarionuevoformActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(UsuarionuevoformActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            }else{
                ActivityCompat.requestPermissions(UsuarionuevoformActivity.this,new String[]{
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
    private void registrarUsuarionuevoSqliteSQL(){
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"sistemaonline",null,1);
        SQLiteDatabase db=conn.getWritableDatabase();
        String insert="INSERT INTO "+Utilidades.tabla_usuarionuevos+" ( " +
                ""+Utilidades.CAMPO_ID+"" +
                ","+Utilidades.CAMPO_CODIGOUSUARIONUEVO+"" +
                ","+Utilidades.CAMPO_NOMBRESUSUARIONUEVO+"" +
                ","+Utilidades.CAMPO_APELLIDOSUSUARIONUEVO+"" +
                ","+Utilidades.CAMPO_CEDULAUSUARIONUEVO+"" +
                ","+Utilidades.CAMPO_TELEFONO1USUARIONUEVO+"" +
                ","+Utilidades.CAMPO_TELEFONO2USUARIONUEVO+"" +
                ","+Utilidades.CAMPO_FECHAAFILIACIONUSUARIONUEVO+"" +
                ","+Utilidades.CAMPO_DIRECCIONUSUARIONUEVO+"" +
                ","+Utilidades.CAMPO_BARRIOUSUARIONUEVO+"" +
                ","+Utilidades.CAMPO_VALORMENSUALUSUARIONUEVO+"" +
                ","+Utilidades.CAMPO_RESPONSABLEUSUARIONUEVO+"" +
                ","+Utilidades.CAMPO_PA1USUARIONUEVO+"" +
                ","+Utilidades.CAMPO_PA2USUARIONUEVO+"" +
                ","+Utilidades.CAMPO_USER_ID+"" +
                ","+Utilidades.CAMPO_GROUPUSER_ID+"" +
                ","+Utilidades.CAMPO_TODOS+"" +
                ","+Utilidades.CAMPO_EXCEPGROUPUSER_ID+"" +
                ","+Utilidades.CAMPO_SISTEMACATEGORIA_ID+"" +
                ","+Utilidades.CAMPO_SISTEMA_ID+"" +


                ") values ("+1+"" +
                ",'"+((TextView) findViewById(R.id.codigousuarionuevo_usuarionuevos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.nombresusuarionuevo_usuarionuevos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.apellidosusuarionuevo_usuarionuevos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.cedulausuarionuevo_usuarionuevos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.telefono1usuarionuevo_usuarionuevos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.telefono2usuarionuevo_usuarionuevos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.fechaafiliacionusuarionuevo_usuarionuevos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.direccionusuarionuevo_usuarionuevos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.barriousuarionuevo_usuarionuevos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.valormensualusuarionuevo_usuarionuevos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.responsableusuarionuevo_usuarionuevos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.pa1usuarionuevo_usuarionuevos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.pa2usuarionuevo_usuarionuevos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.user_id_usuarionuevos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.groupuser_id_usuarionuevos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.todos_usuarionuevos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.excepgroupuser_id_usuarionuevos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.sistemacategoria_id_usuarionuevos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.sistema_id_usuarionuevos)).getText().toString()+"'"+



                ")";
        db.execSQL(insert);
        //db.close();
    }
    public  void funcenviar(){
        fechausuarionuevo_usuarionuevos.getText().toString();
        //horausuarionuevo_usuarionuevos.getText().toString();
        if(globalClass.getEnlinea()==null||globalClass.getEnlinea()=="false") {
            if (usuarionuevo != null) {
                registrarUsuarionuevoSqlite((usuarionuevo == null ? "0" : usuarionuevo.getId()));
            } else {
                registrarUsuarionuevoSqlite((usuarionuevo == null ? "0" : usuarionuevo.getId()));
            }

            exportDatabse("sistemaonline");
        }else {
            registrarFormUsuarionuevos(
                    (usuarionuevo == null ? "0" : usuarionuevo.getId())
                    ,((TextView)findViewById(R.id.codigousuarionuevo_usuarionuevos)).getText().toString()
                    ,((TextView)findViewById(R.id.nombresusuarionuevo_usuarionuevos)).getText().toString()
                    ,((TextView)findViewById(R.id.apellidosusuarionuevo_usuarionuevos)).getText().toString()
                    ,((TextView)findViewById(R.id.cedulausuarionuevo_usuarionuevos)).getText().toString()
                    ,((TextView)findViewById(R.id.telefono1usuarionuevo_usuarionuevos)).getText().toString()
                    ,((TextView)findViewById(R.id.telefono2usuarionuevo_usuarionuevos)).getText().toString()
                    ,((TextView)findViewById(R.id.fechaafiliacionusuarionuevo_usuarionuevos)).getText().toString()
                    ,((TextView)findViewById(R.id.direccionusuarionuevo_usuarionuevos)).getText().toString()
                    ,((TextView)findViewById(R.id.barriousuarionuevo_usuarionuevos)).getText().toString()
                    ,((TextView)findViewById(R.id.valormensualusuarionuevo_usuarionuevos)).getText().toString()
                    ,((TextView)findViewById(R.id.responsableusuarionuevo_usuarionuevos)).getText().toString()
                    ,((TextView)findViewById(R.id.pa1usuarionuevo_usuarionuevos)).getText().toString()
                    ,((TextView)findViewById(R.id.pa2usuarionuevo_usuarionuevos)).getText().toString()
                    ,user_id,((TextView)findViewById(R.id.groupuser_id_usuarionuevos)).getText().toString()
                    ,((TextView)findViewById(R.id.todos_usuarionuevos)).getText().toString()
                    ,((TextView)findViewById(R.id.excepgroupuser_id_usuarionuevos)).getText().toString()
                    ,((TextView)findViewById(R.id.sistemacategoria_id_usuarionuevos)).getText().toString()
                    ,((TextView)findViewById(R.id.sistema_id_usuarionuevos)).getText().toString()

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

        fechausuarionuevo_usuarionuevos.setText(anio+"-"+leftPad( (mes+1)+"", 2, "0")+"-"+leftPad(  padLeftSpaces(dia), 2, "0"));
    }
    public void mostrarHora(){
        horausuarionuevo_usuarionuevos.setText(anio+"-"+(mes+1)+"-"+padLeftSpaces(dia));
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

        horausuarionuevo_usuarionuevos.setText(new StringBuilder().append(leftPad(hour+"",2,"0")).append(":").append(leftPad(min+"",2,"0"))
                .append("").append(format));
    }
    private void loadUsuarionuevoSqlite(final String id){

        //wherecedula="";
        SQLiteDatabase db=conn.getReadableDatabase();
        session = new SessionManager(getApplicationContext());
        sp = session.getVariables2();


        Cursor cursor=db.rawQuery("select * from  "+ Utilidades.tabla_usuarionuevos+"  where id="+id+" ",null);
        while(cursor.moveToNext()){

            /*if(cursor.getString(cursor.getColumnIndex("tiempousuarionuevo"))!=null) {
                ((TextView) findViewById(R.id.tiempousuarionuevo_usuarionuevos)).setText(cursor.getString(cursor.getColumnIndex("tiempousuarionuevo")).replace(".0", ""));
            }


            if(cursor.getString(cursor.getColumnIndex("subtotaladicionalusuarionuevo"))!=null) {
                ((TextView) findViewById(R.id.subtotaladicionalusuarionuevo_usuarionuevos)).setText(cursor.getString(cursor.getColumnIndex("subtotaladicionalusuarionuevo")).replace("-0", "0"));
            }*/

            ((TextView)findViewById(R.id.codigousuarionuevo_usuarionuevos)).setText(cursor.getString(cursor.getColumnIndex("codigousuarionuevo")));
            ((TextView)findViewById(R.id.nombresusuarionuevo_usuarionuevos)).setText(cursor.getString(cursor.getColumnIndex("nombresusuarionuevo")));
            ((TextView)findViewById(R.id.apellidosusuarionuevo_usuarionuevos)).setText(cursor.getString(cursor.getColumnIndex("apellidosusuarionuevo")));
            ((TextView)findViewById(R.id.cedulausuarionuevo_usuarionuevos)).setText(cursor.getString(cursor.getColumnIndex("cedulausuarionuevo")));
            ((TextView)findViewById(R.id.telefono1usuarionuevo_usuarionuevos)).setText(cursor.getString(cursor.getColumnIndex("telefono1usuarionuevo")));
            ((TextView)findViewById(R.id.telefono2usuarionuevo_usuarionuevos)).setText(cursor.getString(cursor.getColumnIndex("telefono2usuarionuevo")));
            ((TextView)findViewById(R.id.fechaafiliacionusuarionuevo_usuarionuevos)).setText(cursor.getString(cursor.getColumnIndex("fechaafiliacionusuarionuevo")));
            ((TextView)findViewById(R.id.direccionusuarionuevo_usuarionuevos)).setText(cursor.getString(cursor.getColumnIndex("direccionusuarionuevo")));
            ((TextView)findViewById(R.id.barriousuarionuevo_usuarionuevos)).setText(cursor.getString(cursor.getColumnIndex("barriousuarionuevo")));
            ((TextView)findViewById(R.id.valormensualusuarionuevo_usuarionuevos)).setText(cursor.getString(cursor.getColumnIndex("valormensualusuarionuevo")));
            ((TextView)findViewById(R.id.responsableusuarionuevo_usuarionuevos)).setText(cursor.getString(cursor.getColumnIndex("responsableusuarionuevo")));
            ((TextView)findViewById(R.id.pa1usuarionuevo_usuarionuevos)).setText(cursor.getString(cursor.getColumnIndex("pa1usuarionuevo")));
            ((TextView)findViewById(R.id.pa2usuarionuevo_usuarionuevos)).setText(cursor.getString(cursor.getColumnIndex("pa2usuarionuevo")));
            ((TextView)findViewById(R.id.user_id_usuarionuevos)).setText(cursor.getString(cursor.getColumnIndex("user_id")));
            ((TextView)findViewById(R.id.groupuser_id_usuarionuevos)).setText(cursor.getString(cursor.getColumnIndex("groupuser_id")));
            ((TextView)findViewById(R.id.todos_usuarionuevos)).setText(cursor.getString(cursor.getColumnIndex("todos")));
            ((TextView)findViewById(R.id.excepgroupuser_id_usuarionuevos)).setText(cursor.getString(cursor.getColumnIndex("excepgroupuser_id")));
            ((TextView)findViewById(R.id.sistemacategoria_id_usuarionuevos)).setText(cursor.getString(cursor.getColumnIndex("sistemacategoria_id")));
            ((TextView)findViewById(R.id.sistema_id_usuarionuevos)).setText(cursor.getString(cursor.getColumnIndex("sistema_id")));






        }


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        hideKeyboard(UsuarionuevoformActivity.this);
                    }
                },
                100);



    }
    private void loadRegistroUsuarionuevo(final String cobrocuota) {

        final ProgressDialog pDialog2 = new ProgressDialog(UsuarionuevoformActivity.this);
        pDialog2.setMessage("Cargando Registro...");
        pDialog2.setCancelable(false);
        pDialog2.show();

        AsyncTask<String, String, String> clientestask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                //String urlPedidos = "http://190.255.49.85/facturacion/CilAndroid/v1/index.php/buscar/cliente/" + cliente;
                //String urlPedidos = "http://sistemaonline.co/task_manager/v1/index.php/listar/usuarionuevo/" + usuarionuevo.getId();
                String urlPedidos = "http://sistemaonline.co/factura/public/api/usuarionuevo/editar/" + usuarionuevo.getId();
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

                                        ((TextView)findViewById(R.id.codigousuarionuevo_usuarionuevos)).setText(c.getString("codigousuarionuevo"));
                                        ((TextView)findViewById(R.id.nombresusuarionuevo_usuarionuevos)).setText(c.getString("nombresusuarionuevo"));
                                        ((TextView)findViewById(R.id.apellidosusuarionuevo_usuarionuevos)).setText(c.getString("apellidosusuarionuevo"));
                                        ((TextView)findViewById(R.id.cedulausuarionuevo_usuarionuevos)).setText(c.getString("cedulausuarionuevo"));
                                        ((TextView)findViewById(R.id.telefono1usuarionuevo_usuarionuevos)).setText(c.getString("telefono1usuarionuevo"));
                                        ((TextView)findViewById(R.id.telefono2usuarionuevo_usuarionuevos)).setText(c.getString("telefono2usuarionuevo"));
                                        ((TextView)findViewById(R.id.fechaafiliacionusuarionuevo_usuarionuevos)).setText(c.getString("fechaafiliacionusuarionuevo"));
                                        ((TextView)findViewById(R.id.direccionusuarionuevo_usuarionuevos)).setText(c.getString("direccionusuarionuevo"));
                                        ((TextView)findViewById(R.id.barriousuarionuevo_usuarionuevos)).setText(c.getString("barriousuarionuevo"));
                                        ((TextView)findViewById(R.id.valormensualusuarionuevo_usuarionuevos)).setText(c.getString("valormensualusuarionuevo"));
                                        ((TextView)findViewById(R.id.responsableusuarionuevo_usuarionuevos)).setText(c.getString("responsableusuarionuevo"));
                                        ((TextView)findViewById(R.id.pa1usuarionuevo_usuarionuevos)).setText(c.getString("pa1usuarionuevo"));
                                        ((TextView)findViewById(R.id.pa2usuarionuevo_usuarionuevos)).setText(c.getString("pa2usuarionuevo"));
                                        ((TextView)findViewById(R.id.user_id_usuarionuevos)).setText(c.getString("user_id"));
                                        ((TextView)findViewById(R.id.groupuser_id_usuarionuevos)).setText(c.getString("groupuser_id"));
                                        ((TextView)findViewById(R.id.todos_usuarionuevos)).setText(c.getString("todos"));
                                        ((TextView)findViewById(R.id.excepgroupuser_id_usuarionuevos)).setText(c.getString("excepgroupuser_id"));
                                        ((TextView)findViewById(R.id.sistemacategoria_id_usuarionuevos)).setText(c.getString("sistemacategoria_id"));
                                        ((TextView)findViewById(R.id.sistema_id_usuarionuevos)).setText(c.getString("sistema_id"));






                                    }

                                    pDialog2.hide();
                                    new android.os.Handler().postDelayed(
                                            new Runnable() {
                                                public void run() {

                                                    hideKeyboard(UsuarionuevoformActivity.this);
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
                MySingleton.getInstance(UsuarionuevoformActivity.this).addRequestQueue(jsonRequest);
                return "";
            }
        };

        clientestask.execute();
    }

    private void registrarFormUsuarionuevos(final String id ,final String codigousuarionuevo,final String nombresusuarionuevo,final String apellidosusuarionuevo,final String cedulausuarionuevo,final String telefono1usuarionuevo,final String telefono2usuarionuevo,final String fechaafiliacionusuarionuevo,final String direccionusuarionuevo,final String barriousuarionuevo,final String valormensualusuarionuevo,final String responsableusuarionuevo,final String pa1usuarionuevo,final String pa2usuarionuevo,final String user_id,final String groupuser_id,final String todos,final String excepgroupuser_id,final String sistemacategoria_id,final String sistema_id) {
        //final String ser_pro_entr = "108096144149";
        //tv_msg.setText(id_cli + " - " + id_dir + " - " + fec_ped + " - " + hra_ped + " - " + id_fac + " - " + tip_reg + " - " + tel_cli + " - " + id_pro + " - " + num_fac + " - " + val_pro + " - " + ser_pro_ent + " - " + subsidio + " - " + latitud + " - " + longitud + " - " + municipio);

        final ProgressDialog pDialog2 = new ProgressDialog(UsuarionuevoformActivity.this);
        pDialog2.setMessage("Registrando ...");
        pDialog2.setCancelable(false);
        pDialog2.show();

        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {


                String urlVenta = "http://sistemaonline.co/task_manager/v1/index.php/registrar/formusuarionuevo";


                StringRequest jsonRequest = new StringRequest(Request.Method.POST, urlVenta, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response.toString());
                            if (jsonObject.getString("error") == "true"){
                                Toast.makeText(UsuarionuevoformActivity.this,"ERROR",Toast.LENGTH_LONG).show();

                                //tv_msg.setText("Error: " + jsonObject.getString("message"));
                            }else{
                                //tv_msg.setText(jsonObject.getString("message"));
                                Toast.makeText(UsuarionuevoformActivity.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                Intent finalizar = new Intent(UsuarionuevoformActivity.this, UsuarionuevosActivity.class);
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
                        parameters.put("codigousuarionuevo", codigousuarionuevo);
                        parameters.put("nombresusuarionuevo", nombresusuarionuevo);
                        parameters.put("apellidosusuarionuevo", apellidosusuarionuevo);
                        parameters.put("cedulausuarionuevo", cedulausuarionuevo);
                        parameters.put("telefono1usuarionuevo", telefono1usuarionuevo);
                        parameters.put("telefono2usuarionuevo", telefono2usuarionuevo);
                        parameters.put("fechaafiliacionusuarionuevo", fechaafiliacionusuarionuevo);
                        parameters.put("direccionusuarionuevo", direccionusuarionuevo);
                        parameters.put("barriousuarionuevo", barriousuarionuevo);
                        parameters.put("valormensualusuarionuevo", valormensualusuarionuevo);
                        parameters.put("responsableusuarionuevo", responsableusuarionuevo);
                        parameters.put("pa1usuarionuevo", pa1usuarionuevo);
                        parameters.put("pa2usuarionuevo", pa2usuarionuevo);
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
                MySingleton.getInstance(UsuarionuevoformActivity.this).addRequestQueue(jsonRequest);
                return "";
            }
        };

        task.execute();
    }
    public void deleteRow(String value)
    {
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"sistemaonline",null,9);
        SQLiteDatabase db=conn.getWritableDatabase();
        db.execSQL("UPDATE " + Utilidades.tabla_usuarionuevos+ " SET deleted_at =datetime('now','localtime') WHERE "+Utilidades.CAMPO_ID+"='"+value+"'");
        db.close();
        Toast.makeText(UsuarionuevoformActivity.this,"Registro Eliminado",Toast.LENGTH_LONG).show();
        Intent finalizar = new Intent(UsuarionuevoformActivity.this, UsuarionuevosActivity.class);


        startActivity(finalizar);
        finish();
    }
    private void eliminarUsuarionuevos(final String idusuarionuevo) {

        final ProgressDialog pDialog2 = new ProgressDialog(UsuarionuevoformActivity.this);
        pDialog2.setMessage("Eliminando...");
        pDialog2.setCancelable(false);
        pDialog2.show();

        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {


                String urlVenta = "http://sistemaonline.co/factura/public/api/usuarionuevo/"+idusuarionuevo;


                StringRequest jsonRequest = new StringRequest(Request.Method.DELETE, urlVenta, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response.toString());
                            if (jsonObject.getString("error") == "true"){
                                Toast.makeText(UsuarionuevoformActivity.this,"ERROR",Toast.LENGTH_LONG).show();

                                //tv_msg.setText("Error: " + jsonObject.getString("message"));
                            }else{
                                //tv_msg.setText(jsonObject.getString("message"));
                                Toast.makeText(UsuarionuevoformActivity.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                Intent finalizar = new Intent(UsuarionuevoformActivity.this, UsuarionuevosActivity.class);
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
                        parameters.put("id", idusuarionuevo) ;






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
                MySingleton.getInstance(UsuarionuevoformActivity.this).addRequestQueue(jsonRequest);
                return "";
            }
        };

        task.execute();
    }

}
