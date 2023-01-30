package edu.andreaivanova.myfavouritespets.utils


import android.content.Context
import android.os.*
import android.util.Log
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
        val dbHelper= DBAdapter(context, null)
        return dbHelper.allPets()
    }
    // inserto pet
    fun savePet(context: Context, pet:Pet):Boolean{
        val dbHelper= DBAdapter(context, null)
        return dbHelper.addPet(pet)
    }
    // borro el registro según el id y la tabla pasada por parámetro
    fun delItem(context: Context, id:Int, tabla:String):Int{
        val dbHelper= DBAdapter(context, null)
        return dbHelper.deleteItem(id, tabla)
    }
    //actualizo pet
    fun updatePet(context:Context,id:Int, valores: MutableMap<String, String> ){
        val dbHelper= DBAdapter(context, null)
        return dbHelper.updatePet(id, valores)
    }
    // obtengo clases
    fun getClases(context: Context):MutableList<Clase>{
        val dbHelper= DBAdapter(context, null)
        return dbHelper.allClasses()
    }
    // inserto clase
    fun saveClase(context: Context, clase:Clase){
        val dbHelper= DBAdapter(context, null)
        dbHelper.addClase(clase)
    }
    // obtengo pelajes
    fun getPelaje(context: Context):MutableList<Pelaje>{
        val dbHelper= DBAdapter(context, null)
        return dbHelper.obtenerPelajes()
    }
    // inserto pelaje
    fun savePelaje(context: Context, pelaje:Pelaje){
        val dbHelper= DBAdapter(context, null)
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
    fun listFiles(context: Context): MutableList<File> {
        val tmp: MutableList<File> = ArrayList()

        val files = context.getExternalFilesDir(
            Environment.DIRECTORY_PICTURES
        )!!.absoluteFile.listFiles()

        if (files != null) {
            for (file in files) {
                if (file != null) {
                    Log.d("DIRECTORY", file.absolutePath)
                    tmp.add(file)
                }
            }
        }
        return tmp
    }
    fun getSimpleDialog(context:Context, title:Int, msg:Int){
        androidx.appcompat.app.AlertDialog.Builder(context).apply {
            // Se asigna un título.
            if(title != -1){
                setTitle(title)
            }
            setMessage(msg)
            setPositiveButton(android.R.string.ok, null)
        }.show()
    // Se muestra el AlertDialog.
    }
    fun vibrate(context:Context, vibrator :Vibrator){

        //compruebo si el dispositivo tiene un vibrador y en caso afirmativo, lo hago vibrar
        if (!vibrator.hasVibrator()) {
            // al lanzar otro toast desde la actividad, este no se ve
            //Toast.makeText(context, "No tienes vibrador!", Toast.LENGTH_SHORT).show()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val vibrationEffect =
                    VibrationEffect.createOneShot(
                        100,
                        VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.vibrate(vibrationEffect)
            } else {
                vibrator.vibrate(100) // Deprecated API 26
                // Toast.makeText(context, "Deprecated version", Toast.LENGTH_SHORT).show()
            }
        }
    }
}