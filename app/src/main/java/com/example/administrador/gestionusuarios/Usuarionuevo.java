package com.example.administrador.gestionusuarios;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by EDIER on 01/09/2017.
 */

class  Usuarionuevo implements Parcelable{
    private String id,codigousuarionuevo,nombresusuarionuevo,apellidosusuarionuevo,cedulausuarionuevo,telefono1usuarionuevo,telefono2usuarionuevo,fechaafiliacionusuarionuevo,direccionusuarionuevo,barriousuarionuevo,valormensualusuarionuevo,responsableusuarionuevo,pa1usuarionuevo,pa2usuarionuevo,user_id,groupuser_id,todos,excepgroupuser_id,sistemacategoria_id,sistema_id;

    public Usuarionuevo(String id,String codigousuarionuevo,String nombresusuarionuevo,String apellidosusuarionuevo,String cedulausuarionuevo,String telefono1usuarionuevo,String telefono2usuarionuevo,String fechaafiliacionusuarionuevo,String direccionusuarionuevo,String barriousuarionuevo,String valormensualusuarionuevo,String responsableusuarionuevo,String pa1usuarionuevo,String pa2usuarionuevo,String user_id,String groupuser_id,String todos,String excepgroupuser_id,String sistemacategoria_id,String sistema_id) {
        this.id=id;
        this.codigousuarionuevo = codigousuarionuevo;
        this.nombresusuarionuevo = nombresusuarionuevo;
        this.apellidosusuarionuevo = apellidosusuarionuevo;
        this.cedulausuarionuevo = cedulausuarionuevo;
        this.telefono1usuarionuevo = telefono1usuarionuevo;
        this.telefono2usuarionuevo = telefono2usuarionuevo;
        this.fechaafiliacionusuarionuevo = fechaafiliacionusuarionuevo;
        this.direccionusuarionuevo = direccionusuarionuevo;
        this.barriousuarionuevo = barriousuarionuevo;
        this.valormensualusuarionuevo = valormensualusuarionuevo;
        this.responsableusuarionuevo = responsableusuarionuevo;
        this.pa1usuarionuevo = pa1usuarionuevo;
        this.pa2usuarionuevo = pa2usuarionuevo;
        this.user_id = user_id;
        this.groupuser_id = groupuser_id;
        this.todos = todos;
        this.excepgroupuser_id = excepgroupuser_id;
        this.sistemacategoria_id = sistemacategoria_id;
        this.sistema_id = sistema_id;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getCodigousuarionuevo() {
        return codigousuarionuevo;
    }

    public void setCodigousuarionuevo(String codigousuarionuevo) {
        this.codigousuarionuevo = codigousuarionuevo;
    }
    public String getNombresusuarionuevo() {
        return nombresusuarionuevo;
    }

    public void setNombresusuarionuevo(String nombresusuarionuevo) {
        this.nombresusuarionuevo = nombresusuarionuevo;
    }
    public String getApellidosusuarionuevo() {
        return apellidosusuarionuevo;
    }

    public void setApellidosusuarionuevo(String apellidosusuarionuevo) {
        this.apellidosusuarionuevo = apellidosusuarionuevo;
    }
    public String getCedulausuarionuevo() {
        return cedulausuarionuevo;
    }

    public void setCedulausuarionuevo(String cedulausuarionuevo) {
        this.cedulausuarionuevo = cedulausuarionuevo;
    }
    public String getTelefono1Usuarionuevo() {
        return telefono1usuarionuevo;
    }

    public void setTelefono1Usuarionuevo(String telefono1usuarionuevo) {
        this.telefono1usuarionuevo = telefono1usuarionuevo;
    }
    public String getTelefono2Usuarionuevo() {
        return telefono2usuarionuevo;
    }

    public void setTelefono2Usuarionuevo(String telefono2usuarionuevo) {
        this.telefono2usuarionuevo = telefono2usuarionuevo;
    }
    public String getFechaafiliacionusuarionuevo() {
        return fechaafiliacionusuarionuevo;
    }

    public void setFechaafiliacionusuarionuevo(String fechaafiliacionusuarionuevo) {
        this.fechaafiliacionusuarionuevo = fechaafiliacionusuarionuevo;
    }
    public String getDireccionusuarionuevo() {
        return direccionusuarionuevo;
    }

    public void setDireccionusuarionuevo(String direccionusuarionuevo) {
        this.direccionusuarionuevo = direccionusuarionuevo;
    }
    public String getBarriousuarionuevo() {
        return barriousuarionuevo;
    }

    public void setBarriousuarionuevo(String barriousuarionuevo) {
        this.barriousuarionuevo = barriousuarionuevo;
    }
    public String getValormensualusuarionuevo() {
        return valormensualusuarionuevo;
    }

    public void setValormensualusuarionuevo(String valormensualusuarionuevo) {
        this.valormensualusuarionuevo = valormensualusuarionuevo;
    }
    public String getResponsableusuarionuevo() {
        return responsableusuarionuevo;
    }

    public void setResponsableusuarionuevo(String responsableusuarionuevo) {
        this.responsableusuarionuevo = responsableusuarionuevo;
    }
    public String getPa1Usuarionuevo() {
        return pa1usuarionuevo;
    }

    public void setPa1Usuarionuevo(String pa1usuarionuevo) {
        this.pa1usuarionuevo = pa1usuarionuevo;
    }
    public String getPa2Usuarionuevo() {
        return pa2usuarionuevo;
    }

    public void setPa2Usuarionuevo(String pa2usuarionuevo) {
        this.pa2usuarionuevo = pa2usuarionuevo;
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
        dest.writeString(this.codigousuarionuevo);
        dest.writeString(this.nombresusuarionuevo);
        dest.writeString(this.apellidosusuarionuevo);
        dest.writeString(this.cedulausuarionuevo);
        dest.writeString(this.telefono1usuarionuevo);
        dest.writeString(this.telefono2usuarionuevo);
        dest.writeString(this.fechaafiliacionusuarionuevo);
        dest.writeString(this.direccionusuarionuevo);
        dest.writeString(this.barriousuarionuevo);
        dest.writeString(this.valormensualusuarionuevo);
        dest.writeString(this.responsableusuarionuevo);
        dest.writeString(this.pa1usuarionuevo);
        dest.writeString(this.pa2usuarionuevo);
        dest.writeString(this.user_id);
        dest.writeString(this.groupuser_id);
        dest.writeString(this.todos);
        dest.writeString(this.excepgroupuser_id);
        dest.writeString(this.sistemacategoria_id);
        dest.writeString(this.sistema_id);
    }

    protected Usuarionuevo(Parcel in) {
        this.id = in.readString();
        this.codigousuarionuevo = in.readString();
        this.nombresusuarionuevo = in.readString();
        this.apellidosusuarionuevo = in.readString();
        this.cedulausuarionuevo = in.readString();
        this.telefono1usuarionuevo = in.readString();
        this.telefono2usuarionuevo = in.readString();
        this.fechaafiliacionusuarionuevo = in.readString();
        this.direccionusuarionuevo = in.readString();
        this.barriousuarionuevo = in.readString();
        this.valormensualusuarionuevo = in.readString();
        this.responsableusuarionuevo = in.readString();
        this.pa1usuarionuevo = in.readString();
        this.pa2usuarionuevo = in.readString();
        this.user_id = in.readString();
        this.groupuser_id = in.readString();
        this.todos = in.readString();
        this.excepgroupuser_id = in.readString();
        this.sistemacategoria_id = in.readString();
        this.sistema_id = in.readString();
    }

    public static final Creator<Usuarionuevo> CREATOR = new Creator<Usuarionuevo>() {
        @Override
        public Usuarionuevo createFromParcel(Parcel source) {
            return new Usuarionuevo(source);
        }

        @Override
        public Usuarionuevo[] newArray(int size) {
            return new Usuarionuevo[size];
        }
    };
}

