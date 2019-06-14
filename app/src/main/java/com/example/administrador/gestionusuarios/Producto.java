package com.example.administrador.gestionusuarios;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by EDIER on 01/09/2017.
 */

class Producto implements Parcelable{
    private String id, nombreproducto,descripcionproducto,marcaproducto,categoriaproducto,referenciaproducto,preciocompraproducto,precioventaproducto,existenciasproducto,minimoexistenciasproducto,maximoexistenciasproducto,proveedorproducto,pa1producto,pa2producto,user_id,groupuser_id,todos,excepgroupuser_id,sistemacategoria_id,sistema_id;
    private Bitmap imagen;

    public Producto(String id,String nombreproducto,String descripcionproducto,String marcaproducto,String categoriaproducto,String referenciaproducto,String preciocompraproducto,String precioventaproducto,String existenciasproducto,String minimoexistenciasproducto,String maximoexistenciasproducto,String proveedorproducto,String pa1producto,String pa2producto,String user_id,String groupuser_id,String todos,String excepgroupuser_id,String sistemacategoria_id,String sistema_id) {
        this.id=id;
        this.nombreproducto = nombreproducto;
        this.descripcionproducto = descripcionproducto;
        this.marcaproducto = marcaproducto;
        this.categoriaproducto = categoriaproducto;
        this.referenciaproducto = referenciaproducto;
        this.preciocompraproducto = preciocompraproducto;
        this.precioventaproducto = precioventaproducto;
        this.existenciasproducto = existenciasproducto;
        this.minimoexistenciasproducto = minimoexistenciasproducto;
        this.maximoexistenciasproducto = maximoexistenciasproducto;
        this.proveedorproducto = proveedorproducto;
        this.pa1producto = pa1producto;
        this.pa2producto = pa2producto;
        this.user_id = user_id;
        this.groupuser_id = groupuser_id;
        this.todos = todos;
        this.excepgroupuser_id = excepgroupuser_id;
        this.sistemacategoria_id = sistemacategoria_id;
        this.sistema_id = sistema_id;


    }

