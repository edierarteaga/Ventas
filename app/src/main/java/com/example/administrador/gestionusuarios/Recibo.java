package com.example.administrador.gestionusuarios;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by EDIER on 01/09/2017.
 */

class Recibo implements Parcelable{
    private String id, usuarioidrecibo,servicioidrecibo,productoidrecibo,aniorecibo,mesrecibo,valorrecibo,valornetorecibo,pagoanticipadorecibo,estadorecibo,fechalimiterecibo,descuentorecibo,formapagorecibo,fechageneradorecibo,horarecibo,cantidadrecibo,fechapagorecibo,ivarecibo,pa1recibo,pa2recibo,user_id,groupuser_id,todos,excepgroupuser_id,sistemacategoria_id,sistema_id;

    public Recibo(String id,String usuarioidrecibo,String servicioidrecibo,String productoidrecibo,String aniorecibo,String mesrecibo,String valorrecibo,String valornetorecibo,String pagoanticipadorecibo,String estadorecibo,String fechalimiterecibo,String descuentorecibo,String formapagorecibo,String fechageneradorecibo,String horarecibo,String cantidadrecibo,String fechapagorecibo,String ivarecibo,String pa1recibo,String pa2recibo,String user_id,String groupuser_id,String todos,String excepgroupuser_id,String sistemacategoria_id,String sistema_id) {
        this.id=id;
        this.usuarioidrecibo = usuarioidrecibo;
        this.servicioidrecibo = servicioidrecibo;
        this.productoidrecibo = productoidrecibo;
        this.aniorecibo = aniorecibo;
        this.mesrecibo = mesrecibo;
        this.valorrecibo = valorrecibo;
        this.valornetorecibo = valornetorecibo;
        this.pagoanticipadorecibo = pagoanticipadorecibo;
        this.estadorecibo = estadorecibo;
        this.fechalimiterecibo = fechalimiterecibo;
        this.descuentorecibo = descuentorecibo;
        this.formapagorecibo = formapagorecibo;
        this.fechageneradorecibo = fechageneradorecibo;
        this.horarecibo = horarecibo;
        this.cantidadrecibo = cantidadrecibo;
        this.fechapagorecibo = fechapagorecibo;
        this.ivarecibo = ivarecibo;
        this.pa1recibo = pa1recibo;
        this.pa2recibo = pa2recibo;
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
    public String getUsuarioidrecibo() {
        return usuarioidrecibo;
    }

    public void setUsuarioidrecibo(String usuarioidrecibo) {
        this.usuarioidrecibo = usuarioidrecibo;
    }
    public String getServicioidrecibo() {
        return servicioidrecibo;
    }

    public void setServicioidrecibo(String servicioidrecibo) {
        this.servicioidrecibo = servicioidrecibo;
    }
    public String getProductoidrecibo() {
        return productoidrecibo;
    }

    public void setProductoidrecibo(String productoidrecibo) {
        this.productoidrecibo = productoidrecibo;
    }
    public String getAniorecibo() {
        return aniorecibo;
    }

    public void setAniorecibo(String aniorecibo) {
        this.aniorecibo = aniorecibo;
    }
    public String getMesrecibo() {
        return mesrecibo;
    }

    public void setMesrecibo(String mesrecibo) {
        this.mesrecibo = mesrecibo;
    }
    public String getValorrecibo() {
        return valorrecibo;
    }

    public void setValorrecibo(String valorrecibo) {
        this.valorrecibo = valorrecibo;
    }
    public String getValornetorecibo() {
        return valornetorecibo;
    }

    public void setValornetorecibo(String valornetorecibo) {
        this.valornetorecibo = valornetorecibo;
    }
    public String getPagoanticipadorecibo() {
        return pagoanticipadorecibo;
    }

    public void setPagoanticipadorecibo(String pagoanticipadorecibo) {
        this.pagoanticipadorecibo = pagoanticipadorecibo;
    }
    public String getEstadorecibo() {
        return estadorecibo;
    }

    public void setEstadorecibo(String estadorecibo) {
        this.estadorecibo = estadorecibo;
    }
    public String getFechalimiterecibo() {
        return fechalimiterecibo;
    }

    public void setFechalimiterecibo(String fechalimiterecibo) {
        this.fechalimiterecibo = fechalimiterecibo;
    }
    public String getDescuentorecibo() {
        return descuentorecibo;
    }

    public void setDescuentorecibo(String descuentorecibo) {
        this.descuentorecibo = descuentorecibo;
    }
    public String getFormapagorecibo() {
        return formapagorecibo;
    }

    public void setFormapagorecibo(String formapagorecibo) {
        this.formapagorecibo = formapagorecibo;
    }
    public String getFechageneradorecibo() {
        return fechageneradorecibo;
    }

    public void setFechageneradorecibo(String fechageneradorecibo) {
        this.fechageneradorecibo = fechageneradorecibo;
    }
    public String getHorarecibo() {
        return horarecibo;
    }

    public void setHorarecibo(String horarecibo) {
        this.horarecibo = horarecibo;
    }
    public String getCantidadrecibo() {
        return cantidadrecibo;
    }

    public void setCantidadrecibo(String cantidadrecibo) {
        this.cantidadrecibo = cantidadrecibo;
    }
    public String getFechapagorecibo() {
        return fechapagorecibo;
    }

    public void setFechapagorecibo(String fechapagorecibo) {
        this.fechapagorecibo = fechapagorecibo;
    }
    public String getIvarecibo() {
        return ivarecibo;
    }

    public void setIvarecibo(String ivarecibo) {
        this.ivarecibo = ivarecibo;
    }
    public String getPa1Recibo() {
        return pa1recibo;
    }

    public void setPa1Recibo(String pa1recibo) {
        this.pa1recibo = pa1recibo;
    }
    public String getPa2Recibo() {
        return pa2recibo;
    }

    public void setPa2Recibo(String pa2recibo) {
        this.pa2recibo = pa2recibo;
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
        dest.writeString(this.usuarioidrecibo);
        dest.writeString(this.servicioidrecibo);
        dest.writeString(this.productoidrecibo);
        dest.writeString(this.aniorecibo);
        dest.writeString(this.mesrecibo);
        dest.writeString(this.valorrecibo);
        dest.writeString(this.valornetorecibo);
        dest.writeString(this.pagoanticipadorecibo);
        dest.writeString(this.estadorecibo);
        dest.writeString(this.fechalimiterecibo);
        dest.writeString(this.descuentorecibo);
        dest.writeString(this.formapagorecibo);
        dest.writeString(this.fechageneradorecibo);
        dest.writeString(this.horarecibo);
        dest.writeString(this.cantidadrecibo);
        dest.writeString(this.fechapagorecibo);
        dest.writeString(this.ivarecibo);
        dest.writeString(this.pa1recibo);
        dest.writeString(this.pa2recibo);
        dest.writeString(this.user_id);
        dest.writeString(this.groupuser_id);
        dest.writeString(this.todos);
        dest.writeString(this.excepgroupuser_id);
        dest.writeString(this.sistemacategoria_id);
        dest.writeString(this.sistema_id);
    }

    protected Recibo(Parcel in) {
        this.id = in.readString();
        this.usuarioidrecibo = in.readString();
        this.servicioidrecibo = in.readString();
        this.productoidrecibo = in.readString();
        this.aniorecibo = in.readString();
        this.mesrecibo = in.readString();
        this.valorrecibo = in.readString();
        this.valornetorecibo = in.readString();
        this.pagoanticipadorecibo = in.readString();
        this.estadorecibo = in.readString();
        this.fechalimiterecibo = in.readString();
        this.descuentorecibo = in.readString();
        this.formapagorecibo = in.readString();
        this.fechageneradorecibo = in.readString();
        this.horarecibo = in.readString();
        this.cantidadrecibo = in.readString();
        this.fechapagorecibo = in.readString();
        this.ivarecibo = in.readString();
        this.pa1recibo = in.readString();
        this.pa2recibo = in.readString();
        this.user_id = in.readString();
        this.groupuser_id = in.readString();
        this.todos = in.readString();
        this.excepgroupuser_id = in.readString();
        this.sistemacategoria_id = in.readString();
        this.sistema_id = in.readString();
    }

    public static final Creator<Recibo> CREATOR = new Creator<Recibo>() {
        @Override
        public Recibo createFromParcel(Parcel source) {
            return new Recibo(source);
        }

        @Override
        public Recibo[] newArray(int size) {
            return new Recibo[size];
        }
    };
}

