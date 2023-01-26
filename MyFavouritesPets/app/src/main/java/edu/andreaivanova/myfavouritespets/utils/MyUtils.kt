package edu.andreaivanova.myfavouritespets.utils

import android.R
import android.app.AlertDialog
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
    fun getPets(context: Context):MutableList<Pet>{
        var dbHelper= DBAdapter(context, null)
        return dbHelper.allPets()
    }
    fun getClases(context: Context):MutableList<Clase>{
        var dbHelper= DBAdapter(context, null)
        return dbHelper.allClasses()
    }
    fun saveClase(context: Context, clase:Clase){
        var dbHelper= DBAdapter(context, null)
        dbHelper.addClase(clase)
    }
    fun getPelaje(context: Context):MutableList<Pelaje>{
        var dbHelper= DBAdapter(context, null)
        return dbHelper.obtenerPelajes()
    }
    fun createImageFile(context: Context): File {
        // Se crea un timeStamp para el nombre del fichero.
        val timeStamp = SimpleDateFormat("yyyyMMdd").format(Date())

        // Se obtiene el directorio según el path creado utilizando el provider.
        val directoryStorage = context.getExternalFilesDir(
            Environment.DIRECTORY_PICTURES
        )
        return File.createTempFile("IMG_$timeStamp", ".jpg", directoryStorage)
    }
    fun myAlertDialog(context:Context, lista:MutableList<Clase>){
        val builder = AlertDialog.Builder(context)
            builder.setTitle("Elige clase")
        var list=arrayOf(lista)
            builder.setItems(list){id, posicion ->
                toast.makeText(context)

            }


        }

    }
}