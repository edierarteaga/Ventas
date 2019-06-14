package com.example.administrador.gestionusuarios;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrador.gestionusuarios.utilidades.GlobalClass;
import com.example.administrador.gestionusuarios.utilidades.Utilidades;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EDIER on 01/09/2017.
 */

class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder>{
    private Context ctx;
    private List<Producto> productos;
    private char tipo_clic;
    private ImageButton ib_sumarProducto;
    private ImageButton ib_restarProducto;
    private EditText et_unidadesacomprar;
    private ImageView personPhoto;




    public ProductoAdapter(Context ctx, List<Producto> productos, char tipo_clic){
        this.ctx = ctx;
        this.productos = productos;
        this.tipo_clic = tipo_clic;

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cv;
        TextView producto;
        TextView direccion;
        TextView telefono;
        ImageView personPhoto;
        EditText et_unidadesacomprar;
        ImageButton ib_sumarProducto;
        ImageButton ib_restarProducto;
        public ImageView imagen;
       // public EditText text;
        Context ctx;
        List<Producto> productos = new ArrayList<Producto>();


        public ViewHolder(final View itemView, Context ctx,final List<Producto> productos) {

            super(itemView);
            itemView.setOnClickListener(this);
            this.productos = productos;
            this.ctx = ctx;


            //et_unidadesacomprar = tv;

            cv = (CardView)itemView.findViewById(R.id.cv);
            producto = (TextView)itemView.findViewById(R.id.zona);
            direccion = (TextView)itemView.findViewById(R.id.capacidad);
            telefono = (TextView)itemView.findViewById(R.id.telefono);
            personPhoto = (ImageView)itemView.findViewById(R.id.per_photo);
            ib_sumarProducto= (ImageButton)itemView.findViewById(R.id.btnSumarProducto);
            ib_restarProducto= (ImageButton)itemView.findViewById(R.id.BtnRestarProducto);
            et_unidadesacomprar=(EditText) itemView.findViewById(R.id.unidadesacomprar);
            imagen= (ImageView) itemView.findViewById(R.id.per_photo);

            //text=et_unidadesacomprar;


            ib_sumarProducto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ProductosActivity.globalClass.getEnlinea() == null || ProductosActivity.globalClass.getEnlinea() == "false") {
                        EditText text;
                        int i = getAdapterPosition();
                        Producto producto = productos.get(i);
                        if (producto.getExistenciasproducto().equals("") || Integer.parseInt(producto.getExistenciasproducto().toString()) > Integer.parseInt((producto.getPa2Producto().toString().equals("") ? "0" : producto.getPa2Producto().toString()))) {
                            text = (EditText) itemView.findViewById(Integer.parseInt(producto.getId()));
                            text.setText(String.valueOf(Integer.parseInt(text.getText().toString()) + 1));

                            producto.setPa2Producto(String.valueOf(Integer.parseInt(text.getText().toString())));
                            ConexionSQLiteHelper conn = new ConexionSQLiteHelper(view.getContext(), "sistemaonline", null, 10);
                            SQLiteDatabase db = conn.getWritableDatabase();
                            String insert = "update " + Utilidades.tabla_productos + "  set Pa2producto='" + String.valueOf(Integer.parseInt(text.getText().toString())) + "' where id=" + producto.getId() + " ";
                            db.execSQL(insert);
                        } else {
                            Toast.makeText(view.getContext(), "No se realizo! revise existencias", Toast.LENGTH_SHORT).show();
                        }
                        //ProductosActivity.productopedidolist.add(tmpproducto);
                        //ProductosActivity.valorTotalPedido=ProductosActivity.valorTotalPedido+Integer.parseInt(tmpproducto.getPrecioventaproducto().toString());
                        //ProductosActivity.valorTotalPedido=ProductosActivity.valorTotalPedido+Integer.parseInt("1");
                        //ProductosActivity.productopedidolist=productos;
                        ProductosActivity.actualizarVaorPedido(view.getContext());

                        //Toast.makeText(view.getContext(),text.getId()+" - "+ib_sumarProducto.getId(),Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(view.getContext(), "Desactive modo edición", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ib_restarProducto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ProductosActivity.globalClass.getEnlinea() == null || ProductosActivity.globalClass.getEnlinea() == "false") {
                        EditText text;
                        int i = getAdapterPosition();
                        Producto producto = productos.get(i);
                        text=(EditText) itemView.findViewById(Integer.parseInt(producto.getId()));
                        if(Integer.parseInt(text.getText().toString())>0) {

                            text.setText(String.valueOf(Integer.parseInt(text.getText().toString()) - 1));
                            producto.setPa2Producto(String.valueOf(Integer.parseInt(text.getText().toString()) ));
                            ConexionSQLiteHelper conn=new ConexionSQLiteHelper(view.getContext(),"sistemaonline",null,10);
                            SQLiteDatabase db=conn.getWritableDatabase();
                            String insert="update "+ Utilidades.tabla_productos+"  set Pa2producto='"+String.valueOf(Integer.parseInt(text.getText().toString()) )+"' where id="+producto.getId()+" ";
                            db.execSQL(insert);
                        }
                        //ProductosActivity.productopedidolist=productos;
                        ProductosActivity.actualizarVaorPedido(view.getContext());
                        //Toast.makeText(view.getContext(),text.getText().toString(),Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(view.getContext(), "Desactive modo edición", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {

            int i = getAdapterPosition();
            Producto producto = this.productos.get(i);
            if (tipo_clic=='n') {

                if(ProductosActivity.globalClass.getEnlinea()==null||ProductosActivity.globalClass.getEnlinea()=="false") {
                }else{
                    Intent productoi = new Intent(this.ctx, ProductoformActivity.class);
                    productoi.putExtra("producto", producto);
                    this.ctx.startActivity(productoi);
                }
                /*Intent productoi = new Intent(this.ctx, ConsultarCilindroActivity.class);
                productoi.putExtra("producto", producto);
                this.ctx.startActivity(productoi);*/
            }else if(tipo_clic=='f'){
                /*Intent productoi = new Intent(this.ctx, FugaCapturaActivity.class);
                productoi.putExtra("producto", producto);
                this.ctx.startActivity(productoi);*/
            }
            //ctx.finish();
        }
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    @Override
    public ProductoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.producto_card, parent, false);
        return new ProductoAdapter.ViewHolder(v,ctx,productos);
    }

    @Override
    public void onBindViewHolder(ProductoAdapter.ViewHolder holder, int position) {
       holder.producto.setText(productos.get(position).getNombreproducto());
        holder.direccion.setText(productos.get(position).getCategoriaproducto() + " - " + productos.get(position).getMarcaproducto());
        holder.telefono.setText(productos.get(position).getPrecioventaproducto());
        /*if(!productos.get(position).getPa1Producto().toString().equals("")) {
            holder.imagen.setImageBitmap(BitmapFactory.decodeFile(productos.get(position).getPa1Producto()));

        }*/

/*      int targetImageViewWidth = productos.get(position).getImagen().getWidth();
        int targetImageViewHeight = productos.get(position).getImagen().getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(productos.get(position).getPa1Producto(), bmOptions);
        int cameraImageWidth = bmOptions.outWidth;
        int cameraImageHeight = bmOptions.outHeight;

        int scaleFactor = Math.min(cameraImageWidth/targetImageViewWidth, cameraImageHeight/targetImageViewHeight);
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inJustDecodeBounds = false;

        Bitmap photoReducedSizeBitmp = BitmapFactory.decodeFile(productos.get(position).getPa1Producto(), bmOptions);
*/
           holder.imagen.setImageBitmap(productos.get(position).getImagen());
        holder.imagen.setImageBitmap(productos.get(position).getImagen());
        
        holder.et_unidadesacomprar.setId(Integer.parseInt(productos.get(position).getId()));
        holder.et_unidadesacomprar.setText((productos.get(position).getPa2Producto()==null||productos.get(position).getPa2Producto().toString().equals("")?"0":productos.get(position).getPa2Producto()));
        holder.ib_sumarProducto.setId(Integer.parseInt(productos.get(position).getId())+1000);
        holder.ib_restarProducto.setId(Integer.parseInt(productos.get(position).getId())+1000);


    }

}