    public Bitmap getImagen(){
        Bitmap bitmap;

if(pa1producto==null||pa1producto.equals("")){
     bitmap=BitmapFactory.decodeFile("/storage/emulated/0/misImagenesPrueba/misFotos/ic_produto.png");
}else{
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inSampleSize = 8;
     bitmap=BitmapFactory.decodeFile(pa1producto,options);
}



        return bitmap;
    }
    public void setImagen(Bitmap imagen){
        this.imagen=imagen;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getNombreproducto() {
        return nombreproducto;
    }

    public void setNombreproducto(String nombreproducto) {
        this.nombreproducto = nombreproducto;
    }
    public String getDescripcionproducto() {
        return descripcionproducto;
    }

    public void setDescripcionproducto(String descripcionproducto) {
        this.descripcionproducto = descripcionproducto;
    }
    public String getMarcaproducto() {
        return marcaproducto;
    }

    public void setMarcaproducto(String marcaproducto) {
        this.marcaproducto = marcaproducto;
    }
    public String getCategoriaproducto() {
        return categoriaproducto;
    }

    public void setCategoriaproducto(String categoriaproducto) {
        this.categoriaproducto = categoriaproducto;
    }
    public String getReferenciaproducto() {
        return referenciaproducto;
    }

    public void setReferenciaproducto(String referenciaproducto) {
        this.referenciaproducto = referenciaproducto;
    }
    public String getPreciocompraproducto() {
        return preciocompraproducto;
    }

    public void setPreciocompraproducto(String preciocompraproducto) {
        this.preciocompraproducto = preciocompraproducto;
    }
    public String getPrecioventaproducto() {
        return precioventaproducto;
    }

    public void setPrecioventaproducto(String precioventaproducto) {
        this.precioventaproducto = precioventaproducto;
    }
    public String getExistenciasproducto() {
        return existenciasproducto;
    }

    public void setExistenciasproducto(String existenciasproducto) {
        this.existenciasproducto = existenciasproducto;
    }
    public String getMinimoexistenciasproducto() {
        return minimoexistenciasproducto;
    }

    public void setMinimoexistenciasproducto(String minimoexistenciasproducto) {
        this.minimoexistenciasproducto = minimoexistenciasproducto;
    }
    public String getMaximoexistenciasproducto() {
        return maximoexistenciasproducto;
    }

    public void setMaximoexistenciasproducto(String maximoexistenciasproducto) {
        this.maximoexistenciasproducto = maximoexistenciasproducto;
    }
    public String getProveedorproducto() {
        return proveedorproducto;
    }

    public void setProveedorproducto(String proveedorproducto) {
        this.proveedorproducto = proveedorproducto;
    }
    public String getPa1Producto() {
        return pa1producto;
    }

    public void setPa1Producto(String pa1producto) {
        this.pa1producto = pa1producto;
    }
    public String getPa2Producto() {
        return pa2producto;
    }

    public void setPa2Producto(String pa2producto) {
        this.pa2producto = pa2producto;
    }
    public String getUser_Id() {
        return user_id;
    }

    public void setUser_Id(String user_id) {
        this.user_id = user_id;
    }
    public String getGroupuser_Id() {
        return groupuser_id;
    }

    public void setGroupuser_Id(String groupuser_id) {
        this.groupuser_id = groupuser_id;
    }
    public String getTodos() {
        return todos;
    }

    public void setTodos(String todos) {
        this.todos = todos;
    }
    public String getExcepgroupuser_Id() {
        return excepgroupuser_id;
    }

    public void setExcepgroupuser_Id(String excepgroupuser_id) {
        this.excepgroupuser_id = excepgroupuser_id;
    }
    public String getSistemacategoria_Id() {
        return sistemacategoria_id;
    }

    public void setSistemacategoria_Id(String sistemacategoria_id) {
        this.sistemacategoria_id = sistemacategoria_id;
    }
    public String getSistema_Id() {
        return sistema_id;
    }

    public void setSistema_Id(String sistema_id) {
        this.sistema_id = sistema_id;
    }




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.nombreproducto);
        dest.writeString(this.descripcionproducto);
        dest.writeString(this.marcaproducto);
        dest.writeString(this.categoriaproducto);
        dest.writeString(this.referenciaproducto);
        dest.writeString(this.preciocompraproducto);
        dest.writeString(this.precioventaproducto);
        dest.writeString(this.existenciasproducto);
        dest.writeString(this.minimoexistenciasproducto);
        dest.writeString(this.maximoexistenciasproducto);
        dest.writeString(this.proveedorproducto);
        dest.writeString(this.pa1producto);
        dest.writeString(this.pa2producto);
        dest.writeString(this.user_id);
        dest.writeString(this.groupuser_id);
        dest.writeString(this.todos);
        dest.writeString(this.excepgroupuser_id);
        dest.writeString(this.sistemacategoria_id);
        dest.writeString(this.sistema_id);
    }

    protected Producto(Parcel in) {
        this.id = in.readString();
        this.nombreproducto = in.readString();
        this.descripcionproducto = in.readString();
        this.marcaproducto = in.readString();
        this.categoriaproducto = in.readString();
        this.referenciaproducto = in.readString();
        this.preciocompraproducto = in.readString();
        this.precioventaproducto = in.readString();
        this.existenciasproducto = in.readString();
        this.minimoexistenciasproducto = in.readString();
        this.maximoexistenciasproducto = in.readString();
        this.proveedorproducto = in.readString();
        this.pa1producto = in.readString();
        this.pa2producto = in.readString();
        this.user_id = in.readString();
        this.groupuser_id = in.readString();
        this.todos = in.readString();
        this.excepgroupuser_id = in.readString();
        this.sistemacategoria_id = in.readString();
        this.sistema_id = in.readString();
    }

    public static final Creator<Producto> CREATOR = new Creator<Producto>() {
        @Override
        public Producto createFromParcel(Parcel source) {
            return new Producto(source);
        }

        @Override
        public Producto[] newArray(int size) {
            return new Producto[size];
        }
    };

}

