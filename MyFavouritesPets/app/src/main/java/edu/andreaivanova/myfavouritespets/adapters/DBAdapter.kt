package edu.andreaivanova.myfavouritespets.adapters

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import edu.andreaivanova.myfavouritespets.model.Clase
import edu.andreaivanova.myfavouritespets.model.Pelaje
import edu.andreaivanova.myfavouritespets.model.Pet

class DBAdapter(context: Context, factory: SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper(context,DATABASE_NAME, factory, DATABASE_VERSION) {
    val TAG ="SQLite"

    //creo el companion object con los valores
    companion object{
        val DATABASE_VERSION = 3
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
            val crearTablaPets = "CREATE TABLE pets(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT," +
                    "latNombre TEXT," +
                    "enlace TEXT," +
                    "id_clase INTEGER," +
                    "id_pelaje INTEGER," +
                    "rating INTEGER," +
                    "favorite INTEGER," +
                    "constraint fk_id_clase foreign key (id_clase) references $TABLA_CLASE (id)," +
                    "constraint fk_id_pelaje foreign key (id_pelaje) references pelaje (id))"
            db!!.execSQL(crearTablaPets)

        }catch(e: SQLiteException){
            Log.e("$TAG (onCreate", e.message.toString())
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        try {
            val dropTablaPets = "DROP TABLE IF EXISTS pets"
            db!!.execSQL(dropTablaPets)
            val dropTablaClase = "DROP TABLE IF EXISTS $TABLA_CLASE"
            db!!.execSQL(dropTablaClase)
            val dropTablaPelaje = "DROP TABLE IF EXISTS pelaje"
            db!!.execSQL(dropTablaPelaje)
            onCreate(db)
        } catch (e: SQLiteException) {
            Log.e("$TAG (onUpgrade)", e.message.toString())
        }
    }
    //método que devuelve el listado con todos los registros.
    fun allPets(): MutableList<Pet>{
        var id: Int
        var name: String
        var latName:String
        var id_clase:Int
        var id_pelo:Int
        var image:String
        var rating:Int
        var fav:Int
        var pet : Pet
        val lista:MutableList<Pet> = ArrayList()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM pets;",null)
        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(0)
                name = cursor.getString(1)
                latName = cursor.getString(2)
                image= cursor.getString(3)
                id_clase = cursor.getInt(4)
                id_pelo = cursor.getInt(5)
                rating = cursor.getInt(6)
                fav=cursor.getInt(7)
                pet = Pet (id,name,latName,image,getClase(id_clase),getPelaje(id_pelo),rating,fav)
                lista.add(pet)
            }while(cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }
    fun getClase(num:Int):Clase{
        var id =0
        var nombre=""
        val db = this.readableDatabase
        var cursor: Cursor = db.rawQuery("SELECT * FROM clase where id="+num+";",null)
        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(0)
                nombre = cursor.getString(1)
            }while(cursor.moveToNext())
        }
        var clase=Clase(id,nombre)
        cursor.close()
        db.close()
        return clase
    }
    //método que devuelve el listado con todos los registros.
    fun allClasses(): MutableList<Clase>{
        var id: Int
        var name: String
        var clase : Clase
        val lista:MutableList<Clase> = ArrayList()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM clase;",null)
        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(0)
                name = cursor.getString(1)
                clase = Clase(id,name)
                lista.add(clase)
            }while(cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }
    fun getPelaje(num:Int):Pelaje{
        var id =0
        var nombre=""
        val db = this.readableDatabase
        var cursor: Cursor = db.rawQuery("SELECT * FROM pelaje where id="+num+";",null)
        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(0)
                nombre = cursor.getString(1)
            }while(cursor.moveToNext())
        }
        var pelo=Pelaje(id,nombre)
        cursor.close()
        db.close()
        return pelo
    }
    fun obtenerPelajes(): MutableList<Pelaje>{
        var id: Int
        var name: String
        var pelaje : Pelaje
        val lista:MutableList<Pelaje> = ArrayList()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM pelaje;",null)
        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(0)
                name = cursor.getString(1)
                pelaje = Pelaje(id,name)
                lista.add(pelaje)
            }while(cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }
}