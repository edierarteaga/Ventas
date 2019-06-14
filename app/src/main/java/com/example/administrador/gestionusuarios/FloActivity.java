package com.example.administrador.gestionusuarios;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
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
import com.example.administrador.gestionusuarios.utilidades.Utilidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.example.administrador.gestionusuarios.ProductosActivity.hideKeyboard;

public class FloActivity extends AppCompatActivity {
    private List<String> mList;
    private RecyclerView rv_productos;
    private ProductoAdapter adapter;
    private List<Producto> productolist;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mActionButton;
    private Button  btn_crearProducto,btn_buscarced;
    private ListaAdapter listAdapter;
    private List<Producto> listaProductos;
    private GridLayoutManager glm_cli;
    ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"sistemaonline",null,10);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flo);
        rv_productos = (RecyclerView) findViewById(R.id.rv_productos);
        btn_crearProducto = (Button) findViewById(R.id.btn_crearProducto);

        setUpView();
        fakeData();
        glm_cli = new GridLayoutManager(this,1);
        rv_productos.setLayoutManager(glm_cli);
        //setUpRecyclerView();
        loadProductosSqlite("0");
        //showHideWhenScroll();
        btn_crearProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productonew = new Intent(FloActivity.this, ProductoformActivity.class);
                productonew.putExtra("editarproducto","1");
                startActivity(productonew);

            }
        });
    }
    private void setUpView() {
        //mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mActionButton = (FloatingActionButton) findViewById(R.id.fab);
    }

    private void fakeData() {
        mList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 100; i++)
            mList.add(String.valueOf(random.nextInt(100)));
    }

    private void setUpRecyclerView() {

        /*mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);*/
        adapter = new ProductoAdapter(FloActivity.this,productolist,'n');
        rv_productos.setAdapter(adapter);
       /* listAdapter = new ListaAdapter(FloActivity.this,mList);
        mRecyclerView.setAdapter(listAdapter);*/
    }

    private void showHideWhenScroll() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) mActionButton.hide();
                else mActionButton.show();
                super.onScrolled(recyclerView, dx, dy);
            }
        });
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
        Cursor cursor=db.rawQuery("select * from "+ Utilidades.tabla_productos+" where deleted_at is null "+wherecedula+"  order by id desc ",null);
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

        adapter = new ProductoAdapter(FloActivity.this,listaProductos,'n');
        rv_productos.setAdapter(adapter);
        /*tv_msg.setText("Resultados encontrados: " + adapter.getItemCount());*/
        adapter.notifyDataSetChanged();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        hideKeyboard(FloActivity.this);
                    }
                },
                1000);
        //btn_crearProducto.setVisibility(View.INVISIBLE);
    }
}
