package com.example.administrador.gestionusuarios;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.administrador.gestionusuarios.utilidades.Utilidades;


public class ConexionSQLiteHelper extends SQLiteOpenHelper {

    public ConexionSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Utilidades.CREAR_TABLA_RECIBO);
        db.execSQL(Utilidades.CREAR_TABLA_PRODUCTO);
        db.execSQL(Utilidades.CREAR_TABLA_RECIBOSPRODUCTO);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {

        db.execSQL("DROP TABLE IF EXISTs productos");
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTs recibos");
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTs recibosproducto");
        onCreate(db);
    }
}


