package com.example.encriptador_de_texto_eas

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelpedAdmin(context: Context, name:String, factory: CursorFactory?, version: Int): SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        if (db != null) {
            db.execSQL("create table Usuarios(codigo int primary key, contraseña text)")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}