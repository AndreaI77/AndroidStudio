package edu.andreaivanova.myfavouritespets.adapters

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBAdapter(context: Context, factory: SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper(context,DATABASE_NAME, factory, DATABASE_VERSION) {
    val TAG ="SQLite"

    //creo el companion object con los valores
    companion object{
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "MyFavouritesPets.db"
        val TABLA_CLASE = "clase"
        val COLUMNA_ID =  "id"
        val COLUMNA_NOMBRE = "nombre"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        try{
            val crearTablaClase = "CREATE TABLE $TABLA_CLASE(" +
                    "$COLUMNA_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMNA_NOMBRE TEXT)"
            db!!.execSQL(crearTablaClase)
            val crearTablaPelaje = "CREATE TABLE pelaje(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT)"
            db!!.execSQL(crearTablaPelaje)
            val crearTablaPet = "CREATE TABLE pet(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT," +
                    "latNombre TEXT," +
                    "enlace TEXT," +
                    "id_clase INTEGER," +
                    "id_pelaje INTEGER," +
                    "image TEXT)"
            db!!.execSQL(crearTablaPet)

        }catch(e: SQLiteException){
            Log.e("$TAG (onCreate", e.message.toString())
        }
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}