package edu.andreaivanova.mypointscounter.utils

import android.app.Activity
import android.content.Context
import android.content.Context.VIBRATOR_MANAGER_SERVICE
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import com.google.android.material.snackbar.Snackbar
import edu.andreaivanova.mypointscounter.R
import edu.andreaivanova.mypointscounter.adapters.MyPointsCounterDBAdapter
import edu.andreaivanova.mypointscounter.model.MyPoints
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter


class MyUtils {
    fun vibrate(context:Context){
       val vibrator:Vibrator
       //compruebo la versión, ya que a partir de la api 31 se usa vibratorManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                context.getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibrator = vibratorManager.defaultVibrator
        } else {

            vibrator = context.getSystemService(VIBRATOR_SERVICE) as Vibrator
        }
        //compruebo si el dispositivo tiene un vibrador y en caso afirmativo, lo hago vibrar
        if (!vibrator.hasVibrator()) {
            Toast.makeText(context, "No tienes vibrador!", Toast.LENGTH_SHORT).show()
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
    //función para llamar a la función que inserta nuevos registros en la BD
    fun savePointsDB(context:Context, datos:MyPoints) :Boolean{
        var dbHelper= MyPointsCounterDBAdapter(context, null)
        dbHelper.addPoints(datos)
        return true
    }
    //función para reinsertar los registros eliminados (con su id original)
    fun savePointsWithId(context:Context, datos:MyPoints) :Boolean{
        var dbHelper= MyPointsCounterDBAdapter(context, null)
        dbHelper.reAddPoints(datos)
        return true
    }
    //función para eliminar los registros
    fun deletePointDB(myPoint:MyPoints,context:Context) :Int{
        var dbHelper= MyPointsCounterDBAdapter(context, null)
        return dbHelper.delPoints(myPoint.id)
    }
    //función para leer los registros de la BD. Devuelve una lista
    fun getPoints(context:Context):MutableList<MyPoints>{
        var dbHelper= MyPointsCounterDBAdapter(context, null)
        return dbHelper.allPoints()
    }
    // función del ejercico anterior usada para escribir en el fichero.
    fun savePoints(context: Context, datos: MyPoints): Boolean {
        try {
            // Si el fichero no existe se crea,
            // si existe se añade la información

            val salida =OutputStreamWriter(
                 context.openFileOutput(context.getString(R.string.filename), Activity.MODE_APPEND)
            )
            salida.write("${datos.points};${datos.date};${datos.hour};\n")
            // Se confirma la escritura.
            salida.flush()
            salida.close()

            /*try{
                var texto=""
                val entrada = InputStreamReader(
                     context.openFileInput(context.getString(R.string.filename)))
                 val br = BufferedReader(entrada)
                 var linea = br.readLine()

                 while (!linea.isNullOrEmpty()) {
                    texto +=linea
                     linea = br.readLine()
                     }
                    Toast.makeText(context, texto, Toast.LENGTH_LONG).show()
                 br.close()
                 entrada.close()
             } catch (e: IOException) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
             }*/
            return true
        } catch (e: IOException)
        {
            return false
        }
    }

    //actualizo el marcador y cambio el color del texto, si procede
    fun updateMarcador(context:Context,marcador:TextView, contador:Int){
        marcador.text= contador.toString()

        if(contador <0){
            marcador.setTextColor(getColor(context,R.color.reset))
        }else{
            marcador.setTextColor(context.getColor(R.color.black))
        }
    }

}