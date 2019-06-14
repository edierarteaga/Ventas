package com.example.administrador.gestionusuarios.utilidades;

import android.app.Application;

public class GlobalClass extends Application {
    private String enlinea;

    public String getEnlinea() {
        return enlinea;
    }

    public void setEnlinea(String enlinea) {
        this.enlinea = enlinea;
    }
}

