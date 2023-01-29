package edu.andreaivanova.myfavouritepets.adapters

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import edu.andreaivanova.myfavouritepets.model.Clase
import edu.andreaivanova.myfavouritepets.model.Pelaje
import edu.andreaivanova.myfavouritepets.model.Pet

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
            val crearTablaPets = "CREATE TABLE pets(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT," +
                    "latNombre TEXT," +
                    "enlace TEXT," +
                    "id_clase INTEGER," +
                    "id_pelaje INTEGER," +
                    "rating REAL," +
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
        var idClase:Int
        var idPelo:Int
        var image:String
        var rating:Float
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
                idClase = cursor.getInt(4)
                idPelo = cursor.getInt(5)
                rating = cursor.getFloat(6)
                fav=cursor.getInt(7)
                pet = Pet (id,name,latName,image,getClase(idClase),getPelaje(idPelo),rating,fav)
                lista.add(pet)
            }while(cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }
    fun favPets(): MutableList<Pet>{
        var id: Int
        var name: String
        var latName:String
        var idClase:Int
        var idPelo:Int
        var image:String
        var rating:Float
        var fav:Int
        var pet : Pet
        val lista:MutableList<Pet> = ArrayList()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM pets where favorite = 1 ;",null)
        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(0)
                name = cursor.getString(1)
                latName = cursor.getString(2)
                image= cursor.getString(3)
                idClase = cursor.getInt(4)
                idPelo = cursor.getInt(5)
                rating = cursor.getFloat(6)
                fav=cursor.getInt(7)
                pet = Pet (id,name,latName,image,getClase(idClase),getPelaje(idPelo),rating,fav)
                lista.add(pet)
            }while(cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }
    fun addPet(pet:Pet): Boolean{
        val data = ContentValues()
//        val lista : MutableList<Int> = ArrayList()
//        val db = this.readableDatabase
//        val cursor: Cursor = db.rawQuery("SELECT id FROM pets;",null)
//        if(cursor.moveToFirst()){
//            do{
//                lista.add( cursor.getInt(0))
//            }while(cursor.moveToNext())
//        }
//        cursor.close()
//        db.close()
//        if(!lista.contains(pet.id)){
//            data.put("id", pet.id)
//        }
        data.put("nombre", pet.nombre)
        data.put("latNombre", pet.latName)
        data.put("enlace", pet.image)
        data.put("id_clase", pet.clase.id)
        data.put("id_pelaje", pet.pelo.id)
        data.put("rating", pet.rating)
        data.put("favorite", pet.favorite)
        val db1 = this.writableDatabase
        db1.insert("pets", null, data)
        db1.close()
        return true
    }
    fun updatePet(id:Int, valores: MutableMap<String, String>){
        val args = arrayOf(id.toString())
        val data = ContentValues()
        for(item in valores){
            if(item.key.equals("id_clase") || item.key=="id_pelaje" || item.key == "favorite"){
                data.put(item.key, item.value.toInt())
            }else if ( item.key=="rating"){
                data.put(item.key,item.value.toFloat())
            }else{
                data.put( item.key, item.value)
            }

        }
        val db = this.writableDatabase
        db.update("pets", data, "id=?", args)
        db.close()
    }

    fun deletePet(id:Int):Int{
        val args = arrayOf(id.toString())
        val db = this.writableDatabase
        val result = db.delete("pets", "id =?",args)
        db.close()
        return result
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
    //método que inserta clase
    fun addClase(clase:Clase){
        val data = ContentValues()
        data.put("nombre", clase.nombre)
        val db = this.writableDatabase
        db.insert("clase", null, data)
        db.close()
    }
    fun delClase(id:Int):Int{
        val args = arrayOf(id.toString())
        val db = this.writableDatabase
        val result = db.delete("clase", "id =?",args)
        db.close()
        return result
    }

    //método que devuelve el listado con todos los registros.
    fun allClasses(): MutableList<Clase>{
        var id: Int
        var name: String
        var clase : Clase
        val lista:MutableList<Clase> = ArrayList()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM clase ORDER BY id ASC;",null)
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
    //método que inserta clase
    fun addPelaje(pelaje:Pelaje){
        val data = ContentValues()
        data.put("nombre", pelaje.nombre)
        val db = this.writableDatabase
        db.insert("pelaje", null, data)
        db.close()
    }
    fun delPelaje(id:Int):Int{
        val args = arrayOf(id.toString())
        val db = this.writableDatabase
        val result = db.delete("pelaje", "id =?",args)
        db.close()
        return result
    }
    fun obtenerPelajes(): MutableList<Pelaje>{
        var id: Int
        var name: String
        var pelaje : Pelaje
        val lista:MutableList<Pelaje> = ArrayList()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM pelaje ORDER BY id ASC;",null)
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