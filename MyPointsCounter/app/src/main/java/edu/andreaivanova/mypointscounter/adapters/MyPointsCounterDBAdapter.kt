package edu.andreaivanova.mypointscounter.adapters

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import edu.andreaivanova.mypointscounter.model.MyPoints

class MyPointsCounterDBAdapter(context: Context, factory: SQLiteDatabase.CursorFactory?):
SQLiteOpenHelper(context,DATABASE_NAME, factory, DATABASE_VERSION){
    val TAG ="SQLite"

    //creo el companion object con los valores
    companion object{
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "MyPointsCounter.db"
        val TABLA_POINTS = "points"
        val COLUMNA_ID =  "id"
        val COLUMNA_POINTS = "points"
        val COLUMNA_DATE = "date"
        val COLUMNA_HOUR = "hour"

    }
    //creo la base de datos y la tabla con sus columnas
    override fun onCreate(db:SQLiteDatabase?){
        try{
            val crearTablaPoints = "CREATE TABLE $TABLA_POINTS (" +
                    "$COLUMNA_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMNA_POINTS INTEGER, " +
                    "$COLUMNA_DATE TEXT, " +
                    "$COLUMNA_HOUR TEXT)"
            db!!.execSQL(crearTablaPoints)
        }catch(e: SQLiteException){
            Log.e("$TAG (onCreate", e.message.toString())
        }
    }
    // implemento la función obligatoria de la interface
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        try {
            val dropTablaPoints = "DROP TABLE IF EXISTS $TABLA_POINTS"
            db!!.execSQL(dropTablaPoints)
            onCreate(db)
        } catch (e: SQLiteException) {
            Log.e("$TAG (onUpgrade)", e.message.toString())
            }
    }
    // método para añadir nuevos puntos (sin id)
    fun addPoints(points: MyPoints){
        val data = ContentValues()
        data.put(COLUMNA_POINTS,points.points)
        data.put(COLUMNA_DATE, points.date)
        data.put(COLUMNA_HOUR,points.hour)
        val db = this.writableDatabase
        db.insert(TABLA_POINTS, null, data)
        db.close()
    }
    // método para volver a añadir los puntos borrados, con su id original
    fun reAddPoints(points: MyPoints){
        val data = ContentValues()
        data.put(COLUMNA_ID,points.id)
        data.put(COLUMNA_POINTS,points.points)
        data.put(COLUMNA_DATE, points.date)
        data.put(COLUMNA_HOUR,points.hour)
        val db = this.writableDatabase
        db.insert(TABLA_POINTS, null, data)
        db.close()
    }

    //método para borrar los registros según el id
    fun delPoints(id:Int):Int{
        val args = arrayOf(id.toString())
        val db = this.writableDatabase
        val result = db.delete(TABLA_POINTS, "$COLUMNA_ID =?",args)
        db.close()
        return result
    }

    //método que devuelve el listado con todos los registros.
    fun allPoints(): MutableList<MyPoints>{
        var id =0
        var points=0
        var fecha=""
        var hora=""
        var myPoint :MyPoints? = null
        val lista:MutableList<MyPoints> = ArrayList()
        val db = this.readableDatabase
        var cursor: Cursor = db.rawQuery("SELECT * FROM $TABLA_POINTS ORDER BY $COLUMNA_POINTS;",null)
        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(0)
                points = cursor.getInt(1)
                fecha = cursor.getString(2)
                hora= cursor.getString(3)
                myPoint = MyPoints(id,points,fecha,hora)
                lista.add(myPoint)
            }while(cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return lista
    }

}