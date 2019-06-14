package com.example.administrador.gestionusuarios;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
import android.widget.ImageView;
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

public class ProductoformActivity extends AppCompatActivity {
    private TextView placaproducto_productos, tv_producto, tv_msg, tv_placa;
    private String user_id,apiKey,vendedor, nom_conductor, placa, cedulan;
    private int dia,mes,anio;
    private Spinner spinner;
    private EditText fechaproducto_productos,horaproducto_productos;
    private static DatePickerDialog.OnDateSetListener selectorFec;
    private static TimePickerDialog.OnTimeSetListener selectorTim;
    private TimePicker timePicker1;
    private String format = "";
    private Button btnformproductos,btnformproductos2,btneditarproductos,btneliminarproductos;
    private Producto producto;
    private SharedPreferences sp;
    private Uri imageUri;
    SessionManager session;



    private final String CARPETA_RAIZ="ventasApp/";
    private final String RUTA_IMAGEN=CARPETA_RAIZ+"fotos";

    final int COD_SELECCIONA=10;
    final int COD_FOTO=20;

    Button botonCargar;
    ImageView imagen;
    String path;





    private static final int MY_PERMISSION_REQUEST_WHRITE_EXTERNAL=1;
    String enlinea;
    GlobalClass globalClass;
    ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"sistemaonline",null,10);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productoform);
        imagen= (ImageView) findViewById(R.id.imagemId);
        botonCargar= (Button) findViewById(R.id.btnCargarImg);
        checkPermission();
        globalClass=(GlobalClass)getApplicationContext();


        // Toast.makeText(getBaseContext(), globalClass.getEnlinea(), Toast.LENGTH_SHORT).show();
       /* int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        */

        String pathDB = getDatabasePath("sistemaonline").toString();
        /*copiaBD(pathDB,Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + getPackageName() + "/" + "xxxxxx");*/

        /*exportDatabse("sistemaonline"); //si funciona */
        //((EditText)findViewById(R.id.descuentoproducto_productos)).setHint("Dia");

        btnformproductos=(Button) findViewById(R.id.btn_crearformProducto);
        btnformproductos2=(Button) findViewById(R.id.btn_crearformProducto2);
        btneditarproductos=(Button) findViewById(R.id.btn_editarproductos);
        btneliminarproductos=(Button) findViewById(R.id.btn_eliminarproductos);

        //String[] items = new String[]{"CARRO", "MOTO", "BICICLETA"};

        Calendar calendar = Calendar.getInstance();
        anio = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);

       // fechaproducto_productos = (EditText) findViewById(R.id.fechaproducto_productos);
        session = new SessionManager(getApplicationContext());
        sp = session.getVariables2();
        user_id=sp.getString("name","23");
        String[] items = sp.getString("comboes","fdf,jj").split(",");

        if(sp.getString("cobrardias","false").equals("true")) {
            //((TextInputLayout) findViewById(R.id.tarifahoraadicionalproducto_productosl)).setHint("Dia adicional");
        }
        /*horaproducto_productos = (EditText) findViewById(R.id.horaproducto_productos);*/
       // mostrarFecha();
        /*selectorFec = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                anio = year;
                mes = month;
                dia = dayOfMonth;
                mostrarFecha();
            }
        };*/

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
            producto = getIntent().getExtras().getParcelable("producto");
        }
        if (producto != null){

            // ((TextView)findViewById(R.id.id_productos)).setText("" + producto.getId());
            ((TextView)findViewById(R.id.nombreproducto_productos)).setText("" + producto.getNombreproducto());
            ((TextView)findViewById(R.id.descripcionproducto_productos)).setText("" + producto.getDescripcionproducto());
            ((TextView)findViewById(R.id.marcaproducto_productos)).setText("" + producto.getMarcaproducto());
            ((TextView)findViewById(R.id.categoriaproducto_productos)).setText("" + producto.getCategoriaproducto());
            ((TextView)findViewById(R.id.referenciaproducto_productos)).setText("" + producto.getReferenciaproducto());
            ((TextView)findViewById(R.id.preciocompraproducto_productos)).setText("" + producto.getPreciocompraproducto());
            ((TextView)findViewById(R.id.precioventaproducto_productos)).setText("" + producto.getPrecioventaproducto());
            ((TextView)findViewById(R.id.existenciasproducto_productos)).setText("" + producto.getExistenciasproducto());
            ((TextView)findViewById(R.id.minimoexistenciasproducto_productos)).setText("" + producto.getMinimoexistenciasproducto());
            ((TextView)findViewById(R.id.maximoexistenciasproducto_productos)).setText("" + producto.getMaximoexistenciasproducto());
            ((TextView)findViewById(R.id.proveedorproducto_productos)).setText("" + producto.getProveedorproducto());
            ((TextView)findViewById(R.id.pa1producto_productos)).setText("" + producto.getPa1Producto());
            ((TextView)findViewById(R.id.pa2producto_productos)).setText("" + producto.getPa2Producto());
            ((TextView)findViewById(R.id.user_id_productos)).setText("" + producto.getUser_Id());
            ((TextView)findViewById(R.id.groupuser_id_productos)).setText("" + producto.getGroupuser_Id());
            ((TextView)findViewById(R.id.todos_productos)).setText("" + producto.getTodos());
            ((TextView)findViewById(R.id.excepgroupuser_id_productos)).setText("" + producto.getExcepgroupuser_Id());
            ((TextView)findViewById(R.id.sistemacategoria_id_productos)).setText("" + producto.getSistemacategoria_Id());
            ((TextView)findViewById(R.id.sistema_id_productos)).setText("" + producto.getSistema_Id());



            findViewById(R.id.btn_crearformProducto).setVisibility(View.VISIBLE);
            findViewById(R.id.btn_crearformProducto2).setVisibility(View.GONE);



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

        btneliminarproductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(globalClass.getEnlinea()==null||globalClass.getEnlinea()=="false") {
                    deleteRow(producto.getId());
                }else{
                    //eliminarProductos(producto.getId());
                    deleteRow(producto.getId());

                }

            }
        });
        btneditarproductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findViewById(R.id.nombreproducto_productos).setEnabled(true);
                findViewById(R.id.descripcionproducto_productos).setEnabled(true);
                findViewById(R.id.marcaproducto_productos).setEnabled(true);
                findViewById(R.id.categoriaproducto_productos).setEnabled(true);
                findViewById(R.id.referenciaproducto_productos).setEnabled(true);
                findViewById(R.id.preciocompraproducto_productos).setEnabled(true);
                findViewById(R.id.precioventaproducto_productos).setEnabled(true);
                findViewById(R.id.existenciasproducto_productos).setEnabled(true);
                findViewById(R.id.minimoexistenciasproducto_productos).setEnabled(true);
                findViewById(R.id.maximoexistenciasproducto_productos).setEnabled(true);
                findViewById(R.id.proveedorproducto_productos).setEnabled(true);
                findViewById(R.id.pa1producto_productos).setEnabled(true);
                findViewById(R.id.pa2producto_productos).setEnabled(true);
                findViewById(R.id.user_id_productos).setEnabled(true);
                findViewById(R.id.groupuser_id_productos).setEnabled(true);
                findViewById(R.id.todos_productos).setEnabled(true);
                findViewById(R.id.excepgroupuser_id_productos).setEnabled(true);
                findViewById(R.id.sistemacategoria_id_productos).setEnabled(true);
                findViewById(R.id.sistema_id_productos).setEnabled(true);


                /*$findViewByIdRid1*/

                findViewById(R.id.tr_botonesproductos).setVisibility(View.GONE);

                //findViewById(R.id.tableRowobsersalidabotonactualizar).setVisibility(View.GONE);
                findViewById(R.id.btn_crearformProducto2).setVisibility(View.VISIBLE);



            }

        });
        btnformproductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcenviar();

            }
        });
        btnformproductos2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funcenviar();

            }
        });

        botonCargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarImagen();

            }
        });

        /*placaproducto_productos= (TextView) findViewById(R.id.placaproducto_productos); */
        if (getIntent().getExtras() != null){
            cedulan = getIntent().getExtras().getString("editarproducto");
            if (cedulan != null){
                //placaproducto_productos.setVisibility(View.GONE);
                /*$findViewByIdRid1*/
                findViewById(R.id.tr_botonesproductos).setVisibility(View.GONE);
                //placaproducto_productos.requestFocus();

            }else{
                /*findViewById(R.id.obserbacionsalidaproducto_productos).requestFocus();
                findViewById(R.id.obserbacionsalidaproducto_productos).clearFocus();*/
                /*findViewById(R.id.nombreproducto_productos).setEnabled(false);
((EditText)findViewById(R.id.nombreproducto_productos)).setTextColor(Color.BLACK);findViewById(R.id.descripcionproducto_productos).setEnabled(false);
((EditText)findViewById(R.id.descripcionproducto_productos)).setTextColor(Color.BLACK);findViewById(R.id.marcaproducto_productos).setEnabled(false);
((EditText)findViewById(R.id.marcaproducto_productos)).setTextColor(Color.BLACK);findViewById(R.id.categoriaproducto_productos).setEnabled(false);
((EditText)findViewById(R.id.categoriaproducto_productos)).setTextColor(Color.BLACK);findViewById(R.id.referenciaproducto_productos).setEnabled(false);
((EditText)findViewById(R.id.referenciaproducto_productos)).setTextColor(Color.BLACK);findViewById(R.id.preciocompraproducto_productos).setEnabled(false);
((EditText)findViewById(R.id.preciocompraproducto_productos)).setTextColor(Color.BLACK);findViewById(R.id.precioventaproducto_productos).setEnabled(false);
((EditText)findViewById(R.id.precioventaproducto_productos)).setTextColor(Color.BLACK);findViewById(R.id.existenciasproducto_productos).setEnabled(false);
((EditText)findViewById(R.id.existenciasproducto_productos)).setTextColor(Color.BLACK);findViewById(R.id.minimoexistenciasproducto_productos).setEnabled(false);
((EditText)findViewById(R.id.minimoexistenciasproducto_productos)).setTextColor(Color.BLACK);findViewById(R.id.maximoexistenciasproducto_productos).setEnabled(false);
((EditText)findViewById(R.id.maximoexistenciasproducto_productos)).setTextColor(Color.BLACK);findViewById(R.id.proveedorproducto_productos).setEnabled(false);
((EditText)findViewById(R.id.proveedorproducto_productos)).setTextColor(Color.BLACK);findViewById(R.id.pa1producto_productos).setEnabled(false);
((EditText)findViewById(R.id.pa1producto_productos)).setTextColor(Color.BLACK);findViewById(R.id.pa2producto_productos).setEnabled(false);
((EditText)findViewById(R.id.pa2producto_productos)).setTextColor(Color.BLACK);findViewById(R.id.user_id_productos).setEnabled(false);
((EditText)findViewById(R.id.user_id_productos)).setTextColor(Color.BLACK);findViewById(R.id.groupuser_id_productos).setEnabled(false);
((EditText)findViewById(R.id.groupuser_id_productos)).setTextColor(Color.BLACK);findViewById(R.id.todos_productos).setEnabled(false);
((EditText)findViewById(R.id.todos_productos)).setTextColor(Color.BLACK);findViewById(R.id.excepgroupuser_id_productos).setEnabled(false);
((EditText)findViewById(R.id.excepgroupuser_id_productos)).setTextColor(Color.BLACK);findViewById(R.id.sistemacategoria_id_productos).setEnabled(false);
((EditText)findViewById(R.id.sistemacategoria_id_productos)).setTextColor(Color.BLACK);findViewById(R.id.sistema_id_productos).setEnabled(false);
((EditText)findViewById(R.id.sistema_id_productos)).setTextColor(Color.BLACK);*/


                if(globalClass.getEnlinea()==null||globalClass.getEnlinea()=="false") {
                    loadProductoSqlite(producto.getId());
                }else {
                    loadProductoSqlite(producto.getId());
                    //loadRegistroProducto(producto.getId());
                }



            }
        }else{

        }
    }
    private void cargarImagen() {

        final CharSequence[] opciones={"Tomar Foto","Cargar Imagen","Cancelar"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(ProductoformActivity.this);
        alertOpciones.setTitle("Seleccione una Opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar Foto")){
                    tomarFotografia();
                }else{
                    if (opciones[i].equals("Cargar Imagen")){
                        Intent intent=new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),COD_SELECCIONA);
                    }else{
                        dialogInterface.dismiss();
                    }
                }
            }
        });
        alertOpciones.show();

    }
    private void tomarFotografia() {
        File fileImagen=new File(Environment.getExternalStorageDirectory(),RUTA_IMAGEN);
        boolean isCreada=fileImagen.exists();
        String nombreImagen="";
        if(isCreada==false){
            isCreada=fileImagen.mkdirs();
        }

        if(isCreada==true){
            nombreImagen=(System.currentTimeMillis()/1000)+".jpg";
        }


        path=Environment.getExternalStorageDirectory()+
                File.separator+RUTA_IMAGEN+File.separator+nombreImagen;
        //Toast.makeText(getApplicationContext(),path,Toast.LENGTH_SHORT).show();
        File imagen=new File(path);

        Intent intent=null;
        intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ////
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
        {
            String authorities=getApplicationContext().getPackageName()+".provider";
            imageUri= FileProvider.getUriForFile(this,authorities,imagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }else
        {
            imageUri=Uri.fromFile(imagen);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen));
        }
        startActivityForResult(intent,COD_FOTO);

        ////
    }
    public void registrarProductoSqlite(final String id){
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"sistemaonline",null,10);
        //SQLiteDatabase db=conn.getWritableDatabase();
        SQLiteDatabase db=conn.getReadableDatabase();
        ContentValues values=new ContentValues();


        if(id!="0") {
            values.put(Utilidades.CAMPO_ID,id);
        }
        values.put(Utilidades.CAMPO_NOMBREPRODUCTO,((TextView) findViewById(R.id.nombreproducto_productos)).getText().toString());
        values.put(Utilidades.CAMPO_DESCRIPCIONPRODUCTO,((TextView) findViewById(R.id.descripcionproducto_productos)).getText().toString());
        values.put(Utilidades.CAMPO_MARCAPRODUCTO,((TextView) findViewById(R.id.marcaproducto_productos)).getText().toString());
        values.put(Utilidades.CAMPO_CATEGORIAPRODUCTO,((TextView) findViewById(R.id.categoriaproducto_productos)).getText().toString());
        values.put(Utilidades.CAMPO_REFERENCIAPRODUCTO,((TextView) findViewById(R.id.referenciaproducto_productos)).getText().toString());
        values.put(Utilidades.CAMPO_PRECIOCOMPRAPRODUCTO,((TextView) findViewById(R.id.preciocompraproducto_productos)).getText().toString());
        values.put(Utilidades.CAMPO_PRECIOVENTAPRODUCTO,((TextView) findViewById(R.id.precioventaproducto_productos)).getText().toString());
        values.put(Utilidades.CAMPO_EXISTENCIASPRODUCTO,((TextView) findViewById(R.id.existenciasproducto_productos)).getText().toString());
        values.put(Utilidades.CAMPO_MINIMOEXISTENCIASPRODUCTO,((TextView) findViewById(R.id.minimoexistenciasproducto_productos)).getText().toString());
        values.put(Utilidades.CAMPO_MAXIMOEXISTENCIASPRODUCTO,((TextView) findViewById(R.id.maximoexistenciasproducto_productos)).getText().toString());
        values.put(Utilidades.CAMPO_PROVEEDORPRODUCTO,((TextView) findViewById(R.id.proveedorproducto_productos)).getText().toString());


        values.put(Utilidades.CAMPO_PA1PRODUCTO,(/*(TextView) findViewById(R.id.pa1producto_productos)).getText().toString()*/(imageUri==null?((TextView) findViewById(R.id.pa1producto_productos)).getText().toString():convertMediaUriToPath(imageUri))));
        values.put(Utilidades.CAMPO_PA2PRODUCTO,((TextView) findViewById(R.id.pa2producto_productos)).getText().toString());
        values.put(Utilidades.CAMPO_USER_ID,((TextView) findViewById(R.id.user_id_productos)).getText().toString());
        values.put(Utilidades.CAMPO_GROUPUSER_ID,((TextView) findViewById(R.id.groupuser_id_productos)).getText().toString());
        values.put(Utilidades.CAMPO_TODOS,((TextView) findViewById(R.id.todos_productos)).getText().toString());
        values.put(Utilidades.CAMPO_EXCEPGROUPUSER_ID,((TextView) findViewById(R.id.excepgroupuser_id_productos)).getText().toString());
        values.put(Utilidades.CAMPO_SISTEMACATEGORIA_ID,((TextView) findViewById(R.id.sistemacategoria_id_productos)).getText().toString());
        values.put(Utilidades.CAMPO_SISTEMA_ID,((TextView) findViewById(R.id.sistema_id_productos)).getText().toString());




        Long idResulttante =db.replace(Utilidades.tabla_productos,Utilidades.CAMPO_ID,values);
        //Toast.makeText(getApplicationContext(),"respuesta: "+idResulttante,Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),"Registro guardado correctamente",Toast.LENGTH_SHORT).show();
        //db.execSQL("drop table productos");



        Intent finalizar = new Intent(ProductoformActivity.this, ProductosActivity.class);
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

    public String convertMediaUriToPath(Uri uri) {
        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, proj,  null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
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
        if(ContextCompat.checkSelfPermission(ProductoformActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(ProductoformActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            }else{
                ActivityCompat.requestPermissions(ProductoformActivity.this,new String[]{
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
    private void registrarProductoSqliteSQL(){
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"sistemaonline",null,1);
        SQLiteDatabase db=conn.getWritableDatabase();
        String insert="INSERT INTO "+Utilidades.tabla_productos+" ( " +
                ""+Utilidades.CAMPO_ID+"" +
                ","+Utilidades.CAMPO_NOMBREPRODUCTO+"" +
                ","+Utilidades.CAMPO_DESCRIPCIONPRODUCTO+"" +
                ","+Utilidades.CAMPO_MARCAPRODUCTO+"" +
                ","+Utilidades.CAMPO_CATEGORIAPRODUCTO+"" +
                ","+Utilidades.CAMPO_REFERENCIAPRODUCTO+"" +
                ","+Utilidades.CAMPO_PRECIOCOMPRAPRODUCTO+"" +
                ","+Utilidades.CAMPO_PRECIOVENTAPRODUCTO+"" +
                ","+Utilidades.CAMPO_EXISTENCIASPRODUCTO+"" +
                ","+Utilidades.CAMPO_MINIMOEXISTENCIASPRODUCTO+"" +
                ","+Utilidades.CAMPO_MAXIMOEXISTENCIASPRODUCTO+"" +
                ","+Utilidades.CAMPO_PROVEEDORPRODUCTO+"" +
                ","+Utilidades.CAMPO_PA1PRODUCTO+"" +
                ","+Utilidades.CAMPO_PA2PRODUCTO+"" +
                ","+Utilidades.CAMPO_USER_ID+"" +
                ","+Utilidades.CAMPO_GROUPUSER_ID+"" +
                ","+Utilidades.CAMPO_TODOS+"" +
                ","+Utilidades.CAMPO_EXCEPGROUPUSER_ID+"" +
                ","+Utilidades.CAMPO_SISTEMACATEGORIA_ID+"" +
                ","+Utilidades.CAMPO_SISTEMA_ID+"" +


                ") values ("+1+"" +
                ",'"+((TextView) findViewById(R.id.nombreproducto_productos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.descripcionproducto_productos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.marcaproducto_productos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.categoriaproducto_productos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.referenciaproducto_productos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.preciocompraproducto_productos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.precioventaproducto_productos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.existenciasproducto_productos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.minimoexistenciasproducto_productos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.maximoexistenciasproducto_productos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.proveedorproducto_productos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.pa1producto_productos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.pa2producto_productos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.user_id_productos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.groupuser_id_productos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.todos_productos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.excepgroupuser_id_productos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.sistemacategoria_id_productos)).getText().toString()+"'"+
                ",'"+((TextView) findViewById(R.id.sistema_id_productos)).getText().toString()+"'"+



                ")";
        db.execSQL(insert);
        //db.close();
    }
    public  void funcenviar(){
//        fechaproducto_productos.getText().toString();
        //horaproducto_productos.getText().toString();
        if(/*globalClass.getEnlinea()==null||globalClass.getEnlinea()=="false"*/true) {
            if (producto != null) {
                registrarProductoSqlite((producto == null ? "0" : producto.getId()));
            } else {
                registrarProductoSqlite((producto == null ? "0" : producto.getId()));
            }

            exportDatabse("sistemaonline");
        }else {
            registrarFormProductos(
                    (producto == null ? "0" : producto.getId())
                    ,((TextView)findViewById(R.id.nombreproducto_productos)).getText().toString()
                    ,((TextView)findViewById(R.id.descripcionproducto_productos)).getText().toString()
                    ,((TextView)findViewById(R.id.marcaproducto_productos)).getText().toString()
                    ,((TextView)findViewById(R.id.categoriaproducto_productos)).getText().toString()
                    ,((TextView)findViewById(R.id.referenciaproducto_productos)).getText().toString()
                    ,((TextView)findViewById(R.id.preciocompraproducto_productos)).getText().toString()
                    ,((TextView)findViewById(R.id.precioventaproducto_productos)).getText().toString()
                    ,((TextView)findViewById(R.id.existenciasproducto_productos)).getText().toString()
                    ,((TextView)findViewById(R.id.minimoexistenciasproducto_productos)).getText().toString()
                    ,((TextView)findViewById(R.id.maximoexistenciasproducto_productos)).getText().toString()
                    ,((TextView)findViewById(R.id.proveedorproducto_productos)).getText().toString()
                    ,((TextView)findViewById(R.id.pa1producto_productos)).getText().toString()
                    ,((TextView)findViewById(R.id.pa2producto_productos)).getText().toString()
                    ,user_id,((TextView)findViewById(R.id.groupuser_id_productos)).getText().toString()
                    ,((TextView)findViewById(R.id.todos_productos)).getText().toString()
                    ,((TextView)findViewById(R.id.excepgroupuser_id_productos)).getText().toString()
                    ,((TextView)findViewById(R.id.sistemacategoria_id_productos)).getText().toString()
                    ,((TextView)findViewById(R.id.sistema_id_productos)).getText().toString()

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

        fechaproducto_productos.setText(anio+"-"+leftPad( (mes+1)+"", 2, "0")+"-"+leftPad(  padLeftSpaces(dia), 2, "0"));
    }
    public void mostrarHora(){
        horaproducto_productos.setText(anio+"-"+(mes+1)+"-"+padLeftSpaces(dia));
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

        horaproducto_productos.setText(new StringBuilder().append(leftPad(hour+"",2,"0")).append(":").append(leftPad(min+"",2,"0"))
                .append("").append(format));
    }
    private void loadProductoSqlite(final String id){

        //wherecedula="";
        SQLiteDatabase db=conn.getReadableDatabase();
        session = new SessionManager(getApplicationContext());
        sp = session.getVariables2();


        Cursor cursor=db.rawQuery("select * from productos where id="+id+" ",null);
        while(cursor.moveToNext()){

            /*if(cursor.getString(cursor.getColumnIndex("tiempoproducto"))!=null) {
                ((TextView) findViewById(R.id.tiempoproducto_productos)).setText(cursor.getString(cursor.getColumnIndex("tiempoproducto")).replace(".0", ""));
            }


            if(cursor.getString(cursor.getColumnIndex("subtotaladicionalproducto"))!=null) {
                ((TextView) findViewById(R.id.subtotaladicionalproducto_productos)).setText(cursor.getString(cursor.getColumnIndex("subtotaladicionalproducto")).replace("-0", "0"));
            }*/

            ((TextView)findViewById(R.id.nombreproducto_productos)).setText(cursor.getString(cursor.getColumnIndex("nombreproducto")));
            ((TextView)findViewById(R.id.descripcionproducto_productos)).setText(cursor.getString(cursor.getColumnIndex("descripcionproducto")));
            ((TextView)findViewById(R.id.marcaproducto_productos)).setText(cursor.getString(cursor.getColumnIndex("marcaproducto")));
            ((TextView)findViewById(R.id.categoriaproducto_productos)).setText(cursor.getString(cursor.getColumnIndex("categoriaproducto")));
            ((TextView)findViewById(R.id.referenciaproducto_productos)).setText(cursor.getString(cursor.getColumnIndex("referenciaproducto")));
            ((TextView)findViewById(R.id.preciocompraproducto_productos)).setText(cursor.getString(cursor.getColumnIndex("preciocompraproducto")));
            ((TextView)findViewById(R.id.precioventaproducto_productos)).setText(cursor.getString(cursor.getColumnIndex("precioventaproducto")));
            ((TextView)findViewById(R.id.existenciasproducto_productos)).setText(cursor.getString(cursor.getColumnIndex("existenciasproducto")));
            ((TextView)findViewById(R.id.minimoexistenciasproducto_productos)).setText(cursor.getString(cursor.getColumnIndex("minimoexistenciasproducto")));
            ((TextView)findViewById(R.id.maximoexistenciasproducto_productos)).setText(cursor.getString(cursor.getColumnIndex("maximoexistenciasproducto")));
            ((TextView)findViewById(R.id.proveedorproducto_productos)).setText(cursor.getString(cursor.getColumnIndex("proveedorproducto")));
            ((TextView)findViewById(R.id.pa1producto_productos)).setText(cursor.getString(cursor.getColumnIndex("pa1producto")));
            ((TextView)findViewById(R.id.pa2producto_productos)).setText(cursor.getString(cursor.getColumnIndex("pa2producto")));
            ((TextView)findViewById(R.id.user_id_productos)).setText(cursor.getString(cursor.getColumnIndex("user_id")));
            ((TextView)findViewById(R.id.groupuser_id_productos)).setText(cursor.getString(cursor.getColumnIndex("groupuser_id")));
            ((TextView)findViewById(R.id.todos_productos)).setText(cursor.getString(cursor.getColumnIndex("todos")));
            ((TextView)findViewById(R.id.excepgroupuser_id_productos)).setText(cursor.getString(cursor.getColumnIndex("excepgroupuser_id")));
            ((TextView)findViewById(R.id.sistemacategoria_id_productos)).setText(cursor.getString(cursor.getColumnIndex("sistemacategoria_id")));
            ((TextView)findViewById(R.id.sistema_id_productos)).setText(cursor.getString(cursor.getColumnIndex("sistema_id")));



            if(!cursor.getString(cursor.getColumnIndex("pa1producto")).equals("")) {
                imagen.setImageBitmap(BitmapFactory.decodeFile(cursor.getString(cursor.getColumnIndex("pa1producto"))));
            }
            /*File imagen2=new File(cursor.getString(cursor.getColumnIndex("pa1producto")));

            Intent intent=null;

                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen2));*/

            /*imageUri=data.getData();
            imagen.setImageURI(imageUri);

            /*File imagen2=new File(cursor.getString(cursor.getColumnIndex("pa1producto")));
            imagen.setImageURI(Uri.fromFile(imagen2));


            /*Intent intent=null;
            intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            ////
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
            {
                String authorities=getApplicationContext().getPackageName()+".provider";
                imageUri= FileProvider.getUriForFile(this,authorities,imagen2);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            }else
            {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagen2));
            }
            startActivityForResult(intent,COD_FOTO);
            //Bitmap bm = bitmap.decodefile(Uri.decode(cursor.getString(cursor.getColumnIndex("pa1producto"))));
            //bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(),Uri.parse(filePath));

            //imagen.setImageBitmap(bm);
            */






        }


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        hideKeyboard(ProductoformActivity.this);
                    }
                },
                100);



    }
    private void loadRegistroProducto(final String cobrocuota) {

        final ProgressDialog pDialog2 = new ProgressDialog(ProductoformActivity.this);
        pDialog2.setMessage("Cargando Registro...");
        pDialog2.setCancelable(false);
        pDialog2.show();

        AsyncTask<String, String, String> clientestask = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                //String urlPedidos = "http://190.255.49.85/facturacion/CilAndroid/v1/index.php/buscar/cliente/" + cliente;
                //String urlPedidos = "http://sistemaonline.co/task_manager/v1/index.php/listar/producto/" + producto.getId();
                String urlPedidos = "http://sistemaonline.co/factura/public/api/producto/editar/" + producto.getId();
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

                                        ((TextView)findViewById(R.id.nombreproducto_productos)).setText(c.getString("nombreproducto"));
                                        ((TextView)findViewById(R.id.descripcionproducto_productos)).setText(c.getString("descripcionproducto"));
                                        ((TextView)findViewById(R.id.marcaproducto_productos)).setText(c.getString("marcaproducto"));
                                        ((TextView)findViewById(R.id.categoriaproducto_productos)).setText(c.getString("categoriaproducto"));
                                        ((TextView)findViewById(R.id.referenciaproducto_productos)).setText(c.getString("referenciaproducto"));
                                        ((TextView)findViewById(R.id.preciocompraproducto_productos)).setText(c.getString("preciocompraproducto"));
                                        ((TextView)findViewById(R.id.precioventaproducto_productos)).setText(c.getString("precioventaproducto"));
                                        ((TextView)findViewById(R.id.existenciasproducto_productos)).setText(c.getString("existenciasproducto"));
                                        ((TextView)findViewById(R.id.minimoexistenciasproducto_productos)).setText(c.getString("minimoexistenciasproducto"));
                                        ((TextView)findViewById(R.id.maximoexistenciasproducto_productos)).setText(c.getString("maximoexistenciasproducto"));
                                        ((TextView)findViewById(R.id.proveedorproducto_productos)).setText(c.getString("proveedorproducto"));
                                        ((TextView)findViewById(R.id.pa1producto_productos)).setText(c.getString("pa1producto"));
                                        ((TextView)findViewById(R.id.pa2producto_productos)).setText(c.getString("pa2producto"));
                                        ((TextView)findViewById(R.id.user_id_productos)).setText(c.getString("user_id"));
                                        ((TextView)findViewById(R.id.groupuser_id_productos)).setText(c.getString("groupuser_id"));
                                        ((TextView)findViewById(R.id.todos_productos)).setText(c.getString("todos"));
                                        ((TextView)findViewById(R.id.excepgroupuser_id_productos)).setText(c.getString("excepgroupuser_id"));
                                        ((TextView)findViewById(R.id.sistemacategoria_id_productos)).setText(c.getString("sistemacategoria_id"));
                                        ((TextView)findViewById(R.id.sistema_id_productos)).setText(c.getString("sistema_id"));






                                    }

                                    pDialog2.hide();
                                    new android.os.Handler().postDelayed(
                                            new Runnable() {
                                                public void run() {

                                                    hideKeyboard(ProductoformActivity.this);
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
                MySingleton.getInstance(ProductoformActivity.this).addRequestQueue(jsonRequest);
                return "";
            }
        };

        clientestask.execute();
    }

    private void registrarFormProductos(final String id ,final String nombreproducto,final String descripcionproducto,final String marcaproducto,final String categoriaproducto,final String referenciaproducto,final String preciocompraproducto,final String precioventaproducto,final String existenciasproducto,final String minimoexistenciasproducto,final String maximoexistenciasproducto,final String proveedorproducto,final String pa1producto,final String pa2producto,final String user_id,final String groupuser_id,final String todos,final String excepgroupuser_id,final String sistemacategoria_id,final String sistema_id) {
        //final String ser_pro_entr = "108096144149";
        //tv_msg.setText(id_cli + " - " + id_dir + " - " + fec_ped + " - " + hra_ped + " - " + id_fac + " - " + tip_reg + " - " + tel_cli + " - " + id_pro + " - " + num_fac + " - " + val_pro + " - " + ser_pro_ent + " - " + subsidio + " - " + latitud + " - " + longitud + " - " + municipio);

        final ProgressDialog pDialog2 = new ProgressDialog(ProductoformActivity.this);
        pDialog2.setMessage("Registrando ...");
        pDialog2.setCancelable(false);
        pDialog2.show();

        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {


                String urlVenta = "http://sistemaonline.co/task_manager/v1/index.php/registrar/formproducto";


                StringRequest jsonRequest = new StringRequest(Request.Method.POST, urlVenta, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response.toString());
                            if (jsonObject.getString("error") == "true"){
                                Toast.makeText(ProductoformActivity.this,"ERROR",Toast.LENGTH_LONG).show();

                                //tv_msg.setText("Error: " + jsonObject.getString("message"));
                            }else{
                                //tv_msg.setText(jsonObject.getString("message"));
                                Toast.makeText(ProductoformActivity.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                Intent finalizar = new Intent(ProductoformActivity.this, ProductosActivity.class);
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
                        parameters.put("nombreproducto", nombreproducto);
                        parameters.put("descripcionproducto", descripcionproducto);
                        parameters.put("marcaproducto", marcaproducto);
                        parameters.put("categoriaproducto", categoriaproducto);
                        parameters.put("referenciaproducto", referenciaproducto);
                        parameters.put("preciocompraproducto", preciocompraproducto);
                        parameters.put("precioventaproducto", precioventaproducto);
                        parameters.put("existenciasproducto", existenciasproducto);
                        parameters.put("minimoexistenciasproducto", minimoexistenciasproducto);
                        parameters.put("maximoexistenciasproducto", maximoexistenciasproducto);
                        parameters.put("proveedorproducto", proveedorproducto);
                        parameters.put("pa1producto", pa1producto);
                        parameters.put("pa2producto", pa2producto);
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
                MySingleton.getInstance(ProductoformActivity.this).addRequestQueue(jsonRequest);
                return "";
            }
        };

        task.execute();
    }
    public void deleteRow(String value)
    {
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"sistemaonline",null,10);
        SQLiteDatabase db=conn.getWritableDatabase();
        db.execSQL("UPDATE " + Utilidades.tabla_productos+ " SET deleted_at =datetime('now','localtime') WHERE "+Utilidades.CAMPO_ID+"='"+value+"'");
        db.close();
        Toast.makeText(ProductoformActivity.this,"Registro Eliminado",Toast.LENGTH_LONG).show();
        Intent finalizar = new Intent(ProductoformActivity.this, ProductosActivity.class);


        startActivity(finalizar);
        finish();
    }
    private void eliminarProductos(final String idproducto) {

        final ProgressDialog pDialog2 = new ProgressDialog(ProductoformActivity.this);
        pDialog2.setMessage("Eliminando...");
        pDialog2.setCancelable(false);
        pDialog2.show();

        AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {


                String urlVenta = "http://sistemaonline.co/factura/public/api/producto/"+idproducto;


                StringRequest jsonRequest = new StringRequest(Request.Method.DELETE, urlVenta, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject= new JSONObject(response.toString());
                            if (jsonObject.getString("error") == "true"){
                                Toast.makeText(ProductoformActivity.this,"ERROR",Toast.LENGTH_LONG).show();

                                //tv_msg.setText("Error: " + jsonObject.getString("message"));
                            }else{
                                //tv_msg.setText(jsonObject.getString("message"));
                                Toast.makeText(ProductoformActivity.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                Intent finalizar = new Intent(ProductoformActivity.this, ProductosActivity.class);
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
                        parameters.put("id", idproducto) ;






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
                MySingleton.getInstance(ProductoformActivity.this).addRequestQueue(jsonRequest);
                return "";
            }
        };

        task.execute();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){

            switch (requestCode){
                case COD_SELECCIONA:
                    imageUri=data.getData();
                    imagen.setImageURI(imageUri);

                    break;

                case COD_FOTO:
                    MediaScannerConnection.scanFile(this, new String[]{path}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    imageUri=uri;
                                    //Log.i("Ruta de almacenamiento","Path: "+path);
                                }
                            });

                    Bitmap bitmap= BitmapFactory.decodeFile(path);
                    imagen.setImageBitmap(bitmap);

                    break;
            }


        }
    }

}
