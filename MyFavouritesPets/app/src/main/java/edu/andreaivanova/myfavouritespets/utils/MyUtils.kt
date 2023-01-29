package edu.andreaivanova.myfavouritespets.utils


import android.content.Context
import android.os.Environment
import edu.andreaivanova.myfavouritespets.adapters.DBAdapter
import edu.andreaivanova.myfavouritespets.model.Clase
import edu.andreaivanova.myfavouritespets.model.Pelaje
import edu.andreaivanova.myfavouritespets.model.Pet
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class MyUtils {
    //obtengo todos los pets
    fun getPets(context: Context):MutableList<Pet>{
        var dbHelper= DBAdapter(context, null)
        return dbHelper.allPets()
    }
    // inserto pet
    fun savePet(context: Context, pet:Pet):Boolean{
        var dbHelper= DBAdapter(context, null)
        return dbHelper.addPet(pet)
    }
    // borro el registro según el id y la tabla pasada por parámetro
    fun delItem(context: Context, id:Int, tabla:String):Int{
        var dbHelper= DBAdapter(context, null)
        return dbHelper.deleteItem(id, tabla)
    }
    //actualizo pet
    fun updatePet(context:Context,id:Int, valores: MutableMap<String, String> ){
        var dbHelper= DBAdapter(context, null)
        return dbHelper.updatePet(id, valores)
    }
    // obtengo clases
    fun getClases(context: Context):MutableList<Clase>{
        var dbHelper= DBAdapter(context, null)
        return dbHelper.allClasses()
    }
    // inserto clase
    fun saveClase(context: Context, clase:Clase){
        var dbHelper= DBAdapter(context, null)
        dbHelper.addClase(clase)
    }
    // obtengo pelajes
    fun getPelaje(context: Context):MutableList<Pelaje>{
        var dbHelper= DBAdapter(context, null)
        return dbHelper.obtenerPelajes()
    }
    // inserto pelaje
    fun savePelaje(context: Context, pelaje:Pelaje){
        var dbHelper= DBAdapter(context, null)
        dbHelper.addPelaje(pelaje)
    }
     //crea el file y lo devuelve
    fun createImageFile(context: Context): File {
        // Se crea un timeStamp para el nombre del fichero.
        val timeStamp = SimpleDateFormat("yyyyMMdd").format(Date())

        // Se obtiene el directorio según el path creado utilizando el provider.
        val directoryStorage = context.getExternalFilesDir(
            Environment.DIRECTORY_PICTURES
        )
        return File.createTempFile("IMG_$timeStamp", ".jpg", directoryStorage)
    }

}